package com.fanrong.frwallet.ui.activity

import android.text.Editable
import android.text.TextWatcher
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.CommonButton
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.aactivity_modity_walletpassword.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.utils.checkNotEmpty

class ModityWalletPasswordActivity: BaseActivity() {
    lateinit var walletInfo: WalletDao
    override fun getLayoutId(): Int {
        return R.layout.aactivity_modity_walletpassword;
    }

    override fun initView() {
        walletInfo = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao

        wim_title.apply {
            setTitleText(getString(R.string.xgmm))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        cb_save.setClickListener(object : CommonButton.ClickListener {
            override fun clickListener() {
                if (!set_mm.et_content_1.text.toString().equals(set_mm.et_content_2.text.toString())) {
                    showTopToast(this@ModityWalletPasswordActivity,getString(R.string.lcmmbyz),false)
                    return
                }
                if (set_layout.et_content.text.toString().equals(walletInfo.password)){
                    walletInfo.password = set_mm.et_content_1.text.toString()
                    WalletOperator.update(walletInfo)
                    EventBus.getDefault().post(WalletInfoChangeEvent())
                    showTopToast(this@ModityWalletPasswordActivity,getString(R.string.xgcg),true)
                    extFinishWithAnim()
                }else{
                    showTopToast(this@ModityWalletPasswordActivity,getString(R.string.mmcw_tip),false)
                }
            }
        })

        set_layout.et_content.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                updateBtnState()
            }
        })
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
    }

    private fun updateBtnState() {
        if (set_layout.et_content.text.toString().checkNotEmpty() &&
            set_mm.et_content_1.text.toString().checkNotEmpty()   &&
            set_mm.et_content_2.text.toString().checkNotEmpty()
        ) {
            cb_save.setEnableState(true)
        } else {
            cb_save.setEnableState(false)
        }
    }

    override fun loadData() {

    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}