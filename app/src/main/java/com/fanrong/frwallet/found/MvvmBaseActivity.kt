package com.fanrong.frwallet.found

import android.os.Bundle
import com.basiclib.base.BaseActivity
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel

abstract class MvvmBaseActivity<T : BaseLiveData, R : BaseViewModel<T>> : BaseActivity() {


    val viewmodel: R by lazy {
        getViewModel()
    }

    abstract fun getViewModel(): R

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewmodel.observerDataChange(this, this::stateChange)
    }

    override fun initView() {
    }

    abstract fun stateChange(state: T)
}