package com.fanrong.frwallet.dao.database

import org.litepal.crud.LitePalSupport
import java.io.Serializable


class WalletDao(var address: String) : LitePalSupport(), Serializable {

    constructor() : this("") {

    }

    var id: Long = 0
    var pubKey: String? = null   //公钥

    var privateKey: String? = null //私钥
    var walletName: String? = null  //钱包名字

    // 身份钱包名称
    var identityName: String? = null

    /**
     * 0,不是 ，1，是
     */
    var isCurrentWallet: String = "0"
    var isMainWallet: String = "0"
    var isBackUp: String = "0"
    var password = ""
    var passwordHint = ""

    /**
     * 0,不是 ，1，是
     */
    var isFinger = ""

    var balance: String? = null   // 钱包剩余钱数

    var amount: String? = null
    var mnemonic: String? = null
    var keystore: String? = null
    var chainType: String? = null // 主链类型
    var symbol:String? = null

    var sortType: String = "0" // 排序类型 0默认 1价值 2名称 3拖拽

    public var isShowRmb: Boolean? = null
        get() {
            return if (field == null) true else field
        }


    override fun toString(): String {
        return "GreWalletModel(address='$address', pubKey='$pubKey', privateKey='$privateKey', walletName='$walletName', balance='$balance', id=$id)"
    }
}

