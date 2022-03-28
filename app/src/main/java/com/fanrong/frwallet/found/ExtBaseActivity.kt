package com.fanrong.frwallet.found

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.basiclib.base.BaseActivity


inline fun BaseActivity.extStartActivityForResult(
    activity: Class<out Activity>, requestCode: Int, noinline callback: ((resultCode: Int, data: Intent?) -> Unit)
) {
    val intent = Intent(this, activity)
    startActivityForResult(intent, requestCode)
    onActivityResultListeners.put(requestCode, callback)
}

inline fun BaseActivity.extStartActivityForResult(
    activity: Class<out Activity>, params: Bundle, requestCode: Int, noinline callback: ((resultCode: Int, data: Intent?) -> Unit)
) {
    val intent = Intent(this, activity)
    intent.putExtras(params)
    startActivityForResult(intent, requestCode)
    onActivityResultListeners.put(requestCode, callback)
}
