package com.fanrong.frwallet.wallet.eth.eth

class QueryBannerReq {
    var type:String = ""
    var place:String = ""
    var languageCode:String = ""

    constructor(_type:String,_place:String,_languageCode:String){
        type = _type
        place = _place
        languageCode = _languageCode
    }
}

class QueryBannerResp{
    var code:String = ""
    var msg:String = ""
    var data:MutableList<BannerItem>? = null

    class BannerItem{
        var id:Int = 0
        var status:Int = 0
        var type:Int = 0
        var place:Int = 0
        var imgWeb:String = ""
        var imgH5:String = ""
        var videoUrl:String = ""
        var videoImg:String = ""
        var videoUrlH5:String = ""
        var videoImgH5:String = ""
        var clickUrl:String = ""
        var languageCode:String = ""
        var title:String = ""
        var createTime:String = ""
        var updateTime:String = ""
    }
}