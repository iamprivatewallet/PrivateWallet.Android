package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.import_wallet_words_activity.*
import kotlinx.android.synthetic.main.password_dialog.*
import kotlinx.android.synthetic.main.password_dialog.cb_eye
import kotlinx.android.synthetic.main.password_dialog.et_password
import xc.common.kotlinext.showToast
import xc.common.viewlib.view.EditTextPasswordStyle
import xc.common.viewlib.view.customview.FullScreenDialog

class PasswordDialog(context: Context,isWarn:String) : FullScreenDialog(context) {
    override var contentGravity: Int? = Gravity.BOTTOM
    lateinit var walletInfo: WalletDao
     var isw: String=isWarn   //-1表示不在这里进行验证,在回调中进行验证
    override fun getContentView(): Int {
        return R.layout.password_dialog
    }
    override fun initView() {
//        val tv_warn = findViewById<TextView>(R.id.tv_warn)
//        if (isw=="1"){
//            tv_warn.visibility=View.GONE
//        }
        iv_close.setOnClickListener {
            dismiss()
            onCancel?.cancel()
        }

        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
        }
        cb_eye.isChecked = false

        tv_confirm.setOnClickListener {
            val toString = et_password.text.toString()
            if (isw != "-1"){
                if (!toString.equals(walletInfo.password)) {
                    showTopToast(context,context.getString(R.string.mmcw),false)
                    return@setOnClickListener
                }
            }
            dismiss()
            onConfrim?.confirm(toString)
        }

    }
}