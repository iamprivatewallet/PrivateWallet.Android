package com.fanrong.frwallet.tools

import xc.common.tool.utils.checkIsEmpty
import java.math.BigDecimal
import java.math.RoundingMode


fun String?.projectAmountStr(): String {
    return ProjectAmountUtils.showAmount(this)
}

object ProjectAmountUtils {

    fun plus(number: String, number1: String): String {
        var result = "0"
        try {
            result = BigDecimal(number).add(BigDecimal(number1))
                .setScale(4, RoundingMode.DOWN).toString()
            return result
        } catch (e: Exception) {
        }
        return result
    }

    fun cnyToUsdt(cny: String, usdtPrice: String): String {
        var result = "0"
        try {
            return BigDecimal(cny).divide(BigDecimal(usdtPrice),4,RoundingMode.DOWN)
                .toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }


    fun multiplyPrice(amount: String, price: String): String {
        var result = BigDecimal.ZERO
        try {
            result = BigDecimal(amount)
            result = BigDecimal(amount).multiply(BigDecimal(price)).setScale(2, RoundingMode.DOWN)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result.toString()
    }

    fun divide18(amount: String?): String {
        if (amount.checkIsEmpty()) {
            return "0.00"
        }
        return BigDecimal(amount).divide(BigDecimal(Math.pow(10.0, 18.0)))
            .setScale(8, RoundingMode.DOWN).toString()
    }

    fun isInteger(value: String?): Boolean {
        return if (value.checkIsEmpty()) {
            false
        } else {
            BigDecimal(BigDecimal(value).toBigInteger())
                .compareTo(BigDecimal(value)) == 0
        }
    }


    fun toBigDecimal(str: String?): BigDecimal {
        if (str.checkIsEmpty()) {
            return BigDecimal("0")
        }

        return BigDecimal(str)
    }


    fun keepDecimal2(str: String?): String {

        if (str.checkIsEmpty()) {
            return "0.00"
        }

        return BigDecimal(str).setScale(2, RoundingMode.DOWN).toString()
    }

    fun keepDecimal4(str: String?): String {

        if (str.checkIsEmpty()) {
            return "0.0000"
        }

        return BigDecimal(str).setScale(4, RoundingMode.DOWN).toString()
    }

    fun showAmount(str: String?): String {
        if (str.checkIsEmpty()) {
            return "0.00"
        }

        var formatStr: String
        try {
            var eight = BigDecimal(str).setScale(8, RoundingMode.DOWN)
            var two = BigDecimal(str).setScale(2, RoundingMode.DOWN)
            if (two.compareTo(eight) == 0) {
                return two.toString()
            } else {
                return eight.toDouble().toString()
            }

        } catch (e: Exception) {
            e.printStackTrace()
            formatStr = "0.00"
        }
        return formatStr
    }

    fun isBigThanZero(amount: String): Boolean {
        try {
            return BigDecimal(amount).compareTo(BigDecimal.ZERO) > 0
        } catch (e: java.lang.Exception) {
            return false
        }
    }
}