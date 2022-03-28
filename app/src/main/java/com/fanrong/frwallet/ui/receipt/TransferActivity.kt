package com.fanrong.frwallet.ui.receipt

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import com.codersun.fingerprintcompat.AonFingerChangeCallback
import com.codersun.fingerprintcompat.FingerManager
import com.codersun.fingerprintcompat.SimpleFingerCheckCallback
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.TransferFinishEvent
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.dapp.TransferInfoDialog
import com.fanrong.frwallet.found.MvvmBaseActivity
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.ui.address.AddressListActivity
import com.fanrong.frwallet.ui.receipt.viewmdel.TransferViewmodel
import com.fanrong.frwallet.ui.walletmanager.FingerSetttingActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.add_address_activity.*
import kotlinx.android.synthetic.main.finger_setting_activity.*
import kotlinx.android.synthetic.main.import_wallet_words_activity.*
import kotlinx.android.synthetic.main.transfer_activity.*
import kotlinx.android.synthetic.main.transfer_activity.et_addr
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog
import java.math.BigDecimal


class TransferActivity : MvvmBaseActivity<TransferViewmodel.State, TransferViewmodel>() {

    var tokenInfo: CoinDao? = null
    var transferInfoDialog: TransferInfoDialog? = null

    var gasInfo: GasInfoBean? = null
    var toAddr: String? = null
    var amount: String? = null

    override fun getLayoutId(): Int {
        return R.layout.transfer_activity
    }

