package com.fanrong.frwallet.tools

import android.os.Bundle
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectUtil
import com.fanrong.frwallet.ui.address.AddAddressActivity
import com.fanrong.frwallet.ui.receipt.TransferActivity
import com.yzq.zxinglibrary.common.Constant
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast

object QrcodeDecode {

    fun handleScan(data: Bundle, activity: BaseActivity) {

        var result = data?.getString(Constant.CODED_CONTENT) ?: ""
        var jumpType = data?.getString(Constant.JUMP_TYPE) ?: ""
        if (result.startsWith("http")) {
            activity.extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString(DappBrowserActivity.PARAMS_URL, result)
                putString(FrConstants.PP.IS_DAPP, "1")
            })
        } else if (result.startsWith("wc")) {
            WalletConnectUtil.connectSocket(result)
        } else if (result.extCheckIsAddrNoToast()) {
            if (jumpType == "AddAddress") {
                activity.extStartActivity(AddAddressActivity::class.java, Bundle().apply {
                    putString(Constant.CODED_CONTENT, result)
                })
            } else if (jumpType == "Transfer") {
                activity.extStartActivity(TransferActivity::class.java, Bundle().apply {
                    putString(Constant.CODED_CONTENT, result)
                })
            }
        } else if (result.contains("ethereum:") && result.contains("&value=")) {
            activity.extStartActivity(TransferActivity::class.java, Bundle().apply {
                putString(Constant.CODED_CONTENT, result)
            })
        } else {
            showToast("暂不支持的二维码")
        }
    }

}