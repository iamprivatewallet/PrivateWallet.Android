package com.fanrong.frwallet.ui.backup

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.backup_hint_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class DontCutScreenDialog(context: Context) : FullScreenDialog(context) {

    var iKnow: (() -> Unit)? = null

    override var contentGravity: Int? = Gravity.BOTTOM
    override fun getContentView(): Int {
        return R.layout.backup_hint_dialog
    }

    override fun initView() {
        iv_close.setOnClickListener {
            dismiss()
        }

        btn_know.setOnClickListener {
            dismiss()
            iKnow?.invoke()
        }
    }

}