package com.dhh.demo.rxlife1

import android.util.Log
import com.dhh.demo.mvp.PresenterDelegate
import com.dhh.rxlife1.bindonDestroy
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by dhh on 2018/11/26.
 *
 * @author dhh
 */
class RxLife1PresenterImpl : PresenterDelegate<RxLife1Contract.View>(), RxLife1Contract.Presenter {
    override fun start() {
        requestdata()
    }

    override fun requestdata() {
        Observable.just("我是模拟网络请求的数据" + Random().nextInt(100))
                .delay(5, TimeUnit.SECONDS, AndroidSchedulers.mainThread())

                .doOnSubscribe { Log.d("RxLife1PresenterImpl", "doOnSubscribe") }
                .doOnCompleted { Log.d("RxLife1PresenterImpl", "doOnCompleted") }
                .doOnUnsubscribe { Log.d("RxLife1PresenterImpl", "doOnUnsubscribe") }
                //P层使用用例
                .bindonDestroy(this)
                .subscribe { view.onSuccess(it) }
    }
}