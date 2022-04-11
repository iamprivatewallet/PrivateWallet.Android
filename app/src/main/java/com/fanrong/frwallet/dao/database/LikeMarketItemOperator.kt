package com.fanrong.frwallet.dao.database

import org.litepal.LitePal
import xc.common.tool.utils.checkNotEmpty

object LikeMarketItemOperator {

    fun queryIsLike(symbol: String): Boolean {
        return LitePal.where("symbol like ?", symbol).find(LikeMarketItemDao::class.java).checkNotEmpty()
    }

    fun getItemBySymbol(symbol: String):List<LikeMarketItemDao>{
        return LitePal.where("symbol like ?", symbol).find(LikeMarketItemDao::class.java)
    }

    fun update(item: LikeMarketItemDao) {
        val list = getItemBySymbol(item.symbol!!)
        for (data in list){
            data.isLike = item.isLike
            data.save()
        }
    }

    fun getAll():List<LikeMarketItemDao>{
        return LitePal.findAll(LikeMarketItemDao::class.java)
    }


}