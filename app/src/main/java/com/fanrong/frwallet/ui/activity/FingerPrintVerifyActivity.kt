package com.fanrong.frwallet.ui.activity

import android.os.Build
import androidx.annotation.RequiresApi
import com.basiclib.base.BaseActivity
import com.codersun.fingerprintcompat.AonFingerChangeCallback
import com.codersun.fingerprintcompat.FingerManager
import com.codersun.fingerprintcompat.SimpleFingerCheckCallback
import com.fanrong.frwallet.R
import com.fanrong.frwallet.main.MainActivity
import kotlinx.android.synthetic.main.activity_finger_print_verify.*
import kotlinx.android.synthetic.main.use_setting_activity.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast

class FingerPrintVerifyActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_finger_print_verify
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
        openFingerPrint()
        iv_houmen.setOnClickListener {
            extStartActivity(MainActivity::class.java)
            extFinishWithAnim()
        }
        tv_finger.setOnClickListener {
            openFingerPrint()
        }
        tv_usepsw.setOnClickListener {
            extStartActivity(UseWalletPswLoginActivity::class.java)
        }
    }

    override fun loadData() {
    }
    @RequiresApi(Build.VERSION_CODES.M)
    fun openFingerPrint(){
        FingerManager.updateFingerData(this@FingerPrintVerifyActivity)
        when (FingerManager.checkSupport(this@FingerPrintVerifyActivity)) {
            FingerManager.SupportResult.SUPPORT -> FingerManager.build().setApplication(application)
                .setTitle("Log In")
                .setDes("")
                .setNegativeText("CANCEL")
//                .setFingerDialogApi23()
                .setFingerCheckCallback(object : SimpleFingerCheckCallback() {
                    override fun onSucceed() {
                        extStartActivity(MainActivity::class.java)
                        extFinishWithAnim()
                    }

                    override fun onError(error: String) {
                        showToast("验证失败")
                    }

                    override fun onCancel() {
//                        showToast("您取消了识别")
                    }
                })
                .setFingerChangeCallback(object : AonFingerChangeCallback() {
                    override fun onFingerDataChange() {
                        showToast("指纹数据发生了变化")
                    }
                })
                .create()
                .startListener(this)
        }
    }
}