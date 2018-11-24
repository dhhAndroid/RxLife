package com.dhh.demo

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import rx.Observable
import java.util.concurrent.TimeUnit

class RxLife1Activity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rx_life1)
        Observable.timer(10, TimeUnit.SECONDS)
                .doOnSubscribe { Log.d("MainActivity-onCreate", "doOnSubscribe") }
                .doOnCompleted { Log.d("MainActivity-onCrate", "doOnComplete")  }

    }
}
