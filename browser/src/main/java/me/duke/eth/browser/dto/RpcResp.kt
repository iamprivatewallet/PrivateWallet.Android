package me.duke.eth.browser.dto

import androidx.annotation.Keep

/**
 * rpc的响应
 */
@Keep
data class RpcResp<T>(
    val id: Long,
    val jsonrpc:String,
    val result:T?,
    val error:RpcError?,
    val rawResponse:String
)