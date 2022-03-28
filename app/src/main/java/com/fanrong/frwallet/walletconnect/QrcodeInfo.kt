package com.fanrong.frwallet.walletconnect

import java.net.URLDecoder

class QrcodeInfo {

    var sessionId: String = ""
    var bridgeServe = ""
    var key = ""
    var version = ""

    companion object {
        fun isWcQrcode(str: String): Boolean {
            try {
                decode(str)
                return true
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return false
        }


//        var a =
//            "wc:00e46b69-d0cc-4b3e-b6a2-cee442f97188@1?bridge=https%3A%2F%2Fbridge.walletconnect.org&key=91303dedf64285cbbaf9120f6e9d160a5c8aa3deb67017a3874cd272323f48ae"

        fun decode(str: String): QrcodeInfo {


            return QrcodeInfo().apply {
                val decode = URLDecoder.decode(str)
                val split = decode.split("?")
                var params = split[1].split("&")
                split[0].subSequence(0, split[0].indexOf(":"))
                sessionId = split[0].substring(split[0].indexOf(":") + 1, split[0].indexOf("@"))
                version = split[0].substring(split[0].indexOf("@") + 1, split[0].length)
                for (param in params) {
                    if (param.startsWith("bridge")) {
                        bridgeServe = param.substring(param.indexOf("=") + 1, param.length)
                    } else if (param.startsWith("key")) {
                        key = param.substring(param.indexOf("=") + 1, param.length)
                    }
                }
            }
        }


    }
}