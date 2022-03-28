package com.fanrong.frwallet.ui.contract

import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.CoinOperator
import com.fanrong.frwallet.dao.database.ConfigTokenOperator
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.found.GoodSnackbar
import com.fanrong.frwallet.ui.activity.HomeAssetManageActivity
import com.fanrong.frwallet.ui.activity.MyAllAssetsActivity
import com.fanrong.frwallet.ui.activity.SearchTokenActivity
import com.fanrong.frwallet.ui.contract.custom.CustomTokensActivity
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_add_contract.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.checkNotEmpty

class AddContractActivity : BaseActivity() {

    val wallet: WalletDao by lazy {
        intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
    }

    val assetAdapter: ContractAssetAdapter by lazy {
        ContractAssetAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                if (view.id == assetAdapter.addId) {
                    var token = assetAdapter.getItem(position)!!
                    if (split.contains(token.tokenChain+"_"+token.tokenSymbol)){
                        val queryContractCoin = CoinOperator.queryContractCoin(wallet, token.tokenContract!!)
                        if (queryContractCoin!=null)
                            CoinOperator.deleteCoin(wallet, queryContractCoin)
                        GoodSnackbar.showMsg(this@AddContractActivity, "已删除")
                        EventBus.getDefault().post(CurrentWalletChange())

//                    notifyDataSetChanged()
                        loadData()
                    }else{

                        CoinOperator.addContractAsset(wallet, token)
                        GoodSnackbar.showMsg(this@AddContractActivity, "已添加至首页资产")
                        EventBus.getDefault().post(CurrentWalletChange())
//                    notifyDataSetChanged()
                        loadData()
                    }
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_contract
    }

    override fun initView() {

        iv_ac_back.setOnClickListener {
            extFinishWithAnim()
        }
        tv_ac_search.setOnClickListener {
            extStartActivity(SearchTokenActivity::class.java, intent.extras!!)
        }
        tv_ac_assetsmanage.setOnClickListener {
            extStartActivity(HomeAssetManageActivity::class.java, intent.extras!!)
        }
        tv_ac_myassets.setOnClickListener {
            extStartActivity(MyAllAssetsActivity::class.java, intent.extras!!)
        }
        tv_ac_custom.setOnClickListener {
            extStartActivity(CustomTokensActivity::class.java, intent.extras!!)
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@AddContractActivity)
            adapter = assetAdapter
        }


    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)


        val queryContractAssetWithWallet = CoinOperator.queryContractAssetWithWallet(wallet)!!
        if (queryContractAssetWithWallet.checkNotEmpty()) {
            queryContractAssetWithWallet.removeAt(0)
        }
        assetAdapter.split = mutableListOf()
        for (liteCoinBeanModel in queryContractAssetWithWallet) {
            assetAdapter.split.add(liteCoinBeanModel.chain_name+"_"+liteCoinBeanModel.coin_name)
        }
        var hottokenList = ConfigTokenOperator.queryHotTokensByChainName(wallet.chainType!!)
        assetAdapter.setNewData(hottokenList)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: CurrentWalletChange) {
        loadData()
    }

}