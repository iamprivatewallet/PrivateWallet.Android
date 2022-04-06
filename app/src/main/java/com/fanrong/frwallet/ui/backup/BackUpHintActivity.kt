package com.fanrong.frwallet.ui.backup

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.eventbus.BackUpFinish
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_back_up_hint.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.viewlib.utils.extGoneOrVisible

class BackUpHintActivity : BaseActivity() {

    val isFromCreate: Boolean by lazy {
        return@lazy FrConstants.PRE_PAGE_CREATE.equals(intent.getStringExtra(FrConstants.PRE_PAGE))
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_back_up_hint
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@BackUpHintActivity, "")
            if (isFromCreate) {
                setBackBtnHide()
            }
        }
        btn_next.setOnClickListener {
            DontCutScreenDialog(this).apply {
                iKnow = {
                    extStartActivity(BackupWordsShowActivity::class.java, intent.extras!!)
                }
            }.show()
        }
        tv_delay.setOnClickListener {
            extFinishWithAnim()
            extStartActivity(MainActivity::class.java)
        }


        var wallet = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
        tv_delay.extGoneOrVisible(isFromCreate)


        if (isFromCreate) {
            btn_next.setText("立即备份")
        } else {
            btn_next.setText("下一步")
        }
    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: BackUpFinish) {
        extFinishWithAnim()

    }

    override fun onBackPressed() {
        if (!isFromCreate) {
            super.onBackPressed()
        }
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }

}