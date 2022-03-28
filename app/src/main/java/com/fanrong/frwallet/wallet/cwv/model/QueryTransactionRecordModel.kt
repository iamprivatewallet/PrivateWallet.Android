package com.fanrong.frwallet.wallet.cwv.model

import java.io.Serializable

data class TransactionRecordReq(var dapp_id: String) : Serializable {
    var limit: String? = null
    var offset: String? = null
    var address: String? = null

}


data class TransactionRecordResp(var msg: String) : Serializable {
    var transactionInfo: MutableList<TransRecordItem>? = null
}

data class TransRecordItem(
    val block_height: String
) : Serializable {

//    "status": "0x01",
//    "timestamp": 1598521860000,
//    "from": "0x3c1ea4aa4974d92e0eabd5d024772af3762720a0",
//    "to": "0x8c4c0be3000620b527f29b23597f6f1818169108",
//    "transaction_hash": "0xce814b657350799da20ccde4c0ca986695d6129530eedca16d920fac8a162da7de",
//    "direction": "in",
//    "nonce": 0,
//    "quantity": "0.00000000",
//    "is_to_more": 0


    var status: String? = null
    var timestamp: String? = null
    var from: String? = null
    var to: String? = null
    var transaction_hash: String? = null
    var direction: String? = null
    var nonce: String? = null
    var quantity: String? = null
    var is_to_more: String? = null

    fun isTransOut(): Boolean {
        return !"in".equals(direction,ignoreCase = true)
    }

    fun getTransOutAddr(): String {
        return from?:""
    }

    fun getTransInAddr(): String {
        return to!!
    }

}