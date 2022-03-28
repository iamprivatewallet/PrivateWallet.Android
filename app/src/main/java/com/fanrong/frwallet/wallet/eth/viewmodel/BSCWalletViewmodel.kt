package com.fanrong.frwallet.wallet.eth.viewmodel

import androidx.lifecycle.viewModelScope
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.extHexWei2Ten2EtherKeep2
import com.fanrong.frwallet.wallet.bsc.bscApi
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryEthBalanceReq
import com.fanrong.frwallet.wallet.eth.eth.QueryEthTokenBalanceReq
import com.fanrong.frwallet.wallet.eth.eth.QueryTokenPriceReq
import xc.common.framework.mvvm.OperationResult
import xc.common.tool.utils.checkNotEmpty

class BSCWalletViewmodel : WalletViewmodel() {


    override fun getBalance(coin: MutableList<CoinDao>) {

        viewModelScope.tryLaunch {
            for (coinBean in coin) {
                if (coinBean.contract_addr.checkNotEmpty()) {
                    var balanceResult =
                        bscApi.susQueryEthTokenBalance(QueryEthTokenBalanceReq(coinBean.sourceAddr!!, coinBean.contract_addr!!)).body()!!
                    coinBean.balance = balanceResult.getBalance().extHexWei2Ten2EtherKeep2()
                } else {
                    var balanceResult = bscApi.susQueryEthBalance(QueryEthBalanceReq(coinBean.sourceAddr!!)).body()!!
                    coinBean.balance = balanceResult.getBalance().extHexWei2Ten2EtherKeep2()
                }

                val resp = centerApi.queryTokenPrice2(QueryTokenPriceReq(CoinNameCheck.getNameByName(coinBean.coin_name))).body()
                coinBean.price = resp?.data!!
                if (coinBean.contract_addr.checkNotEmpty()){
                    coinBean.coin_icon = CoinNameCheck.getCoinImgUrl(coinBean.contract_addr!!)//"https://mos-wallet-public.oss-cn-hangzhou.aliyuncs.com/mos/BXH/picture/"+ CoinNameCheck.getNameByName(coinBean.coin_name)+".png"
                }else{
                    coinBean.coin_icon = CoinNameCheck.getCoinImgUrl("")
                }
            }


            newAndPostValue {
                balanceResult = OperationResult(coin, true)
            }
        }

    }
}