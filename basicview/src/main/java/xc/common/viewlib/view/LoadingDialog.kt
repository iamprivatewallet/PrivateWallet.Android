package xc.common.viewlib.view

import android.app.Activity
import xc.common.viewlib.R
import kotlinx.android.synthetic.main.bv_dialog_loading.*
import xc.common.viewlib.BasicView
import xc.common.viewlib.view.customview.FullScreenDialog

class LoadingDialog(activity: Activity) : FullScreenDialog(activity) {
    override fun getContentView(): Int {
        return R.layout.bv_dialog_loading
    }

    override fun initView() {
        BasicView.mainHandler.post {
            lv_circular_ring?.startAnim()
        }
    }


    override fun show() {
        super.show()
        BasicView.mainHandler.post {
            lv_circular_ring?.startAnim()
        }
    }

    override fun dismiss() {
        lv_circular_ring?.stopAnim()
        super.dismiss()
    }
}
