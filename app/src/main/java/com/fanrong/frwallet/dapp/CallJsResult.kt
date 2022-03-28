package com.fanrong.frwallet.dapp

import android.webkit.WebView
import xc.common.tool.CommonTool
import xc.common.tool.utils.SWLog

object CallJsResult {
    fun toResult(webview: WebView, requestId: String, result: String) {
//        SWLog.e("toResult() called with: requestId = $requestId, result = $result")
        SWLog.e(String.format("window.ethereum.frAppResult('%s','%s')", requestId, result))

        CommonTool.mainHandler.post {
            webview.evaluateJavascript(
                String.format("ethereum.frAppResult('%s','%s')", requestId, result), null
            )
        }
    }
}