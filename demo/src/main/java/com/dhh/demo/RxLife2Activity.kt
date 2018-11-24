package com.dhh.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.dhh.rxlife2.RxLife
import com.dhh.rxlife2.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class RxLife2Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxlife2)
        Observable.timer(10, TimeUnit.SECONDS)
                .doOnSubscribe { Log.d("RxLife2-onCreate", "doOnSubscribe") }
                .doOnDispose { Log.d("RxLife2-onCreate", "doOnDispose") }
                //标准使用模式
                .compose(RxLife.with(this).bindToLifecycle())
                //通过kotlin扩展方法使用，推荐
                .bindToLifecycle(this)
                .doOnComplete { Log.d("RxLife2-onCrate", "doOnComplete") }
                .subscribe { }
    }

    override fun onResume() {
        super.onResume()
        Observable.timer(10, TimeUnit.SECONDS)
                .doOnSubscribe { Log.d("RxLife2-onResume", "doOnSubscribe") }
                .doOnDispose { Log.d("RxLife2-onResume", "doOnDispose") }
                //标准使用模式
                .compose(RxLife.with(this).bindToLifecycle())
                //通过kotlin扩展方法使用，推荐
                .bindToLifecycle(this)
                .doOnComplete { Log.d("RxLife2-onResume", "doOnComplete") }
                .subscribe { }
    }
}

fun String.toast() {
    AndroidSchedulers.mainThread().scheduleDirect { }
}