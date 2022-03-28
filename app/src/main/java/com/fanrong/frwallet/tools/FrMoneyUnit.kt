package com.fanrong.frwallet.tools

object FrMoneyUnit {
    var currentUnit = "CNY"
    fun getSymbal(): String {
        when (currentUnit) {
            "CNY" -> {
                return "￥"
            }
            "USD" -> {
                return "$"
            }
        }
        return "￥"
    }

}