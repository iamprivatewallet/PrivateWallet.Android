package com.fanrong.frwallet.ui.receipt

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import java.io.Serializable

class SetGasAdapter : BaseQuickAdapter<SetGasAdapter.Item, BaseViewHolder>(R.layout.set_gas_item) {

    class Item : Serializable {
        var speed: String
        var speedTime: String
        var gasPrice: String
        var gasLimit: String = "21000"

        constructor(speed: String, speedTime: String, gas: String,gasLimit:String) {
            this.speed = speed
            this.speedTime = speedTime
            this.gasPrice = gas
            this.gasLimit = gasLimit
        }
    }

    var current = 0

    override fun convert(helper: BaseViewHolder, item: SetGasAdapter.Item) {
        helper.setVisible(R.id.iv_select, helper.adapterPosition == current)

        helper.setText(R.id.tv_gas_price, item.gasPrice)
        helper.setText(R.id.tv_speed, item.speed)
        helper.setText(R.id.tv_speed_time, item.speedTime)
    }
}