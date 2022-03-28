package com.fanrong.frwallet.adapter

import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.SearchHistoryResult
import com.fanrong.frwallet.tools.extFormatAddr
import org.litepal.LitePal
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils

class SearchHistoryAdapter : BaseQuickAdapter<SearchHistoryResult, BaseViewHolder>(R.layout.item_search_history) {
    var split = mutableListOf<String>()
    override fun convert(helper: BaseViewHolder, item: SearchHistoryResult) {

        //iv_chainicon
        //tv_coinname
        //tv_contractaddress
        //iv_operation


        helper.setText(R.id.tv_coinname,item.tokenName)
        helper.setText(R.id.tv_contractaddress,item.tokenContract?.extFormatAddr())

        Glide.with(helper.getView<ImageView>(R.id.iv_chainicon)).load(item.tokenLogo).into(helper.getView<ImageView>(R.id.iv_chainicon))

        var iv_operation = helper.getView<ImageView>(R.id.iv_operation)
        if (split.contains(item.tokenChain+"_"+item.tokenSymbol)){
            iv_operation.setImageResource(R.mipmap.con_delectfromlist)
        }else{
            iv_operation.setImageResource(R.mipmap.icon_addtolist)
        }

        val addressView = helper.getView<TextView>(R.id.tv_contractaddress)
        if (addressView != null){
            addressView.setOnClickListener{
                LibAppUtils.copyText(item.tokenContract!!)
                showToast("复制成功")
            }
        }

        helper.addOnClickListener(R.id.iv_operation)
    }

    fun delectItem(item:SearchHistoryResult){
        LitePal.delete(SearchHistoryResult::class.java,item.id)
    }

}