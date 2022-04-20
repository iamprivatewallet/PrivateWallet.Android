package com.fanrong.frwallet.ui.activity

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.ManageAssetsBalanceAdapter1
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.found.MvvmBaseActivity
import com.fanrong.frwallet.inteface.OnAssetsSortDialogItemClick
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.view.AssetsSortDialog
import com.fanrong.frwallet.wallet.eth.viewmodel.WalletViewmodel
import com.yanzhenjie.recyclerview.swipe.*
import kotlinx.android.synthetic.main.activity_home_asset_manage.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.DensityUtil
import xc.common.tool.utils.LibViewUtils
import xc.common.tool.utils.checkNotEmpty


class HomeAssetManageActivity : MvvmBaseActivity<WalletViewmodel.State, WalletViewmodel>() {

    val wallet: WalletDao by lazy {
        intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
    }
    var mutableList = mutableListOf<CoinDao>()
    var mutableListCommonSort = mutableListOf<CoinDao>()
    val mAdapter: ManageAssetsBalanceAdapter1 by lazy {
        ManageAssetsBalanceAdapter1().apply {
            onitemDelectListener = object : ManageAssetsBalanceAdapter1.ItemDelectListener {
                override fun onDelect(position: Int) {
                    val item = mAdapter.getItem(position)
                    mAdapter.remove(position)
//                    CoinOperator.deleteCoin(wallet, item)
////                    showToast(item!!.id.toString())
//                    EventBus.getDefault().post(CurrentWalletChange())
                }
            }
            onitemToTopListener = object : ManageAssetsBalanceAdapter1.ItemToTopListener{
                override fun onToTop(position: Int) {
                    val item = mAdapter.getItem(position)
                    mAdapter.remove(position)
                    mAdapter.add(0,item!!)

                    mutableList.clear()
                    CoinOperator.queryContractAssetWithWallet(wallet)?.get(0)?.let { mutableList.add(it) }
                    mutableList.addAll(mAdapter.data)
                    wallet.sortType="3"
                    WalletOperator.update(wallet)
                    CoinOperator.saveSortCoins(wallet, mutableList)
                    EventBus.getDefault().post(CurrentWalletChange())
                }

            }
//            setOnItemChildClickListener { adapter, view, position ->
//                if (view.id == remoreId) {
////                    rcv_ham_wallet.smoothOpenRightMenu(position)
////                    mAdapter.getViewByPosition(position, ll_container)!!.visibility = View.GONE
//                    (view.parent as ViewGroup).getChildAt(3).visibility = View.GONE
//                } else if (view.id == ll_container) {
//                    (view.parent as ViewGroup).getChildAt(3).visibility = View.VISIBLE
//                }
//            }


        }
    }


    var menuCreate = object : SwipeMenuCreator {
        override fun onCreateMenu(leftMenu: SwipeMenu, rightMenu: SwipeMenu, position: Int) {

            val deleteItem = SwipeMenuItem(this@HomeAssetManageActivity)
            deleteItem.text = getString(R.string.sc)
            deleteItem.setTextColor(Color.parseColor("#ffffff"))
            deleteItem.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            deleteItem.setWidth(DensityUtil.dp2px(90))
            deleteItem.setBackgroundColor(Color.parseColor("#D7390D"))
            rightMenu.addMenuItem(deleteItem)
        }

    }

