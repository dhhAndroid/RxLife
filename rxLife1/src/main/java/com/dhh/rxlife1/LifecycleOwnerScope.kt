package com.dhh.rxlife1

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.view.View
import rx.Completable
import rx.Observable
import rx.Single
import rx.Subscription

/**
 * Created by dhh on 2019/12/16.
 *
 * @author dhh
 */
interface LifecycleOwnerScope {

    fun getLifecycleOwner(): LifecycleOwner {
        return when (this) {
            is LifecycleOwner -> this
            is View -> context as LifecycleOwner
            else -> throw RuntimeException("${javaClass.simpleName}:Unable to get LifecycleOwner, please override the getLifecycleOwner method!")
        }
    }

    /**
     * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
     */
    fun <T> Observable<T>.bindToLifecycle() = compose(RxLife.with(getLifecycleOwner()).bindToLifecycle())

    /**
     * 指定在[event]生命周期事件注销
     */
    fun <T> Observable<T>.bindUntilEvent(event: Lifecycle.Event) = compose(RxLife.with(getLifecycleOwner()).bindUntilEvent(event))

    /**
     * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
     */
    fun <T> Observable<T>.bindOnDestroy() = compose(RxLife.with(getLifecycleOwner()).bindOnDestroy())


//------------------------------------

    /**
     * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
     */
    fun <T> Single<T>.bindToLifecycle() = toObservable().bindToLifecycle().toSingle()

    /**
     * 指定在[event]生命周期事件注销
     */
    fun <T> Single<T>.bindUntilEvent(event: Lifecycle.Event) = toObservable().bindUntilEvent(event).toSingle()

    /**
     * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
     */
    fun <T> Single<T>.bindOnDestroy() = toObservable().bindOnDestroy().toSingle()


//------------------------------------

    /**
     * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
     */
    fun Completable.bindToLifecycle() = toObservable<Any>().bindToLifecycle().toCompletable()

    /**
     * 指定在[event]生命周期事件注销
     */
    fun Completable.bindUntilEvent(event: Lifecycle.Event) = toObservable<Any>().bindUntilEvent(event).toCompletable()

    /**
     * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
     */
    fun Completable.bindOnDestroy() = toObservable<Any>().bindOnDestroy().toCompletable()

}