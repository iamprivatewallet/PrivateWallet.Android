package com.fanrong.frwallet.wallet.eth.eth

class GetTransactionRecordReq {
    var address: String?=null
    var apikey: String?=null
    var contractAddress: String?=null
    var page: String?=null
    var offset: String="20"
    var type: String?=null
}

class GetTransactionRecordResp {

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
        var status: String? = null
        var message: String? = null
        var result: List<ResultBean>? = null

        class ResultBean {

            var blockNumber: String? = null
            var timeStamp: String? = null
            var hash: String? = null
            var nonce: String? = null
            var blockHash: String? = null
            var transactionIndex: String? = null
            var from: String? = null
            var to: String? = null
            var value: String? = null
            var gas: String? = null
            var gasPrice: String? = null
            var isError: String? = null
            var txreceipt_status: String? = null
            var input: String? = null
            var contractAddress: String? = null
            var cumulativeGasUsed: String? = null
            var gasUsed: String? = null
            var confirmations: String? = null
            var status: String="D"
            fun getOppositeAddr(self: String): String? {
                if (from.equals(self)) {
                    return to
                } else {
                    return from
                }
            }
            fun isTransOut(self:String): Boolean {
                return from.equals(self)
            }

        }
    }
}