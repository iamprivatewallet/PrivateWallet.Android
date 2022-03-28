package com.fanrong.frwallet.ui.dialog

import android.content.Context
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.general_hint_dialog.*
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

class GeneralHintDialog(context: Context) : FullScreenDialog(context) {

    var content = ""
    var cancelText = ""
    var confirmText = ""

    override fun getContentView(): Int {
        return R.layout.general_hint_dialog
    }

    override fun initView() {

        tv_cancel.setOnClickListener {
            dismiss()
            onCancel?.cancel()
        }

        tv_confirm.setOnClickListener {

            dismiss()
            onConfrim?.confirm(null)
        }
        tv_content.text = content
        if (cancelText.checkNotEmpty()){
            tv_cancel.text=cancelText
        }
        if (confirmText.checkNotEmpty()){
            tv_confirm.text=confirmText
        }
    }
}