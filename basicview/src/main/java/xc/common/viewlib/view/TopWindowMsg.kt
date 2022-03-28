package xianchao.com.topmsg

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import xc.common.framework.lifecycle.OnDestroyObserver
import xc.common.viewlib.R

object TopWindowMsg {

    fun getPopupWindow(context: Context): TopMsgDialog {
        return TopMsgDialog(context)
    }


    class TopMsgDialog {
        var window: PopupWindow? = null
        var contentView: View

        constructor(context: Context) {
            contentView = LayoutInflater.from(context).inflate(R.layout.bv_toast_layout, null)
        }


        fun getMsgView(): TextView {
            return contentView.findViewById<TextView>(R.id.text)
        }

        fun getContainerView(): LinearLayout {
            return contentView.findViewById<LinearLayout>(R.id.ll_container)
        }

        fun getNotnullWindow():PopupWindow {
            if (window == null) {
                window = PopupWindow()
                window!!.contentView = contentView
                window!!.width = ViewGroup.LayoutParams.MATCH_PARENT
                window!!.height = ViewGroup.LayoutParams.WRAP_CONTENT
                window!!.isClippingEnabled = false
                window!!.animationStyle = R.style.bv_top_msg_dialog
            }
            return window!!
        }


        fun show(activity: AppCompatActivity) {
            activity.lifecycle.addObserver(object : OnDestroyObserver() {
                override fun onCallDestroy() {
                    Log.e("addObserver", "OnDestroyObserver")
                    getNotnullWindow().dismiss()
                }
            })
            getNotnullWindow().showAtLocation(activity.window.decorView, Gravity.NO_GRAVITY, 0, 0)
        }

        fun show(activity: AppCompatActivity, msg: String) {
            activity.lifecycle.addObserver(object : OnDestroyObserver() {
                override fun onCallDestroy() {
                    Log.e("addObserver", "OnDestroyObserver")
                    getNotnullWindow().dismiss()
                }
            })
            this.getMsgView().text = msg
            getNotnullWindow().showAtLocation(activity.window.decorView, Gravity.NO_GRAVITY, 0, 0)
        }


        fun dismiss() {
            getNotnullWindow().dismiss()
        }
    }
}