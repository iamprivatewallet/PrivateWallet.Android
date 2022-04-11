package com.fanrong.frwallet.adapter

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.ChainNodeDao
import com.fanrong.frwallet.dao.database.ConfigTokenDao
import com.fanrong.frwallet.tools.CoinNameCheck

class NetWorkManagerAdapter : BaseItemDraggableAdapter<ChainNodeDao, BaseViewHolder>(R.layout.item_networkmanager, mutableListOf()) {
    var isEdit:Boolean = false
    var dragId = R.id.iv_totop
    var delectId = R.id.iv_delect
    override fun convert(helper: BaseViewHolder, item: ChainNodeDao) {

        //iv_chainicon
        //tv_chainmame
        val iv_chainicon = helper.getView<ImageView>(R.id.iv_chainicon)
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(iv_chainicon).load(CoinNameCheck.getCoinImgUrl2(item.chainType!!)).apply(options).into(iv_chainicon)

        helper.setText(R.id.tv_chainmame,item.chainType)

        val iv_delect = helper.getView<ImageView>(R.id.iv_delect)
        val iv_next = helper.getView<ImageView>(R.id.iv_next)
        val iv_totop = helper.getView<ImageView>(R.id.iv_totop)
        val iv_drag = helper.getView<ImageView>(R.id.iv_drag)

        if (isEdit){
            iv_delect.visibility = View.VISIBLE
            iv_next.visibility = View.GONE
            iv_totop.visibility = View.VISIBLE
            iv_drag.visibility = View.VISIBLE
        }else{
            iv_delect.visibility = View.GONE
            iv_next.visibility = View.VISIBLE
            iv_totop.visibility = View.GONE
            iv_drag.visibility = View.GONE
        }

        helper.addOnClickListener(R.id.iv_totop)
        helper.addOnClickListener(R.id.iv_delect)
    }
}