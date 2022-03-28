package xc.common.viewlib.view.customview

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import xc.common.viewlib.R

abstract class FullScreenHalfUpDialog(context: Activity) : Dialog(context) {

    init {
        setOwnerActivity(context as Activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCanceledOnTouchOutside(false)

        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window!!.statusBarColor = Color.TRANSPARENT
            }
        }
        val from = LayoutInflater.from(context)
        val view = from
                .inflate(R.layout.basiclib_layout_dialog_top, null) as ViewGroup
        val realContent = from.inflate(getContentView(), view, false)
        view.findViewById<ViewGroup>(R.id.ll_container).addView(realContent)
        setContentView(view)

        initView()
    }

    abstract fun getContentView(): Int

    abstract fun initView()

    override fun show() {
        super.show()

        val layoutParams = window!!.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        window!!.attributes = layoutParams
    }
}