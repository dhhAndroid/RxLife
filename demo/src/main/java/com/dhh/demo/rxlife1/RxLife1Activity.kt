package com.dhh.demo.rxlife1

import androidx.lifecycle.Lifecycle
import android.util.Log
import android.view.View
import com.dhh.demo.R
import com.dhh.demo.mvp.BaseActivity
import com.dhh.rxlife1.LifecycleOwnerScope
import com.dhh.rxlife1.RxLife
import kotlinx.android.synthetic.main.activity_rx_life1.*
import rx.Observable
import rx.android.MainThreadSubscription
import rx.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit


class RxLife1Activity : BaseActivity<RxLife1PresenterImpl>(), RxLife1Contract.View,LifecycleOwnerScope {
    override val layoutID: Int
        get() = R.layout.activity_rx_life1

    override fun initView() {
        //监听界面生命周期变化
        RxLife.with(this)
                .getLifecycle()
                .subscribe { Log.d("RxLife1", it.name) }
        Observable.timer(10, TimeUnit.SECONDS)
                .doOnSubscribe { Log.d("RxLife1-onCreate", "doOnSubscribe") }
                //标准使用模式,自动在[Lifecycle.Event.ON_DESTROY]注销
                .compose(RxLife.with(this).bindToLifecycle())
                //通过kotlin扩展方法使用，推荐；自动在[Lifecycle.Event.ON_DESTROY]注销
                .bindToLifecycle()
                .doOnCompleted { Log.d("RxLife1-onCreate", "doOnCompleted") }
                .doOnUnsubscribe { Log.d("RxLife1-onCreate", "doOnUnsubscribe") }
                .subscribe { Log.d("RxLife1-onCreate", it.toString()) }



        Observable.timer(20, TimeUnit.SECONDS)
                .doOnSubscribe { Log.d("RxLife1-onCreate", "doOnSubscribe2") }
                //标准使用模式,自动在[Lifecycle.Event.ON_STOP]注销
                .compose(RxLife.with(this).bindUntilEvent(Lifecycle.Event.ON_STOP))
                //通过kotlin扩展方法使用，推荐；自动在[Lifecycle.Event.ON_STOP]注销
                .bindUntilEvent( Lifecycle.Event.ON_STOP)
                .doOnCompleted { Log.d("RxLife1-onCreate", "doOnCompleted2") }
                .doOnUnsubscribe { Log.d("RxLife1-onCreate", "doOnUnsubscribe2") }
                .subscribe { Log.d("RxLife1-onCreate", it.toString()) }



        btn_msg.rxClick().switchMap { Observable.interval(0, 1, TimeUnit.SECONDS, AndroidSchedulers.mainThread()) }
                .map { 10 - it }
                .doOnNext {
                    btn_msg.text = "${it}s后开始网络请求"
                }
                .first { it == 0L }
                .repeat()
                .bindToLifecycle()
                .subscribe { presenter.requestdata() }
        presenter.start()

    }

    override fun onResume() {
        super.onResume()
        Observable.timer(10, TimeUnit.SECONDS)
                .doOnSubscribe { Log.d("RxLife1-onResume", "doOnSubscribe") }
                //标准使用模式,自动在[Lifecycle.Event.ON_PAUSE]注销
                .compose(RxLife.with(this).bindToLifecycle())
                //通过kotlin扩展方法使用，推荐；自动在[Lifecycle.Event.ON_PAUSE]注销
                .bindToLifecycle()
                .doOnCompleted { Log.d("RxLife1-onResume", "doOnCompleted") }
                .doOnUnsubscribe { Log.d("RxLife1-onResume", "doOnUnsubscribe") }
                .subscribe { Log.d("RxLife1-onResume", it.toString()) }

        Observable.timer(10, TimeUnit.SECONDS)
                .doOnSubscribe { Log.d("RxLife1-onResume", "doOnSubscribe2") }
                //标准使用模式,自动在[Lifecycle.Event.ON_DESTROY]注销
                .compose(RxLife.with(this).bindOnDestroy())
                //通过kotlin扩展方法使用，推荐；自动在[Lifecycle.Event.ON_DESTROY]注销
                .bindOnDestroy()
                .doOnCompleted { Log.d("RxLife1-onResume", "doOnCompleted2") }
                .doOnUnsubscribe { Log.d("RxLife1-onResume", "doOnUnsubscribe2") }
                .subscribe { Log.d("RxLife1-onResume", it.toString()) }
    }

    override fun onSuccess(msg: String) {
        btn_msg.text = msg
    }
}

@Suppress("UNCHECKED_CAST")
fun <V : View> V.rxClick(): Observable<V> {
    return Observable.unsafeCreate<V> { subscriber ->
        setOnClickListener {
            if (!subscriber.isUnsubscribed) subscriber.onNext(it as V)
        }
        subscriber.add(object : MainThreadSubscription() {
            override fun onUnsubscribe() {
                setOnClickListener(null)
            }
        })
    }
}
