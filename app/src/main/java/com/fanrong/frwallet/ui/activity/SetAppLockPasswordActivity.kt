package com.fanrong.frwallet.ui.activity

import android.os.Bundle
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.view.VerificationCodeView
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_lockapp.*
import kotlinx.android.synthetic.main.activity_set_applockpassword.*
import kotlinx.android.synthetic.main.activity_set_applockpassword.vfcv

import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
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
            if (password!=null&&password!=""&&password.length == 6){
                extStartActivity(SetAppLockPassword2Activity::class.java, Bundle().apply {
                    putString(FrConstants.APPLOCKPASSWORD, password)
                })
                extFinishWithAnim()
            }else
            {
                showTopToast(this,getString(R.string.mmcw),false)
            }

        }
    }

    override fun loadData() {

    }
}