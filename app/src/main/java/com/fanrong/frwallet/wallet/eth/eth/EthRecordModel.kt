package com.fanrong.frwallet.wallet.eth.eth

class EthRecordResp {
    var code: String? = null
    var msg: String? = null
    var data: MutableList<Record>? = null

    class Record {
        var tx_hash: String? = null
        var block_hash: String? = null
        var block_number: String? = null
        var tx_value: String? = null
        var tx_from: String? = null
        var tx_to: String? = null
        var contract_addr: String? = null
        var create_time: String? = null
        var modified_time: String? = null
        var tx_status: String? = null
    }
}