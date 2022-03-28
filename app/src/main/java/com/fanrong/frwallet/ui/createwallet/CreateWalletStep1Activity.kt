package com.fanrong.frwallet.ui.createwallet

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.BackUpFinish
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.checkPassword
import com.fanrong.frwallet.tools.checkTwoPasswordIsSame
import com.fanrong.frwallet.tools.checkWalletName
import com.fanrong.frwallet.ui.SuccessDialog
import com.fanrong.frwallet.ui.activity.AddCoinsActivity
import com.fanrong.frwallet.ui.backup.BackUpHintActivity
import com.fanrong.frwallet.view.topDialogPasswordDes
import com.fanrong.frwallet.wallet.WalletHelper
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_create_wallet_step1.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extStartActivity
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.checkIsEmpty
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
            extInitCommonBgAutoBack(this@CreateWalletStep1Activity, "")
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_helpblack) {
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putString("url", FrConstants.CREATE_WALLET_HELPER)
                })
            }

        }
        btn_create.setOnClickListener {
            extShowOrDismissDialog(true)


            if ((!et_name.text.toString().checkWalletName()
                        || !et_password.text.toString().checkPassword()
                        || !et_password.text.toString().checkTwoPasswordIsSame(et_repassword.text.toString()))
            ) {
                extShowOrDismissDialog(false)
                return@setOnClickListener
            }


            createWallet()

        }

//        LibViewUtils.updateBtnStatus(btn_create, et_name, et_password, et_repassword)

        cb_show_hide.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
            et_repassword.extShowOrHide(isChecked, trans)
        }

        cb_show_hide.isChecked = false

        et_password.onFocusChangeListener = object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                tv_password_hint.extGoneOrVisible(hasFocus)

                if (hasFocus) {
                    passwordDes.show(this@CreateWalletStep1Activity)
                } else {
                    passwordDes.dismiss()
                }
            }

        }
        et_password.addTextChangedListener(object : TextWatcherAfter(){
            override fun afterTextChanged(s: Editable?) {
                super.afterTextChanged(s)
                if (s.toString().checkIsEmpty()) {
                    tv_password_hint.setText("不少于 8 位字符，建议混合大小写字母")
                } else {
                    tv_password_hint.setText("${s?.length}字符")
                }
            }
        })
    }


    /**
     * 创建钱包流程
     *      创建钱包-》成功对话框-》添加其他主链-》提示备份助记词
     */
    private fun createWallet() {
        //备份
        fun toBackUp() {

            extStartActivity(
                BackUpHintActivity::class.java, Bundle().apply {
                    putString(FrConstants.PRE_PAGE, FrConstants.PRE_PAGE_CREATE)
                    putSerializable(FrConstants.WALLET_INFO, WalletOperator.queryIdentityWallet())
                }
            )
        }

        //添加除了eth系的其他主链
        fun toAddCoins() {
            extStartActivityForResult(
                AddCoinsActivity::class.java, Bundle().apply {
                    putString(FrConstants.PRE_PAGE, FrConstants.PRE_PAGE_CREATE)
                }, 101
            ) { resultCode: Int, data: Intent? ->
                toBackUp()
            }
        }
        //成功
        fun showSuccessDialog() {
            SuccessDialog("创建成功", this).apply {
                setOnDismissListener {
                    toAddCoins()
                }
            }.show()
        }
        //1.WalletHelper.initMainWallet先添加eth系的钱包
        WalletHelper.initMainWallet(
            et_name.text.toString(),
            et_password.text.toString(),
            et_password_hide.text.toString(),
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


}