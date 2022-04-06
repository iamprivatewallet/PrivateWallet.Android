package com.fanrong.frwallet.dapp

import android.os.Bundle
import android.webkit.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.DappHistoryEvent
import com.fanrong.frwallet.dapp.dappapi.error.AccountNotFoundException
import com.fanrong.frwallet.dapp.dukedapp.RpcImpl
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.bitMapAndStringConvertUtil
import com.fanrong.frwallet.tools.getUrlHostUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.SelectWalletListDialog
import kotlinx.android.synthetic.main.activity_dapp_browser.*
import kotlinx.android.synthetic.main.activity_dapp_browser.iv_close
import kotlinx.android.synthetic.main.activity_dapp_browser.iv_menu
import kotlinx.android.synthetic.main.dapp_menu_dialog.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import kotlinx.android.synthetic.main.fragment_wallet.ll_change_wallet
import me.duke.eth.browser.Web3BrowserFragment
import me.duke.eth.browser.control.AdapterWallet
import me.duke.eth.browser.dto.PushConfig
import me.duke.eth.browser.model.BrowserViewModel
import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.WebviewUtils
import xc.common.viewlib.view.customview.FullScreenDialog
import java.math.BigDecimal


class DappBrowserActivity : BaseActivity() {

    fun restartActivity(){
        extStartActivity(DappBrowserActivity::class.java,intent.extras!!)
        extFinishWithAnim()
    }


    val sourceUrl: String by lazy {
        val dapp = intent.getSerializableExtra(PARAMS_DAPP)

        return@lazy if (dapp == null) {
            intent.getStringExtra(PARAMS_URL)!!
        } else {
            (dapp as DappInfoDao).url!!
        }
    }

    var dappInfo: DappInfoDao? = null
        get() {
            val dapp = intent.getSerializableExtra(PARAMS_DAPP)
            if (dapp == null) {
                return null
            } else {
                return dapp as DappInfoDao
            }
        }

    lateinit var jsCallHandler: JsCallHandler

    companion object {
        const val PARAMS_URL = "url"
        const val PARAMS_DAPP = "dapp"
        lateinit var activity: DappBrowserActivity
        lateinit var webView: WebView
    }

    val webviewUtils: WebviewUtils by lazy {
        WebviewUtils()
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_dapp_browser
    }

    override fun initView() {
//        webviewUtils.initSetting(webview)
//        jsCallHandler = JsCallHandler(webview, this)
        activity = this
//        webView = webview
        WebIconDatabase.getInstance().open(getDir("icons", MODE_PRIVATE).getPath());

//        webview.webViewClient = object : WebViewClient() {
//            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
//                super.onPageStarted(view, url, favicon)
//                loadJs()
//                SWLog.e("onPageStarted() called with: view = $view, url = $url, favicon = $favicon")
//            }
//
//            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
//                if (url!!.startsWith("wc:")) {
//                    if (QrcodeInfo.isWcQrcode(url!!)) {
//                        WalletConnectUtil.connectSocket(url)
//                    }
//                    return true
//                } else {
//                    return super.shouldOverrideUrlLoading(view, url)
//                }
//
//            }
//
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                SWLog.e("onPageFinished() called with: view = $view, url = $url")
//            }
//        }
//
//
//        webview.webChromeClient = object : WebChromeClient() {
//
//            override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
//                SWLog.e(
//                    "onConsoleMessage：：" + consoleMessage!!.message() +
//                            "----" + consoleMessage!!.lineNumber() +
//                            "====" + consoleMessage!!.sourceId()
//                )
//                return super.onConsoleMessage(consoleMessage)
//            }
//
//            override fun onProgressChanged(view: WebView?, newProgress: Int) {
//                super.onProgressChanged(view, newProgress)
//                progress_bar.extGoneOrVisible(newProgress != 100)
//                progress_bar.progress = newProgress
//
//            }
//
//            override fun onReceivedTitle(view: WebView?, title: String?) {
//                super.onReceivedTitle(view, title)
//                tv_title.text = title
//            }
//        }
//
//
//        webview.addJavascriptInterface(JsCall(), "frwallet")
//        webview.settings.javaScriptEnabled = true
//        webview.settings.domStorageEnabled = true
//
////        webview!!.loadUrl("javascript:" + JsRead.readJs(this@DappBrowserActivity))
////        webview.evaluateJavascript(JsRead.readJs(this@DappBrowserActivity), null)
//        webview.loadUrl(sourceUrl)


        if ("1".equals(intent.getStringExtra(FrConstants.PP.IS_DAPP))) {
            DappInfoOperator.insert(sourceUrl, dappInfo?.name, dappInfo?.icon, dappInfo?.des)
            DappHistoryOperator.addOrUpdate(sourceUrl)
            EventBus.getDefault().post(DappHistoryEvent())
            // 提示责任说明
            showEscapteCause()
        }

        openDappBrowser()

    }

