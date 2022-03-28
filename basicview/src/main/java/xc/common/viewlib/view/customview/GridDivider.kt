package xc.common.viewlib.view.customview

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GridDivider : RecyclerView.ItemDecoration {

    class Builder {
        private var dividerWidth = 10
        private var dividerColor = Color.parseColor("#000000")

        fun setDividerWidth(int: Int): Builder {
            dividerWidth = int
            return this
        }

        fun setDividerColor(color: Int): Builder {
            dividerColor = color
            return this
        }

        fun build(): GridDivider {
            return GridDivider(dividerWidth, dividerColor)
        }
    }


    private constructor(dividerWidth: Int, dividerColor: Int) : super() {
        this.dividerWidth = dividerWidth
        setDividerColor(dividerColor)
    }

    private var dividerWidth = 10
    private var dividerPaint = Paint()

    fun getDividerWidth(): Int {
        return dividerWidth
    }

    fun setDividerColor(color: Int) {
        dividerPaint.color = color
    }


    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        if (parent.layoutManager is GridLayoutManager) {
            initGrid(outRect, view, parent, state, parent.layoutManager as GridLayoutManager)
        }
    }

    private fun initGrid(outRect: Rect, view: View, parent: RecyclerView,
                         state: RecyclerView.State, gridLayoutManager: GridLayoutManager) {
        val spanCount = gridLayoutManager.spanCount
        val indexOfChild = parent.indexOfChild(view)
//        SWLog.e(outRect.toString())
//        if (view.getTag() != null) {
//            return
//        }
//        SWLog.e("${view.measuredWidth}  ${view.measuredHeight} view.getTag ${view.getTag()} ")


        val fl = dividerWidth
        outRect.top = 0
        outRect.bottom = fl.toInt()
        outRect.left = 0
        outRect.right = fl.toInt()
        // 最右边
        if ((indexOfChild + 1) % spanCount == 0) {
            outRect.right = 0
            view.setTag(ViewType.RIGHT)
        } else {
            view.setTag(ViewType.EMPTY)
        }
        // 最下边
        if (indexOfChild >= parent.adapter!!.itemCount - (parent.adapter!!.itemCount % spanCount)) {
            outRect.bottom = 0
        }

    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {

        super.onDraw(c, parent, state)
        for (i in 0..(parent.childCount - 1)) {
            val childAt = parent.getChildAt(i)
            var rect = Rect()
//            childAt.getDrawingRect(rect)
//            parent.getChildVisibleRect(childAt, rect, null)
            rect.top = ViewCompat.getY(childAt).toInt()
            rect.left = ViewCompat.getX(childAt).toInt()
            rect.bottom = ViewCompat.getY(childAt).toInt() + childAt.measuredHeight
            rect.right = ViewCompat.getX(childAt).toInt() + childAt.measuredWidth

            if (childAt.getTag() == ViewType.RIGHT) {
                // 画下边
                c.drawRect(Rect(rect.left, rect.bottom, rect.right, rect.bottom + dividerWidth), dividerPaint)
            } else {
                // 画右边
                c.drawRect(Rect(rect.right, rect.top, rect.right + dividerWidth, rect.bottom + dividerWidth), dividerPaint)
                // 画下边
                c.drawRect(Rect(rect.left, rect.bottom, rect.right, rect.bottom + dividerWidth), dividerPaint)
            }
        }

    }

    enum class ViewType {
        RIGHT, EMPTY
    }


}