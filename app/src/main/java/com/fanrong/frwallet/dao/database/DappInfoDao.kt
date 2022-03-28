package com.fanrong.frwallet.dao.database

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

class DappInfoDao : LitePalSupport(), Serializable {

    var id: Long = 0

    @Column(unique = true)
    var url: String? = null
    var name: String? = null
    var icon: String? = null
    var des: String? = null
    var isShowHint = "0"
}