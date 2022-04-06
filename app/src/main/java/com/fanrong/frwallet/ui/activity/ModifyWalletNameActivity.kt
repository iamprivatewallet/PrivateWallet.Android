package com.fanrong.frwallet.ui.activity

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.CommonInputDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.SuperEditText
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_modity_walletname.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

class ModifyWalletNameActivity: BaseActivity() {
    lateinit var walletInfo: WalletDao
    override fun getLayoutId(): Int {
        return R.layout.activity_modity_walletname
    }

    override fun initView() {
        walletInfo = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
        wim_title.apply {
            setTitleText(getString(R.string.xgqbm))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        tv_confirm.setOnClickListener{
            if (set_layout.et_content.text.toString().checkNotEmpty()){
                walletInfo.walletName = set_layout.et_content.text.toString()
                WalletOperator.update(walletInfo)
                EventBus.getDefault().post(WalletInfoChangeEvent())
                showTopToast(this,getString(R.string.xgcg),true)
                extFinishWithAnim()
            }else{
                showTopToast(this,getString(R.string.mcbnwk),false)
            }

        }
        set_layout.et_content.setHint(walletInfo.walletName ?: walletInfo.chainType.toString())

    }

    override fun loadData() {

    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}