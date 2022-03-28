package xc.common.viewlib.view

import android.app.Activity
import android.view.View
import xc.common.viewlib.R
import kotlinx.android.synthetic.main.bv_dialog_common_hint.*
import xc.common.viewlib.view.customview.FullScreenDialog

abstract class CommonHintDialog(activity: Activity) : FullScreenDialog(activity) {

    private var dialogBtnListener: DialogBtnListener? = null
    private var msg: String = ""

    fun setDialogBtnListener(dialogBtnListener: DialogBtnListener): CommonHintDialog {
        this.dialogBtnListener = dialogBtnListener
        return this
    }

    fun setMsg(msg: String): CommonHintDialog {
        this.msg = msg
        return this
    }


    override fun getContentView(): Int {
        return R.layout.bv_dialog_common_hint
    }

    override fun initView() {
        initView(ll_dialog_view)
    }

    abstract fun initView(contentView: View)

    interface DialogBtnListener {
        fun onCancel()
        fun onConfirm()
    }
}