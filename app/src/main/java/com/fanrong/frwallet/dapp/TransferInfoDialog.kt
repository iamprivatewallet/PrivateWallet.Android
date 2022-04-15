package com.fanrong.frwallet.dapp

import android.content.Context
import android.view.Gravity
import android.view.View
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.ETHChainUtil
import com.fanrong.frwallet.tools.extGwei2Wei
import kotlinx.android.synthetic.main.dapp_dialog_payinfo.*
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog

class TransferInfoDialog(context: Context) : FullScreenDialog(context) {
    var walletInfo: WalletDao = WalletOperator.currentWallet!!
    override var contentGravity: Int? = Gravity.BOTTOM
//    var transferAction:(()->Unit)?=null


    var tokenInfo: CoinDao? = null
    var payAMount = ""
    var receiptAddr = ""
    var gasPrice: String? = null
    var gasLimit: String? = null


    var isShowNode = false
    var onImgListener: OnImgListener? = null

    override fun getContentView(): Int {
        return R.layout.dapp_dialog_payinfo
    }

    override fun initView() {
        setCanceledOnTouchOutside(true)

        iv_close.setOnClickListener {
            dismiss()
        }

        tv_ac_confirm.setOnClickListener {
            ll_password.visibility = View.VISIBLE
            ll_trans_info.visibility = View.GONE
        }

        tv_next.setOnClickListener {
            onConfrim?.confirm(null)
        }

        tv_amount.setText(payAMount + tokenInfo!!.coin_name)
        tv_to_addr.text = receiptAddr
        tv_from_addr.text = tokenInfo!!.sourceAddr



        if (gasLimit.checkNotEmpty() && gasLimit.checkNotEmpty()) {
            ll_gas_container.extGoneOrVisible(true)
            tv_gas.text = ETHChainUtil.compateGas1(gasLimit!!, gasPrice!!.extGwei2Wei())+ CoinNameCheck.getMainCoinName()
            tv_gas_des.text = "Gas Price（${gasPrice} GWEI）*Gas（${gasLimit}）"
        } else {
            ll_gas_container.extGoneOrVisible(false)
        }
    }

    interface OnImgListener {
        fun imgClick()
    }
}