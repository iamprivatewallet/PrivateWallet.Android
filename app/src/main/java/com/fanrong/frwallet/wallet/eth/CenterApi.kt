package com.fanrong.frwallet.wallet.eth

import com.fanrong.frwallet.wallet.eth.eth.*
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import xc.common.framework.net.NetClient


val centerApi: CenterApi by lazy {
    NetClient.getApi(CenterApi::class.java)!!
}

const val CENTER_HOST: String = "https://chain.kimchiii.com/"

interface CenterApi {

    @POST(CENTER_HOST + "wallet/getTransactionInfo")
    suspend fun getTransactionRecordResp(@Body req: GetTransactionRecordReq): Response<GetTransactionRecordResp>


    @POST(CENTER_HOST + "wallet/getWattleSysMessageList")
    suspend fun getSysMessageList(@Body req: GetSysMsgReq): Response<GetSysMsgResp>

    @POST(CENTER_HOST + "/wallet/geWattleToken")
    fun queryTokens(@Body req: QueryTokensReq): Observable<QueryTokensResp>


    //查询token价格
    @POST(CENTER_HOST + "api/wallet/token/price")
    fun queryTokenPrice(@Body req: QueryTokenPriceReq): Observable<QueryTokenPriceResp>

    //查询token价格
    @POST(CENTER_HOST + "api/wallet/token/price")
    suspend fun queryTokenPrice2(@Body req: QueryTokenPriceReq): Response<QueryTokenPriceResp>

    //获取主链网络配置
    @POST(CENTER_HOST + "api/wallet/token/chain")
    fun queryNetworks(@Body req: NetWorkManagerReq): Observable<NetWorkManagerResp>

    //查询dapp页数据
    @POST(CENTER_HOST + "api/wallet/dapp/list")
    fun queryDapp(@Body req: QueryDappReqNew): Observable<QueryDappRespNew>

    //币种coin
    @POST(CENTER_HOST + "api/wallet/token/icon")
    fun queryCoin(@Body req: QueryCoinReq): Observable<QueryCoinResp>

    //app版本更新
    @POST(CENTER_HOST + "api/wallet/version/last")
    fun queryAppVersion(@Body req: QueryAppVersionReq): Observable<QueryAppVersionResp>

    //转账通知分页列表（消息中心）
    @POST(CENTER_HOST + "api/wallet/message/hash/page")
    suspend fun queryTransactionPage(@Body req: QueryTransactionPageReq): Response<QueryTransactionPageResp>

    //系统消息分页列表（消息中心）
    @POST(CENTER_HOST + "api/wallet/message/sys/page")
    suspend fun querySystemMessage(@Body req: QueryTransactionPageReq): Response<QueryTransactionPageResp>

    //系统消息分页列表（消息中心）
    @POST(CENTER_HOST + "api/wallet/message/sys/item")
    fun querySystemMessageDetail(@Body req: QuerySystemMessageDetailReq): Observable<QuerySystemMessageDetailResp>


}