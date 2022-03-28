package com.fanrong.frwallet.wallet.cwv.model

class TransactionRecordDetailReq {
    var transaction_hash: String? = null

    constructor(transaction_hash: String?) {
        this.transaction_hash = transaction_hash
    }
}

class TransactionRecordDetailResp {

    val status: String? = null
    val timestamp: String? = null
    val from: String? = null
    val to: String? = null
    val transaction_hash: String? = null
    val direction: String? = null
    val nonce: String? = null
    val quantity: String? = null
    val is_to_more: String? = null
    val ext_data: String? = null

}