package com.fanrong.frwallet.ui.node

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.FrConstants
import kotlinx.android.synthetic.main.node_setting_activity.*
import xc.common.kotlinext.extStartActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog


class ChainNodeSettingActivity : BaseActivity() {

    val nodeAdapter: ChainNodeAdapter by lazy {
        ChainNodeAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                extStartActivity(ChangeNodeActivity::class.java, Bundle().apply {
                    putString(FrConstants.CHAIN_TYPE, nodeAdapter.getItem(position)!!.name)
                })
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.node_setting_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@ChainNodeSettingActivity, "节点设置")

        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@ChainNodeSettingActivity)
            adapter = nodeAdapter
        }

//        var nodes = mutableListOf<String>()
//        for (suportChain in FrConstants.suport_chains) {
//            nodes.add(suportChain.name)
//        }
        nodeAdapter.setNewData(FrConstants.suport_chains)
    }

    override fun loadData() {
    }

    class ChainNodeAdapter : BaseQuickAdapter<ChainInfo, BaseViewHolder>(R.layout.node_chain_type) {
        override fun convert(helper: BaseViewHolder, item: ChainInfo) {
            helper.setText(R.id.tv_chain_name, item.name)
            helper.setText(R.id.tv_node_name,item.fullName)
        }


    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}