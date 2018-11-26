package com.dhh.demo.mvp

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.lang.reflect.ParameterizedType

/**
 * Created by dhh on 2018/11/26.
 *
 * @author dhh
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<P : BasePresenter> : Fragment(), BaseView {

    protected val TAG = javaClass.simpleName
    protected val presenter: P by lazy { initPresenter() }
    /**
     * 界面id
     */
    @get:LayoutRes
    protected abstract val layoutID: Int

    private fun initPresenter(): P {
        val clazz = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as Class<P>
        return clazz.newInstance()
    }

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (presenter as ContractPresenter<BaseView>).attachView(this, context!!, this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(layoutID, container, false)
        return view
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initView()
    }

    abstract fun initView()
}