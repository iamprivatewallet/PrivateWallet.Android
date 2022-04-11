//package com.fanrong.frwallet.scoket;
//
//import android.util.Log;
//
//import org.java_websocket.client.WebSocketClient;
//import org.java_websocket.drafts.Draft;
//import org.java_websocket.handshake.ServerHandshake;
//
//import java.net.URI;
//import java.util.Map;
//
//public class scoketClient extends WebSocketClient {
//    public scoketClient(URI serverUri) {
//        super(serverUri);
//    }
//
//    public scoketClient(URI serverUri, Draft draft) {
//        super(serverUri, draft);
//    }
//
//    public scoketClient(URI serverUri, Map<String, String> httpHeaders) {
//        super(serverUri, httpHeaders);
//    }
//    @Override
//    public void onOpen(ServerHandshake handshakedata) {
//        Log.d("webscoket", "onOpen: ");
//    }
//
//    @Override
//    public void onMessage(String message) {
//        Log.d("webscoket", "onMessage: ");
//    }
//
//    @Override
//    public void onClose(int code, String reason, boolean remote) {
//        Log.d("webscoket", "onClose: --->>>>>>code="+code+"     -->>>>reason"+reason);
//    }
//
//    @Override
//    public void onError(Exception ex) {
//        Log.d("webscoket", "onError: ");
//    }
//}
