package com.fanrong.frwallet.ui.node

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.ChainNodeDao
import com.fanrong.frwallet.dao.database.ChainNodeOperator
import com.fanrong.frwallet.dao.eventbus.NodeChangeEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.activity.ImportAccountActivity
import com.fanrong.frwallet.ui.backup.BackUpHintActivity
import com.fanrong.frwallet.ui.dialog.AddNodeDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.dialog.PasswordDialog
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_wallet_manger.*
import kotlinx.android.synthetic.main.change_node_activity.*
import kotlinx.android.synthetic.main.change_node_activity.ac_title
import kotlinx.android.synthetic.main.custom_node_activity.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.AppManager
import xc.common.tool.utils.checkIsEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.view.customview.FullScreenDialog


class ChangeNodeActivity : BaseActivity() {
    var inputNodeUrl = ""
    val viewmodel: NodeViewmodel by lazy {
        NodeViewmodel()
    }
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
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        return R.layout.change_node_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@ChangeNodeActivity, "${intent.getStringExtra(FrConstants.CHAIN_TYPE)} 节点设置")
//            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_addcontact) {
//                extStartActivity(CustomNodeActivity::class.java, intent.extras!!)
//            }
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@ChangeNodeActivity)
            adapter = listAdapter
        }

        val footer: View? = LayoutInflater.from(this)?.inflate(R.layout.layout_wallet_footer, rcv_wm_wallet, false)
        val tv_title = footer?.findViewById<TextView>(R.id.tv_title)
        tv_title?.setText(resources.getString(R.string.tjzdyjd))
        listAdapter.setFooterView(footer)
        footer!!.setOnClickListener{
//            extStartActivity(CustomNodeActivity::class.java, intent.extras!!)
            AddNodeDialog(this).apply {
                onConfrim = object :
                    FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        inputNodeUrl = params as String
                        addNode()
                    }
                }
            }.show()
        }


        viewmodel.observerDataChange(this, this::stateChange)
    }
    var nodeInfo: ChainNodeDao? = null
    fun addNode(){
        nodeInfo = ChainNodeDao()
        val getlist = ChainNodeOperator.queryNodeList(intent.getStringExtra(FrConstants.CHAIN_TYPE))
        if (getlist!=null && getlist?.size!! > 0){
            var item = getlist.get(0)
            nodeInfo!!.run {
                chainType = intent.getStringExtra(FrConstants.CHAIN_TYPE)
                nodeName = item.nodeName
                nodeUrl = inputNodeUrl//item.nodeUrl
                chainId = item.chainId
                symbol = item.symbol
                browser = item.browser
                isCurrent = 0
                netType = 2
            }


            if (nodeInfo!!.nodeName.checkIsEmpty()) {
                showToast("请输入节点名称")
            }
            if (nodeInfo!!.nodeUrl.checkIsEmpty()) {
                showToast("请输入RPC地址")
            }
            if (nodeInfo!!.chainId.checkIsEmpty()) {
                showToast("请输入ChainId")
            }

            viewmodel.verifyNode(
                intent.getStringExtra(FrConstants.CHAIN_TYPE), nodeInfo!!.nodeUrl!!, nodeInfo!!.chainId!!
            )
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

    fun stateChange(state: NodeViewmodel.State) {
        extShowOrDismissDialog(state.isShowLoading)
        state.errorinfo?.run {
            showTopToast(this@ChangeNodeActivity,msg,false)
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
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}