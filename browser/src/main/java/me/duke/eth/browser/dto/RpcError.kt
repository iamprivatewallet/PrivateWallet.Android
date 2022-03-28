package me.duke.eth.browser.dto

import androidx.annotation.Keep

/**
 * rpc发生错误，如签名发生错误
 */
@Keep
class RpcError(
    val code:Int,
    val message:String,
    val data:String
)