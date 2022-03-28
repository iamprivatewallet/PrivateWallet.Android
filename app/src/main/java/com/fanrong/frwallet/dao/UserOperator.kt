package com.fanrong.frwallet.dao

import org.litepal.LitePal
import xc.common.tool.utils.SPUtils

object UserOperator {

    fun loginout(){
//        LitePal.deleteAll(GreWalletModel::class.java)
        LitePal.deleteDatabase("frwallet")
        SPUtils.clear()
    }
}