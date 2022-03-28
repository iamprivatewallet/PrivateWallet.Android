package xc.common.viewlib.view.customview

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import xc.common.utils.LibAppUtils
import xc.common.viewlib.R
import xc.common.viewlib.utils.NavigationBarInfo

abstract class FullScreenDialog(context: Context) : Dialog(context, R.style.bv_BottomDialog) {

    init {
        setOwnerActivity(context as Activity)
    }

    var onConfrim: OnConfirmListener? = null
    var onCancel: OnCancelListener? = null


    /**
     * 必须在构造方法里边初始化才有效
     */
    protected open var contentGravity: Int? = null

    protected var dialogBgColor = Color.TRANSPARENT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(canceledOutside)

        window!!.setBackgroundDrawableResource(android.R.color.transparent)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window!!.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window!!.statusBarColor = Color.TRANSPARENT
            }
        }
        val from = LayoutInflater.from(context)
        val view = from
            .inflate(R.layout.basiclib_layout_dialog_container, null) as ViewGroup
        view.addView(from.inflate(getContentView(), view, false))


        if (LibAppUtils.hasVirtualNavigationBar(context)) {
            val barHeight = LibAppUtils.getNavigationBarHeight(context)
            view.setPadding(0, 0, 0, barHeight)
        }

        view.setOnClickListener {
            if (canceledOutside) {
                dismiss()
            }
        }
        if (view.childCount > 0) {
            view.getChildAt(0)?.setOnClickListener {
            }
        }

        view.setBackgroundColor(dialogBgColor)

        if (!(contentGravity == null || contentGravity == 0)) {
            setGravity(view, contentGravity!!)
        }

        setContentView(view)

        initView()
    }

    private fun setGravity(view: ViewGroup, gravity: Int) {
        if (view is LinearLayout) {
            view.gravity = gravity
        }
    }

    var canceledOutside = true
    override fun setCanceledOnTouchOutside(cancel: Boolean) {
        super.setCanceledOnTouchOutside(cancel)
        canceledOutside = cancel
    }

    abstract protected fun getContentView(): Int

    abstract protected fun initView()

    override fun show() {
        super.show()
        val layoutParams = window!!.attributes
        layoutParams.width = window!!.getWindowManager().getDefaultDisplay().getWidth();
        layoutParams.height = NavigationBarInfo.getHeight(ownerActivity!!).toInt()
        layoutParams.gravity = Gravity.BOTTOM;
        window!!.attributes = layoutParams

    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


    interface OnConfirmListener {
        fun confirm(params: Any?)
    }

    interface OnCancelListener {
        fun cancel()
    }
}