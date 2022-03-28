package com.fanrong.frwallet.adapter

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.tools.CoinNameCheck

class AddConinTypesAdapter : BaseQuickAdapter<ChainInfo, BaseViewHolder>(R.layout.item_add_cointypes) {

    override fun convert(helper: BaseViewHolder, item: ChainInfo) {
        val iv_coin_icon = helper.itemView.findViewById<ImageView>(R.id.iv_coin_icon)
        val iv_select = helper.itemView.findViewById<ImageView>(R.id.iv_select)
        val tv_wm_coinaddress = helper.itemView.findViewById<TextView>(R.id.tv_wm_coinaddress)

        helper.setText(R.id.tv_wm_coinname, item.name)
//        iv_coin_icon.setImageResource(item.icon)
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(iv_coin_icon).load(CoinNameCheck.getCoinImgUrl2(item.name)).apply(options).into(iv_coin_icon)
        iv_select.setImageResource(R.mipmap.src_lib_eui_icon_checkboxdisable)

        tv_wm_coinaddress.setText(item.fullName)

        if (item.isDefaultSupport) {
            iv_select.setImageResource(R.mipmap.src_lib_eui_icon_checkboxdisable)
        } else {
            if (addCoins.contains(helper.adapterPosition)) {
                iv_select.setImageResource(R.mipmap.src_lib_eui_icon_checkboxchecked)
            } else {
                iv_select.setImageResource(R.mipmap.src_lib_eui_icon_checkboxunchecked)
            }
        }
    }


    var addCoins = mutableListOf<Int>()

    fun getAddCoinsName(): MutableList<String> {
        return mutableListOf<String>().apply {
            for (addCoin in addCoins) {
                add(getItem(addCoin)!!.name)
            }
        }
    }

}