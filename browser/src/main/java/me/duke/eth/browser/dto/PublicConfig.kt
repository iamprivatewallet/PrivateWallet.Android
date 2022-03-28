package me.duke.eth.browser.dto

import androidx.annotation.Keep

@Keep
class PublicConfig(
    val data: PushConfig
) : Event("publicConfig")
