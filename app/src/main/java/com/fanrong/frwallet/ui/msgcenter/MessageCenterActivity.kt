package com.fanrong.frwallet.ui.msgcenter

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.found.MvvmBaseActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.ui.walletassets.TransferInfoDetailActivity
import com.fanrong.frwallet.wallet.eth.eth.QueryTransactionPageReq
import com.flyco.tablayout.listener.OnTabSelectListener
import kotlinx.android.synthetic.main.activity_message_center.*
import kotlinx.android.synthetic.main.activity_message_center.recyclerview
import kotlinx.android.synthetic.main.activity_message_center.smt_refresh
import kotlinx.android.synthetic.main.activity_message_center.tab_layout
import kotlinx.android.synthetic.main.wallet_asset_detail_activity.*
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.viewlib.tabslayout.TabEntity

class MessageCenterActivity : MvvmBaseActivity<SysMsgRecodViewmodel.State, SysMsgRecodViewmodel>() {

    val msgCenterAdapter: MsgCenterAdapter by lazy {
        MsgCenterAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                if (msgCenterAdapter.getItem(position)?.txHash.equals("")){
                    extStartActivity(MsgDetailActivity::class.java, Bundle().apply {
                        putSerializable(FrConstants.MSG_INFO, msgCenterAdapter.getItem(position))
                    })
                }else{
                    var wallet: WalletDao = WalletOperator.currentWallet!! //当前钱包
                    var item = msgCenterAdapter.getItem(position)
                    var liteCoinBeanModel = CoinDao(item?.symbol!!)
                    liteCoinBeanModel.contract_addr = item.contract
                    liteCoinBeanModel.coin_decimals = "0"
                    liteCoinBeanModel.sourceAddr = item.fromAddress
                    liteCoinBeanModel.sourceWallet = wallet.chainType + "_" + wallet.address

                    extStartActivity(TransferInfoDetailActivity::class.java, Bundle().apply {
                        val item = getItem(position)!!
                        putSerializable(FrConstants.TOKEN_INFO, liteCoinBeanModel)
                        putSerializable(FrConstants.TRANSACTION_INFO, TransferDao().apply {
                            to= item.toAddress
                            from=item.fromAddress
                            status="D"
                            transaction_hash=item.txHash
                            amount=item.amount.toString()
                            timestamp=item.createTime
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
    }
    var zzList = mutableListOf<MsgDao>()
    var xtList = mutableListOf<MsgDao>()

    var cur_pagenumber:Int = 0
    var cur_pagesize:Int = 20

    override fun getLayoutId(): Int {
        return R.layout.activity_message_center
    }
    var currentTab = 0

    override fun initView() {
        mc_title.apply {
            extInitCommonBgAutoBack(this@MessageCenterActivity,"系统消息")
            setRightText("全部已读")
            setRightTextClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    showToast("全部已读")
                }
            })
        }
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@MessageCenterActivity)
            adapter = msgCenterAdapter
        }

        smt_refresh.setOnLoadMoreListener {
            cur_pagenumber++
            loadData()
        }

        tab_layout.apply {
            var tabs = mutableListOf<TabEntity>()
            tabs.add(TabEntity("转账通知"))
            tabs.add(TabEntity("系统消息"))
            setTabData(ArrayList(tabs))
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    this@MessageCenterActivity.currentTab = position
                    this@MessageCenterActivity.cur_pagenumber = 0
                    loadData()
                }
                override fun onTabReselect(position: Int) {

                }
            })
        }
    }

    override fun loadData() {
        var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
        val nodeInfo = ChainNodeOperator.queryCurrentNode(walletInfo.chainType!!)//当前网络
        smt_refresh.setEnableLoadMore(false)
        smt_refresh.setEnableRefresh(false)
        if (currentTab == 0)
            viewmodel.getSysMsgRecordReq(QueryTransactionPageReq(nodeInfo.chainId!!,"0x2093c44a1990fddd1f2976a70e1b525510530401",cur_pagenumber.toString(),cur_pagesize.toString()))
        else
            viewmodel.getSysMsgRecordReq2(QueryTransactionPageReq(nodeInfo.chainId!!,walletInfo.address,cur_pagenumber.toString(),cur_pagesize.toString()))
    }

    override fun getViewModel(): SysMsgRecodViewmodel {
        return SysMsgRecodViewmodel()
    }

    override fun stateChange(state: SysMsgRecodViewmodel.State) {
        smt_refresh.finishLoadMore()
        state.msgRecordResult?.run {

            msgCenterAdapter.setNewData(resultData)
        }
    }

}