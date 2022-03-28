package com.fanrong.frwallet.ui.backup

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import kotlinx.android.synthetic.main.activity_back_up_keystore_hint.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity

class BackUpKeyStoreHintActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_back_up_keystore_hint
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@BackUpKeyStoreHintActivity, "")
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
        }
        btn_next.setOnClickListener {
            DontCutScreenDialog(this).apply {
                iKnow = {
                    extStartActivity(BackupKeystoreActivity::class.java, intent.extras!!)
                    extFinishWithAnim()
                }
            }.show()
        }
    }

    override fun loadData() {
    }

}