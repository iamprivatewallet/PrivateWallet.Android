package xc.common.tool.utils

import com.google.gson.Gson

object XcSingleten {
    val gson:Gson by lazy {
        Gson()
    }
}