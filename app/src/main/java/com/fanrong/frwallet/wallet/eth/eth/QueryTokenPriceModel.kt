package com.fanrong.frwallet.wallet.eth.eth

class QueryTokenPriceReq{
    var tokenSymbol:String

    constructor(symbol:String){
        tokenSymbol = symbol
    }

}

class QueryTokenPriceResp{
    var code: Int = 0
    var data: Float = 0f
}