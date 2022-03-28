package com.fanrong.frwallet.wallet.eth.eth

class NetWorkManagerReq {

}
class  NetWorkManagerResp{
    var code = 0
    var msg: String? = null
    var data: List<NetWorkItem>? = null
}

class NetWorkItem{
    var id = 0
    var chainId = 0
    var title: String? = null
    var symbol: String? = null
    var rpcUrl: String? = null
    var browseUrl: String? = null
    var backUrl: String? = null
}