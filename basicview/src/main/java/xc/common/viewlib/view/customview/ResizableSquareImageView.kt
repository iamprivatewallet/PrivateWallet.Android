package xc.common.viewlib.view.customview

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView

class ResizableSquareImageView : ImageView {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = MeasureSpec.getSize(widthMeasureSpec)
        setMeasuredDimension(width, width)
    }

}
