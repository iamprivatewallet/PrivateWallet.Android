package com.fanrong.frwallet.ui.activity

import androidx.recyclerview.widget.LinearLayoutManager
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.ManageAssetsBalanceAdapter
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.CoinOperator
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.found.MvvmBaseActivity
import com.fanrong.frwallet.wallet.eth.viewmodel.WalletViewmodel
import kotlinx.android.synthetic.main.activity_home_asset_manage.*
import kotlinx.android.synthetic.main.activity_my_all_assets.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.showToast

class MyAllAssetsActivity : MvvmBaseActivity<WalletViewmodel.State, WalletViewmodel>() {


    val wallet: WalletDao by lazy {
        intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
    }

    val mAdapter: ManageAssetsBalanceAdapter by lazy {
        ManageAssetsBalanceAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                showToast("跳转Dapp浏览器")
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_my_all_assets
    }

    override fun initView() {
        super.initView()

        ma_title.apply {
            setTitleText("我的所有资产")
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }
        rcv_ma_wallet.apply {
            layoutManager = LinearLayoutManager(this@MyAllAssetsActivity)
            adapter = mAdapter
        }
    }

    override fun loadData() {
        val contracts = CoinOperator.queryContractAssetWithWallet(wallet)
        viewmodel.getBalance(contracts!!)
    }

    override fun getViewModel(): WalletViewmodel {
        return WalletViewmodel.getViewmodel(wallet)
    }

    override fun stateChange(state: WalletViewmodel.State) {
        state.balanceResult?.run {
            mAdapter.setNewData(resultData!!)
        }
    }

}