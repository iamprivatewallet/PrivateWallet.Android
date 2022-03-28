package com.fanrong.frwallet.ui.walletmanager

import android.graphics.Color
import android.text.Editable
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extShowOrHide
import kotlinx.android.synthetic.main.password_hint_activity.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.listener.TextWatcherAfter
import xc.common.viewlib.view.EditTextPasswordStyle


class PasswordHintActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.password_hint_activity
    }

    override fun initView() {

        var wallet = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao

        ac_title.apply {
            extInitCommonBgAutoBack(this@PasswordHintActivity, "密码提示")
            setRightTextClickListener("完成") {
                wallet.apply {
                    passwordHint = et_password_hide.text.toString()
                    WalletOperator.update(this)
                    EventBus.getDefault().post(WalletInfoChangeEvent())
                }
                extFinishWithAnim()
            }
        }


        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password_hide.extShowOrHide(isChecked, trans)
        }
        et_password_hide.addTextChangedListener(object : TextWatcherAfter() {
            override fun tryAfterTextChanged(s: Editable?) {
                if (et_password_hide.text.toString().equals(wallet.passwordHint)) {
                    ac_title.rightTextView.setTextColor(Color.parseColor("#888888"))
                    ac_title.rightTextView.isClickable = false
                } else {

                    ac_title.rightTextView.setTextColor(Color.parseColor("#000000"))
                    ac_title.rightTextView.isClickable = true
                }
            }
        })


        cb_eye.isChecked = false
        et_password_hide.setText(WalletOperator.queryWallet(wallet).passwordHint)

    }

    override fun loadData() {
    }
}