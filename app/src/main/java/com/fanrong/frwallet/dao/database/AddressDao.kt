package com.fanrong.frwallet.dao.database

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

class AddressDao : LitePalSupport(), Serializable {

    var id: Long = 0
    var name: String? = ""
    var address: String? = ""
    var remark: String? = ""
    var type: String? = null

    @Column(unique = true)
    var uniqueKey: String = ""
}