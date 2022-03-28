package com.fanrong.frwallet.wallet.eth.eth

class QueryDappReq {

    var firType: String? = null
    var sedType = ""
    var str = ""
    var pageNum = 1
    var pageSize = 10

}


class QueryDappResp {
    var code: String? = null
    var isok: String? = null
    var message: String? = null
    var data: Data? = null


    class Data {
        var records: MutableList<Record>? = null


    }

    class Record {
        var appName: String? = null
        var description: String? = null
        var iconUrl: String? = null
        var appUrl: String? = null
        var firType: String? = null
        var secType: String? = null

    }

}