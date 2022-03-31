package com.fanrong.frwallet.ui.receipt

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.SeekBar
import android.widget.TextSwitcher
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
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
import com.fanrong.frwallet.ui.activity.SelectCoinFromWalletActivity
import com.fanrong.frwallet.ui.address.AddressListActivity
import com.fanrong.frwallet.ui.receipt.viewmdel.TransferViewmodel
import com.fanrong.frwallet.ui.walletmanager.FingerSetttingActivity
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.set_gas_activity.*
import kotlinx.android.synthetic.main.transfer_activity.*
import kotlinx.android.synthetic.main.transfer_activity.tv_gas
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
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
    var gasTypeIsZdy = false

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
        tv_title.setText("${name} 转账")
        iv_back.setOnClickListener{
            extFinishWithAnim()
        }
        btn_right.setOnClickListener{
            LibPremissionUtils.needCamera(this@TransferActivity, object : PermissonSuccess {
                override fun hasSuccess() {
                    extStartActivityForResult(CaptureActivity::class.java, Bundle().apply {
                        putString(Constant.JUMP_TYPE, "Transfer")
                    }, 101) { i: Int, intent: Intent? ->
                        if (i == Activity.RESULT_OK) {
                            if (intent?.getStringExtra(Constant.CODED_CONTENT)!!.extCheckIsAddrNoToast()) {
                                var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包

                                if (walletInfo.chainType!!.startsWith("CVN")){
                                    var formatJson = NetTools.formatJson(intent?.getStringExtra(Constant.CODED_CONTENT)!!, CVNScanResult::class.java)
                                    if (formatJson == null){
                                        formatJson = CVNScanResult()
                                        formatJson.address = intent?.getStringExtra(Constant.CODED_CONTENT)
                                    }
                                    et_addr.setText(formatJson?.address ?: "")
                                }else{
                                    et_addr.setText(intent?.getStringExtra(Constant.CODED_CONTENT).extGetRightAddress(walletInfo.chainType) ?: "")
                                }

                                return@extStartActivityForResult
                            }
                            if (!(intent?.getStringExtra(Constant.CODED_CONTENT)!!.startsWith("ethereum:"))) {
                                showToast("暂不支持的二维码")
                                return@extStartActivityForResult
                            }
                            val receiptqrcodeContractaddress =
                                FrWalletUtil.getReceiptQrcode_contractAddress(intent?.getStringExtra(Constant.CODED_CONTENT) ?: "")
                            if (receiptqrcodeContractaddress == tokenInfo!!.contract_addr ?: "") {
                                et_amount.setText(FrWalletUtil.getReceiptQrcode_value(intent?.getStringExtra(Constant.CODED_CONTENT) ?: ""))
                                et_addr.setText(FrWalletUtil.getReceiptQrcode_ethereum(intent?.getStringExtra(Constant.CODED_CONTENT) ?: ""))
                            } else {
                                showToast("收款二维码与当前币种不匹配")
                            }
                        }
                    }
                }
            })
        }

        Glide.with(iv_coinicon).load(tokenInfo?.getTokenIcon()).into(iv_coinicon)
        coinname.setText(CoinNameCheck.getNameByName(tokenInfo?.coin_name))
        tv_chainname.setText(tokenInfo?.chain_name)

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

        ll_wallet.setOnClickListener{
            extStartActivityForResult(SelectCoinFromWalletActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.SELECT_COIN, tokenInfo)
            }, 101) { resultCode: Int, data: Intent? ->
                if (resultCode == RESULT_OK) {
                    tokenInfo = data?.getSerializableExtra(FrConstants.SELECT_COIN) as CoinDao

                    Glide.with(iv_coinicon).load(tokenInfo?.getTokenIcon()).into(iv_coinicon)
                    coinname.setText(CoinNameCheck.getNameByName(tokenInfo?.coin_name))
                    tv_chainname.setText(tokenInfo?.chain_name)
                    viewmodel.getBalance(tokenInfo!!)

                }
            }
        }

