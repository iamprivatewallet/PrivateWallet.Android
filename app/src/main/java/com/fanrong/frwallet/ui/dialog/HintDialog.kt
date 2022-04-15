package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.dialog_hint.*
import xc.common.viewlib.view.customview.FullScreenDialog

class HintDialog(context: Context) : FullScreenDialog(context) {
    override var contentGravity: Int? = Gravity.BOTTOM
    override fun getContentView(): Int {
        return R.layout.dialog_hint
    }

    override fun initView() {

        iv_close.setOnClickListener{
            dismiss()
        }

        tv_cancel.setOnClickListener{
            onCancel?.cancel()
        }

        tv_confirm.setOnClickListener{
            onConfrim?.confirm(null)
        }

    }
}