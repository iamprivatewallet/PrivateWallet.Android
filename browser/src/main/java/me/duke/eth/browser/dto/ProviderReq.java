package me.duke.eth.browser.dto;

import androidx.annotation.Keep;

@Keep
public class ProviderReq {
    private String name;
    private RpcReq data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RpcReq getData() {
        return data;
    }

    public void setData(RpcReq data) {
        this.data = data;
    }
}
