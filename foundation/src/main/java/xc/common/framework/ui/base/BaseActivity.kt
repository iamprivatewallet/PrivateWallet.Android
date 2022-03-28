package com.basiclib.base

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import xc.common.framework.Foundation
import xc.common.framework.listener.NoShakeOnClickListener
import xc.common.framework.ui.base.BaseFragment
import xc.common.tool.utils.SWLog
import xc.common.utils.LibAppUtils

abstract class BaseActivity() : AppCompatActivity(), NoShakeOnClickListener {


    val onActivityResultListeners = mutableMapOf<Int, (resultCode: Int, data: Intent?) -> Unit>()

    abstract fun getLayoutId(): Int
    abstract fun initView()
    override fun onNoShakeClick(v: View) {
    }

    abstract fun loadData()

    var delayLoadData = false


    override fun attachBaseContext(newBase: Context?) {
        if (Foundation.attachBaseContext == null) {
            super.attachBaseContext(newBase)
        } else {
            super.attachBaseContext(Foundation.attachBaseContext?.attachBaseContext(newBase))
        }
    }

    /**
     * savedInstanceState 不为空时初始页面有bug，所有这里保存起来，让子类判断
     */
    var savedInstanceState: Bundle? = null

    var IslistenerNet = false

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState

        ImmersionBar.with(this).statusBarDarkFont(true).init();
//        StatusBarUtils.setStatusBarColor(this, Color.BLACK)
        if (getLayoutId() != 0) {
            setContentView(getLayoutId())
        }
        initView()

        if (!delayLoadData) {
            loadData()
        }
        if (IslistenerNet) {
            initNetListener()
        }
    }

    private fun initNetListener() {
        var tag = true
        if (!LibAppUtils.isNetworkConnected()) {
            showNullNetView()
        }

        var netReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                if (tag == true) {
                    tag = false
                    return
                }
                if (LibAppUtils.isNetworkConnected()) {
                    showHasNetView()
                    loadData()
                } else {
                    showNullNetView()
                }
            }
        }

        var intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(netReceiver, intentFilter)
    }

    open fun showHasNetView() {

    }

    open fun showNullNetView() {

    }

    override fun onDestroy() {
        super.onDestroy()
        SWLog.e("onDestroy-> ${localClassName}")

        onActivityResultListeners.clear()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        onActivityResultListeners.get(requestCode)?.invoke(resultCode, data)
    }


    override fun onBackPressed() {
        for (fragment in supportFragmentManager.fragments) {
            if (fragment is BaseFragment) {
                fragment.onBackPressed()
            }
        }
        super.onBackPressed()
    }

}