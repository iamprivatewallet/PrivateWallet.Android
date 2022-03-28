package com.fanrong.frwallet.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.ConfigTokenDao

class SearchTJAdapter: BaseQuickAdapter<ConfigTokenDao, BaseViewHolder>(R.layout.item_search_tj) {

    override fun convert(helper: BaseViewHolder, item: ConfigTokenDao) {
        //iv_chainicon
        //tv_chainmame
        helper.setText(R.id.tv_chainmame,item.tokenName)

        var iv_chainicon = helper.getView<ImageView>(R.id.iv_chainicon)
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(iv_chainicon).load(item.tokenLogo).apply(options).into(iv_chainicon)
    }
}