package com.fanrong.frwallet.ui.walletassets

import android.graphics.Color
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.TransferDao
import com.fanrong.frwallet.dao.database.TransferOperator
import com.fanrong.frwallet.dao.eventbus.TransferFinishEvent
import com.fanrong.frwallet.found.MvvmBaseActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.ui.receipt.ReceiptActivity
import com.fanrong.frwallet.ui.receipt.TransferActivity
import com.fanrong.frwallet.ui.receipt.viewmdel.TransferRecodViewmodel
import com.fanrong.frwallet.ui.receipt.viewmdel.TransferViewmodel
import com.fanrong.frwallet.wallet.eth.eth.GetTransactionRecordReq
import com.fanrong.frwallet.wallet.eth.eth.GetTransactionRecordResp
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import com.flyco.tablayout.listener.OnTabSelectListener
import com.xianchao.divider.divider.ListDivider
import kotlinx.android.synthetic.main.wallet_asset_detail_activity.*
import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.DensityUtil
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.tabslayout.TabEntity
import java.math.BigDecimal


class WalletAssetDetailActivty : MvvmBaseActivity<TransferViewmodel.State, TransferViewmodel>() {

    val transactionAdapter: TransactionAdapter by lazy {
        TransactionAdapter().apply {
            currentTokenInfo = tokenInfo
            setOnItemClickListener { adapter, view, position ->
                extStartActivity(TransferInfoDetailActivity::class.java, Bundle().apply {
                    val item = getItem(position)!!
                    putSerializable(FrConstants.TOKEN_INFO, tokenInfo)
                    putSerializable(FrConstants.TRANSACTION_INFO, TransferDao().apply {
                        to= item.to
                        from=item.from
                        status=item.status
                        transaction_hash=item.hash
                        amount=item.value.extHexWei2Ten2EtherKeep4()//extWei2Ether  extHexWei2Ten2EtherKeep8
                        timestamp=item.timeStamp?.toLong()?.times(1).toString()
//                        if (item.gasUsed.checkNotEmpty()){
//                            gasUsed= ETHChainUtil.compateGasRetWei(item.gasUsed!!, item.gasPrice!!)
//                        }else{
//                            gasUsed=item.gas
//                        }
                    } )
                })
            }
        }
    }
    private val transferRecordViewModel: TransferRecodViewmodel by lazy {
        TransferRecodViewmodel()
    }
//    var resultList: MutableList<GetTransactionRecordResp.DataBean.ResultBean>? = null
    var resultList = mutableListOf<GetTransactionRecordResp.DataBean.ResultBean>()
    var centerResultList = mutableListOf<GetTransactionRecordResp.DataBean.ResultBean>()

    var unFinishList: MutableList<GetTransactionRecordResp.DataBean.ResultBean>? = null
    val tokenInfo: CoinDao by lazy {
        intent.getSerializableExtra(FrConstants.TOKEN_INFO) as CoinDao
    }

    var currentTab = 0
    var pageNum = 1

