package xc.common.kotlinext

import android.graphics.drawable.Drawable
import androidx.annotation.DimenRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import xc.common.tool.CommonTool

inline fun Any.extGetString(@StringRes id: Int): String {
    return CommonTool.context!!.getString(id)
}

inline fun Any.extGetDimen(@DimenRes id: Int): Float {
    return CommonTool.context!!.resources.getDimension(id)
}

inline fun Any.extGetDrawable(@DrawableRes id: Int): Drawable? {
    return CommonTool.context!!.resources.getDrawable(id)
}

