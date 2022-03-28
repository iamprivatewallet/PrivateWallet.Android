package com.fanrong.frwallet.ui.receipt

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.transfer_risk_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class TransferRiskDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM

    override fun getContentView(): Int {
        return R.layout.transfer_risk_dialog
    }

    override fun initView() {
        setCanceledOnTouchOutside(false)
        iv_closedilog.setOnClickListener {
            dismiss()
        }
//        tv_see.setOnClickListener {
//            // 常见骗局
//            onCancel?.cancel()
//            dismiss()
//        }

        btn_confirm.setOnClickListener {
            onConfrim?.confirm(null)
            dismiss()
        }
    }
}