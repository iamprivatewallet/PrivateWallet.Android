package com.fanrong.frwallet.dao.database

import org.litepal.LitePal
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty

object ChainNodeOperator {

    fun changeNode(chainNodeDao: ChainNodeDao) {
        val find = LitePal.where("chainType=? and isCurrent=?", chainNodeDao.chainType, "1").find(ChainNodeDao::class.java)
        if (find.checkNotEmpty()) {
            for (chainNodeDao in find) {
                chainNodeDao.isCurrent = 0
                chainNodeDao.save()
            }
        }

        chainNodeDao.isCurrent = 1
        chainNodeDao.save()
    }

    fun addNode(chainNodeDao: ChainNodeDao): Boolean {
        return chainNodeDao.save()
    }

    fun updateNode(chainNodeDao: ChainNodeDao, id: Long): Boolean {
        return chainNodeDao.update(id) == 1
    }

    fun queryNodeList(chainType: String): MutableList<ChainNodeDao>? {
        return LitePal.where("chainType=?", chainType).find(ChainNodeDao::class.java)
    }

    fun queryCurrentNode(chainType: String): ChainNodeDao {
        val find = LitePal.where("chainType=? and isCurrent=?", chainType, "1").find(ChainNodeDao::class.java)
        if (find.checkIsEmpty()) {
            return LitePal.where("chainType=? and netType=?", chainType, "1").find(ChainNodeDao::class.java)[0]
//            throw RuntimeException("当前配置节点为空")
        } else {
            return find[0]
        }
    }

    fun delete(id: Long) {
        LitePal.delete(ChainNodeDao::class.java, id)
    }

}