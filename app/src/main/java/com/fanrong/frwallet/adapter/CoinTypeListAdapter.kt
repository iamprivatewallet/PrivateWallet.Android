package com.fanrong.frwallet.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.FrMoneyUnit
import com.fanrong.frwallet.tools.extToFiatMoney
import xc.common.tool.utils.SPUtils

class CoinTypeListAdapter : BaseQuickAdapter<CoinDao, BaseViewHolder>(R.layout.item_coin_type_list) {

    override fun convert(helper: BaseViewHolder, item: CoinDao) {
        helper.setText(R.id.tv_coin_name_cl, CoinNameCheck.getNameByName(item.coin_name))//CoinNameCheck  item.coin_name
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(helper.getView<ImageView>(R.id.iv_coin_cl)).load(item.coin_icon).apply(options).into(helper.getView<ImageView>(R.id.iv_coin_cl))
        if (SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)){
            helper.setText(R.id.tv_balance, "****")
            helper.setText(R.id.tv_balance_cny, "****")
        }else{
            helper.setText(R.id.tv_balance, item.balance)
            helper.setText(R.id.tv_balance_cny, FrMoneyUnit.getSymbal() + item.balance.extToFiatMoney(item.price))
        }

        helper.setText(R.id.tv_coin_price,FrMoneyUnit.getSymbal() + item.price)

    }
}