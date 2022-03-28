package com.fanrong.frwallet.dao.database

import org.litepal.crud.LitePalSupport

class DappHistoryDao : LitePalSupport() {

    var id: Long = 0

    var url:String?=null
    var name: String? = null
    var icon: String? = null
    var des: String? = null

    var lastVisiteTime = ""

}