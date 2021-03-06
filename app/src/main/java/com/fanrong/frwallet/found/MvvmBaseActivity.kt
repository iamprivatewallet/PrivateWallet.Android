package com.fanrong.frwallet.found

import android.os.Bundle
import android.view.WindowManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel

abstract class MvvmBaseActivity<T : BaseLiveData, R : BaseViewModel<T>> : BaseActivity() {


    val viewmodel: R by lazy {
        getViewModel()
    }

    abstract fun getViewModel(): R

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        viewmodel.observerDataChange(this, this::stateChange)
    }

    override fun initView() {
    }

    abstract fun stateChange(state: T)

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}