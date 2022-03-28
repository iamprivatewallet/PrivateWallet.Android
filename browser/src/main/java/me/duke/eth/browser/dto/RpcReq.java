package me.duke.eth.browser.dto;

import androidx.annotation.Keep;

import java.util.List;

@Keep
public class RpcReq {
    private String jsonrpc;
    private String method;
    private List<Object> params;
    private Long id;
    private boolean toNative;

    public String getJsonrpc() {
        return jsonrpc;
    }

    public void setJsonrpc(String jsonrpc) {
        this.jsonrpc = jsonrpc;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isToNative() {
        return toNative;
    }

    public void setToNative(boolean toNative) {
        this.toNative = toNative;
    }
}
