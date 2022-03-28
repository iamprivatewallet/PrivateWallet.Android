package xc.common.tool.utils

import org.json.JSONObject
import java.math.BigDecimal

inline fun String?.isHexStr(): Boolean {
    if (this.checkIsEmpty()) {
        return false
    } else {
        var value = this!!.removePrefix("0x")
        if (value.length % 2 != 0) {
            return false
        }
        return value.matches(Regex("^-?[0-9a-fA-F]+"))
    }
}

public inline fun String?.checkIsEmpty(): Boolean {
    return this == null || this.isEmpty()
}

public inline fun String?.checkNotEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

inline fun String?.checkIsPhone(): Boolean {
    return this != null && this.startsWith("1") && this.length == 11
}

inline fun String?.checkIsOnlyNumber(): Boolean {
    return this != null && this.matches(Regex("^[0-9]{0,}$"))
}

/**
 * 是否是大于0 的整整数
 */
inline fun String?.checkIsPosNumber(): Boolean {
    try {
        return BigDecimal(this).compareTo(BigDecimal.ZERO) > 0
    } catch (e: java.lang.Exception) {
        return false
    }
}


inline fun String?.checkIsEmail(): Boolean {
    return this != null && this.matches(Regex("^\\S+@\\S+\\.\\S+$"))
}

inline fun String?.checkIsQQ(): Boolean {
    return this != null && this.matches(Regex("^[0-9]{5,11}$"))
}

inline fun String?.checkIsBankCard(): Boolean {
    return this != null && this.matches(Regex("^[0-9]{16,}$"))
}

inline fun String?.checkIsChinaWords(): Boolean {
    return this != null && this.matches(Regex("^[\\u4e00-\\u9fa5]+$"))
}


inline fun String?.checkIsRealName(): Boolean {
    return this != null && this.matches(Regex("^[a-zA-Z\\u4e00-\\u9fa5 _]+$"))
}

inline fun String?.checkIsNickName(): Boolean {
    return this != null && this.matches(Regex("^[a-zA-Z0-9\\u4e00-\\u9fa5]+$"))
}

inline fun String?.checkIsNotNickName(): Boolean {
    return this == null || !this.matches(Regex("^[()_（）a-zA-Z0-9\\u4e00-\\u9fa5]+$"))
}


inline fun String?.checkIsUsdtAddress(): Boolean {
    return this != null && this.matches(Regex("^[a-zA-Z0-9]{33,34}$"))
}

inline fun String?.checkIsTransHash(): Boolean {
    return this != null && this.matches(Regex("^[a-zA-Z0-9]{64,66}$"))
}


inline fun String?.checkIsChaniCard(): Boolean {
    return this != null && this.matches(Regex("^[a-zA-Z0-9]{15,18}$"))
}

inline fun String?.checkIsPassportCard(): Boolean {
    return this != null && this.matches(Regex("^[a-zA-Z0-9]{7,}$"))
}


public inline fun List<*>?.checkIsEmpty(): Boolean {
    return this == null || this.isEmpty()
}

public inline fun List<*>?.checkNotEmpty(): Boolean {
    return this != null && !this.isEmpty()
}


public inline fun Map<*, *>?.checkIsEmpty(): Boolean {
    return this == null || this.isEmpty()
}

public inline fun Map<*, *>?.checkNotEmpty(): Boolean {
    return this != null && !this.isEmpty()
}

public inline fun MutableMap<String, String>.putSafety(key: String, value: String?) {
    this.put(key, if (value.checkIsEmpty()) "" else value!!)
}

inline fun Boolean?.checkedIsTrue(): Boolean {
    return this != null && this
}

fun String?.isJsonObj(): Boolean {
    try {
        val fromJson = JSONObject(this)

        return fromJson != null
    } catch (e: Exception) {
        return false
    }
}
