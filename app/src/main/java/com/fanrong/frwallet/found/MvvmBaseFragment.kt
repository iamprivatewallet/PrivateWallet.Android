package com.fanrong.frwallet.found

import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel
import xc.common.framework.ui.base.BaseFragment

abstract class MvvmBaseFragment<T : BaseLiveData, R : BaseViewModel<T>> : BaseFragment() {


    val viewmodel: R by lazy {
        getViewModel()
    }

    abstract fun getViewModel(): R


    override fun initView() {
        viewmodel.observerDataChange(this, this::stateChange)
    }

    abstract fun stateChange(state: T)
}