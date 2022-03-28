package com.fanrong.frwallet.task

import com.fanrong.frwallet.dao.database.TransferDao
import com.fanrong.frwallet.dao.database.TransferOperator
import com.fanrong.frwallet.dao.eventbus.TransferFinishEvent
import com.fanrong.frwallet.tools.extGwei2Wei
import com.fanrong.frwallet.tools.extHexToTen
import com.fanrong.frwallet.wallet.bsc.bscApi
import com.fanrong.frwallet.wallet.cwv.api.cvnInterfaceApi
import com.fanrong.frwallet.wallet.cwv.api.models.CVNHashInfoReq
import com.fanrong.frwallet.wallet.eth.eth.EthTransferInfoReq
import com.fanrong.frwallet.wallet.eth.eth.Eth_GetTransactionByHashReq
import com.fanrong.frwallet.wallet.eth.eth.ethApi
import com.fanrong.frwallet.wallet.heco.hecoApi
import org.greenrobot.eventbus.EventBus
import xc.common.tool.utils.checkNotEmpty
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

object UpdateTransactionsTask {


    val runnable: Runnable = Runnable {

        try {
            var onGoings = TransferOperator.queryPendingOrder().toMutableList()
            var hasStateChange = false
            if (onGoings.checkNotEmpty()) {
                for (onGoing in onGoings) {
                    try {
                        var oldState = onGoing.status
                        if (onGoing.uniqueToken.startsWith("CVN")) {
                            hasStateChange = updateCVNTransaction(onGoing, oldState, hasStateChange)
                        } else if (onGoing.uniqueToken.startsWith("ETH")) {
                            hasStateChange = updateETHTransaction(onGoing, oldState, hasStateChange)
                        } else if (onGoing.uniqueToken.startsWith("HECO")) {
                            hasStateChange = updateHECOTransaction(onGoing, oldState, hasStateChange)
                        } else if (onGoing.uniqueToken.startsWith("BSC")) {
                            hasStateChange = updateBSCTransaction(onGoing, oldState, hasStateChange)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            // 状态变化发通知
            if (hasStateChange) {
                EventBus.getDefault().post(TransferFinishEvent())
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    private fun updateETHTransaction(
        onGoing: TransferDao,
        oldState: String?,
        hasStateChange: Boolean
    ): Boolean {
        var hasStateChange1 = hasStateChange
        val body = ethApi.ethTransferInfo(EthTransferInfoReq(onGoing.transaction_hash!!),url = onGoing.node!!).execute().body()!!
        onGoing.gasUsed = body.result!!.gasUsed?.extHexToTen()
        when (body.result!!.status) {
            "0x1" -> {
                onGoing.status = "D"
            }
            "0x0" -> {
                onGoing.status = "E"
            }
            else -> {
                onGoing.status = "E"
            }
        }
        if (!oldState.equals(onGoing.status)) {
            val body = ethApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(onGoing.transaction_hash!!)).execute().body()!!
            onGoing.gasPrice = body.result?.gasPrice?.extHexToTen().extGwei2Wei()
            hasStateChange1 = true
            onGoing.save()
        }
        return hasStateChange1
    }

    private fun updateHECOTransaction(
        onGoing: TransferDao,
        oldState: String?,
        hasStateChange: Boolean
    ): Boolean {
        var hasStateChange1 = hasStateChange
        val body = hecoApi.ethTransferInfo(EthTransferInfoReq(onGoing.transaction_hash!!),url = onGoing.node!!).execute().body()!!
        onGoing.gasUsed = body.result!!.gasUsed?.extHexToTen()
        when (body.result!!.status) {
            "0x1" -> {
                onGoing.status = "D"
            }
            "0x0" -> {
                onGoing.status = "E"
            }
            else -> {
                onGoing.status = "E"
            }
        }
        if (!oldState.equals(onGoing.status)) {
            val body = ethApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(onGoing.transaction_hash!!)).execute().body()!!
            onGoing.gasPrice = body.result?.gasPrice?.extHexToTen()
            hasStateChange1 = true
            onGoing.save()
        }
        return hasStateChange1
    }

    private fun updateBSCTransaction(
        onGoing: TransferDao,
        oldState: String?,
        hasStateChange: Boolean
    ): Boolean {
        var hasStateChange1 = hasStateChange
        val body = bscApi.ethTransferInfo(EthTransferInfoReq(onGoing.transaction_hash!!),url = onGoing.node!!).execute().body()!!
        onGoing.gasUsed = body.result!!.gasUsed
        when (body.result!!.status) {
            "0x1" -> {
                onGoing.status = "D"
            }
            "0x0" -> {
                onGoing.status = "E"
            }
            else -> {
                onGoing.status = "E"
            }
        }
        if (!oldState.equals(onGoing.status)) {
            val body = ethApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(onGoing.transaction_hash!!)).execute().body()!!
            onGoing.gasPrice = body.result?.gasPrice?.extHexToTen()
            hasStateChange1 = true
            onGoing.save()
        }
        return hasStateChange1
    }

    private fun updateCVNTransaction(
        onGoing: TransferDao,
        oldState: String?,
        hasStateChange: Boolean
    ): Boolean {
        var hasStateChange1 = hasStateChange
        val body = cvnInterfaceApi.call_hashInfo(CVNHashInfoReq(onGoing.transaction_hash!!),url = onGoing.node + "/fbs/tct/pbgth.do").execute().body()!!
//        onGoing.gas = body.result!!.gasUsed
        when (body.status?.status ?: "") {
            "0x01" -> {
                onGoing.status = "D"
            }
            "0xff" -> {
                onGoing.status = "E"
            }
            else -> {
                onGoing.status = "P"
            }
        }
        if (!oldState.equals(onGoing.status)) {
            hasStateChange1 = true
            onGoing.save()
        }
        return hasStateChange1
    }

    fun start() {
        val newSingleThreadScheduledExecutor = Executors.newSingleThreadScheduledExecutor()
        newSingleThreadScheduledExecutor.scheduleAtFixedRate(runnable, 1000, 2000, TimeUnit.MILLISECONDS)
    }

}