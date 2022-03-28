package com.fanrong.frwallet.wallet.eth.eth

import java.io.Serializable

class GetSysMsgReq {
    var address: String = "0e1a343dce06e6985ec7ced89c9c0387bc7b7aac"

}

class GetSysMsgResp {

    private var isok: Boolean? = null
    private var code: Int? = null
    private var message: String? = null
    private var data: DataBean? = null

    fun getIsok(): Boolean? {
        return isok
    }

    fun setIsok(isok: Boolean?) {
        this.isok = isok
    }

    fun getCode(): Int? {
        return code
    }

    fun setCode(code: Int?) {
        this.code = code
    }

    fun getMessage(): String? {
        return message
    }

    fun setMessage(message: String?) {
        this.message = message
    }

    fun getData(): DataBean? {
        return data
    }

    fun setData(data: DataBean?) {
        this.data = data
    }

    class DataBean {
        var records: List<RecordsBean>? = null
        var total: Int? = null
        var size: Int? = null
        var current: Int? = null
        var orders: List<*>? = null
        var optimizeCountSql: Boolean? = null
        var hitCount: Boolean? = null
        var countId: Any? = null
        var maxLimit: Any? = null
        var searchCount: Boolean? = null
        var pages: Int? = null

        class RecordsBean : Serializable {
            var id: String? = null
            var title: String? = null
            var iconUrl: Any? = null
            var context: String? = null
            var address: String? = null
            var createTime: String? = null
            var author: String? = null
        }
    }
}