package com.fanrong.frwallet.dapp.dappapi.cvn

import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dapp.*
import com.fanrong.frwallet.dapp.dappapi.FuncHandler
import com.fanrong.frwallet.dapp.dappapi.error.AccountNotFoundException
import com.fanrong.frwallet.dapp.dappapi.error.TokenNotFoundException
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.tools.BrewChainJsUtils
import com.fanrong.frwallet.tools.extHexToTen
import com.fanrong.frwallet.tools.extTen2Hex
import com.fanrong.frwallet.ui.receipt.viewmdel.TransferUtils
import com.fanrong.frwallet.view.DappSelectWalletListDialog
import com.fanrong.frwallet.wallet.cwv.CVNSignUtils
import com.fanrong.frwallet.wallet.cwv.api.CVNApi
import com.fanrong.frwallet.wallet.cwv.api.cvnInterfaceApi
import com.fanrong.frwallet.wallet.cwv.api.models.CVNCallReq
import com.fanrong.frwallet.wallet.cwv.api.models.CVNCallResp
import com.fanrong.frwallet.wallet.cwv.api.models.CVNSendTxReq
import com.fanrong.frwallet.wallet.cwv.api.models.UserInfoModel
import com.fanrong.frwallet.wallet.eth.eth.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.brewchain.sdk.HiChain
import org.brewchain.sdk.model.TransferInfo
import org.brewchain.sdk.util.AccountUtil
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.kotlinext.showToastAtMainThread
import xc.common.tool.CommonTool
import xc.common.tool.utils.XcSingleten
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.view.customview.FullScreenDialog
import kotlin.concurrent.thread

object DappCVNApi {

    open class EthChainId : FuncHandler {
        override var method: String = "cvn_eth_chainId"
        override fun excute(params: Any?): Any {

            val currentNode = NodeList.getCurrentNodeObj(WalletOperator.currentWallet!!.chainType!!)

            CommonTool.mainHandler.post {
                CallJsResult.toResult(DappBrowserActivity.webView, method, currentNode.chainId!!.extTen2Hex())
            }
            return JsCallHandler.PROMISE_RESULT
        }

    }

    /**
     * 字符串签名处理
     *
     */
    open class PersonalSign : FuncHandler {

        override var method = "cvn_personal_sign"

        override fun excute(params: Any?): Any {
            var _params = DappParamsDecode.decode_personalSign(params)
            var addrInfo = WalletOperator.queryAddress(_params.from)

            if (addrInfo == null) {
                throw AccountNotFoundException()
            }

            CVNSignUtils.signStr(addrInfo.privateKey!!, _params.content) {
                if (it.checkIsEmpty()) {
                    CallJsResult.toResult(
                        DappBrowserActivity.webView,
                        method,
                        JsCallHandler.ERROR_CALL_ERROR.toString()
                    )
                } else {
                    CallJsResult.toResult(DappBrowserActivity.webView, method, it)
                }
            }
            return JsCallHandler.PROMISE_RESULT
        }

    }

    open class RPCRequest : FuncHandler {

        override var method = ""

        override fun excute(params: Any?): Any {

            if (method.equals("cvn_eth_getBalance")) {
                var _params = DappParamsDecode.decode_getBalance(params)
                // 查询余额
                thread {
                    val balance = CVNApi.getChainTokenBalance(_params.addr).balance
                    CallJsResult.toResult(
                        DappBrowserActivity.webView, method, balance!!
                    )
                }

            } else if (method.equals("cvn_eth_call")) {
                var _params = DappParamsDecode.decode_ethCall(params)

                cvnInterfaceApi.call(CVNCallReq().apply {
                    this.data = _params.data
                    this.to = _params.to
                }).netSchduler()
                    .subscribeObj(object : NetCallBack<CVNCallResp> {
                        override fun onSuccess(t: CVNCallResp) {
                            CallJsResult.toResult(DappBrowserActivity.webView, method, t.result!!)
                        }

                        override fun onFailed(error: Throwable) {
                            CallJsResult.toResult(DappBrowserActivity.webView, method, JsCallHandler.ERROR_CALL_ERROR.toString())
                        }
                    })

            } else {

                var _params = DappParamsDecode.decode_rpcRequest(params)

                ethApi.rpcRequest(RpcRequestReq(method, _params))
                    .netSchduler()
                    .subscribeObj(object : NetCallBack<RpcRequestResp> {
                        override fun onSuccess(t: RpcRequestResp) {
                            CallJsResult.toResult(DappBrowserActivity.webView, method, t.result.toString())
                        }

                        override fun onFailed(error: Throwable) {

                            CallJsResult.toResult(
                                DappBrowserActivity.webView,
                                method,
                                JsCallHandler.ErrorData(4002, "method call error").toString()
                            )
                        }
                    })
            }

            return JsCallHandler.PROMISE_RESULT

        }
    }


    open class Eth_accounts : FuncHandler {
        override var method: String = "cvn_eth_accounts"
        override fun excute(params: Any?): Any {

            fun showSelectAccount() {
                DappSelectWalletListDialog(DappBrowserActivity.activity).apply {
                    this.selectWalletType = "CVN"
                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            DappBrowserActivity.webView.reload()
                            dismiss()
                        }
                    }
                }.show()
            }

