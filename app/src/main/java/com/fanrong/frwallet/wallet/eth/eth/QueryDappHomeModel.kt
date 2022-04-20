package com.fanrong.frwallet.wallet.eth.eth

import com.fanrong.frwallet.dao.FrConstants
import xc.common.tool.utils.SPUtils

class QueryDappHomeReq {
    var chainId:String = ""
    var languageCode:String = SPUtils.getString(FrConstants.LAN_CODE, "en_US")
    constructor(_chainid:String){
        chainId = _chainid
    }
}

class QueryDappHomeResp {
    var code: Int? = null
    var msg: String? = null
    var data: DataDTO? = null

    class DataDTO {
        var dapp_56: List<Dapp56DTO>? = null
        var dapp_128: List<Dapp128DTO>? = null
        var banner_1_1: List<Banner11DTO>? = null
        var banner_2_3: List<Banner23DTO>? = null
        var banner_2_2: List<Banner22DTO>? = null
        var dappTop: List<DappTopDTO>? = null
        var dapp_1: List<Dapp1DTO>? = null

        class Dapp56DTO {
            var id: String? = null
            var appName: String? = null
            var description: String? = null
            var iconUrl: String? = null
            var appUrl: String? = null
            var firType: String? = null
            var secType: String? = null
            var chainId: Int? = null
            var isTop: Int? = null
            var languageCode: String? = null
        }

        class Dapp128DTO {
            var id: String? = null
            var appName: String? = null
            var description: String? = null
            var iconUrl: String? = null
            var appUrl: String? = null
            var firType: String? = null
            var secType: String? = null
            var chainId: Int? = null
            var isTop: Int? = null
            var languageCode: String? = null
        }

        class Banner11DTO {
            var id: Int? = null
            var status: Int? = null
            var type: Int? = null
            var place: Int? = null
            var imgWeb: String? = null
            var imgH5: String? = null
            var videoUrl: String? = null
            var videoImg: String? = null
            var videoUrlH5: String? = null
            var videoImgH5: String? = null
            var sortId: Int? = null
            var clickUrl: String? = null
            var languageCode: String? = null
            var title: String? = null
            var createTime: String? = null
            var updateTime: String? = null
        }

        class Banner23DTO {
            var id: Int? = null
            var status: Int? = null
            var type: Int? = null
            var place: Int? = null
            var imgWeb: String? = null
            var imgH5: String? = null
            var videoUrl: String? = null
            var videoImg: String? = null
            var videoUrlH5: String? = null
            var videoImgH5: String? = null
            var sortId: Int? = null
            var clickUrl: String? = null
            var languageCode: String? = null
            var title: String? = null
            var createTime: String? = null
            var updateTime: String? = null
        }

        inner class Banner22DTO {
            var id: Int? = null
            var status: Int? = null
            var type: Int? = null
            var place: Int? = null
            var imgWeb: Any? = null
            var imgH5: String? = null
            var videoUrl: String? = null
            var videoImg: String? = null
            var videoUrlH5: String? = null
            var videoImgH5: String? = null
            var sortId: Int? = null
            var clickUrl: String? = null
            var languageCode: String? = null
            var title: String? = null
            var createTime: String? = null
            var updateTime: String? = null
        }

        inner class DappTopDTO {
            var id: String? = null
            var appName: String? = null
            var description: String? = null
            var iconUrl: String? = null
            var appUrl: String? = null
            var firType: String? = null
            var secType: String? = null
            var chainId: Int? = null
            var isTop: Int? = null
            var languageCode: String? = null
        }

        inner class Dapp1DTO {
            var id: String? = null
            var appName: String? = null
            var description: String? = null
            var iconUrl: String? = null
            var appUrl: String? = null
            var firType: String? = null
            var secType: String? = null
            var chainId: Int? = null
            var isTop: Int? = null
            var languageCode: String? = null
        }
    }

}