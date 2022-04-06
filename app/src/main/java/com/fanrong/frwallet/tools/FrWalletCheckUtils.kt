package com.fanrong.frwallet.tools

import android.content.Context
import android.widget.EditText
import com.fanrong.frwallet.dao.database.CVNScanResult
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.view.showTopToast
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.isHexStr


fun String?.checkPassword(): Boolean {
    if (this?.length!! >= 8) {
        return true
    } else {
        this?.showToast("密码格式应不少于8位字符")
        return false
    }
}
fun String?.checkPassword(context: Context): Boolean {
    if (this?.length!! >= 8) {
        return true
    } else {
        this?.showTopToast(context,"密码格式应不少于8位字符",false)
        return false
    }
}
fun String?.checkTwoPasswordIsSame(repassword:String): Boolean {
    if (repassword.equals(this)) {
        return true
    } else {
        this?.showToast("两次密码不一致")
        return false
    }
}
fun String?.checkTwoPasswordIsSame(context: Context,repassword:String): Boolean {
    if (repassword.equals(this)) {
        return true
    } else {
        this?.showTopToast(context,"两次密码不一致",false)
        return false
    }
}

fun String?.checkWalletName(): Boolean {
    if (this?.checkIsEmpty()!!) {
        this?.showToast("身份名不能为空")
        return false
    } else if (this?.length>12){
        this?.showToast("请输入 1~12 位的身份名")
        return false
    }else{
        return true
    }
}

fun String?.checkWalletName(context: Context): Boolean {
    if (this?.checkIsEmpty()!!) {
        this?.showTopToast(context,"身份名不能为空",false)
        return false
    } else if (this?.length>12){
        this?.showTopToast(context,"请输入 1~12 位的身份名",false)
        return false
    }else{
        return true
    }
}

fun String?.checkIsPrivateKey(): Boolean {
    return if (this?.length == 64 && this.isHexStr()) {
        true
    } else {
        this?.showToast("请输入 16 进制 64 位私钥")
        false
    }
}
fun String?.checkIsPrivateKey(context: Context): Boolean {
    return if (this?.length == 64 && this.isHexStr()) {
        true
    } else {
        this?.showTopToast(context,"请输入 16 进制 64 位私钥",false)
        false
    }
}



fun String?.checkIsWords(): Boolean {
    return if (this != null && this.split(" ")!!.size == 12) {
        true
    } else {
        this?.showToast("输入助记词非12个单词")
        false
    }
}

fun String?.checkIsWords(context: Context): Boolean {
    return if (this != null && this.split(" ")!!.size == 12) {
        true
    } else {
        this?.showTopToast(context,"输入助记词非12个单词",false)
        false
    }
}

fun String?.extCheckNotAddr(chainType: String?): Boolean {
    return !extCheckIsAddr(chainType)
}
fun String?.extGetRightAddress(chainType: String?):String{
    var curAddress = this
    if (this!!.startsWith("ethereum:")){
        curAddress = this.removePrefix("ethereum:")
    }

    if ("ETH".equals(chainType)
        || "HECO".equals(chainType)
        || "BSC".equals(chainType)
    ) {
        if (curAddress == null || curAddress.removePrefix("0x").length != 40) {
            showToast("d地址格式不对")
            return ""
        } else {
            return curAddress
        }

    } else if ("CVN".equals(chainType)) {
        var formatJson = NetTools.formatJson(curAddress!!, CVNScanResult::class.java)
        if (formatJson == null){
            formatJson = CVNScanResult()
            formatJson.address = curAddress
        }
        if (curAddress == null || formatJson?.address?.removePrefix("CVN")?.length != 40) {
            showToast("地址格式不对")
            return ""
        } else {
            return formatJson.address
        }
    } else {
        // 不确定链类型 不做校验
        return ""
    }
}

fun String?.extCheckIsAddr(chainType: String?): Boolean {
//    var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
//    var chainType = walletInfo.chainType
    var curAddress = this
    if (this!!.startsWith("ethereum:")){
        curAddress = this.removePrefix("ethereum:")
    }
    if ("ETH".equals(chainType)
        || "HECO".equals(chainType)
        || "BSC".equals(chainType)
    ) {
        if (curAddress == null || curAddress.removePrefix("0x").length != 40) {
            showToast("d地址格式不对")
            return false
        } else {
            return true
        }

    } else if ("CVN".equals(chainType)) {
        var formatJson = NetTools.formatJson(curAddress!!, CVNScanResult::class.java)
        if (formatJson == null){
            formatJson = CVNScanResult()
            formatJson.address = curAddress
        }
        if (curAddress == null || formatJson?.address?.removePrefix("CVN")?.length != 40) {
            showToast("地址格式不对")
            return false
        } else {
            return true
        }
    } else {
        // 不确定链类型 不做校验
        return true
    }
}

fun String?.extCheckIsAddrNoToast(): Boolean {
    var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
    if (walletInfo.chainType.equals("CVN")){
        var formatJson = NetTools.formatJson(this!!, CVNScanResult::class.java)
        if (formatJson == null){
            formatJson = CVNScanResult()
            formatJson.address = this
        }
        return !(this == null || formatJson?.address?.removePrefix("CVN")?.length != 40)
    }else{
        var curAddress = this
        if (this!!.startsWith("ethereum:")){
            curAddress = this.removePrefix("ethereum:")
        }
        return !(this == null || curAddress!!.removePrefix("0x").length != 40)
    }

}

fun String?.extCheckIsContractAddr(): Boolean {
    if (this == null || this.removePrefix("0x").length != 40) {
        showToast("合约地址格式不对")
        return false
    } else {
        return true
    }

}


/*---------------------------------     fromat      -------------------------------------*/
fun String.extFormatAddr(): String {
    return this.substring(0, 10) + "..." + this.substring(this.length - 10, this.length)
}


object FrWalletCheckUtils {

    fun checkPasswrod(password: EditText): Boolean {
        if (password.text.length == 8) {
            return false
        } else {
            showToast("密码格式不对")
            return true
        }
    }
}