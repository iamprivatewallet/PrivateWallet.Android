package com.fanrong.frwallet.wallet.eth.eth

class QuerySystemMessageDetailReq {
    var id:String = ""

    constructor(_id:String){
        id = _id
    }
}

class QuerySystemMessageDetailResp {
    var code:Int = 0
    var msg:String = ""
    var data:Data? = null

    class Data{
        var limitStart:Float = 0f
        var limitEnd:Float = 0f
        var pageNumber:Int = 0
        var pageSize:Int = 0
        var errors:String = ""
        var id:Int = 0
        var title:String = ""
        var iconUrl:String = ""
        var address:String = ""
        var createTime:String = ""
        var author:String = ""
        var status:String = ""
        var context:String = ""
    }
}