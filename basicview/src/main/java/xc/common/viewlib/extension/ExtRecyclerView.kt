package xc.common.viewlib.extension

import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import xc.common.viewlib.R

fun RecyclerView.extUpdateBg(drawalId: Int) {
    if (adapter == null) {
        return
    }
    if (adapter!!.itemCount < 1) {
        setBackgroundResource(drawalId)
    } else {
        setBackgroundColor(Color.parseColor("#00000000"))
    }
}

fun RecyclerView.extUpdateBg() {
    if (adapter == null) {
        return
    }
    if (adapter!!.itemCount < 1) {
        setBackgroundResource(R.drawable.bv_empty_data_bg_trans)
    } else {
        setBackgroundColor(Color.parseColor("#00000000"))
    }
}


fun RecyclerView.extUpdateBgHasHeader() {
    if (adapter == null) {
        return
    }
    if (adapter!!.itemCount < 2) {
        setBackgroundResource(R.drawable.bv_empty_data_bg_trans)
    } else {
        setBackgroundColor(Color.parseColor("#00000000"))
    }
}