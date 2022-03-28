package com.fanrong.frwallet.dao.database

import org.litepal.crud.LitePalSupport

class SearchHistoryResult: LitePalSupport() {
    var id: Long = 0

    var tokenContract: String? = null
    var tokenName: String? = null
    var tokenSymbol: String? = null
    var tokenDecimals: String? = null
    var tokenChain: String? = null
    var tokenLogo: String? = null
    var createTime: String? = null
    var hotTokens: String? = null
}