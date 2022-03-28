package com.fanrong.frwallet.tools;

import android.annotation.SuppressLint;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.lifecycle.LifecycleOwner;

import com.fanrong.frwallet.main.MyApplication;

import org.brewchain.core.crypto.cwv.util.BytesHelper;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import xc.common.framework.lifecycle.OnDestroyObserver;
import xc.common.tool.CommonTool;
import xc.common.tool.utils.SWLog;

public class CallJsCodeUtils {

    private static WebView webView;

    public static WebView getJsHandler() {
        return webView;
    }

    public static void init() {
        webView = getWebView();
    }


    static class Callback {
        @JavascriptInterface
        public void getContractInfoResult(String result) {
            if (results.get("getContractInfo") != null) {
                results.get("getContractInfo").onReceiveValue(result);
            }
            SWLog.e(result);
        }
    }

    @SuppressLint("JavascriptInterface")
    public static WebView getWebView() {
        final WebView webView = new WebView(MyApplication.context);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        webView.setWebViewClient(new WebViewClient());
        webView.addJavascriptInterface(new Callback(), "frwallet");
        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
                SWLog.e("onConsoleMessage：：" + consoleMessage.message() +
                        "----" + consoleMessage.lineNumber() +
                        "====" + consoleMessage.sourceId());
                return super.onConsoleMessage(consoleMessage);
            }
        });
        webView.loadUrl("file:///android_asset/www/index.html");

        return webView;
    }

    public static Map<String, ValueCallback<String>> results = new HashMap<>();

    public static void getContractInfo(ValueCallback callback, LifecycleOwner owner) {
        owner.getLifecycle().addObserver(new OnDestroyObserver() {
            @Override
            public void onCallDestroy() {
                results.remove("getContractInfo");
            }
        });
        results.put("getContractInfo", callback);
        webView.evaluateJavascript("getContractInfo('0x229ca99574909692ec72e96053dcafd91de951e2')", null);
    }


    public static String readStringJsValue(String jsValue) {
        JsonReader reader = new JsonReader(new StringReader(jsValue));
        // Must set lenient to parse single values
        reader.setLenient(true);
        try {
            if (reader.peek() != JsonToken.NULL) {
                if (reader.peek() == JsonToken.STRING) {
                    String msg = reader.nextString();
                    return msg;
                }
            }
        } catch (IOException e) {
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                // NOOP
            }
        }
        return null;
    }


    public static void generateMnemonic(ValueCallback<String> valueCallback) {
        final String english = String.format("bip44.generateMnemonic(null,null, bip44.wordlists.EN);");
        webView.evaluateJavascript(english, valueCallback);

    }

    public static void mnemonicToHDPrivateKey(String mnemonicStr, ValueCallback<String> valueCallback) {

        final String english = String.format("var base58 = bip44.mnemonicToHDPrivateKey('%s','');base58.xprivkey", mnemonicStr);
        webView.evaluateJavascript(english, valueCallback);

    }

    public static void getAddress(String dPrivateKey, ValueCallback<String> valueCallback) {

        final String english = String.format("bip44.getAddress('%s',0);", dPrivateKey);
        webView.evaluateJavascript(english, valueCallback);

    }

    public static void getPrivateKey(String dPrivateKey, ValueCallback<String> valueCallback) {

        final String english = String.format("bip44.getPrivateKey('%s',0).toString('hex');", dPrivateKey);
        webView.evaluateJavascript(english, valueCallback);

    }


    public static void privateToAddress(String privateKey, ValueCallback<String> valueCallback) {
        // ETH 通过私钥获取 地址，CWV 私钥和ETH 生成出来是一样的
        final String english = String.format("bip44.privateToAddress('%s');", privateKey);
        webView.evaluateJavascript(english, valueCallback);

    }

    public static void cwv_GenFromPrikey(String dPrivateKey, ValueCallback<String> valueCallback) {

        final String english = String.format("android_funcCreateWallet('%s');", dPrivateKey);
        webView.evaluateJavascript(english, valueCallback);

    }


    public static void cwv_ecHexSign(String dPrivateKey, String hexStr, ValueCallback<String> valueCallback) {
        String script = String.format("chain.default.KeyPair.genCVNFromPrikey('%s').ecHexSign('%s')"
                , dPrivateKey, hexStr);
        webView.evaluateJavascript(script, valueCallback);

    }


    public static void eth_outKeystore(String addr, String privateKey, String pwd, ValueCallback<String> valueCallback) {
        final String english = String.format("chain.default.keystore.exportJSON(JSON.parse('{\"hexPrikey\":\"%s\",\"hexPubkey\":\"\",\"hexAddress\":\"%s\",\"nonce\":0}'),'%s')"
                , privateKey, addr, pwd);
        webView.evaluateJavascript(english, valueCallback);
    }

    //
    public static class EthParamter {
        public String nonce;
        public String gasPrice;
        public String to;
        public String value;
        public String data;
        public String gasLimit = "0x61A80";
        public String chainId = "1337";
    }
    public static class callParams{
        public String nonce;
        public String gasPrice;
        public String to;
        public String value;
        public String data;
        public String gasLimit = "0xea60";
        public String chainId = "56";

        @Override
        public String toString() {
            return "BscParamter{" +
                    "nonce='" + nonce + '\'' +
                    ", gasPrice='" + gasPrice + '\'' +
                    ", to='" + to + '\'' +
                    ", value='" + value + '\'' +
                    ", data='" + data + '\'' +
                    ", gasLimit='" + gasLimit + '\'' +
                    ", chainId='" + chainId + '\'' +
                    '}';
        }
    }

    public static void eth_signStr(EthParamter paramter, String privateKey, ValueCallback<String> valueCallback) {
        String singe = "var tx = new ethsign({";
        if (paramter.nonce != null) {
            singe += String.format("nonce:'%s',", paramter.nonce);
        }
        if (paramter.gasPrice != null) {
            singe += String.format("gasPrice:'%s',", paramter.gasPrice);
        }
        if (paramter.gasLimit != null) {
            singe += String.format("gasLimit:'%s',", paramter.gasLimit);
        }
        if (paramter.to != null) {
            singe += String.format("to:'%s',", paramter.to);
        }
        if (paramter.value != null) {
            singe += String.format("value:'%s',", paramter.value);
        }
        if (paramter.data != null) {
            singe += String.format("data:'%s',", paramter.data);
        }
        if (paramter.chainId != null) {
            singe += String.format("chainId:'%s',", paramter.chainId);
        }
        singe = singe.substring(0, singe.length() - 1);
        singe += String.format("});tx.sign('%s');tx.serialize().toString('hex')", privateKey);

        SWLog.e(singe);
        webView.evaluateJavascript(singe, valueCallback);

    }

    public static void eth_ecHexSign(EthParamter paramter, String privateKey, ValueCallback<String> valueCallback) {
        String singe = "var tx = new ethsign({" +
                "nonce:'%s'," +
                "gasPrice:'%s'," +
                "gasLimit:'%s'," +
                "to:'%s'," +
                "value:'%s'," +
                "data:'%s'," +
                "chainId:%d" +
                "});tx.sign('%s');tx.serialize().toString('hex')";


        String format = String.format(singe,
                paramter.nonce,
                paramter.gasPrice,
                paramter.gasLimit,
                paramter.to,
                paramter.value,
                paramter.data,
                Integer.valueOf(paramter.chainId),
                privateKey);

        SWLog.e(format);
        webView.evaluateJavascript(format, valueCallback);

    }

    public static void bsc_ecHexSign(callParams paramter, String privateKey, ValueCallback<String> valueCallback) {
        String singe = "var tx = new ethsign({" +
                "nonce:'%s'," +
                "gasPrice:'%s'," +
                "gasLimit:'%s'," +
                "to:'%s'," +
                "value:'%s'," +
                "data:'%s'," +
                "chainId:%d" +
                "});tx.sign('%s');tx.serialize().toString('hex')";


        String format = String.format(singe,
                paramter.nonce,
                paramter.gasPrice,
                paramter.gasLimit,
                paramter.to,
                paramter.value,
                paramter.data,
                Integer.valueOf(paramter.chainId),
                privateKey);

        SWLog.e(format);

        webView.evaluateJavascript(format, valueCallback);

    }

    public static void ecHexSign_eth(callParams paramter, String privateKey, ValueCallback<String> valueCallback){
        //发送主链币开始
        String fromAddress = Keys
                .toChecksumAddress(Keys.getAddress(ECKeyPair.create(Numeric.toBigInt(privateKey))));

        String _nonce = BytesHelper.hexStr2BigDecimal(paramter.nonce, 0, 0).toPlainString();
        String _gasLimit = BytesHelper.hexStr2BigDecimal(paramter.gasLimit, 0, 0).toPlainString();
        String _gasPrice = BytesHelper.hexStr2BigDecimal(paramter.gasPrice, 0, 0).toPlainString();
        String _value = BytesHelper.hexStr2BigDecimal(paramter.value, 0, 0).toPlainString();

        BigInteger nonce = new BigInteger(_nonce);

        // 支付的矿工费
        BigInteger Gwei2WeiDecimal = new BigInteger("1000000000");
        BigInteger gasPrice = new BigInteger(_gasPrice);
        BigInteger gasLimit = new BigInteger(_gasLimit);
        BigInteger amountWei = new BigInteger(_value);

        // 签名交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice.multiply(Gwei2WeiDecimal), gasLimit, paramter.to,
                amountWei, "");
        Credentials credentials = Credentials.create(privateKey);
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, Integer.parseInt(paramter.chainId), credentials);

        String sign_str = Numeric.toHexString(signMessage);
        sign_str = sign_str.substring(2);
        valueCallback.onReceiveValue(sign_str);
        //发送主链币结束
    }
    public static void ecHexSign_Token(callParams paramter, String privateKey, ValueCallback<String> valueCallback){
        //发送主链token开始
        String fromAddress = Keys
                .toChecksumAddress(Keys.getAddress(ECKeyPair.create(Numeric.toBigInt(privateKey))));


        String _nonce = BytesHelper.hexStr2BigDecimal(paramter.nonce, 0, 0).toPlainString();
        String _gasLimit = BytesHelper.hexStr2BigDecimal(paramter.gasLimit, 0, 0).toPlainString();
        String _gasPrice = BytesHelper.hexStr2BigDecimal(paramter.gasPrice, 0, 0).toPlainString();

        BigInteger nonce = new BigInteger(_nonce);

        // 支付的矿工费
        BigInteger Gwei2WeiDecimal = new BigInteger("1000000000");
        BigInteger gasPrice = new BigInteger(_gasPrice);
        BigInteger gasLimit = new BigInteger(_gasLimit);

        // 封装转账交易
        String data = paramter.data;

        // 签名交易
        RawTransaction rawTransaction = RawTransaction.createTransaction(nonce, gasPrice.multiply(Gwei2WeiDecimal), gasLimit, paramter.to,
                data);
        Credentials credentials = Credentials.create(privateKey);
        byte[] signMessage = TransactionEncoder.signMessage(rawTransaction, Integer.parseInt(paramter.chainId), credentials);
        String sign_str = Numeric.toHexString(signMessage);
        sign_str = sign_str.substring(2);
        valueCallback.onReceiveValue(sign_str);
    }


    public static void signCwv(String fromPri, int nonce, String toAddr, String amount, ValueCallback<String> valueCallback) {

        String js = "signCwv('%s',%d,'%s','%s')";
        String format = String.format(js, fromPri, nonce, toAddr, amount);
        SWLog.e(format);

        CommonTool.INSTANCE.getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                webView.evaluateJavascript(format, valueCallback);
            }
        });


    }

}
