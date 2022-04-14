package com.fanrong.frwallet.ui.createwallet

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.BackUpFinish
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.checkPassword
import com.fanrong.frwallet.tools.checkTwoPasswordIsSame
import com.fanrong.frwallet.tools.checkWalletName
import com.fanrong.frwallet.ui.SuccessDialog
import com.fanrong.frwallet.ui.activity.AddCoinsActivity
import com.fanrong.frwallet.ui.backup.BackUpHintActivity
import com.fanrong.frwallet.ui.backup.BackupWordsShowActivity
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.CommonButton
import com.fanrong.frwallet.view.SuperEditText2
import com.fanrong.frwallet.view.showTopToast
import com.fanrong.frwallet.view.topDialogPasswordDes
import com.fanrong.frwallet.wallet.WalletHelper
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_create_wallet_step1.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.LibViewUtils
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.EditTextPasswordStyle
import xianchao.com.topmsg.TopWindowMsg

class CreateWalletStep1Activity : BaseActivity() {

    val passwordDes:TopWindowMsg.TopMsgDialog by lazy {
        topDialogPasswordDes(this)
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_create_wallet_step1
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@CreateWalletStep1Activity, getString(R.string.cjsf))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_helpblack) {
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putString("url", FrConstants.CREATE_WALLET_HELPER)
                })
            }

        }
        cb_save.setClickListener(object : CommonButton.ClickListener {
            override fun clickListener() {
                extShowOrDismissDialog(true)

                if (!set_name.et_content.text.toString().checkWalletName(this@CreateWalletStep1Activity)
                    || !set_mm.et_content_1.text.toString().checkPassword(this@CreateWalletStep1Activity)
                    || !set_mm.et_content_2.text.toString().checkTwoPasswordIsSame(this@CreateWalletStep1Activity,set_mm.et_content_1.text.toString())){
                    extShowOrDismissDialog(false)

                    return
                }

                createWallet()
            }
        })

//        LibViewUtils.updateBtnStatus(btn_create, set_name.et_content, set_mm.et_content_1, set_mm.et_content_2)

        set_mm.et_content_1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateBtnState()
            }
        })
        set_mm.et_content_2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateBtnState()
            }
        })
        set_name.et_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateBtnState()
            }
        })

    }

    private fun updateBtnState() {
        if (set_mm.et_content_1.text.toString().checkNotEmpty() &&
            set_mm.et_content_2.text.toString().checkNotEmpty() &&
            set_name.et_content.text.toString().checkNotEmpty()
        ) {
            cb_save.setEnableState(true)
        } else {
            cb_save.setEnableState(false)
        }
    }


    /**
     * 创建钱包流程
     *      创建钱包-》成功对话框-》添加其他主链-》提示备份助记词
     */
    private fun createWallet() {
        //备份
        fun toBackUp() {

            extStartActivity(
                //BackUpHintActivity
                BackupWordsShowActivity::class.java, Bundle().apply {
                    putString(FrConstants.PRE_PAGE, FrConstants.PRE_PAGE_CREATE)
                    putSerializable(FrConstants.WALLET_INFO, WalletOperator.queryIdentityWallet())
                }
            )
        }

        //添加除了eth系的其他主链
        fun toAddCoins() {
//            extStartActivitylForResult(
//                AddCoinsActivity::class.java, Bundle().apply {
//                    putString(FrConstants.PRE_PAGE, FrConstants.PRE_PAGE_CREATE)
//                }, 101
//            ) { resultCode: Int, data: Intent? ->
//                toBackUp()
//            }
            toBackUp()
        }
        //成功
        fun showSuccessDialog() {
            SuccessDialog(getString(R.string.cjcg), this).apply {
                setOnDismissListener {
                    toAddCoins()
                }
            }.show()
        }
        //1.WalletHelper.initMainWallet先添加eth系的钱包
        WalletHelper.initMainWallet(
            set_name.et_content.text.toString(),
            set_mm.et_content_1.text.toString(),
            "",
            callback = { success: Boolean ->
                extShowOrDismissDialog(false)
                if (success) {
                    showSuccessDialog()
                }
            })

    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: BackUpFinish) {
        extStartActivity(MainActivity::class.java)
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }

}