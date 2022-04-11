package com.fanrong.frwallet.ui.fragment

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.SearchTJAdapter
import com.fanrong.frwallet.adapter.marketItemAdapter
import com.fanrong.frwallet.dao.data.marketDataBean
import com.fanrong.frwallet.dao.database.ChainNodeDao
import com.fanrong.frwallet.dao.database.CoinOperator
import com.fanrong.frwallet.dao.database.LikeMarketItemDao
import com.fanrong.frwallet.dao.database.LikeMarketItemOperator
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.dao.eventbus.ScoketDataEvent
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.found.GoodSnackbar
import com.fanrong.frwallet.scoket.WsManager
import com.fanrong.frwallet.tools.NetTools
import com.fanrong.frwallet.view.showTopToast
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_search_token.*
import kotlinx.android.synthetic.main.fragment_marketlist.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.framework.ui.base.BaseFragment

class MarketListFragment(_type:Int): BaseFragment() {
    var type:Int = _type
    var MarketList:marketDataBean? = null

    val marketItemAdapter: marketItemAdapter by lazy {
        marketItemAdapter().apply {
            setOnItemClickListener { adapter, view, position ->

            }
            setOnItemChildClickListener{adapter, view, position ->
                if (view.id == marketItemAdapter.iv_zx){
                    val item = marketItemAdapter.getItem(position)

                    val itemList = LikeMarketItemOperator.getItemBySymbol(item!!.symbol)

                    if (itemList == null || itemList.size == 0){
                        //没添加自选
                        val likeMarketItemDao = LikeMarketItemDao().apply {
                            this.amout = item.amout
                            this.createTime = item.createTime
                            this.id = item.id.toLong()
                            this.last = item.last
                            this.lastVol = item.lastVol
                            this.rose = item.rose
                            this.status = item.status
                            this.symbol = item.symbol
                            this.type = item.type
                            this.updateTime = item.updateTime
                            this.isLike = true
                        }
                        likeMarketItemDao.save()
                        showTopToast(activity!!,activity!!.getString(R.string.tjzx),true)
                    }else{
                        val get = itemList.get(0)
                        if (get.isLike!!){
                            get.isLike = false
                            showTopToast(activity!!,activity!!.getString(R.string.qxzx),true)
                        }else{
                            get.isLike = true
                            showTopToast(activity!!,activity!!.getString(R.string.tjzx),true)
                        }
                        LikeMarketItemOperator.update(get)

                    }
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_marketlist
    }

    override fun initView() {
//        tv_test.setText(type.toString())

        rl_recyclerview.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = marketItemAdapter
        }
//        marketItemAdapter.setEmptyView(R.layout.emptyviewlayout,rl_recyclerview)
    }

    override fun loadData() {
        if (WsManager.getInstance().cur_data!=null && WsManager.getInstance().cur_data != ""){
            refreshData(NetTools.formatJson(WsManager.getInstance().cur_data,marketDataBean::class.java)!!)
        }
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: ScoketDataEvent) {
        Log.d("webscoket", "onReceiptEvent: ")
        if (event.data != null){
            refreshData(event.data!!)
        }
    }

    fun refreshData(result:marketDataBean){
        if (result != null){
            if (type != 0){
                marketItemAdapter.setNewData(result?.data?.filter { it.type == type })
            }else{
                val all = LikeMarketItemOperator.getAll()
                val marketDataBean = marketDataBean()
                var list = mutableListOf<marketDataBean.DataDTO>()
                for (item in all){
                    if (item.isLike!!){
                        val bean = marketDataBean.DataDTO().apply {
                            this.amout = item.amout
                            this.createTime = item.createTime
                            this.id = item.id.toInt()
                            this.last = item.last
                            this.lastVol = item.lastVol
                            this.rose = item.rose
                            this.status = item.status
                            this.symbol = item.symbol
                            this.type = item.type
                            this.updateTime = item.updateTime
                            this.isLike = true
                        }
                        list?.add(bean)
                    }
                }
                marketItemAdapter.setNewData(list)
            }

            if (marketItemAdapter.itemCount > 0){
                inclode_empty.visibility = View.GONE
                ll_normal.visibility = View.VISIBLE
            }else{
                inclode_empty.visibility = View.VISIBLE
                ll_normal.visibility = View.GONE
            }

        }
    }


    override fun onNoShakeClick(v: View) {

    }
}