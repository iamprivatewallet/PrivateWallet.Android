package xc.common.viewlib.view.customview

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.children
import androidx.core.view.get
import xc.common.viewlib.R

class ButtomTabView : LinearLayout, View.OnClickListener {
    companion object {
        var TAG = "ButtomTabView"
    }


    var labInfos: MutableList<TabData> = mutableListOf()
        set(value) {
            field.clear()
            field.addAll(value)
            initViews()
        }

    var labTextCheckColor = Color.BLACK
    var labTextUncheckColor = Color.GRAY
    var labTextSize = 15

    var onTabClickListener: OnTabClickListener? = null


    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) :
            super(
                context, attrs, defStyleAttr
            ) {
        if (attrs != null) {

            val obtainStyledAttributes =
                context!!.obtainStyledAttributes(attrs, R.styleable.buttomTabView)

            labTextSize = obtainStyledAttributes.getDimensionPixelSize(
                R.styleable.buttomTabView_tabTextSize,
                labTextSize
            )
            labTextUncheckColor = obtainStyledAttributes.getColor(
                R.styleable.buttomTabView_tabTextUncheckColor,
                labTextUncheckColor
            )
            labTextCheckColor = obtainStyledAttributes.getColor(
                R.styleable.buttomTabView_tabTextColor,
                labTextCheckColor
            )
        }
        gravity = Gravity.CENTER_VERTICAL
    }

    private fun initViews() {
        if (labInfos == null || labInfos.isEmpty()) {
            Log.d(TAG, "labInfos is null or empty");
        }

        removeAllViews()

        for (index in labInfos.indices) {

            var labInfo = labInfos.get(index)

            var wrapParams = LayoutParams(1, ViewGroup.LayoutParams.MATCH_PARENT)
            wrapParams.weight = 1.0f

            val linearLayout = LinearLayout(context)
            linearLayout.layoutParams = wrapParams
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.gravity = Gravity.CENTER_HORIZONTAL


            val imageView = ImageView(context)
            imageView.setImageResource(labInfo.labUncheckedIcon)

            val view = View(context)


            val textView = TextView(context)
            textView.setText(labInfo.labName)
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, labTextSize.toFloat())
            textView.gravity = Gravity.CENTER

            linearLayout.tag = index
            linearLayout.setOnClickListener(this)
            linearLayout.addView(imageView)
            linearLayout.addView(view)
            linearLayout.addView(textView)

            var imageViewParams = imageView.layoutParams as LinearLayout.LayoutParams
            imageViewParams.setMargins(0, dp2px(context, 6f), 0, 0)
            imageView.layoutParams = imageViewParams

            var viewParams = view.layoutParams as LinearLayout.LayoutParams
            viewParams.height = 1
            viewParams.weight = 1.0f
            view.layoutParams = viewParams

            var textViewParams = textView.layoutParams as LinearLayout.LayoutParams
            textViewParams.setMargins(0, 0, 0, dp2px(context, 3f))
            textView.layoutParams = textViewParams

            addView(linearLayout)
        }

    }

    fun switchToIndex(index: Int) {
        children.toMutableList().get(index).callOnClick()
    }

    override fun onClick(v: View?) {
        var index = (v!!.tag as Int)
        onTabClickListener?.onTabClicked(index, labInfos.get(index))

        children.forEachIndexed { indexT, view ->

            (((view as LinearLayout).get(0)) as ImageView).setImageResource(labInfos.get(indexT).labUncheckedIcon)
            //tab图标为选中的颜色
//            (((view as LinearLayout).get(0)) as ImageView).setColorFilter(Color.argb(189, 197, 207, 1))
            (((view as LinearLayout).get(2)) as TextView).setTextColor(labTextUncheckColor)
        }

        (((v as LinearLayout).get(0)) as ImageView).setImageResource(labInfos.get(index).labCheckedIcon)
        //tab图标选中的颜色
//        (((v as LinearLayout).get(0)) as ImageView).setColorFilter(Color.parseColor("#10D574"))
        (((v as LinearLayout).get(2)) as TextView).setTextColor(labTextCheckColor)

    }

    fun clickTab(position: Int) {
        if (childCount > position) {
            getChildAt(position).callOnClick()
        }
    }


    class TabData {
        var labName: String = ""
        var labCheckedIcon: Int
        var labUncheckedIcon: Int

        constructor(labName: String, labIcon: Int, labUncheckedIcon: Int) {
            this.labName = labName
            this.labCheckedIcon = labIcon
            this.labUncheckedIcon = labUncheckedIcon
        }
    }

    interface OnTabClickListener {
        fun onTabClicked(index: Int, data: TabData)
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        return (dpValue * context.resources.displayMetrics.density + 0.5f).toInt()
    }
}