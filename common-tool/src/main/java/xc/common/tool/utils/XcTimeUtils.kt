package xc.common.tool.utils

import java.text.SimpleDateFormat
import java.util.*

object XcTimeUtils {
    val ONE_MONTH = 1000L * 60 * 60 * 24 * 30
    val ONE_DAY = 1000L * 60 * 60 * 24

    fun getBefore30Day(): Long {
        return System.currentTimeMillis() - ONE_MONTH
    }

    /**
     * 获取30 天之前的 0点
     */
    fun getBefore30DayClock0(): Long {
        return getTodayBeginTime(System.currentTimeMillis() - ONE_MONTH)
    }

    fun getBackFlashymd(str: String?): String {
        if (str.checkIsEmpty()) {
            return ""
        }
        try {
            var dateFormat = SimpleDateFormat("yyyy/MM/dd")
            return dateFormat.format(Date(str!!.toLong()))
        } catch (e: Exception) {
            return ""
        }

    }

    fun get_ymd(str: String?): String {
        if (str.checkIsEmpty()) {
            return ""
        }
        try {
            var dateFormat = SimpleDateFormat("yyyy_MM_dd")
            return dateFormat.format(Date(str!!.toLong()))
        } catch (e: Exception) {
            return ""
        }

    }

    fun get_yMd_hms(str: Long): String {
        if (str < 1) {
            return ""
        }
        try {
            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return dateFormat.format(Date(str))
        } catch (e: Exception) {
            return ""
        }
    }
    fun get_yMd_hms(str: String): String {
        try {
            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            return dateFormat.format(Date(str.toLong()))
        } catch (e: Exception) {
            return ""
        }
    }

    fun get_yMd_hms1(str: Long): String {
        if (str < 1) {
            return ""
        }
        try {
            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH-mm-ss")
            return dateFormat.format(Date(str))
        } catch (e: Exception) {
            return ""
        }
    }

    fun get_yMd_hm(str: String?): String {
        if (str.checkIsEmpty()) {
            return ""
        }
        try {
            var dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
            return dateFormat.format(Date(str!!.toLong()))
        } catch (e: Exception) {
            return ""
        }
    }

    fun format_yyMMdd(time:Long):String{
        try {
            var dateFormat = SimpleDateFormat("yyyyMMdd")
            return dateFormat.format(Date(time))
        } catch (e: Exception) {
            return ""
        }
    }
    fun format_yyMMdd(time:String?):String{
        try {
            var dateFormat = SimpleDateFormat("yyyyMMdd")
            return dateFormat.format(Date(time!!.toLong()))
        } catch (e: Exception) {
            return ""
        }
    }

    fun getTodayBeginTime(): Long {
        val instance = Calendar.getInstance()
        instance.set(Calendar.HOUR_OF_DAY, 0)
        instance.set(Calendar.SECOND, 0)
        instance.set(Calendar.MINUTE, 0)
        instance.set(Calendar.MILLISECOND, 0)
        val inMillis = instance.timeInMillis
        return inMillis
    }

    fun getTodayBeginTime(long: Long): Long {
        val instance = Calendar.getInstance()
        instance.timeInMillis = long
        instance.set(Calendar.HOUR_OF_DAY, 0)
        instance.set(Calendar.SECOND, 0)
        instance.set(Calendar.MINUTE, 0)
        instance.set(Calendar.MILLISECOND, 0)
        val inMillis = instance.timeInMillis
        return inMillis
    }

    fun getTodayEndTime(): Long {
        val instance = Calendar.getInstance()
        instance.set(Calendar.HOUR_OF_DAY, 23)
        instance.set(Calendar.SECOND, 59)
        instance.set(Calendar.MINUTE, 59)
        instance.set(Calendar.MILLISECOND, 999)
        val inMillis = instance.timeInMillis
        return inMillis
    }

    fun getTodayEndTime(long: Long): Long {
        val instance = Calendar.getInstance()
        instance.timeInMillis = long
        instance.set(Calendar.HOUR_OF_DAY, 23)
        instance.set(Calendar.SECOND, 59)
        instance.set(Calendar.MINUTE, 59)
        instance.set(Calendar.MILLISECOND, 999)
        val inMillis = instance.timeInMillis
        return inMillis
    }

}