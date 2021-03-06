package com.fanrong.frwallet.dao

import com.fanrong.frwallet.R


class ChainInfo {

    companion object {
        fun getChainBg(chainName: String): Int {
            when (chainName) {
                "ETH" -> {
                    return R.drawable.bg_wallet_title
                }
                "HECO" -> {
                    return R.drawable.bg_wallet_title
                }
                "BSC" -> {
                    return R.drawable.bg_wallet_title
                }
                "CVN" -> {
                    return R.drawable.bg_wallet_title
                }
            }
            return R.drawable.bg_wallet_title

//            return "https://privatewallet.s3.ap-southeast-1.amazonaws.com/pic/chain/"+chainName.toLowerCase()+".png"
        }
    }


    var name: String
    var icon: Int
    var fullName:String
    var isDefaultSupport: Boolean = false


    constructor(name: String, icon: Int,fullname:String) {
        this.name = name
        this.icon = icon
        this.fullName = fullname
    }

//    constructor()


    var walletManagerCheckIcon: Int = 0
        get() {
            when (name) {
                "All" -> {
                    return R.mipmap.all_select
                }
                "ETH" -> {

                    return R.mipmap.eth_select
                }
                "HECO" -> {

                    return R.mipmap.heco_select
                }
                "BSC" -> {
                    return R.mipmap.bsc_select
                }
                "CVN" -> {
                    return R.mipmap.cvn_select
                }
            }
            return R.mipmap.src_lib_eui_icon_walletethereumnormal
        }


    var walletManagerUnCheckIcon: Int = 0
        get() {
            when (name) {
                "All" -> {
                    return R.mipmap.all_unselect
                }
                "ETH" -> {

                    return R.mipmap.eth_unselect
                }
                "HECO" -> {

                    return R.mipmap.heco_unselect
                }
                "BSC" -> {
                    return R.mipmap.bsc_unselect
                }
                "CVN" -> {
                    return R.mipmap.cvn_unselect
                }
            }
            return R.mipmap.src_lib_eui_icon_walletethereum
        }
}

object FrConstants {

    object SP {
        val IS_FRIST_INSTALL = "is_frist_install"
    }

    /**
     * ????????????key
     */
    object PP {
        val IS_DAPP = "is_dapp"
        val WALLET_QRCODE = "wallet_qrcode"
        val WC_PAGETYPE = ""
        val WC_TRANSACTION = "wc_transaction"
        val WC_SIGN = "wc_sign"
    }

    val suport_chains = mutableListOf<ChainInfo>(
//        ChainInfo("BTC", R.mipmap.src_lib_eui_icon_walletethereumnormal,"Bitcoin"),
        ChainInfo("ETH", R.mipmap.src_lib_eui_icon_walletethereumnormal,"Ethereum"),
        ChainInfo("HECO", R.mipmap.src_lib_eui_icon_walletethereumnormal,"Heco Chain"),
        ChainInfo("BSC", R.mipmap.src_lib_eui_icon_walletethereumnormal,"BNB Smart Chain"),
        ChainInfo("CVN", R.mipmap.src_lib_eui_icon_walletethereumnormal,"Conscious Value Network")
    )
    val language = mutableListOf<String>("????????????", "????????????", "English")
    val language_code = mutableListOf<String>("ch", "zh_rTW", "en")
    val monetary_unit = mutableListOf<String>("CNY", "USD")

    val monetary_unit_cn = mutableListOf<String>("?????????????????", "?????????\$???")
    val monetary_unit_en = mutableListOf<String>("CNY", "USD")


    val USER_SERVICE = "file:///android_asset/appdes/userservice.html"
    val CREATE_WALLET_HELPER = "https://support.token.im/hc/zh-cn/articles/360035597853?locale=zh-CN&utm_source=imtoken"


    // ????????????

    val WALLET_TYPE = "wallet_type"
    val FROM_CREATE = "from_create"
    val WALLET_INFO = "wallet_info"
    val TOKEN_INFO = "token_info"
    val CHAIN_TYPE = "chain_type"
    val ADDR_INFO = "addr_info"