    var fragment: Web3BrowserFragment? = null
    var rpc: RpcImpl? = null
    private fun openDappBrowser() {
        var addrInfo = WalletOperator.currentWallet!!
        if (addrInfo == null) {
            throw AccountNotFoundException()
        }
        val nodeInfo = ChainNodeOperator.queryCurrentNode(addrInfo.chainType!!)
        val address = addrInfo.address//"0xb4a8f00266DdA25841790421BdDF243ca1E30157"
        val version = "0x1"
        val chainId = "0x"+BytesHelper.bigDecimal2HexStr(BigDecimal(nodeInfo.chainId), 0) //现在是56，需要转0x38
        val privateKey = addrInfo.privateKey.toString()//"363ebe34a2936247211f6cee4458213bec1897dd6402240cb4a6974624aa48f5"
        val bundle = Bundle()

        rpc = RpcImpl(this,this@DappBrowserActivity, version, address, nodeInfo.nodeUrl.toString(),privateKey,chainId,sourceUrl,addrInfo)
        AdapterWallet.getInstance().initAdapterWallet(PushConfig(true, true, address, version, chainId), rpc)
        fragment = Web3BrowserFragment()
        bundle.putString("url", sourceUrl)
        fragment!!.arguments = bundle

        supportFragmentManager.beginTransaction()
            .add(R.id.content, fragment!!, "web3")
            .show(fragment!!)
            .commit()

        val model = ViewModelProviders.of(activity!!).get(BrowserViewModel::class.java)

        model.result.observe(this, Observer {
            if (it.title != null)
                tv_title.text = it.title

            if (it.url != null)
                tv_url.text = it.url
            initTitle()
            var iconConfig = bitMapAndStringConvertUtil.getWebIconConfigByWebUrl(it.url);
            if (iconConfig != null && iconConfig.size > 0){
                var _iconConfig = iconConfig.get(0)
                _iconConfig?.webUrl = getUrlHostUtils.getHost(it.url!!);
                if (it.title != null){
                    _iconConfig?.title = it.title;
                }
                if (it.icon != null){
                    _iconConfig?.iconStr = bitMapAndStringConvertUtil.convertIconToString(it.icon!!);
                    _iconConfig?.favicon = it.icon
                }
                _iconConfig?.save();

            }else{
                var _iconConfig = WebIconConfig();
                _iconConfig.webUrl = getUrlHostUtils.getHost(it.url!!);
                if (it.title != null){
                    _iconConfig?.title = it.title;
                }
                if (it.icon != null){
                    _iconConfig?.iconStr = bitMapAndStringConvertUtil.convertIconToString(it.icon!!);
                }
                _iconConfig.save();
            }
        })

//        model.icon.observe(this, Observer {
//            var iconConfig = bitMapAndStringConvertUtil.getWebIconConfigByWebUrl(sourceUrl);
//            if (iconConfig != null && iconConfig.size > 0){
//                var _iconConfig = iconConfig.get(0)
//                _iconConfig?.webUrl = getUrlHostUtils.getHost(sourceUrl);
//                _iconConfig?.iconStr = bitMapAndStringConvertUtil.convertIconToString(it!!);
//                _iconConfig?.save();
//
//            }else{
//                var _iconConfig = WebIconConfig();
//                _iconConfig.webUrl = getUrlHostUtils.getHost(sourceUrl);
//                _iconConfig.iconStr = bitMapAndStringConvertUtil.convertIconToString(it!!);
//                _iconConfig.save();
//            }
////            iconConfig.apply {
////                this.webUrl = sourceUrl
////                this.iconStr = bitMapAndStringConvertUtil.convertIconToString(it!!)
////                save()
////            }
//        })

    }


    var isload = false

    private fun loadJs() {

        SWLog.e("----js sdk init----")
//        webview!!.loadUrl("javascript:" + JsRead.readJs(this@DappBrowserActivity))
//        webview!!.loadUrl(
//            "javascript:(function(){ \n" +
//                    "window.ethereum.chainid = '0x38'\n" +
//                    "})();"
//        )
//        webview!!.loadUrl("javascript:console.log(window.ethereum.chainid);")


        if (!isload) {
        }
        isload = true

    }


    /**
     * 责任说明
     */
    fun showEscapteCause() {
        if (DappInfoOperator.isShowEscapteCause(sourceUrl)) {
            return
        }

        fun update() {
            DappInfoOperator.updateEscapteCause(sourceUrl)
        }


        DappEscapeClauseDialog(this,sourceUrl).apply {
            onCancel = object : FullScreenDialog.OnCancelListener {
                override fun cancel() {
                    dismiss()
                    extFinishWithAnim()
                }
            }
            onConfrim = object : FullScreenDialog.OnConfirmListener {
                override fun confirm(params: Any?) {
                    dismiss()
                    if (isShow=="1"){
                        update()
                    }
                }
            }
        }.show()
    }


    override fun loadData() {
    }


    private fun initTitle() {
        iv_close.setOnClickListener {
            extFinishWithAnim()
        }
        iv_menu.setOnClickListener {
            DappMenuDialog(this).apply {
                isDapp = "1".equals(intent.getStringExtra(FrConstants.PP.IS_DAPP))
                webview = fragment!!.rootWebView
            }.show()
        }

        iv_changewallet.setOnClickListener {
            SelectWalletListDialog(this).apply {
                var selectWalletListDialog = this
                this.isFromDapp = true
                this.onConfrim = object : FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        fragment?.rootWebView?.loadUrl(fragment?.rootWebView!!.originalUrl ?: "")
                        selectWalletListDialog.dismiss()
                        restartActivity()
                    }
                }
            }.show()
        }

    }


    inner class JsCall {

        /**
         * wallet.getAddr
         * wallet.getBalance
         * wallet.transfer amount，toAddr
         */
        @JavascriptInterface
        fun callAPI(api: String, param: String): String? {
            SWLog.e("callAPI() called with: callID = $api, param = $param")
//            showToastAtMainThread("callAPI() called with: callID = $api, param = $param")
            var result = jsCallHandler.apiCall(api, param.toString(), this@DappBrowserActivity)
            SWLog.e("callAPI() called with: callID  result ="+result)
            return result
        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        webview.destroy()
        fragment?.rootWebView?.destroy()
    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}