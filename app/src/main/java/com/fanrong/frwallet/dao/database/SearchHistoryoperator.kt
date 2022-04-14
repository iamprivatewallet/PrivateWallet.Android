package com.fanrong.frwallet.dao.database

import org.litepal.LitePal

object SearchHistoryoperator {

    fun queryHistoryByChainType(chainName: String): MutableList<SearchHistoryResult>?{
        return LitePal.where("tokenChain=?", chainName).find(SearchHistoryResult::class.java)
    }

}