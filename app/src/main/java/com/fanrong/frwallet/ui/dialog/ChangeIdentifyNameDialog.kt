package com.fanrong.frwallet.ui.dialog

import android.content.Context
import com.fanrong.frwallet.R
import com.fanrong.frwallet.tools.checkWalletName
import kotlinx.android.synthetic.main.identify_name_dialog.*
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

class ChangeIdentifyNameDialog(context: Context) : FullScreenDialog(context) {
    override fun getContentView(): Int {
        return R.layout.identify_name_dialog
    }

    override fun initView() {
        tv_cancel.setOnClickListener {
            dismiss()
            onCancel?.cancel()
        }


        tv_confirm.setOnClickListener {
            val toString = et_name.text.toString()
            if (toString.checkWalletName()) {
                onConfrim?.confirm(toString)
                dismiss()
            }
        }

    }
}