    val APPLOCKPASSWORD = "app_lock_password"
    val ISOPENAPPLICATIONLOCK = "is_open_password_lock_application"

    val AMOUNT = "amount"
    val GAS_INFO = "gas_info"
    val TRANSACTION_INFO = "transaction_info"

    val SELECT_COIN = "select_coin"

    val PRE_PAGE = "pre_page"
    val PRE_PAGE_CREATE = "pre_page_create"
    val PARAMS_NODE_INFO = "nodeinfo"

    val IS_ONLY_SHOW_SMALL = "show_small"

    val PICK = "pick"

    val MSG_INFO = "msg_info"
    val FINGER_SETTING = "finger_setting"
    val SHOW_MONEY_SETTING = "show_money_setting"
    val LUA_COINTYPE_SETTING = "lua_cointype_setting"
    val ISMORELAN = "is_more_lan"
    val LUA_SETTING = "lua_setting"
    val LAN_CODE = "lan_code"
    val UNIT_SETTING = "unit_setting"
    val REDUP_GREENDOWN= "red_up_green_down"

    //    var WALLET_TYPE = "wallet_type"
    //???????????? Private Wallet 1.0??????
    val MIGRATION_WALLET_1 = "https://support.token.im/hc/zh-cn/articles/360035597513?locale=zh-CN&utm_source=imtoken"

    //????????????
    val TERMS_OF_SERVICE = "https://v2.token.im/tos?locale=zh-CN"

    //??????????????????
    val EXPORT_PRIVATEKEY_HELPER = "https://support.token.im/hc/zh-cn/articles/360035109054?locale=zh-CN&utm_source=imtoken"

    //??????????????????
    val RECOVER_ID_HELPER = "https://support.token.im/hc/zh-cn/articles/360035108954?locale=zh-CN&utm_source=imtoken"

    //????????????
    val RECEIPT_HELPER = "https://support.token.im/hc/zh-cn/sections/360005979753?locale=zh-CN&utm_source=imtoken"

    //??????????????????
    val ID_MANAGE_HELPER = "https://support.token.im/hc/zh-cn/articles/360003231194?locale=zh-CN&utm_source=imtoken"

    //???????????????
    val GAS_EXPLAIN = "https://support.token.im/hc/zh-cn/articles/115000957653?locale=zh-CN&utm_source=imtoken"

    //????????????
    val FORGET_PSW = "https://support.token.im/hc/zh-cn/articles/360003123953?locale=zh-CN&utm_source=imtoken"

    //????????????
    val COMMON_FRAUD = "https://support.token.im/hc/zh-cn/articles/900003102126?locale=zh-CN&utm_source=imtoken"

    //????????????   ????????????
    val HELP_FEEDBACK = "https://privatewallet.gitbook.io/home/v/cn/instructions"//"https://support.token.im/hc/zh-cn"

    //????????????
    val USER_AGREEMENT = "https://privatewallet.gitbook.io/home/v/cn/agreement"//"https://token.im/tos?locale=zh-cn&utm_source=imtoken"

    //????????????
    val USER_GUIDE = "https://privatewallet.gitbook.io/home/v/cn/advice"//"https://manual.token.im/imtoken/?locale=zh-CN&utm_source=imtoken"

    //?????????????????????
    val HELP_BLACKUP = "https://support.token.im/hc/zh-cn/articles/360035109014?locale=zh-CN&utm_source=imtoken"

    //???????????? dapp
    val HELPER_HOW_STAR = "https://support.token.im/hc/zh-cn/articles/360039684214?locale=zh-CN&utm_source=imtoken"



    //??????????????????
    val IMPORTWORD_HELP = "https://support.token.im/hc/zh-cn/articles/360002074233?locale=zh-CN&utm_source=imtoken"
    //???????????????
    val IMPORTPRIKEY_HELP = "https://support.token.im/hc/zh-cn/articles/360002074073?locale=zh-CN&utm_source=imtoken"
    //?????????keystore
    val IMPORTKEYSTORE_HELP = "https://support.token.im/hc/zh-cn/articles/360002074313?locale=zh-CN&utm_source=imtoken"

}