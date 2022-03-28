package com.fanrong.frwallet.ui

import android.content.Context
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.dialog_success.*
import xc.common.viewlib.BasicView
import xc.common.viewlib.view.customview.FullScreenDialog

class SuccessDialog(var msg:String, context: Context) : FullScreenDialog(context) {
    override fun getContentView(): Int {
        return R.layout.dialog_success
    }

    override fun initView() {
        BasicView.mainHandler.postDelayed(Runnable {
            dismiss()
        },2000)

        tv_msg.setText(msg)
    }
}