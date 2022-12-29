package com.dhh.rxlife2

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import android.view.View
import io.reactivex.*

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
    fun <T> Flowable<T>.bindToLifecycle() = compose(RxLife.with(getLifecycleOwner()).bindToLifecycle())

    /**
     * 指定在[event]生命周期事件注销
     */
    fun <T> Flowable<T>.bindUntilEvent(event: Lifecycle.Event) = compose(RxLife.with(getLifecycleOwner()).bindUntilEvent(event))

    /**
     * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
     */
    fun <T> Flowable<T>.bindOnDestroy() = compose(RxLife.with(getLifecycleOwner()).bindOnDestroy())


//------------------------------------

    /**
     * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
     */
    fun <T> Maybe<T>.bindToLifecycle() = compose(RxLife.with(getLifecycleOwner()).bindToLifecycle())

    /**
     * 指定在[event]生命周期事件注销
     */
    fun <T> Maybe<T>.bindUntilEvent(event: Lifecycle.Event) = compose(RxLife.with(getLifecycleOwner()).bindUntilEvent(event))

    /**
     * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
     */
    fun <T> Maybe<T>.bindOnDestroy() = compose(RxLife.with(getLifecycleOwner()).bindOnDestroy())


//------------------------------------

    /**
     * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
     */
    fun <T> Single<T>.bindToLifecycle() = compose(RxLife.with(getLifecycleOwner()).bindToLifecycle())

    /**
     * 指定在[event]生命周期事件注销
     */
    fun <T> Single<T>.bindUntilEvent(event: Lifecycle.Event) = compose(RxLife.with(getLifecycleOwner()).bindUntilEvent(event))

    /**
     * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
     */
    fun <T> Single<T>.bindOnDestroy() = compose(RxLife.with(getLifecycleOwner()).bindOnDestroy())


//------------------------------------

    /**
     * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
     */
    fun Completable.bindToLifecycle() = compose(RxLife.with(getLifecycleOwner()).bindToLifecycle<Any>())

    /**
     * 指定在[event]生命周期事件注销
     */
    fun Completable.bindUntilEvent(event: Lifecycle.Event) = compose(RxLife.with(getLifecycleOwner()).bindUntilEvent<Any>(event))

    /**
     * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
     */
    fun Completable.bindOnDestroy() = compose(RxLife.with(getLifecycleOwner()).bindOnDestroy<Any>())

}