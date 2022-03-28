package com.fanrong.frwallet.dao.database

import android.util.Log
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryCoinReq
import com.fanrong.frwallet.wallet.eth.eth.QueryCoinResp
import org.litepal.LitePal
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj

object ConfigTokenOperator {

    fun initServiceToken() {
//        centerApi.queryTokens(QueryTokensReq("1", "1000"))
//            .netSchduler()
//            .subscribeObj(object : NetCallBack<QueryTokensResp> {
//                override fun onSuccess(t: QueryTokensResp) {
//                    if (t.code == 200 && t.data?.records.checkNotEmpty()) {
//                        LitePal.deleteAll(ConfigTokenDao::class.java)
//                        for (tokenDao in t.data!!.records!!) {
//                            tokenDao.save()
//                        }
//                    }
//                }
//
//                override fun onFailed(error: Throwable) {
//                }
//            })

        if (WalletOperator.currentWallet != null){
            centerApi.queryCoin(QueryCoinReq(CoinNameCheck.getCurrentNetID(),"1","")).netSchduler()
                .subscribeObj(object : NetCallBack<QueryCoinResp> {
                    override fun onSuccess(t: QueryCoinResp) {

                        if (t != null && t.data != null){
                            LitePal.deleteAll(ConfigTokenDao::class.java)
                            for (item in t.data!!){
                                var coin = ConfigTokenDao().apply {
                                    this.id = item.id.toLong()
                                    this.tokenContract = item.tokenContract
                                    this.tokenName = item.tokenName
                                    this.tokenSymbol = item.tokenSymbol
                                    this.tokenDecimals = item.tokenDecimals.toString()
                                    this.tokenChain = CoinNameCheck.getNetWorkNameByID(item.tokenChain)
                                    this.tokenLogo = item.tokenLogo
                                    this.createTime = item.createTime
                                    this.hotTokens = item.hotTokens
                                    save()
                                }
                            }
                        }

                    }

                    override fun onFailed(error: Throwable) {
                        Log.d("TAG", "onSuccess: --->>>>>")
//                    newAndPostValue {
//                        errorinfo = LiveDataErrorInfo(error.message?:"未知错误")
//                    }
                    }

                })
        }

    }

    fun queryHotTokensByChainName(chainName: String): MutableList<ConfigTokenDao>? {
        return LitePal.where("tokenChain=? and hotTokens=?", chainName, "1").find(ConfigTokenDao::class.java)
    }

    fun queryTokensByChainName(chainName: String): MutableList<ConfigTokenDao>? {
        return LitePal.where("tokenChain=?", chainName).find(ConfigTokenDao::class.java)
    }

}