package com.fanrong.frwallet.ui.activity

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.ui.dialog.ChangeIdentifyNameDialog
import kotlinx.android.synthetic.main.activity_identity_info_setting.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.viewlib.view.customview.FullScreenDialog

class IdentityInfoSettingActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_identity_info_setting
    }

    override fun initView() {
        idi_title.apply {
            setTitleText("身份信息")
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        ll_idi_name.setOnClickListener {
            ChangeIdentifyNameDialog(this).apply {
                onConfrim = object : FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        WalletOperator.updateIdiWalletName(params.toString())
                        this@IdentityInfoSettingActivity.tv_name.setText(params.toString())
                        EventBus.getDefault().post(WalletInfoChangeEvent())
                    }
                }
            }.show()

        }

        tv_name.setText(WalletOperator.queryIdentityWallet().identityName)

    }

    override fun onResume() {
        super.onResume()
    }
    override fun loadData() {
    }

}