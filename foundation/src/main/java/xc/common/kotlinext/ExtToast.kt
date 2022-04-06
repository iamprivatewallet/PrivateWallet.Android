
package xc.common.kotlinext

import android.view.Gravity
import android.widget.Toast
import xc.common.tool.CommonTool


fun Any?.showToast(msg: String) {
    Toast.makeText(CommonTool.context, msg, Toast.LENGTH_SHORT).show()
}

fun Any.showToastAtMainThread(msg: String) {
    CommonTool.mainHandler.post {
        Toast.makeText(CommonTool.context, msg, Toast.LENGTH_SHORT).show()
    }
}