package com.fanrong.frwallet.wallet.eth.eth

import com.fanrong.frwallet.dao.FrConstants
import xc.common.tool.utils.SPUtils

class QueryDappReqNew {
    var chainId:String = ""
    var isTop:String = ""
    var languageCode:String = SPUtils.getString(FrConstants.LAN_CODE,"en_US")

    constructor(id:String,istop:String){
        chainId = id
        isTop = istop
    }
}

class QueryDappRespNew {
    var code:Int = 0
    var msg:String = ""
    var data:MutableList<dappItem>? = null

    class dappItem{
        var id:String = ""
        var appName:String = ""
        var description:String = ""
        var iconUrl:String = ""
        var appUrl:String = ""
        var firType:String = ""
        var secType:String = ""
        var chainId:String = ""
    }
}