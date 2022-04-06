package com.fanrong.frwallet.ui.activity

import android.graphics.Color
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.inteface.OnSelectWalletDialogItemClick
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.PswLoginSelectWalletDialog
import kotlinx.android.synthetic.main.activity_use_wallet_psw_login.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.AppManager
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.view.EditTextPasswordStyle

class UseWalletPswLoginActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_use_wallet_psw_login
    }
    lateinit var selectWallet: WalletDao
    override fun initView() {
        uwp_title.apply {
            extInitCommonBgAutoBack(this@UseWalletPswLoginActivity, "")
            setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
        selectWallet= WalletOperator.queryAllWallet()[0]
        tv_wallet_name.text=selectWallet.walletName ?: selectWallet.chainType
        tv_select.setOnClickListener {
            PswLoginSelectWalletDialog(this,  OnSelectWalletDialogItemClick{
                selectWallet=it
                tv_wallet_name.text=selectWallet.walletName ?: selectWallet.chainType
            }).apply {
                selectWallet=this@UseWalletPswLoginActivity.selectWallet
            }.show()
        }
        cb_show_hide.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
        }
        cb_show_hide.isChecked=false

        tv_confirm.setOnClickListener {
            if (et_password.text.toString().checkIsEmpty()){
                showToast("请输入密码")
                return@setOnClickListener
            }
            if (et_password.text.toString()==selectWallet.password){
                AppManager.getAppManager().finishActivity(FingerPrintVerifyActivity::class.java)
                extFinishWithAnim()
                extStartActivity(MainActivity::class.java)
            }else{
                showToast("密码错误")
            }
        }
    }

    override fun loadData() {

    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }

}