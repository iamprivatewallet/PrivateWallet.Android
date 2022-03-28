package xc.common.framework.ui.base

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import xc.common.framework.R
import xc.common.framework.listener.NoShakeOnClickListener

abstract class BaseFragment : Fragment(), NoShakeOnClickListener {

    val onActivityResultListeners = mutableMapOf<Int, (resultCode: Int, data: Intent?) -> Unit>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        loadData()
    }

    fun startActivity(clazz: Class<out Activity>) {
        activity!!.startActivity(Intent(activity, clazz))
        activity!!.overridePendingTransition(
            R.anim.basiclib_slide_right_enter,
            R.anim.basiclib_slide_left_exit
        )
    }

    fun startActivity(clazz: Class<out Activity>, bundle: Bundle) {
        var intent = Intent(activity, clazz)
        intent.putExtras(bundle)
        activity!!.startActivity(intent)
        activity!!.overridePendingTransition(
            R.anim.basiclib_slide_right_enter,
            R.anim.basiclib_slide_left_exit
        )
    }

    abstract fun getLayoutId(): Int
    abstract fun initView()
    abstract fun loadData()
    abstract override fun onNoShakeClick(v: View)

    open fun onActivityRestart() {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResultListeners.get(requestCode)?.invoke(resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        onActivityResultListeners.clear()
    }

    open fun onBackPressed(){

    }
}