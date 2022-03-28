package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.logout_backups_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class LoginOutBackupsHintDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM

    override fun getContentView(): Int {
        return R.layout.logout_backups_dialog
    }

    override fun initView() {

        btn_cancle.setOnClickListener {
            dismiss()
        }

        btn_backups.setOnClickListener {
            dismiss()
            onConfrim?.confirm(null)
        }
    }
}