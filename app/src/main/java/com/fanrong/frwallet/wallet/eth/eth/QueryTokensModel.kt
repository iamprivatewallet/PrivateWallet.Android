package com.fanrong.frwallet.wallet.eth.eth

import com.fanrong.frwallet.dao.database.ConfigTokenDao

class QueryTokensReq {
    var pageNum: String
    var pageSize: String

    constructor(pageNum: String, pageSize: String) {
        this.pageNum = pageNum
        this.pageSize = pageSize
    }
}


class QueryTokensResp {
    var code: Int = 0
    var data: Data? = null

    class Data {
        var records: MutableList<ConfigTokenDao>? = null
    }
}