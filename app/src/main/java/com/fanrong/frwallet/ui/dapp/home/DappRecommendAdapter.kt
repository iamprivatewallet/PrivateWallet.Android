package com.fanrong.frwallet.ui.dapp.home

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.wallet.eth.eth.QueryDappRespNew

class DappRecommendAdapter : BaseQuickAdapter<QueryDappRespNew.dappItem, BaseViewHolder>(R.layout.dapp_recommend_item) {
    override fun convert(helper: BaseViewHolder, item: QueryDappRespNew.dappItem) {
        helper.setText(R.id.tv_dapp_name, item.appName)
        helper.setText(R.id.tv_dapp_des, item.description)
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(helper.getView<ImageView>(R.id.iv_image)).load(item.iconUrl).apply(options).into(helper.getView<ImageView>(R.id.iv_image))
        helper.setText(R.id.tv_dapp_name, item.appName)

        //src_lib_eui_icon_defaultdappicon
    }
}