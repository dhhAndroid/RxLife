package com.dhh.rxlife1

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import rx.Completable
import rx.Observable
import rx.Single

/**
 * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
 */
fun <T> Observable<T>.bindToLifecycle(owner: LifecycleOwner) = compose(RxLife.with(owner).bindToLifecycle())

/**
 * 指定在[event]生命周期事件注销
 */
fun <T> Observable<T>.bindUntilEvent(owner: LifecycleOwner, event: Lifecycle.Event) = compose(RxLife.with(owner).bindUntilEvent(event))

/**
 * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
 */
fun <T> Observable<T>.bindonDestroy(owner: LifecycleOwner) = compose(RxLife.with(owner).bindOnDestroy())


//------------------------------------

/**
 * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
 */
fun <T> Single<T>.bindToLifecycle(owner: LifecycleOwner) = toObservable().compose(RxLife.with(owner).bindToLifecycle()).toSingle()

/**
 * 指定在[event]生命周期事件注销
 */
fun <T> Single<T>.bindUntilEvent(owner: LifecycleOwner, event: Lifecycle.Event) = toObservable().compose(RxLife.with(owner).bindUntilEvent(event)).toSingle()

/**
 * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
 */
fun <T> Single<T>.bindonDestroy(owner: LifecycleOwner) = bindUntilEvent<T>(owner,Lifecycle.Event.ON_DESTROY)


//------------------------------------

/**
 * 自动绑定生命周期,请查看：[LifecycleTransformer.convertLifecycle]生命周期转化函数。
 */
fun <T> Completable.bindToLifecycle(owner: LifecycleOwner) = toObservable<T>().compose(RxLife.with(owner).bindToLifecycle()).toCompletable()

/**
 * 指定在[event]生命周期事件注销
 */
fun <T> Completable.bindUntilEvent(owner: LifecycleOwner, event: Lifecycle.Event) = toObservable<T>().compose(RxLife.with(owner).bindUntilEvent(event)).toCompletable()

/**
 * 在[Lifecycle.Event.ON_DESTROY],即界面销毁的时候注销
 */
fun <T> Completable.bindonDestroy(owner: LifecycleOwner) = bindUntilEvent<T>(owner,Lifecycle.Event.ON_DESTROY)
