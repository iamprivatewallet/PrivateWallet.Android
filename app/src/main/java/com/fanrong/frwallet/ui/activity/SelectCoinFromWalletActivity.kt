package com.fanrong.frwallet.ui.activity

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.SelectCoinAdapter
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.found.GoodSnackbar
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.ui.contract.ContractAssetAdapter
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryCoinReq
import com.fanrong.frwallet.wallet.eth.eth.QueryCoinResp
import kotlinx.android.synthetic.main.activity_search_token.*
import kotlinx.android.synthetic.main.activity_select_coinfromwallet.*
import kotlinx.android.synthetic.main.activity_select_coinfromwallet.et_st_search
import kotlinx.android.synthetic.main.activity_select_coinfromwallet.recyclerview
import org.greenrobot.eventbus.EventBus
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.utils.checkIsEmpty

class SelectCoinFromWalletActivity : BaseActivity() {
    val selectCoinAdapter: SelectCoinAdapter by lazy {
        SelectCoinAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                current_coin = selectCoinAdapter.getItem(position)
                setCurrentCoin(current_coin)
                notifyDataSetChanged()


                setResult(RESULT_OK, Intent().apply {
                    putExtra(FrConstants.SELECT_COIN, current_coin)
                })

                extFinishWithAnim()
            }
        }
    }

    val selectCoinAdapter_search: SelectCoinAdapter by lazy {
        SelectCoinAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                current_coin = selectCoinAdapter_search.getItem(position)
                setCurrentCoin(current_coin)
                notifyDataSetChanged()


                setResult(RESULT_OK, Intent().apply {
                    putExtra(FrConstants.SELECT_COIN, current_coin)
                })

                extFinishWithAnim()
            }
        }
    }

    lateinit var currentWallet: WalletDao
    var contractAsset: MutableList<CoinDao>? = null
    var current_coin:CoinDao? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_select_coinfromwallet
    }

    override fun initView() {
        current_coin = intent.getSerializableExtra(FrConstants.SELECT_COIN) as CoinDao

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@SelectCoinFromWalletActivity)
            adapter = selectCoinAdapter
        }
        selectCoinAdapter.setEmptyView(R.layout.emptyviewlayout,recyclerview)


        recyclerview_searchresult.apply {
            layoutManager = LinearLayoutManager(this@SelectCoinFromWalletActivity)
            adapter = selectCoinAdapter_search
        }
        selectCoinAdapter_search.setEmptyView(R.layout.emptyviewlayout,recyclerview_searchresult)


        et_st_search.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                searchToken(s.toString())
                if (s.toString().checkIsEmpty()){
                    ll_search_result.visibility = View.GONE
                }
            }

        })
    }

    override fun loadData() {
        currentWallet = WalletOperator.currentWallet!!
        contractAsset = CoinOperator.queryContractAssetWithWallet(currentWallet)
        selectCoinAdapter.setCurrentCoin(current_coin)
        selectCoinAdapter_search.setCurrentCoin(current_coin)
        selectCoinAdapter.setNewData(contractAsset)

        ll_search_result.visibility = View.GONE

    }

    fun searchToken(searchContent:String){
        if (WalletOperator.currentWallet != null && !searchContent.equals("")){
            centerApi.queryCoin(QueryCoinReq(CoinNameCheck.getCurrentNetID(),"1",searchContent)).netSchduler()
                .subscribeObj(object : NetCallBack<QueryCoinResp> {
                    override fun onSuccess(t: QueryCoinResp) {
                        var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
                        val mutableListOf = mutableListOf<CoinDao>()
                        if (t.data!=null){
                            for (item in t.data!!){
                                val liteCoinBeanModel = CoinDao(item.tokenName)
                                liteCoinBeanModel.contract_addr = item.tokenContract
                                liteCoinBeanModel.coin_decimals = item.tokenDecimals.toString()
                                liteCoinBeanModel.sourceAddr = walletInfo.address
                                liteCoinBeanModel.sourceWallet = walletInfo.chainType + "_" + walletInfo.address
                                mutableListOf.add(liteCoinBeanModel)
                            }
                            selectCoinAdapter_search.setNewData(mutableListOf)
                            ll_search_result.visibility = View.VISIBLE
                        }

                    }
                    override fun onFailed(error: Throwable) {

                    }

                })
        }else{

        }
    }
}