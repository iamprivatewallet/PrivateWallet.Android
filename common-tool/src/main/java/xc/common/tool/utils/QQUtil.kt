package xc.common.tool.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri

class QQUtil {

    companion object {
        fun contactQQ(activity: Activity, qqNum: String,action:()->Unit={}) {
            try {
                val url = "mqqwpa://im/chat?chat_type=wpa&uin=$qqNum"//uin是发送过去的qq号码
                activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
            } catch (e: Exception) {
                e.printStackTrace()
                action()
            }
        }

        fun joinQQGroup(activity: Activity, qqGroupKey: String,action:()->Unit={}) {
            val intent = Intent()
            intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$qqGroupKey")
            try {
                activity.startActivity(intent)
            } catch (e: Exception) {
                action()
            }
        }
    }
}