package com.fanrong.frwallet.ui.import

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.checkIsPrivateKey
import com.fanrong.frwallet.tools.checkPassword
import com.fanrong.frwallet.view.topDialogPasswordDes
import com.fanrong.frwallet.wallet.WalletHelper
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.import_wallet_privatekey_activity.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.checkIsEmpty
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.EditTextPasswordStyle
import xianchao.com.topmsg.TopWindowMsg


class ImportWalletPrivateKeyActivity : BaseActivity() {

    val topDialogPassword: TopWindowMsg.TopMsgDialog by lazy {
        topDialogPasswordDes(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.import_wallet_privatekey_activity
    }

    override fun initView() {

        ac_title.apply {
            extInitCommonBgAutoBack(this@ImportWalletPrivateKeyActivity, "导入${intent.getStringExtra(FrConstants.WALLET_TYPE)}钱包")
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_scan) {
                LibPremissionUtils.needCamera(this@ImportWalletPrivateKeyActivity, object : PermissonSuccess {
                    override fun hasSuccess() {
                        extStartActivityForResult(CaptureActivity::class.java, Bundle().apply {
                            putString(Constant.JUMP_TYPE, "ImportWalletPrivateKey")
                        }, 101) { i: Int, intent: Intent? ->
                            if (i == Activity.RESULT_OK) {
                                et_prikey.setText(intent?.getStringExtra(Constant.CODED_CONTENT) ?: "");
                            }
                        }
                    }
                })
            }
        }


//        LibViewUtils.updateBtnStatus(btn_create, et_prikey, et_password, et_repassword)


        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
            et_repassword.extShowOrHide(isChecked, trans)
        }
        cb_eye.isChecked = false

        et_password.onFocusChangeListener = object : View.OnFocusChangeListener {
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                tv_password_hint.extGoneOrVisible(hasFocus)
                if (hasFocus) {
                    topDialogPassword.show(this@ImportWalletPrivateKeyActivity)
                } else {
                    topDialogPassword.dismiss()
                }
            }
        }

        et_password.addTextChangedListener(object : TextWatcherAfter() {
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s.toString().checkIsEmpty()) {
                    tv_password_hint.setText("不少于 8 位字符，建议混合大小写字母")
                } else {
                    tv_password_hint.setText("${s?.length}字符")
                }
            }
        })

        btn_create.setOnClickListener {
            if (!et_prikey.text.toString().checkIsPrivateKey()
                || !et_password.text.toString().checkPassword()
            ) {
                return@setOnClickListener
            }
            if (!et_password.text.toString().equals(et_repassword.text.toString())) {
                showToast("两次密码输入不一致")
                return@setOnClickListener
            }


            WalletHelper.createPrivateKey(
                intent.getStringExtra(FrConstants.WALLET_TYPE),
                et_prikey.text.toString(),
                et_password.text.toString(),
                et_password_hide.text.toString()
            ) {
                if (it.success) {

                    extFinishWithAnim()
                    extStartActivity(MainActivity::class.java)
                } else {
                    showToast(it.error.toString())
                }
            }
        }
        tv_help_prikey.setOnClickListener {
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString("url", FrConstants.IMPORTPRIKEY_HELP)
            })
        }
    }

    override fun loadData() {
    }
}