package com.fanrong.frwallet.wallet.eth.eth

import java.io.Serializable

class QueryTransactionPageReq {
    var chainId:String = ""
    var fromAddress:String = ""
    var pageNumber:String = ""
    var pageSize:String = ""

    constructor(id:String,address:String,number:String,size:String){
        chainId = id
        fromAddress = address
        pageNumber = number
        pageSize = size
    }
}

class QueryTransactionPageResp{
    var code: Int = 0
    var msg:String = ""
    var data: Data? = null

    class Data {
        var totalPages:Int = 0
        var content: MutableList<transactionItem>? = null
    }

    class transactionItem: Serializable {
        var symbol:String = ""
        var amount:Float = 0f
        var chainId:Int = 0
        var createTime:String = ""
        var contract:String = ""
        var fromAddress:String = ""
        var id:Int = 0
        var toAddress:String = ""
        var txHash:String = ""
        //任务消息字段
        var context:String = ""
        var iconUrl:String = ""
        var title:String = ""
    }

}