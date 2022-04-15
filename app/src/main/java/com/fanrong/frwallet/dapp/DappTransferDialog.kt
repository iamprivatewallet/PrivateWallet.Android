package com.fanrong.frwallet.dapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.dao.data.TransferDataBean
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.found.extShowOrHide
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.found.tryLaunch
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.ui.contract.custom.TokenViewModel
import com.fanrong.frwallet.ui.dialog.PasswordDialog
import com.fanrong.frwallet.ui.dialog.SetApproveAmountDialog
import com.fanrong.frwallet.ui.dialog.SetGasDialog
import com.fanrong.frwallet.ui.login.LoginActivity
import com.fanrong.frwallet.ui.receipt.SetGasActivity
import com.fanrong.frwallet.ui.walletmanager.FingerSetttingActivity
import com.fanrong.frwallet.view.showTopToast
import com.fanrong.frwallet.wallet.bsc.bscApi
import com.fanrong.frwallet.wallet.eth.eth.CallReq
import com.fanrong.frwallet.wallet.eth.eth.ethApi
import kotlinx.android.synthetic.main.activity_custom_tokens.*
import kotlinx.android.synthetic.main.dapp_dialog_transfer_info.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.AppManager
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.EditTextPasswordStyle
import xc.common.viewlib.view.customview.FullScreenDialog
import java.math.BigInteger

class DappTransferDialog(context: Context) : FullScreenDialog(context) {
    override var contentGravity: Int? = Gravity.BOTTOM
//    var transferAction:(()->Unit)?=null

    var tokenInfo: CoinDao? = null
    lateinit var walletInfo: WalletDao

    lateinit var dappUrl: String

    var payAMount = ""
    var receiptAddr = ""
    var payAddr = ""

    var newApproveAmount:String = ""

    //1交易   2授权   3签名
    var showType = 1
    var signMemo = ""
    var approveSpender = ""
    var approveTokenAddress = ""
    lateinit var cur_activity:Activity

    var approveTokenAmount = "0"
    var approveFillData = ""

    lateinit var gasInfo_old:GasInfoBean
    var gasPrice_half_old:Float = 0.0f

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

//        iv_finger.setOnClickListener {
//
//            walletInfo = WalletOperator.currentWallet!!
//            if (walletInfo.isFinger == "1") {
//                iv_close.extGoneOrVisible(false)
//                iv_back.extGoneOrVisible(true)
//                ll_password.visibility = View.VISIBLE
//                ll_trans_info.visibility = View.GONE
//
//            } else {
//                (ownerActivity as BaseActivity).extStartActivityForResult(FingerSetttingActivity::class.java, 101) { resultCode: Int, data: Intent? ->
//                    if (resultCode == Activity.RESULT_OK) {
//                        setRightImg()
//                    }
//                }
//            }
//
//        }

