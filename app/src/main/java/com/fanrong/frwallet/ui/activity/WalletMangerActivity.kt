package com.fanrong.frwallet.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basiclib.base.BaseActivity
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.WalletListAdapter
import com.fanrong.frwallet.adapter.WalletManagerListAdapter
import com.fanrong.frwallet.adapter.WalletTypeListAdapter
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.dao.eventbus.DappHistoryEvent
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.ui.contract.custom.CustomTokensActivity
import com.fanrong.frwallet.ui.createwallet.AddWalletActivity
import com.fanrong.frwallet.ui.dialog.ReceiptBackupsHintDialog
import com.fanrong.frwallet.ui.dialog.SelectWalletNetworkListDialog
import com.fanrong.frwallet.ui.node.CustomNodeActivity
import com.fanrong.frwallet.ui.node.NodeListAdapter
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_wallet_manger.*
import kotlinx.android.synthetic.main.activity_wallet_manger.tv_changenetwork
import kotlinx.android.synthetic.main.activity_wallet_manger.tv_current_chainname
import kotlinx.android.synthetic.main.activity_wallet_manger.tv_current_chaintype_fullname
import kotlinx.android.synthetic.main.dapp_star_list_activity.*
import kotlinx.android.synthetic.main.dialog_select_wallet.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.LibViewUtils
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

class WalletMangerActivity : BaseActivity() {
    var chainlist = mutableListOf<ChainInfo>().apply {
//        add(ChainInfo("All", R.mipmap.src_lib_eui_icon_walletethereum,""))
        addAll(FrConstants.suport_chains)
    }

    val walletTypeAdapter: WalletTypeListAdapter by lazy {
        WalletTypeListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                setmPosition(position)
                notifyDataSetChanged()

                if (walletTypeAdapter.getItem(position)!!.name.equals("All")) {
                    mWalletAdapter.setNewData(getAllWallet())
                } else {
                    val addrs = WalletOperator.queryWalletWithChainType(walletTypeAdapter.getItem(position)!!.name)
                    if (addrs.checkNotEmpty()) {
                        var data = mutableListOf<WalletListAdapter.Item>()
                        for (addr in addrs) {
                            data.add(WalletListAdapter.Item(3, addr))
                        }
                        mWalletAdapter.setNewData(data)
                    } else {
                        mWalletAdapter.setNewData(mutableListOf())
                    }
                }
            }

