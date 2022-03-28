package com.fanrong.frwallet.dao.database

import org.litepal.LitePal

object DappStarOperator {
    fun update(dao: DappStarDao) {
        dao.update(dao.id)
//        LitePal.update(DappStarDao::class.java,dao,dao.id)
    }

    fun queryStar(): MutableList<DappStarDao> {
        return LitePal.order("sort desc").find(DappStarDao::class.java, true)
    }

    fun queryDapp(url: String): DappStarDao? {
        try {

            return LitePal.where("url=?", url).find(DappStarDao::class.java)[0]
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return null
    }

    fun delete(item: DappStarDao?) {
        item?.delete()
    }

    fun dappStar(url: String,title:String) {
        DappStarDao().apply {

            var dapp = DappInfoOperator.query(url)
            this.icon = dapp?.icon
//            this.icon_bitmap = img
            this.name = title
            this.des = dapp?.des
            this.url = url
            save()
        }


    }

    fun dappCancelStar(url: String) {
        queryDapp(url)?.delete()
    }

    fun resort(data: List<DappStarDao>) {

//        var size = data.size
//        for (datum in data) {
//            datum.apply {
//                sort = size--
//                saveOrUpdate()
//            }
//        }

        LitePal.deleteAll(DappStarDao::class.java)
        for (datum in data) {
//            datum.save()
            val dappStarDao = DappStarDao()
            dappStarDao.id = datum.id
            dappStarDao.url = datum.url
            dappStarDao.name = datum.name
            dappStarDao.icon_bitmap = datum.icon_bitmap
            dappStarDao.icon = datum.icon
            dappStarDao.des = datum.des
            dappStarDao.sort = datum.sort
            dappStarDao.save()
        }
    }


}