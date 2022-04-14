package com.fanrong.frwallet.ui.receipt

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.found.extShowOrHide
import kotlinx.android.synthetic.main.transfer_password_dialog.*
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.view.EditTextPasswordStyle
import xc.common.viewlib.view.customview.FullScreenDialog

class TransferPasswordDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM

    override fun getContentView(): Int {
        return R.layout.transfer_password_dialog
    }

    override fun initView() {
        setCanceledOnTouchOutside(true)
        iv_close.setOnClickListener {
            dismiss()
        }
//        tv_forgat.setOnClickListener {
//            onCancel?.cancel()
//        }


        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
        }
        cb_eye.isChecked = false

        btn_confirm.setOnClickListener {
            val toString = et_password.text.toString()
            if (toString.checkIsEmpty()) {
                showToast("请输入密码")
                return@setOnClickListener
            }

            onConfrim?.confirm(toString)
//            dismiss()
        }
    }
}