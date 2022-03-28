package com.fanrong.frwallet.found

import android.app.Activity
import android.graphics.Color
import android.view.View
import com.fanrong.frwallet.R
import xc.common.kotlinext.extFinishWithAnim
import xc.common.viewlib.view.customview.TitleLayout

fun TitleLayout.removeTopPadding() {
    findViewById<View>(R.id.ll_title_wraper).setPadding(0, 0, 0, 0)
}

fun TitleLayout.extInitCommonBgAutoBack(activity: Activity, title: String) {
    initTitle(activity, title, "#00FFFFFF", true)
}

fun TitleLayout.extSetRightBtnInfo(resouceId: Int, onClickListener: (v: View) -> Unit) {
    setRightBtnIcon(resouceId)
    setRightTextClickListener(object : View.OnClickListener {
        override fun onClick(v: View?) {
            onClickListener.invoke(v!!)
        }

    })
}

fun TitleLayout.initCommonRightTextView(msg: String, onClickListener: View.OnClickListener) {
    rightText = msg
    setRightTextColor(Color.parseColor("#0140C3"))
    setRightTextClickListener(onClickListener)
}

fun TitleLayout.initTitle(activity: Activity, title: String, bg: String, isBack: Boolean) {
    this.setTitleText(title)
    this.setBackgroundColor(Color.parseColor(bg))
    if (isBack) {
        this.setBackIcon(R.mipmap.src_lib_eui_icon_back)
        this.setOnBackClickListener {
            activity.extFinishWithAnim()
        }
    }
}