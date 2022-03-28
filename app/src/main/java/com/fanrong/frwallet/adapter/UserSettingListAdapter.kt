package com.fanrong.frwallet.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.ChainInfo
import xc.common.viewlib.utils.extInvisibleOrVisible

class UserSettingListAdapter :   BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_seting_lan) {

    override fun convert(helper: BaseViewHolder, item: String) {

        val iv_select = helper.itemView.findViewById<ImageView>(R.id.iv_select)
        val tv_select = helper.itemView.findViewById<TextView>(R.id.tv_select)
        iv_select.extInvisibleOrVisible(helper.layoutPosition == mPosition)
        tv_select.text=item
    }
    private var mPosition = 0


    fun setmPosition(mPosition: Int) {
        this.mPosition = mPosition
    }
}