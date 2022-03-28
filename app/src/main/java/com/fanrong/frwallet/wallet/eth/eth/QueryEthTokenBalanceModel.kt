package com.fanrong.frwallet.wallet.eth.eth

class QueryEthTokenBalanceReq {

    var jsonrpc: String = "2.0"
    var method = "eth_call"
    var id: String = "67"
    var params = mutableListOf<Any>()

    class Params(addr: String, tokenAddr: String) {
        var data = "0x70a08231000000000000000000000000${addr.removePrefix("0x")}"
        var from = addr
        var to = tokenAddr
    }

    constructor(addr: String, tokenAddr: String) {

        params.add(Params(addr, tokenAddr))
        params.add("latest")

    }
}


class QueryEthTokenBalanceResp {
    var code: String? = null
    var msg: String? = null
    var result: String? = null
    var id: String? = null


    fun getBalance(): String {
        if ("0x0000000000000000000000000000000000000000000000000000000000000000".equals(result)) {
            return "0"
        } else {
            return result!!
        }
    }

}