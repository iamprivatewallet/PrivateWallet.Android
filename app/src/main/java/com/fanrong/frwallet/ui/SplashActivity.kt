package com.fanrong.frwallet.ui

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.ui.activity.FingerPrintVerifyActivity
import com.fanrong.frwallet.ui.login.LoginActivity
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.SPUtils
import xc.common.tool.utils.checkIsEmpty
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonFailure
import xc.common.utils.PermissonSuccess
import xc.common.viewlib.BasicView


class SplashActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.splash_activity
    }

    override fun initView() {


        if (SPUtils.getString(FrConstants.LUA_SETTING).checkIsEmpty()) {
            SPUtils.saveValue(FrConstants.LUA_SETTING, "简体中文")
        }
        if (SPUtils.getString(FrConstants.UNIT_SETTING).checkIsEmpty()) {
            SPUtils.saveValue(FrConstants.UNIT_SETTING, "CNY")
        }

        LibPremissionUtils.needStore(this, success = object : PermissonSuccess {
            override fun hasSuccess() {
                toNextPage()
            }
        }, failure = object : PermissonFailure {
            override fun failure() {
                showToast("请开启相应权限")
            }

        })


    }

    private fun toNextPage() {

        BasicView.mainHandler.postDelayed(Runnable {
            when {
                WalletOperator.queryMainWallet().checkIsEmpty() -> {
                    extStartActivity(LoginActivity::class.java)
                }
                SPUtils.getBoolean(FrConstants.FINGER_SETTING) -> {
                    extStartActivity(FingerPrintVerifyActivity::class.java)
                }
                else -> {
                    extStartActivity(MainActivity::class.java)
                }
            }
            extFinishWithAnim()
        }, 1500)

    }

    override fun loadData() {
    }

    override fun onRestart() {
        super.onRestart()
        initView()
    }
}