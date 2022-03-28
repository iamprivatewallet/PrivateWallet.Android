package com.fanrong.frwallet.wallet.eth.eth

class RpcRequestReq {
    var jsonrpc: String = "2.0"
    var method = ""
    var id: String = "56"
    var params: Any?


    constructor(method: String, params: Any?) {
        this.method = method
        this.params = params
    }
}


class RpcRequestResp {
    var result: Any? = null
}