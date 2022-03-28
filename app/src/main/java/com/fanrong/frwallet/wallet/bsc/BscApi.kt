package com.fanrong.frwallet.wallet.bsc

import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.tools.HEADER_BASE_URL
import com.fanrong.frwallet.wallet.eth.eth.*
import io.reactivex.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import xc.common.framework.net.NetClient


val bscApi: BscApi by lazy {
    NetClient.getApi(BscApi::class.java)!!
}

const val HOST: String = "https://bsc-dataseed1.ninicoin.io" //https://bsc-dataseed1.ninicoin.io   http://94.74.87.188:8545/

interface BscApi {


    @POST(HOST)
    suspend fun susQueryEthBalance(
        @Body req: QueryEthBalanceReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Response<QueryEthBalanceResp>

//    @POST(HOST)
//    fun queryEthTokenBalance(@Body req: QueryEthTokenBalanceReq): Observable<QueryEthTokenBalanceResp>

    @POST(HOST)
    suspend fun susQueryEthTokenBalance(
        @Body req: QueryEthTokenBalanceReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Response<QueryEthTokenBalanceResp>

    @POST(HOST)
    suspend fun susEthTransfer(
        @Body req: EthTransferReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Response<EthTransferResp>

    @POST(HOST)
    fun ethTransfer(
        @Body req: EthTransferReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Call<EthTransferResp>

    @POST(HOST)
    fun unlockaccount(
        @Body req: EthUnLockReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Call<EthTransferResp>

    @POST(HOST)
    fun ethTransferInfo(
        @Body req: EthTransferInfoReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Call<EthTransferInfoResp>


    @POST(HOST)
    suspend fun susGasPrice(
        @Body req: GasPriceReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Response<GasPriceResp>

    @POST(HOST)
    fun gasPrice(
        @Body req: GasPriceReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Call<GasPriceResp>

    @POST(HOST)
    suspend fun susGetNoce(
        @Body req: GetNoceReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Response<GetNoceResp>

    @POST(HOST)
    fun getNoce(
        @Body req: GetNoceReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Call<GetNoceResp>

    /**
     * 查询 详细信息
     */
    @POST(HOST)
    fun eth_getTransactionByHash(
        @Body req: Eth_GetTransactionByHashReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Call<Eth_GetTransactionByHashResp>

//
//    @POST(HOST)
//    fun getNoce1(
//        @Body req: GetNoceReq.RpcParamter,
//        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_ETH
//    ): Call<GetNoceResp>
//

    @POST(HOST)
    fun call(
        @Body req: CallReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Observable<CallResp>

    @POST(HOST)
    suspend fun susCall(
        @Body req: CallReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Response<CallResp>


    @POST(HOST)
    fun rpcRequest(
        @Body req: RpcRequestReq,
        @Header(HEADER_BASE_URL) url: String = NodeList.CURRENT_BSC
    ): Observable<RpcRequestResp>

}