package com.fanrong.frwallet.ui.dialog

import android.content.Context
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.receipt_backups_hint_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class ReceiptBackupsHintDialog(context: Context) : FullScreenDialog(context) {


    override fun getContentView(): Int {
        return R.layout.receipt_backups_hint_dialog
    }

    override fun initView() {

        tv_cancel.setOnClickListener {
            dismiss()
            onCancel?.cancel()
        }

        btn_backups.setOnClickListener {

            dismiss()
            onConfrim?.confirm(null)
        }

    }
}