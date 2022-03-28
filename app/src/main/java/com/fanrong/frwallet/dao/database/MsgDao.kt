package com.fanrong.frwallet.dao.database

import org.litepal.crud.LitePalSupport
import java.io.Serializable

class MsgDao() : LitePalSupport(), Serializable {

    var id: Long = 0
    var title: String? = null
    var time: String? = null
    var body: String? = null
    var isRead: Boolean = false

}