package com.fanrong.frwallet.dapp

import xc.common.tool.utils.SWLog
import java.io.BufferedReader
import java.io.InputStreamReader

object JsRead {
    fun readJs(activity: DappBrowserActivity): String {
        val open = activity.resources.assets.open("dapp/main.js")
        val open1 = BufferedReader(InputStreamReader(open))
        var a: String? = null
        var ab  = StringBuilder()
        while (open1.readLine().also { a = it } != null) {
            ab.append(a + "\n")
        }

        SWLog.e(ab.toString())
        return ab.toString()
   }
}