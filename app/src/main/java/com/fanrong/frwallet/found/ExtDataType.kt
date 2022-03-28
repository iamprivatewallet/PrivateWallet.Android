package com.fanrong.frwallet.found


fun Boolean?.extToInt(): Int {
    return if (this ?: false) 1 else 0
}