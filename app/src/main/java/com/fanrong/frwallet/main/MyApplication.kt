package com.fanrong.frwallet.main

import android.R
import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.util.Log
import com.facebook.stetho.Stetho
import com.fanrong.frwallet.BuildConfig
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.dao.database.ChainNodeDao
import com.fanrong.frwallet.dao.database.ConfigTokenOperator
import com.fanrong.frwallet.dapp.dukedapp.WalletUtils
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectUtil
import com.fanrong.frwallet.task.UpdateTransactionsTask
import com.fanrong.frwallet.tools.AppLanguageUtils
import com.fanrong.frwallet.tools.BaseUrlChangeInterceptor
import com.fanrong.frwallet.tools.CallJsCodeUtils
import com.fanrong.frwallet.tools.getUrlHostUtils
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

        val host =
            getUrlHostUtils.getHost("https://blog.csdn.net/smallnetvisitor/article/details/84515265?spm=1001.2101.3001.6650.6&utm_medium=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-6.pc_relevant_paycolumn_v3&depth_1-utm_source=distribute.pc_relevant.none-task-blog-2%7Edefault%7EBlogCommendFromBaidu%7ERate-6.pc_relevant_paycolumn_v3&utm_relevant_index=13")

        val host1 = getUrlHostUtils.getHost("https://pancakeswap.finance/swap?outputCurrency=0x2a2949b4a8ab8b5f4e703a0dbedfa6c22c1c4098")
        Log.d("TAG", "onCreate: --->>>>>"+host)
    }

    private fun initTokens() {
        ConfigTokenOperator.initServiceToken()
//        ConfigTokenOperator.getTokenPriceByName("bnb") //token 价格（美金）
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
                this.nodeName = "Ethereum"
                this.nodeUrl = "https://mainnet.infura.io/v3/9aa3d95b3bc440fa88ea12eaa4456161"
                this.chainType = "ETH"
                this.chainId = "1"
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


    companion object {
        lateinit var context: MyApplication
    }

    private fun initRefreshLayout() {

        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator { context, layout ->
            //                layout.setPrimaryColors(getResources().getColor(R.color.white)
//                        , getResources().getColor(android.R.color.darker_gray));
            layout.setPrimaryColorsId(R.color.holo_blue_bright, R.color.white) //全局设置主题颜色
            ClassicsHeader(context) //.setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        }
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator { context, layout ->
//            ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载更多"
//            ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载"
//            ClassicsFooter.REFRESH_FOOTER_LOADING = "正在加载..."
//            ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在刷新..."
//            ClassicsFooter.REFRESH_FOOTER_FINISH = "加载完成"
//            ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败"
//            ClassicsFooter.REFRESH_FOOTER_NOTHING = "没有更多数据了"
            ClassicsFooter(context).setDrawableSize(20f)
        }
    }
}