package com.fanrong.frwallet.ui.receipt.viewmdel

import androidx.lifecycle.viewModelScope
import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.TransferFinishEvent
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.tools.extHexWei2Ten2EtherKeep4
import com.fanrong.frwallet.wallet.eth.EthSignUtils
import com.fanrong.frwallet.wallet.eth.eth.*
import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.greenrobot.eventbus.EventBus
import xc.common.framework.mvvm.OperationResult
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import java.math.BigDecimal

class ETHTransferViewmodel : TransferViewmodel() {

    override fun getGasPrice() {
        viewModelScope.tryLaunch {
            val gasResp = ethApi.susGasPrice(GasPriceReq()).body()
            SWLog.e(gasResp!!.result)
            newAndPostValue {
                this.gasInfoResult = OperationResult(GasInfoBean("0x5208", gasResp.result!!), true)
            }
        }
    }


    override fun getBalance(coin: CoinDao) {

        viewModelScope.tryLaunch {
            if (coin.contract_addr.checkNotEmpty()) {
                var balanceResult =
                    ethApi.susQueryEthTokenBalance(QueryEthTokenBalanceReq(coin.sourceAddr!!, coin.contract_addr!!)).body()!!
                coin.balance = balanceResult.getBalance().extHexWei2Ten2EtherKeep4()
            } else {
                var balanceResult = ethApi.susQueryEthBalance(QueryEthBalanceReq(coin.sourceAddr!!)).body()!!
                coin.balance = balanceResult.getBalance().extHexWei2Ten2EtherKeep4()
            }

            newAndPostValue {
                balanceResult = OperationResult(coin, true)
            }
        }

    }


    override fun transfer(tokenInfo: CoinDao, toAddr: String, amount: String, gas: GasInfoBean?) {

        postLoading(isShow = true)

//        ETHChainUtil
        val queryWallet = WalletOperator.queryWallet(tokenInfo)

        var nonce = "0"

        fun sendTransaction(sign: String) {
            viewModelScope.tryLaunch {
                var result = ethApi.susEthTransfer(EthTransferReq(sign)).body()!!
                val hash = result.getHash()

                if (hash.checkIsEmpty()) {
                    postErrorMsg(result.error?.message ?: "????????????")
                    return@tryLaunch
                }


                newAndPostValue {
                    transferResult = OperationResult(true, true)
                    transferInfoResult = OperationResult(hash, true)
                }
                postLoading(false)

                LocalNoceOperator.save(nonce, WalletOperator.queryWallet(tokenInfo), ChainNodeOperator.queryCurrentNode(tokenInfo.chain_name!!))

                TransferOperator.save(
                    tokenInfo,
                    toAddr,
                    hash,
                    nonce,
                    amount,
                    "",
                    System.currentTimeMillis().toString()
                )
                EventBus.getDefault().post(TransferFinishEvent())
            }
        }

        viewModelScope.tryLaunch {

            nonce = TransferUtils.getRealNonce(tokenInfo, ethApi.susGetNoce(GetNoceReq(queryWallet.address)).body()!!.getNoce())

            if (tokenInfo.contract_addr.checkIsEmpty()) {
                EthSignUtils.signEthTransfer(
                    "0x" + BytesHelper.bigDecimal2HexStr(BigDecimal(amount), 18),
                    queryWallet.address,
                    queryWallet.privateKey!!,
                    toAddr,
                    "0x" + BytesHelper.bigDecimal2HexStr(BigDecimal(gas!!.gasPrice), 0),
                    nonce
                ) {
                    SWLog.e(it)
                    sendTransaction("0x" + it)

                }
            } else {

                EthSignUtils.signEthTokenTransfer(
                    "0x" + BytesHelper.bigDecimal2HexStr(BigDecimal(amount), 18),
                    queryWallet.address,
                    queryWallet.privateKey!!,
                    toAddr,
                    tokenInfo.contract_addr!!,
                    "0x" + BytesHelper.bigDecimal2HexStr(BigDecimal(gas!!.gasPrice), 0),
                    nonce
                ) {
                    SWLog.e(it)
                    sendTransaction("0x" + it)
                }
            }

        }

    }

}