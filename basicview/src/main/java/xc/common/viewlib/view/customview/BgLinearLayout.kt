package xc.common.viewlib.view.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.LinearLayout
import xc.common.viewlib.R
import xc.common.tool.utils.DensityUtil

class BgLinearLayout : LinearLayout {
    constructor(context: Context?) : this(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    var bgPaint: Paint
    var roundCorner: Int

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        setWillNotDraw(false)
        var a = context!!.resources.obtainAttributes(attrs, R.styleable.BgLinearLayout)
        var color = a.getColor(R.styleable.BgLinearLayout_mbackground_color, Color.TRANSPARENT)
        roundCorner = a.getInteger(R.styleable.BgLinearLayout_round_cornerdp, 0)

        roundCorner = DensityUtil.dp2px(context!!, roundCorner!!.toFloat())
        bgPaint = Paint()
        bgPaint.color = color
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawRoundRect(RectF(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat())
                , roundCorner!!.toFloat(), roundCorner!!.toFloat(), bgPaint)
        super.onDraw(canvas)
    }
}