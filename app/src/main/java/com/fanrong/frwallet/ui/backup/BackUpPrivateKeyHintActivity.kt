package com.fanrong.frwallet.ui.backup

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import kotlinx.android.synthetic.main.activity_back_up_private_key_hint.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity

class BackUpPrivateKeyHintActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_back_up_private_key_hint
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@BackUpPrivateKeyHintActivity, "")
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
        }
        btn_next.setOnClickListener {
            DontCutScreenDialog(this).apply {
                iKnow = {
                    extStartActivity(BackupPrivateKeyActivity::class.java, intent.extras!!)
                    extFinishWithAnim()
                }
            }.show()
        }
    }

    override fun loadData() {
    }

}