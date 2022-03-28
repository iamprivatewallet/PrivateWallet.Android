package com.fanrong.frwallet.wallet.cwv.bean

import java.io.Serializable

data class MarketInfoResp(
    var retcode: String
) : Serializable {
    var kmarket: List<Kmarket>? = null
    var msg: String? = null
}

data class Kmarket(
    var coin: String
) : Serializable {

    var close: String? = null
    var currencyvalue: String? = null
    var period: String? = null
    var rmbclose: String? = null
    var rmbcurrencyvalue: String? = null
    var tradecoin: String? = null
    var updown: String? = null
}