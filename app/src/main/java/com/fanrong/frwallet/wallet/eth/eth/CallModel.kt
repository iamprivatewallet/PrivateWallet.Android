package com.fanrong.frwallet.wallet.eth.eth

class CallReq {
//    {"jsonrpc":"2.0","method":"eth_call","params":[{"from":"0xf7788958b08ed5788e4ebc0bf9ce44c6d72e58d5","to":"0x229ca99574909692ec72e96053dcafd91de951e2","data":"0x313ce567"},"latest"],"id":1}


    var jsonrpc: String = "2.0"
    var method = "eth_call"
    var id: String = "3388"
    var params: MutableList<Any>


    constructor(addr: ReqInfo) {
        params = mutableListOf<Any>(addr, "latest")
    }


    class ReqInfo {
        var from: String? = null
        var to: String? = null
        var gas: String? = null
        var gasPrice: String? = null
        var value: String? = null
        var data: String? = null
    }

}

class CallResp {

    var error: Any? = null
    var result: String? = null
}