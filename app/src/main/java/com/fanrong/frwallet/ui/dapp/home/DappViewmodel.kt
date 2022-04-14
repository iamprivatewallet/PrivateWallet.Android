package com.fanrong.frwallet.ui.dapp.home

import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryDappReqNew
import com.fanrong.frwallet.wallet.eth.eth.QueryDappRespNew
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel
import xc.common.framework.mvvm.LiveDataErrorInfo
import xc.common.framework.mvvm.OperationResult
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj

class DappViewmodel : BaseViewModel<DappViewmodel.State>() {

    class State : BaseLiveData() {

        var loadMoreResult: OperationResult<MutableList<QueryDappRespNew.dappItem>>? = null
        var initDappResult: OperationResult<MutableList<QueryDappRespNew.dappItem>>? = null

    }

    override fun createDefautState(): State {
        return State()
    }


    fun queryRecommentDapp(type:String?) {

        val req = QueryDappReqNew(CoinNameCheck.getChainIdByName(type!!),"0")

        centerApi.queryDapp(req).netSchduler()
            .subscribeObj(object : NetCallBack<QueryDappRespNew> {
                override fun onSuccess(t: QueryDappRespNew) {

                    newAndPostValue {
                        initDappResult = OperationResult(t.data, true)
                    }

                }

                override fun onFailed(error: Throwable) {
                    newAndPostValue {
                        errorinfo = LiveDataErrorInfo(error.message?:"未知错误")
                    }
                }

            })
    }
}