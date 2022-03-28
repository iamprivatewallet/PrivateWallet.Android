package com.fanrong.frwallet.dapp.dappapi.bsc

import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dapp.*
import com.fanrong.frwallet.dapp.dappapi.FuncHandler
import com.fanrong.frwallet.dapp.dappapi.cvn.DappParamsDecode
import com.fanrong.frwallet.dapp.dappapi.error.AccountNotFoundException
import com.fanrong.frwallet.tools.extHexToTen
import com.fanrong.frwallet.tools.extTen2Hex
import com.fanrong.frwallet.ui.receipt.viewmdel.TransferUtils
import com.fanrong.frwallet.view.DappSelectWalletListDialog
import com.fanrong.frwallet.wallet.bsc.bscApi
import com.fanrong.frwallet.wallet.eth.EthSignUtils
import com.fanrong.frwallet.wallet.eth.eth.*
import org.brewchain.core.crypto.cwv.util.BytesHelper
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.kotlinext.showToast
import xc.common.kotlinext.showToastAtMainThread
import xc.common.tool.CommonTool
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.XcSingleten
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.view.customview.FullScreenDialog
import kotlin.concurrent.thread

object DappBSCApi {

    open class BscChainId : FuncHandler {
        override var method: String = "eth_chainId"
        override fun excute(params: Any?): Any {

            val currentNode = NodeList.getCurrentNodeObj(WalletOperator.currentWallet!!.chainType!!)

            CommonTool.mainHandler.post {
                CallJsResult.toResult(DappBrowserActivity.webView, method, currentNode.chainId!!.extTen2Hex())
            }
            return JsCallHandler.PROMISE_RESULT
        }

    }

    open class PersonalSign : FuncHandler {
        override var method: String = "personal_sign"
        override fun excute(params: Any?): Any {
            val personalSign = DappParamsDecode.decode_personalSign(params)
            var addrInfo = WalletOperator.queryAddress(personalSign.from)

            if (addrInfo == null) {
                showToast("未添加该钱包 ${personalSign.from}")
                throw AccountNotFoundException()
            }

            CommonTool.mainHandler.post {
                EthSignUtils.signStr(addrInfo.privateKey!!, personalSign.content) { sign ->
                    CallJsResult.toResult(DappBrowserActivity.webView, method, sign)
                }
            }

            return JsCallHandler.PROMISE_RESULT
        }
    }

    open class RPCRequest : FuncHandler {
        override var method = ""
        override fun excute(params: Any?): Any {
            var _params = DappParamsDecode.decode_rpcRequest(params)

            bscApi.rpcRequest(RpcRequestReq(method, _params))
                .netSchduler()
                .subscribeObj(object : NetCallBack<RpcRequestResp> {
                    override fun onSuccess(t: RpcRequestResp) {
                        CallJsResult.toResult(DappBrowserActivity.webView, method, t.result.toString())
                    }

                    override fun onFailed(error: Throwable) {
                        CallJsResult.toResult(
                            DappBrowserActivity.webView,
                            method,
                            JsCallHandler.ERROR_CALL_ERROR.toString()
                        )
                    }
                })
            return JsCallHandler.PROMISE_RESULT
        }

    }


    open class Eth_accounts : FuncHandler {
        override var method: String = "eth_accounts"
        override fun excute(params: Any?): Any {

            fun showSelectAccount() {

                DappSelectWalletListDialog(DappBrowserActivity.activity).apply {
                    this.selectWalletType = "ETH HECO BSC"
                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            DappBrowserActivity.webView.reload()
                        }
                    }
                }.show()
            }

