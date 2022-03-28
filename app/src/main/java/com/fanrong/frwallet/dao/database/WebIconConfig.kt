package com.fanrong.frwallet.dao.database

import android.graphics.Bitmap
import org.litepal.crud.LitePalSupport
import java.io.Serializable

class WebIconConfig : LitePalSupport(),Serializable{

    var webUrl: String = ""
    var iconStr: String? = null
    var title: String? = null
    var favicon:Bitmap? = null

}