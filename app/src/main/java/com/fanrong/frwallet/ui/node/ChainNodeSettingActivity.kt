package com.fanrong.frwallet.ui.node

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import kotlinx.android.synthetic.main.node_setting_activity.*
import xc.common.kotlinext.extStartActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack


class ChainNodeSettingActivity : BaseActivity() {

    val nodeAdapter: ChainNodeAdapter by lazy {
        ChainNodeAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                extStartActivity(ChangeNodeActivity::class.java, Bundle().apply {
                    putString(FrConstants.CHAIN_TYPE, nodeAdapter.getItem(position))
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

        var nodes = mutableListOf<String>()
        for (suportChain in FrConstants.suport_chains) {
            nodes.add(suportChain.name)
        }
        nodeAdapter.setNewData(nodes)
    }

    override fun loadData() {
    }

    class ChainNodeAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.node_chain_type) {
        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.tv_chain_name, item)
        }


    }
}