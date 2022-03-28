package xc.common.viewlib.view.customview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import xc.common.viewlib.R

abstract class FullScreenPopupWindow : PopupWindow {
    constructor(context: Context?) : super(context, null, R.style.basiclib_transparent_dialog) {
        val view = LayoutInflater.from(context).inflate(getLayoutId(), null)
        width = ViewGroup.LayoutParams.MATCH_PARENT
        height = ViewGroup.LayoutParams.WRAP_CONTENT
//        var lp = (context!! as Activity).getWindow()
//                .getAttributes()
//        lp.alpha = 0.7f
//        ((context!! as Activity)).getWindow().setAttributes(lp);
        initView(view)
        contentView = view
    }

    abstract fun getLayoutId(): Int
    abstract fun initView(view: View)

}