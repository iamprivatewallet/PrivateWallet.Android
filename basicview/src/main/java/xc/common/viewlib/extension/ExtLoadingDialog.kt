package xc.common.viewlib.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import xc.common.tool.listener.SimpleLifecycleObserver
import xc.common.viewlib.view.LoadingDialog
import kotlin.reflect.KProperty


/**
 * 属性委托，
 *  所有owner 对象都用一个代理对象。 及多个对象调用get set 指向的是同一个代理对象
 */
private class LoadingDialogDelegate {
    var loading: LoadingDialog? = null

    operator fun setValue(
        thisRef: AppCompatActivity,
        property: KProperty<*>,
        value: LoadingDialog?
    ) {
        this.loading = value
    }

    operator fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): LoadingDialog {
        if (loading == null) {
            loading = LoadingDialog(thisRef)
            loading!!.setCanceledOnTouchOutside(false)
            println("create LoadingDialog   .........")
        }
        // activity 结束，loading 回收。 防止 多个页面用同一个loading
        thisRef.lifecycle.addObserver(object : SimpleLifecycleObserver() {
            override fun onDestroyCall() {
                loading = null
            }
        })

        return this.loading!!
    }
}

private var AppCompatActivity.extLoadingDialog: LoadingDialog by LoadingDialogDelegate()

fun AppCompatActivity.extShowOrDismissDialog(isShow: Boolean) {
    runOnUiThread {
        if (isShow) {
            extShowLoadingDialog()
        } else {
            extDismissDialog()
        }
    }
}

fun AppCompatActivity.extShowLoadingDialog() {
    extLoadingDialog.dismiss()
    extLoadingDialog.show()
}

fun AppCompatActivity.extDismissDialog() {
    extLoadingDialog.dismiss()
}

fun Fragment.extShowLoadingDialog() {
    (activity as AppCompatActivity).extLoadingDialog.show()
}

fun Fragment.extDismissDialog() {
    (activity as AppCompatActivity).extLoadingDialog.dismiss()
}

fun Fragment.extShowOrDismissDialog(isShow: Boolean) {
    if (isShow) {
        extShowLoadingDialog()
    } else {
        extDismissDialog()
    }
}
