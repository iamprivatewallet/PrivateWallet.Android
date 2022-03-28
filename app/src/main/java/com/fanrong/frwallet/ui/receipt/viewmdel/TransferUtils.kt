package com.fanrong.frwallet.ui.receipt.viewmdel

import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.tools.extHexToTen
import com.fanrong.frwallet.tools.extTen2Hex
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.checkNotEmpty

object TransferUtils {
    /**
     * @param coinDao 币种信息;
     * @nonce 网络查询的nonce
     */
    fun getRealNonce(coinDao: CoinDao, nonce: String): String {
        var realNonce = nonce
        var pendingOrders = TransferOperator.queryPendingOrder(coinDao)
        if (pendingOrders.checkNotEmpty()) {
            var localNonce = LocalNoceOperator.getLocalNoce(
                WalletOperator.queryWallet(coinDao), ChainNodeOperator.queryCurrentNode(coinDao.chain_name!!)
            )?.nonce ?: "0x0"
            realNonce = (localNonce.extHexToTen().toInt() + 1).toString().extTen2Hex()
        }
        SWLog.e("realNonce ${realNonce}")

        return realNonce
    }
}