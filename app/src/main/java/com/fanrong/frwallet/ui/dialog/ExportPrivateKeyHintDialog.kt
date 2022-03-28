package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import kotlinx.android.synthetic.main.export_prikey_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class ExportPrivateKeyHintDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM
    lateinit var type :String
    override fun getContentView(): Int {
        return R.layout.export_prikey_dialog
    }

    override fun initView() {
        tv_copy_tittle.setText("复制 ${type}")
        tv_hint_body.setText("复制 ${type} 存在风险，剪切板容易被第三方应用监控或滥用;建议使用二维码形式，直接扫码进行钱包转移")
        btn_nocopy.setOnClickListener {
            dismiss()
//            onCancel?.cancel()
        }

        btn_copy.setOnClickListener {
            dismiss()
            onConfrim?.confirm(null)
        }
    }
}