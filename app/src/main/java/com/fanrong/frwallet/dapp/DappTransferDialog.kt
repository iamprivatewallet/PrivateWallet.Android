package com.fanrong.frwallet.dapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.ui.receipt.SetGasActivity
import com.fanrong.frwallet.ui.walletmanager.FingerSetttingActivity
import kotlinx.android.synthetic.main.dapp_dialog_transfer_info.*
import xc.common.kotlinext.showToast
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.EditTextPasswordStyle
import xc.common.viewlib.view.customview.FullScreenDialog

class DappTransferDialog(context: Context) : FullScreenDialog(context) {

    override var contentGravity: Int? = Gravity.BOTTOM
//    var transferAction:(()->Unit)?=null

    var tokenInfo: CoinDao? = null
    lateinit var walletInfo: WalletDao

    lateinit var dappUrl: String

    var payAMount = ""
    var receiptAddr = ""
    var payAddr = ""

    //1交易   2授权   3签名
    var showType = 1
    var signMemo = ""
    var approveSpender = ""

    /**
     * 单位 wei 十进制
     */
    var gasPrice = ""

    /**
     * 单位 wei 十进制
     */
    var gasLimit = ""

    var payInfo = ""

    var isShowNode = false


    override fun getContentView(): Int {

        return R.layout.dapp_dialog_transfer_info
    }

    override fun initView() {
        setOnDismissListener {
            onCancel?.cancel()
        }

        iv_close.setOnClickListener {
            dismiss()
        }

        iv_finger.setOnClickListener {

            walletInfo = WalletOperator.currentWallet!!
            if (walletInfo.isFinger == "1") {
                iv_close.extGoneOrVisible(false)
                iv_back.extGoneOrVisible(true)
                ll_password.visibility = View.VISIBLE
                ll_trans_info.visibility = View.GONE

            } else {
                (ownerActivity as BaseActivity).extStartActivityForResult(FingerSetttingActivity::class.java, 101) { resultCode: Int, data: Intent? ->
                    if (resultCode == Activity.RESULT_OK) {
                        setRightImg()
                    }
                }
            }

        }

        iv_back.setOnClickListener {
            ll_password.extGoneOrVisible(false)
            iv_back.extGoneOrVisible(false)
            ll_trans_info.extGoneOrVisible(true)
            iv_close.extGoneOrVisible(true)
        }

        tv_next.setOnClickListener {

            walletInfo = WalletOperator.currentWallet!!
            if (walletInfo.isFinger == "1") {
                FingerPrintUtil.openFingerPrint(ownerActivity!!,onSucceed = {
                    onConfrim?.confirm(null)
                    onCancel = null
                    dismiss()
                })
            } else {
                // 密码支付
                iv_close.extGoneOrVisible(false)
                iv_back.extGoneOrVisible(true)
                ll_password.visibility = View.VISIBLE
                ll_trans_info.visibility = View.GONE
            }
        }
        tv_next_sign.setOnClickListener{
            walletInfo = WalletOperator.currentWallet!!
            if (walletInfo.isFinger == "1") {
                FingerPrintUtil.openFingerPrint(ownerActivity!!,onSucceed = {
                    onConfrim?.confirm(null)
                    onCancel = null
                    dismiss()
                })
            } else {
                // 密码支付
                iv_close.extGoneOrVisible(false)
                iv_back.extGoneOrVisible(true)
                ll_password.visibility = View.VISIBLE
                ll_trans_info.visibility = View.GONE
            }
        }
        tv_next_approve.setOnClickListener{
            walletInfo = WalletOperator.currentWallet!!
            if (walletInfo.isFinger == "1") {
                FingerPrintUtil.openFingerPrint(ownerActivity!!,onSucceed = {
                    onConfrim?.confirm(null)
                    onCancel = null
                    dismiss()
                })
            } else {
                // 密码支付
                iv_close.extGoneOrVisible(false)
                iv_back.extGoneOrVisible(true)
                ll_password.visibility = View.VISIBLE
                ll_trans_info.visibility = View.GONE
            }
        }

        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
        }
        cb_eye.isChecked = false

        tv_confirm.setOnClickListener {
            if (et_password.text.toString().equals(walletInfo!!.password)) {
                onConfrim?.confirm(null)
                onCancel = null
                dismiss()
            } else {
                showToast("密码错误")
            }
        }



