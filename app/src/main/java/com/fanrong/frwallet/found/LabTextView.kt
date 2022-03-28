package com.fanrong.frwallet.found

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.fanrong.frwallet.R
import xc.common.tool.utils.DensityUtil

class LabTextView : TextView {

    var isSelect = false

    constructor(context: Context?) : super(context) {
        initview()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initview()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initview()
    }

    fun initview() {
        setCompoundDrawablesWithIntrinsicBounds(R.drawable.point_yellow, 0, 0, 0)
        compoundDrawablePadding = DensityUtil.dp2px(10)
        setOnClickListener {
            isSelect = !isSelect
            isLightDrawable(isSelect)
        }
    }


    fun isLightDrawable(isLight: Boolean) {
//         isSelect = isLight
//        if (isLight) {
//            setCompoundDrawablesWithIntrinsicBounds(R.drawable.point_yellow, 0, 0, 0)
//        } else {
//
//            setCompoundDrawablesWithIntrinsicBounds(R.drawable.point_unselect, 0, 0, 0)
//
//        }

    }

}