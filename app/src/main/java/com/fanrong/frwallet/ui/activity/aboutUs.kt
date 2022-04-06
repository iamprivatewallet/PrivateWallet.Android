package com.fanrong.frwallet.ui.activity

import android.content.pm.PackageInfo
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.DappInfoDao
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.appVersionDetailDialog
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryAppVersionReq
import com.fanrong.frwallet.wallet.eth.eth.QueryAppVersionResp
import kotlinx.android.synthetic.main.activity_about_us.*
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity

class aboutUs : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_about_us
    }

    override fun initView() {
        idi_title.apply {
            setTitleText(context.getString(R.string.gywm))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }
        getAppInfo()

        //openWeb
        tv_website.setOnClickListener{
            openWeb(tv_website.text.toString())
        }
        tv_lt.setOnClickListener{
            openWeb(tv_lt.text.toString())
        }
        tv_twitter.setOnClickListener{
            openWeb(tv_twitter.text.toString())
        }

    }

    override fun loadData() {

        centerApi.queryAppVersion(QueryAppVersionReq("zh_CN")).netSchduler()
            .subscribeObj(object : NetCallBack<QueryAppVersionResp> {
                override fun onSuccess(t: QueryAppVersionResp) {
                    if (t.code == 1 && t.data != null){
                        if (appVersionCode<t.data?.code!!.toInt()){
                            view_needUpdate.visibility = View.VISIBLE
                            ll_version.setOnClickListener{
                                appVersionDetailDialog(this@aboutUs as AppCompatActivity,t,appVersionCode).show()
                            }
                        }
                    }
                }

                override fun onFailed(error: Throwable) {

                }

            })
    }
    var appVersionCode: Int = 0
    fun getAppInfo(){
        var pkName = packageName;

        val packageInfo: PackageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0)
        var appVersionName = packageInfo.versionName
        appVersionCode = packageInfo.versionCode

        tv_version.setText("v"+appVersionName)

    }

    fun openWeb(url:String){
        extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
            putSerializable(DappBrowserActivity.PARAMS_DAPP, DappInfoDao().apply {
                this.url = url
            })
        })
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}