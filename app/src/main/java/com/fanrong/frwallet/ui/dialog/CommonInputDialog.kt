package com.fanrong.frwallet.ui.dialog

import android.content.Context
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.common_input_dialog.*
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

class CommonInputDialog(context: Context) : FullScreenDialog(context) {

    var title = ""
    var hint = ""

    override fun getContentView(): Int {
        return R.layout.common_input_dialog
    }

    override fun initView() {

        tv_cancel.setOnClickListener {
            dismiss()
            onCancel?.cancel()
        }

        tv_confirm.setOnClickListener {
            val toString = et_input.text.toString()
            if (toString.checkIsEmpty()) {
                showToast("请输入内容")
                return@setOnClickListener
            }

            dismiss()
            onConfrim?.confirm(toString)
        }


        tv_title.setText(title)
        et_input.hint = hint

    }
}