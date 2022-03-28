package com.fanrong.frwallet.view

import android.content.Intent
import android.net.Uri
import android.text.method.ScrollingMovementMethod
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.wallet.eth.eth.QueryAppVersionResp
import kotlinx.android.synthetic.main.dialog_app_version_detail.*
import xc.common.tool.utils.SPUtils
import xc.common.viewlib.view.customview.FullScreenDialog


class appVersionDetailDialog(
    var activity: AppCompatActivity,versionData: QueryAppVersionResp,_appVersionCode:Int
) : FullScreenDialog(activity)  {

    override var contentGravity: Int? = Gravity.CENTER
    var version = versionData
    var appVersionCode = _appVersionCode
    override fun getContentView(): Int {
        return R.layout.dialog_app_version_detail
    }

    override fun initView() {
        tv_updatecontent.setMovementMethod(ScrollingMovementMethod.getInstance())
        tv_updatecontent.setText(version.data?.content)

        if (version.data?.isForce == 1){
            tv_shzs.visibility = View.GONE
        }else{
            tv_shzs.visibility = View.VISIBLE
        }


        tv_update.setOnClickListener{
            val uri: Uri = Uri.parse(version.data?.url)
            val intent = Intent(Intent.ACTION_VIEW, uri)
            context.startActivity(intent)
            dismiss()
        }

        tv_shzs.setOnClickListener{
            SPUtils.saveStaticValue("appUpdateStatus"+appVersionCode,-1)
            dismiss()
        }
    }
}