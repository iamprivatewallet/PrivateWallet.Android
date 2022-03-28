package com.fanrong.frwallet.wallet.cwv.api.models

class CVNHashInfoReq {
    var hash: String? = null

    constructor(hash: String?) {
        this.hash = hash
    }
}

class CVNHashInfoResp {

    var transaction: Transaction? = null
    var status: Status? = null

    class Transaction {
        var status: Status? = null
    }

    class Status {
        var status: String? = null
        var result: String? = null
        var height: String? = null
        var timestamp: String? = null
    }

}