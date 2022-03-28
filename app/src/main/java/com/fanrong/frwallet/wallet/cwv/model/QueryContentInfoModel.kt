package com.fanrong.frwallet.wallet.cwv.model

class QueryContentInfoReq {
    var search_content: String? = null

    constructor(search_content: String?) {
        this.search_content = search_content
    }
}

class QueryContentInfoResp {
    /**
     * 搜索结果类型:0-未找到搜索内容,1-区块信息,2-交易信息,3-地址信息
     */
    val result_type: String? = null

}