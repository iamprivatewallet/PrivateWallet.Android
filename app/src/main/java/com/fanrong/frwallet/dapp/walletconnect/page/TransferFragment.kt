package com.fanrong.frwallet.dapp.walletconnect.page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.dao.database.CoinOperator
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectUtil
import com.fanrong.frwallet.found.MvvmBaseFragment
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.ui.receipt.SetGasActivity
import com.fanrong.frwallet.ui.receipt.viewmdel.TransferViewmodel
import kotlinx.android.synthetic.main.wc_fragment_transfer.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.showToastAtMainThread
import xc.common.viewlib.extension.extShowOrDismissDialog

class TransferFragment : MvvmBaseFragment<TransferViewmodel.State, TransferViewmodel>() {

    val transferViewmodel: TransferViewmodel by lazy {
        TransferViewmodel.getViewmodel(WalletOperator.currentWallet!!)
    }

    var gasInfo: GasInfoBean? = null

    override fun getLayoutId(): Int {
        return R.layout.wc_fragment_transfer
    }

    override fun initView() {

        tv_from_addr.setText(WalletConnectUtil.sendTransaction?.from)
        tv_to_addr.setText(WalletConnectUtil.sendTransaction?.to)
        tv_balance.setText(WalletConnectUtil.sendTransaction?.value.extHexToTen() + WalletOperator.currentWallet!!.chainType)


        btn_confirm.setOnClickListener {
            transferViewmodel.transfer(
                CoinOperator.queryChainCoin(WalletConnectUtil.connectWallet!!),
                tv_to_addr.text.toString(),
                WalletConnectUtil.sendTransaction?.value.extHexToTen(),
                gasInfo
            )

        }

        btn_cancel.setOnClickListener {
            WalletConnectUtil.session.rejectRequest(WalletConnectUtil.sendTransaction?.id ?: 0L, 0, "")
            extFinishWithAnim()
        }

        ll_gas_layout.setOnClickListener {
            extStartActivityForResult(SetGasActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, WalletOperator.currentWallet)
                putSerializable(FrConstants.GAS_INFO, gasInfo)
            }, 101) { resultCode: Int, data: Intent? ->
                if (resultCode == Activity.RESULT_OK) {
                    gasInfo = data?.getSerializableExtra(FrConstants.GAS_INFO) as GasInfoBean
                    var gasAmount = ETHChainUtil.compateGas1(gasInfo!!.gasLimit!!, gasInfo!!.gasPrice.extGwei2Wei())
                    tv_gas.setText(gasAmount)
                    tv_gas_cny.setText(gasAmount.extToFiatMoney())
                }
            }
        }
        transferViewmodel.observerDataChange(this, this::stateChange)
    }

    override fun loadData() {
        transferViewmodel.getGasPrice()
    }

    override fun onNoShakeClick(v: View) {
    }

    override fun getViewModel(): TransferViewmodel {
        return transferViewmodel
    }

    override fun stateChange(state: TransferViewmodel.State) {
        extShowOrDismissDialog(state.isShowLoading)

        state.gasInfoResult?.run {
            gasInfo = resultData!!.apply {
                this.gasPrice = gasPrice.extHexToTen()
                this.gasLimit = gasLimit.extHexToTen()
            }
            var gasAMount = ETHChainUtil.compateGas1(gasInfo!!.gasLimit, gasInfo!!.gasPrice)
            gasInfo = GasInfoBean(gasInfo!!.gasLimit, gasInfo!!.gasPrice.extWei2Gwei())
            tv_gas.setText(gasAMount)
            tv_gas_cny.setText(gasAMount.extToFiatMoney())
        }

        state.transferResult?.run {
            if (isExcuteSuccess) {
                showToastAtMainThread("转账成功")
                WalletConnectUtil.session.approveRequest(
                    WalletConnectUtil.sendTransaction?.id ?: 0L, state.transferInfoResult?.resultData ?: ""
                )
                extFinishWithAnim()
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isVisible) {
            WalletConnectUtil.session.rejectRequest(WalletConnectUtil.sendTransaction?.id ?: 0L, 0, "")
        }
    }
}