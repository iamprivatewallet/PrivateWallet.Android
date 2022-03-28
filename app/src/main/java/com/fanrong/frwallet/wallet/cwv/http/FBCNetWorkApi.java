package com.fanrong.frwallet.wallet.cwv.http;

import com.fanrong.frwallet.wallet.cwv.model.CWVCoinType;
import com.fanrong.frwallet.wallet.cwv.model.NodeListResp;
import com.fanrong.frwallet.wallet.cwv.model.QueryContentInfoResp;
import com.fanrong.frwallet.wallet.cwv.model.TransactionRecordDetailResp;
import com.fanrong.frwallet.wallet.cwv.model.TransactionRecordResp;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FBCNetWorkApi {


    String FBC_NODE = "http://52.220.97.222:8000";

    String FBC_ROOT = "http://scan.cwv.one/api/";
//        String FBC_ROOT = "http://8.210.164.131:8000/browser/";
//    String FBC_ROOT = "https://dev.wallet.icwv.co/";
    //CWV代币查询


    @POST(FBC_ROOT + "bca/pbnls.do")
    Call<NodeListResp> requestNodeList(@Body RequestBody map);


    /**
     * 查询交易记录
     */
    @POST(FBC_ROOT + "bca/pbatl.do")
    Call<TransactionRecordResp> queryTransactionRecord(@Body RequestBody map);

    @POST(FBC_ROOT + "bro/app/pbtok.do")
    Call<CWVCoinType> CWVCoinType(@Body RequestBody map);

    @POST(FBC_ROOT + "bca/pbtds.do")
    Call<TransactionRecordDetailResp> orderDetail(@Body RequestBody map);

    @POST(FBC_ROOT + "bca/pbser.do")
    Call<QueryContentInfoResp> checkIsAddr(@Body RequestBody map);

}
