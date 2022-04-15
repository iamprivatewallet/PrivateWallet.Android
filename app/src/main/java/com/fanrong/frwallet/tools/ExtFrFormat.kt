package com.fanrong.frwallet.tools

import org.brewchain.core.crypto.cwv.util.BytesHelper
import xc.common.tool.utils.tryRunDefault
import java.math.BigDecimal
import java.math.BigInteger
import java.math.RoundingMode
import java.text.DecimalFormat


fun String?.extHexWei2Gwei(): String {
    return tryRunDefault("0") {
        BigDecimal(BigInteger(this!!.removePrefix("0x"), 16).toString())
            .divide(BigDecimal(10).pow(9)).setScale(2, RoundingMode.UP).toPlainString()
    }
}

fun String?.extGwei2Wei(): String {
    return tryRunDefault("0") {
        BigDecimal(this).multiply(BigDecimal(10).pow(9)).setScale(2, RoundingMode.UP).toString()
    }
}

fun String?.extWei2Gwei(): String {
    return tryRunDefault("0") {
        BigDecimal(this)
            .divide(BigDecimal(10).pow(9)).setScale(2, RoundingMode.UP).toString()
    }
}

fun String?.extKillPointAfterZero():String{
    if (this!!.contains(".")){
        val split = this.split(".")
        return split[0]
    }
    return this
}

fun String?.extPow(len:Int):String{
    return tryRunDefault("0") {
        BigDecimal(this!!).multiply(BigDecimal(10).pow(len)).toPlainString()
    }
}

fun String?.extHexToTen(): String {
    return tryRunDefault("0") {
        BigInteger(this!!.removePrefix("0x"), 16).toString()
    }
}

fun String?.extTen2Hex(): String {
    return tryRunDefault("0x0") {
        "0x" + BigInteger(this!!).toString(16)
    }
}

/**
 *  wei 转 ether 保留两位小数
 */
fun String?.extHexWei2Ten2EtherKeep2(): String {
    return tryRunDefault("0.00") {
        BytesHelper.hexStr2BigDecimal(this, 18, 2).toPlainString()
    }
}


fun String?.extEther2Wei(): String {
    return tryRunDefault("0") {
        BigDecimal(this!!).multiply(BigDecimal(10).pow(18)).toPlainString()
    }
}


fun String?.extWei2Ether(): String {
    return tryRunDefault("0") {
        BigDecimal(this!!).divide(BigDecimal(10).pow(18)).toPlainString()
    }
}

fun String?.extWei2EtherKeep4(): String {
    return tryRunDefault("0.0000") {
        BigDecimal(this!!).divide(BigDecimal(10).pow(18)).toPlainString()
    }
}

fun checkHexString(string: String): Boolean {

    for (element in string) {
        var cInt: Int = element.toInt()
        if ((cInt in 48..57)
            || (cInt in 65..70)
            || (cInt in 97..102)
        ) {

        } else {
            return false
        }
    }
    return true
}

fun String?.extHexWei2Ten2EtherKeep4_2(): String {
    if (this.equals("0") || this.equals("0x0")){
        return "0.0000"
    }
    val checkHexString = checkHexString(this!!)
    if (checkHexString){
        return tryRunDefault("0.0000") {
            BytesHelper.hexStr2BigDecimal(this, 18, 4).toPlainString()
        }
    }else{
        var f= this.toFloat()
        var decimalFormat= DecimalFormat("0.0000");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        return decimalFormat.format(f)
    }

}

fun String?.extHexWei2Ten2EtherKeep4(): String {
    return tryRunDefault("0.0000") {
        BytesHelper.hexStr2BigDecimal(this, 18, 4).toPlainString()
    }
}

fun String?.extHexWei2Ten2EtherKeep8(): String {
    return tryRunDefault("0.0000") {
        BytesHelper.hexStr2BigDecimal(this, 18, 8).toPlainString()
    }
}


fun String?.extHexWei2Ten2EtherKeep18(): String {
    return tryRunDefault("0.0000") {
        BytesHelper.hexStr2BigDecimal(this, 18, 18).toPlainString()
    }
}


fun String?.extToFiatMoney(): String {
    return tryRunDefault("0.00") {
        BigDecimal(this).multiply(BigDecimal("7")).setScale(2, RoundingMode.DOWN).toPlainString()
    }
}

fun String?.extToFiatMoney(price:Float): String {
    return tryRunDefault("0.00") {
        BigDecimal(this).multiply(BigDecimal(price.toString())).setScale(2, RoundingMode.DOWN).toPlainString()
    }
}