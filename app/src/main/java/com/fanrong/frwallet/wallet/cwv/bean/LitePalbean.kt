package com.fanrong.frwallet.wallet.cwv.bean

import org.litepal.annotation.Column
import org.litepal.crud.LitePalSupport
import java.io.Serializable

data class AddressModel(val name: String, val address: String, var remark: String = "") : LitePalSupport(), Serializable {
    val id: Long = 0
    override fun toString(): String {
        return "AddressModel(name='$name', address='$address', remark='$remark', id=$id)"
    }
}


data class GreNodeModel(var node_url: String) : LitePalSupport() {
    var isUsing: Boolean? = null
        get() {
            if (field == null) {
                return false
            }
            return field
        }

    var node_name: String? = null
    var is_def: Boolean? = null
        get() {
            if (field == null) {
                return false
            }
            return field
        }
    var id: Long = 0
    var isFromService: Boolean? = null
        get() {
            return if (field == null) false else field
        }


    override fun toString(): String {
        return "GreNodeModel(node_url='$node_url', isUsing=$isUsing, is_def=$is_def, id=$id)"
    }
}


data class GreWalletModel(@Column(unique = true) var address: String) : LitePalSupport(), Serializable {
    var id: Long = 0
    var pubKey: String? = null   //公钥
    var privateKey: String? = null //私钥
    var walletName: String? = null  //钱包名字
    var balance: String? = null   // 钱包剩余钱数

    var rmb: String? = null
    var amount: String? = null
    var mnemonic: String? = null
    var walletType: String? = null

    public var isShowRmb: Boolean? = null
        get() {
            return if (field == null) true else field
        }

    var isImport: Boolean? = null
        get() {
            if (field == null) {
                return false
            }
            return field
        }

    override fun toString(): String {
        return "GreWalletModel(address='$address', pubKey='$pubKey', privateKey='$privateKey', walletName='$walletName', balance='$balance', id=$id)"
    }
}


data class LiteCoinBeanModel(var coin_name: String) : LitePalSupport(), Serializable {

    var id: Long = 0
    var contract_addr: String? = null//暂不使用
    var channel_name: String? = null //暂不使用
    var coin_symbol: String? = null//钱包名称 咱不使用
    var coin_decimals: String? = null //不使用
    var coin_total_supply: String? = null //不使用
    var coin_icon: String? = null //图标
    var single_max_amt: String? = null //暂不使用
    var single_min_amt: String? = null//暂不使用
    var count: String? = null //余额
    var countCNY: String? = null //余额转成RMB
    var sourceAddr: String? = null //当前主币地址
    var walletName: String? = null // 主币名称

    override fun toString(): String {
        return "LiteCoinBeanModel(coin_name='$coin_name', id=$id, contract_addr=$contract_addr, channel_name=$channel_name, coin_symbol=$coin_symbol, coin_decimals=$coin_decimals, coin_total_supply=$coin_total_supply, coin_icon=$coin_icon, single_max_amt=$single_max_amt, single_min_amt=$single_min_amt, count=$count, countCNY=$countCNY)"
    }


}

class TokenInfo(var tokenName: String) : LitePalSupport(), Serializable {
    //币地址
    var tokenAddress: String? = null

    //非接口返回
    var tokenType: String = "CVN"
    //主币地址
    var sourceAddr: String? = null
    var isOpen: Boolean? = null
        get() {
            if (field == null) {
                field = false
            }
            return field
        }

}