//        ll_gas.setOnClickListener {
//            var walletInfo = WalletOperator.queryWallet(tokenInfo!!)
//            // gas
//            extStartActivityForResult(SetGasActivity::class.java, Bundle().apply {
//                putSerializable(FrConstants.WALLET_INFO, walletInfo)
//                putSerializable(FrConstants.GAS_INFO, gasInfo)
//            }, 101) { resultCode: Int, data: Intent? ->
//                if (resultCode == RESULT_OK) {
//                    gasInfo = data?.getSerializableExtra(FrConstants.GAS_INFO) as GasInfoBean
//                    var gasAmount = ETHChainUtil.compateGas1(gasInfo!!.gasLimit!!, gasInfo!!.gasPrice.extGwei2Wei())
//                    tv_gas.setText(gasAmount+CoinNameCheck.getMainCoinName())
//                    tv_gas_cny.setText("￥ " + gasAmount.extToFiatMoney())
//                }
//            }
//        }
//
//        ll_gas.extGoneOrVisible(
//            tokenInfo!!.chain_name.equals("ETH") ||
//                    tokenInfo!!.chain_name.equals("HECO") ||
//                    tokenInfo!!.chain_name.equals("BSC")
//        )
//
//        tv_recharge.text = "矿工费加油站，快速充值" + tokenInfo?.chain_name
//        et_addr.hint = tokenInfo?.chain_name + " 地址"
//
//        tv_recharge.setOnClickListener {
//            // 兑换
//        }
//
//        tv_high_mode.setOnClickListener {
//
//        }
        ll_zdy.visibility = View.GONE
        tv_zdy.setOnClickListener{
            if (!gasTypeIsZdy){
                gasTypeIsZdy = true
                ll_zdy.visibility = View.VISIBLE
            }
            else{
                gasTypeIsZdy = false
                ll_zdy.visibility = View.GONE
            }
        }

        et_gasPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                gasInfo!!.gasPrice = s.toString()
            }
        })
        et_gasLimit.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                gasInfo!!.gasLimit = s.toString()
            }
        })

        var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
        tv_fsdd.setText(getString(R.string.fsdd)+walletInfo.address.extFormatAddr())

        tv_all.setOnClickListener{
            et_amount.setText(balance)
        }

        btn_next.setOnClickListener {
            if (gasTypeIsZdy){
                if (et_gasPrice.text.toString().checkIsEmpty()) {
                    showToast(getString(R.string.please_input_gasprice))
                    return@setOnClickListener
                }
                if (et_gasLimit.text.toString().checkIsEmpty()) {
                    showToast(getString(R.string.please_input_gaslimit))
                    return@setOnClickListener
                }
                if (et_gasLimit.text.toString().toInt() < gasInfo!!.gasLimit.toInt()) {
                    showToast(getString(R.string.gaslow))
                    return@setOnClickListener
                }
            }
            transfer()
        }

        tv_speed1.setOnClickListener{
            seekbar.setSeekBarProgress(0)
            setSeekBarProgressType(0.0)
        }
        tv_speed2.setOnClickListener{
            seekbar.setSeekBarProgress(25)
            setSeekBarProgressType(1.0)
        }
        tv_speed3.setOnClickListener{
            seekbar.setSeekBarProgress(50)
            setSeekBarProgressType(2.0)
        }
        tv_speed4.setOnClickListener{
            seekbar.setSeekBarProgress(75)
            setSeekBarProgressType(3.0)
        }
        seekbar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val d = BigDecimal(progress.toString()).divide(BigDecimal("25")).toDouble()
                setSeekBarProgressType(d)

                val calc_gasPrice = (gasPrice_half + ((progress / 25.0) * gasPrice_half)).toString()
                gasInfo!!.gasPrice = calc_gasPrice

                var gasAMount = ETHChainUtil.compateGas1(gasInfo!!.gasLimit, gasInfo!!.gasPrice.extGwei2Wei())
                tv_gas.setText(gasAMount+CoinNameCheck.getMainCoinName())
                tv_gas_cny.setText("≈$"+gasAMount.extToFiatMoney())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

        })
    }

    private fun setSeekBarProgressType(cur_addsuType:Double){
        if (cur_addsuType < 1){
            //缓慢
            tv_speed1.setBackgroundResource(R.drawable.bg_select)
            tv_speed2.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed3.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed4.setBackgroundResource(R.drawable.bg_common_input_bg)
        }else if (cur_addsuType < 2){
            //正常
            tv_speed1.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed2.setBackgroundResource(R.drawable.bg_select)
            tv_speed3.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed4.setBackgroundResource(R.drawable.bg_common_input_bg)
        }else if (cur_addsuType < 3){
            //快速
            tv_speed1.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed2.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed3.setBackgroundResource(R.drawable.bg_select)
            tv_speed4.setBackgroundResource(R.drawable.bg_common_input_bg)
        }else{
            //很快
            tv_speed1.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed2.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed3.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed4.setBackgroundResource(R.drawable.bg_select)
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

        if (BigDecimal(et_amount.text.toString()) > BigDecimal(balance)) {
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
    var gasPrice_half:Float = 0f
    var balance:String? = ""
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
            gasPrice_half = gasInfo!!.gasPrice.toFloat() / 2
            seekbar.setPrice(gasPrice_half.toString(),gasInfo!!.gasLimit)
            seekbar.invalidate()
        }

        state.transferResult?.run {
            EventBus.getDefault().post(TransferFinishEvent())
            showToast("交易已提交")
            viewmodel.getBalance(tokenInfo!!)
            extFinishWithAnim()
        }

        state.balanceResult?.run {
            val name = CoinNameCheck.getNameByName(tokenInfo?.coin_name)
            tv_balance.setText(resultData!!.balance+name)
            balance = resultData!!.balance
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
