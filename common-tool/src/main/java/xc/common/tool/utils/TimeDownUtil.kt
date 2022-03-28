package xc.common.tool.utils

import androidx.lifecycle.LifecycleOwner
import xc.common.tool.CommonTool
import xc.common.tool.listener.SimpleLifecycleObserver
import java.util.*


class TimeDownUtil(var lifecycleOwner: LifecycleOwner) {
    var timer: Timer? = null

    fun start(howLong: Int, callback: (current: Int) -> Unit) {
        var time = howLong

        timer = Timer()
        timer?.schedule(object : TimerTask() {
            override fun run() {
                time--
                if (time < 0) {
                    time = 0
                    timer?.cancel()
                    timer = null
                }

                CommonTool.mainHandler.post {
                    callback(time)
                }
            }

        }, 0, 1000)

        lifecycleOwner.lifecycle.addObserver(object : SimpleLifecycleObserver() {
            override fun onDestroyCall() {
                cancel()
            }
        })
    }

    fun cancel() {
        timer?.cancel()
        timer = null

    }

}