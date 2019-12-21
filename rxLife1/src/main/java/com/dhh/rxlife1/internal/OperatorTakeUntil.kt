package com.dhh.rxlife1.internal

import rx.Observable
import rx.Subscriber
import rx.observers.SerializedSubscriber

internal class OperatorTakeUntil<T, E>(private val other: Observable<out E>) : Observable.Operator<T, T> {
        override fun call(child: Subscriber<in T>): Subscriber<in T> {
            val serial: Subscriber<T> = SerializedSubscriber(child, false)
            val main: Subscriber<T> = object : Subscriber<T>(serial, false) {
                override fun onNext(t: T) {
                    serial.onNext(t)
                }

                override fun onError(e: Throwable) {
                    try {
                        serial.onError(e)
                    } finally {
                        serial.unsubscribe()
                    }
                }

                override fun onCompleted() {
                    serial.unsubscribe()
                }
            }
            val so: Subscriber<E> = object : Subscriber<E>() {
                override fun onStart() {
                    request(Long.MAX_VALUE)
                }

                override fun onCompleted() {
                    main.onCompleted()
                }

                override fun onError(e: Throwable) {
                    main.onError(e)
                }

                override fun onNext(t: E) {
                    onCompleted()
                }
            }
            serial.add(main)
            serial.add(so)
            child.add(serial)
            other.unsafeSubscribe(so)
            return main
        }

    }
