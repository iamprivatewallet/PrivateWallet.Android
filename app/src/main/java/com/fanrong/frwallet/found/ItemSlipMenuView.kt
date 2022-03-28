package com.fanrong.frwallet.found

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.Scroller
import android.widget.TextView
import xc.common.tool.utils.DensityUtil

class ItemSlipMenuView : LinearLayout {

    var onMenuClickListener: OnClickListener? = null

    lateinit var scroller: Scroller

    constructor(context: Context?) : super(context) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initView()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    fun initView() {
        scroller = Scroller(context)
    }


    fun enableRightMenu(view: View) {
        val textView = TextView(context)
        addView(view, childCount)

    }


    fun openRightMenu() {
        val textView = TextView(context)
        val layoutParams = LayoutParams(DensityUtil.dp2px(70), LayoutParams.MATCH_PARENT)
        textView.layoutParams = layoutParams
        textView.setText("删除")
        textView.gravity = Gravity.CENTER
        textView.textSize = 15.toFloat()
        textView.setTextColor(Color.parseColor("#FFFFFF"))
        textView.setBackgroundColor(Color.parseColor("#FF0000"))
        textView.setOnClickListener {
            onMenuClickListener?.onClick(it)
        }
        addView(textView, childCount)
        invalidate()
        scroller.startScroll(0, 0, DensityUtil.dp2px(70), 0, 1000)
    }

    override fun computeScroll() {
        super.computeScroll()
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());//
            invalidate()
        }
    }


    fun closeRightMenu() {
        scroller.startScroll(scrollX, 0, -scrollX, 0, 1000)
        invalidate()
    }
}