package xc.common.viewlib.utils

import android.view.View


fun View.extInvisibleOrVisible(isVisible: Boolean){
    if (isVisible) {
        visibility = View.VISIBLE
    } else {
        visibility = View.INVISIBLE
    }
}

fun View.extGoneOrVisible(isVisible: Boolean){
    if (isVisible) {
        visibility = View.VISIBLE
    } else {
        visibility = View.GONE
    }
}