            setNewData(chainlist)
        }

    }
    val mWalletAdapter: WalletManagerListAdapter by lazy {
        WalletManagerListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                if (mWalletAdapter.getItem(position)!!.myItemType == 1) {
                    extStartActivity( IdentityWalletManageActivity::class.java)
                } else if (mWalletAdapter.getItem(position)!!.myItemType == 3) {

                    extStartActivity(WalletInfoManageActivity::class.java, Bundle().apply {
                        putSerializable(FrConstants.WALLET_INFO, mWalletAdapter.getItem(position)!!.itemData)
                    })
                } else if (mWalletAdapter.getItem(position)!!.myItemType == 4) {
                    extStartActivity(AddCoinsActivity::class.java, Bundle().apply {
                        putSerializable(FrConstants.WALLET_INFO, WalletOperator.queryIdentityWallet())
                    })
                }
            }

            setOnItemChildClickListener { adapter, view, position ->
                if (view.id == (adapter as WalletManagerListAdapter).iv_totop) {
                    val item = mWalletAdapter.getItem(position)
                    mWalletAdapter.remove(position)
                    mWalletAdapter.add(0,item!!)
                }
            }

            val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(this)
            val itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
            itemTouchHelper.attachToRecyclerView(rcv_wm_wallet)
            // open drag
            enableDragItem(itemTouchHelper, R.id.iv_drag_view, true);
            setOnItemDragListener(object : OnItemDragListener {
                override fun onItemDragStart(p0: RecyclerView.ViewHolder?, p1: Int) {
                }

                override fun onItemDragMoving(p0: RecyclerView.ViewHolder?, p1: Int, p2: RecyclerView.ViewHolder?, p3: Int) {
                }

                override fun onItemDragEnd(p0: RecyclerView.ViewHolder?, p1: Int) {
                    SWLog.e("onItemDragEnd() called with: p0 = $p0, p1 = $p1")
                }

            })
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_wallet_manger
    }

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        wm_title.apply {
            extInitCommonBgAutoBack(this@WalletMangerActivity, "管理钱包")
            rightTextView.text = "编辑"
            rightTextView.setTextColor(resources.getColor(R.color.main_blue))
            rightTextView.visibility = View.VISIBLE

            LibViewUtils.setViewTwoState(rightTextView, onChangeState1 = {
                rightTextView.text = "编辑"
                mWalletAdapter.isEdit = false
                mWalletAdapter.notifyDataSetChanged()

                var data = mutableListOf<WalletDao?>()
                for (item in mWalletAdapter.data) {
                    data.add(item.itemData)
                }
                WalletOperator.reSortByChain(data)
                EventBus.getDefault().post(CurrentWalletChange())
            }, onChangeState2 = {
                rightTextView.text = "完成"
                mWalletAdapter.isEdit = true
                mWalletAdapter.notifyDataSetChanged()
            })
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        rcv_wm_walleticon.apply {
            layoutManager = LinearLayoutManager(this@WalletMangerActivity)
            adapter = walletTypeAdapter
        }

        rcv_wm_wallet.apply {
            layoutManager = LinearLayoutManager(this@WalletMangerActivity)
            adapter = mWalletAdapter
        }

        val footer: View? = LayoutInflater.from(this)?.inflate(R.layout.layout_wallet_footer, rcv_wm_wallet, false)
        val tv_title = footer?.findViewById<TextView>(R.id.tv_title)
        tv_title?.setText(resources.getString(R.string.tjqb))
        mWalletAdapter.setFooterView(footer)
        footer!!.setOnClickListener{
            extStartActivity(ImportAccountActivity::class.java)
        }

//        tv_add_wallet.setOnClickListener {
//            extStartActivity(AddWalletActivity::class.java)
//        }

        var walletInfo: WalletDao = WalletOperator.currentWallet!!
        val nodeInfo = ChainNodeOperator.queryCurrentNode(walletInfo.chainType!!)
        tv_current_chainname.setText(walletInfo.chainType!!)
        tv_current_chaintype_fullname.setText(nodeInfo.nodeName)

        tv_changenetwork.setOnClickListener{
            SelectWalletNetworkListDialog(this).apply {
                changeListener = object : SelectWalletNetworkListDialog.IChainChangeListener {
                    override fun chainChange(position:Int) {
                        if (walletTypeAdapter.getItem(position)!!.name.equals("All")) {
                            mWalletAdapter.setNewData(getAllWallet())
                        } else {
                            val addrs = WalletOperator.queryWalletWithChainType(walletTypeAdapter.getItem(position)!!.name)
                            if (addrs.checkNotEmpty()) {
                                var data = mutableListOf<WalletListAdapter.Item>()
                                for (addr in addrs) {
                                    data.add(WalletListAdapter.Item(3, addr))
                                }
                                mWalletAdapter.setNewData(data)
                            } else {
                                mWalletAdapter.setNewData(mutableListOf())
                            }
                        }
                    }

                }
            }.show()
        }

        var current_index = 0
        for (item in chainlist){
            if (item.name.equals(walletInfo.chainType)){
                walletTypeAdapter.setOnItemClick(null, current_index)
            }
            current_index++
        }
    }

    fun getAllWallet(): MutableList<WalletListAdapter.Item> {
        var list = mutableListOf<WalletListAdapter.Item>()
        list.add(WalletListAdapter.Item(1, null))

        for (greWalletModel in WalletOperator.queryMainWallet()) {
            list.add(WalletListAdapter.Item(3, greWalletModel))
        }


        list.add(WalletListAdapter.Item(4, null))

        val queryOtherWallet = WalletOperator.queryOtherWallet()
        if (queryOtherWallet.checkNotEmpty()) {
            list.add(WalletListAdapter.Item(2, null))
            for (greWalletModel in queryOtherWallet) {
                list.add(WalletListAdapter.Item(3, greWalletModel))
            }
        }

        return list

    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: WalletInfoChangeEvent) {
        initView()
    }


}