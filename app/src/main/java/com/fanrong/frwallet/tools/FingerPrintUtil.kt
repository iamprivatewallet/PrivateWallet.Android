package com.fanrong.frwallet.tools

import android.app.Activity
import com.codersun.fingerprintcompat.AonFingerChangeCallback
import com.codersun.fingerprintcompat.FingerManager
import com.codersun.fingerprintcompat.SimpleFingerCheckCallback
import com.fanrong.frwallet.main.MyApplication
import xc.common.kotlinext.showToast

object FingerPrintUtil {
    fun openFingerPrint(
        activity: Activity, onSucceed: () -> Unit, onError: ((error: String) -> Unit) = {}, onCancel: () -> Unit = {}
    ) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            when (FingerManager.checkSupport(activity)) {
                FingerManager.SupportResult.DEVICE_UNSUPPORTED -> {
                    showToast("您的设备不支持指纹")
                }
                FingerManager.SupportResult.SUPPORT_WITHOUT_DATA -> {
                    showToast("请在系统录入指纹后再验证")
                }
                FingerManager.SupportResult.SUPPORT -> FingerManager.build().setApplication(MyApplication.context)
                    .setTitle("指纹验证")
                    .setDes("请按下指纹")
                    .setNegativeText("取消")
                    .setFingerCheckCallback(object : SimpleFingerCheckCallback() {
                        override fun onSucceed() {
                            onSucceed.invoke()
                            //                        showToast("验证成功")
                        }

                        override fun onError(error: String) {
                            onError.invoke(error)
                            showToast(error)
                        }

                        override fun onCancel() {
                            onCancel.invoke()
                            showToast("您取消了识别")
                        }
                    })
                    .setFingerChangeCallback(object : AonFingerChangeCallback() {
                        override fun onFingerDataChange() {
                            showToast("指纹数据发生了变化")
                        }
                    })
                    .create()
                    .startListener(activity)
            }
        } else {
            showToast("当前系统不兼容指纹支付")
            onError("当前系统不兼容指纹支付")
        }
    }


}