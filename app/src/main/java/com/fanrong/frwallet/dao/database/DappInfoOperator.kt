package com.fanrong.frwallet.dao.database

import org.litepal.LitePal

object DappInfoOperator {

    fun query(url: String): DappInfoDao? {
        try {
            return LitePal.where("url=?", url).find(DappInfoDao::class.java, true)[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }


    fun insert(url: String, name: String?, icon: String?, des: String?): DappInfoDao? {
        try {
            return LitePal.where("url=?", url).find(DappInfoDao::class.java, true)[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return DappInfoDao().apply {
            this.url = url
            this.name = name
            this.icon = icon
            this.des = des
            save()
        }
    }


    fun updateEscapteCause(sourceUrl: String) {
        var query = query(sourceUrl)
        if (query == null) {
            query = DappInfoDao()
        }
        query!!.isShowHint = "1"
         query.update(query.id)
    }

    fun isShowEscapteCause(sourceUrl: String): Boolean {
        return "1".equals(query(sourceUrl)?.isShowHint ?: "")
    }
}