package me.duke.eth.browser.control;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import androidx.annotation.Keep;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

import me.duke.eth.browser.dto.ProviderReq;
import me.duke.eth.browser.dto.ProviderResp;
import me.duke.eth.browser.dto.RpcReq;
import me.duke.eth.browser.widget.Web3View;

public class MetaMaskFlow {

    public Map<Uri, MetaMaskControl> web3ViewMetaMaskControlMap = new HashMap<>();

    private final static String META_MASK_TAG = "MetaMask-JS";

    public Web3View web3View;

    public MetaMaskFlow(Web3View web3View) {
        this.web3View = web3View;
    }

    @JavascriptInterface
    @Keep
    public void onMessage(Object object) {
        JSONObject json = JSON.parseObject(object.toString());
        if ("FRAME_READY".equals(json.getString("type"))) {
            Uri uri = Uri.parse(json.getString("payload"));
            MetaMaskControl control = new MetaMaskControl(web3View,uri.toString(),true);
            control.onChange();
            web3ViewMetaMaskControlMap.put(uri,control);
            AdapterWallet.getInstance().addOnChangeListener(control);
        } else if (json.getString("name") != null) {
            String name = json.getString("name");
            if ("provider".equals(name)) {
                String origin = json.getString("origin");
                if(TextUtils.isEmpty(origin)){
                    Log.d(META_MASK_TAG, "origin 为空，没有安全域，出错");
                    return;
                }
//                Uri uri = Uri.parse(origin);
//                String http = uri.getScheme()+":"+uri.getSchemeSpecificPart();
//                uri = Uri.parse(http);
//                MetaMaskControl control = web3ViewMetaMaskControlMap.get(uri);

                Uri uri = Uri.parse(origin);
                MetaMaskControl control = new MetaMaskControl(web3View,uri.toString(),false);
                control.onChange();
                web3ViewMetaMaskControlMap.put(uri,control);
                AdapterWallet.getInstance().addOnChangeListener(control);
                if(control == null){
                    Log.d(META_MASK_TAG, "origin:"+origin+" 没有安全授权，出错");
                    return;
                }
                json.toJavaObject(ProviderReq.class);
                ProviderReq provider = json.toJavaObject(ProviderReq.class);
                RpcReq rpcReq = provider.getData();
                if (rpcReq == null) {
                    Log.d(META_MASK_TAG, "name:" + name + " 无数据，出错");
                    return;
                }
                String method = rpcReq.getMethod();
                if (method == null) {
                    Log.d(META_MASK_TAG, "无请求方法,出错");
                    return;
                }
                if (rpcReq.getId() == null || rpcReq.getId() == 0) {
                    Log.d(META_MASK_TAG, "无请求ID,出错");
                    return;
                }
                if(!rpcReq.isToNative()){
                    Log.i(META_MASK_TAG, "method: "+method+" 不需要原生支持，被驳回");
                    return;
                }
                AdapterWallet.getInstance().getRpc().rpcHandler(rpcReq,resp -> control.postMessage(new ProviderResp<>(resp)));
//                switch (method) {
//                    case "eth_accounts":
//                    case "eth_requestAccounts": {
//                        AdapterWallet.getInstance().getRpc().ethAccounts(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "eth_coinbase": {
//                        AdapterWallet.getInstance().getRpc().ethCoinbase(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "eth_getBalance":{
//                        AdapterWallet.getInstance().getRpc().ethGetBalance(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "net_version": {
//                        AdapterWallet.getInstance().getRpc().netVersion(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "web3_clientVersion": {
//                        AdapterWallet.getInstance().getRpc().web3ClientVersion(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "eth_sign": {
//                        AdapterWallet.getInstance().getRpc().ethSign(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "personal_sign": {
//                        AdapterWallet.getInstance().getRpc().personalSign(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "eth_signTypedData": {
//                        AdapterWallet.getInstance().getRpc().ethSignTypeData(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "eth_signTypedData_v3": {
//                        AdapterWallet.getInstance().getRpc().ethSignTypeDataV3(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "eth_signTypedData_v4": {
//                        AdapterWallet.getInstance().getRpc().ethSignTypeDataV4(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "eth_blockNumber":{
//                        AdapterWallet.getInstance().getRpc().ethBlockNumber(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    case "eth_call":{
//                        AdapterWallet.getInstance().getRpc().ethCall(rpcReq, resp -> control.postMessage(new ProviderResp<>(resp)));
//                        break;
//                    }
//                    default: {
//                        Log.d(META_MASK_TAG, "method:" + method + " 暂不支持");
//                        break;
//                    }
//                }
            } else {
                Log.d(META_MASK_TAG, "未知 name:" + name + " 暂不支持");
            }
        }
    }

//    public static void clearWebView(Web3View web3View) {
//        if (web3View == null)
//            return;
//        web3View.removeJavascriptObject("ETH");
//        MetaMaskFlow metaMaskFlow = web3ViewMetaMaskFlowMap.get(web3View);
//        if (metaMaskFlow == null)
//            return;
//        AdapterWallet.getInstance().removeOnchangeListener(metaMaskFlow.control);
//        metaMaskFlow.control.setMWeb3View(null);
//        web3ViewMetaMaskFlowMap.remove(web3View);
//    }
}
