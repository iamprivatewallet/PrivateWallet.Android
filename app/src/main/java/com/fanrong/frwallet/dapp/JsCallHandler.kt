package com.fanrong.frwallet.dapp

import android.annotation.SuppressLint
import android.webkit.WebView
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.dapp.dappapi.FuncHandler
import com.fanrong.frwallet.dapp.dappapi.bsc.DappBSCApi
import com.fanrong.frwallet.dapp.dappapi.cvn.DappCVNApi
import com.fanrong.frwallet.dapp.dappapi.cvn.DappParamsError
import com.fanrong.frwallet.dapp.dappapi.error.AccountNotFoundException
import com.fanrong.frwallet.dapp.dappapi.error.TokenNotFoundException
import xc.common.tool.utils.XcSingleten

@SuppressLint("StaticFieldLeak")
class JsCallHandler {

    companion object {
        val PROMISE_RESULT = "promise"

        val ERROR_USER_CANCEL = ErrorData(4001, "User rejected the request.")
        val ERROR_PARAMS_ERROR = ErrorData(4002, "params error")
        val ERROR_CALL_ERROR = ErrorData(4003, "method invoke error")
    }


    val funcs = mutableMapOf<String, FuncHandler>(
        Pair("eth_chainId", DappBSCApi.BscChainId()),
        Pair("eth_accounts", DappBSCApi.Eth_accounts()),
        Pair("eth_requestAccounts", DappBSCApi.Eth_requestAccounts()),
        Pair("eth_sendTransaction", DappBSCApi.Eth_sendTransaction()),
        Pair("personal_sign", DappBSCApi.PersonalSign()),

        Pair("cvn_eth_chainId", DappCVNApi.EthChainId()),
        Pair("cvn_eth_accounts", DappCVNApi.Eth_accounts()),
        Pair("cvn_eth_requestAccounts", DappCVNApi.Eth_requestAccounts()),
        Pair("cvn_eth_sendTransaction", DappCVNApi.Eth_sendTransaction()),
        Pair("cvn_personal_sign", DappCVNApi.PersonalSign()),

        Pair("app_isConnected", App_isConnected())
    )

    constructor(webview: WebView, activity: BaseActivity) {
    }

    fun apiCall(api: String, param: String?, activity: DappBrowserActivity): String {
        try {

            val get = funcs.get(api)
            if (get == null) {
                // get 为 null 调用 RPC 接口处理
                if (api.startsWith("cvn")) {
                    val rpcRequest = DappCVNApi.RPCRequest()
                    rpcRequest.method = api
                    var result = rpcRequest.excute(param).toString()
                    return result
                } else {
                    val rpcRequest = DappBSCApi.RPCRequest()
                    rpcRequest.method = api
                    var result = rpcRequest.excute(param).toString()
                    return result
                }
            } else {
                var result = get?.excute(param)
                return result.toString()
            }
        } catch (e: DappParamsError) {
            CallJsResult.toResult(DappBrowserActivity.webView, api, ERROR_PARAMS_ERROR.toString())
            return PROMISE_RESULT
        } catch (e: AccountNotFoundException) {
            CallJsResult.toResult(
                DappBrowserActivity.webView, api, ErrorData(4004, e.message!!).toString()
            )
            return PROMISE_RESULT
        } catch (e: TokenNotFoundException) {
            CallJsResult.toResult(
                DappBrowserActivity.webView, api, ErrorData(4005, e.message!!).toString()
            )
            return PROMISE_RESULT
        }
    }


    class App_isConnected() : FuncHandler {
        override var method: String = ""
        override fun excute(params: Any?): Any {
            return true
        }
    }


    /**
     * 错误说明
     */
    class ErrorData {
        var code: Int
        var message: String

        constructor(code: Int, message: String) {
            this.code = code
            this.message = message
        }

        override fun toString(): String {
            return XcSingleten.gson.toJson(this)
        }
    }
}