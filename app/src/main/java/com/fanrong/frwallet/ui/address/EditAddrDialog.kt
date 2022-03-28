package com.fanrong.frwallet.ui.address

import android.content.Context
import android.os.Bundle
import android.view.Gravity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.AddressDao
import kotlinx.android.synthetic.main.edit_addr_dialog.*
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils
import xc.common.viewlib.view.customview.FullScreenDialog

class EditAddrDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM

    lateinit var addrInfo: AddressDao

    override fun getContentView(): Int {
        return R.layout.edit_addr_dialog
    }

    override fun initView() {
        tv_copy_addr.setOnClickListener {
            dismiss()
            LibAppUtils.copyText(addrInfo.address!!)
            showToast("地址已复制")

        }
        tv_edit.setOnClickListener {
            dismiss()
            ownerActivity!!.extStartActivity(AddAddressActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.ADDR_INFO, addrInfo)
            })
        }

        tv_cancel.setOnClickListener {
            dismiss()
        }

    }
}