package com.fanrong.frwallet.tools

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.fanrong.frwallet.dao.database.WebIconConfig
import org.litepal.LitePal
import java.io.ByteArrayOutputStream

object bitMapAndStringConvertUtil {
    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    fun convertIconToString(bitmap: Bitmap): String? {
        val baos = ByteArrayOutputStream() // outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val appicon = baos.toByteArray() // 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT)
    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    fun convertStringToIcon(st: String?): Bitmap? {
        // OutputStream out;
        var bitmap: Bitmap? = null
        return try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            val bitmapArray: ByteArray
            bitmapArray = Base64.decode(st, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(
                bitmapArray, 0,
                bitmapArray.size
            )
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            bitmap
        } catch (e: Exception) {
            null
        }
    }


    fun getWebIconConfigByWebUrl(webUrl: String?): MutableList<WebIconConfig?>? {
        val host = getUrlHostUtils.getHost(webUrl!!)
        return LitePal.where("webUrl=?", host).find(WebIconConfig::class.java)
    }

    fun getAllWebIconConfig(): MutableList<WebIconConfig?>? {
        return LitePal.findAll(WebIconConfig::class.java)
    }

    fun getWebIconConfigByWebUrlFirst(webUrl: String?): WebIconConfig? {
        val host = getUrlHostUtils.getHost(webUrl!!)
        return LitePal.where("webUrl=?", host).find(WebIconConfig::class.java)[0]
    }

    fun getBitMapByWebUrl(webUrl: String?): Bitmap? {
        val host = getUrlHostUtils.getHost(webUrl!!)
        val webIconConfig = LitePal.where("webUrl=?", host).find(WebIconConfig::class.java)
        if (webIconConfig!=null && webIconConfig.size>0){
            return convertStringToIcon(webIconConfig.get(0).iconStr)
        }else{
            return null
        }

    }
}