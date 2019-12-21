package com.dhh.demo.rxlife2

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dhh.demo.R
import com.dhh.demo.rxlife1.RxLife1Activity
import com.dhh.rxlife2.LifecycleOwnerScope
import com.dhh.rxlife2.RxLife
import io.reactivex.Single
import kotlinx.android.synthetic.main.activity_rxlife2.*
import java.util.concurrent.TimeUnit

class RxLife2Activity : AppCompatActivity(), LifecycleOwnerScope {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val vvv = ViewModelProviders.of(this).get(ViewModelTest::class.java)
        setContentView(R.layout.activity_rxlife2)
        btn_startRxLife1.setOnClickListener { startActivity(Intent(this, RxLife1Activity::class.java)) }
        Single.timer(10, TimeUnit.SECONDS)
                .doOnSubscribe { Log.d("RxLife2-onCreate", "doOnSubscribe") }
                .doOnDispose { Log.d("RxLife2-onCreate", "doOnDispose") }
                //标准使用模式,自动在[Lifecycle.Event.ON_DESTROY]注销
                .compose(RxLife.with(this).bindToLifecycle())
                //指定在[Lifecycle.Event.ON_DESTROY]注销
                .compose(RxLife.with(this).bindOnDestroy())
                //指定在某一生命周期注销，不常用
                .compose(RxLife.with(this).bindUntilEvent(Lifecycle.Event.ON_DESTROY))
                .doOnSuccess { Log.d("RxLife2-onCreate", "doOnComplete") }
                .subscribe { it -> Log.d("RxLife2-onCreate", it.toString()) }
        Single.timer(1, TimeUnit.SECONDS)
                .toObservable()
                .bindOnDestroy()
                .subscribe {

                }

//        Observable.timer(10, TimeUnit.SECONDS)
//                .doOnSubscribe { Log.d("RxLife2-onCreate", "doOnSubscribe2") }
//                .doOnDispose { Log.d("RxLife2-onCreate", "doOnDispose2") }
//                //通过kotlin扩展方法使用，推荐；自动在[Lifecycle.Event.ON_STOP]注销
//                .bindToLifecycle(this)
//                //指定在[Lifecycle.Event.ON_DESTROY]注销
//                .bindOnDestroy(this)
//                //指定在某一生命周期注销，不常用
//                .bindUntilEvent(this, Lifecycle.Event.ON_DESTROY)
//                .doOnComplete { Log.d("RxLife2-onCreate", "doOnComplete2") }
//                .subscribe { Log.d("RxLife2-onCreate", it.toString()) }
    }

//    override fun onResume() {
//        super.onResume()
//        Observable.timer(10, TimeUnit.SECONDS)
//                .doOnSubscribe { Log.d("RxLife2-onResume", "doOnSubscribe") }
//                .doOnDispose { Log.d("RxLife2-onResume", "doOnDispose") }
//                //标准使用模式,自动在[Lifecycle.Event.ON_PAUSE]注销
//                .compose(RxLife.with(this).bindToLifecycle())
//                //通过kotlin扩展方法使用，推荐；自动在[Lifecycle.Event.ON_PAUSE]注销
//                .bindToLifecycle(this)
//                .doOnComplete { Log.d("RxLife2-onResume", "doOnComplete") }
//                .subscribe { Log.d("RxLife2-onResume", it.toString()) }
//
//
//        Observable.timer(10, TimeUnit.SECONDS)
//                .doOnSubscribe { Log.d("RxLife2-onResume", "doOnSubscribe2") }
//                .doOnDispose { Log.d("RxLife2-onResume", "doOnDispose2") }
//                //标准使用模式,自动在[Lifecycle.Event.ON_DESTROY]注销
//                .compose(RxLife.with(this).bindOnDestroy())
//                //通过kotlin扩展方法使用，推荐；自动在[Lifecycle.Event.ON_DESTROY]注销
//                .bindOnDestroy(this)
//                .doOnComplete { Log.d("RxLife2-onResume", "doOnComplete2") }
//                .subscribe { Log.d("RxLife2-onResume", it.toString()) }
//    }
}
