package com.fanrong.frwallet.ui.import

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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
//                    tv_password_hint.setText("不少于 8 位字符，建议混合大小写字母")
//                } else {
//                    tv_password_hint.setText("${s?.length}字符")
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
            et_way.setHint(getString(R.string.mr_way))
            ll_popup.visibility = View.GONE
            isZDY = false
        }
        rl_ledger.setOnClickListener{
            et_way.setHint(getString(R.string.ledger_way))
            ll_popup.visibility = View.GONE
            isZDY = false
        }
        rl_zdy.setOnClickListener{
            et_way.setHint(getString(R.string.zdylj_way))
            et_way.setFocusableInTouchMode(true);//不可编辑
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
        et_way.setFocusableInTouchMode(false);//不可编辑

        btn_create.setOnClickListener {
            if (!set_zjc.et_content.text.toString().checkIsWords(this)
                || !set_mm.et_content_1.text.toString().checkPassword(this)
            ) {
                return@setOnClickListener
            }

            if (!set_mm.et_content_1.text.toString().equals(set_mm.et_content_2.text.toString())) {
                showTopToast(this,getString(R.string.lcmmbyz),false)
                return@setOnClickListener
            }

            WalletHelper.createFromWords(
                chainType, set_zjc.et_content.text.toString(), set_mm.et_content_1.text.toString(), set_mm.et_content_2.text.toString()
            ) {
                if (it.success) {
                    showTopToast(this,getString(R.string.drcg),true)
                    extFinishWithAnim()
                    extStartActivity(MainActivity::class.java)

                } else {
                    showToast(it.error.toString())

                }
            }

        }
//        tv_help.setOnClickListener {
//            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
//                putString("url", FrConstants.IMPORTWORD_HELP)
//            })
//        }
    }

    override fun loadData() {
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}