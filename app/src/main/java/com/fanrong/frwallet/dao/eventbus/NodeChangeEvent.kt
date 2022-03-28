package com.fanrong.frwallet.dao.eventbus

import com.fanrong.frwallet.dao.database.ChainNodeDao

class NodeChangeEvent {
    var nodeInfo:ChainNodeDao

    constructor(nodeInfo: ChainNodeDao) {
        this.nodeInfo = nodeInfo
    }
}