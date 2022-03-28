package com.fanrong.frwallet.wallet.cwv.api;

import com.fanrong.frwallet.wallet.cwv.SignTxResp;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * 调用节点 接口，切换节点会重新创建 实例
 */
public interface FBCNodeApi {

    @POST("/fbs/tct/pbmtx.do")
    Call<SignTxResp> sendSignTx(@Body RequestBody map);
}
