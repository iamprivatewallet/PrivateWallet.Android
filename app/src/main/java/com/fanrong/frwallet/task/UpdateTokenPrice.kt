package com.fanrong.frwallet.task

import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.CoinOperator
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import xc.common.tool.utils.checkNotEmpty
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object UpdateTokenPrice {
    lateinit var currentWallet: WalletDao
    var contractAsset: MutableList<CoinDao>? = null

    val runnable: Runnable = Runnable {

        try {
            currentWallet = WalletOperator.currentWallet!!//有chainType
            contractAsset = CoinOperator.queryContractAssetWithWallet(currentWallet) //币种的数组，contractAsset[0].coin_name


            if (contractAsset.checkNotEmpty()){
                for (item in contractAsset!!){

                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }





    fun start() {
        val newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        newSingleThreadScheduledExecutor.scheduleAtFixedRate(runnable, 1000, 2000, TimeUnit.MILLISECONDS)
    }
}