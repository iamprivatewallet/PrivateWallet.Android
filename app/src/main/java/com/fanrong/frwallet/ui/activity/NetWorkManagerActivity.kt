package com.fanrong.frwallet.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import com.fanrong.frwallet.adapter.NetWorkManagerAdapter
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.address.AddressListAdapter
import com.fanrong.frwallet.ui.address.EditAddrDialog
import com.fanrong.frwallet.ui.node.CustomNodeActivity
import kotlinx.android.synthetic.main.activity_network_manager.*
import kotlinx.android.synthetic.main.activity_wallet_manger.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.LibViewUtils
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.checkNotEmpty

class NetWorkManagerActivity : BaseActivity()  {
    val netAdapter: NetWorkManagerAdapter by lazy {
        NetWorkManagerAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val item = netAdapter.getItem(position)
                if (!netAdapter.isEdit){
                    extStartActivity(CustomNodeActivity::class.java, Bundle().apply {
                        putString(FrConstants.CHAIN_TYPE, item!!.chainType)
                        putSerializable(FrConstants.PARAMS_NODE_INFO,netAdapter.getItem(position)!!)
                    })
                }
            }
            setOnItemChildClickListener{adapter, view, position ->
                if (view.id == netAdapter.dragId){
                    //置顶
                    val item = netAdapter.getItem(position)
                    netAdapter.remove(position)
                    netAdapter.add(0,item!!)
                }else if(view.id == netAdapter.delectId){
                    netAdapter.remove(position)
                    netAdapter.notifyDataSetChanged()
                }
            }

            val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(this)
            val itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
            itemTouchHelper.attachToRecyclerView(recyclerview)
            // open drag
            enableDragItem(itemTouchHelper, R.id.iv_drag, true);
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
        return R.layout.activity_network_manager
    }

    override fun initView() {
        wim_title.apply {
            setTitleText(getString(R.string.wlgl))
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }

        }

        wim_title.apply {
            extInitCommonBgAutoBack(this@NetWorkManagerActivity, getString(R.string.wlgl))
            rightTextView.text = getString(R.string.bj)
            rightTextView.setTextColor(resources.getColor(R.color.main_blue))
            rightTextView.visibility = View.VISIBLE
            LibViewUtils.setViewTwoState(rightTextView, onChangeState1 = {
                rightTextView.text = getString(R.string.bj)
                netAdapter.isEdit = false
                netAdapter.notifyDataSetChanged()

                var data = mutableListOf<ChainNodeDao>()
                for (item in netAdapter.data) {
                    data.add(item)
                }
                ChainNodeOperator.reSort(data)
            }, onChangeState2 = {
                rightTextView.text = getString(R.string.wc)
                netAdapter.isEdit = true
                netAdapter.notifyDataSetChanged()
            })
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }


        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@NetWorkManagerActivity)
            adapter = netAdapter
        }

        val footer: View? = LayoutInflater.from(this)?.inflate(R.layout.layout_wallet_footer, recyclerview, false)
        val tv_title = footer?.findViewById<TextView>(R.id.tv_title)
        tv_title?.setText(resources.getString(R.string.wlgl))
        netAdapter.setFooterView(footer)
        footer!!.setOnClickListener{
            extStartActivity(CustomNodeActivity::class.java, Bundle().apply {
                putString(FrConstants.CHAIN_TYPE, "ETH")
            })
        }

    }

    override fun loadData() {
        val queryAllList = ChainNodeOperator.queryAllList()
        netAdapter.setNewData(queryAllList)
        netAdapter.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
        val queryAllList = ChainNodeOperator.queryAllList()
        netAdapter.setNewData(queryAllList)
        netAdapter.notifyDataSetChanged()
    }


}