package xc.common.kotlinext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import xc.common.framework.R


fun Fragment.extExecuteInAnimation() {

    activity?.overridePendingTransition(R.anim.fd_activity_in, R.anim.fd_activity_default)
}

fun Fragment.extExecuteOutAnimation() {
    activity?.overridePendingTransition( R.anim.fd_activity_default, R.anim.fd_activity_out)
}

fun Fragment.extFinishWithAnim(){
    activity?.extFinishWithAnim()
}

inline fun Fragment.extStartActivity(activity: Class<out Activity>) {
    val intent = Intent(getActivity(), activity)
    startActivity(intent)
    extExecuteInAnimation()
}

inline fun Fragment.extStartActivityForResult(
    activity: Class<out Activity>,
    bundle: Bundle,
    requestCode: Int
) {
    val intent = Intent(getActivity(), activity)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
    extExecuteInAnimation()
}

inline fun Fragment.extStartActivityForResult(activity: Class<out Activity>, requestCode: Int) {
    val intent = Intent(getActivity(), activity)
    startActivityForResult(intent, requestCode)
    extExecuteInAnimation()
}

inline fun Fragment.extStartActivity(activity: Class<out Activity>, bundle: Bundle) {
    val intent = Intent(getActivity(), activity)
    intent.putExtras(bundle)
    startActivity(intent)
    extExecuteInAnimation()
}