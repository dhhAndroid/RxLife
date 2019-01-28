package com.dhh.rxlife1

import android.arch.lifecycle.LifecycleOwner
import com.dhh.rxlife.base.RxLifeInitHelper

/**
 * Created by dhh on 2018/11/22.
 *
 * @author dhh
 */
object RxLife {
    init {
        RxLifeInitHelper.init { with(it) }
    }

    @JvmStatic
    fun with(owner: LifecycleOwner): LifecycleProvider = LifecycleBridge.get(owner)
}