package com.fanrong.frwallet.ui.node

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.ChainNodeOperator
import com.fanrong.frwallet.dao.eventbus.NodeChangeEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.main.MainActivity
import kotlinx.android.synthetic.main.change_node_activity.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.AppManager


class ChangeNodeActivity : BaseActivity() {
    val listAdapter: NodeListAdapter by lazy {
        NodeListAdapter().apply {

            setOnItemChildClickListener { adapter, view, position ->
                if (view.id == (adapter as NodeListAdapter).moreViewId) {
                    extStartActivity(CustomNodeActivity::class.java, Bundle().apply {
                        putString(FrConstants.CHAIN_TYPE, intent.getStringExtra(FrConstants.CHAIN_TYPE))
                        putSerializable(FrConstants.PARAMS_NODE_INFO,listAdapter.getItem(position)!!)
                    })
                }
            }

            setOnItemClickListener { adapter, view, position ->
                ChainNodeOperator.changeNode(listAdapter.getItem(position)!!)
                EventBus.getDefault().post(NodeChangeEvent(listAdapter.getItem(position)!!))

                AppManager.getAppManager().findActivity(MainActivity::class.java)?.backHome()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.change_node_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@ChangeNodeActivity, "${intent.getStringExtra(FrConstants.CHAIN_TYPE)} 节点设置")
            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_addcontact) {
                extStartActivity(CustomNodeActivity::class.java, intent.extras!!)
            }
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@ChangeNodeActivity)
            adapter = listAdapter
        }
    }

    override fun loadData() {
        val get = ChainNodeOperator.queryNodeList(intent.getStringExtra(FrConstants.CHAIN_TYPE))
        listAdapter.setNewData(get)
    }

    override fun onRestart() {
        super.onRestart()
        loadData()
    }
}