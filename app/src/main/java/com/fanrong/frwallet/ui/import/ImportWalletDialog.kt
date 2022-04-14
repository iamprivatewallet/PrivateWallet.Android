package com.fanrong.frwallet.ui.import

import android.content.Context
import android.view.Gravity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.ui.createwallet.CreateWalletStep1Activity
import kotlinx.android.synthetic.main.import_wallet_dialog.*
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.BundleUtils
import xc.common.viewlib.view.customview.FullScreenDialog

class ImportWalletDialog(val walletType: String, context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM

    override fun getContentView(): Int {
        return R.layout.import_wallet_dialog
    }

    override fun initView() {
        tv_title.setText(walletType)
        iv_close.setOnClickListener {
            dismiss()
        }

        ll_create.setOnClickListener {
            dismiss()
            ownerActivity?.extStartActivity(CreateWalletStep1Activity::class.java, BundleUtils.createWith(FrConstants.WALLET_TYPE, walletType))

        }

        ll_words.setOnClickListener {
            dismiss()
            ownerActivity?.extStartActivity(ImportWalletWordsActivity::class.java, BundleUtils.createWith(FrConstants.WALLET_TYPE, walletType))

        }

        ll_private_key.setOnClickListener {
            dismiss()
            ownerActivity?.extStartActivity(ImportWalletPrivateKeyActivity::class.java, BundleUtils.createWith(FrConstants.WALLET_TYPE, walletType))

        }

        ll_keystore.setOnClickListener {
            dismiss()
            ownerActivity?.extStartActivity(ImportWalletKeyStoreActivity::class.java, BundleUtils.createWith(FrConstants.WALLET_TYPE, walletType))

        }

    }
}