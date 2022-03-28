package com.fanrong.frwallet.found

import android.text.method.PasswordTransformationMethod
import android.widget.EditText

fun EditText.extShowOrHide(isShow: Boolean, transaction: PasswordTransformationMethod) {
    if (isShow) {
        transformationMethod = null
    } else {
        transformationMethod = transaction
    }

    setSelection(text.length)
}