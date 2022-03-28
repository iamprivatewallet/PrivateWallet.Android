package xc.common.viewlib

import android.app.Application
import android.os.Handler
import android.os.Looper

object BasicView {
    lateinit var context:Application
    val mainHandler = Handler(Looper.getMainLooper())
    fun init(context:Application){
        this.context = context
    }
}