    override fun initView() {
        super.initView()
        var result = intent?.getStringExtra(Constant.CODED_CONTENT) ?: ""
        if (result.checkNotEmpty()) {
            //扫码跳过来的
            tokenInfo = CoinOperator.queryChainCoin(WalletOperator.currentWallet!!)
            if (result.startsWith("0x")) {
                et_addr.setText(result)
            } else {
                val receiptqrcodeContractaddress = FrWalletUtil.getReceiptQrcode_contractAddress(result)
                if (receiptqrcodeContractaddress == tokenInfo!!.contract_addr ?: "") {
                    et_amount.setText(FrWalletUtil.getReceiptQrcode_value(result))
                    et_addr.setText(FrWalletUtil.getReceiptQrcode_ethereum(result))
                } else {
                    showToast("收款二维码与当前币种不匹配")
                }
            }
        } else {
            tokenInfo = intent.getSerializableExtra(FrConstants.TOKEN_INFO) as CoinDao
        }
        val name = CoinNameCheck.getNameByName(tokenInfo?.coin_name)
//        ac_title.apply {
//            extInitCommonBgAutoBack(this@TransferActivity, "${name} 转账")
//            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_scan) {
//                LibPremissionUtils.needCamera(this@TransferActivity, object : PermissonSuccess {
//                    override fun hasSuccess() {
//                        extStartActivityForResult(CaptureActivity::class.java, Bundle().apply {
//                            putString(Constant.JUMP_TYPE, "Transfer")
//                        }, 101) { i: Int, intent: Intent? ->
//                            if (i == Activity.RESULT_OK) {
//                                if (intent?.getStringExtra(Constant.CODED_CONTENT)!!.extCheckIsAddrNoToast()) {
//                                    var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
//
//                                    if (walletInfo.chainType!!.startsWith("CVN")){
//                                        var formatJson = NetTools.formatJson(intent?.getStringExtra(Constant.CODED_CONTENT)!!, CVNScanResult::class.java)
//                                        if (formatJson == null){
//                                            formatJson = CVNScanResult()
//                                            formatJson.address = intent?.getStringExtra(Constant.CODED_CONTENT)
//                                        }
//                                        et_addr.setText(formatJson?.address ?: "")
//                                    }else{
//                                        et_addr.setText(intent?.getStringExtra(Constant.CODED_CONTENT).extGetRightAddress(walletInfo.chainType) ?: "")
//                                    }
//
//                                    return@extStartActivityForResult
//                                }
//                                if (!(intent?.getStringExtra(Constant.CODED_CONTENT)!!.startsWith("ethereum:"))) {
//                                    showToast("暂不支持的二维码")
//                                    return@extStartActivityForResult
//                                }
//                                val receiptqrcodeContractaddress =
//                                    FrWalletUtil.getReceiptQrcode_contractAddress(intent?.getStringExtra(Constant.CODED_CONTENT) ?: "")
//                                if (receiptqrcodeContractaddress == tokenInfo!!.contract_addr ?: "") {
//                                    et_amount.setText(FrWalletUtil.getReceiptQrcode_value(intent?.getStringExtra(Constant.CODED_CONTENT) ?: ""))
//                                    et_addr.setText(FrWalletUtil.getReceiptQrcode_ethereum(intent?.getStringExtra(Constant.CODED_CONTENT) ?: ""))
//                                } else {
//                                    showToast("收款二维码与当前币种不匹配")
//                                }
//                            }
//                        }
//                    }
//                })
//            }
//        }


        tv_addr_book.setOnClickListener {
            extStartActivityForResult(AddressListActivity::class.java, Bundle().apply {
                putBoolean(FrConstants.PICK, true)
                putString(FrConstants.CHAIN_TYPE, tokenInfo?.chain_name)
            }, 101) { resultCode: Int, data: Intent? ->
                if (resultCode == RESULT_OK) {
                    et_addr.setText(data!!.getStringExtra(FrConstants.ADDR_INFO))
                }

            }
        }

        ll_gas.setOnClickListener {
            var walletInfo = WalletOperator.queryWallet(tokenInfo!!)
            // gas
            extStartActivityForResult(SetGasActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, walletInfo)
                putSerializable(FrConstants.GAS_INFO, gasInfo)
            }, 101) { resultCode: Int, data: Intent? ->
                if (resultCode == RESULT_OK) {
                    gasInfo = data?.getSerializableExtra(FrConstants.GAS_INFO) as GasInfoBean
                    var gasAmount = ETHChainUtil.compateGas1(gasInfo!!.gasLimit!!, gasInfo!!.gasPrice.extGwei2Wei())
                    tv_gas.setText(gasAmount+CoinNameCheck.getMainCoinName())
                    tv_gas_cny.setText("￥ " + gasAmount.extToFiatMoney())
                }
            }
        }

        ll_gas.extGoneOrVisible(
            tokenInfo!!.chain_name.equals("ETH") ||
                    tokenInfo!!.chain_name.equals("HECO") ||
                    tokenInfo!!.chain_name.equals("BSC")
        )

        tv_recharge.text = "矿工费加油站，快速充值" + tokenInfo?.chain_name
        et_addr.hint = tokenInfo?.chain_name + " 地址"

        tv_recharge.setOnClickListener {
            // 兑换
        }

        tv_high_mode.setOnClickListener {

        }

        btn_next.setOnClickListener {
            transfer()
        }


    }

    private fun transfer() {
        toAddr = et_addr.text.toString()
        amount = et_amount.text.toString()

        if (!toAddr.extCheckIsAddr(tokenInfo!!.chain_name!!)) {
            return
        }
        if (amount.checkIsEmpty()) {
            showToast("请输入转账金额")
            return
        }

        // cvn 不需要 gas ，else 需要
        if (!"CVN".equals(tokenInfo?.chain_name)) {
            if (gasInfo == null) {
                showToast("未获取到 gas 信息")
                return
            }
        }

        if (BigDecimal(et_amount.text.toString()) > BigDecimal(tv_balance.text.toString())) {
            showToast("转账金额不能大于余额")
            return
        }


        TransferRiskDialog(this).apply {
            onConfrim = object : FullScreenDialog.OnConfirmListener {
                override fun confirm(params: Any?) {
                    showTransferInfoDialog()
                    if (!transferInfoDialog!!.isShowing) {
                        transferInfoDialog!!.show()
                    }
                }
            }
            onCancel = object : FullScreenDialog.OnCancelListener {
                override fun cancel() {
                    extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                        putString(DappBrowserActivity.PARAMS_URL, FrConstants.COMMON_FRAUD)
                    })
                }
            }
        }.show()
    }

    fun showTransferInfoDialog() {
        transferInfoDialog = TransferInfoDialog(this).apply {
            this.tokenInfo = this@TransferActivity.tokenInfo
            this.isShowNode = false
            this.receiptAddr = this@TransferActivity.et_addr.text.toString()
            this.gasLimit = gasInfo?.gasLimit
            this.gasPrice = gasInfo?.gasPrice
            this.payAMount = this@TransferActivity.et_amount.text.toString()

            onConfrim = object : FullScreenDialog.OnConfirmListener {
                @RequiresApi(Build.VERSION_CODES.M)
                override fun confirm(params: Any?) {
                    if (walletInfo.isFinger == "1") {
                        FingerPrintUtil.openFingerPrint(this@TransferActivity,onSucceed = {
                            if ("CVN".equals(tokenInfo?.chain_name)){
                                viewmodel.transfer(tokenInfo!!, toAddr!!, amount!!,null)
                            }else{
                                viewmodel.transfer(tokenInfo!!, toAddr!!, amount!!, gasInfo)  //除了CVN用这个
                            }

                            if (transferInfoDialog!!.isShowing) {
                                transferInfoDialog!!.dismiss()
                            }
                        })
                        return
                    }
                    showPasswordDialog()
                    dismiss()
                }
            }
            onImgListener = object : TransferInfoDialog.OnImgListener {
                override fun imgClick() {
                    walletInfo = WalletOperator.currentWallet!!
                    if (walletInfo.isFinger == "1") {
                        showPasswordDialog()
                        dismiss()
                    } else {
                        extStartActivityForResult(FingerSetttingActivity::class.java, 101) { resultCode: Int, data: Intent? ->
                            if (resultCode == Activity.RESULT_OK) {
                                setRightImg()
                            }
                        }
                    }
                }
            }
        }
    }

    fun showPasswordDialog() {
        TransferPasswordDialog(this).apply {
            onConfrim = object : FullScreenDialog.OnConfirmListener {
                override fun confirm(params: Any?) {
                    if (params!!.toString().equals(WalletOperator.queryWallet(tokenInfo!!).password)) {
                        viewmodel.transfer(tokenInfo!!, toAddr!!, amount!!, gasInfo)
                        dismiss()
                    } else {
                        showToast("输入密码错误")
                    }
                }
            }
            onCancel = object : FullScreenDialog.OnCancelListener {
                override fun cancel() {
                    extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                        putString(DappBrowserActivity.PARAMS_URL, FrConstants.FORGET_PSW)
                    })
                }
            }
        }.show()
    }

    override fun loadData() {
        viewmodel.getGasPrice()
        viewmodel.getBalance(tokenInfo!!)
//        var model = viewmodel as CVNTransferViewmodel
//        model.queryOrderDetail("0x578f596a2b3fca8acee07332b5394156cd14bf8eb850e551659cefd6dbf15947",this::getRecord)
    }

