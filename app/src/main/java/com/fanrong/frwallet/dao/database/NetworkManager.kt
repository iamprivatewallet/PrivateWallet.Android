package com.fanrong.frwallet.dao.database

import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.NetWorkManagerReq
import com.fanrong.frwallet.wallet.eth.eth.NetWorkManagerResp
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.kotlinext.showToast

object NetworkManager {
    fun getNetworkManager(){
        centerApi.queryNetworks(NetWorkManagerReq())
            .netSchduler()
            .subscribeObj(object : NetCallBack<NetWorkManagerResp> {
                override fun onSuccess(t: NetWorkManagerResp) {
                    showToast("成功")
                }

                override fun onFailed(error: Throwable) {
                    showToast("失败")
                }

            })
    }
}