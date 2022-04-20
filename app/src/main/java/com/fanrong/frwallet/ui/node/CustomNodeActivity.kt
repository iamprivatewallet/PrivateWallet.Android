package com.fanrong.frwallet.ui.node

import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.ChainNodeDao
import com.fanrong.frwallet.dao.database.ChainNodeOperator
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.showTopToast
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
            extInitCommonBgAutoBack(this@CustomNodeActivity, getString(R.string.tjzdjwl))

            setRightTextClickListener(getString(R.string.save)) {
                nodeInfo = ChainNodeDao()
                nodeInfo!!.run {
                    chainType = intent.getStringExtra(FrConstants.CHAIN_TYPE)
                    nodeName = set_wlmc.et_content.text.toString()
                    nodeUrl = set_rpc.et_content.text.toString()
                    chainId = set_chainid.et_content.text.toString()
                    symbol = set_symbol.et_content.text.toString()
                    browser = set_qkllq.et_content.text.toString()
                    isCurrent = 0
                    netType = 2
                }


                if (nodeInfo!!.nodeName.checkIsEmpty()) {
                    showTopToast(this@CustomNodeActivity,getString(R.string.mc_hint),false)
                    return@setRightTextClickListener
                }
                if (nodeInfo!!.nodeUrl.checkIsEmpty()) {
                    showTopToast(this@CustomNodeActivity,getString(R.string.qsrrpcdz),false)
                    return@setRightTextClickListener
                }
                if (nodeInfo!!.chainId.checkIsEmpty()) {
                    showTopToast(this@CustomNodeActivity,getString(R.string.qsrchainid),false)
                    return@setRightTextClickListener
                }
                var _type = intent.getStringExtra(FrConstants.CHAIN_TYPE)
                viewmodel.verifyNode(
                    _type , nodeInfo!!.nodeUrl!!, nodeInfo!!.chainId!!
                )
            }
        }

        val nodeInfo = intent.getSerializableExtra(FrConstants.PARAMS_NODE_INFO)
        if (nodeInfo != null) {
            val chainNodeDao = nodeInfo as ChainNodeDao
            set_wlmc.et_content.setText(chainNodeDao.nodeName)
            set_rpc.et_content.setText(chainNodeDao.nodeUrl)
            set_chainid.et_content.setText(chainNodeDao.chainId)
            set_symbol.et_content.setText(chainNodeDao.symbol)
            set_qkllq.et_content.setText(chainNodeDao.browser)
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
        if (nodeInfo == null){
            ll_firstsave.visibility = View.VISIBLE
        }else{
            ll_firstsave.visibility = View.GONE
        }

        btn_cancel.setOnClickListener{
            extFinishWithAnim()
        }
        btn_save.setOnClickListener{
            saveData()
        }

        viewmodel.observerDataChange(this, this::stateChange)
    }

    fun saveData(){
        nodeInfo = ChainNodeDao()
        nodeInfo!!.run {
            chainType = intent.getStringExtra(FrConstants.CHAIN_TYPE)
            nodeName = set_wlmc.et_content.text.toString()
            nodeUrl = set_rpc.et_content.text.toString()
            chainId = set_chainid.et_content.text.toString()
            symbol = set_symbol.et_content.text.toString()
            browser = set_qkllq.et_content.text.toString()
            isCurrent = 0
            netType = 2
        }

        if (nodeInfo!!.nodeName.checkIsEmpty()) {
            showTopToast(this,getString(R.string.qsuwlmc),false)
            return
        }
        if (nodeInfo!!.nodeUrl.checkIsEmpty()) {
            showTopToast(this,getString(R.string.qsrrpcdz),false)
            return
        }
        if (nodeInfo!!.chainId.checkIsEmpty()) {
            showTopToast(this,getString(R.string.qsrchainid),false)
            return
        }

        viewmodel.verifyNode(
            intent.getStringExtra(FrConstants.CHAIN_TYPE), nodeInfo!!.nodeUrl!!, nodeInfo!!.chainId!!
        )
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
                    showTopToast(this@CustomNodeActivity,getString(R.string.yczxtjd),false)
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