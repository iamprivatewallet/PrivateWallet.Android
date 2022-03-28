package com.fanrong.frwallet.tools;

import android.webkit.ValueCallback;

public class Web3JsUtils {
    public static void signStr(String content, String privateKey, ValueCallback<String> valueCallback) {
        final String english = String.format("signStr('%s','%s')", content, privateKey);
        CallJsCodeUtils.getJsHandler().evaluateJavascript(english, valueCallback);
    }

    public static void getContractTokenNameAbi(ValueCallback<String> valueCallback) {
        final String english = String.format("getSymbolAbi()");
        CallJsCodeUtils.getJsHandler().evaluateJavascript(english, valueCallback);
    }

    public static void contractTokenDecode(String data, ValueCallback<String> valueCallback) {
        final String english = String.format("getSymbolResultDecode('%s')", data);
        CallJsCodeUtils.getJsHandler().evaluateJavascript(english, valueCallback);
    }

    public static void getDecimalsAbiEncode(ValueCallback<String> valueCallback) {
        final String english = String.format("getDecimalsAbiEncode()");
        CallJsCodeUtils.getJsHandler().evaluateJavascript(english, valueCallback);
    }

    public static void getDecimalsResultDecode(String data, ValueCallback<String> valueCallback) {
        final String english = String.format("getDecimalsResultDecode('%s')", data);
        CallJsCodeUtils.getJsHandler().evaluateJavascript(english, valueCallback);
    }

    public static void eth_encrypt(String privateKey, String password, ValueCallback<String> valueCallback) {
        final String english = String.format("JSON.stringify(web3.eth.accounts.privateKeyToAccount('%s').encrypt('%s'))", privateKey, password);
        CallJsCodeUtils.getJsHandler().evaluateJavascript(english, valueCallback);
    }

    public static void eth_decrypt(String keystoreJson, String password, ValueCallback<String> valueCallback) {
        final String english = String.format("JSON.stringify(web3.eth.accounts.decrypt('%s','%s'))", keystoreJson, password);
        CallJsCodeUtils.getJsHandler().evaluateJavascript(english, valueCallback);
    }


}
