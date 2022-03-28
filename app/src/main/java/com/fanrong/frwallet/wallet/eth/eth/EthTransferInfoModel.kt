package com.fanrong.frwallet.wallet.eth.eth

class EthTransferInfoReq {
    var jsonrpc: String = "2.0"
    var method = "eth_getTransactionReceipt"
    var id: String = "67"
    var params: MutableList<String>

    constructor(hash: String) {
        params = mutableListOf<String>(hash)
    }

}

class EthTransferInfoResp {

    var result: TransferInfo? = null

    class TransferInfo {
        var transactionHash: String? = null
        var from: String? = null
        var to: String? = null
        var blockNumber: String? = null
        var status: String? = null
        var gasUsed: String? = null
        var blockHash: String? = null
    }


}