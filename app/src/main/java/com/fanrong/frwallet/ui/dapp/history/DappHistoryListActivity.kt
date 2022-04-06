package com.fanrong.frwallet.ui.dapp.history

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.ManageAssetsBalanceAdapter1
import com.fanrong.frwallet.dao.database.DappHistoryOperator
import com.fanrong.frwallet.dao.database.DappInfoDao
import com.fanrong.frwallet.dao.eventbus.DappHistoryEvent
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import kotlinx.android.synthetic.main.dapp_star_list_activity.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.LibViewUtils


class DappHistoryListActivity : BaseActivity() {

    val itemAdapter: DappHistoryAdapter by lazy {
        DappHistoryAdapter().apply {
            this.onitemDelectListener = object :
                ManageAssetsBalanceAdapter1.ItemDelectListener {
                override fun onDelect(position: Int) {
                    DappHistoryOperator.delete(itemAdapter.getItem(position))
                    EventBus.getDefault().post(DappHistoryEvent())
                    loadData()
                }
            }
            setOnItemClickListener { adapter, view, position ->
                if (!itemAdapter.isEdit) {
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
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.dapp_star_list_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@DappHistoryListActivity, "最近浏览")
            rightTextView.text = "编辑"
            rightTextView.setTextColor(resources.getColor(R.color.main_blue))
            rightTextView.visibility = View.VISIBLE

            LibViewUtils.setViewTwoState(rightTextView, onChangeState1 = {
                rightTextView.text = "编辑"
                itemAdapter.isEdit = false
                itemAdapter.notifyDataSetChanged()
            }, onChangeState2 = {
                rightTextView.text = "完成"
                itemAdapter.isEdit = true
                itemAdapter.notifyDataSetChanged()
            })
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@DappHistoryListActivity)
            adapter = itemAdapter
        }
    }

    override fun loadData() {
        itemAdapter.setNewData(DappHistoryOperator.queryDappSortByTime())
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