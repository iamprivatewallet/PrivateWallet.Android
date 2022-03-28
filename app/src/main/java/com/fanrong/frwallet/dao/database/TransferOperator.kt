package com.fanrong.frwallet.dao.database

import com.fanrong.frwallet.dao.data.NodeList
import org.litepal.LitePal

object TransferOperator {
    fun save(
        tokenInfo: CoinDao, to: String, hash: String, nonce: String, amount: String, status: String, timestamp: String
    ) {
        save(tokenInfo.chain_name!!, tokenInfo.sourceAddr!!, tokenInfo.coin_name, to, hash, nonce, amount, status, timestamp)
    }

    fun save(
        chain_name: String,
        sourceAddr: String,
        coin_name: String,
        to: String,
        hash: String,
        nonce: String,
        amount: String,
        status: String,
        timestamp: String
    ) {
        val bean = TransferDao()

        if ("0x1".equals(status, ignoreCase = true)) {
            bean.status = "D"
        } else if ("0x0".equals(status, ignoreCase = true)) {
            bean.status = "E"
        } else {
            bean.status = "P"
        }

        bean.timestamp = timestamp
        bean.from = sourceAddr
        bean.uniqueToken = chain_name + "_" + sourceAddr + "_" + coin_name
        bean.to = to
        bean.transaction_hash = hash
        bean.direction = "out"
        bean.nonce = nonce
        bean.amount = amount
        bean.node = NodeList.getCurrentNode(chain_name)
//        bean.gasPrice = gasPrice
//        bean.gasUsed = gasLimit
        bean.save()

    }

    fun query(hash: String): List<TransferDao> {
        return LitePal.where("transaction_hash like ?", hash).find(TransferDao::class.java)
            ?: mutableListOf()
    }

    /**
     * 查询当前节点 下币交易数据
     */
    fun queryOrder(tokenInfo: CoinDao): List<TransferDao> {
        return LitePal.where(
            "uniqueToken = ? and node = ?", tokenInfo.getUniqueTokenInfo(), NodeList.getCurrentNode(tokenInfo.chain_name!!)
        )
            .find(TransferDao::class.java)
            ?: mutableListOf()
    }

    fun queryPendingOrder(): List<TransferDao> {
        return LitePal.where("status = ?", "P").find(TransferDao::class.java)
            ?: mutableListOf()
    }

    fun queryPendingOrder(tokenInfo: CoinDao): List<TransferDao> {
        return LitePal.where(
            "uniqueToken = ? and node = ? and status = ?",
            tokenInfo.getUniqueTokenInfo(),
            NodeList.getCurrentNode(tokenInfo.chain_name!!),
            "P"
        ).find(TransferDao::class.java)
            ?: mutableListOf()
    }


}