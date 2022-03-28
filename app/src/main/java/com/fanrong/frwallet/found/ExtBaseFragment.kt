package com.fanrong.frwallet.found

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import xc.common.framework.ui.base.BaseFragment


inline fun BaseFragment.extStartActivityForResult(
    activity: Class<out Activity>, requestCode: Int, noinline callback: ((resultCode: Int, data: Intent?) -> Unit)
) {
    val intent = Intent(getActivity(), activity)
    startActivityForResult(intent, requestCode)
    onActivityResultListeners.put(requestCode, callback)
}

inline fun BaseFragment.extStartActivityForResult(
    activity: Class<out Activity>, params: Bundle, requestCode: Int, noinline callback: ((resultCode: Int, data: Intent?) -> Unit)
) {
    val intent = Intent(getActivity(), activity)
    intent.putExtras(params)
    startActivityForResult(intent, requestCode)
    onActivityResultListeners.put(requestCode, callback)
}
