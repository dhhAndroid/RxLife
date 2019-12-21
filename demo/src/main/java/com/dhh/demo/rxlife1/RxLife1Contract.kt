package com.dhh.demo.rxlife1

import com.dhh.demo.mvp.BasePresenter
import com.dhh.demo.mvp.BaseView
import com.dhh.rxlife1.LifecycleOwnerScope

/**
 * Created by dhh on 2018/11/26.
 *
 * @author dhh
 */
interface RxLife1Contract {
    interface View : BaseView {
        fun onSuccess(msg: String)
    }

    interface Presenter : BasePresenter ,LifecycleOwnerScope{
        fun requestdata()

    }

}