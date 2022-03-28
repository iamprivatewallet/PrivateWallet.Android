package com.fanrong.frwallet.wallet.eth.eth

class Eth_GetTransactionByHashReq {
    var jsonrpc: String = "2.0"
    var method = "eth_getTransactionByHash"
    var id: String = "67"
    var params: MutableList<String>

    constructor(hash: String) {
        params = mutableListOf<String>(hash)
    }

}

class Eth_GetTransactionByHashResp {

    var result: TransferInfo? = null

    class TransferInfo {
        var transactionHash: String? = null
        var from: String? = null
        var to: String? = null
        var blockNumber: String? = null
        var status: String? = null
        var gasPrice: String? = null
        var gas: String? = null
        var blockHash: String? = null
        var value:String? = null
        var input:String? = null
    }


}