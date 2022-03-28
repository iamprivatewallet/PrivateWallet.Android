package com.fanrong.frwallet.tools

import com.google.gson.Gson

object NetTools {
    fun <T> formatJson(json: String, clazz: Class<T>): T? {
        try {
            return Gson().fromJson(json, clazz)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}