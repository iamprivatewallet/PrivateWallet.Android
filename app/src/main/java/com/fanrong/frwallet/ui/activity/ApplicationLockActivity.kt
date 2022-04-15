package com.fanrong.frwallet.ui.activity

import android.content.Intent
import android.os.Build
import android.widget.Switch
import androidx.annotation.RequiresApi
import com.basiclib.base.BaseActivity
import com.codersun.fingerprintcompat.AonFingerChangeCallback
import com.codersun.fingerprintcompat.FingerManager
import com.codersun.fingerprintcompat.SimpleFingerCheckCallback
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.AppLockPasswordInputDialog
import com.fanrong.frwallet.ui.dialog.HintDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.dialog.PasswordDialog
import com.fanrong.frwallet.view.FingerDialog
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_applicationlock.*
import kotlinx.android.synthetic.main.activity_lockapp.*
import kotlinx.android.synthetic.main.finger_setting_activity.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.SPUtils
import xc.common.viewlib.view.customview.FullScreenDialog

class ApplicationLockActivity: BaseActivity() {
    lateinit var walletInfo: WalletDao
    override fun getLayoutId(): Int {
        return R.layout.activity_applicationlock
    }

    override fun initView() {
        wim_title.apply {
            setTitleText(getString(R.string.yys))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }
        walletInfo = WalletOperator.currentWallet!!
        ll_mm.setOnClickListener{
            val applockpassword = SPUtils.getString(FrConstants.APPLOCKPASSWORD, "")
            if (applockpassword.equals("")){
                extStartActivity(SetAppLockPasswordActivity::class.java)
            }else{
//                PasswordDialog(this,"-1").apply {
//                    this.walletInfo = this@ApplicationLockActivity.walletInfo
//                    onConfrim = object : FullScreenDialog.OnConfirmListener {
//                        override fun confirm(params: Any?) {
//                            val s = params as String
//                            if (s.equals(applockpassword)){
//                                extStartActivity(SetAppLockPasswordActivity::class.java)
//                                dismiss()
//                            }else{
//                                showTopToast(this@ApplicationLockActivity,getString(R.string.mmcw),false)
//                                dismiss()
//                            }
//
//                        }
//                    }
//                }.show()

                AppLockPasswordInputDialog(this).apply {
                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            val s = params as String
                            if (s.equals(applockpassword)){
                                extStartActivity(SetAppLockPasswordActivity::class.java)
                                dismiss()
                            }else{
                                showTopToast(this@ApplicationLockActivity,getString(R.string.mmcw),false)
                                dismiss()
                            }

                        }
                    }
                }.show()
            }
        }

        val applockpassword = SPUtils.getString(FrConstants.APPLOCKPASSWORD, "")
        if (applockpassword == null || applockpassword.equals("")){
            tv_mmsetState.setText(getString(R.string.wsz))
        }else{
            tv_mmsetState.setText(getString(R.string.xg))
        }

        //开启应用锁
        val isOpenAppLock = SPUtils.getBoolean(FrConstants.ISOPENAPPLICATIONLOCK, false)
        if (isOpenAppLock){
            iv_mmunlockapp.setImageResource(R.mipmap.icon_toggle_on)
        }else{
            iv_mmunlockapp.setImageResource(R.mipmap.icon_toggle_off)
        }
        iv_mmunlockapp.setOnClickListener{
            val applockpassword = SPUtils.getString(FrConstants.APPLOCKPASSWORD, "")
            if (applockpassword.equals("")){
                showTopToast(this,getString(R.string.qxszmm),false)
            }else{
//                PasswordDialog(this,"-1").apply {
//                    this.walletInfo = this@ApplicationLockActivity.walletInfo
//                    onConfrim = object : FullScreenDialog.OnConfirmListener {
//                        override fun confirm(params: Any?) {
//
//                        }
//                    }
//                }.show()

                AppLockPasswordInputDialog(this).apply {
                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            val s = params as String
                            if (s.equals(applockpassword)){
                                LockAppStateChange()
                                dismiss()
                            }else{
                                showTopToast(this@ApplicationLockActivity,getString(R.string.mmcw),false)
                                dismiss()
                            }
                        }
                    }
                }.show()
            }
        }

        //指纹交易
        iv_openfinger.setOnClickListener{
            val walletInfo_temp = WalletOperator.currentWallet!!
            if (walletInfo_temp.isFinger == "1"){
                HintDialog(this).apply {
                    this.canceledOutside=false
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun confirm(params: Any?) {
                            walletInfo.isFinger = "0"
                            WalletOperator.update(walletInfo)
                            this@ApplicationLockActivity.iv_openfinger.setImageResource(R.mipmap.icon_toggle_off)
                            dismiss()
                            walletInfo = WalletOperator.currentWallet!!
                        }
                    }
                    onCancel = object :
                        FullScreenDialog.OnCancelListener{
                        override fun cancel() {
                            dismiss()
                        }
                    }
                }.show()
            }else{
                PasswordDialog(this,"1").apply {
                    canceledOutside=false
                    this.walletInfo = this@ApplicationLockActivity.walletInfo
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        @RequiresApi(Build.VERSION_CODES.M)
                        override fun confirm(params: Any?) {
                            openFingerPrint()
                            this@ApplicationLockActivity.iv_openfinger.setImageResource(R.mipmap.icon_toggle_on)
                        }
                    }
                    onCancel = object :
                        FullScreenDialog.OnCancelListener{
                        override fun cancel() {
                            dismiss()
                        }
                    }
                }.show()
            }
        }
        if (walletInfo.isFinger == "1"){
            //开启了指纹
            iv_openfinger.setImageResource(R.mipmap.icon_toggle_on)
        }else{
            //未开启
            iv_openfinger.setImageResource(R.mipmap.icon_toggle_off)
        }
    }

    fun LockAppStateChange(){
        val isOpenAppLock = SPUtils.getBoolean(FrConstants.ISOPENAPPLICATIONLOCK, false)
        if (isOpenAppLock){
            iv_mmunlockapp.setImageResource(R.mipmap.icon_toggle_off)
        }else{
            iv_mmunlockapp.setImageResource(R.mipmap.icon_toggle_on)
        }
        SPUtils.saveValue(FrConstants.ISOPENAPPLICATIONLOCK,!isOpenAppLock)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun openFingerPrint() {
        when (FingerManager.checkSupport(this@ApplicationLockActivity)) {
            FingerManager.SupportResult.DEVICE_UNSUPPORTED -> {
                showToast(getString(R.string.ndsbbzczw))
            }
            FingerManager.SupportResult.SUPPORT_WITHOUT_DATA -> {
                showToast(getString(R.string.systemnofingerdata))
            }
            FingerManager.SupportResult.SUPPORT -> FingerManager.build().setApplication(application)
                .setTitle(getString(R.string.zwyz))
                .setDes(getString(R.string.qaxzw))
                .setNegativeText(getString(R.string.qx))
                .setFingerCheckCallback(object : SimpleFingerCheckCallback() {
                    override fun onSucceed() {
                        showToast(getString(R.string.szcg))
                        walletInfo.isFinger = "1"
                        WalletOperator.update(walletInfo)
                        setResult(RESULT_OK, Intent().apply {
                            putExtra(FrConstants.FINGER_SETTING,"1")
                        })
                        walletInfo = WalletOperator.currentWallet!!
                    }
                    override fun onError(error: String) {
                        showToast(getString(R.string.yzsb))
                    }

                    override fun onCancel() {
                        showToast(getString(R.string.nqxlsb))
                    }
                })
                .setFingerChangeCallback(object : AonFingerChangeCallback() {
                    override fun onFingerDataChange() {
                        showToast(getString(R.string.zwsjfsbh))
                    }
                })
                .create()
                .startListener(this)
        }
    }

    override fun loadData() {

    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}