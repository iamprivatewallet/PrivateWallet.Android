package com.yzq.zxinglibrary

import android.content.Context
import android.view.Gravity
import android.view.TextureView
import android.widget.TextView
import xc.common.viewlib.view.customview.FullScreenDialog

class BottomJumpDialog(context: Context) : FullScreenDialog(context) {
    var onAddAdrresListener: OnAddAdrresListener? = null
    override var contentGravity: Int? = Gravity.BOTTOM

    override fun getContentView(): Int {
        return R.layout.dialog_bottom_jump
    }

    override fun initView() {
        val tv_bj_cancel = findViewById<TextView>(R.id.tv_bj_cancel)
        val tv_bj_xjdz = findViewById<TextView>(R.id.tv_bj_xjdz)
        val tv_bj_zz = findViewById<TextView>(R.id.tv_bj_zz)
        tv_bj_xjdz.setOnClickListener {
            onAddAdrresListener!!.jump()
        }
        tv_bj_cancel.setOnClickListener {
            dismiss()
        }

        tv_bj_zz.setOnClickListener {
            onConfrim?.confirm(null)
        }
    }
    interface OnAddAdrresListener {
        fun jump()
    }
}