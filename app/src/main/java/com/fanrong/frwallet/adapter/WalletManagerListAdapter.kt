package com.fanrong.frwallet.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.DappStarDao
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.extFormatAddr
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils

class WalletManagerListAdapter : BaseItemDraggableAdapter<WalletListAdapter.Item, BaseViewHolder>(R.layout.wallet_manager_addr_type2, mutableListOf()) {
    var isEdit:Boolean = false
    open var iv_totop = R.id.iv_totop

    override fun convert(helper: BaseViewHolder, item: WalletListAdapter.Item) {
        helper.setText(R.id.tv_wm_coinname, item.itemData?.walletName ?: CoinNameCheck.getNameByName(item.itemData!!.chainType))
        helper.setText(R.id.tv_wm_coinaddress, item.itemData!!.address!!.extFormatAddr())
        helper.setVisible(R.id.iv_operation, "1".equals(item.itemData!!.isCurrentWallet))


        val addressView = helper.getView<TextView>(R.id.tv_wm_coinaddress)
        if (addressView != null){
            addressView.setOnClickListener{
                LibAppUtils.copyText(item.itemData!!.address!!)
                showToast("复制成功")
            }
        }

        val iv_totop = helper.getView<ImageView>(R.id.iv_totop)
        val iv_drag_view = helper.getView<ImageView>(R.id.iv_drag_view)
        if (isEdit){
            if (iv_totop != null){
                iv_totop.visibility = View.VISIBLE
            }
            if (iv_drag_view != null){
                iv_drag_view.visibility = View.VISIBLE
            }
        }else{
            if (iv_totop != null){
                iv_totop.visibility = View.GONE
            }
            if (iv_drag_view != null){
                iv_drag_view.visibility = View.GONE
            }
        }

        helper.addOnClickListener(R.id.iv_totop)

    }
}