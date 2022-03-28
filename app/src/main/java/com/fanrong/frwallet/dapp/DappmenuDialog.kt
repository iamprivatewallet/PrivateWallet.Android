package com.fanrong.frwallet.dapp

import android.content.Context
import android.view.Gravity
import android.webkit.WebView
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.DappStarOperator
import com.fanrong.frwallet.dao.eventbus.DappHistoryEvent
import com.fanrong.frwallet.view.SelectWalletListDialog
import kotlinx.android.synthetic.main.dapp_menu_dialog.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog

class DappMenuDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM
    var isDapp: Boolean = false
    lateinit var webview: WebView

    override fun getContentView(): Int {
        return R.layout.dapp_menu_dialog
    }

    override fun initView() {

        ll_dapp_menu.extGoneOrVisible(isDapp)

        iv_close.setOnClickListener {
            dismiss()
        }

        ll_share.setOnClickListener {
            LibAppUtils.shareText(webview.originalUrl ?: "", ownerActivity!!)
        }
        ll_copy.setOnClickListener {
            LibAppUtils.copyText(webview.originalUrl ?: "")
            showToast("复制成功")
        }
        ll_refresh.setOnClickListener {
            webview.loadUrl(webview.originalUrl ?: "")
            dismiss()
        }
        ll_change_wallet.setOnClickListener {
            SelectWalletListDialog(DappBrowserActivity.activity).apply {
                var selectWalletListDialog = this
                this.isFromDapp = true
                this.onConfrim = object : OnConfirmListener {
                    override fun confirm(params: Any?) {
                        webview.loadUrl(webview.originalUrl ?: "")
                        selectWalletListDialog.dismiss()
                        this@DappMenuDialog.dismiss()
                        (ownerActivity as DappBrowserActivity).restartActivity()
                    }
                }
            }.show()

        }

        val dapp = DappStarOperator.queryDapp(webview.originalUrl)
        if (dapp != null) {
            ll_isstar.tag = "1"
            iv_isstar.setImageResource(R.mipmap.src_lib_eui_icon_addtocustommarketadded)
        } else {
            ll_isstar.tag = "0"
            iv_isstar.setImageResource(R.mipmap.src_lib_eui_icon_addtocustommarket)
        }

        ll_isstar.setOnClickListener {
            if ("0".equals(ll_isstar.tag)) {
                ll_isstar.tag = "1"
                DappStarOperator.dappStar(webview.originalUrl,webview.title)
                iv_isstar.setImageResource(R.mipmap.src_lib_eui_icon_addtocustommarketadded)
                showToast("收藏成功")

            } else {
                ll_isstar.tag = "0"
                DappStarOperator.dappCancelStar(webview.originalUrl)
                iv_isstar.setImageResource(R.mipmap.src_lib_eui_icon_addtocustommarket)
                showToast("取消收藏成功")
            }
            EventBus.getDefault().post(DappHistoryEvent())
        }

    }

}