package com.fanrong.frwallet.dao.database

import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QuerySystemMessageDetailReq
import com.fanrong.frwallet.wallet.eth.eth.QuerySystemMessageDetailResp
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj

object CenterDataManager {
    fun getSystemMsgDetail(id:String,callback: (result: QuerySystemMessageDetailResp) -> Unit) {
        centerApi.querySystemMessageDetail(QuerySystemMessageDetailReq(id))
            .netSchduler()
            .subscribeObj(object : NetCallBack<QuerySystemMessageDetailResp> {
                override fun onSuccess(t: QuerySystemMessageDetailResp) {
                    callback(t)
                }

                override fun onFailed(error: Throwable) {

                }

            })
    }
}