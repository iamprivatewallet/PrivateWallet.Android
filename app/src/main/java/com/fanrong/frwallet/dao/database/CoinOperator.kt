package com.fanrong.frwallet.dao.database

import org.litepal.LitePal

object CoinOperator {

    fun queryChainCoin(wallet: WalletDao): CoinDao {
        return LitePal.where("sourceWallet = ? and coin_name = ?", "${wallet.chainType}_${wallet.address}", wallet.chainType)
            .find(CoinDao::class.java)[0]
    }

    fun queryContractCoin(wallet: WalletDao, contactAddr: String): CoinDao? {
        var coins = LitePal.where("sourceWallet = ?", "${wallet.chainType}_${wallet.address}")
            .find(CoinDao::class.java)
        for (coin in coins) {
            if (coin.contract_addr.equals(contactAddr)) {
                return coin
            }
        }
        return null
    }

    fun queryContractAssetWithWallet(wallet: WalletDao?): MutableList<CoinDao>? {
        if (wallet == null) {
            return mutableListOf()
        }
        return LitePal.where("sourceWallet = ?", "${wallet.chainType}_${wallet.address}")
            .find(CoinDao::class.java)
    }

    fun addContractAsset(wallet: WalletDao, coin: CoinDao) {

        var liteCoinBeanModel = CoinDao(coin.coin_name)
        liteCoinBeanModel.contract_addr = coin.contract_addr
        liteCoinBeanModel.coin_decimals = coin.coin_decimals
        liteCoinBeanModel.sourceAddr = wallet.address
        liteCoinBeanModel.sourceWallet = wallet.chainType + "_" + wallet.address
        liteCoinBeanModel.save()
    }

    fun addContractAsset(wallet: WalletDao, coin: ConfigTokenDao) {
        var liteCoinBeanModel = CoinDao(coin.tokenSymbol!!)
        liteCoinBeanModel.coin_decimals = coin.tokenDecimals
        liteCoinBeanModel.contract_addr = coin.tokenContract
        liteCoinBeanModel.sourceWallet = wallet.chainType + "_" + wallet.address
        liteCoinBeanModel.sourceAddr = wallet.address
        liteCoinBeanModel.save()
    }

    fun deleteCoin(wallet: WalletDao, item: CoinDao?) {
        LitePal.delete(CoinDao::class.java, item!!.id!!)
    }

    fun saveSortCoins(wallet: WalletDao?, sort: MutableList<CoinDao>?) {
        if (wallet == null) {
            return
        }
        LitePal.deleteAll(CoinDao::class.java, "sourceWallet = ?", "${wallet.chainType}_${wallet.address}")
        if (sort != null) {
            for (coin in sort) {
                var liteCoinBeanModel = CoinDao(coin.coin_name)
                liteCoinBeanModel.contract_addr = coin.contract_addr
                liteCoinBeanModel.chain_name = coin.chain_name
                liteCoinBeanModel.coin_icon = coin.coin_icon
                liteCoinBeanModel.balance = coin.balance
                liteCoinBeanModel.sourceAddr = coin.sourceAddr
                liteCoinBeanModel.sourceWallet = coin.sourceWallet
                addContractAsset(wallet, liteCoinBeanModel)
            }
        }
//        LitePal.saveAll(sort)
    }
}