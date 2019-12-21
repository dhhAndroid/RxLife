package com.dhh.rxlife2

import com.dhh.rxlife2.internal.ObservableTakeUntil
import io.reactivex.*
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.Subject
import java.util.*
/**
 * Created by dhh on 2019/12/16.
 *
 * @author dhh
 */
interface DisposeScope : Disposable {
    companion object {
        private val cache = WeakHashMap<DisposeScope, Subject<Any>>()
    }

    private val trigger: Subject<Any>
        get() = cache.getOrPut(this) { BehaviorSubject.create<Any>().toSerialized() }

    override fun dispose() {
        trigger.onComplete()
    }

    override fun isDisposed() = trigger.hasComplete()

    fun <T> Observable<T>.bindOnDestroy(): Observable<T> = ObservableTakeUntil(this, trigger)
    fun <T> Flowable<T>.bindOnDestroy(): Flowable<T> = toObservable().bindOnDestroy().toFlowable(BackpressureStrategy.ERROR)
    fun <T> Maybe<T>.bindOnDestroy(): Maybe<T> = toObservable().bindOnDestroy().firstElement()
    fun <T> Single<T>.bindOnDestroy(): Single<T> = toObservable().bindOnDestroy().firstOrError()
    fun Completable.bindOnDestroy(): Completable = toObservable<Any>().bindOnDestroy().ignoreElements()

}
