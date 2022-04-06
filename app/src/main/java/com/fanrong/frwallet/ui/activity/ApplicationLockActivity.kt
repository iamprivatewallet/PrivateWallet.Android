package com.fanrong.frwallet.ui.activity

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import kotlinx.android.synthetic.main.activity_applicationlock.*
import kotlinx.android.synthetic.main.activity_lockapp.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity

class ApplicationLockActivity: BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_applicationlock
    }

    override fun initView() {
        wim_title.apply {
            setTitleText(getString(R.string.yys))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        ll_mm.setOnClickListener{
            extStartActivity(SetAppLockPasswordActivity::class.java)
        }
    }

    override fun loadData() {

    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}