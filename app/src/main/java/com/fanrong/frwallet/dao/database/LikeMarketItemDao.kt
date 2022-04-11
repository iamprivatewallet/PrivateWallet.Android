package com.fanrong.frwallet.dao.database

import org.litepal.crud.LitePalSupport
import org.web3j.abi.datatypes.Bool

class LikeMarketItemDao : LitePalSupport() {
    var amout: Int? = null
    var createTime: Long? = null
    var id: Long = 0
    var last: Double? = null
    var lastVol: Long? = null
    var rose: Double? = null
    var status: Int? = null
    var symbol: String? = null
    var type: Int? = null
    var updateTime: Long? = null
    var isLike:Boolean? = false
}