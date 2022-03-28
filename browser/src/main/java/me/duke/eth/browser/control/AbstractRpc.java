package me.duke.eth.browser.control;

import androidx.annotation.NonNull;

import me.duke.eth.browser.dto.*;


public interface AbstractRpc {

    void rpcHandler(@NonNull RpcReq request, @NonNull MetaMaskCallBack<RpcResp<?>> callBack);

}