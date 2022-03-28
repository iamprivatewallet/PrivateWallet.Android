package com.fanrong.frwallet.ui.activity

import android.os.Handler
import android.os.Message
import android.text.Editable
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.SearchHistoryAdapter
import com.fanrong.frwallet.adapter.SearchTJAdapter
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.GoodSnackbar
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.ui.contract.ContractAssetAdapter
import com.fanrong.frwallet.ui.contract.custom.TokenViewModel
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryCoinReq
import com.fanrong.frwallet.wallet.eth.eth.QueryCoinResp
import kotlinx.android.synthetic.main.activity_custom_tokens.*
import kotlinx.android.synthetic.main.activity_search_token.*
import kotlinx.android.synthetic.main.fragment_dapp.*
import org.greenrobot.eventbus.EventBus
import org.litepal.LitePal
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.CommonTool
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.utils.extGoneOrVisible

class SearchTokenActivity : BaseActivity() {

    companion object {
        val RECYCLER_EMPTY = 1
        val RECYCLER_FULL = 2
    }

    val wallet: WalletDao by lazy {
        intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
    }

    val viewmodel: TokenViewModel by lazy {
        TokenViewModel()
    }

    var inputListener = Runnable {
//        if (et_st_search.text.toString().extCheckIsContractAddr()) {
//            viewmodel.getTokenInfo(wallet, et_st_search.text.toString())
//        } else {
//            showToast("token 合约地址不对")
//        }
    }


