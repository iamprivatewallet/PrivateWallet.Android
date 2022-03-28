package com.fanrong.frwallet.ui.importwallet

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
import com.fanrong.frwallet.tools.checkIsWords
import com.fanrong.frwallet.tools.checkPassword
import com.fanrong.frwallet.tools.checkTwoPasswordIsSame
import com.fanrong.frwallet.ui.SuccessDialog
import com.fanrong.frwallet.ui.activity.AddCoinsActivity
import com.fanrong.frwallet.ui.login.LoginActivity
import com.fanrong.frwallet.view.topDialogPasswordDes
import com.fanrong.frwallet.wallet.WalletHelper
import kotlinx.android.synthetic.main.import_wallet_activity.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.AppManager
import xc.common.tool.utils.LibViewUtils
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.EditTextPasswordStyle
import xianchao.com.topmsg.TopWindowMsg


class ImportWalletActivity : BaseActivity() {


    val topDialogPasswordDes: TopWindowMsg.TopMsgDialog by lazy {
        topDialogPasswordDes(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.import_wallet_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@ImportWalletActivity, "")
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_helpblack) {
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putString(DappBrowserActivity.PARAMS_URL, FrConstants.RECOVER_ID_HELPER)
                })
            }
        }


        et_password.onFocusChangeListener = object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                tv_password_hint.extGoneOrVisible(hasFocus)

                if (hasFocus) {
                    topDialogPasswordDes.show(this@ImportWalletActivity)
                } else {
                    topDialogPasswordDes.dismiss()
                }
            }

        }
        et_password.addTextChangedListener(object : TextWatcherAfter(){
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
            extShowOrDismissDialog(true)
            if ((!et_words.text.toString().checkIsWords()
                        || !et_password.text.toString().checkPassword()
                        || !et_password.text.toString().checkTwoPasswordIsSame(et_repassword.text.toString()))
            ) {
                extShowOrDismissDialog(false)
                return@setOnClickListener
            }

            WalletHelper.initMainWallet(
                et_words.text.toString(),
                "",
                et_password.text.toString(),
                et_password_hide.text.toString(),
                callback = { success: Boolean ->
                    extShowOrDismissDialog(false)
                    if (success) {
                        SuccessDialog("导入成功", this).apply {
                            setOnDismissListener {
                                extStartActivityForResult(
                                    AddCoinsActivity::class.java,
                                    Bundle().apply {
                                        putString(FrConstants.PRE_PAGE, FrConstants.PRE_PAGE_CREATE)
                                    }, 101
                                ) { resultCode: Int, data: Intent? ->
                                    if (resultCode == Activity.RESULT_OK) {
                                        extStartActivity(MainActivity::class.java)
                                        AppManager.getAppManager().findActivity(LoginActivity::class.java)
                                        extFinishWithAnim()
                                    }
                                }
                            }
                        }.show()
                    }
                })

        }
//        tv_ex_prikey.setOnClickListener {
//            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
//                putString(DappBrowserActivity.PARAMS_URL, FrConstants.EXPORT_PRIVATEKEY_HELPER)
//            })
//        }
        LibViewUtils.updateBtnStatus(btn_create, et_words, et_password, et_repassword)

        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
            et_repassword.extShowOrHide(isChecked, trans)
        }
        cb_eye.isChecked = false
    }

    override fun loadData() {
    }

}