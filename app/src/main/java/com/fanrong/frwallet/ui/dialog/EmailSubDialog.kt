package com.fanrong.frwallet.ui.dialog

import android.content.Context
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.email_sub_dialog.*
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkIsEmail
import xc.common.viewlib.view.customview.FullScreenDialog

class EmailSubDialog(context: Context) : FullScreenDialog(context) {
    override fun getContentView(): Int {
        return R.layout.email_sub_dialog
    }

    override fun initView() {
        tv_cancel.setOnClickListener {
            dismiss()
            onCancel?.cancel()
        }


        tv_confirm.setOnClickListener {
            val toString = et_email.text.toString()

            if (!toString.checkIsEmail()) {
                showToast("请输入正确的邮箱")
                return@setOnClickListener
            }
            dismiss()
            onConfrim?.confirm(toString)
        }

    }
}