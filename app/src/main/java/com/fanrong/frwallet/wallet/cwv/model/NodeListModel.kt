package com.fanrong.frwallet.wallet.cwv.model

import com.fanrong.frwallet.wallet.cwv.Constants
import xc.common.tool.utils.checkNotEmpty
import java.io.Serializable

data class NodeListReq(var node_net: String) : Serializable {
    var dapp_id: String = Constants.DAPP_ID

}

data class NodeListResp(var err_code: String) : Serializable {

    var msg: String? = null

    // 自定义字段
    var nodeInfo: List<String>? = null
        get() {
            var listNode = mutableListOf<String>()
            if (nodes.checkNotEmpty()) {
                for (node in nodes!!) {
                    listNode.add(node.node_url!!)
                }
            }
            return listNode
        }
    var nodes: List<ServiceNode>? = null
}

class ServiceNode : Serializable {
    var node_url: String? = null
}

data class NodeModel(
    val node_url: String
) : Serializable {
    var isUsing: Boolean? = null
        get() {
            if (field == null) {
                return false
            }
            return field
        }
    var node_des: String? = null
    var node_name: String? = null
    var node_net: String? = null
    var is_def: Boolean? = null
        get() {
            if (field == null) {
                return false
            }
            return field
        }

}