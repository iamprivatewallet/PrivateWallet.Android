package com.fanrong.frwallet.ui.contract.custom

import androidx.lifecycle.viewModelScope
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.tools.CallJsCodeUtils
import com.fanrong.frwallet.tools.Web3JsUtils
import com.fanrong.frwallet.wallet.bsc.bscApi
import com.fanrong.frwallet.wallet.cwv.api.cvnInterfaceApi
import com.fanrong.frwallet.wallet.cwv.api.models.CVNCallReq
import com.fanrong.frwallet.wallet.eth.eth.CallReq
import com.fanrong.frwallet.wallet.eth.eth.ethApi
import com.fanrong.frwallet.wallet.heco.hecoApi
import kotlinx.android.synthetic.main.activity_custom_tokens.*
import kotlinx.coroutines.*
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel
import xc.common.framework.mvvm.OperationResult
import xc.common.tool.utils.checkIsEmpty

class TokenViewModel : BaseViewModel<TokenViewModel.State>() {
    class State : BaseLiveData() {
        var symbolResult: OperationResult<String>? = null
        var decimalResult: OperationResult<String>? = null
    }

    override fun createDefautState(): State {
        return State()
    }

    fun getTokenInfo(wallet: WalletDao, contractAddr: String) {

        newAndPostValue {
            isShowLoading = true
        }
        viewModelScope.tryLaunch(Dispatchers.IO) {
            var symbolAbi = "0x95d89b41"
            var decimalAbi = "0x313ce567"

            var symbolResp: String? = null
            var decimalResp: String? = null

            if ("ETH".equals(wallet.chainType)) {
                symbolResp = ethApi.susCall(CallReq(CallReq.ReqInfo().apply {
                    to = contractAddr
                    data = symbolAbi
                })).body()?.result

                decimalResp = ethApi.susCall(CallReq(CallReq.ReqInfo().apply {
                    to = contractAddr
                    data = decimalAbi
                })).body()?.result

            } else if ("HECO".equals(wallet.chainType)) {
                symbolResp = hecoApi.susCall(CallReq(CallReq.ReqInfo().apply {
                    to = contractAddr
                    data = symbolAbi
                })).body()?.result

                decimalResp = hecoApi.susCall(CallReq(CallReq.ReqInfo().apply {
                    to = contractAddr
                    data = decimalAbi
                })).body()?.result

            } else if ("BSC".equals(wallet.chainType)) {
                symbolResp = bscApi.susCall(CallReq(CallReq.ReqInfo().apply {
                    to = contractAddr
                    data = symbolAbi
                })).body()?.result

                decimalResp = bscApi.susCall(CallReq(CallReq.ReqInfo().apply {
                    to = contractAddr
                    data = decimalAbi
                })).body()?.result

            } else if ("CVN".equals(wallet.chainType)) {
                symbolResp = cvnInterfaceApi.susCall(CVNCallReq().apply {
                    to = contractAddr
                    data = symbolAbi
                }).body()?.result

                decimalResp = cvnInterfaceApi.susCall(CVNCallReq().apply {
                    to = contractAddr
                    data = decimalAbi
                }).body()?.result


            }

            if (symbolResp.checkIsEmpty()) {
                postErrorMsg("查询 symbol 失败")
                return@tryLaunch
            }
            if (decimalResp.checkIsEmpty()) {
                postErrorMsg("查询 decimal 失败")
                return@tryLaunch
            }

            var symbol: String? = null
            GlobalScope.tryLaunch(Dispatchers.Main) {
                Web3JsUtils.contractTokenDecode(symbolResp) {
                    symbol = CallJsCodeUtils.readStringJsValue(it) ?: ""
                }
            }
            while (symbol == null) {
                delay(200)
            }
            if (symbol.checkIsEmpty()) {
                postErrorMsg("解析 symbol 失败")
                return@tryLaunch
            }

            var decimal: String? = null
            GlobalScope.tryLaunch(Dispatchers.Main) {
                Web3JsUtils.getDecimalsResultDecode(decimalResp) {
                    decimal = CallJsCodeUtils.readStringJsValue(it) ?: ""
                }
            }
            while (decimal == null) {
                delay(200)
            }
            if (decimal.checkIsEmpty()) {
                postErrorMsg("解析 decimal 失败")
                return@tryLaunch
            }

            newAndPostValue {
                symbolResult = OperationResult(symbol, true)
                decimalResult = OperationResult(decimal, true)
            }

        }

    }
}