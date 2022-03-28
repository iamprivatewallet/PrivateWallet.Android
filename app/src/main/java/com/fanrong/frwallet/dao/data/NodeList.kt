package com.fanrong.frwallet.dao.data

import com.fanrong.frwallet.dao.database.ChainNodeDao
import com.fanrong.frwallet.dao.database.ChainNodeOperator

object NodeList {

    var CURRENT_ETH = ""
        get() {
            return ChainNodeOperator.queryCurrentNode("ETH").nodeUrl!!
        }
    var CURRENT_HECO = ""
        get() {
            return ChainNodeOperator.queryCurrentNode("HECO").nodeUrl!!
        }
    var CURRENT_BSC = ""
        get() {
            return ChainNodeOperator.queryCurrentNode("BSC").nodeUrl!!
        }

    var CURRENT_CVN = "http://52.220.97.222:1235"
        get() {
            return ChainNodeOperator.queryCurrentNode("CVN").nodeUrl!!
        }


    fun getCurrentNodeObj(type: String): ChainNodeDao {

        return when (type) {
            "ETH" -> {
                ChainNodeOperator.queryCurrentNode("ETH")
            }
            "HECO" -> {

                ChainNodeOperator.queryCurrentNode("HECO")
            }
            "BSC" -> {

                ChainNodeOperator.queryCurrentNode("BSC")
            }
            "CVN" -> {

                ChainNodeOperator.queryCurrentNode("CVN")
            }
            else -> {
                throw RuntimeException("不支持主链")
            }
        }
    }

    fun getCurrentNode(type: String): String {
        return when (type) {
            "ETH" -> {
                CURRENT_ETH
            }
            "HECO" -> {
                CURRENT_HECO
            }
            "BSC" -> {
                CURRENT_BSC
            }
            "CVN" -> {
                CURRENT_CVN
            }
            else -> {
                ""
            }
        }
    }


}