//    fun getRecord(detail: TransactionRecordDetailResp?){
//
//    }

    override fun getViewModel(): TransferViewmodel {
        return TransferViewmodel.getViewmodel(tokenInfo!!)
    }

    override fun stateChange(state: TransferViewmodel.State) {
        extShowOrDismissDialog(state.isShowLoading)

        state.errorinfo?.run {
            showToast(msg)
        }


        state.gasInfoResult?.run {
            gasInfo = resultData!!.apply {
                this.gasPrice = gasPrice.extHexToTen()
                this.gasLimit = gasLimit.extHexToTen()
            }
//            var gasAMount = ETHChainUtil.compateGas1(gasInfo!!.gasPrice.toGwei(), gasInfo!!.gasLimit)
            var gasAMount = ETHChainUtil.compateGas1(gasInfo!!.gasLimit, gasInfo!!.gasPrice)
//            gasInfo = GasInfoBean(gasInfo!!.gasPrice, gasInfo!!.gasLimit)
            gasInfo = GasInfoBean(gasInfo!!.gasLimit, gasInfo!!.gasPrice.extWei2Gwei())
            tv_gas.setText(gasAMount+CoinNameCheck.getMainCoinName())
            tv_gas_cny.setText(gasAMount.extToFiatMoney())
        }

        state.transferResult?.run {
            EventBus.getDefault().post(TransferFinishEvent())
            showToast("交易已提交")
            viewmodel.getBalance(tokenInfo!!)
            extFinishWithAnim()
        }

        state.balanceResult?.run {
            tv_balance.setText(resultData!!.balance)
        }


    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun openFingerPrint() {
        when (FingerManager.checkSupport(this@TransferActivity)) {
            FingerManager.SupportResult.DEVICE_UNSUPPORTED -> {
                showToast("您的设备不支持指纹")
            }
            FingerManager.SupportResult.SUPPORT_WITHOUT_DATA -> {
                showToast("请在系统录入指纹后再验证")
            }
            FingerManager.SupportResult.SUPPORT -> FingerManager.build().setApplication(application)
                .setTitle("指纹验证")
                .setDes("请按下指纹")
                .setNegativeText("取消")
                .setFingerCheckCallback(object : SimpleFingerCheckCallback() {
                    override fun onSucceed() {
//                        showToast("验证成功")
//                        bscApi.unlockaccount(EthUnLockReq("0xb4a8f00266DdA25841790421BdDF243ca1E30157"))
                        viewmodel.transfer(tokenInfo!!, toAddr!!, amount!!, gasInfo!!)
                        if (transferInfoDialog!!.isShowing) {
                            transferInfoDialog!!.dismiss()
                        }
                    }

                    override fun onError(error: String) {
                        showToast("验证失败")
                    }

                    override fun onCancel() {
                        showToast("您取消了识别")
                    }
                })
                .setFingerChangeCallback(object : AonFingerChangeCallback() {
                    override fun onFingerDataChange() {
                        showToast("指纹数据发生了变化")
                    }
                })
                .create()
                .startListener(this)
        }
    }

}
