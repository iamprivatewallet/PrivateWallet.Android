package com.fanrong.frwallet.wallet.eth.eth

class GasPriceReq {

    var jsonrpc: String = "2.0"
    var method = "eth_gasPrice"
    var id: String = "67"
    var params: MutableList<String>

    constructor() {
        params = mutableListOf<String>()
    }
}

class GasPriceResp {
    var result:String?=null

    fun getGasPrice(): String {

        return result?:""
    }

}