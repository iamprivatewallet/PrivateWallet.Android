package com.fanrong.frwallet.ui.activity

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.ui.import.ImportWalletPrivateKeyActivity
import com.fanrong.frwallet.ui.import.ImportWalletWordsActivity
import kotlinx.android.synthetic.main.activity_import_account.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.BundleUtils

class ImportAccountActivity  : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_import_account
    }

    override fun initView() {
        ac_title.apply {
            setTitleText(resources.getString(R.string.import_account))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        var walletType: WalletDao = WalletOperator.currentWallet!! //当前钱包

        ll_import_form_sy.setOnClickListener{
            extStartActivity(
                ImportWalletPrivateKeyActivity::class.java, BundleUtils.createWith(
                    FrConstants.WALLET_TYPE, walletType.chainType!!))
        }
        ll_import_form_zjc.setOnClickListener{
            extStartActivity(ImportWalletWordsActivity::class.java, BundleUtils.createWith(FrConstants.WALLET_TYPE, walletType.chainType!!))
        }
    }

    override fun loadData() {

    }
}