    override fun getLayoutId(): Int {
        return R.layout.wallet_asset_detail_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@WalletAssetDetailActivty, CoinNameCheck.getNameByName(tokenInfo.coin_name!!))
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@WalletAssetDetailActivty)
            adapter = transactionAdapter

            addItemDecoration(
                ListDivider.Builder()
                    .setDividerColor(Color.parseColor("#EEEEEE"))
                    .setLeftMargin(DensityUtil.dp2px(40)).build()
            )
        }

        tv_receipt.setOnClickListener {
            extStartActivity(ReceiptActivity::class.java, intent.extras!!)
        }
        tv_transfer.setOnClickListener {
            extStartActivity(TransferActivity::class.java, intent.extras!!)
        }
        smt_refresh.setOnLoadMoreListener {
            pageNum++
            loadData()
        }
        transferRecordViewModel.observerDataChange(this,this::recordStateChange)
        tab_layout.apply {
            var tabs = mutableListOf<TabEntity>()
            tabs.add(TabEntity("全部"))
            tabs.add(TabEntity("转出"))
            tabs.add(TabEntity("转入"))
            tabs.add(TabEntity("失败"))
            setTabData(ArrayList(tabs))
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    this@WalletAssetDetailActivty.currentTab = position
                    loadListData()
                }
                override fun onTabReselect(position: Int) {

                }
            })
        }

        transactionAdapter.setEmptyView(R.layout.emptyviewlayout,recyclerview)
    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
        viewmodel.getBalance(tokenInfo)

        transferRecordViewModel.getTransactionRecordReq(GetTransactionRecordReq().apply {
            address=tokenInfo.sourceAddr
            contractAddress=tokenInfo.contract_addr
            type=tokenInfo.coin_name
            page=pageNum.toString()
//            SWLog.e("--pageNum-$pageNum")
        })
        addUnfinishList()
        loadListData()

    }
    fun loadListData(){
        smt_refresh.setEnableLoadMore(false)
        smt_refresh.setEnableRefresh(false)
        var inUnFinishList = mutableListOf<GetTransactionRecordResp.DataBean.ResultBean>()
        var outUnFinishList = mutableListOf<GetTransactionRecordResp.DataBean.ResultBean>()
        var errotList = mutableListOf<GetTransactionRecordResp.DataBean.ResultBean>()

        if (resultList.checkIsEmpty()){
            return
        }
        when (currentTab) {
            0 -> {
                smt_refresh.setEnableAutoLoadMore(false)
                if (centerResultList.checkNotEmpty()){
                    if (resultList.size>10){
                        smt_refresh.setEnableLoadMore(true)
                    }
                }
                resultList.sortByDescending{
                    it.timeStamp
                }
                transactionAdapter.setNewData(resultList)
            }
            1 -> {
                for (inList in resultList!!){
                    if (inList.isTransOut(tokenInfo.sourceAddr!!)&&inList.status!="E"){
                        inUnFinishList.add(inList)
                    }
                }
                inUnFinishList.sortByDescending{
                    it.timeStamp
                }
                transactionAdapter.setNewData(inUnFinishList)
            }
            2 -> {
                for (inList in resultList!!){
                    if (!inList.isTransOut(tokenInfo.sourceAddr!!)&&inList.status!="E"){
                        outUnFinishList.add(inList)
                    }
                }
                outUnFinishList.sortByDescending{
                    it.timeStamp
                }
                transactionAdapter.setNewData(outUnFinishList)
            }
            3 -> {
                for (error in resultList!!){
                    if (error.status=="E"||error.isError!="0"){
                        errotList.add(error)
                    }
                }
                errotList.sortByDescending{
                    it.timeStamp
                }
                transactionAdapter.setNewData(errotList)
            }
            else -> {
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: TransferFinishEvent) {
        pageNum=1
        resultList.clear()
        centerResultList.clear()
        loadData()
    }
    //未完成的订单
    private fun addUnfinishList(){
//        val iterator = resultList.iterator()
//        while (iterator.hasNext()){
//            val next = iterator.next()
//            if (next.status=="P"){
//                iterator.remove()
//            }
//        }
//        for (unfinsh in resultList){
//            if (unfinsh.status=="P"){
//                resultList.remove(unfinsh)
//            }
//        }
        val allUnFinishList = TransferOperator.queryOrder(tokenInfo).toMutableList()
        if (allUnFinishList.checkNotEmpty()){
            for (all in allUnFinishList){
                val apply = GetTransactionRecordResp.DataBean.ResultBean().apply {
                    value= BytesHelper.bigDecimal2HexStr(BigDecimal(all.amount), 18)
                    from=all.from
                    to=all.to
                    hash=all.transaction_hash
                    timeStamp=all.timestamp?.toLong()?.div(1000).toString()
                    status= all.status!!
                    gasPrice=all.gasPrice
                    gasUsed=all.gasUsed
                    status= all.status!!
                    isError=if (all.status=="E"){
                        "1"
                    }else{
                        "0"
                    }
                }
                resultList.add(0,apply)
            }
        }
    }

    override fun getViewModel(): TransferViewmodel {
        return TransferViewmodel.getViewmodel(tokenInfo)
    }
    //中心化获取交易订单
    private fun  recordStateChange(state:TransferRecodViewmodel.State){
        state.transactionRecordResult?.run {
            smt_refresh.finishLoadMore()
            val mutableList = resultData as MutableList<GetTransactionRecordResp.DataBean.ResultBean>?
            SWLog.e("--pageNum-$pageNum")
            centerResultList!!.addAll(mutableList!!)
            if (centerResultList.checkNotEmpty()){
                for (all in resultList){
                    if (all.timeStamp?.toLong()?.div(1000)!! >(centerResultList[0]?.timeStamp!!.toLong())){
                        if (centerResultList[0].hash!=all.hash){
                            centerResultList.add(0,all)
                        }
                    }
                }
//                resultList.clear()
                resultList.addAll(centerResultList)
            }
            loadListData()
        }
    }
    override fun stateChange(state: TransferViewmodel.State) {
        state.balanceResult?.run {
            tv_balance.setText(resultData!!.balance)
        }

    }
}