        gasInfo_old = GasInfoBean(gasLimit,gasPrice.extWei2Gwei())
        gasPrice_half_old = gasInfo_old!!.gasPrice.toFloat() / 2

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
//                iv_close.extGoneOrVisible(false)
//                iv_back.extGoneOrVisible(true)
//                ll_password.visibility = View.VISIBLE
//                ll_trans_info.visibility = View.GONE
                var _walletInfo: WalletDao = WalletOperator.currentWallet!!
                PasswordDialog(cur_activity,"1").apply {
                    this.walletInfo = _walletInfo
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            ConfirmClick()
                        }
                    }
                }.show()
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
                var _walletInfo: WalletDao = WalletOperator.currentWallet!!
                PasswordDialog(cur_activity,"1").apply {
                    this.walletInfo = _walletInfo
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            ConfirmClick()
                        }
                    }
                }.show()
            }
        }
        tv_next_approve.setOnClickListener{
            walletInfo = WalletOperator.currentWallet!!
            if (walletInfo.isFinger == "1") {
                FingerPrintUtil.openFingerPrint(ownerActivity!!,onSucceed = {
                    onConfrim?.confirm(approveFillData)
                    onCancel = null
                    dismiss()
                })
            } else {
                // 密码支付
//                iv_close.extGoneOrVisible(false)
//                iv_back.extGoneOrVisible(true)
//                ll_password.visibility = View.VISIBLE
//                ll_approve_info.visibility = View.GONE
//                ll_trans_info.visibility = View.GONE
                var _walletInfo: WalletDao = WalletOperator.currentWallet!!
                PasswordDialog(cur_activity,"1").apply {
                    this.walletInfo = _walletInfo
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            ConfirmClick()
                        }
                    }
                }.show()
            }
        }

        cb_eye.setOnCheckedChangeListener { buttonView, isChecked ->
            val trans = EditTextPasswordStyle()
            et_password.extShowOrHide(isChecked, trans)
        }
        cb_eye.isChecked = false

        tv_confirm.setOnClickListener {

        }



        ll_gas.setOnClickListener {
//            val baseActivity = ownerActivity as BaseActivity
//            baseActivity.extStartActivityForResult(
//                SetGasActivity::class.java,
//                Bundle().apply {
//                    putSerializable(FrConstants.WALLET_INFO, walletInfo)
//                    putSerializable(FrConstants.GAS_INFO,GasInfoBean(gasLimit,gasPrice.extWei2Gwei()))
//                },
//                101
//            ) { resultCode: Int, data: Intent? ->
//                if (resultCode == AppCompatActivity.RESULT_OK) {
//                    var gasInfo = data?.getSerializableExtra(FrConstants.GAS_INFO) as GasInfoBean
//                    gasPrice = gasInfo!!.gasPrice.extGwei2Wei()
//                    gasLimit = gasInfo!!.gasLimit
//
//                    tv_gas.text = ETHChainUtil.compateGas1(gasLimit, gasPrice) + (if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW")
//                    tv_gas_des.text = "Gas Price（${gasPrice.extWei2Gwei()} GWEI）*Gas（${gasLimit}）"
//                }
//            }
            showSetGasDialog()
        }
        ll_gas_approve.setOnClickListener{
//            val baseActivity = ownerActivity as BaseActivity
//            baseActivity.extStartActivityForResult(
//                SetGasActivity::class.java,
//                Bundle().apply {
//                    putSerializable(FrConstants.WALLET_INFO, walletInfo)
//                    putSerializable(FrConstants.GAS_INFO,GasInfoBean(gasLimit,gasPrice.extWei2Gwei()))
//                },
//                101
//            ) { resultCode: Int, data: Intent? ->
//                if (resultCode == AppCompatActivity.RESULT_OK) {
//                    var gasInfo = data?.getSerializableExtra(FrConstants.GAS_INFO) as GasInfoBean
//                    gasPrice = gasInfo!!.gasPrice.extGwei2Wei()
//                    gasLimit = gasInfo!!.gasLimit
//
//                    tv_gas_approve.text = ETHChainUtil.compateGas1(gasLimit, gasPrice) + (if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW")
//                    tv_gas_des_approve.text = "Gas Price（${gasPrice.extWei2Gwei()} GWEI）*Gas（${gasLimit}）"
//                }
//            }
            showSetGasDialog()
        }



//        tv_host.setText(Uri.parse(dappUrl).host)
//        tv_full_url.setText(dappUrl)

        tv_amount.setText(payAMount + (if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW"))
//        tv_payinfo.text = "提交交易"//(if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW") + "转账"
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
            tv_title.setText(context.getString(R.string.qqsq))
        }else if(showType == 3){//签名
            ll_trans_info.visibility = View.GONE
            ll_approve_info.visibility = View.GONE
            ll_sign_info.visibility = View.VISIBLE
            tv_title.setText(context.getString(R.string.qqqm))
        }else{
            //交易或其他
            ll_trans_info.visibility = View.VISIBLE
            ll_approve_info.visibility = View.GONE
            ll_sign_info.visibility = View.GONE
            tv_title.setText(context.getString(R.string.qrxx))
        }

        //签名
//        tv_host_sign.setText(Uri.parse(dappUrl).host)
//        tv_full_url_sign.setText(dappUrl)
        tv_from_addr_sign.setText(payAddr)
        edit_memo.setText(signMemo)

        //授权  approveSpender
        tv_from_addr_approve.setText(payAddr)
        tv_sqdbhydz.setText(approveTokenAddress)
        tv_approve_spender.setText(approveSpender)
        tv_Approveamount.setText(approveTokenAmount)
        if (approveTokenAddress!=null && approveTokenAddress != "" && approveTokenAmount != context.getString(R.string.wxz)){
            getTokenSymbol(approveTokenAmount,approveTokenAddress)
        }
        tv_Approveamount.setOnClickListener{
            SetApproveAmountDialog(cur_activity).apply {
                onConfrim = object : FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        //approveFillData   重新设置授权数量
                        newApproveAmount = params as String

                        this@DappTransferDialog.getTokenSymbol(newApproveAmount,approveTokenAddress)
                        this@DappTransferDialog.getTokenDecimal(approveTokenAddress)

                        dismiss()
                    }
                }

                onCancel = object : FullScreenDialog.OnCancelListener {
                    override fun cancel() {
                        dismiss()
                    }
                }
            }.show()
        }

