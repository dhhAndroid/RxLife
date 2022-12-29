package com.dhh.demo.mvp

import androidx.lifecycle.LifecycleOwner
import android.content.Context

/**
 * Created by dhh on 2018/11/26.
 *
 * 本文件主要展示RxLife在MVP中怎么使用，其实就是在P层中提供获取[LifecycleOwner]的方法即可
 *
 * @author dhh
 */
interface Contract

interface BaseView

interface BasePresenter {
    /**
     * P层开始工作
     */
    fun start()
}

/**
 * V-P层关系桥梁,不对外暴露
 */
internal interface ContractPresenter<V : BaseView> : BasePresenter {
    /**
     * 绑定[view]层
     * @param view V
     * @param context Context
     * @param owner LifecycleOwner
     */
    fun attachView(view: V, context: Context, owner: LifecycleOwner)

}

/**
 * 一个代理实现，业务P层需要继承[PresenterDelegate]
 * @param V : BaseView
 * @property view V
 * @property context Context
 * @property owner LifecycleOwner
 */
abstract class PresenterDelegate<V : BaseView> : ContractPresenter<V> {
    protected lateinit var view: V
    protected lateinit var context: Context
    protected lateinit var owner: LifecycleOwner

    override fun attachView(view: V, context: Context, owner: LifecycleOwner) {
        this.view = view
        this.context = context
        this.owner = owner
    }
}
