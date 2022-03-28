package com.fanrong.frwallet.wallet.cwv.api;

import com.fanrong.frwallet.wallet.cwv.http.FBCNetWorkApi;
import com.fanrong.frwallet.wallet.cwv.model.WalletTransferModel;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ETHNetWorkApi {

    /**
     * 代币-转账
     *
     * @param map
     * @return
     */
    @POST(FBCNetWorkApi.FBC_ROOT + "eth/pbtxt.do")
    Call<WalletTransferModel> transfer(@Body RequestBody map);

}
