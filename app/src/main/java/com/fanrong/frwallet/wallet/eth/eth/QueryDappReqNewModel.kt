package com.fanrong.frwallet.wallet.eth.eth

class QueryDappReqNew {
    var chainId:String = ""

    constructor(id:String){
        chainId = id
    }
}

class QueryDappRespNew {
    var code:Int = 0
    var msg:String = ""
    var data:MutableList<dappItem>? = null

    class dappItem{
        var id:String = ""
        var appName:String = ""
        var description:String = ""
        var iconUrl:String = ""
        var appUrl:String = ""
        var firType:String = ""
        var secType:String = ""
        var chainId:String = ""
    }
}