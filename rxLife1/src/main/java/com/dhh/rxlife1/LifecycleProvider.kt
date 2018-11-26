package com.dhh.rxlife1

import android.arch.lifecycle.Lifecycle
import rx.Observable

/**
 * Created by dhh on 2018/11/22.
 *
 * @author dhh
 */
interface LifecycleProvider {
    /**
     * 获取生命周期回调
     * @return Observable<Lifecycle.Event>
     */
    fun getLifecycle(): Observable<Lifecycle.Event>

    /**
     * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
     * @return LifecycleTransformer<T>
     */
    fun <T> bindToLifecycle(): LifecycleTransformer<T>

    /**
     * 指定在[disposeEvent]生命周期事件注销
     * @param disposeEvent Lifecycle.Event
     * @return LifecycleTransformer<T>
     */
    fun <T> bindUntilEvent(disposeEvent: Lifecycle.Event): LifecycleTransformer<T>

    /**
     * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
     * @return LifecycleTransformer<T>
     */
    fun <T> bindOnDestroy() = bindUntilEvent<T>(Lifecycle.Event.ON_DESTROY)
}