package com.fanrong.frwallet.wallet.eth

import com.fanrong.frwallet.tools.CallJsCodeUtils
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.Web3JsUtils
import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.web3j.abi.FunctionEncoder
import org.web3j.abi.datatypes.Address
import org.web3j.abi.datatypes.Function
import org.web3j.abi.datatypes.generated.Uint256
import xc.common.tool.utils.SWLog
import xc.common.viewlib.BasicView
import java.math.BigInteger
import java.util.*

object EthSignUtils {
    var chain_Id = "3388"


    fun signStr(
        privateKey: String,
        data: String,
        callback: (signInfo: String) -> Unit
    ) {


        Web3JsUtils.signStr(data,privateKey){
            var signedMessage = CallJsCodeUtils.readStringJsValue(it)
            callback.invoke(signedMessage)
        }
    }
    //发送主链币
    fun signEthTransfer(
        transAmount: String,
        fromAddr: String,
        privateKey: String,
        toAddr: String,
        gas_price: String,
        nonce: String,
        callback: (signInfo: String) -> Unit
    ) {


        BasicView.mainHandler.post {


            val signParamter = CallJsCodeUtils.callParams()

            signParamter.chainId = CoinNameCheck.getCurrentNetID()//"56"//chain_Id
            signParamter.nonce = nonce
            signParamter.gasPrice = gas_price

            signParamter.to = toAddr
            signParamter.value = transAmount
            signParamter.data = "0x0"

            //BytesHelper.hexStr2BigDecimal

            SWLog.e("拦截器+++++++++>"+signParamter.toString())

            CallJsCodeUtils.ecHexSign_eth(signParamter, privateKey) { value ->
//                var signedMessage = CallJsCodeUtils.readStringJsValue(value)
                callback.invoke(value)
            }

        }

    }


    fun signEthTokenTransfer(
        transAmount: String,
        fromAddr: String,
        privateKey: String,
        toAddr: String,
        tokenAddr: String,
        gas_price: String,
        nonce: String,
        callback: ((signInfo: String) -> Unit)
    ) {
        val _value = BytesHelper.hexStr2BigDecimal(transAmount, 0, 0).toPlainString()

        val signParamter = CallJsCodeUtils.callParams()
        signParamter.chainId = CoinNameCheck.getCurrentNetID()//"56"//chain_Id
        signParamter.nonce = nonce
        signParamter.gasPrice = gas_price

        signParamter.to = tokenAddr
        signParamter.value = "0x0"


        // 封装转账交易
        val function = Function(
            "transfer",
            Arrays.asList(
                Address(toAddr),
                Uint256(BigInteger(_value))
            ), emptyList()
        )
        val data = FunctionEncoder.encode(function)
        signParamter.data = data
//        val valueSize = "0000000000000000000000000000000000000000000000000000000000000000"
//        signParamter.data = "0xa9059cbb000000000000000000000000" +
//                toAddr_ +
//                // 截掉 金额的长度 然后拼接上金额
//                valueSize.substring(0, valueSize.length - transAmount_.length) + transAmount_

        SWLog.e("拦截器+++++++++>"+signParamter.toString())

        CallJsCodeUtils.ecHexSign_Token(signParamter, privateKey) { value ->
//            var signedMessage = CallJsCodeUtils.readStringJsValue(value)
            callback.invoke(value)
        }
    }


}