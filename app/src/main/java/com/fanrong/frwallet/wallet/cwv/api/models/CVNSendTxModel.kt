package com.fanrong.frwallet.wallet.cwv.api.models

class CVNSendTxReq {
    var tx: String? = null

    constructor(tx: String?) {
        this.tx = tx
    }
}

class CVNSendTxResp {

    var hash:String? = null


}