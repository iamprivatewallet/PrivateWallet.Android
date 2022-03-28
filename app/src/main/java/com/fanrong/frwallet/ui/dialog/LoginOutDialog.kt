package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.logout_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class LoginOutDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM

    override fun getContentView(): Int {
        return R.layout.logout_dialog
    }

    override fun initView() {

        iv_close.setOnClickListener {
            dismiss()
        }

        btn_logout.setOnClickListener {
            dismiss()
            onConfrim?.confirm(null)
        }
    }
}