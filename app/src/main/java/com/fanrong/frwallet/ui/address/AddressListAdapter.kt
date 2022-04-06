package com.fanrong.frwallet.ui.address

import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.AddressDao
import com.fanrong.frwallet.tools.CoinNameCheck
import xc.common.tool.utils.checkNotEmpty

class AddressListAdapter : BaseQuickAdapter<AddressDao, BaseViewHolder>(R.layout.item_address_list) {
    var isEdit:Boolean = false
    open val delectId = R.id.iv_delect
    override fun convert(helper: BaseViewHolder, item: AddressDao) {
        helper.setText(R.id.tv_wm_coinname, item.name)
        helper.setText(R.id.tv_wm_coinaddress, item.address)
        helper.setText(R.id.tv_des, item.remark)
        helper.setGone(R.id.tv_des, item.remark.checkNotEmpty())

//        val view = helper.getView<ImageView>(R.id.iv_coin_icon)
//        Glide.with(view).load(CoinNameCheck.getCoinImgUrl2(item?.type!!)).into(view)

        //tv_chaintype
        //iv_delect
        helper.addOnClickListener(R.id.iv_delect)
        val iv_delect = helper.getView<ImageView>(R.id.iv_delect)

        if (isEdit){
            iv_delect.visibility = View.VISIBLE
        }else{
            iv_delect.visibility = View.GONE
        }

        helper.setText(R.id.tv_chaintype,item.type)

    }
}