        ll_gas.setOnClickListener {
            val baseActivity = ownerActivity as BaseActivity
            baseActivity.extStartActivityForResult(
                SetGasActivity::class.java,
                Bundle().apply {
                    putSerializable(FrConstants.WALLET_INFO, walletInfo)
                    putSerializable(FrConstants.GAS_INFO,GasInfoBean(gasLimit,gasPrice.extWei2Gwei()))
                },
                101
            ) { resultCode: Int, data: Intent? ->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    var gasInfo = data?.getSerializableExtra(FrConstants.GAS_INFO) as GasInfoBean
                    gasPrice = gasInfo!!.gasPrice.extGwei2Wei()
                    gasLimit = gasInfo!!.gasLimit

                    tv_gas.text = ETHChainUtil.compateGas1(gasLimit, gasPrice) + (if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW")
                    tv_gas_des.text = "Gas Price（${gasPrice.extWei2Gwei()} GWEI）*Gas（${gasLimit}）"
                }
            }
        }
        ll_gas_approve.setOnClickListener{
            val baseActivity = ownerActivity as BaseActivity
            baseActivity.extStartActivityForResult(
                SetGasActivity::class.java,
                Bundle().apply {
                    putSerializable(FrConstants.WALLET_INFO, walletInfo)
                    putSerializable(FrConstants.GAS_INFO,GasInfoBean(gasLimit,gasPrice.extWei2Gwei()))
                },
                101
            ) { resultCode: Int, data: Intent? ->
                if (resultCode == AppCompatActivity.RESULT_OK) {
                    var gasInfo = data?.getSerializableExtra(FrConstants.GAS_INFO) as GasInfoBean
                    gasPrice = gasInfo!!.gasPrice.extGwei2Wei()
                    gasLimit = gasInfo!!.gasLimit

                    tv_gas_approve.text = ETHChainUtil.compateGas1(gasLimit, gasPrice) + (if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW")
                    tv_gas_des_approve.text = "Gas Price（${gasPrice.extWei2Gwei()} GWEI）*Gas（${gasLimit}）"
                }
            }
        }



        tv_host.setText(Uri.parse(dappUrl).host)
        tv_full_url.setText(dappUrl)

        tv_amount.setText(payAMount + (if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW"))
        tv_payinfo.text = "提交交易"//(if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW") + "转账"
        tv_to_addr.text = receiptAddr
        tv_from_addr.text = payAddr

        if ("CVN".equals(walletInfo.chainType)) {
            ll_gas_layout.extGoneOrVisible(false)
        } else {
            ll_gas_layout.extGoneOrVisible(true)
            tv_gas.text = ETHChainUtil.compateGas1(gasLimit, gasPrice) + CoinNameCheck.getNameByName(tokenInfo!!.coin_name)
            tv_gas_des.text = "Gas Price（${gasPrice.extWei2Gwei()} GWEI）*Gas（${gasLimit}）"


            tv_gas_approve.text = ETHChainUtil.compateGas1(gasLimit, gasPrice) + CoinNameCheck.getNameByName(tokenInfo!!.coin_name)
            tv_gas_des_approve.text = "Gas Price（${gasPrice.extWei2Gwei()} GWEI）*Gas（${gasLimit}）"
        }


        if(showType == 2){//授权
            ll_trans_info.visibility = View.GONE
            ll_approve_info.visibility = View.VISIBLE
            ll_sign_info.visibility = View.GONE
            tv_title.setText("请求授权")
        }else if(showType == 3){//签名
            ll_trans_info.visibility = View.GONE
            ll_approve_info.visibility = View.GONE
            ll_sign_info.visibility = View.VISIBLE
            tv_title.setText("请求签名")
        }else{
            //交易或其他
            ll_trans_info.visibility = View.VISIBLE
            ll_approve_info.visibility = View.GONE
            ll_sign_info.visibility = View.GONE
            tv_title.setText("支付详情")
        }

        //签名
        tv_host_sign.setText(Uri.parse(dappUrl).host)
        tv_full_url_sign.setText(dappUrl)
        tv_from_addr_sign.setText(payAddr)
        edit_memo.setText(signMemo)

        //授权  approveSpender
        tv_host_approve.setText(Uri.parse(dappUrl).host)
        tv_from_addr_approve.setText(payAddr)
        tv_spender_addr_approve.setText(approveSpender)


        setRightImg()

    }



    fun setRightImg() {
        walletInfo = WalletOperator.currentWallet!!
        if (walletInfo.isFinger == "1") {
            iv_finger.setImageResource(R.mipmap.src_lib_eui_icon_privatekey)
        } else {
            iv_finger.setImageResource(R.mipmap.src_lib_eui_icon_fingerprintgray)
        }
    }

}