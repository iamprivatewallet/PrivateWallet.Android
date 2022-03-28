package com.fanrong.frwallet.ui.node

import org.json.JSONObject
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel
import xc.common.framework.mvvm.LiveDataErrorInfo
import xc.common.framework.mvvm.OperationResult
import xc.common.tool.utils.SWLog
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class NodeViewmodel : BaseViewModel<NodeViewmodel.State>() {
    class State : BaseLiveData() {
        var chainIdResult: OperationResult<Int>? = null
        var verifyResult: OperationResult<Boolean>? = null

    }

    override fun createDefautState(): State {
        return State()
    }


    fun verifyNode(chainType: String, url: String, chainIdInput: String) {

        postLoading(true)
        thread {

            try {
                if ("ETH".equals(chainType, false)
                    || "HECO".equals(chainType, false)
                    || "BSC".equals(chainType, false)
                ) {

                    var requestStr = "{\"jsonrpc\":\"2.0\",\"method\":\"net_version\",\"params\":[],\"id\":67}"
                    val openConnection = URL(url).openConnection() as HttpURLConnection
                    openConnection.connectTimeout = 15 * 1000
                    openConnection.readTimeout = 15 * 1000
                    openConnection.requestMethod = "POST"

                    openConnection.setDoInput(true)
                    openConnection.setDoOutput(false)
                    openConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8")
                    val outputStream = openConnection.outputStream
                    outputStream.write(requestStr.toByteArray())
                    openConnection.connect()

                    val responseCode = openConnection.getResponseCode()
                    val inputStream = openConnection.inputStream
                    val bufferedReader = BufferedReader(InputStreamReader(inputStream))
                    val readLines = bufferedReader.readLines()
                    var string = ""
                    for (readLine in readLines) {
                        string += readLine
                    }

                    SWLog.e(string)
                    val jsonObject = JSONObject(string)
                    val chainId = jsonObject.getInt("result")
                    newAndPostValue {
                        if (chainId.toString().equals(chainIdInput)) {
                            verifyResult = OperationResult(true, true)
                        } else {
                            errorinfo = LiveDataErrorInfo("输入错误，查询出 Chainid 是 ${chainId}")
                        }
                    }

                } else if ("CVN".equals(chainType, false)) {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        newAndPostValue {
                            verifyResult = OperationResult(true, true)
                        }
                    } else {
                        postErrorMsg("rpc 地址格式不对，请是用 http 或 https")
                    }
                }

            } catch (e: Exception) {
                e.printStackTrace()
                postErrorMsg("rpc地址不可用")

            }

        }
    }
}