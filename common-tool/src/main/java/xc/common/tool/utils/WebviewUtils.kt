package xc.common.tool.utils

import android.content.Context
import android.os.Build
import android.webkit.*

class WebviewUtils {
    fun initSetting(webview: WebView) {
        val settings = webview.settings

        //设置自适应屏幕，两者合用
        //设置自适应屏幕，两者合用
        settings.useWideViewPort = true //将图片调整到适合webview的大小
        settings.loadWithOverviewMode = true // 缩放至屏幕的大小
        //缩放操作
        //缩放操作
        settings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        settings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        settings.displayZoomControls = false //隐藏原生的缩放控件


        //其他细节操作
        //其他细节操作
        settings.cacheMode = WebSettings.LOAD_NO_CACHE

        settings.allowFileAccess = true //设置可以访问文件。

        settings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口

        settings.javaScriptEnabled = true
        settings.loadsImagesAutomatically = true //支持自动加载图片
        settings.defaultTextEncodingName = "utf-8" //设置编码格式
        settings.useWideViewPort = true
        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL

        settings.domStorageEnabled = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webview.settings.defaultTextEncodingName = "utf-8" //设置文本编码（根据页面要求设置： utf-8）

        //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
        webview.webViewClient = object : WebViewClient() {}
        webview.webChromeClient = object : WebChromeClient() {}
    }


    /**
     * 给WebView同步Cookie
     *
     * @param context 上下文
     * @param url     可以使用[domain][host]
     */
    fun syncCookie(context: Context, url: String, webview: WebView) {

        //判断api版本21以上，webview做了较大的改动，同步cookie的操作已经可以自动同步、但前提是我们必须开启第三方cookie的支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(webview, true)
            CookieManager.getInstance().flush()
        }
        //判断api版本21以下
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(context)
        }
        val cookieManager = CookieManager.getInstance()
        cookieManager.setAcceptCookie(true)
        cookieManager.removeSessionCookie() //移除

        cookieManager.removeAllCookie()
        cookieManager.setCookie(
            url,
            SPUtils.getString("cookie")
        ) //如果没有特殊需求，这里只需要将session id以"key=value"形式作为cookie即可

        CookieSyncManager.getInstance().sync()
    }

}