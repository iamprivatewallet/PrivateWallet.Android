package com.fanrong.frwallet.dapp

import android.content.Context
import android.net.Uri
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.dapp_impower_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class DappImpowerDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM

    lateinit var url: String

    override fun getContentView(): Int {
        return R.layout.dapp_impower_dialog
    }

    override fun initView() {

        val parse = Uri.parse(url)
        tv_host.setText(parse.host)
        tv_msg.setText(context.getString(R.string.dapp_impower_msg).format(parse.host))

        tv_cancel.setOnClickListener {
            onCancel?.cancel()
        }

        tv_confirm.setOnClickListener {
            onConfrim?.confirm(null)
        }

    }

}