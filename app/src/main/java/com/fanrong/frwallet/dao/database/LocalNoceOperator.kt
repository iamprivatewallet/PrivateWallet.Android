package com.fanrong.frwallet.dao.database

import org.litepal.LitePal
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.extGet

object LocalNoceOperator {

    fun getLocalNoce(walletDao: WalletDao, nodeDao: ChainNodeDao): LocalNoceDao? {
        return LitePal.where("unique = ?", "${nodeDao.nodeUrl}_${walletDao.chainType}_${walletDao.address}")
            .find(LocalNoceDao::class.java).extGet(0)
    }

    /**
     * @param nonce 最后一次本地交易nonce
     */
    fun save(nonce: String, walletDao: WalletDao, nodeDao: ChainNodeDao) {
        getLocalNoce(walletDao, nodeDao)?.delete()
        LocalNoceDao().apply {
            this.nonce = nonce
            this.unique = "${nodeDao.nodeUrl}_${walletDao.chainType}_${walletDao.address}"
            save()
            SWLog.e("nonce:${nonce} unique:${unique}")
        }
    }
}