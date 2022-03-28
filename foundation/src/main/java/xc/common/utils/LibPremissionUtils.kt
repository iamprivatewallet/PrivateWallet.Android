package xc.common.utils

import android.Manifest
import android.app.Activity
import androidx.fragment.app.Fragment
import com.yanzhenjie.permission.Action
import com.yanzhenjie.permission.AndPermission
import xc.common.kotlinext.showToast

public interface PermissonSuccess {
    fun hasSuccess()
}

public interface PermissonFailure {
    fun failure()
}

class DefaultPermissonFailure : PermissonFailure {
    override fun failure() {
        LibPremissionUtils.showToast()
    }

}

object LibPremissionUtils {


    fun needStore(fragment: Fragment, success: PermissonSuccess, failure: PermissonFailure = DefaultPermissonFailure()) {

        AndPermission.with(fragment)
            .runtime()
            .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onGranted(Action {
                success.hasSuccess()
            })
            .onDenied {
                failure.failure()
            }.start()
    }

    fun needStore(activity: Activity, success: PermissonSuccess, failure: PermissonFailure = DefaultPermissonFailure()) {

        // bug onGranted onDenied 都会执行
        // 添加变量判断
//        var  onGrantedCalled = false

        AndPermission.with(activity)
            .runtime()
            .permission(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            .onGranted(Action {
//                onGrantedCalled = true
                success.hasSuccess()
            })
            .onDenied {
                failure.failure()
//                if (!onGrantedCalled) {
//                }
            }.start()
    }


    fun needCamera(activity: Activity, success: PermissonSuccess) {

        AndPermission.with(activity)
            .runtime()
            .permission(Manifest.permission.CAMERA)
            .onGranted { success.hasSuccess() }
            .onDenied {
                showToast()
            }.start()
    }

    fun needCamera(fragment: Fragment, success: PermissonSuccess) {

        AndPermission.with(fragment)
            .runtime()
            .permission(Manifest.permission.CAMERA)
            .onGranted(object : Action<MutableList<String>> {
                override fun onAction(data: MutableList<String>?) {
                    success.hasSuccess()
                }
            })
            .onDenied {
                showToast()
            }.start()
    }


    fun showToast() {
        showToast("没有相应权限")
    }

}