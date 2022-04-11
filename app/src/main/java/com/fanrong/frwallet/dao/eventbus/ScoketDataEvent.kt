package com.fanrong.frwallet.dao.eventbus

import com.fanrong.frwallet.dao.data.marketDataBean

class ScoketDataEvent {
    var data:marketDataBean? = null
    constructor(_data: marketDataBean){
        data = _data;
    }
}