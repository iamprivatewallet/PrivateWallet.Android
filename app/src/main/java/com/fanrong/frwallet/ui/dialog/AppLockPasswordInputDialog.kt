package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.view.VerificationCodeView
import kotlinx.android.synthetic.main.dialog_applockpasswordinput.*
import xc.common.tool.utils.SPUtils
import xc.common.viewlib.view.customview.FullScreenDialog

class AppLockPasswordInputDialog(context: Context) : FullScreenDialog(context) {
    var password:String = ""
    override var contentGravity: Int? = Gravity.BOTTOM
    override fun getContentView(): Int {
        return R.layout.dialog_applockpasswordinput
    }

    override fun initView() {


        iv_close.setOnClickListener{
            dismiss()
        }

        vfcv.overnputListener = object : VerificationCodeView.IInputOverListener {
            override fun overListener(_value: MutableList<String>) {
                password = ""
                for (item in _value){
                    password+=item
                }

            }
        }

        tv_confirm.setOnClickListener{
            onConfrim?.confirm(password)
        }
    }
}