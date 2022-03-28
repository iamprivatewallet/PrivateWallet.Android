package xc.common.framework

import android.content.Context
import android.os.Looper

object Foundation {
    private lateinit var context: Context

    var mainHandler = android.os.Handler(Looper.getMainLooper())
    var attachBaseContext: AttachBaseContext? = null

    fun getContext(): Context {
        if (context == null) {
            throw IllegalStateException("BasicLibComponant must be init")
        }
        return context
    }

    fun init(context: Context) {
        Foundation.context = context
//        ToastUtils.init(context)
//        ToastUtils.setGravity(Gravity.NO_GRAVITY, 0, 0)
    }

    fun postMainDelay(action: Runnable, delay: Long) {
        mainHandler.postDelayed(action, delay)
    }

    fun postMainDelay(action: Runnable) {
        mainHandler.postDelayed(action, 0)
    }

    fun registerActivity_AttachBaseContext(attachBaseContext1: AttachBaseContext) {
        attachBaseContext = attachBaseContext1
    }

    interface AttachBaseContext {
        fun attachBaseContext(newBase: Context?): Context?
    }

}