    var menuClick = object : SwipeMenuItemClickListener {
        override fun onItemClick(menuBridge: SwipeMenuBridge, position: Int) {
            CoinOperator.deleteCoin(wallet, mAdapter.getItem(position))
            mAdapter.remove(position)
            EventBus.getDefault().post(CurrentWalletChange())
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.activity_home_asset_manage
    }

    override fun initView() {
        super.initView()
//        Scroller(this
        if (wallet.sortType=="0"||wallet.sortType=="3"){
            tv_ham_sort.text="默认排序"
        }else if (wallet.sortType=="1"){
            tv_ham_sort.text="价值排序"
        }else{
            tv_ham_sort.text="名称排序"
        }
        ham_title.apply {
            setTitleText(getString(R.string.bzgl))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)

            rightTextView.text = getString(R.string.bj)
            rightTextView.setTextColor(resources.getColor(R.color.main_blue))
            rightTextView.visibility = View.VISIBLE
            LibViewUtils.setViewTwoState(rightTextView, onChangeState1 = {
                rightTextView.text = getString(R.string.bj)
                mAdapter.isEdit = false
                mAdapter.notifyDataSetChanged()

                mutableList.clear()
                CoinOperator.queryContractAssetWithWallet(wallet)?.get(0)?.let { mutableList.add(it) }
                mutableList.addAll(mAdapter.data)
                wallet.sortType="3"
                WalletOperator.update(wallet)
                CoinOperator.saveSortCoins(wallet, mutableList)
                EventBus.getDefault().post(CurrentWalletChange())

            }, onChangeState2 = {
                rightTextView.text = getString(R.string.wc)
                mAdapter.isEdit = true
                mAdapter.notifyDataSetChanged()
            })


            setOnBackClickListener {
                extFinishWithAnim()
            }
        }
        tv_ham_sort.setOnClickListener {

            AssetsSortDialog(this,
                OnAssetsSortDialogItemClick {
                    tv_ham_sort.text = it
//                    showToast(it)
                    mutableListCommonSort.clear()
                    if (it == "价值排序") {
                        wallet.sortType="1"
                        mutableListCommonSort.addAll(mAdapter.data.sortedByDescending { it.balance })
                        mAdapter.setNewData(mAdapter.data.sortedByDescending { it.balance })
                    } else if (it == "名称排序") {
                        wallet.sortType="2"
                        mutableListCommonSort.addAll(mAdapter.data.sortedWith(compareBy { it.coin_name.toUpperCase() }))
                        mAdapter.setNewData(mAdapter.data.sortedWith(compareBy { it.coin_name.toUpperCase() }))
                    } else {
                        wallet.sortType="0"
                        mutableListCommonSort.addAll(mAdapter.data.sortedBy { it.coin_name })
                        mAdapter.setNewData(mAdapter.data.sortedBy { it.coin_name })
                    }
                    WalletOperator.update(wallet)
                    val mutableListOf = mutableListOf<CoinDao>()
                    CoinOperator.queryContractAssetWithWallet(wallet)?.get(0)?.let { mutableListOf.add(it) }
                    mutableListOf.addAll(mutableListCommonSort)
                    CoinOperator.saveSortCoins(wallet, mutableListOf)
                    EventBus.getDefault().post(CurrentWalletChange())
//                    mAdapter.setNewData(LiteCoinOperator.queryAndSortContractAssetWithWallet(wallet,it))
                }).show()
        }
        rcv_ham_wallet.apply {
            layoutManager = LinearLayoutManager(this@HomeAssetManageActivity)
//            setSwipeMenuCreator(menuCreate)
//            setSwipeMenuItemClickListener(menuClick)
            adapter = mAdapter
//            rcv_ham_walle
        }
        mAdapter.setEmptyView(R.layout.emptyviewlayout,rcv_ham_wallet)

        val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(mAdapter)
        val itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
        itemTouchHelper.attachToRecyclerView(rcv_ham_wallet)
        // open drag
        mAdapter.enableDragItem(itemTouchHelper, R.id.iv_drag_view, true);
//        mAdapter.setOnItemDragListener(object : OnItemDragListener {
//            override fun onItemDragStart(p0: RecyclerView.ViewHolder?, p1: Int) {
//            }
//
//            override fun onItemDragMoving(p0: RecyclerView.ViewHolder?, p1: Int, p2: RecyclerView.ViewHolder?, p3: Int) {
//            }
//
//            override fun onItemDragEnd(p0: RecyclerView.ViewHolder?, p1: Int) {
//                mutableList.clear()
//                CoinOperator.queryContractAssetWithWallet(wallet)?.get(0)?.let { mutableList.add(it) }
//                mutableList.addAll(mAdapter.data)
//                wallet.sortType="3"
//                WalletOperator.update(wallet)
//                CoinOperator.saveSortCoins(wallet, mutableList)
//                EventBus.getDefault().post(CurrentWalletChange())
//            }
//
//        });
        tv_ham_coinname.setText(wallet.chainType)
//        ll_toptitle.visibility = View.VISIBLE

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
            if (resultData.checkNotEmpty()) {
                val mainCoin = resultData!!.get(0)
                tv_ham_balance.setText(mainCoin.balance)
                tv_ham_coinname.setText(CoinNameCheck.getNameByName(mainCoin.coin_name))
                ll_toptitle.visibility = View.VISIBLE
//                iv_maincoin
                var options: RequestOptions =  RequestOptions()
                    .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
                    .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
                    .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
                Glide.with(iv_maincoin).load(mainCoin.coin_icon).apply(options).into(iv_maincoin)
                resultData!!.removeAt(0)
                mAdapter.setNewData(resultData!!)
            }
        }
    }
}