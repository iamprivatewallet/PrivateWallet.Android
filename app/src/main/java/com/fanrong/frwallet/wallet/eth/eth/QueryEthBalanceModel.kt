package com.fanrong.frwallet.wallet.eth.eth

class QueryEthBalanceReq {

    var jsonrpc: String = "2.0"
    var method = "eth_getBalance"
    var id: String = "67"
    var params: MutableList<String>

    constructor(addr: String) {
        params = mutableListOf<String>("0x" + addr.removePrefix("0x"), "latest")
    }
}



class QueryEthBalanceResp {
    var data: String? = null
    var code: String? = null
    var msg: String? = null
    var result: String? = null


    fun getBalance(): String {
        return result ?: "0x0"
    }

}