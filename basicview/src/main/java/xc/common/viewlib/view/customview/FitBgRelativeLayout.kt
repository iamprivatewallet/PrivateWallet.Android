package xc.common.viewlib.view.customview

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout

class FitBgRelativeLayout : RelativeLayout {
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val drawable = background
        if (drawable != null) {
            val width = View.MeasureSpec.getSize(widthMeasureSpec)
            val height = Math.ceil((width.toFloat() * drawable.intrinsicHeight.toFloat() / drawable.intrinsicWidth.toFloat()).toDouble()).toInt()

            super.onMeasure(View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY)
                    , View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY))
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
