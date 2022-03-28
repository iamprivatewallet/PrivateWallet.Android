package xc.common.tool

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import com.google.gson.Gson
import java.lang.RuntimeException

object CommonTool {
    fun init(myApplication: Application) {
        this.context = myApplication
    }

    val mainHandler: Handler by lazy {
        Handler(Looper.getMainLooper())
    }

    var context: Application? = null
        get() {
            if (field == null) {
                throw RuntimeException("context 没有被初始化。。。。。。。。。")
            }
            return field!!
        }
        set(value) {
            field = value
        }


    val gson: Gson by lazy {
        Gson()
    }
}