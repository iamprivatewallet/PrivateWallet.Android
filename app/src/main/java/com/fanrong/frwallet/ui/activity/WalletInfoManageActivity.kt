package com.fanrong.frwallet.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.found.GoodSnackbar
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.backup.BackUpHintActivity
import com.fanrong.frwallet.ui.backup.BackUpKeyStoreHintActivity
import com.fanrong.frwallet.ui.backup.BackUpPrivateKeyHintActivity
import com.fanrong.frwallet.ui.backup.BackupWordsShowActivity
import com.fanrong.frwallet.ui.dialog.CommonInputDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.dialog.PasswordDialog
import com.fanrong.frwallet.ui.dialog.ReceiptBackupsHintDialog
import com.fanrong.frwallet.ui.login.LoginActivity
import com.fanrong.frwallet.ui.walletmanager.FingerSetttingActivity
import com.fanrong.frwallet.ui.walletmanager.PasswordHintActivity
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_wallet_info_manage.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.AppManager
import xc.common.tool.utils.checkNotEmpty
import xc.common.utils.LibAppUtils
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog

class WalletInfoManageActivity : BaseActivity() {

    lateinit var walletInfo: WalletDao

    var advanced_state = "0"
    override fun getLayoutId(): Int {
        walletInfo = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
        return R.layout.activity_wallet_info_manage
    }

