package com.fanrong.frwallet.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.UserOperator
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.ui.backup.BackUpHintActivity
import com.fanrong.frwallet.ui.dialog.LoginOutBackupsHintDialog
import com.fanrong.frwallet.ui.dialog.LoginOutDialog
import com.fanrong.frwallet.ui.dialog.PasswordDialog
import com.fanrong.frwallet.ui.login.LoginActivity
import com.fanrong.frwallet.ui.walletmanager.PasswordHintActivity
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_identity_wallet_manage.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.viewlib.utils.extInvisibleOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog

class IdentityWalletManageActivity : BaseActivity() {

    lateinit var mainWallet: WalletDao

    override fun getLayoutId(): Int {

        return R.layout.activity_identity_wallet_manage
    }

    @SuppressLint("ResourceAsColor")
    override fun initView() {
//        mainWallet = intent.getSerializableExtra(FrConstants.WALLET_INFO) as GreWalletModel
        mainWallet = WalletOperator.queryIdentityWallet()
        iwm_title.apply {
            setTitleText("管理身份钱包")
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_infoblack) {
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putString(DappBrowserActivity.PARAMS_URL, FrConstants.ID_MANAGE_HELPER)
                })
            }
        }
        tv_name.setText(mainWallet.identityName)

        ll_idw_userinfo.setOnClickListener {
            extStartActivity(IdentityInfoSettingActivity::class.java)
        }
        ll_idw_backups.setOnClickListener {
            showPasswordDialog4Backups()
        }

        ll_undo_backup.extInvisibleOrVisible(!"1".equals(mainWallet.isBackUp))

        ll_idw_addconins.setOnClickListener {
            extStartActivity(AddCoinsActivity::class.java)
        }

        ll_password.setOnClickListener {
            extStartActivity(PasswordHintActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, mainWallet)
            })
        }
        tv_idw_logout.setOnClickListener {
            logout()
        }
    }

    private fun showPasswordDialog4Backups() {
        PasswordDialog(this, "1").apply {
            walletInfo = this@IdentityWalletManageActivity.mainWallet
            onConfrim = object :
                FullScreenDialog.OnConfirmListener {
                override fun confirm(params: Any?) {
                    extStartActivity(BackUpHintActivity::class.java, Bundle().apply {
                        putSerializable(FrConstants.WALLET_INFO, mainWallet)
                    })
                }
            }
        }.show()
    }

    private fun logout() {
        if (!"1".equals(mainWallet.isBackUp)){
            LoginOutBackupsHintDialog(this).apply {
                onConfrim = object :
                    FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        showPasswordDialog4Backups()
                    }
                }
            }.show()
            return
        }

        fun showPasswordDialog() {
            PasswordDialog(this,"0").apply {
                walletInfo = this@IdentityWalletManageActivity.mainWallet
                onConfrim = object :
                    FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        UserOperator.loginout()
                        extStartActivity(LoginActivity::class.java)
                    }
                }
            }.show()
        }
        LoginOutDialog(this).apply {
            onConfrim = object :
                FullScreenDialog.OnConfirmListener {
                override fun confirm(params: Any?) {
                    showPasswordDialog()
                }
            }
        }.show()

    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: WalletInfoChangeEvent) {
        mainWallet = WalletOperator.queryWallet(mainWallet)
        initView()
    }

}