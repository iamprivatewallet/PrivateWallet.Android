package com.fanrong.frwallet.ui.dapp.setting

import android.graphics.Color
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import kotlinx.android.synthetic.main.dapp_setting_activity.*
import xc.common.kotlinext.showToast
import xc.common.tool.utils.FileOperatorUtils


class DappSettingActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.dapp_setting_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@DappSettingActivity,"DApp 设置")
            setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        tv_aprove.setOnClickListener {  }

        tv_clear_cache.setOnClickListener {
            FileOperatorUtils.delete(cacheDir)
            showToast("清除成功")
        }
    }

    override fun loadData() {
    }
}