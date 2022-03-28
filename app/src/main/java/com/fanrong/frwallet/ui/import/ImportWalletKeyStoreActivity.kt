package com.fanrong.frwallet.ui.import

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.wallet.WalletHelper
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.import_wallet_keystore_activity.*
import kotlinx.android.synthetic.main.import_wallet_keystore_activity.ac_title
import kotlinx.android.synthetic.main.import_wallet_keystore_activity.btn_create
import kotlinx.android.synthetic.main.import_wallet_keystore_activity.cb_eye
import kotlinx.android.synthetic.main.import_wallet_keystore_activity.et_password
import kotlinx.android.synthetic.main.import_wallet_words_activity.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.LibViewUtils
import xc.common.tool.utils.checkIsEmpty
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
import xc.common.viewlib.view.EditTextPasswordStyle


class ImportWalletKeyStoreActivity : BaseActivity() {

    val walletType: String by lazy {
        intent.getStringExtra(FrConstants.WALLET_TYPE)
    }

    override fun getLayoutId(): Int {
        return R.layout.import_wallet_keystore_activity
    }

    override fun initView() {


        ac_title.apply {
            extInitCommonBgAutoBack(this@ImportWalletKeyStoreActivity, "导入${walletType}钱包")
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_scan) {
                LibPremissionUtils.needCamera(this@ImportWalletKeyStoreActivity, object : PermissonSuccess {
                    override fun hasSuccess() {
                        extStartActivityForResult(CaptureActivity::class.java, Bundle().apply {
                            putString(Constant.JUMP_TYPE, "ImportWalletKeyStore")
                        }, 101) { i: Int, intent: Intent? ->
                            if (i == Activity.RESULT_OK) {
                                et_keystore.setText(intent?.getStringExtra(Constant.CODED_CONTENT) ?: "");
                            }
                        }
                    }
                })
            }
        }


        LibViewUtils.updateBtnStatus(btn_create, et_keystore, et_password)


        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
        }
        cb_eye.isChecked = false

        btn_create.setOnClickListener {
            if (et_keystore.text.toString().checkIsEmpty()) {
                showToast("请输入私钥")
                return@setOnClickListener
            }
            if (et_password.text.toString().checkIsEmpty()) {
                showToast("请输入密码")
                return@setOnClickListener
            }

            WalletHelper.createFromKeystore(
                intent.getStringExtra(FrConstants.WALLET_TYPE),
                et_keystore.text.toString(), et_password.text.toString()
            ) { result ->
                if (result.success) {
                    EventBus.getDefault().post(WalletInfoChangeEvent())
                    showToast("导入成功")
                    extFinishWithAnim()
                } else {
                    showToast(result.error.toString())
                }
            }
        }
        tv_help_keystore.setOnClickListener {
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString("url", FrConstants.IMPORTKEYSTORE_HELP)
            })
        }
    }

    override fun loadData() {
    }
}