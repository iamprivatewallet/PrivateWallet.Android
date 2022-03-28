package com.fanrong.frwallet.ui.receipt

import android.graphics.Color
import android.os.Bundle
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.FrWalletUtil
import com.fanrong.frwallet.ui.activity.IdentityWalletManageActivity
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

    val tokenInfo: CoinDao by lazy {
        intent.getSerializableExtra(FrConstants.TOKEN_INFO) as CoinDao
    }

    override fun getLayoutId(): Int {
        return R.layout.receipt_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@ReceiptActivity, "收款")
            setBackIcon(R.mipmap.src_lib_eui_icon_backwhite)
            setTitleTextColor(Color.parseColor("#FFFFFF"))
//            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_helpwhite) {
//                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
//                    putString(DappBrowserActivity.PARAMS_URL, FrConstants.RECEIPT_HELPER)
//                })
//            }
        }

        iv_set_amount.setOnClickListener {

            fun showInputDialog() {
                InputAmountDialog(this).apply {
                    onConfrim = object : FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            resetAmount(params)
                        }
                    }
                }.show()
            }


            EditReceiptAmountDialog(this).apply {
                onConfrim = object : FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        showInputDialog()
                    }
                }
            }.show()
        }

        ll_copy.setOnClickListener {
            LibAppUtils.copyText(tokenInfo.sourceAddr!!)
            showToast("复制成功")
        }

        ll_share.setOnClickListener {
            LibPremissionUtils.needStore(this, object : PermissonSuccess {
                override fun hasSuccess() {
                    ShareReceiptQrDialog(this@ReceiptActivity).apply {
                        bitmap = LibAppUtils.inviteViewToBitmap(this@ReceiptActivity.ll_share_content)
                    }.show()
                }
            })
        }

//        iv_qrcode.setImageResource()
        tv_amount.text = "扫描二维码，转入 " + CoinNameCheck.getNameByName(tokenInfo.coin_name)
        tv_addr.text = tokenInfo.sourceAddr


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
        this@ReceiptActivity.tv_amount.text = "扫描二维码，转入 " + params + " " + tokenInfo.coin_name
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

    }
}