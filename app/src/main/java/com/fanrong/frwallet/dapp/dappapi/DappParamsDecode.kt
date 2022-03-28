package com.fanrong.frwallet.dapp.dappapi.cvn

import xc.common.tool.utils.XcSingleten
import xc.common.tool.utils.isHexStr


class DappParamsError : RuntimeException {
    constructor()

    constructor(msg: String) : super(msg) {

    }
}

object DappParamsDecode {

    class EthCall {
        var to: String? = null
        var data: String? = null
    }

    fun decode_ethCall(params: Any?): EthCall {
        try {
            val paramsMap = XcSingleten.gson.fromJson(params.toString(), List::class.java)[0] as Map<String, String>
            var params = EthCall()
            params.to = paramsMap.get("to") ?: ""
            params.data = paramsMap.get("data") ?: ""

            return params

        } catch (e: Exception) {
            e.printStackTrace()
            throw DappParamsError()
        }
    }


    /*************************************************************************************************************/

    class GetBalance {
        var addr = ""

        constructor(addr: String) {
            this.addr = addr
        }
    }

    fun decode_getBalance(params: Any?): GetBalance {
        try {
            val addr = XcSingleten.gson.fromJson(params.toString(), List::class.java)[0].toString()
            return GetBalance(addr)

        } catch (e: Exception) {
            e.printStackTrace()
            throw DappParamsError()
        }
    }


    /*************************************************************************************************************/


    class PersonalSign {
        var from = ""
        var content = ""

        constructor(from: String, content: String) {
            this.from = from
            this.content = content
        }
    }

    fun decode_personalSign(params: Any?): PersonalSign {
        try {

            var _params = XcSingleten.gson.fromJson(params.toString(), List::class.java)
            var from = _params[1].toString()
            var content = _params[0].toString()

            if (!content.isHexStr()) {
                throw DappParamsError("sign msg not hex")
            }

            return PersonalSign(from, content)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DappParamsError(e.message ?: "")
        }
    }


    /*************************************************************************************************************/

    fun decode_rpcRequest(params: Any?): List<*>? {

        try {
            return XcSingleten.gson.fromJson(params.toString(), List::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            throw DappParamsError(e.message ?: "")
        }
    }


    /*************************************************************************************************************/

    class SendTransaction {
        var from = ""
        var value = ""
        var to = ""
        var gasPrice = ""
        var data = ""
        var nonce = ""

    }

    fun decode_sendTransaction(params: Any?): SendTransaction {

        try {

            val paramsMap = XcSingleten.gson.fromJson(params.toString(), List::class.java)[0] as Map<String, String>

            var sendTransaction = SendTransaction().apply {
                from = paramsMap.get("from") ?: ""
                value = paramsMap.get("value") ?: ""
                to = paramsMap.get("to") ?: ""
                gasPrice = paramsMap.get("gasPrice") ?: ""
                data = paramsMap.get("data") ?: ""
            }

            return sendTransaction
        } catch (e: Exception) {
            e.printStackTrace()
            throw DappParamsError(e.message ?: "")
        }
    }


}