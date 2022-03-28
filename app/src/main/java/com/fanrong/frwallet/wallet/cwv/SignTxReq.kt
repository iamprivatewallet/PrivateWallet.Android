package com.fanrong.frwallet.wallet.cwv

class SignTxReq {
    var tx: String? = null

    constructor(tx: String?) {
        this.tx = tx
    }
}

class SignTxResp {
    var retCode:Int? = null
    var retMsg:String? = null
    var hash:String?= null
}