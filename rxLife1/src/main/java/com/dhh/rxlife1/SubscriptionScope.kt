package com.dhh.rxlife1

import rx.Completable
import rx.Observable
import rx.Single
import rx.Subscription
import rx.internal.operators.OperatorTakeUntil
import rx.subjects.BehaviorSubject
import java.util.*

/**
 * Created by dhh on 2019/12/16.
 *
 * @author dhh
 */
interface SubscriptionScope : Subscription {
    companion object {
        private val cache = WeakHashMap<SubscriptionScope, BehaviorSubject<Any>>()
    }

    private val trigger: BehaviorSubject<Any>
        get() = cache.getOrPut(this) { BehaviorSubject.create() }


    override fun isUnsubscribed()=trigger.hasCompleted()

    override fun unsubscribe() {
        trigger.onCompleted()
    }

    fun <T> Observable<T>.bindOnDestroy(): Observable<T> = lift(OperatorTakeUntil(trigger))
    fun <T> Single<T>.bindOnDestroy(): Single<T> = toObservable().bindOnDestroy().toSingle()
    fun Completable.bindOnDestroy(): Completable = toObservable<Any>().bindOnDestroy().toCompletable()

}
