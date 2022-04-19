package com.fanrong.frwallet.ui.contract.custom

import android.text.Editable
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.CoinOperator
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.extCheckIsContractAddr
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_custom_tokens.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.showToast
import xc.common.tool.CommonTool
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.LibViewUtils
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog

class CustomTokensActivity : BaseActivity() {
    val walletInfo: WalletDao by lazy {
        intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
    }

    val viewmodel: TokenViewModel by lazy {
        TokenViewModel()
    }

    var inputListener = Runnable {
        if (et_token_addr.text.toString().extCheckIsContractAddr(this,getString(R.string.hydzgsbzq))) {
            viewmodel.getTokenInfo(walletInfo, et_token_addr.text.toString())
        } else {
            showTopToast(this,getString(R.string.hydzgsbzq),false)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_custom_tokens
    }

    override fun initView() {
        ct_title.apply {
            setTitleText(getString(R.string.zdydb))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setRightTextClickListener(getString(R.string.save)) {

                if (!et_token_addr.text.toString().extCheckIsContractAddr(this@CustomTokensActivity,getString(R.string.hydzgsbzq))) {
                    return@setRightTextClickListener
                }

                if (et_decimal.text.toString().checkIsEmpty()) {
                    showTopToast(this@CustomTokensActivity,getString(R.string.dicimal_noempty),false)
                    return@setRightTextClickListener
                }

                if (et_symbol.text.toString().checkIsEmpty()) {
                    showTopToast(this@CustomTokensActivity,getString(R.string.symbol_noempty),false)
                    return@setRightTextClickListener
                }

                var coins = CoinOperator.queryContractAssetWithWallet(walletInfo)
                if (coins.checkNotEmpty()) {
                    for (coin in coins!!) {
                        if (coin.contract_addr.equals(et_token_addr.text.toString())) {
                            showTopToast(this@CustomTokensActivity,getString(R.string.current_wallet_is_save_coin),false)
                            return@setRightTextClickListener
                        }
                    }
                }

                CoinOperator.addContractAsset(walletInfo, CoinDao(et_symbol.text.toString()).apply {
                    this.contract_addr = et_token_addr.text.toString()
                    this.coin_decimals = et_decimal.text.toString()
                    this.coin_name = et_symbol.text.toString()
                })
                EventBus.getDefault().post(CurrentWalletChange())
                extFinishWithAnim()
            }
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        et_token_addr.addTextChangedListener(object : TextWatcherAfter() {
            override fun tryAfterTextChanged(s: Editable?) {
                super.tryAfterTextChanged(s)
                et_decimal.setText("")
                et_symbol.setText("")

                CommonTool.mainHandler.removeCallbacks(inputListener)
                CommonTool.mainHandler.postDelayed(inputListener, 1500)
            }
        })


        LibViewUtils.disableInput(et_decimal)
        LibViewUtils.disableInput(et_symbol)

        viewmodel.observerDataChange(this, this::stateChange)
    }


    fun stateChange(state: TokenViewModel.State) {
        extShowOrDismissDialog(state.isShowLoading)

        state.errorinfo?.run {
            showToast(msg)
        }

        state.decimalResult?.run {
            if (isExcuteSuccess) {
                et_decimal.setText(resultData)
            }
        }
        state.symbolResult?.run {
            if (isExcuteSuccess) {
                et_symbol.setText(resultData)
            }
        }

    }


    override fun loadData() {
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}