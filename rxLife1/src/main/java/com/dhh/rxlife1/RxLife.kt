package com.dhh.rxlife1

import android.app.ActivityThread
import android.arch.lifecycle.LifecycleOwner

/**
 * Created by dhh on 2018/11/22.
 *
 * @author dhh
 */
object RxLife {
    init {
        ActivityThread.currentApplication().registerActivityLifecycleCallbacks(RxLifeActivityLifecycleCallbacks())
    }

    @JvmStatic
    fun with(owner: LifecycleOwner): LifecycleProvider = LifecycleBridge.get(owner)
}