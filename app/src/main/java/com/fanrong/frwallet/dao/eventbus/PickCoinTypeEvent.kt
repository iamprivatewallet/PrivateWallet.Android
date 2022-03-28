package com.fanrong.frwallet.dao.eventbus

import com.fanrong.frwallet.dao.ChainInfo

class PickCoinTypeEvent {

    val addr:MutableList<ChainInfo>

    constructor(addr: MutableList<ChainInfo>) {
        this.addr = addr
    }
}