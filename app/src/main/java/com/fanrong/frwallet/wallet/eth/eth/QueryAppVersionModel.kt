package com.fanrong.frwallet.wallet.eth.eth

class QueryAppVersionReq {
    var type:String = "Android"
    var languageCode:String = ""//(en_US、zh_CN)

    constructor(language:String){
        languageCode = language
    }
}

class  QueryAppVersionResp {
    var code:Int = 0
    var msg:String = ""
    var data:versionDetail? = null

    class versionDetail {
        var id:Int = 0
        var isForce:Int = 0
        var code:String = ""
        var type:String = ""
        var languageCode:String = ""
        var url:String = ""
        var createTime:String = ""
        var updateTime:String = ""
        var content:String = ""
    }
}