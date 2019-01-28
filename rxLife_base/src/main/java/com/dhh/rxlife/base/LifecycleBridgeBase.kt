package com.dhh.rxlife.base

import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner

/**
 * Created by dhh on 2019/1/28.
 * 将[LifecycleOwner]转换成 RxJava 的基础桥梁
 * @author dhh
 */
abstract class LifecycleBridgeBase(val lifecycleOwner: LifecycleOwner) : GenericLifecycleObserver {

    /**
     * 是否是销毁状态
     */
    private val isDestroy
        get() = lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED

    init {
        lifecycleOwner.lifecycle.addObserver(this)
    }


    /**
     * V4-support包下，lifecycle模块回调，support版本要高于 26.1+
     * @param source LifecycleOwner
     * @param event Lifecycle.Event
     */
    override final fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        onStateChanged(event)
        if (isDestroy) release()
    }

    abstract fun onStateChanged(event: Lifecycle.Event)

    /**
     * 注销相关监听器
     */
    private fun release() {
        onRelease()
        lifecycleOwner.lifecycle.removeObserver(this)
    }

    abstract fun onRelease()
}