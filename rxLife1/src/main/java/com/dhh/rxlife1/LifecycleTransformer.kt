package com.dhh.rxlife1

import android.arch.lifecycle.Lifecycle
import rx.Observable

/**
 * Created by dhh on 2018/11/22.
 *
 * @author dhh
 */
class LifecycleTransformer<T> internal constructor(
        private val lifecycleObservable: Observable<Lifecycle.Event>,
        private val disposeEvent: Lifecycle.Event = Lifecycle.Event.ON_ANY
) : Observable.Transformer<T, T> {

    override fun call(upstream: Observable<T>): Observable<T> {
        return upstream.takeUntil(getTakeUntilObservable())
    }


    private fun getTakeUntilObservable(): Observable<*> {
        return when (disposeEvent) {
            Lifecycle.Event.ON_ANY -> {
                Observable.combineLatest(
                        lifecycleObservable.take(1).map(convertLifecycle),
                        lifecycleObservable.skip(1), compareFunction
                )
                        .first { it }
            }
            else -> lifecycleObservable.first { it == disposeEvent }

        }
    }

    private val compareFunction: (Lifecycle.Event, Lifecycle.Event) -> Boolean = { disposeEvent, lifecycleEvent -> disposeEvent == lifecycleEvent }

    /**
     * 界面生命周期转化，输入是当[Observable]订阅时的生命周期，转化成对应的注销生命周期，
     * 转化出来的生命周期，相当于外边传入的[disposeEvent]
     */
    private val convertLifecycle: (Lifecycle.Event) -> Lifecycle.Event = { event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> Lifecycle.Event.ON_DESTROY
            Lifecycle.Event.ON_START -> Lifecycle.Event.ON_STOP
            Lifecycle.Event.ON_RESUME -> Lifecycle.Event.ON_PAUSE
            Lifecycle.Event.ON_PAUSE -> Lifecycle.Event.ON_STOP
            Lifecycle.Event.ON_STOP, Lifecycle.Event.ON_DESTROY, Lifecycle.Event.ON_ANY -> Lifecycle.Event.ON_DESTROY
        }
    }
}