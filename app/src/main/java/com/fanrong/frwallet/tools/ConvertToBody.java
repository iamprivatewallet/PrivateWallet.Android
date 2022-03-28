package com.fanrong.frwallet.tools;

import com.google.gson.Gson;

import okhttp3.RequestBody;

public class ConvertToBody {
    public static RequestBody ConvertToBody(Object paremter){
        String jsonParemter = new Gson().toJson(paremter);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json;charset=utf-8"), jsonParemter);
        return body;
    }
}
