package com.fanrong.frwallet.ui.login

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.FrConstants.USER_SERVICE
import kotlinx.android.synthetic.main.login_dialog_protocol.*
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils
import xc.common.viewlib.view.customview.FullScreenDialog

class ProtocolDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM
    override fun getContentView(): Int {
        return R.layout.login_dialog_protocol
    }

    override fun initView() {
        iv_close.setOnClickListener {
            dismiss()
        }
        iv_global.setOnClickListener {
            LibAppUtils.openBrowser(context, FrConstants.TERMS_OF_SERVICE)
        }

        webview.loadUrl(USER_SERVICE)

        btn_confirm.setOnClickListener {
            if (cb_check.isChecked) {
                onConfrim?.confirm(null)
            } else {
                showToast("请勾选阅读协议")
            }
        }
    }
}