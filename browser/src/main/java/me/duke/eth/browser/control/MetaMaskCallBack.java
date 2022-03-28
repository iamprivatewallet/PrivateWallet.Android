package me.duke.eth.browser.control;

import me.duke.eth.browser.dto.RpcResp;

public interface MetaMaskCallBack<T extends RpcResp<?>> {

    void callback(T resp);

}
