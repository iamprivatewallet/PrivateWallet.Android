package com.fanrong.frwallet.ui.contract

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.ConfigTokenDao
import com.fanrong.frwallet.tools.extFormatAddr
import com.fanrong.otherlib.glide.noCacheLoad
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils

class ContractAssetAdapter : BaseQuickAdapter<ConfigTokenDao, BaseViewHolder>(R.layout.asset_item) {

    var split = mutableListOf<String>()

    open val addId = R.id.iv_add

    override fun convert(helper: BaseViewHolder, item: ConfigTokenDao) {
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(helper.getView<ImageView>(R.id.iv_coin_icon)).noCacheLoad(item.tokenLogo).apply(options).into(helper.getView<ImageView>(R.id.iv_coin_icon))
        helper.setText(R.id.tv_name, item.tokenSymbol)
        helper.setText(R.id.tv_contract, item.tokenContract!!.extFormatAddr())

        if (split.contains(item.tokenChain+"_"+item.tokenSymbol)) {
            helper.setImageResource(R.id.iv_add, R.mipmap.con_delectfromlist)
        } else {
            helper.setImageResource(R.id.iv_add, R.mipmap.icon_addtolist)
        }
        helper.addOnClickListener(R.id.iv_add)
        helper.getView<ImageView>(R.id.iv_add).isClickable = !split.contains(item.tokenSymbol)

        val tv_contract = helper.getView<TextView>(R.id.tv_contract)
        tv_contract.setOnClickListener{

        }

        val addressView = helper.getView<TextView>(R.id.tv_contract)
        if (addressView != null){
            addressView.setOnClickListener{
                LibAppUtils.copyText(item.tokenContract!!)
                showToast("复制成功")
            }
        }

    }
}