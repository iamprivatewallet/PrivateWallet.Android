package com.fanrong.frwallet.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.tools.CoinNameCheck

class SelectCoinAdapter: BaseQuickAdapter<CoinDao, BaseViewHolder>(R.layout.item_select_coin) {
    var current_Select_coin:CoinDao? = null
    override fun convert(helper: BaseViewHolder, item: CoinDao) {
        val iv_icon = helper.getView<ImageView>(R.id.iv_icon)
        val iv_coinselect_state = helper.getView<ImageView>(R.id.iv_coinselect_state)

        Glide.with(iv_icon).load(item?.getTokenIcon()).into(iv_icon)

        helper.setText(R.id.tv_coinname, CoinNameCheck.getNameByName(item.coin_name))
        helper.setText(R.id.tv_chainname,item.chain_name)

        if (item.contract_addr.equals(current_Select_coin?.contract_addr)){
            iv_coinselect_state.setImageResource(R.mipmap.icon_select_coin)
        }else{
            iv_coinselect_state.setImageResource(R.mipmap.icon_unselect_coin)
        }
    }

    fun setCurrentCoin(coin:CoinDao?){
        current_Select_coin = coin
    }
}