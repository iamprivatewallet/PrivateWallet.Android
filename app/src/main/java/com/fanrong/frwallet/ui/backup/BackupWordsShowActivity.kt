package com.fanrong.frwallet.ui.backup

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.basiclib.base.BaseActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.eventbus.BackUpFinish
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_backup_words_show.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity

class BackupWordsShowActivity : BaseActivity() {

    lateinit var wordAdapter: WordAdapter

    override fun getLayoutId(): Int {
        return R.layout.activity_backup_words_show
    }


    override fun initView() {

        val wallet = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao

        ac_title.apply {
            extInitCommonBgAutoBack(this@BackupWordsShowActivity, getString(R.string.bfqb))

            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_helpblack) {
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putString("url", FrConstants.HELP_BLACKUP)
                })
            }
        }
        wordAdapter = WordAdapter()
        recyclerview.apply {
            layoutManager = GridLayoutManager(this@BackupWordsShowActivity, 3)
            adapter = wordAdapter
        }
        wordAdapter.setNewData(wallet.mnemonic!!.split(" "))

        btn_confirm.setOnClickListener {
            extStartActivity(BackupWordsConfirmActivity::class.java, intent.extras!!)
        }

        DontCutScreenDialog(this).apply {
            iKnow = {

            }
        }.show()

    }


    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: BackUpFinish) {
        extFinishWithAnim()
    }


    class WordAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.backup_item_words) {
        override fun convert(helper: BaseViewHolder, item: String?) {
            helper.setText(R.id.tv_word, item)
//            helper.setText(R.id.tv_number, (helper.adapterPosition + 1).toString())
        }

    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}