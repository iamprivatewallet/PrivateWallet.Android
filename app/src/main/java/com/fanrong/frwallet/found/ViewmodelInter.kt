//package com.fanrong.frwallet.found
//
//import com.fanrong.frwallet.R
//import xc.common.framework.mvvm.BaseLiveData
//import xc.common.framework.mvvm.BaseViewModel
//
//interface ViewmodelInter<R : BaseLiveData, T : BaseViewModel<R>> {
//    var viewmodel: T
//    fun getViewModel(): T
//
//    fun init() {
//        viewmodel = getViewModel()
//        getViewModel().observerDataChange(null, this::onStateChange)
//    }
//
//    fun onStateChange(state: T){
//
//    }
//}