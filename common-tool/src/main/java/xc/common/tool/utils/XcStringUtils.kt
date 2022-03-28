package xc.common.tool.utils

import java.util.regex.Pattern

object XcStringUtils {

    private fun getStar(lengh: Int): String {
        var stars = "********************"
        while (stars.length < lengh) {
            stars += "********************"
        }

        return stars.substring(0, lengh)
    }

    /**
     * 判断是否是六位连续数组，或重复
     */
    fun checkContinueNumber(password: String): Boolean {
        if (password.length != 6) {
            return false
        }
        var regres =
            "^(\\d)\\1{5}$"
        var series = "0123456789_9876543210"
        var illegal = Pattern.matches(regres, password) || series.contains(password)
        return illegal
    }


    fun safeGetLast4Char(str: String?): String? {
        if (str.checkIsEmpty() || str!!.length < 5) {
            return str
        }
        return str!!.substring(str.length - 4, str.length)
    }

    fun safeGetStart4Char(str: String?): String? {
        if (str.checkIsEmpty() || str!!.length < 5) {
            return str
        }
        return str!!.substring(0, 4)
    }


    /**
     * 获取字符长度， 中文两位、中文一位
     */
    fun getBytesLenght(str: String?): Int {
        if (str.checkIsEmpty()) {
            return 0
        }

        var lenght = 0
        for (c in str!!) {
            if (c.toString().checkIsChinaWords()) {
                lenght += 2
            } else {
                lenght++
            }
        }
        return lenght
    }

    fun getHintStr(str: String?): String? {
        if (str.checkIsEmpty()) {
            return ""
        }
        str!!
        var str20 = "********************"
        val i = str.length / 20
        for (time in 0..i) {
            str20 += "********************"
        }

        return str20.substring(0, str.length)
    }

    fun onlyShowLast(str: String?): String {
        if (str.checkIsEmpty()) {
            return ""
        }

        return getHintStr(str!!.substring(0, str.length - 1)) + str.get(str.length - 1)
    }

    fun hideChinaCard(str: String?): String? {
        if (str.checkIsEmpty() || str!!.length <= 10) {
            return str
        } else {
            return str.substring(0, 6) + getStar(str.length - 10) + str.substring(
                str.length - 4,
                str.length
            )
        }
    }

    fun hidePassportCard(str: String?): String? {
        if (str.checkIsEmpty() || str!!.length <= 4) {
            return str
        } else {
            return str.substring(0, 2) + getStar(str.length - 4) + str.substring(
                str.length - 2,
                str.length
            )
        }
    }


    fun hidePhoneFormat(str: String?): String? {
        if (str == null || str.isEmpty() || str.length < 7) {
            return str
        }
        return str.substring(0, 3) + "****" + str.substring(str.length - 4, str.length)
    }

    fun hideEmailFormat(str: String?): String? {
        if (str == null || str.isEmpty() || !str.contains("@") || str.indexOf("@") < 5) {
            return str
        }

        return str.substring(
            0,
            3
        ) + "***" + str.subSequence(str.indexOf("@") - 2, str.length)
    }


}