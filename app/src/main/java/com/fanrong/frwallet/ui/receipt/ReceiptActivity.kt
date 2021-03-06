package com.fanrong.frwallet.ui.receipt

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.basiclib.base.BaseActivity
import com.bumptech.glide.Glide
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.FrWalletUtil
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.ShareUtils
import com.fanrong.frwallet.ui.activity.IdentityWalletManageActivity
import com.fanrong.frwallet.ui.activity.SelectCoinFromWalletActivity
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.dialog.ReceiptBackupsHintDialog
import com.yzq.zxinglibrary.EncodingUtils
import kotlinx.android.synthetic.main.receipt_activity.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
import xc.common.viewlib.view.customview.FullScreenDialog


class ReceiptActivity : BaseActivity() {

    lateinit var tokenInfo:CoinDao
    override fun getLayoutId(): Int {
        return R.layout.receipt_activity
    }

    override fun initView() {
        tokenInfo = intent.getSerializableExtra(FrConstants.TOKEN_INFO) as CoinDao
        iv_back.setOnClickListener{
            extFinishWithAnim()
        }
//        iv_set_amount.setOnClickListener {
//
//            fun showInputDialog() {
//                InputAmountDialog(this).apply {
//                    onConfrim = object : FullScreenDialog.OnConfirmListener {
//                        override fun confirm(params: Any?) {
//                            resetAmount(params)
//                        }
//                    }
//                }.show()
//            }
//
//
//            EditReceiptAmountDialog(this).apply {
//                onConfrim = object : FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        showInputDialog()
//                    }
//                }
//            }.show()
//        }

        ll_copy.setOnClickListener {
            LibAppUtils.copyText(tokenInfo.sourceAddr!!)
            showToast("????????????")
        }

        ll_share.setOnClickListener {
            ll_share_layout.visibility = View.VISIBLE
            LibPremissionUtils.needStore(this, object : PermissonSuccess {
                override fun hasSuccess() {
                    //??????????????????
                    ll_share_content_invisible.setDrawingCacheEnabled(true)
                    ll_share_content_invisible.buildDrawingCache()
                    val bitmap: Bitmap = Bitmap.createBitmap(ll_share_content_invisible.getDrawingCache())
                    ShareUtils.shareImage(bitmap,"share_address",this@ReceiptActivity)

//                    ShareReceiptQrDialog(this@ReceiptActivity).apply {
////                        bitmap = LibAppUtils.inviteViewToBitmap(this@ReceiptActivity.ll_share_content)
//                        receiptAddress = tokenInfo?.sourceAddr
//                        coinName = CoinNameCheck.getNameByName(tokenInfo.coin_name)
//                    }.show()
                }
            })
        }
        tv_cancel.setOnClickListener{
            ll_share_layout.visibility = View.GONE
        }

        iv_downloadqrcode.setImageBitmap(EncodingUtils.createQRCodeWithoutWhite("https://privatewallet.tech/download", 200, 200, null))

//        iv_qrcode.setImageResource()
        tv_amount.text = "???????????????????????? " + CoinNameCheck.getNameByName(tokenInfo.coin_name)
        tv_addr.text = tokenInfo?.sourceAddr
        tv_receipt_tip.setText(getString(R.string.receipt_tip,tokenInfo.coin_name))
        Glide.with(iv_coinicon).load(tokenInfo.getTokenIcon()).into(iv_coinicon)
        coinname.setText(CoinNameCheck.getNameByName(tokenInfo.coin_name))
//        tv_chainname.setText(tokenInfo.chain_name)
        ll_wallet.setOnClickListener{
            extStartActivityForResult(SelectCoinFromWalletActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.SELECT_COIN, tokenInfo)
            }, 101) { resultCode: Int, data: Intent? ->
                if (resultCode == RESULT_OK) {
                    tokenInfo = data?.getSerializableExtra(FrConstants.SELECT_COIN) as CoinDao

                    Glide.with(iv_coinicon).load(tokenInfo.getTokenIcon()).into(iv_coinicon)
                    coinname.setText(CoinNameCheck.getNameByName(tokenInfo.coin_name))
//                    tv_chainname.setText(tokenInfo.chain_name)

                    tv_amount.text = "???????????????????????? " + CoinNameCheck.getNameByName(tokenInfo.coin_name)
                    tv_addr.text = tokenInfo?.sourceAddr
                    tv_receipt_tip.setText(getString(R.string.receipt_tip,CoinNameCheck.getNameByName(tokenInfo.coin_name)))

                }
            }
        }

        checkBackUp()

    }

    private fun checkBackUp() {
        var wallet = WalletOperator.queryWallet(tokenInfo)
        if (wallet.isBackUp == "0" && wallet.isMainWallet == "1") {
            ReceiptBackupsHintDialog(this!!).apply {

                onConfrim = object :
                    FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        extStartActivity(IdentityWalletManageActivity::class.java, Bundle().apply {
                            putSerializable(FrConstants.WALLET_INFO, wallet)
                        })
                    }
                }
                onCancel = object : FullScreenDialog.OnCancelListener {
                    override fun cancel() {
                        extFinishWithAnim()
                    }
                }
            }.show()
        }
    }

    private fun resetAmount(params: Any?) {
        this@ReceiptActivity.tv_amount.text = "???????????????????????? " + params + " " + tokenInfo.coin_name
        this@ReceiptActivity.tv_amount.tag = params
        loadData()
    }

    override fun loadData() {
        var qrcodeMsg: String? = null
        if (intent.getStringExtra(FrConstants.ADDR_INFO) == null || tv_amount.tag != null) {
            qrcodeMsg = FrWalletUtil.createReceiptQrcode(
                tokenInfo.sourceAddr!!, tokenInfo.contract_addr ?: "", tokenInfo.coin_decimals ?: "18", tv_amount.tag?.toString() ?: ""
            )
        } else {
            qrcodeMsg = intent.getStringExtra(FrConstants.ADDR_INFO)
        }

        val qrcodeImg = EncodingUtils.createQRCodeWithoutWhite(qrcodeMsg, 400, 400, null)
        iv_qrcode.setImageBitmap(qrcodeImg)
        iv_qrcode_invisible.setImageBitmap(qrcodeImg)
        tv_qbdz.setText(resources.getString(R.string.qbdz,CoinNameCheck.getNameByName(tokenInfo.coin_name)))
        tv_address.setText(tokenInfo?.sourceAddr)
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}