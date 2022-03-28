package com.fanrong.frwallet.ui.receipt

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.AddressDao
import kotlinx.android.synthetic.main.edit_receipt_amount_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class EditReceiptAmountDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM

    lateinit var addrInfo: AddressDao

    override fun getContentView(): Int {
        return R.layout.edit_receipt_amount_dialog
    }

    override fun initView() {
        tv_set_amount.setOnClickListener {
            dismiss()
            onConfrim?.confirm(null)

        }

        tv_cancel.setOnClickListener {
            dismiss()
        }

    }
}