package com.fanrong.frwallet.ui.receipt.viewmdel

import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletDao
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel
import xc.common.framework.mvvm.OperationResult
import xc.common.kotlinext.showToast

abstract class TransferViewmodel : BaseViewModel<TransferViewmodel.State>() {

    companion object {
        fun getViewmodel(walletModel: CoinDao): TransferViewmodel {
            if ("CVN".equals(walletModel.chain_name)) {
                return CVNTransferViewmodel()
            } else if ("ETH".equals(walletModel.chain_name)) {
                return ETHTransferViewmodel()
            } else if ("HECO".equals(walletModel.chain_name)) {
                return HECOTransferViewmodel()
            } else if ("BSC".equals(walletModel.chain_name)) {
                return BSCTransferViewmodel()
            } else {
                showToast("不支持主链 ${walletModel.chain_name} 查询，要抛异常了")
                throw RuntimeException("不支持主链")
            }
        }
        fun getViewmodel(walletModel: WalletDao): TransferViewmodel {
            if ("CVN".equals(walletModel.chainType)) {
                return CVNTransferViewmodel()
            } else if ("ETH".equals(walletModel.chainType)) {
                return ETHTransferViewmodel()
            }  else if ("HECO".equals(walletModel.chainType)) {
                return HECOTransferViewmodel()
            } else if ("BSC".equals(walletModel.chainType)) {
                return BSCTransferViewmodel()
            }else {
                showToast("不支持主链 ${walletModel.chainType} 查询，要抛异常了")
                throw RuntimeException("不支持主链")
            }
        }
    }

    class State : BaseLiveData() {
        var gasInfoResult: OperationResult<GasInfoBean>? = null
        var transferResult: OperationResult<Boolean>? = null
        var transferInfoResult: OperationResult<String>? = null
        var balanceResult: OperationResult<CoinDao>? = null
    }

    override fun createDefautState(): State {
        return State()
    }

    abstract fun getGasPrice()
    abstract fun getBalance(coin: CoinDao)
    abstract fun transfer(tokenInfo: CoinDao, toAddr: String, amount: String, gas: GasInfoBean?)
}