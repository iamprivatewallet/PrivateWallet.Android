package com.fanrong.frwallet.dao.data

import java.io.Serializable
import javax.annotation.Nullable

class TransferDataBean: Serializable {
    var gasInfo:GasInfoBean? = null
    var transferData:String = ""

    constructor(_transferData:String, @Nullable _gasInfo:GasInfoBean){
        transferData = _transferData
        gasInfo = _gasInfo
    }
}