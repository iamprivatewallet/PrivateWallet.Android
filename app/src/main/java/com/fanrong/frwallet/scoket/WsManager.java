package com.fanrong.frwallet.scoket;

import android.os.Handler;
import android.util.Log;

import com.fanrong.frwallet.dao.data.marketDataBean;
import com.fanrong.frwallet.dao.database.LikeMarketItemOperator;
import com.fanrong.frwallet.dao.eventbus.ScoketDataEvent;
import com.fanrong.frwallet.tools.MD5Utils;
import com.fanrong.frwallet.tools.NetTools;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketFactory;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WsManager {

    private JSONObject jsonObject1;
    private JSONObject jsonObject;
    private JSONArray content;

    enum CONNECTTYPE{
        CONNECT,
        DISCINNECT,
        DISCINNECTCONNECT
    }

    private static WsManager mwsManager;
    private String url = "wss://chain.kimchiii.com/ws/api/wallet/ticker";

    private CONNECTTYPE connect_type = CONNECTTYPE.DISCINNECT;

    public static WsManager getInstance(){
        if (mwsManager == null){
            mwsManager = new WsManager();
        }
        return mwsManager;
    }
    public interface wsListener{
        public void InitListener();
        public void upDateUiShow(JSONObject data);
        public void disConnectConnect();
    }

    public WebSocket ws;
    private WsListener ws1Listener;

    public String deviceId = "";

    private List<wsListener> listeners = new ArrayList<>();

    public String cur_data = "";

    public void Init(){
        try {
            ws1Listener = new WsListener(new WsListener.wsContentListener() {
                @Override
                public void textMessageListener(String data) {
                    Log.d("webscoket", "textMessageListener: --->>>>>>"+data);
                    isReConnect = true;
                    cur_data = data;

                    marketDataBean marketList = NetTools.INSTANCE.formatJson(data, marketDataBean.class);
                    for (int i = 0; i < marketList.data.size(); i++) {
                        //queryIsLike
                        boolean b = LikeMarketItemOperator.INSTANCE.queryIsLike(marketList.data.get(i).symbol);
                        marketList.data.get(i).isLike = b;
                    }
                    EventBus.getDefault().post(new ScoketDataEvent(marketList));
                }

                @Override
                public void binaryMessageListener() {

                }

                @Override
                public void connectedListener() {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("action","market.last.price");
                        jsonObject.put("symbol","");
                        jsonObject.put("type","0");
                        jsonObject.put("deviceId", MD5Utils.digest(deviceId));
                        Log.d("webscoket", "connectedListener: --->>>>>>>"+jsonObject.toString());
                        ws.sendText(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (listeners != null){
                        for (int i = 0;i<listeners.size();i++){
                            listeners.get(i).InitListener();
                        }
                    }
                    connect_type = CONNECTTYPE.CONNECT;
                    cancelReconnect();
//                        EndDraw();
                }

                @Override
                public void connectedErrorListener() {
                    reConnect();
                    connect_type = CONNECTTYPE.DISCINNECT;
                    isReConnect = false;
//                    StartDraw();
                }

                @Override
                public void disConnectListener() {
                    reConnect();
                    connect_type = CONNECTTYPE.DISCINNECT;
                    isReConnect = false;
//                    StartDraw();
                }
            });
            ws = new WebSocketFactory().createSocket(url,3000)
                    .setFrameQueueSize(5)//设置帧队列最大值为5
                    .setMissingCloseFrameAllowed(false)//设置不允许服务端关闭连接却未发送关闭帧
                    .addListener(ws1Listener)//添加回调监听
                    .addHeader("Origin","chain.kimchiii.com")
                    .connectAsynchronously();//异步连接

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void registenerListener(wsListener listener){
        if (listeners != null && listener!= null){
            listeners.add(listener);
        }
    }
    public void unRegistenerListener(wsListener listener){
        if (listeners != null && listeners.equals(listener)){
            listeners.remove(listener);
        }
    }
    public void clearAll(){
        listeners.clear();
    }

    private boolean isFirest = true;
    boolean isReConnect = true;
    double jiagecha = 0;
    private void addData(String data){
    }
    private int show_type = 1;

    public void changeData(int index){
        show_type = index;
    }


    public void closeWS(){
        ws.sendClose();
        ws.disconnect();
        ws.flush();
        ws = null;
    }

    private Handler mHandler = new Handler();
    private int reconnectCount = 0;//重连次数
    private long minInterval = 3000;//重连最小时间间隔
    private long maxInterval = 60000;//重连最大时间间隔

    private long selfDrawKLine = 1000;

    public void reConnect(){
        if (ws != null && !ws.isOpen()){
            reconnectCount++;
            long reconnectTime = minInterval;
            if (reconnectCount > 3) {
                long temp = minInterval * (reconnectCount - 2);
                reconnectTime = temp > maxInterval ? maxInterval : temp;
            }

            mHandler.postDelayed(mReconnectTask, reconnectTime);
        }
    }

    public void SelfDrawKline(){
//        if (wsDataList != null && wsDataList.size() > 0){
//            KChartBean kLineEntity = wsDataList.get(wsDataList.size() - 1);
//            wsDataList.add(kLineEntity);
//
//            if (listeners!=null){
//                for (int i = 0;i<listeners.size();i++){
//                    listeners.get(i).UpDateListener(kLineEntity);
//                }
//            }
//        }
    }
    private Runnable mReconnectTask = new Runnable() {

        @Override
        public void run() {
            Init();
        }
    };

    private void cancelReconnect() {
        reconnectCount = 0;
        mHandler.removeCallbacks(mReconnectTask);
    }

}
