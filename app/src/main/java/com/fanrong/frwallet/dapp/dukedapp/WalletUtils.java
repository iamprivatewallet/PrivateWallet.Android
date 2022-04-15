package com.fanrong.frwallet.dapp.dukedapp;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.FunctionReturnDecoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Bytes32;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthCall;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WalletUtils {
    public interface IRPCRequestSuccess{
        void RPCSuccess(String ruselt);
    }

    private static WalletUtils walletUtils;

    public static WalletUtils getInstance(){
        if (walletUtils == null)
            walletUtils = new WalletUtils();
        return walletUtils;
    }

    public void Init(){}

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。
    public static void _CallToContract(String id, String jsonrpc,String RpcUrl, String method, JSONArray paramsArray, IRPCRequestSuccess callback){

        JSONObject jsonObject = new JSONObject();

        JSONArray jsonArray1 = new JSONArray();
        JSONObject jsonObject1 =null;
        try {
            //封装最外层的包含数组的结构
            jsonObject.put("id",id);
            jsonObject.put("jsonrpc",jsonrpc);
            jsonObject.put("method",method);
            if (paramsArray!=null && paramsArray.length()>0)
                jsonObject.put("params",paramsArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("sdgsdgsgsgsssgsg", "postDataWithParame: ------>>>>>>>>>>"+jsonObject.toString());
        RequestBody body = RequestBody.create(JSON,jsonObject.toString());
        Request request = new Request.Builder()//创建Request 对象。
                .url(RpcUrl)
                .post(body)//传递请求体
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("sdfgdsfaq", "onResponse: 2");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject1 = new JSONObject(response.body().string());
                    callback.RPCSuccess(jsonObject1.optString("result"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });//回调方法的使用与get异步请求相同，此时略。
    }
}
