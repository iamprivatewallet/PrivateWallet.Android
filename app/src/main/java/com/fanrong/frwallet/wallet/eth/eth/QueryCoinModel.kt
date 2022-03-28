package com.fanrong.frwallet.wallet.eth.eth

class QueryCoinReq {
    var tokenChain:String = ""
    var isTop:String = ""
    var tokenContract:String = ""

    constructor(chain:String,top:String,key:String){
        tokenChain = chain
        isTop = top
        tokenContract = key
    }
}

class QueryCoinResp{
    var code:Int = 0
    var msg:String = ""
    var data:MutableList<IconItem>? = null

    class IconItem{
        var id:String = ""
        var tokenContract:String = ""
        var tokenName:String = ""
        var tokenSymbol:String = ""
        var tokenDecimals:Int = 0
        var tokenChain:String = ""
        var hotTokens:String = ""
        var tokenLogo:String = ""
        var createTime:String = ""
        var isTop:String = ""
    }
}