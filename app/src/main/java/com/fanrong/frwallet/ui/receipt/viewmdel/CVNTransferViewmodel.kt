package com.fanrong.frwallet.ui.receipt.viewmdel

import android.util.Log
import android.webkit.ValueCallback
import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.wallet.cwv.BalanceResp
import com.fanrong.frwallet.wallet.cwv.SignTxReq
import com.fanrong.frwallet.wallet.cwv.SignTxResp
import com.fanrong.frwallet.wallet.cwv.api.CVNApi
import com.fanrong.frwallet.wallet.cwv.api.cvnInterfaceApi
import com.fanrong.frwallet.wallet.cwv.api.models.CVNGetBalanceReq
import com.fanrong.frwallet.wallet.cwv.api.models.CVNSendTxReq
import com.fanrong.frwallet.wallet.cwv.api.models.UserInfoModel
import com.fanrong.frwallet.wallet.cwv.http.RetrofitClient
import kotlinx.coroutines.GlobalScope
import org.brewchain.sdk.HiChain
import org.brewchain.sdk.util.AccountUtil
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import xc.common.framework.mvvm.OperationResult
import xc.common.tool.utils.XcSingleten
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty

class CVNTransferViewmodel : TransferViewmodel() {

    override fun getGasPrice() {
        newAndPostValue { }
    }


    override fun getBalance(coin: CoinDao) {

        GlobalScope.tryLaunch {
            if (coin.contract_addr.checkNotEmpty()) {
                val balanceResp = cvnInterfaceApi.susCall_getBalance(CVNGetBalanceReq(coin.contract_addr!!, coin.sourceAddr!!))
                coin.balance = balanceResp.body()!!.result.extHexWei2Ten2EtherKeep2()
            } else {
                coin.balance = CVNApi.susGetChainTokenBalance(coin.sourceAddr!!).balance.extHexWei2Ten2EtherKeep2()
            }

            newAndPostValue {
                balanceResult = OperationResult(coin, true)
            }
        }

    }

//    fun queryOrderDetail(hash: String, callback: (detail: TransactionRecordDetailResp?) -> Unit) {
//        RetrofitClient.getFBCNetWorkApi()
//            .orderDetail(ConvertToBody.ConvertToBody(TransactionRecordDetailReq(hash)))
//            .enqueue(object : Callback<TransactionRecordDetailResp> {
//                override fun onFailure(call: Call<TransactionRecordDetailResp>, t: Throwable) {
//                    callback(null)
//                }
//
//                override fun onResponse(call: Call<TransactionRecordDetailResp>
//                                        , response: Response<TransactionRecordDetailResp>) {
//                    callback(response.body())
//                }
//
//            })
//    }


