package com.fanrong.frwallet.ui.dapp.star

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basiclib.base.BaseActivity
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback
import com.chad.library.adapter.base.listener.OnItemDragListener
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.ManageAssetsBalanceAdapter1
import com.fanrong.frwallet.dao.database.DappInfoDao
import com.fanrong.frwallet.dao.database.DappStarOperator
import com.fanrong.frwallet.dao.eventbus.DappHistoryEvent
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import kotlinx.android.synthetic.main.activity_home_asset_manage.*
import kotlinx.android.synthetic.main.dapp_star_list_activity.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.LibViewUtils
import xc.common.tool.utils.SWLog


class DappStarListActivity : BaseActivity() {

    val itemAdapter: DappStarAdapter by lazy {
        DappStarAdapter().apply {
            this.onitemDelectListener = object :
                ManageAssetsBalanceAdapter1.ItemDelectListener {
                override fun onDelect(position: Int) {
                    DappStarOperator.delete(itemAdapter.getItem(position))
                    EventBus.getDefault().post(DappHistoryEvent())
                    loadData()
                }
            }

            setOnItemClickListener { adapter, view, position ->

                val item = itemAdapter.getItem(position)!!
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putSerializable(DappBrowserActivity.PARAMS_DAPP, DappInfoDao().apply {
                        this.icon = item.icon
                        this.name = item.name
                        this.des = item.des
                        this.url = item.url
                    })
                })
            }


            val itemDragAndSwipeCallback = ItemDragAndSwipeCallback(this)
            val itemTouchHelper = ItemTouchHelper(itemDragAndSwipeCallback)
            itemTouchHelper.attachToRecyclerView(recyclerview)
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
        return R.layout.dapp_star_list_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@DappStarListActivity, "收藏")
            rightTextView.text = "编辑"
            rightTextView.setTextColor(resources.getColor(R.color.main_blue))
            rightTextView.visibility = View.VISIBLE

            LibViewUtils.setViewTwoState(rightTextView, onChangeState1 = {
                rightTextView.text = "编辑"
                itemAdapter.isEdit = false
                DappStarOperator.resort(itemAdapter.data)
                itemAdapter.notifyDataSetChanged()
                EventBus.getDefault().post(DappHistoryEvent())

            }, onChangeState2 = {
                rightTextView.text = "完成"
                itemAdapter.notifyDataSetChanged()
                itemAdapter.isEdit = true
            })
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@DappStarListActivity)
            adapter = itemAdapter
        }
    }

    override fun loadData() {
        itemAdapter.setNewData(DappStarOperator.queryStar())
    }


    override fun onRestart() {
        super.onRestart()
        loadData()
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}