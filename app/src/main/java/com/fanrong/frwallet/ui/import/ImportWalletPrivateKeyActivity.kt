package com.fanrong.frwallet.ui.import

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.checkIsPrivateKey
import com.fanrong.frwallet.tools.checkPassword
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.CommonButton
import com.fanrong.frwallet.view.showTopToast
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
import xc.common.tool.utils.checkNotEmpty
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.import_wallet_privatekey_activity
    }

    override fun initView() {

        ac_title.apply {
            extInitCommonBgAutoBack(this@ImportWalletPrivateKeyActivity, getString(R.string.sydr))
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_scan) {
                LibPremissionUtils.needCamera(this@ImportWalletPrivateKeyActivity, object : PermissonSuccess {
                    override fun hasSuccess() {
                        extStartActivityForResult(CaptureActivity::class.java, Bundle().apply {
                            putString(Constant.JUMP_TYPE, "ImportWalletPrivateKey")
                        }, 101) { i: Int, intent: Intent? ->
                            if (i == Activity.RESULT_OK) {
//                                et_prikey.setText(intent?.getStringExtra(Constant.CODED_CONTENT) ?: "");
                                set_sy.et_content.setText(intent?.getStringExtra(Constant.CODED_CONTENT) ?: "")
                            }
                        }
                    }
                })
            }
        }


//        LibViewUtils.updateBtnStatus(btn_create, et_prikey, et_password, et_repassword)


//        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
//            val trans = EditTextPasswordStyle()
//            et_password.extShowOrHide(isChecked, trans)
//            et_repassword.extShowOrHide(isChecked, trans)
//        }
//        cb_eye.isChecked = false

//        et_password.onFocusChangeListener = object : View.OnFocusChangeListener {
//            override fun onFocusChange(v: View?, hasFocus: Boolean) {
//                tv_password_hint.extGoneOrVisible(hasFocus)
//                if (hasFocus) {
//                    topDialogPassword.show(this@ImportWalletPrivateKeyActivity)
//                } else {
//                    topDialogPassword.dismiss()
//                }
//            }
//        }

//        et_password.addTextChangedListener(object : TextWatcherAfter() {
//            override fun afterTextChanged(s: Editable?) {
//                super.afterTextChanged(s)
//                if (s.toString().checkIsEmpty()) {
//                    tv_password_hint.setText("????????? 8 ???????????????????????????????????????")
//                } else {
//                    tv_password_hint.setText("${s?.length}??????")
//                }
//            }
//        })

        cb_save.setClickListener(object : CommonButton.ClickListener {
            override fun clickListener() {
                if (!set_sy.et_content.text.toString().checkIsPrivateKey(this@ImportWalletPrivateKeyActivity)
                    || !set_mm.et_content_1.text.toString().checkPassword(this@ImportWalletPrivateKeyActivity)
                ) {
                    return
                }
                if (!set_mm.et_content_1.text.toString().equals(set_mm.et_content_2.text.toString())) {
                    showTopToast(this@ImportWalletPrivateKeyActivity,getString(R.string.lcmmbyz),false)
                    return
                }


                WalletHelper.createPrivateKey(
                    intent.getStringExtra(FrConstants.WALLET_TYPE),
                    set_sy.et_content.text.toString(),
                    set_mm.et_content_1.text.toString(),
                    ""
                ) {
                    if (it.success) {

                        extFinishWithAnim()
                        extStartActivity(MainActivity::class.java)
                    } else {
                        showTopToast(this@ImportWalletPrivateKeyActivity,it.error.toString(),false)
                    }
                }
            }
        })

//        tv_help_prikey.setOnClickListener {
//            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
//                putString("url", FrConstants.IMPORTPRIKEY_HELP)
//            })
//        }

        set_sy.et_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateBtnState()
            }
        })
        set_name.et_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateBtnState()
            }
        })
        set_mm.et_content_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateBtnState()
            }
        })
        set_mm.et_content_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateBtnState()
            }
        })
    }

    private fun updateBtnState() {
        if (set_sy.et_content.text.toString().checkNotEmpty() &&
            set_mm.et_content_1.text.toString().checkNotEmpty() &&
            set_mm.et_content_2.text.toString().checkNotEmpty()
        ) {
            cb_save.setEnableState(true)
        } else {
            cb_save.setEnableState(false)
        }
    }

    override fun loadData() {
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}