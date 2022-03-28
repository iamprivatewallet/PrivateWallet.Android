package com.fanrong.frwallet.dao.database

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

class ChainNodeDao : LitePalSupport(), Serializable {

    var id: Long = 0
    var chainType: String? = null

//    @Column(unique = true)
    var nodeUrl: String? = null
    var nodeName: String? = null
    var chainId: String? = null
    var symbol: String? = null
    var browser: String? = null
    var isCurrent = 0
    var netType = 1


    @Column(ignore = true)
    var typeStr: String? = null
        get() {
            return when (netType) {
                1 -> {
                    return "主网络"
                }
                2 -> {
                    return "自定义网络"
                }
                1 -> {
                    return "测试网络"
                }
                else -> {
                    return "其他网络"
                }
            }
        }
}