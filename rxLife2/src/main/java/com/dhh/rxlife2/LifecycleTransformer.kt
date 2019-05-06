package com.dhh.rxlife2

import android.arch.lifecycle.Lifecycle
import io.reactivex.*
import io.reactivex.functions.BiFunction

/**
 * Created by dhh on 2018/7/13.
 *
 * @author dhh
 */
class LifecycleTransformer<T> internal constructor(
        private val lifecycleObservable: Observable<Lifecycle.Event>,
        private val disposeEvent: Lifecycle.Event = Lifecycle.Event.ON_ANY
) :
        ObservableTransformer<T, T>,
        FlowableTransformer<T, T>,
        MaybeTransformer<T, T>,
        SingleTransformer<T, T>,
        CompletableTransformer {

    override fun apply(upstream: Observable<T>): ObservableSource<T> {
        return upstream.takeUntil(getTakeUntilFlowable().toObservable())
    }

    override fun apply(upstream: Flowable<T>): Flowable<T> {
        return upstream.takeUntil(getTakeUntilFlowable())
    }

    override fun apply(upstream: Maybe<T>): MaybeSource<T> {
        return upstream.takeUntil(getTakeUntilFlowable())
    }

    override fun apply(upstream: Single<T>): SingleSource<T> {
        return upstream.takeUntil(getTakeUntilFlowable())
    }

    override fun apply(upstream: Completable): CompletableSource {
        return upstream.takeUntil(getTakeUntilFlowable().ignoreElements())
    }

    private fun getTakeUntilFlowable(): Flowable<*> {
        return when (disposeEvent) {
            Lifecycle.Event.ON_ANY -> {
                Observable.combineLatest(
                        lifecycleObservable.take(1).map(convertLifecycle),
                        lifecycleObservable.skip(1), compareFunction
                )
                        .filter { it }
                        .take(1)
            }
            else -> lifecycleObservable.filter { it == disposeEvent }.take(1)

        }.toFlowable(BackpressureStrategy.LATEST)
    }

    private val compareFunction = BiFunction<Lifecycle.Event, Lifecycle.Event, Boolean> { disposeEvent, lifecycleEvent -> disposeEvent == lifecycleEvent }

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