            DappBrowserActivity.activity.runOnUiThread {

                DappImpowerDialog(DappBrowserActivity.activity).apply {
                    url = DappBrowserActivity.webView.originalUrl
                    onCancel = object : FullScreenDialog.OnCancelListener {
                        override fun cancel() {
                            CallJsResult.toResult(
                                DappBrowserActivity.webView,
                                method,
                                JsCallHandler.ERROR_USER_CANCEL.toString()
                            )
                            dismiss()
                        }
                    }

                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            val address = WalletOperator.currentWallet!!.address
                            if (address.startsWith("CVN")) {
                                showToastAtMainThread("当前为CVN钱包 ，请切换至钱包")
                                showSelectAccount()
                            } else {
                                var addr = arrayOf(WalletOperator.currentWallet!!.address)
                                CallJsResult.toResult(DappBrowserActivity.webView, method, XcSingleten.gson.toJson(addr))

                            }
                            dismiss()
                        }
                    }
                }.show()
            }

            return JsCallHandler.PROMISE_RESULT
        }
    }

    open class Eth_requestAccounts : FuncHandler {
        override var method: String = "eth_requestAccounts"

        override fun excute(params: Any?): Any {

            fun showSelectAccount() {
                DappSelectWalletListDialog(DappBrowserActivity.activity).apply {
                    this.selectWalletType = "ETH HECO BSC"
                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            if ((params as WalletDao).address!!.startsWith("CVN")) {
                                showToastAtMainThread("当前为CVN钱包 ，请切换至钱包")
                                showSelectAccount()
                            } else {
                                DappBrowserActivity.webView.reload()
                            }
                        }
                    }
                }.show()
            }


            DappBrowserActivity.activity.runOnUiThread {
                DappImpowerDialog(DappBrowserActivity.activity).apply {
                    this.url = DappBrowserActivity.webView.originalUrl
                    onCancel = object : FullScreenDialog.OnCancelListener {
                        override fun cancel() {
                            CallJsResult.toResult(
                                DappBrowserActivity.webView,
                                method,
                                JsCallHandler.ERROR_USER_CANCEL.toString()
                            )
                            dismiss()
                        }
                    }

                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            val address = WalletOperator.currentWallet!!.address
                            if (address.startsWith("CVN")) {
                                showToastAtMainThread("当前为CVN钱包 ，请切换至钱包")
                                showSelectAccount()
                            } else {
                                var addr = arrayOf(WalletOperator.currentWallet!!.address)
                                CallJsResult.toResult(DappBrowserActivity.webView, method, XcSingleten.gson.toJson(addr))
                            }
                            dismiss()
                        }
                    }
                }.show()
            }

            return JsCallHandler.PROMISE_RESULT
        }
    }


    open class Eth_sendTransaction : FuncHandler {
        override var method: String = "eth_sendTransaction"

        override fun excute(params: Any?): Any {
            val transaction = DappParamsDecode.decode_sendTransaction(params)

            var addrInfo = WalletOperator.queryAddress(transaction.from)
            if (addrInfo == null) {
                throw AccountNotFoundException()
            }

            var tokenInfo: CoinDao? = CoinOperator.queryChainCoin(addrInfo!!)

            if (transaction.data.startsWith("0xa9059cbb000000000000000000000000")) {
                tokenInfo = CoinOperator.queryContractCoin(addrInfo!!, "0x" + transaction.data.subSequence(34, 34 + 40).toString())
                transaction.value = "0x" + transaction.data.subSequence(34 + 40, transaction.data.length).replaceFirst(Regex("0*"), "")
            }

            if (tokenInfo == null) {
                throw AccountNotFoundException()
            }

            transaction.gasPrice = bscApi.gasPrice(GasPriceReq()).execute().body()!!.getGasPrice()
            transaction.nonce = TransferUtils.getRealNonce(tokenInfo!!,bscApi.getNoce(GetNoceReq(transaction.from)).execute().body()!!.getNoce())

            /**
             * 签名发交易
             */
            fun sendTransfer(sign: String?) {
                SWLog.e(sign)
                thread {
                    val hash = bscApi.ethTransfer(EthTransferReq("0x" + sign)).execute().body()!!.getHash()

                    LocalNoceOperator.save(transaction.nonce, WalletOperator.queryWallet(tokenInfo), ChainNodeOperator.queryCurrentNode(tokenInfo.chain_name!!))

                    TransferOperator.save(
                        addrInfo.chainType!!,
                        transaction.from,
                        if (tokenInfo != null) tokenInfo.coin_name else "UNKNOW",
                        transaction.to,
                        hash,
                        transaction.nonce,
                        BytesHelper.hexStr2BigDecimal(transaction.value, 18, 4).toPlainString(),
                        "",
                        System.currentTimeMillis().toString()
                    )
                    CommonTool.mainHandler.post {
                        CallJsResult.toResult(DappBrowserActivity.webView, method, hash)
                    }

                    DappBrowserActivity.activity.extShowOrDismissDialog(false)
                }
            }

            fun signAndTrans() {
                CommonTool.mainHandler.post {
                    DappBrowserActivity.activity.extShowOrDismissDialog(true)
                    if (transaction.data.startsWith("0xa9059cbb000000000000000000000000")) {
                        EthSignUtils.signEthTokenTransfer(
                            transAmount = "0x" + transaction.data.subSequence(34 + 40, transaction.data.length).replaceFirst(Regex("0*"), ""),
                            fromAddr = addrInfo!!.address,
                            privateKey = addrInfo!!.privateKey!!,
                            toAddr = transaction.to,
                            tokenAddr = "0x" + transaction.data.subSequence(34, 34 + 40).toString(),
                            gas_price = transaction.gasPrice,
                            nonce = transaction.nonce,
                            callback = ::sendTransfer
                        )
                    } else {
                        EthSignUtils.signEthTransfer(
                            transAmount = transaction.value,
                            fromAddr = addrInfo!!.address,
                            toAddr = transaction.to,
                            privateKey = addrInfo!!.privateKey!!,
                            gas_price = transaction.gasPrice,
                            nonce = transaction.nonce,
                            callback = ::sendTransfer
                        )
                    }
                }
            }

            CommonTool.mainHandler.post {
                DappTransferDialog(DappBrowserActivity.webView.context).apply {
                    dappUrl = DappBrowserActivity.webView.originalUrl
                    walletInfo = addrInfo
                    this.tokenInfo = tokenInfo

                    this.isShowNode = false
                    this.receiptAddr = transaction.to
                    this.payAddr = addrInfo!!.address
                    this.gasLimit = "21000"
                    this.gasPrice = transaction.gasPrice.extHexToTen()
                    if (transaction.value == "" || transaction.value == null){
                        transaction.value = "0"
                    }

                    this.payAMount = BytesHelper.hexStr2BigDecimal(transaction.value, 18, 4).toPlainString()

                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            signAndTrans()
                        }
                    }

                    onCancel = object : FullScreenDialog.OnCancelListener {
                        override fun cancel() {
                            CallJsResult.toResult(
                                DappBrowserActivity.webView, method, JsCallHandler.ERROR_USER_CANCEL.toString()
                            )
                        }
                    }

                }.show()
            }

            return JsCallHandler.PROMISE_RESULT
        }

    }

}