package com.fanrong.frwallet.wallet.cwv.api.models

import xc.common.tool.utils.checkIsEmpty


/**
 * 查询智能合约余额
 */
class CVNGetBalanceReq {
    var to: String? = null
    var data: String? = null

    constructor(contract: String, addr: String) {
        this.to = contract
        this.data = "0x70a08231000000000000000000000000" + addr.removePrefix("CVN").removePrefix("0x")
    }
}

class CVNGetBalanceResp {
    var result: String? = null
        get() {
            if (field.checkIsEmpty()) {
                return "0x0"
            } else {
                return field
            }
        }
}