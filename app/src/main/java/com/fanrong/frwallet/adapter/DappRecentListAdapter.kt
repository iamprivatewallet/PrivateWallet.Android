package com.fanrong.frwallet.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.WebIconConfig
import com.fanrong.frwallet.tools.bitMapAndStringConvertUtil

class DappRecentListAdapter : BaseQuickAdapter<WebIconConfig, BaseViewHolder>(R.layout.item_dapp_recent) {
    override fun convert(helper: BaseViewHolder, item: WebIconConfig) {
//        var options: RequestOptions =  RequestOptions()
//            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
//            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
//            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
//        Glide.with(helper.getView<ImageView>(R.id.iv_coin_cl)).load(item.).apply(options).into(helper.getView<ImageView>(R.id.iv_coin_cl))

        val convertStringToIcon = bitMapAndStringConvertUtil.convertStringToIcon(item.iconStr)
        val view = helper.getView<ImageView>(R.id.iv_coin_cl)
        if (convertStringToIcon != null)
            view.setImageBitmap(convertStringToIcon)
        else
            view.setImageResource(R.mipmap.src_lib_eui_icon_defaultdappicon)
        helper.setText(R.id.tv_title,item.title)
        helper.setText(R.id.tv_url,item.webUrl)
    }
}