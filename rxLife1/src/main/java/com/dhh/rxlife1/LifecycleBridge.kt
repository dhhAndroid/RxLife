package com.dhh.rxlife1

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.OnLifecycleEvent
import rx.Observable
import rx.subjects.BehaviorSubject
import rx.subjects.Subject
import java.util.*

/**
 * Created by dhh on 2019/1/28.
 *
 * 将[LifecycleOwner]转换成 [LifecycleProvider] 的桥梁
 * @author dhh
 */
internal class LifecycleBridge(private val lifecycleOwner: LifecycleOwner) : LifecycleObserver, LifecycleProvider {

    companion object {
        private val cacheMap = WeakHashMap<LifecycleOwner, LifecycleBridge>()
        fun get(owner: LifecycleOwner) = cacheMap.getOrPut(owner) { LifecycleBridge(owner) }
    }

    private val lifecycleSubject: Subject<Lifecycle.Event, Lifecycle.Event> = BehaviorSubject.create<Lifecycle.Event>().toSerialized()

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }

    /**
     * 是否是销毁状态
     */
    private val isDestroy
        get() = lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED

    /**
     * V4-support包下，lifecycle模块回调，support版本要高于 26.1+
     * @param source LifecycleOwner
     * @param event Lifecycle.Event
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        lifecycleSubject.onNext(event)
        if (isDestroy) release()
    }

    /**
     * 注销相关监听器
     */
    private fun release() {
        lifecycleSubject.onCompleted()
        cacheMap.remove(lifecycleOwner)
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    override fun getLifecycle(): Observable<Lifecycle.Event> = lifecycleSubject.takeUntil { it == Lifecycle.Event.ON_DESTROY }

    override fun <T> bindToLifecycle() = LifecycleTransformer<T>(lifecycleSubject)

    override fun <T> bindUntilEvent(disposeEvent: Lifecycle.Event) = LifecycleTransformer<T>(lifecycleSubject, disposeEvent)

}