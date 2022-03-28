package com.fanrong.frwallet.ui.backup

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.fanrong.frwallet.R

class SelectWordView:LinearLayout {

    val errorView:ImageView by lazy {
        findViewById<ImageView>(R.id.iv_error)
    }

    val textView:TextView by lazy {
        findViewById<TextView>(R.id.tv_words)
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)



}