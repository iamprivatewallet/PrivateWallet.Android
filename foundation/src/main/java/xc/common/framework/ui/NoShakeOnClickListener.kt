package xc.common.framework.listener

import android.view.View
import xc.common.framework.R

interface NoShakeOnClickListener : View.OnClickListener {
    override fun onClick(v: View?) {
        val l = if (v!!.getTag(R.id.basiclib_last_click_time) == null) {
            0L
        } else {
            v!!.getTag(R.id.basiclib_last_click_time) as Long
        }
        if (System.currentTimeMillis() - l < 1000) {
            return
        } else {
            v.setTag(R.id.basiclib_last_click_time, System.currentTimeMillis())
            onNoShakeClick(v)
        }
    }

    fun onNoShakeClick(v: View)

}