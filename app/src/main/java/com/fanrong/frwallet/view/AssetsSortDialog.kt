package com.fanrong.frwallet.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.fanrong.frwallet.R
import com.fanrong.frwallet.inteface.OnAssetsSortDialogItemClick

/**
 * 选择钱包弹框
 */
class AssetsSortDialog(
    var activity: AppCompatActivity,
    val onAssetsSortDialogItemClick: OnAssetsSortDialogItemClick
) : DialogFragment() {

    private val TAG = "base_bottom_dialog"

    private val DEFAULT_DIM = 0.5f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(getCancelOutside())



        val v = inflater.inflate(getContentView(), container, false)
        initView(v)
        return v
    }

    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val params = window?.attributes
        params?.dimAmount = getDimAmount()
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        val outMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(outMetrics)
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        params?.gravity = Gravity.BOTTOM
        window?.attributes = params
        dialog!!.window?.setWindowAnimations(R.style.BottomDialogAnimation)
    }

    fun getHeight(): Int {
        return -1
    }

    fun getDimAmount(): Float {
        return DEFAULT_DIM
    }

    fun getCancelOutside(): Boolean {
        return true
    }

    fun getFragmentTag(): String {
        return TAG
    }

    fun show() {
        show(activity.supportFragmentManager)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, getFragmentTag())
    }

    fun getContentView() = R.layout.dialog_assets_sort

    private fun initView(view: View) {
        val tv_default = view.findViewById<TextView>(R.id.tv_default)
        val tv_value = view.findViewById<TextView>(R.id.tv_value)
        val tv_name = view.findViewById<TextView>(R.id.tv_name)
        val tv_cancel = view.findViewById<TextView>(R.id.tv_cancel)
        tv_default.setOnClickListener {
            onAssetsSortDialogItemClick.selectSort(tv_default.text.toString())
            dismiss()
        }
        tv_value.setOnClickListener {
            onAssetsSortDialogItemClick.selectSort(tv_value.text.toString())
            dismiss()
        }
        tv_name.setOnClickListener {
            onAssetsSortDialogItemClick.selectSort(tv_name.text.toString())
            dismiss()
        }
        tv_cancel.setOnClickListener {
            dismiss()
        }
    }
}
