package com.fanrong.frwallet.wallet.eth.viewmodel

import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletDao
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel
import xc.common.framework.mvvm.OperationResult

open abstract class WalletViewmodel : BaseViewModel<WalletViewmodel.State>() {
    companion object {
        fun getViewmodel(wallet: WalletDao): WalletViewmodel {
            if ("CVN".equals(wallet.chainType)) {
                return CVNWalletViewmodel().apply {
                    this.walletModel = wallet
                }
            } else if("BSC".equals(wallet.chainType)) {
                return BSCWalletViewmodel().apply {
                    this.walletModel = wallet
                }
            }
            else if("HECO".equals(wallet.chainType)) {
                return HECOWalletViewmodel().apply {
                    this.walletModel = wallet
                }
            }
            else{
                return ETHWalletViewmodel().apply {
                    this.walletModel = wallet
                }
            }
        }
    }


    lateinit var walletModel: WalletDao

    class State : BaseLiveData() {
        var balanceResult: OperationResult<MutableList<CoinDao>>? = null
    }

    override fun createDefautState(): State {
        return State()
    }


    abstract fun getBalance(coin: MutableList<CoinDao>)

}