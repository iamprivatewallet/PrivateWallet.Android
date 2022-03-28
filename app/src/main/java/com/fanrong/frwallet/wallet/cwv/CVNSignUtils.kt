package com.fanrong.frwallet.wallet.cwv

import com.fanrong.frwallet.tools.CallJsCodeUtils
import xc.common.tool.CommonTool

object CVNSignUtils {

    fun signStr(privateKey: String, msg: String, callback: (result: String) -> Unit) {
        CommonTool.mainHandler.post {
            CallJsCodeUtils.cwv_ecHexSign(privateKey, msg) {
                callback( CallJsCodeUtils.readStringJsValue(it))
            }
        }
    }

}