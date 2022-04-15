package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.dialog_setapproveamount.*
import xc.common.viewlib.view.customview.FullScreenDialog

class SetApproveAmountDialog(context: Context) : FullScreenDialog(context) {
    override var contentGravity: Int? = Gravity.BOTTOM
    override fun getContentView(): Int {
        return R.layout.dialog_setapproveamount
    }

    override fun initView() {

        tv_confirm.setOnClickListener{
            onConfrim?.confirm(et_amount.text.toString())
        }
    }
}