            DappBrowserActivity.activity.runOnUiThread {

                DappImpowerDialog(DappBrowserActivity.activity).apply {
                    var impowerDialog = this
                    url = DappBrowserActivity.webView.originalUrl
                    onCancel = object : FullScreenDialog.OnCancelListener {
                        override fun cancel() {
                            CallJsResult.toResult(
                                DappBrowserActivity.webView,
                                this@Eth_accounts.method,
                                JsCallHandler.ERROR_USER_CANCEL.toString()
                            )
                            dismiss()
                        }
                    }

                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            if (!"CVN".equals(WalletOperator.currentWallet!!.chainType)) {
                                showToastAtMainThread("请切换至CVN钱包")
                                showSelectAccount()
                            } else {
                                var addr = arrayOf(WalletOperator.currentWallet!!.address)
                                CallJsResult.toResult(DappBrowserActivity.webView, this@Eth_accounts.method, XcSingleten.gson.toJson(addr))
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
        override var method = "cvn_eth_requestAccounts"

        override fun excute(params: Any?): Any {


            fun showSelectAccount() {
                DappSelectWalletListDialog(DappBrowserActivity.activity).apply {
                    this.selectWalletType = "CVN"
                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            DappBrowserActivity.webView.reload()
                            dismiss()
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
                            if (!"CVN".equals(WalletOperator.currentWallet!!.chainType)) {
                                showToastAtMainThread("请切换至CVN钱包")
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

        override var method = "cvn_eth_sendTransaction"


        override fun excute(params: Any?): Any {

            val transaction = DappParamsDecode.decode_sendTransaction(params)


            var addrInfo = WalletOperator.queryAddress(transaction.from)
            if (addrInfo == null) {
                throw AccountNotFoundException()
            }

            var tokenInfo: CoinDao? = CoinOperator.queryChainCoin(addrInfo!!)

            // 如果是代币转账 解析 data 参数
            if (transaction.data.startsWith("0xa9059cbb000000000000000000000000")) {
                tokenInfo = CoinOperator.queryContractCoin(addrInfo!!, transaction.to)
                transaction.to = "0x" + transaction.data.subSequence(34, 34 + 40).toString()
                transaction.value = "0x" + transaction.data.subSequence(34 + 40, transaction.data.length).replaceFirst(Regex("0*"), "")
            }

            if (tokenInfo == null) {
                throw TokenNotFoundException()
            }

            /**
             * 转账签名
             */
            fun signAndTrans() {
                DappBrowserActivity.activity.extShowOrDismissDialog(true)

                GlobalScope.tryLaunch(Dispatchers.IO) {
                    var addr = AccountUtil.cvnFiler(addrInfo!!.address)
                    var accountInfoStr = HiChain.getUserInfo(addr) ?: ""
                    var balanceResp = XcSingleten.gson.fromJson(accountInfoStr, UserInfoModel::class.java)

                    if (balanceResp == null || balanceResp.nonce == null) {
                        showToastAtMainThread("查询nonce错误")
                        return@tryLaunch
                    }

                    // Hex str
                    balanceResp.nonce =
                        TransferUtils.getRealNonce(tokenInfo!!, CVNApi.getChainTokenBalance(tokenInfo!!.sourceAddr!!).nonce.extTen2Hex())

                    val arrayListOf =
                        arrayListOf<TransferInfo>(
                            TransferInfo(
                                AccountUtil.cvnFiler(transaction.to), BytesHelper.hexStr2BigDecimal(transaction.value, 0, 0).toString()
                            )
                        )

                    var resultHash: String

                    if (transaction.data.startsWith("0xa9059cbb000000000000000000000000")) {

                        var tx = BrewChainJsUtils.susGetTransactionC20Tx(
                            addrInfo.privateKey,
                            addr.removePrefix("CVN").removePrefix("0x"),
                            balanceResp.nonce.extHexToTen(),
                            tokenInfo.contract_addr,
                            transaction.value.extHexToTen()
                        )
                        resultHash = cvnInterfaceApi.call_sendTx(CVNSendTxReq(tx)).execute().body()!!.hash!!

                    } else {
                        resultHash = HiChain.transferTo(
                            addr, balanceResp.nonce!!.extHexToTen().toInt(), addrInfo!!.privateKey!!,
                            "", arrayListOf
                        ).hash
                    }

                    DappBrowserActivity.activity.extShowOrDismissDialog(false)
                    if (resultHash.checkIsEmpty()) {
                        CommonTool.mainHandler.post {
                            CallJsResult.toResult(
                                DappBrowserActivity.webView,
                                method,
                                JsCallHandler.ErrorData(4001, "转账错误").toString()
                            )
                        }
                    } else {

                        LocalNoceOperator.save(balanceResp.nonce!!, WalletOperator.queryWallet(tokenInfo), ChainNodeOperator.queryCurrentNode(tokenInfo.chain_name!!))

                        TransferOperator.save(
                            addrInfo.chainType!!,
                            transaction.from,
                            if (tokenInfo != null) tokenInfo.coin_name else "UNKNOW",
                            transaction.to,
                            resultHash,
                            balanceResp.nonce!!,
                            BytesHelper.hexStr2BigDecimal(transaction.value, 18, 4).toPlainString(),
                            "",
                            System.currentTimeMillis().toString()
                        )

                        CommonTool.mainHandler.post {
                            CallJsResult.toResult(DappBrowserActivity.webView, method, resultHash)
                        }
                    }

                }
            }

            /**
             * 转账确认框
             */
            CommonTool.mainHandler.post {
                DappTransferDialog(DappBrowserActivity.webView.context).apply {
                    dappUrl = DappBrowserActivity.webView.originalUrl
                    walletInfo = addrInfo
                    this.tokenInfo = tokenInfo
                    this.isShowNode = false
                    this.receiptAddr = transaction.to
                    this.payAddr = addrInfo!!.address

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