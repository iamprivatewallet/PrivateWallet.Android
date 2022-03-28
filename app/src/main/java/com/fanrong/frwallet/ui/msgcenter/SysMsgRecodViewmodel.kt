package com.fanrong.frwallet.ui.msgcenter

import androidx.lifecycle.viewModelScope
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryTransactionPageReq
import com.fanrong.frwallet.wallet.eth.eth.QueryTransactionPageResp
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel
import xc.common.framework.mvvm.OperationResult

/**
 * Created by shiyulong on 2021/9/16.
 */
class SysMsgRecodViewmodel :BaseViewModel<SysMsgRecodViewmodel.State>(){
    class State : BaseLiveData() {
        var msgRecordResult: OperationResult<List<QueryTransactionPageResp.transactionItem>>? = null
    }

    override fun createDefautState(): State {
        return State()
    }
     fun getSysMsgRecordReq(req:QueryTransactionPageReq){
        viewModelScope.tryLaunch {
            val resp = centerApi.queryTransactionPage(req).body()
            if (resp?.code == 1){
                newAndPostValue {
                    msgRecordResult= OperationResult(resp.data?.content, true)
                }
            }else{
                postErrorMsg(resp?.msg.toString())
            }
        }
    }

    fun getSysMsgRecordReq2(req:QueryTransactionPageReq){
        viewModelScope.tryLaunch {
            val resp = centerApi.querySystemMessage(req).body()
            if (resp?.code == 1){
                newAndPostValue {
                    msgRecordResult= OperationResult(resp.data?.content, true)
                }
            }else{
                postErrorMsg(resp?.msg.toString())
            }
        }
    }
}