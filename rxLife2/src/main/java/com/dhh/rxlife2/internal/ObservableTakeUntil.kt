package com.dhh.rxlife2.internal

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper
import io.reactivex.internal.util.AtomicThrowable
import io.reactivex.internal.util.HalfSerializer
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicReference

internal class ObservableTakeUntil<T, U>(val source: ObservableSource<T>, val other: ObservableSource<out U>) : Observable<T>() {

        public override fun subscribeActual(child: Observer<in T>) {
            val parent = TakeUntilMainObserver<T, U>(child)
            child.onSubscribe(parent)
            other.subscribe(parent.otherObserver)
            source.subscribe(parent)
        }

        internal class TakeUntilMainObserver<T, U>(val downstream: Observer<in T>) : AtomicReference<Disposable>(), Observer<T>, Disposable {
            val wip = AtomicInteger()
            val otherObserver: OtherObserver = OtherObserver()
            val error: AtomicThrowable = AtomicThrowable()

            override fun dispose() {
                DisposableHelper.dispose(this)
                DisposableHelper.dispose(otherObserver)
            }

            override fun isDisposed() = DisposableHelper.isDisposed(get())

            override fun onSubscribe(d: Disposable) {
                DisposableHelper.setOnce(this, d)
            }

            override fun onNext(t: T) {
                HalfSerializer.onNext(downstream, t, wip, error)
            }

            override fun onError(e: Throwable) {
                DisposableHelper.dispose(otherObserver)
                HalfSerializer.onError(downstream, e, wip, error)
            }

            override fun onComplete() {
                DisposableHelper.dispose(otherObserver)
                HalfSerializer.onComplete(downstream, wip, error)
            }

            fun otherError(e: Throwable) {
                DisposableHelper.dispose(this)
                HalfSerializer.onError(downstream, e, wip, error)
            }

            fun otherComplete() {
                DisposableHelper.dispose(this)
            }

            inner class OtherObserver : AtomicReference<Disposable>(), Observer<U> {

                override fun onSubscribe(d: Disposable) {
                    DisposableHelper.setOnce(this, d)
                }

                override fun onNext(t: U) {
                    DisposableHelper.dispose(this)
                    otherComplete()
                }

                override fun onError(e: Throwable) {
                    otherError(e)
                }

                override fun onComplete() {
                    otherComplete()
                }


            }
        }
    }
