package com.fanrong.frwallet.dapp.walletconnect

import android.app.Application
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.eventbus.walletconnect.*
import com.fanrong.frwallet.walletconnect.QrcodeInfo
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import org.greenrobot.eventbus.EventBus
import org.walletconnect.Session
import org.walletconnect.impls.*
import org.walletconnect.nullOnThrow
import xc.common.tool.utils.SWLog
import java.io.File

object WalletConnectUtil {

    private lateinit var application: Application

    private lateinit var client: OkHttpClient
    private lateinit var moshi: Moshi
    private lateinit var bridge: BridgeServer
    private lateinit var storage: WCSessionStore
    lateinit var config: Session.Config

    lateinit var session: Session

    var connectWallet: WalletDao? = null
    fun getWalletNotNull(): WalletDao {
        return connectWallet!!
    }

    var dappPeerMeta: Session.PeerMeta? = null
    var sendTransaction: Session.MethodCall.SendTransaction? = null
    var signMessage: Session.MethodCall.SignMessage? = null

    fun init(application: Application) {
        this.application = application
        initDapp()
    }


    private fun initDapp() {
        initClient()
        initMoshi()
        initBridge()
        initSessionStorage()
    }


    private fun initClient() {
        client = OkHttpClient.Builder().build()
    }

    private fun initMoshi() {
        moshi = Moshi.Builder().build()
    }


    private fun initBridge() {
        bridge = BridgeServer(moshi)
        bridge.start()
    }

    private fun initSessionStorage() {
        storage = FileWCSessionStore(File(application.cacheDir, "session_store.json").apply { createNewFile() }, moshi)
    }


    fun connectSocket(wcString:String){
        if (QrcodeInfo.isWcQrcode(wcString)) {
            val decode = QrcodeInfo.decode(wcString)
            resetSession(decode.sessionId, decode.bridgeServe, decode.key)
            addCallback()
            EventBus.getDefault().post(DoConnectBeforeEvent())
        }
    }



    fun resetSession(topic: String, bridge: String, key: String) {
        nullOnThrow { session }?.clearCallbacks()
        config = Session.Config(topic, bridge, key)
        session = WCSession(
            config,
            MoshiPayloadAdapter(moshi),
            storage,
            OkHttpTransport.Builder(client, moshi),
            Session.PeerMeta(name = "Example App")
        )
        session.init()
//            session.peerMeta()
//            session.offer()
    }

    fun addCallback() {
        session.addCallback(Callback())
    }


    var isConnected = false
    var isApproved = false

    class Callback() : Session.Callback {

        override fun onStatus(status: Session.Status) {
            SWLog.e("onStatus->" + status)

            if (status is Session.Status.Connected) {
                isConnected = true
            }

            if (status is Session.Status.Approved) {
                isApproved = true
            }

            if (status is Session.Status.Closed
                || status is Session.Status.Disconnected
            ) {
                isConnected = false
                isApproved = false
//                session = null
            }

            EventBus.getDefault().post(StateChangeEvent())
        }

        override fun onMethodCall(call: Session.MethodCall) {
            SWLog.e("onMethodCall->" + call.javaClass.simpleName)
            SWLog.e("onMethodCall->" + call)

            if (call is Session.MethodCall.SessionRequest) {
                dappPeerMeta = call.peer.meta
                EventBus.getDefault().post(ConnectEvent(call))
            } else if (call is Session.MethodCall.SignMessage) {
                signMessage = call
                EventBus.getDefault().post(SignEvent(call))
            } else if (call is Session.MethodCall.SendTransaction) {
                sendTransaction = call
                EventBus.getDefault().post(TransactionEvent(call))
            }
        }
    }


}