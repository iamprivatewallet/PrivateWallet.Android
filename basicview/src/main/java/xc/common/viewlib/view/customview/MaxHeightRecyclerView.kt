package xc.common.viewlib.view.customview

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView

class MaxHeightRecyclerView : RecyclerView {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        var heightSpec = MeasureSpec.makeMeasureSpec(dp2px(240f), MeasureSpec.AT_MOST);
        super.onMeasure(widthSpec, heightSpec)

    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(dpValue: Float): Int {
        var scale = getResources().getDisplayMetrics().density
        return (dpValue * scale + 0.5f).toInt()
    }
}