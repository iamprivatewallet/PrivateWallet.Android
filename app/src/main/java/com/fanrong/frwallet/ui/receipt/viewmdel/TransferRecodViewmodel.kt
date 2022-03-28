package com.fanrong.frwallet.ui.receipt.viewmdel

import androidx.lifecycle.viewModelScope
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dapp.dappapi.error.AccountNotFoundException
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.GetTransactionRecordReq
import com.fanrong.frwallet.wallet.eth.eth.GetTransactionRecordResp
import xc.common.framework.mvvm.BaseLiveData
import xc.common.framework.mvvm.BaseViewModel
import xc.common.framework.mvvm.OperationResult

/**
 * Created by shiyulong on 2021/9/16.
 */
class TransferRecodViewmodel :BaseViewModel<TransferRecodViewmodel.State>(){
    class State : BaseLiveData() {
        var transactionRecordResult: OperationResult<List<GetTransactionRecordResp.DataBean.ResultBean>>? = null
    }

    override fun createDefautState(): State {
        return State()
    }
     fun getTransactionRecordReq(req: GetTransactionRecordReq){
         var addrInfo = WalletOperator.currentWallet!!
         if (addrInfo == null) {
             throw AccountNotFoundException()
         }

//         val nodeInfo = ChainNodeOperator.queryCurrentNode(addrInfo.chainType!!)
         val useraddress = addrInfo.address//"0xb4a8f00266DdA25841790421BdDF243ca1E30157"
        viewModelScope.tryLaunch {
            val resp = centerApi.getTransactionRecordResp(req.apply {
                address = useraddress
            }).body()
            if (resp?.getIsok() == true){
                if (resp.getData()?.status=="1"){
                    newAndPostValue {
                        transactionRecordResult= OperationResult(resp?.getData()?.result, true)
                    }
                }
            }else{
                postErrorMsg(resp?.getMessage().toString())
            }
        }
    }
}