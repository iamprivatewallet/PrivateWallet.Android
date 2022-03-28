package com.fanrong.frwallet.view

import android.util.TypedValue
import android.view.Gravity
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.main.MyApplication
import xc.common.tool.CommonTool
import xc.common.tool.utils.DensityUtil
import xianchao.com.topmsg.TopWindowMsg

private fun initStyle(dialog: TopWindowMsg.TopMsgDialog) {

    dialog.getContainerView().apply {

        setBackgroundColor(MyApplication.context.resources.getColor(R.color.main_blue))
        setPadding(DensityUtil.dp2px(10f), DensityUtil.dp2px(30f), DensityUtil.dp2px(10f), DensityUtil.dp2px(10f))
    }
    dialog.getMsgView().apply {
        setTextColor(MyApplication.context.resources.getColor(R.color.white))
        setTextSize(TypedValue.COMPLEX_UNIT_SP, 13f)
        setGravity(Gravity.LEFT)
        layoutParams = layoutParams
    }
}

fun topDialogPasswordDes(baseActivity: BaseActivity): TopWindowMsg.TopMsgDialog {
    return TopWindowMsg.getPopupWindow(baseActivity).apply {
        CommonTool.mainHandler.post {
            initStyle(this)
            getMsgView().text = "该密码将作为身份下多链钱包的交易密码。\n" +
                    "Private Wallet不会存储密码，也无法帮你找回，请务必妥善保管密码"
        }
    }
}