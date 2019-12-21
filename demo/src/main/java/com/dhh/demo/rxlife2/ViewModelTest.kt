package com.dhh.demo.rxlife2

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by dhh on 2019/12/16.
 *
 * @author dhh
 */
class ViewModelTest : BaseViewModel() {
    val userName = MutableLiveData<String>()

    init {
        loadData()
    }

    fun loadData() {
        Observable.timer(5, TimeUnit.SECONDS)
                .map { "dhh" }
                .doOnSubscribe { Log.d("VVV", "doOnSubscribe") }
                .doOnDispose { Log.d("VVV", "doOnDispose") }
                .commit { userName.value = it }
    }

}

