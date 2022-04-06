package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.dialog_addnode.*
import xc.common.kotlinext.showToast
import xc.common.viewlib.view.customview.FullScreenDialog

class AddNodeDialog(context: Context) : FullScreenDialog(context) {
    override var contentGravity: Int? = Gravity.BOTTOM
    override fun getContentView(): Int {
        return R.layout.dialog_addnode
    }

    override fun initView() {

        iv_close.setOnClickListener {
            dismiss()
            onCancel?.cancel()
        }


        tv_confirm.setOnClickListener {
            dismiss()
            onConfrim?.confirm(et_nodeurl.text.toString())
        }
    }
}