    override fun transfer(tokenInfo: CoinDao, toAddr: String, amount: String, gas: GasInfoBean?) {

        var wallet = WalletOperator.queryWallet(tokenInfo)

        fun sendSignTx(tx: String,nonce:String,tokenInfo: CoinDao, toAddr: String, amount: String, gas: GasInfoBean?){
            RetrofitClient.getFbcNodeApi().sendSignTx(ConvertToBody.ConvertToBody(SignTxReq(tx)))
                .enqueue(object : Callback<SignTxResp> {
                    override fun onResponse(
                        call: Call<SignTxResp>,
                        response: Response<SignTxResp>
                    ) {
                        val transferTo = response.body()
                        saveTranction(transferTo?.hash,nonce,tokenInfo,toAddr,amount,gas)

//                        if (1 == transferTo?.retCode) {
//                            Thread.sleep(1100)
//                            LitePalTransferBeanOperator.save(
//                                from = withAddress.address,
//                                to = transferReq.to_addr,
//                                hash = transferTo.hash ?: "",
//                                nonce = nonce.toString(),
//                                quantity = transferReq.value,
//                                status = "D",
//                                timestamp = System.currentTimeMillis().toString()
//                            )
//                        }
//
//                        BasicLibComponant.postMainThread {
//                            if (1 == transferTo?.retCode) {
//                                callBack.success()
//                            } else {
//                                callBack.failed(transferTo?.retMsg ?: "未知异常")
//                            }
//                        }

                    }

                    override fun onFailure(call: Call<SignTxResp>, t: Throwable) {
                        Log.d("ddf", "onFailure: --->>>>>>"+call);
//                        postErrorMsg("转账失败")
//                        return@tryLaunch
                    }
                })
        }

        GlobalScope.tryLaunch {

            var addr = AccountUtil.cvnFiler(tokenInfo!!.sourceAddr)
            var accountInfoStr = HiChain.getUserInfo(tokenInfo!!.sourceAddr) ?: ""
            var balanceResp = NetTools.formatJson(accountInfoStr, BalanceResp::class.java)

            if (balanceResp == null || balanceResp.nonce == null){
                postErrorMsg("查询nonce失败")
                return@tryLaunch
            }

            var nonce_token = TransferUtils.getRealNonce(
                tokenInfo, XcSingleten.gson.fromJson(accountInfoStr, UserInfoModel::class.java).nonce!!.extTen2Hex()
            )
            var nonce = balanceResp.nonce?.toInt() ?: 0

//            val arrayListOf =
//                arrayListOf<TransferInfo>(
//                    TransferInfo(
//                        AccountUtil.cvnFiler(toAddr), amount.extEther2Wei()
//                    )
//                )

            var hash: String? = null
            if (tokenInfo.contract_addr.checkIsEmpty()) {
//                hash = HiChain.transferTo(
//                    addr, nonce.extHexToTen().toInt(), wallet!!.privateKey!!,
//                    "", arrayListOf
//                ).hash
                CallJsCodeUtils.signCwv(
                    wallet.privateKey,
                    nonce,
                    toAddr,
                    AccountUtil.multy18(amount.toString()),
                    object : ValueCallback<String> {
                        override fun onReceiveValue(value: String?) {
                            if (value == null || value.isEmpty()) {

                            } else {
                                //签名 Tx
                                val fromJson = NetTools.formatJson(value, SignTxReq::class.java)

//                                  val sign:SignTxReq = SignTxReq(value)

                                sendSignTx(fromJson?.tx!!,nonce.toString(),tokenInfo,toAddr,amount,gas)
//                                hash = fromJson?.tx!!
                            }
                        }
                    }
                )
            } else {
                var tx = BrewChainJsUtils.susGetTransactionC20Tx(
                    wallet.privateKey,
                    toAddr.removePrefix("CVN").removePrefix("0x"),
                    nonce_token.extHexToTen(),
                    tokenInfo.contract_addr,
                    amount.extEther2Wei()
                )
                if (tx.checkIsEmpty()) {
                    postErrorMsg("tx 生成错误")
                    return@tryLaunch
                }
                var resp = cvnInterfaceApi.call_sendTx(CVNSendTxReq(tx)).execute().body()
                hash = resp?.hash

                saveTranction(hash,nonce.toString(),tokenInfo,toAddr,amount,gas)
            }

//            if (hash.checkIsEmpty()) {
//                postErrorMsg("转账失败")
//                return@tryLaunch
//            } else {
//
//                LocalNoceOperator.save(nonce.toString(), WalletOperator.queryWallet(tokenInfo), ChainNodeOperator.queryCurrentNode(tokenInfo.chain_name!!))
//
//                TransferOperator.save(
//                    wallet.chainType!!,
//                    wallet.address,
//                    if (tokenInfo != null) tokenInfo.coin_name else "UNKNOW",
//                    toAddr,
//                    hash!!,
//                    nonce.toString(),
//                    amount,
//                    "",
//                    System.currentTimeMillis().toString()
//                )
//                newAndPostValue {
//                    this.transferResult = OperationResult(true, true)
//                    transferInfoResult = OperationResult(hash, true)
//                }
//            }
        }

    }


    fun saveTranction(hash:String?,nonce:String,tokenInfo: CoinDao, toAddr: String, amount: String, gas: GasInfoBean?){
        var wallet = WalletOperator.queryWallet(tokenInfo)
        GlobalScope.tryLaunch{
            if (hash.checkIsEmpty()) {
                postErrorMsg("转账失败")
                return@tryLaunch
            } else {

                LocalNoceOperator.save(nonce.toString(), WalletOperator.queryWallet(tokenInfo), ChainNodeOperator.queryCurrentNode(tokenInfo.chain_name!!))

                TransferOperator.save(
                    wallet.chainType!!,
                    wallet.address,
                    if (tokenInfo != null) tokenInfo.coin_name else "UNKNOW",
                    toAddr,
                    "0x"+hash!!,
                    nonce.toString(),
                    amount,
                    "",
                    (System.currentTimeMillis()*1000).toString()
                )
                newAndPostValue {
                    this.transferResult = OperationResult(true, true)
                    transferInfoResult = OperationResult(hash, true)
                }
            }
        }
    }

}