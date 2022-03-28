package com.fanrong.frwallet.wallet.eth.eth

import java.util.*

class EthTransferReq {

    var jsonrpc: String = "2.0"
    var method = "eth_sendRawTransaction"
    var id: String = "67"
    var params: MutableList<String>

    constructor(signInfo: String) {
        params = mutableListOf<String>(signInfo)
    }
}

class EthUnLockReq {

    var jsonrpc: String = "2.0"
    var method = "personal_unlockAccount"
    var id: String = Date().time.toString()
    var params: MutableList<String>

    constructor(address: String) {
        params = mutableListOf<String>(address)
    }
}

class EthTransferResp {

    var result: String? = null
    var error: Error? = null

    class Error {
        var code: String? = null
        var message: String? = null
    }


    fun getHash(): String {
        return result ?: ""
    }

}