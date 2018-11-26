package com.dhh.rxlife2

import android.app.Activity
import android.app.ActivityThread
import android.app.Application
import android.arch.lifecycle.GenericLifecycleObserver
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import java.util.*

/**
 * Created by dhh on 2018/7/11.
 *
 * @author dhh
 */
class RxLife private constructor(private val lifecycleOwner: LifecycleOwner) : GenericLifecycleObserver, LifecycleProvider {
    private val lifecycleSubject = BehaviorSubject.create<Lifecycle.Event>().toSerialized()
    /**
     * 是否是销毁状态
     */
    private val isDestroy
        get() = lifecycleOwner.lifecycle.currentState == Lifecycle.State.DESTROYED

    init {
        lifecycleOwner.lifecycle.addObserver(this)
        if (!isInitialized) ActivityThread.currentApplication()?.let { init(it) }
    }

    companion object {
        private var isInitialized = false

        /**
         * 初始化本框架
         * @param application Application
         */
        @Synchronized
        private fun init(application: Application) {
            if (isInitialized) return
            isInitialized = true
            application.registerActivityLifecycleCallbacks(object : EmptyActivityLifecycleCallbacks() {
                override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
                    if (activity is LifecycleOwner) with(activity)
                    if (activity is FragmentActivity) {
                        activity.supportFragmentManager
                                .registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                                    override fun onFragmentCreated(fm: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
                                        with(fragment)
                                    }
                                }, true)

                    }
                }
            })
        }

        private val rxLifeCacheMap = WeakHashMap<LifecycleOwner, RxLife>()

        private fun findRxLife(owner: LifecycleOwner) = rxLifeCacheMap.getOrPut(owner, { RxLife(owner) })

        @JvmStatic
        fun with(owner: LifecycleOwner): LifecycleProvider = findRxLife(owner)
    }


    override fun getLifecycle(): Observable<Lifecycle.Event> = lifecycleSubject.takeUntil { it == Lifecycle.Event.ON_DESTROY }

    override fun <T> bindToLifecycle() = LifecycleTransformer<T>(lifecycleSubject)

    override fun <T> bindUntilEvent(disposeEvent: Lifecycle.Event) = LifecycleTransformer<T>(lifecycleSubject, disposeEvent)

    /**
     * V4-support包下，lifecycle模块回调，support版本要高于 26.1+
     * @param source LifecycleOwner
     * @param event Lifecycle.Event
     */
    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        lifecycleSubject.onNext(event)
        if (isDestroy) release()
    }

    /**
     * 注销相关监听器
     */
    private fun release() {
        lifecycleSubject.onComplete()
        lifecycleOwner.lifecycle.removeObserver(this)
        rxLifeCacheMap.remove(lifecycleOwner)
    }

    private abstract class EmptyActivityLifecycleCallbacks : Application.ActivityLifecycleCallbacks {
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityResumed(activity: Activity) {}
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
        override fun onActivityDestroyed(activity: Activity) {}
    }
}