    @SuppressLint("ResourceAsColor")
    override fun initView() {

        wim_title.apply {
            setTitleText(getString(R.string.wallet_setting))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }
//        ll_wim_identity.setOnClickListener {
//            extStartActivity(IdentityWalletManageActivity::class.java, intent.extras!!)
//        }
//        ll_change_walletname.setOnClickListener {
//            CommonInputDialog(this).apply {
//                this.hint = walletInfo.walletName ?: walletInfo.chainType.toString()
//                this.title = "钱包名称"
//
//                onConfrim = object : FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        walletInfo.walletName = params.toString()
//                        WalletOperator.update(walletInfo)
//                        EventBus.getDefault().post(WalletInfoChangeEvent())
//                    }
//                }
//            }.show()
//        }
//        ll_ssh.setOnClickListener {
//            extStartActivity(FingerSetttingActivity::class.java, Bundle().apply {
//                putSerializable(FrConstants.WALLET_INFO, walletInfo)
//            })
//        }

//        ll_advanced.setOnClickListener {
//            if (advanced_state == "0") {
//                advanced_state = "1"
//                iv_arrow.setImageResource(R.mipmap.wim_shevronup)
//                ll_export_ks.visibility = View.VISIBLE
//                ll_export_pri.visibility = View.VISIBLE
//            } else {
//                advanced_state = "0"
//                iv_arrow.setImageResource(R.mipmap.wim_arrow_down)
//                ll_export_ks.visibility = View.GONE
//                ll_export_pri.visibility = View.GONE
//            }
//        }
//        ll_export_ks.setOnClickListener {
//            PasswordDialog(this,"1").apply {
//                this.walletInfo = this@WalletInfoManageActivity.walletInfo
//                onConfrim = object : FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        extStartActivity(BackUpKeyStoreHintActivity::class.java, intent.extras!!)
//                    }
//                }
//            }.show()
//        }
//        ll_export_pri.setOnClickListener {
//            PasswordDialog(this,"1").apply {
//                this.walletInfo = this@WalletInfoManageActivity.walletInfo
//                onConfrim = object : FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        extStartActivity(BackUpPrivateKeyHintActivity::class.java, intent.extras!!)
//                    }
//                }
//            }.show()
//        }
//
//        ll_eth2.setOnClickListener {
//            showToast("暂未开发")
//        }

//        ll_idw_backups.extGoneOrVisible(walletInfo.mnemonic.checkNotEmpty())
//        ll_backup.extGoneOrVisible(!"1".equals(walletInfo.isBackUp))

//        ll_undo_backup.visibility = View.GONE

//        ll_idw_backups.setOnClickListener {
//
//            PasswordDialog(this,"1").apply {
//                walletInfo = this@WalletInfoManageActivity.walletInfo
//                onConfrim = object :
//                    FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        if (walletInfo.mnemonic.checkNotEmpty()) {
//                            extStartActivity(BackUpHintActivity::class.java, Bundle().apply {
//                                putSerializable(FrConstants.WALLET_INFO, walletInfo)
//                            })
//                        } else if (walletInfo.keystore.checkNotEmpty()){
//                            extStartActivity(BackUpKeyStoreHintActivity::class.java,Bundle().apply{
//                                putSerializable(FrConstants.WALLET_INFO, walletInfo)
//                            })
//                        } else {
//                            extStartActivity(BackUpPrivateKeyHintActivity::class.java,Bundle().apply {
//                                putSerializable(FrConstants.WALLET_INFO, walletInfo)
//                            })
//                        }
//                    }
//                }
//            }.show()
//        }

//        ll_password.setOnClickListener {
//            extStartActivity(PasswordHintActivity::class.java, Bundle().apply {
//                putSerializable(FrConstants.WALLET_INFO, walletInfo)
//            })
//        }

//        tv_remove.setOnClickListener {
//            PasswordDialog(this,"1").apply {
//                walletInfo = this@WalletInfoManageActivity.walletInfo
//                onConfrim = object :
//                    FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        WalletOperator.deleteWallet(walletInfo)
//                        if (WalletOperator.queryMainWallet().size > 0){
//                            WalletOperator.changeCurrentWallet(WalletOperator.queryMainWallet()[0])
//                            EventBus.getDefault().post(CurrentWalletChange())
//                            AppManager.getAppManager().finishOthersActivity(MainActivity::class.java)
//                        }else{
////                            AppManager.getAppManager().finishOthersActivity(LoginActivity::class.java)
//                            extStartActivity(LoginActivity::class.java)
//                        }
//
//                    }
//                }
//            }.show()
//        }

//        tv_addr.setOnClickListener {
//
//            if (walletInfo.isBackUp=="0"&&walletInfo.isMainWallet=="1"){
//                ReceiptBackupsHintDialog(this).apply {
//                    onConfrim = object :
//                        FullScreenDialog.OnConfirmListener {
//                        override fun confirm(params: Any?) {
//                            extStartActivity(IdentityWalletManageActivity::class.java, Bundle().apply {
//                                putSerializable(FrConstants.WALLET_INFO, walletInfo)
//                            })
//                        }
//                    }
//                }.show()
//                return@setOnClickListener
//            }
//
//            LibAppUtils.copyText(walletInfo.address)
//            GoodSnackbar.showMsg(this, "钱包地址已复制")
//        }

//        tv_addr.setText(walletInfo.address)

//        ll_wim_identity.extGoneOrVisible("1".equals(walletInfo.isMainWallet))
//        ll_password.extGoneOrVisible(!"1".equals(walletInfo.isMainWallet))
//        ll_idw_backups.extGoneOrVisible(!"1".equals(walletInfo.isMainWallet))
//        tv_remove.extGoneOrVisible(!"1".equals(walletInfo.isMainWallet))


//        tv_name.text = walletInfo.walletName ?: walletInfo.chainType

//        if ("1".equals(walletInfo.isMainWallet)) {
//            tv_idi_name.text = WalletOperator.queryIdentityWallet().identityName ?: "tv_idi_name"
//        }

        //删除钱包
        ll_delectwallet.setOnClickListener{
            if (walletInfo.isBackUp=="0"&&walletInfo.isMainWallet=="1"){
                //提醒备份
                ReceiptBackupsHintDialog(this).apply {
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            extStartActivity(IdentityWalletManageActivity::class.java, Bundle().apply {
                                putSerializable(FrConstants.WALLET_INFO, walletInfo)
                            })
                        }
                    }
                }.show()
                return@setOnClickListener
            }else{
                //输入密码
                PasswordDialog(this,"1").apply {
                    walletInfo = this@WalletInfoManageActivity.walletInfo
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            WalletOperator.deleteWallet(walletInfo)
                            if (WalletOperator.queryMainWallet().size > 0){
                                WalletOperator.changeCurrentWallet(WalletOperator.queryMainWallet()[0])
                                EventBus.getDefault().post(CurrentWalletChange())
                                AppManager.getAppManager().finishOthersActivity(MainActivity::class.java)
                            }else{
//                            AppManager.getAppManager().finishOthersActivity(LoginActivity::class.java)
                                extStartActivity(LoginActivity::class.java)
                            }

                        }
                    }
                }.show()
                return@setOnClickListener
            }
        }

        //修改钱包名
        ll_xgqbm.setOnClickListener{
            extStartActivity(ModifyWalletNameActivity::class.java,Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, walletInfo)
            })
        }
        ll_xgmm.setOnClickListener{
            extStartActivity(ModityWalletPasswordActivity::class.java,Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, walletInfo)
            })
        }
        ll_cksy.setOnClickListener{
            extStartActivity(ExportPrivateKeyActivity::class.java,Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, walletInfo)
            })
        }
        ll_backupzjc.setOnClickListener{
            extStartActivity(
                //BackUpHintActivity
                BackupWordsShowActivity::class.java, Bundle().apply {
                    putString(FrConstants.PRE_PAGE, FrConstants.PRE_PAGE_CREATE)
                    putSerializable(FrConstants.WALLET_INFO, walletInfo)
                }
            )
        }
        if (walletInfo.isBackUp=="0"&&walletInfo.isMainWallet=="1"){
            ll_backupzjc.visibility = View.VISIBLE
        }else{
            ll_backupzjc.visibility = View.GONE
        }
    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: WalletInfoChangeEvent) {
        walletInfo = WalletOperator.queryWallet(walletInfo)
        initView()
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}