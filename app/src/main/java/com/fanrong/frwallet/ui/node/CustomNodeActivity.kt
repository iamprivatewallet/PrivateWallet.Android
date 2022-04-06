package com.fanrong.frwallet.ui.node

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.ChainNodeDao
import com.fanrong.frwallet.dao.database.ChainNodeOperator
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import kotlinx.android.synthetic.main.custom_node_activity.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.utils.extGoneOrVisible


class CustomNodeActivity : BaseActivity() {

    var nodeInfo: ChainNodeDao? = null

    val viewmodel: NodeViewmodel by lazy {
        NodeViewmodel()
    }

    override fun getLayoutId(): Int {
        return R.layout.custom_node_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@CustomNodeActivity, "自定义节点")

            setRightTextClickListener("保存") {
                nodeInfo = ChainNodeDao()
                nodeInfo!!.run {
                    chainType = intent.getStringExtra(FrConstants.CHAIN_TYPE)
                    nodeName = et_node_name.text.toString()
                    nodeUrl = et_rpc_url.text.toString()
                    chainId = et_chain_id.text.toString()
                    symbol = et_symbol.text.toString()
                    browser = et_browser.text.toString()
                    isCurrent = 0
                    netType = 2
                }


                if (nodeInfo!!.nodeName.checkIsEmpty()) {
                    showToast("请输入节点名称")
                    return@setRightTextClickListener
                }
                if (nodeInfo!!.nodeUrl.checkIsEmpty()) {
                    showToast("请输入RPC地址")
                    return@setRightTextClickListener
                }
                if (nodeInfo!!.chainId.checkIsEmpty()) {
                    showToast("请输入ChainId")
                    return@setRightTextClickListener
                }

                viewmodel.verifyNode(
                    intent.getStringExtra(FrConstants.CHAIN_TYPE), nodeInfo!!.nodeUrl!!, nodeInfo!!.chainId!!
                )
            }
        }

        val nodeInfo = intent.getSerializableExtra(FrConstants.PARAMS_NODE_INFO)
        if (nodeInfo != null) {
            val chainNodeDao = nodeInfo as ChainNodeDao
            et_node_name.setText(chainNodeDao.nodeName)
            et_rpc_url.setText(chainNodeDao.nodeUrl)
            et_chain_id.setText(chainNodeDao.chainId)
            et_symbol.setText(chainNodeDao.symbol)
            et_browser.setText(chainNodeDao.browser)
        }
        btn_del.extGoneOrVisible(nodeInfo != null)
        btn_del.setOnClickListener {
            var nodeInfo = (intent.getSerializableExtra(FrConstants.PARAMS_NODE_INFO) as ChainNodeDao)

            if (nodeInfo.isCurrent == 1) {
                showToast("正在使用的节点不支持删除")
                return@setOnClickListener
            }
            ChainNodeOperator.delete(nodeInfo.id)
            extFinishWithAnim()
        }

        viewmodel.observerDataChange(this, this::stateChange)
    }

    fun stateChange(state: NodeViewmodel.State) {
        extShowOrDismissDialog(state.isShowLoading)
        state.errorinfo?.run {
            showToast(msg)
        }

        state.verifyResult?.run {
            if (resultData ?: false) {

                val oldNode = intent.getSerializableExtra(FrConstants.PARAMS_NODE_INFO)
                var operatorResult = false
                if (oldNode == null) {
                    operatorResult = ChainNodeOperator.addNode(nodeInfo!!)
                } else {
                    operatorResult = ChainNodeOperator.updateNode(nodeInfo!!, (oldNode as ChainNodeDao).id)
                }
                if (operatorResult) {
                    extFinishWithAnim()
                } else {
                    showToast("已存在相同配置节点")
                }
            }
        }
    }

    override fun loadData() {
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}