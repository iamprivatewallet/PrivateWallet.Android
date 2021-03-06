package com.fanrong.frwallet.ui.import

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.checkIsWords
import com.fanrong.frwallet.tools.checkPassword
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.CommonButton
import com.fanrong.frwallet.view.showTopToast
import com.fanrong.frwallet.view.topDialogPasswordDes
import com.fanrong.frwallet.wallet.WalletHelper
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.import_wallet_words_activity.*
import org.consenlabs.tokencore.wallet.WalletManager
import org.consenlabs.tokencore.wallet.model.BIP44Util
import org.consenlabs.tokencore.wallet.model.ChainType
import org.consenlabs.tokencore.wallet.model.Metadata
import org.consenlabs.tokencore.wallet.model.Network
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkNotEmpty
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
import xianchao.com.topmsg.TopWindowMsg


class ImportWalletWordsActivity : BaseActivity() {

    var isZDY:Boolean = false

    val passwordDes: TopWindowMsg.TopMsgDialog by lazy {
        topDialogPasswordDes(this)
    }

    val chainType: String by lazy {
        intent.getStringExtra(FrConstants.WALLET_TYPE)
    }

    override fun getLayoutId(): Int {
        return R.layout.import_wallet_words_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@ImportWalletWordsActivity, getString(R.string.zjcdr))
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_scan) {
                LibPremissionUtils.needCamera(this@ImportWalletWordsActivity, object : PermissonSuccess {
                    override fun hasSuccess() {
                        extStartActivityForResult(CaptureActivity::class.java, Bundle().apply {
                            putString(Constant.JUMP_TYPE, "ImportWalletWords")
                        }, 101) { i: Int, intent: Intent? ->
                            if (i == Activity.RESULT_OK) {
//                                et_words.setText(intent?.getStringExtra(Constant.CODED_CONTENT) ?: "");
                                set_zjc.et_content.setText(intent?.getStringExtra(Constant.CODED_CONTENT) ?: "")
                            }
                        }
                    }
                })
            }
        }


//        LibViewUtils.updateBtnStatus(btn_create, et_words, et_password, et_repassword)

//        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
//            val trans = EditTextPasswordStyle()
//            et_password.extShowOrHide(isChecked, trans)
//            et_repassword.extShowOrHide(isChecked, trans)
//        }
//        cb_eye.isChecked = false
//
//        et_password.onFocusChangeListener = object : View.OnFocusChangeListener {
//            override fun onFocusChange(v: View?, hasFocus: Boolean) {
//                tv_password_hint.extGoneOrVisible(hasFocus)
//                if (hasFocus) {
//                    passwordDes.show(this@ImportWalletWordsActivity)
//                } else {
//                    passwordDes.dismiss()
//                }
//            }
//
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

        tv_gjms.setOnClickListener{
            if (ll_wallet_way.visibility == View.GONE)
                ll_wallet_way.visibility = View.VISIBLE
            else
                ll_wallet_way.visibility = View.GONE
        }
        rl_mr.setOnClickListener{
            et_way.setText(getString(R.string.mr_way))
            ll_popup.visibility = View.GONE
            isZDY = false
        }
        rl_ledger.setOnClickListener{
            et_way.setText(getString(R.string.ledger_way))
            ll_popup.visibility = View.GONE
            isZDY = false
        }
        rl_zdy.setOnClickListener{
            et_way.setText(getString(R.string.zdylj_way))
            et_way.setFocusableInTouchMode(true);//????????????
            ll_popup.visibility = View.GONE
            isZDY = true
        }
        et_way.setOnClickListener{
            if (isZDY){

            }else{
                ll_popup.visibility = View.VISIBLE
            }
        }
        ll_popup.setOnClickListener{
            ll_popup.visibility = View.GONE
        }
        ll_wallet_way.setOnClickListener{
            ll_popup.visibility = View.VISIBLE
        }
        et_way.setFocusableInTouchMode(false);//????????????

        cb_save.setClickListener(object : CommonButton.ClickListener {
            override fun clickListener() {
                if (!set_zjc.et_content.text.toString().checkIsWords(this@ImportWalletWordsActivity)
                    || !set_mm.et_content_1.text.toString().checkPassword(this@ImportWalletWordsActivity)
                ) {
                    return
                }

                if (!set_mm.et_content_1.text.toString().equals(set_mm.et_content_2.text.toString())) {
                    showTopToast(this@ImportWalletWordsActivity,getString(R.string.lcmmbyz),false)
                    return
                }

                var path = et_way.text.toString()
                if (path.equals("")){
                    path = "m/44???/60???/0???/0/0"
                }
                WalletHelper.createFromWords(
                    chainType, set_zjc.et_content.text.toString(),path, set_mm.et_content_1.text.toString(), set_mm.et_content_2.text.toString()
                ) {
                    if (it.success) {
                        showTopToast(this@ImportWalletWordsActivity,getString(R.string.drcg),true)
                        extFinishWithAnim()
                        extStartActivity(MainActivity::class.java)

                    } else {
                        showToast(it.error.toString())

                    }
                }
            }
        })

//        tv_help.setOnClickListener {
//            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
//                putString("url", FrConstants.IMPORTWORD_HELP)
//            })
//        }
        set_zjc.et_content.addTextChangedListener(object : TextWatcher {
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
        if (set_zjc.et_content.text.toString().checkNotEmpty() &&
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