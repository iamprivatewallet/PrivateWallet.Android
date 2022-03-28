package com.fanrong.frwallet.found

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.widget.TextView

@SuppressLint("AppCompatCustomView")
class SelfTextView : TextView {

    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initv()
    }

    private fun initv() {

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val drawable = compoundDrawables[1]
        if (drawable != null) {

        }
    }
}