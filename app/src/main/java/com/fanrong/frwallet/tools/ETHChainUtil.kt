package com.fanrong.frwallet.tools

import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode


object ETHChainUtil {

    fun toGwei(hex: String): BigDecimal {
        return BigDecimal(BigInteger(hex.removePrefix("0x"), 16).toString())
            .divide(BigDecimal(10).pow(9)).setScale(2, RoundingMode.UP) // gwei
    }

    /**
     * @param gasPrice 单位 wei
     */
    fun compateGas1(gasLimit: String, gasPrice: String): String {
        try {
            return BigDecimal(gasPrice)
                .multiply(BigDecimal(gasLimit))
                .divide(BigDecimal(10).pow(18))
                .setScale(8, RoundingMode.UP).toPlainString()
        } catch (e: Exception) {
            e.printStackTrace()
            return "0"
        }
//        BigDecimal(hexNum.hexToTen()).multiply(hexNum1.hexToTen())
    }

    /**
     * @param gasPrice 单位 wei
     * 返回值单位是  wei
     */
    fun compateGasRetWei(gasLimit: String, gasPrice: String): String {
        try {
            return BigDecimal(gasPrice)
                .multiply(BigDecimal(gasLimit)).toPlainString()
        } catch (e: Exception) {
            e.printStackTrace()
            return "0"
        }
//        BigDecimal(hexNum.hexToTen()).multiply(hexNum1.hexToTen())
    }
}