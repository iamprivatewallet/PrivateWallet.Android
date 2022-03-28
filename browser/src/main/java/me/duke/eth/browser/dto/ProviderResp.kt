package me.duke.eth.browser.dto

import androidx.annotation.Keep

/**
 * 通信响应实体类
 */
@Keep
data class ProviderResp<T>(
    val data:RpcResp<T>
):Event("provider")