package me.duke.eth.browser.dto

import androidx.annotation.Keep

/**
 * 账号/状态/网络发生变化的实体类
 */
@Keep
data class PushConfig(
    val isUnlocked: Boolean,
    val isEnabled: Boolean,
    val selectedAddress: String,
    val networkVersion: String,
    val chainId: String
)