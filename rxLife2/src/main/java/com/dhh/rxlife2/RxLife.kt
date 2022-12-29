package com.dhh.rxlife2

import android.app.ActivityThread
import androidx.lifecycle.LifecycleOwner
import com.dhh.rxlife2.internal.RxLifeActivityLifecycleCallbacks

/**
 * Created by dhh on 2018/7/11.
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