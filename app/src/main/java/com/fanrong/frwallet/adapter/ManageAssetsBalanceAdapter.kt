package com.fanrong.frwallet.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.tools.CoinNameCheck

class ManageAssetsBalanceAdapter : BaseQuickAdapter<CoinDao, BaseViewHolder>(R.layout.item_asset_manage) {

    override fun convert(helper: BaseViewHolder, item: CoinDao) {
        helper.setText(R.id.tv_ham_coinname, CoinNameCheck.getNameByName(item.coin_name))
        helper.setText(R.id.tv_ham_balance, item.balance)
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(helper.getView<ImageView>(R.id.iv_coin_icon)).load(item.coin_icon).apply(options).into(helper.getView<ImageView>(R.id.iv_coin_icon))
    }
}