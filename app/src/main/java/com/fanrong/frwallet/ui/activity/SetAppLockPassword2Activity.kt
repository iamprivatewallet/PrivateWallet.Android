package com.fanrong.frwallet.ui.activity

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.view.VerificationCodeView
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_set_applockpassword.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.utils.SPUtils

class SetAppLockPassword2Activity: BaseActivity() {
    var password:String = ""
    var inputPasswordOne = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_set_applockpassword
    }

    override fun initView() {
        inputPasswordOne = intent.getStringExtra(FrConstants.APPLOCKPASSWORD)
        if (inputPasswordOne == null || inputPasswordOne == "" || inputPasswordOne.length != 6){
            extFinishWithAnim()
        }
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
            if (inputPasswordOne.equals(password)){
                SPUtils.saveValue(FrConstants.APPLOCKPASSWORD,password)

                showTopToast(this@SetAppLockPassword2Activity,getString(R.string.xgcg),true)

                extFinishWithAnim()
            }else{
                showTopToast(this,getString(R.string.lcmmbyz),false)
            }

        }

        btn_confirm.setText(getString(R.string.confirm))
        tv_tip.setText(getString(R.string.lockappmm_input_again))
    }

    override fun loadData() {

    }
}