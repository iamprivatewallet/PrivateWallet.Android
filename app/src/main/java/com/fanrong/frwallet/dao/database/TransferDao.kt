package com.fanrong.frwallet.dao.database

import org.litepal.crud.LitePalSupport
import java.io.Serializable

class TransferDao : LitePalSupport(), Serializable {

    /**
     * 钱包 币种 交易
     *
     * chainname_addr_coinname
     */
    var uniqueToken: String = ""

    /**
     * p 打快中， e 交易失败 d 交易成功
     */
    var status: String? = null
    var timestamp: String? = null
    var from: String? = null
    var to: String? = null
    var transaction_hash: String? = null
    var direction: String? = null
    var nonce: String? = null
    var amount: String? = null

    /**
     * 10 进制 wei
     */
    var gasUsed: String? = null

    /**
     * 10 进制 wei
     */
    var gasPrice: String? = null

    /**
     * 交易是那个节点下的
     */
    var node: String? = null


    fun isTransOut(self: String): Boolean {
        return from.equals(self)
    }

}