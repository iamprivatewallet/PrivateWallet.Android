package com.fanrong.frwallet.ui.walletmanager

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
import kotlinx.android.synthetic.main.finger_setting_activity.*
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.GeneralHintDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.dialog.PasswordDialog
import kotlinx.android.synthetic.main.finger_setting_activity.ac_title
import kotlinx.android.synthetic.main.finger_setting_activity.sth_finger
import kotlinx.android.synthetic.main.use_setting_activity.*
import xc.common.kotlinext.showToast
import xc.common.viewlib.view.customview.FullScreenDialog


class FingerSetttingActivity : BaseActivity() {
    lateinit var walletInfo: WalletDao
    override fun getLayoutId(): Int {
        return R.layout.finger_setting_activity
    }
    var isCallback: Boolean = true
    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
//        walletInfo = intent.getSerializableExtra(FrConstants.WALLET_INFO) as GreWalletModel
        walletInfo = WalletOperator.currentWallet!!
        sth_finger.isChecked = "1" == walletInfo.isFinger
        ac_title.apply {
            extInitCommonBgAutoBack(this@FingerSetttingActivity, "免密支付")
        }

        sth_finger.setOnCheckedChangeListener { buttonView, isChecked ->
            if (!isCallback){
                return@setOnCheckedChangeListener
            }
            if (isChecked){
                PasswordDialog(this,"1").apply {
                    canceledOutside=false
                    this.walletInfo = this@FingerSetttingActivity.walletInfo
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            openFingerPrint()
                        }
                    }
                    onCancel = object :
                    FullScreenDialog.OnCancelListener{
                        override fun cancel() {
                            setFingerChecked(buttonView as Switch,false)
                        }
                    }
                }.show()
            }else{
                GeneralHintDialog(this).apply {
                    content="确认关闭免密支付？"
                    onConfrim= object : FullScreenDialog.OnConfirmListener{
                        override fun confirm(params: Any?) {
                            walletInfo.isFinger = "0"
                            WalletOperator.update(walletInfo)
                        }
                    }
                    onCancel= object :FullScreenDialog.OnCancelListener{
                        override fun cancel() {
                            setFingerChecked(buttonView as Switch,true)
                        }
                    }
                }.show()
            }

        }


    }

    override fun loadData() {
    }
    fun setFingerChecked(sw : Switch, state :Boolean){
        isCallback=false
        sw.isChecked=state
        isCallback=true
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun openFingerPrint() {
        when (FingerManager.checkSupport(this@FingerSetttingActivity)) {
            FingerManager.SupportResult.DEVICE_UNSUPPORTED -> {
                showToast("您的设备不支持指纹")
                sth_finger.isChecked = false
            }
            FingerManager.SupportResult.SUPPORT_WITHOUT_DATA -> {
                showToast("请在系统录入指纹后再验证")
                sth_finger.isChecked = false
            }
            FingerManager.SupportResult.SUPPORT -> FingerManager.build().setApplication(application)
                .setTitle("指纹验证")
                .setDes("请按下指纹")
                .setNegativeText("取消")
                .setFingerCheckCallback(object : SimpleFingerCheckCallback() {
                    override fun onSucceed() {
                        showToast("设置成功")
                        walletInfo.isFinger = "1"
                        WalletOperator.update(walletInfo)
                        setResult(RESULT_OK, Intent().apply {
                            putExtra(FrConstants.FINGER_SETTING,"1")
                        })
                    }
                    override fun onError(error: String) {
                        showToast("验证失败")
                        setFingerChecked(sth_finger,false)
                    }

                    override fun onCancel() {
//                        showToast("您取消了识别")
                        setFingerChecked(sth_finger,false)
                    }
                })
                .setFingerChangeCallback(object : AonFingerChangeCallback() {
                    override fun onFingerDataChange() {
                        showToast("指纹数据发生了变化")
                        setFingerChecked(sth_finger,false)
                    }
                })
                .create()
                .startListener(this)
        }
    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }

}