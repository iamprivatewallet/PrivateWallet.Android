package com.fanrong.frwallet.scoket;

import android.util.Log;

import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFrame;

import java.util.List;
import java.util.Map;

/**
 * 继承默认的监听空实现WebSocketAdapter,重写我们需要的方法
 * onTextMessage 收到文字信息
 * onConnected 连接成功
 * onConnectError 连接失败
 * onDisconnected 连接关闭
 */
class WsListener extends WebSocketAdapter {
    public interface wsContentListener{
        public void textMessageListener(String data);
        public void binaryMessageListener();
        public void connectedListener();
        public void connectedErrorListener();
        public void disConnectListener();
    }

    private wsContentListener mListener;

    public WsListener(wsContentListener lll){
        mListener = lll;
    }
    @Override
    public void onTextMessage(WebSocket websocket, String text) throws Exception {
        super.onTextMessage(websocket, text);

        if (mListener != null){
            mListener.textMessageListener(text);
        }
    }

    @Override
    public void onBinaryMessage(WebSocket websocket, byte[] binary) throws Exception {
        super.onBinaryMessage(websocket, binary);
        if (mListener != null){
            mListener.binaryMessageListener();
        }
    }

    @Override
    public void onConnected(WebSocket websocket, Map<String, List<String>> headers)
            throws Exception {
        super.onConnected(websocket, headers);
        Log.d("webscoket", "onConnected: 连接成功");
        if (mListener != null){
            mListener.connectedListener();
        }
    }

    @Override
    public void onConnectError(WebSocket websocket, WebSocketException exception)
            throws Exception {
        super.onConnectError(websocket, exception);
        Log.d("webscoket", "onConnectError: 连接错误:"+exception.getMessage());
        if (mListener != null){
            mListener.connectedErrorListener();
        }
    }

    @Override
    public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer)
            throws Exception {
        super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
        Log.d("webscoket", "onDisconnected: 断开连接");
        if (mListener != null){
            mListener.disConnectListener();
        }
    }

}