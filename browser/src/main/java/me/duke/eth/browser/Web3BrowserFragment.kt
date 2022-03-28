package me.duke.eth.browser

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.annotation.RawRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import me.duke.eth.browser.control.AdapterWallet
import me.duke.eth.browser.control.MetaMaskControl
import me.duke.eth.browser.control.MetaMaskFlow
import me.duke.eth.browser.model.BrowserViewModel
import me.duke.eth.browser.widget.Web3View
import java.io.IOException

/**
 * Web3真正的功能实现
 */
open class Web3BrowserFragment : Fragment() {

    lateinit var rootWebView:Web3View

    private lateinit var progressBar: ProgressBar

    private lateinit var jsInject: String

    private lateinit var webViewClient: WebViewClient

    private val viewModel by activityViewModels<BrowserViewModel>()

    private lateinit var rootControl: MetaMaskControl

    private lateinit var rootFlow: MetaMaskFlow

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_web3_browser, container, false)
        rootWebView = root.findViewById(R.id.web3)
        progressBar = root.findViewById(R.id.pb)
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsInject = loadFile(R.raw.dsbridge) + "\n" + loadFile(R.raw.metamask)
        val url = arguments?.getString("url") ?: "about:blank"
        rootControl = MetaMaskControl(rootWebView,url , false)
        rootFlow = MetaMaskFlow(rootWebView)
        rootWebView.addJavascriptObject(rootFlow, "ETH")
        webViewClient = Web3Client()
        rootWebView.webViewClient = webViewClient
        rootWebView.webChromeClient = Web3ChromeClient()
        rootWebView.loadUrl(url)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        clearWeb()
    }

    private inner class Web3Client : WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (url.startsWith("http")) {
                view.loadUrl(url)
            } else {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                view.context.startActivity(intent)
            }
            return true
        }

        override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
//            val flow = MetaMaskFlow.getInstance(view as Web3View)
//            flow.control = MetaMaskControl(view, url, view == rootWebView)
//            AdapterWallet.getInstance().addOnChangeListener(flow.control)
            rootFlow.web3ViewMetaMaskControlMap.forEach {
                it.value.mWeb3View = null
                AdapterWallet.getInstance().removeOnchangeListener(it.value)
            }
            var uri = Uri.parse(url)
            val http = uri.scheme+":"+uri.schemeSpecificPart
            uri = Uri.parse(http)
            rootFlow.web3ViewMetaMaskControlMap.clear()
            rootControl.url = http
            rootControl.mWeb3View = rootWebView
            rootFlow.web3ViewMetaMaskControlMap[uri] = rootControl
            view.setTag(R.id.web3_api_21, "false")
        }

        override fun onLoadResource(view: WebView?, url: String?) {
            super.onLoadResource(view, url)
            if(Build.VERSION_CODES.M > Build.VERSION.SDK_INT){
                val web3View = view as? Web3View
                if(web3View?.getTag(R.id.web3_api_21) != "true"){
                    web3View?.evaluateJavascript("(function() {\n$jsInject;\n})();")
                    web3View?.setTag(R.id.web3_api_21, "true")
                }
            }

//            rootFlow.web3ViewMetaMaskControlMap.forEach {
//                it.value.mWeb3View = null
//                AdapterWallet.getInstance().removeOnchangeListener(it.value)
//            }
//            var uri = Uri.parse(url)
//            val http = uri.scheme+":"+uri.schemeSpecificPart
//            uri = Uri.parse(http)
//            rootFlow.web3ViewMetaMaskControlMap.clear()
//            rootControl.url = http
//            rootControl.mWeb3View = rootWebView
//            rootFlow.web3ViewMetaMaskControlMap[uri] = rootControl
        }

        override fun onPageCommitVisible(view: WebView?, url: String?) {
            super.onPageCommitVisible(view, url)
            val web3View = view as? Web3View
            web3View?.evaluateJavascript("(function() {\n$jsInject;\n})();")
        }

        override fun onPageFinished(view: WebView, url: String?) {
            super.onPageFinished(view, url)
//            val flow = MetaMaskFlow.getInstance(view as Web3View)
            rootControl.onChange()
        }
    }

    private inner class Web3ChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            if (view != rootWebView)
                return
            progressBar.progress = newProgress
            if (newProgress == 100) {
                if (progressBar.isVisible) {
                    progressBar.isVisible = false
                }
            } else {
                if (!progressBar.isVisible) {
                    progressBar.isVisible = true
                }
            }
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
            if (view != rootWebView)
                return
            val browserViewModel = BrowserViewModel()
            browserViewModel.title = title
            browserViewModel.url = view?.url
            browserViewModel.icon = null

            viewModel.result.postValue(browserViewModel)
        }

        override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
            super.onReceivedIcon(view, icon)
            if (view != rootWebView)
                return
            val browserViewModel = BrowserViewModel()
            browserViewModel.title = null
            browserViewModel.icon = icon
            browserViewModel.url = view?.url
            viewModel.result.postValue(browserViewModel)
        }
    }

    private fun clearWeb() {
        rootWebView.removeJavascriptObject("ETH")
        rootFlow.web3ViewMetaMaskControlMap.forEach {
            it.value.mWeb3View = null
            AdapterWallet.getInstance().removeOnchangeListener(it.value)
        }
        rootFlow.web3ViewMetaMaskControlMap.clear()
        rootFlow.web3View = null
        rootWebView.loadUrl("about:blank")
        rootWebView.stopLoading()
        if (rootWebView.handler != null) {
            rootWebView.handler.removeCallbacksAndMessages(null)
        }
        rootWebView.removeAllViews()
        var mViewGroup: ViewGroup?
        if ((rootWebView.parent as? ViewGroup).also { mViewGroup = it } != null) {
            mViewGroup!!.removeView(rootWebView)
        }
        rootWebView.webChromeClient = null
        rootWebView.webViewClient = null
        rootWebView.tag = null
        rootWebView.clearHistory()
        rootWebView.destroy()
    }

    private fun loadFile(@RawRes rawRes: Int): String? {
        var buffer = ByteArray(0)
        try {
            val `in` = rootWebView.context.resources.openRawResource(rawRes)
            buffer = ByteArray(`in`.available())
            val len = `in`.read(buffer)
            if (len < 1) {
                throw IOException("Nothing is read.")
            }
        } catch (ex: java.lang.Exception) {
            Log.e("READ_JS_TAG", "Ex", ex)
        }
        return String(buffer)
    }
}