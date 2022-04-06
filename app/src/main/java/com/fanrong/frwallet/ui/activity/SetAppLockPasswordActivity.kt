package com.fanrong.frwallet.ui.activity

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.view.VerificationCodeView
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_lockapp.*
import kotlinx.android.synthetic.main.activity_set_applockpassword.*
import kotlinx.android.synthetic.main.activity_set_applockpassword.vfcv

import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.utils.SPUtils

class SetAppLockPasswordActivity : BaseActivity() {

    var password:String = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_set_applockpassword
    }

    override fun initView() {
        wim_title.apply {
            setTitleText(getString(R.string.yys))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        vfcv.overnputListener = object : VerificationCodeView.IInputOverListener {
            override fun overListener(_value: MutableList<String>) {
                password = ""
                for (item in _value){
                    password+=item
                }

            }
        }

        btn_confirm.setOnClickListener{
            SPUtils.saveValue(FrConstants.APPLOCKPASSWORD,password)

            showTopToast(this@SetAppLockPasswordActivity,getString(R.string.xgcg),true)

            extFinishWithAnim()
        }
    }

    override fun loadData() {

    }
}