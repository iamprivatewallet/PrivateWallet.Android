package com.fanrong.frwallet.ui.address

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.tools.CoinNameCheck
import xc.common.viewlib.utils.extInvisibleOrVisible

class AddAddressTypeAdapter : BaseQuickAdapter<ChainInfo, BaseViewHolder>(R.layout.item_select_addr_type) {

    var current = ""

    override fun convert(helper: BaseViewHolder, item: ChainInfo) {
        val iv_coin_icon = helper.itemView.findViewById<ImageView>(R.id.iv_coin_icon)
        val iv_select = helper.itemView.findViewById<ImageView>(R.id.iv_select)
        helper.setText(R.id.tv_wm_coinname, item.name)
        helper.setText(R.id.tv_wm_coinaddress,item.fullName)
//        iv_coin_icon.setImageResource(item.icon)  fullName
        Glide.with(iv_coin_icon).load(CoinNameCheck.getCoinImgUrl2(item.name)).into(iv_coin_icon)
        //点击触发
        if (item.name.equals(current)) {
            iv_select.setImageResource(R.mipmap.src_lib_eui_icon_checkboxchecked)
        }
        iv_select.extInvisibleOrVisible(item.name.equals(current))
    }
}