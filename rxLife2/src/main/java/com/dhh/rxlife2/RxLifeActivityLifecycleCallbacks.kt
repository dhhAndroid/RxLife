package com.dhh.rxlife2

import android.app.Activity
import android.app.Application
import android.arch.lifecycle.LifecycleOwner
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager

/**
 * Created by dhh on 2019/1/28.
 *
 * @author dhh
 */
internal class RxLifeActivityLifecycleCallbacks(private val initAction: (LifecycleOwner) -> Unit) : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is LifecycleOwner) initAction(activity)
        if (activity is FragmentActivity) {
            activity.supportFragmentManager
                    .registerFragmentLifecycleCallbacks(object : FragmentManager.FragmentLifecycleCallbacks() {
                        override fun onFragmentCreated(fm: FragmentManager, fragment: Fragment, savedInstanceState: Bundle?) {
                            initAction(fragment)
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
