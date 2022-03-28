package com.fanrong.frwallet.dao.database

import org.litepal.LitePal

object DappHistoryOperator {
    fun update(dao: DappHistoryDao) {
        dao.update(dao.id)
//        LitePal.update(DappInfoDao::class.java,dao,dao.id)
    }


    fun queryDappSortByTime(): List<DappHistoryDao> {
        return LitePal.order("lastVisiteTime desc").find(DappHistoryDao::class.java, true)
    }

    fun delete(item: DappHistoryDao?) {
        item?.delete()
    }

    fun addOrUpdate(sourceUrl: String) {
        val query = DappInfoOperator.query(sourceUrl)!!
        for (his in queryDappAllHistory()){
            if (his.url==sourceUrl){
                his.lastVisiteTime=System.currentTimeMillis().toString()
                his.update(his.id)
                return
            }
        }
        DappHistoryDao().apply {
            this.icon = query.icon
            this.des = query.des
            this.url = query.url
            this.name = query.name
            lastVisiteTime = System.currentTimeMillis().toString()
            save()
        }
    }
    fun queryDappAllHistory(): List<DappHistoryDao> {
        return LitePal.findAll(DappHistoryDao::class.java)
    }

}