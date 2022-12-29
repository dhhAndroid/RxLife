package com.dhh.demo.mvp

import android.os.Bundle
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import java.lang.reflect.ParameterizedType

/**
 * Created by dhh on 2018/11/26.
 *
 * @author dhh
 */
@Suppress("UNCHECKED_CAST")
abstract class BaseActivity<P : BasePresenter> : AppCompatActivity(), BaseView {

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
        setContentView(layoutID)
        (presenter as ContractPresenter<BaseView>).attachView(this, this, this)
        initView()
    }

    /**
     * 设置UI，在[onCreate]中调用
     */
    abstract fun initView()
}