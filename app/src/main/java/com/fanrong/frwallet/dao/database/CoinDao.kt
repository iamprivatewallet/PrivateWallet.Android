package com.fanrong.frwallet.dao.database

import com.fanrong.frwallet.tools.CoinNameCheck
import org.litepal.crud.LitePalSupport
import java.io.Serializable

class CoinDao(var coin_name: String) : LitePalSupport(), Serializable {

    var id: Long = 0
    var contract_addr: String? = null//
    var chain_name: String? = null
        get() {
            return sourceWallet!!.split("_")[0]
        }
    var coin_decimals: String? = null //不使用
    var coin_icon: String? = null //图标
    var balance: String? = null //余额
    var sourceAddr: String? = null //当前主币地址

    var price:Float = 0f //当前token的价格

    /**
     * 钱包类型_地址
     */
    var sourceWallet: String? = null //当前主币钱包

    /**
     * 唯一标识符 主链类型 + 地址 + tokenname
     */
    fun getUniqueTokenInfo(): String {
        return chain_name + "_" + sourceAddr + "_" + coin_name
    }

    fun getTokenIcon():String{
        return CoinNameCheck.getCoinImgUrl(contract_addr)
    }

}