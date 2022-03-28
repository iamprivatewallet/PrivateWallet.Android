package com.fanrong.frwallet.dao.database

import android.graphics.Bitmap
import org.litepal.crud.LitePalSupport

class DappStarDao : LitePalSupport() {
    var id: Long = 0

    var url: String? = null
    var name: String? = null
    var icon_bitmap:Bitmap? = null
    var icon: String? = null
    var des: String? = null

    var sort = 1
}