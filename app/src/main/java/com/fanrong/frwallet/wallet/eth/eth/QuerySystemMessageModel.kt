package com.fanrong.frwallet.wallet.eth.eth

class QuerySystemMessageReq {
    var pageNumber:String = ""
    var pageSize:String = ""

    constructor(number:String,size:String){
        pageNumber = number
        pageSize = size
    }
}

class QuerySystemMessageResp {

}