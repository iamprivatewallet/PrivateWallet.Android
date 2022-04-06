package com.fanrong.frwallet.ui.import

import android.text.Editable
import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.BackUpFinish
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.checkPassword
import com.fanrong.frwallet.tools.checkTwoPasswordIsSame
import com.fanrong.frwallet.tools.checkWalletName
import com.fanrong.frwallet.ui.backup.BackUpHintActivity
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.wallet.WalletHelper
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.create_wallet_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.AppManager
import xc.common.tool.utils.BundleUtils
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.EditTextPasswordStyle


class CreateWalletActivity : BaseActivity() {

    val walletType: String by lazy {
        intent.getStringExtra(FrConstants.WALLET_TYPE)
    }

    var wallet: WalletDao? = null

    override fun getLayoutId(): Int {
        return R.layout.create_wallet_activity
    }

    override fun initView() {

        ac_title.apply {
            extInitCommonBgAutoBack(this@CreateWalletActivity, "创建${walletType}钱包")
        }


//        LibViewUtils.updateBtnStatus(btn_create, et_name, et_password, et_repassword)

        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
            et_repassword.extShowOrHide(isChecked, trans)
        }
        cb_eye.isChecked = false


        et_password.onFocusChangeListener = object : View.OnFocusChangeListener{
            override fun onFocusChange(v: View?, hasFocus: Boolean) {
                tv_password_hint.extGoneOrVisible(hasFocus)
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

        btn_create.setOnClickListener {
            if (!et_name.text.toString().checkWalletName()
                || !et_password.text.toString().checkPassword()
                || !et_password.text.toString().checkTwoPasswordIsSame(et_repassword.text.toString())
            ) {

                return@setOnClickListener
            }

            WalletHelper.createWalletWithWordsNoSave(
                walletType, et_name.text.toString(), et_password.text.toString(), et_password_hide.text.toString()
            ) { result ->
                if (result.success) {
                    wallet = result.result as WalletDao
                    extStartActivity(
                        BackUpHintActivity::class.java,
                        BundleUtils.createWith(FrConstants.WALLET_INFO, wallet!!)
                    )
                } else {
                    showToast(result.error.toString())
                }
            }
        }
    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: BackUpFinish) {
        WalletOperator.insert(wallet!!)
//        wallet?.save()
        AppManager.getAppManager().finishOthersActivity(MainActivity::class.java)
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}