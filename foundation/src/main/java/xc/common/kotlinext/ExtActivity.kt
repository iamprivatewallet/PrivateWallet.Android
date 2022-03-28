package xc.common.kotlinext

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import xc.common.framework.R

fun Activity.extExecuteInAnimation() {
    overridePendingTransition(R.anim.fd_activity_in,  R.anim.fd_activity_default)
}

fun Activity.extExecuteOutAnimation() {
    overridePendingTransition( R.anim.fd_activity_default, R.anim.fd_activity_out)
}

fun Activity.extStartActivity(activity: Class<out Activity>) {
    val intent = Intent(this, activity)
    this.startActivity(intent)
    extExecuteInAnimation()
}

fun Activity.extStartActivityClearTop(activity: Class<out Activity>) {
    val intent = Intent(this, activity)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
    this.startActivity(intent)
    extExecuteInAnimation()
}

fun Activity.extStartActivity(activity: Class<out Activity>, bundle: Bundle) {
    val intent = Intent(this, activity)
    intent.putExtras(bundle)
    this.startActivity(intent)
    extExecuteInAnimation()
}

fun Activity.extStartActivityNewTask(activity: Class<out Activity>) {
    extStartActivityNewTask(activity, Bundle())
}

fun Activity.extStartActivityNewTask(activity: Class<out Activity>, bundle: Bundle) {
    val intent = Intent(this, activity)
    intent.putExtras(bundle)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    this.startActivity(intent)
    extExecuteInAnimation()
}

fun Activity.extStartActivityForResult(activity: Class<out Activity>, requestCode: Int) {
    val intent = Intent(this, activity)
    startActivityForResult(intent, requestCode)
    extExecuteInAnimation()
}

fun Activity.extStartActivityForResult(
    activity: Class<out Activity>,
    bundle: Bundle,
    requestCode: Int
) {
    val intent = Intent(this, activity)
    intent.putExtras(bundle)
    startActivityForResult(intent, requestCode)
    extExecuteInAnimation()

}

fun Activity.extFinishWithAnim() {
    finish()
    extExecuteOutAnimation()
}

fun Activity.extFullStatusBar() {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
    }
}
