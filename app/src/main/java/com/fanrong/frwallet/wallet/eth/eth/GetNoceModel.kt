package com.fanrong.frwallet.wallet.eth.eth

class GetNoceReq {
    var jsonrpc: String = "2.0"
    var method = "eth_getTransactionCount"
    var id: String = "67"
    var params: MutableList<String>

    constructor(addr: String) {
        params = mutableListOf<String>("0x" + addr.removePrefix("0x"), "latest")
    }

}

class GetNoceResp {
    var result: String? = null


    fun getNoce(): String {

        return result ?: ""
    }

}