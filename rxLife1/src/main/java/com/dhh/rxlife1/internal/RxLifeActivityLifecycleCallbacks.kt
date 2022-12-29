package com.dhh.rxlife1.internal

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LifecycleOwner
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.dhh.rxlife1.RxLife

/**
 * Created by dhh on 2019/1/28.
 *
 * @author dhh
 */
internal class RxLifeActivityLifecycleCallbacks internal constructor() : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is LifecycleOwner) RxLife.with(activity)
        if (activity is androidx.fragment.app.FragmentActivity) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(object : androidx.fragment.app.FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fm: androidx.fragment.app.FragmentManager, fragment: androidx.fragment.app.Fragment, savedInstanceState: Bundle?) {
                            RxLife.with(fragment)
                        }
                    }, true)

        }
    }

    override fun onActivityStarted(activity: Activity) {}
    override fun onActivityResumed(activity: Activity) {}
    override fun onActivityPaused(activity: Activity) {}
    override fun onActivityStopped(activity: Activity) {}
    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle?) {}
    override fun onActivityDestroyed(activity: Activity) {}
}