    val assetAdapter: ContractAssetAdapter by lazy {
        ContractAssetAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                if (view.id == assetAdapter.addId) {
                    val item = assetAdapter.getItem(position)!!
                    if (split.contains(item.tokenChain+"_"+item.tokenSymbol)){
                        val queryContractCoin = CoinOperator.queryContractCoin(wallet, item.tokenContract!!)
                        if (queryContractCoin!=null)
                            CoinOperator.deleteCoin(wallet, queryContractCoin)
                        GoodSnackbar.showMsg(this@SearchTokenActivity, "已删除")
                        EventBus.getDefault().post(CurrentWalletChange())

//                    notifyDataSetChanged()
                        query()
                    }else{
                        CoinOperator.addContractAsset(wallet, item)
                        GoodSnackbar.showMsg(this@SearchTokenActivity, "已添加至首页资产")
                        EventBus.getDefault().post(CurrentWalletChange())
//                    notifyDataSetChanged()
                        query()
                    }
                }
            }
        }
    }

    val tjAdapter: SearchTJAdapter by lazy {
        SearchTJAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val item = tjAdapter.getItem(position)!!
                if (split.contains(item.tokenChain+"_"+item.tokenSymbol)){
                    GoodSnackbar.showMsg(this@SearchTokenActivity, "不能重复添加")
                }else{
                    CoinOperator.addContractAsset(wallet, item)
                    GoodSnackbar.showMsg(this@SearchTokenActivity, "已添加至首页资产")
                    EventBus.getDefault().post(CurrentWalletChange())
//                    notifyDataSetChanged()
                    query()
                }
            }
        }
    }

    val historyAdapter: SearchHistoryAdapter by lazy {
        SearchHistoryAdapter().apply {
            setOnItemChildClickListener { adapter, view, position ->
                val item = historyAdapter.getItem(position)!!

                if (split.contains(item.tokenChain+"_"+item.tokenSymbol)){
                    //删除
                    val queryContractCoin = CoinOperator.queryContractCoin(wallet, item.tokenContract!!)
                    if (queryContractCoin!=null)
                        CoinOperator.deleteCoin(wallet, queryContractCoin)
                    GoodSnackbar.showMsg(this@SearchTokenActivity, "已删除")
                    EventBus.getDefault().post(CurrentWalletChange())

//                    notifyDataSetChanged()
                    query()
                }else{
                    //添加
                    var coin = ConfigTokenDao().apply {
                        this.id = item.id.toLong()
                        this.tokenContract = item.tokenContract
                        this.tokenName = item.tokenName
                        this.tokenSymbol = item.tokenSymbol
                        this.tokenDecimals = item.tokenDecimals.toString()
                        this.tokenChain = CoinNameCheck.getNetWorkNameByID(item.tokenChain!!)
                        this.tokenLogo = item.tokenLogo
                        this.createTime = item.createTime
                        this.hotTokens = item.hotTokens
                    }
                    CoinOperator.addContractAsset(wallet, coin)
                    GoodSnackbar.showMsg(this@SearchTokenActivity, "已添加至首页资产")
                    EventBus.getDefault().post(CurrentWalletChange())
//                    notifyDataSetChanged()
                    query()
                }
            }
        }
    }

    var handler: Handler? = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            query()
        }
    }

    private fun query() {
        val toString = et_st_search.text.toString()
//        val get = ConfigTokenOperator.queryTokensByChainName(wallet.chainType!!) ?: mutableListOf()
//        val mutableListOf = mutableListOf<ConfigTokenDao>()
//
//
//        for (liteCoinBeanModel in get) {
//            if (liteCoinBeanModel.tokenSymbol!!.contains(toString)) {
//                mutableListOf.add(liteCoinBeanModel)
//            }
//        }
        val queryContractAssetWithWallet = CoinOperator.queryContractAssetWithWallet(wallet)
        assetAdapter.split.clear()
        for (liteCoinBeanModel in queryContractAssetWithWallet!!) {
            assetAdapter.split.add(liteCoinBeanModel.chain_name+"_"+liteCoinBeanModel.coin_name)
        }
//        assetAdapter.setNewData(mutableListOf)
//
//        if (mutableListOf.checkNotEmpty()) {co
//            changeState(RECYCLER_FULL)
//        } else {
//            changeState(RECYCLER_EMPTY)
//        }


        if (WalletOperator.currentWallet != null && !toString.equals("")){
            centerApi.queryCoin(QueryCoinReq(CoinNameCheck.getCurrentNetID(),"1",toString)).netSchduler()
                .subscribeObj(object : NetCallBack<QueryCoinResp> {
                    override fun onSuccess(t: QueryCoinResp) {
                        val mutableListOf = mutableListOf<ConfigTokenDao>()

                        if (t != null && t.data != null){
                            for (item in t.data!!){
                                var coin = ConfigTokenDao().apply {
                                    this.id = item.id.toLong()
                                    this.tokenContract = item.tokenContract
                                    this.tokenName = item.tokenName
                                    this.tokenSymbol = item.tokenSymbol
                                    this.tokenDecimals = item.tokenDecimals.toString()
                                    this.tokenChain = CoinNameCheck.getNetWorkNameByID(item.tokenChain)
                                    this.tokenLogo = item.tokenLogo
                                    this.createTime = item.createTime
                                    this.hotTokens = item.hotTokens
                                }

                                var history = SearchHistoryResult().apply {
                                    this.id = item.id.toLong()
                                    this.tokenContract = item.tokenContract
                                    this.tokenName = item.tokenName
                                    this.tokenSymbol = item.tokenSymbol
                                    this.tokenDecimals = item.tokenDecimals.toString()
                                    this.tokenChain = CoinNameCheck.getNetWorkNameByID(item.tokenChain)
                                    this.tokenLogo = item.tokenLogo
                                    this.createTime = item.createTime
                                    this.hotTokens = item.hotTokens

                                    if (checkIsNeedSave(this)){
                                        save()
                                    }
                                }
                                mutableListOf.add(coin)
                            }
                            assetAdapter.setNewData(mutableListOf)
                            if (mutableListOf.checkNotEmpty()) {
                                changeState(RECYCLER_FULL)
                            } else {
                                changeState(RECYCLER_EMPTY)
                            }
                        }

                    }

                    override fun onFailed(error: Throwable) {
                        Log.d("TAG", "onSuccess: --->>>>>")
//                    newAndPostValue {
//                        errorinfo = LiveDataErrorInfo(error.message?:"未知错误")
//                    }
                    }

                })
        }else{
            changeState(RECYCLER_EMPTY)
        }
    }

    fun checkIsNeedSave(result: SearchHistoryResult):Boolean{
        val allSearchHistoryResult = LitePal.findAll(SearchHistoryResult::class.java)
        for (item in allSearchHistoryResult){
            if (item.tokenContract.equals(result.tokenContract)){
                return false
            }
        }
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_search_token
    }

    override fun initView() {

//        myToken 支持所有ETH代币
//        tv_tokens_hint.setText("Private Wallet 支持所有${wallet.chainType}代币")
        et_st_search.requestFocus()
        iv_st_back.setOnClickListener {
            extFinishWithAnim()
        }

        et_st_search.addTextChangedListener(object : TextWatcherAfter() {
            override fun tryAfterTextChanged(s: Editable?) {
                super.tryAfterTextChanged(s)
                handler!!.removeMessages(10)
                handler!!.sendEmptyMessageAtTime(10, 1000)

                CommonTool.mainHandler.removeCallbacks(inputListener)
                CommonTool.mainHandler.postDelayed(inputListener, 500)
            }
        })

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@SearchTokenActivity)
            adapter = assetAdapter
        }

        tv_upload.setOnClickListener {
            extStartActivity(DappBrowserActivity::class.java)
        }

        changeState(RECYCLER_EMPTY)

        viewmodel.observerDataChange(this, this::stateChange)


        rl_tj.apply {
            layoutManager = LinearLayoutManager(this@SearchTokenActivity, RecyclerView.HORIZONTAL, false)
            adapter = tjAdapter
        }

        rl_history.apply {
            layoutManager = LinearLayoutManager(this@SearchTokenActivity)
            adapter = historyAdapter
        }

        iv_delecthistory.setOnClickListener{
            LitePal.deleteAll(SearchHistoryResult::class.java)
            val allSearchHistoryResult = LitePal.findAll(SearchHistoryResult::class.java)
            historyAdapter.setNewData(allSearchHistoryResult)
            historyAdapter.notifyDataSetChanged()
        }
        historyAdapter.setEmptyView(R.layout.emptyviewlayout,rl_history)
    }

    fun stateChange(state: TokenViewModel.State) {
        extShowOrDismissDialog(state.isShowLoading)

        state.errorinfo?.run {
            showToast(msg)
        }

        state.decimalResult?.run {
            if (isExcuteSuccess) {
//                et_decimal.setText(resultData)
                Log.d("TAG", "stateChange: "+resultData)
            }
        }
        state.symbolResult?.run {
            if (isExcuteSuccess) {
//                et_symbol.setText(resultData)
                Log.d("TAG", "stateChange: "+resultData)
            }
        }

    }

    fun changeState(state: Int) {
        ll_empty_layout.extGoneOrVisible(state == 1)
        ll_result.extGoneOrVisible(state == 2)

    }
    var split = mutableListOf<String>()
    override fun loadData() {

        val queryContractAssetWithWallet = CoinOperator.queryContractAssetWithWallet(wallet)!!
        if (queryContractAssetWithWallet.checkNotEmpty()) {
            queryContractAssetWithWallet.removeAt(0)
        }
        split = mutableListOf()
        historyAdapter.split = mutableListOf()
        for (liteCoinBeanModel in queryContractAssetWithWallet) {
            split.add(liteCoinBeanModel.chain_name+"_"+liteCoinBeanModel.coin_name)
            historyAdapter.split.add(liteCoinBeanModel.chain_name+"_"+liteCoinBeanModel.coin_name)
        }
        var hottokenList = ConfigTokenOperator.queryHotTokensByChainName(wallet.chainType!!)
        tjAdapter.setNewData(hottokenList)


        val allSearchHistoryResult = LitePal.findAll(SearchHistoryResult::class.java)
        historyAdapter.setNewData(allSearchHistoryResult)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler = null
    }

}