package xc.common.tool.utils

import android.graphics.Rect
import android.view.ViewTreeObserver
import android.view.Window

object InputSoftUtils {

    interface OnKeyboardStatusChangeListener {
        fun onChanged(isShow: Boolean)
    }

    fun autoJustLayoutWithScrollView(window: Window) {

        window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener {
            val r = Rect()
            window.decorView.getWindowVisibleDisplayFrame(r)
            val hidenHeight = window.getDecorView().getHeight() - r.height()

            if (hidenHeight > 300) {
                val bottom = hidenHeight - DensityUtil.dp2px(20)
                if (window.getDecorView().getPaddingBottom() != bottom) {
                    window.getDecorView().setPadding(0, 0, 0,
                            bottom)
                }
            } else {
                if (window.getDecorView().getPaddingBottom() != 0) {
                    window.getDecorView().setPadding(0, 0, 0, 0)
                }
            }
        })
    }

    fun setOnKeyboardStatusChangeListener(window: Window, listener: OnKeyboardStatusChangeListener) {
        window.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            var lastHidenHeight = -1;
            override fun onGlobalLayout() {

                val r = Rect()
                window.decorView.getWindowVisibleDisplayFrame(r)
                val hidenHeight = window.getDecorView().getHeight() - r.height()
                if (lastHidenHeight == hidenHeight) {
                    return
                }
                lastHidenHeight = hidenHeight
                listener.onChanged(hidenHeight > 400)
            }
        })
    }

    fun isShowInputSoft(window: Window): Boolean {
        val r = Rect()
        window.decorView.getWindowVisibleDisplayFrame(r)
        val hidenHeight = window.getDecorView().getHeight() - r.height()
        return hidenHeight > 400
    }
}