package com.fanrong.frwallet.tools

import android.webkit.ValueCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

object BrewChainJsUtils {

    fun exportKeystore(privateKey:String,password:String,callback: ValueCallback<String>){
        val script = String.format("wraperExportKeystore('%s','%s')", privateKey, password)
        CallJsCodeUtils.getJsHandler().evaluateJavascript(script, callback)
    }
    fun importKeystore(keystore:String,password: String,callback: ValueCallback<String>){
        val script = String.format("wraperImportKeystore('%s','%s')", keystore, password)
        CallJsCodeUtils.getJsHandler().evaluateJavascript(script, callback)
    }

    fun getTransactionC20Tx(privatekey: String?, to: String?, nonce: String?, contract: String?, amount: String?, callback: ValueCallback<String?>?) {
        val script = String.format("getTransactionC20Tx('%s','%s','%s','%s','%s')", privatekey, to, nonce, contract, amount)
        CallJsCodeUtils.getJsHandler().evaluateJavascript(script, callback)
    }

    suspend fun susGetTransactionC20Tx(
        privatekey: String?,
        to: String?,
        nonce: String?,
        contract: String?,
        amount: String?
    ): String? {
        var result: String? = null
        var job = GlobalScope.launch(Dispatchers.Main) {
            val script = String.format("getTransactionC20Tx('%s','%s','%s','%s','%s')", privatekey, to, nonce, contract, amount)
            CallJsCodeUtils.getJsHandler().evaluateJavascript(script, object : ValueCallback<String> {
                override fun onReceiveValue(value: String?) {
                    result = CallJsCodeUtils.readStringJsValue(value)?:""
                }
            })
            while (result == null) {
                delay(200)
            }
        }
        job.join()
        return result
    }
}