package com.fanrong.frwallet.ui.activity

import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.backup.BackupWordsShowActivity
import com.fanrong.frwallet.ui.backup.DontCutScreenDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_export_privatekey.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.utils.LibAppUtils

class ExportPrivateKeyActivity: BaseActivity() {
    lateinit var walletInfo: WalletDao
    override fun getLayoutId(): Int {
        return R.layout.activity_export_privatekey;
    }
    override fun initView() {
        walletInfo = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao

        wim_title.apply {
            setTitleText(getString(R.string.dcsy))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        tv_sy.setText(walletInfo.privateKey)

        tv_djck.setOnClickListener{
            tv_djck.visibility = View.GONE
            tv_sy.visibility = View.VISIBLE
        }
        tv_sy.setOnClickListener{
            LibAppUtils.copyText(walletInfo.privateKey!!)
            showTopToast(this,getString(R.string.copysuccess),true)
        }

        DontCutScreenDialog(this).apply {
            iKnow = {

            }
        }.show()
    }

    override fun loadData() {

    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}