package com.fanrong.frwallet.wallet.cwv

data class BalanceResp(var retCode: String?) {
    var account: BalanceAccount? = null
    var address: String? = null
    var nonce: String? = null
    var balance: String? = null
    var status: String? = null
}

class BalanceAccount {
    var address: String? = null
    var value: BalanceValue? = null

    class BalanceValue {
        var nonce: Int = 0
        var balance: String? = null
        var tokens: List<BalanceToken>? = null

        // 自定义字段，方便页面token 查询
        var tokensMap: HashMap<String, BalanceToken>? = null
    }

    class BalanceToken {
        var token: String? = null
        var balance: String? = null
    }
}