//        setRightImg()
    }

//    fun setRightImg() {
//        walletInfo = WalletOperator.currentWallet!!
//        if (walletInfo.isFinger == "1") {
//            iv_finger.setImageResource(R.mipmap.src_lib_eui_icon_privatekey)
//        } else {
//            iv_finger.setImageResource(R.mipmap.src_lib_eui_icon_fingerprintgray)
//        }
//    }

    fun showSetGasDialog(){
        SetGasDialog(cur_activity).apply {
            this.gasInfo = gasInfo_old
            this.gasPrice_half = gasPrice_half_old

            onConfrim = object :
                FullScreenDialog.OnConfirmListener {
                override fun confirm(params: Any?) {
                    val gasInfoBean = params as GasInfoBean
                    if (gasInfoBean!=null){
                        SetGasDialogResult(gasInfoBean)
                    }
                    dismiss()
                }
            }
        }.show()
    }

    fun SetGasDialogResult(data:GasInfoBean){
        gasPrice = data!!.gasPrice.extGwei2Wei()
        gasLimit = data!!.gasLimit

        tv_gas_approve.text = ETHChainUtil.compateGas1(gasLimit, gasPrice) + (if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW")
        tv_gas_des_approve.text = "Gas Price（${gasPrice.extWei2Gwei()} GWEI）*Gas（${gasLimit}）"

        tv_gas.text = ETHChainUtil.compateGas1(gasLimit, gasPrice) + (if (tokenInfo != null) CoinNameCheck.getNameByName(tokenInfo!!.coin_name) else "UNKNOW")
        tv_gas_des.text = "Gas Price（${gasPrice.extWei2Gwei()} GWEI）*Gas（${gasLimit}）"
    }

    fun ConfirmClick(){
        if (showType == 2){
            val substring = approveFillData.substring(0, 74)
            Log.d("getTokenDicimal", "getTokenDicimal 外面: --->>>>>")
//            newApproveAmount    cur_transfer_coin_decimal
            val amount = newApproveAmount.extPow(cur_transfer_coin_decimal)
            var amountHex = amount.extTen2Hex().removePrefix("0x")
            val sb = StringBuilder()
            sb.append(amountHex)
            var len = sb.length
            while (len < 64){
                sb.insert(0,"0")
                len++
            }
            approveFillData = substring + sb.toString()
        }

        val gasInfoBean = GasInfoBean(gasLimit, gasPrice.extWei2Gwei())
        val transferDataBean = TransferDataBean(approveFillData, gasInfoBean)
        onConfrim?.confirm(transferDataBean)
        onCancel = null
        dismiss()
    }



    fun getTokenSymbol(amount:String,tokenAddress: String?) {
        GlobalScope.tryLaunch{
            var symbolResp = bscApi.susCall(CallReq(CallReq.ReqInfo().apply {
                to = tokenAddress
                data = "0x95d89b41"
            })).body()?.result

            var symbol: String? = null
            GlobalScope.tryLaunch(Dispatchers.Main) {
                Web3JsUtils.contractTokenDecode(symbolResp) {
                    symbol = CallJsCodeUtils.readStringJsValue(it) ?: ""
                    tv_Approveamount.setText(amount+symbol)
                }
            }
            while (symbol == null) {
                delay(200)
            }
            Log.d("TAG", "getTokenSymbol: --->>>>>"+symbol)
        }
    }
    var cur_transfer_coin_decimal = 18
    fun getTokenDecimal(tokenAddress: String?) {
        GlobalScope.tryLaunch{
            var decimalResp = bscApi.susCall(CallReq(CallReq.ReqInfo().apply {
                to = tokenAddress
                data = "0x313ce567"
            })).body()?.result

            while (decimalResp == null) {
                delay(500)
            }
            Log.d("getTokenDicimal", "getTokenDicimal: --->>>>>"+decimalResp)
            cur_transfer_coin_decimal = decimalResp.toInt()
        }
    }
}