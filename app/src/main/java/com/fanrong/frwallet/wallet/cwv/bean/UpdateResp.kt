package com.fanrong.frwallet.wallet.cwv.bean

import java.io.Serializable

data class UpdateResp(
    var current_version: String
) : Serializable {
    var previous_version: String? = null
    var release_date: String? = null
    var download_url: String? = null
    var mandatory_update: String? = null
    var t: String? = null
    var p: List<String>? = null
    var li: List<String>? = null
}