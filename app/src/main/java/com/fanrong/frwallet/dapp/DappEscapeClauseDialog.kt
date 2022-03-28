package com.fanrong.frwallet.dapp

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.dapp_escape_clause_dialog.*
import xc.common.viewlib.view.customview.FullScreenDialog

class DappEscapeClauseDialog(context: Context,_cur_weburl:String) : FullScreenDialog(context) {
    override var contentGravity: Int? = Gravity.CENTER
    var isShow ="0"
    var cur_weburl = _cur_weburl
    override fun getContentView(): Int {
        return R.layout.dapp_escape_clause_dialog
    }

    override fun initView() {
        cb_checkbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked){
                isShow="1"
            }else{
                isShow="0"
            }
        }
        tv_canfirm.setOnClickListener {
            onConfrim?.confirm(null)
        }

        tv_cancel.setOnClickListener {
            onCancel?.cancel()
        }


        tv_tip.setText(context.resources.getString(R.string.dapp_tip,cur_weburl))
    }
}