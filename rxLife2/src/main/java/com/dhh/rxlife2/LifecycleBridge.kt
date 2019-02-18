package com.dhh.rxlife2

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * Created by dhh on 2019/1/28.
 *
 * @author dhh
 */
internal class LifecycleBridge(lifecycleOwner: LifecycleOwner) : LifecycleBridgeBase(lifecycleOwner), LifecycleProvider {
    private val lifecycleSubject = BehaviorSubject.create<Lifecycle.Event>().toSerialized()

    companion object {
        private val cacheMap = WeakHashMap<LifecycleOwner, LifecycleBridge>()
        fun get(owner: LifecycleOwner) = cacheMap.getOrPut(owner) { LifecycleBridge(owner) }
    }

    override fun getLifecycle(): Observable<Lifecycle.Event> = lifecycleSubject.takeUntil { it == Lifecycle.Event.ON_DESTROY }

    override fun <T> bindToLifecycle() = LifecycleTransformer<T>(lifecycleSubject)

    override fun <T> bindUntilEvent(disposeEvent: Lifecycle.Event) = LifecycleTransformer<T>(lifecycleSubject, disposeEvent)

    override fun onStateChanged(event: Lifecycle.Event) {
        lifecycleSubject.onNext(event)
    }

    override fun onRelease() {
        lifecycleSubject.onComplete()
        cacheMap.remove(lifecycleOwner)
    }
}