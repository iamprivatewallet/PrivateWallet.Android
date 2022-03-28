package com.fanrong.frwallet.dao.database

import org.litepal.crud.LitePalSupport

class LocalNoceDao: LitePalSupport() {
    var id: Long = 0

    /**
     * 本地 noce 16进制
     */
    var nonce:String?=null

    /**
     * node_chaintype_addr
     */
    var unique:String?=null
}