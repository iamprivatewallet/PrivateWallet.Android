package xc.common.tool.utils

import java.util.*

object RandomUtils {
    fun getRandomString(length: Int): String {
        val str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val sb = StringBuffer()
        for (i in 0 until length) {
            val number = random.nextInt(62)
            sb.append(str[number])
        }
        return sb.toString()
    }

    fun nextBoolean(): Boolean {
        return Random().nextBoolean()
    }
}