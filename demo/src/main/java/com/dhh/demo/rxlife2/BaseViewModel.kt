package com.dhh.demo.rxlife2

import android.arch.lifecycle.ViewModel

/**
 * Created by dhh on 2019/12/16.
 *
 * @author dhh
 */
open class BaseViewModel : ViewModel(), CustomDisposeScope {
    override fun onCleared() {
        dispose()
    }
}

