package xc.common.tool.utils

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LifecycleOwner
import xc.common.tool.listener.SimpleLifecycleObserver
import java.util.*
import kotlin.concurrent.timerTask

class SecondTimer {
    val mainThread = Handler(Looper.getMainLooper())

    private val lifecycleOwner: LifecycleOwner
    private var timer: Timer
    private var onCallListener: OnCallListener? = null

    constructor(lifecycleOwner: LifecycleOwner, onCallListener: OnCallListener) {
        this.lifecycleOwner = lifecycleOwner
        this.onCallListener = onCallListener
        lifecycleOwner.lifecycle.addObserver(object : SimpleLifecycleObserver() {
            override fun onDestroyCall() {
                destory()
            }
        })
        timer = Timer()

    }

    fun start() {
        timer.schedule(timerTask {
            mainThread.post {
                onCallListener?.onCall()
            }
        }, 0, 1000)
    }

    fun destory() {
        timer.cancel()
    }

    interface OnCallListener {
        fun onCall()
    }
}