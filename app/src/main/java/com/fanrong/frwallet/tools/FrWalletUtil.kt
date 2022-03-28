package com.fanrong.frwallet.tools

import xc.common.tool.utils.checkNotEmpty


object FrWalletUtil {

    fun createReceiptQrcode(addr: String,contractAddress: String, decimal: String, value: String): String {
        if(contractAddress.checkNotEmpty()){
            return "ethereum:${addr}?contractAddress=${contractAddress}&decimal=${18}&value=${value}"
        }
        return "ethereum:${addr}?decimal=${18}&value=${value}"
    }

    fun getReceiptQrcode_value(url:String): String? {

        return url.substring((url.indexOf("&value=")+"&value=".length),url.length)
    }
    fun getReceiptQrcode_ethereum(url:String): String? {
        if (url.contains("contractAddress")){
            return url.substring((url.indexOf("ethereum:")+"ethereum:".length),url.indexOf("?contractAddress="))
        }
        return url.substring((url.indexOf("ethereum:")+"ethereum:".length),url.indexOf("?decimal="))
    }
    fun getReceiptQrcode_contractAddress(url: String): String?{
        if (url.contains("contractAddress")){
            return url.substring((url.indexOf("?contractAddress=")+"?contractAddress=".length),url.indexOf("&decimal="))
        }
        return ""
    }
}