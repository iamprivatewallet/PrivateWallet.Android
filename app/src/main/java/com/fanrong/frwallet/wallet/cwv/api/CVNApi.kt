package com.fanrong.frwallet.wallet.cwv.api

import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.tools.HEADER_BASE_URL
import com.fanrong.frwallet.wallet.cwv.api.models.*
import io.reactivex.Observable
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.brewchain.sdk.HiChain
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import xc.common.framework.net.NetClient
import xc.common.tool.utils.XcSingleten

object CVNApi {
    suspend fun susGetChainTokenBalance(addr: String): UserInfoModel {
        var result = ""
        var job = GlobalScope.tryLaunch {
            result = HiChain.getUserInfo(addr)
        }
        job.join()

        return XcSingleten.gson.fromJson(result, UserInfoModel::class.java)
    }

    fun getChainTokenBalance(addr: String): UserInfoModel {
        var result = HiChain.getUserInfo(addr)
        return XcSingleten.gson.fromJson(result, UserInfoModel::class.java)
    }

    suspend fun getContractTokenBalance(addr: String, contract: String): UserInfoModel {
        var result = ""
        var job = GlobalScope.launch {
            result = HiChain.getUserRC20Info(addr, contract)
        }
        job.join()

        return XcSingleten.gson.fromJson(result, UserInfoModel::class.java)
    }
}


val cvnInterfaceApi: CVNInterfaceApi by lazy {
    NetClient.getApi(CVNInterfaceApi::class.java)!!
}

const val HOST: String = "http://52.220.97.222:1235"

interface CVNInterfaceApi {


    @POST(HOST)
    fun call(
        @Body req: CVNCallReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_CVN + "/fbs/cvm/pbcal.do"
    ): Observable<CVNCallResp>

    @POST(HOST)
    suspend fun susCall(
        @Body req: CVNCallReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_CVN + "/fbs/cvm/pbcal.do"
    ): Response<CVNCallResp>

    @POST(HOST)
    suspend fun susCall_getBalance(
        @Body req: CVNGetBalanceReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_CVN + "/fbs/cvm/pbcal.do"
    ): Response<CVNGetBalanceResp>


    @POST(HOST)
    suspend fun call_sendTransaction(
        @Body req: CVNGetBalanceReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_CVN + "/fbs/cvm/pbcal.do"
    ): Response<CVNGetBalanceResp>




    @POST(HOST)
    fun call_hashInfo(
        @Body req: CVNHashInfoReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_CVN + "/fbs/tct/pbgth.do"
    ): Call<CVNHashInfoResp>


    @POST(HOST)
    fun call_sendTx(
        @Body req: CVNSendTxReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_CVN + "/fbs/tct/pbmtx.do"
    ): Call<CVNSendTxResp>


}