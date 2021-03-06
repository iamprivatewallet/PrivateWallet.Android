package com.fanrong.frwallet.main

//import com.fanrong.frwallet.scoket.scoketClient
import android.R
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import com.facebook.stetho.Stetho
import com.fanrong.frwallet.BuildConfig
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.dao.database.ChainNodeDao
import com.fanrong.frwallet.dao.database.ConfigTokenOperator
import com.fanrong.frwallet.dapp.dukedapp.WalletUtils
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectUtil
import com.fanrong.frwallet.scoket.WsManager
import com.fanrong.frwallet.task.UpdateTransactionsTask
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.wallet.cwv.http.RetrofitClient
import com.fanrong.frwallet.wallet.cwv.http.URLBuilder
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.footer.ClassicsFooter
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import org.brewchain.sdk.Config
import org.litepal.LitePal
import xc.common.framework.Foundation
import xc.common.framework.net.NetClient
import xc.common.tool.CommonTool
import xc.common.tool.comptent.CatchExceptionHandler
import xc.common.tool.utils.AppManager
import xc.common.tool.utils.SPUtils
import xc.common.viewlib.BasicView


class MyApplication : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(AppLanguageUtils.attachBaseContext(base!!, getAppLanguage(base!!)!!))
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        NetClient.initApiClient(mutableListOf<okhttp3.Interceptor>().apply {
            add(BaseUrlChangeInterceptor())
        })

        CatchExceptionHandler.init()
        CatchExceptionHandler.appName = "frwallet"

        NetClient.build()
        LitePal.initialize(this)
        Foundation.init(this)
        BasicView.init(this)
        CommonTool.init(this)
        CallJsCodeUtils.init()
//        ExceptionHelper.
        Thread.getDefaultUncaughtExceptionHandler()
        initNode()
//        Config.host = NodeList.CURRENT_CVN
        Config.changeHost(NodeList.CURRENT_CVN)
        RetrofitClient.changeFbcNodeApi(URLBuilder.ins().hostUrl)

        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }

        AppManager.init(this)

        UpdateTransactionsTask.start()
//        UpdateTokenPrice.start()

        onLanguageChange()
        WalletConnectUtil.init(this)

        initRefreshLayout()

        initTokens()

        WalletUtils.getInstance().Init();

        initBackgroundCallBack()

        initWebScoket()

        Thread.setDefaultUncaughtExceptionHandler(OwnUncaughtExceptionHandler())
    }


    private fun initTokens() {
        ConfigTokenOperator.initServiceToken()
//        ConfigTokenOperator.getTokenPriceByName("bnb") //token ??????????????????
//        NetworkManager.getNetworkManager()
    }

    private fun initNode() {
        if (!"1".equals(SPUtils.getString(FrConstants.SP.IS_FRIST_INSTALL))) {
            SPUtils.saveValue(FrConstants.SP.IS_FRIST_INSTALL, "1")
            ChainNodeDao().apply {
                this.nodeName = "Ethereum"
                this.nodeUrl = "https://mainnet.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161"
                this.chainType = "ETH"
                this.chainId = "1"
                this.isCurrent = 1
                this.symbol = "ETH"
                this.netType = 1
                //                tx/0x62d7284b7aa6443274e9de350c5a363b4c26ec6b6f96ffdb4bfc82af470119ad
                this.browser = "https://cn.etherscan.com/"
                save()
            }
            ChainNodeDao().apply {
                this.nodeName = "Heco Chain"
                this.nodeUrl = "https://http-mainnet.hecochain.com"
                this.chainType = "HECO"
                this.chainId = "128"
                this.isCurrent = 1
                this.symbol = "HT"
                this.netType = 1
                //                tx/0x62d7284b7aa6443274e9de350c5a363b4c26ec6b6f96ffdb4bfc82af470119ad
                this.browser = "https://hecoinfo.com"
                save()
            }
            ChainNodeDao().apply {
                this.nodeName = "BNB Smart Chain"
                this.nodeUrl = "https://bsc-dataseed1.ninicoin.io"//https://bsc-dataseed1.ninicoin.io   http://94.74.87.188:8545
                this.chainType = "BSC"
                this.chainId = "56"
                this.isCurrent = 1
                this.symbol = "BNB"
                this.netType = 1
                //                tx/0x62d7284b7aa6443274e9de350c5a363b4c26ec6b6f96ffdb4bfc82af470119ad
                this.browser = "https://bscscan.com/"
                save()
            }
            ChainNodeDao().apply {
                this.nodeName = "Conscious Value Network"
                this.nodeUrl = "http://52.220.97.222:1235"
                this.chainType = "CVN"
                this.chainId = "3388"
                this.isCurrent = 1
                this.symbol = "CVN"
                this.netType = 1
                //                tx/0x62d7284b7aa6443274e9de350c5a363b4c26ec6b6f96ffdb4bfc82af470119ad
                this.browser = "https://scan.cvn.io/#/"
                save()
            }

        }
    }

    fun initWebScoket(){

        WsManager.getInstance().Init();
//        var str: String? =
//            "ws://chain.kimchiii.com/ws/api/wallet/ticker"
//        var chatClient: scoketClient? = null
//
//        chatClient = scoketClient(URI(str))
//        chatClient?.connect()
    }


    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        onLanguageChange()
    }

    private fun onLanguageChange() {
        AppLanguageUtils.changeAppLanguage(this, AppLanguageUtils.getSupportLanguage(getAppLanguage(context)));
    }

    private fun getAppLanguage(context: Context): String? {
        return context!!.getSharedPreferences("APP_Config", Context.MODE_PRIVATE).getString(FrConstants.LAN_CODE, FrConstants.language_code[0])
//        val localLan = SPUtils.getString(FrConstants.LAN_CODE)
//        if (localLan.checkNotEmpty()) {
//            return localLan
//        }
//        return FrConstants.language_code[0]
    }

    var current_activity_count = 0
    companion object {
        lateinit var context: MyApplication
    }

    private fun initRefreshLayout() {

        //???????????????Header?????????
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            //                layout.setPrimaryColors(getResources().getColor(R.color.white)
//                        , getResources().getColor(android.R.color.darker_gray));
            layout.setPrimaryColorsId(R.color.holo_blue_bright, R.color.white) //????????????????????????
            ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("????????? %s"));
        }
        //???????????????Footer?????????
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
//            ClassicsFooter.REFRESH_FOOTER_PULLING = "??????????????????"
//            ClassicsFooter.REFRESH_FOOTER_RELEASE = "??????????????????"
//            ClassicsFooter.REFRESH_FOOTER_LOADING = "????????????..."
//            ClassicsFooter.REFRESH_FOOTER_REFRESHING = "????????????..."
//            ClassicsFooter.REFRESH_FOOTER_FINISH = "????????????"
//            ClassicsFooter.REFRESH_FOOTER_FAILED = "????????????"
//            ClassicsFooter.REFRESH_FOOTER_NOTHING = "?????????????????????"
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }

    private fun initBackgroundCallBack(){
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            }

            override fun onActivityStarted(activity: Activity) {
                current_activity_count++
            }

            override fun onActivityResumed(activity: Activity) {

            }

            override fun onActivityPaused(activity: Activity) {

            }

            override fun onActivityStopped(activity: Activity) {
                current_activity_count--
                if (current_activity_count == 0){
                    OpenLockAppDialogUtils.isNeedShow = true
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

            }

            override fun onActivityDestroyed(activity: Activity) {

            }
        })
    }

}