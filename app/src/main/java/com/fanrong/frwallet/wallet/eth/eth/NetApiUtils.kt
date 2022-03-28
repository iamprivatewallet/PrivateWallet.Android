package com.fanrong.frwallet.wallet.eth.eth

object NetApiUtils {

    /**
     * H5 host
     */
    var H5_HOST = ""

    fun apiServiceIsSuccess(code: String?): Boolean {
        return "10000".equals(code)
    }


    fun isSuccess(code: String?): Boolean {
        return "1".equals(code)
    }

    fun isSuccess(code: Int?): Boolean {
        return 0 == code
    }


}