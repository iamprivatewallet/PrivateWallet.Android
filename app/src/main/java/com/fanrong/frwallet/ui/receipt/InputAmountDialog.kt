package com.fanrong.frwallet.ui.receipt

import android.content.Context
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.input_amount_dialog.*
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

class InputAmountDialog(context: Context) : FullScreenDialog(context) {
    override fun getContentView(): Int {
        return R.layout.input_amount_dialog
    }

    override fun initView() {

        tv_cancel.setOnClickListener {
            dismiss()
            onCancel?.cancel()
        }


        tv_confirm.setOnClickListener {
            val toString = et_amount.text.toString()

            if (toString.checkIsEmpty()) {
                showToast("请输入金额")
                return@setOnClickListener
            }
            dismiss()
            onConfrim?.confirm(toString)
        }

    }
}