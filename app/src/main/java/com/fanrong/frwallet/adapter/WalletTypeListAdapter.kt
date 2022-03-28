 package com.fanrong.frwallet.adapter

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import xc.common.viewlib.utils.extInvisibleOrVisible

class WalletTypeListAdapter :   BaseQuickAdapter<ChainInfo, BaseViewHolder>(R.layout.item_dialog_coin_icon_list) {

    override fun convert(helper: BaseViewHolder, item: ChainInfo) {

        var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包

//        val iv_wallet_icon = helper.itemView.findViewById<ImageView>(R.id.iv_wallet_icon)
        val indcator_wmlist = helper.itemView.findViewById<View>(R.id.indcator_wmlist)
        indcator_wmlist.extInvisibleOrVisible(walletInfo.chainType == item.name)

        helper.setText(R.id.tv_chain_name,item.name)
        helper.setText(R.id.tv_chain_fullname,item.fullName)
        //点击触发
//        if (helper.layoutPosition == mPosition){
//            iv_wallet_icon.setImageResource(item.walletManagerCheckIcon)
//        } else {
//            iv_wallet_icon.setImageResource(item.walletManagerUnCheckIcon)
//        }
    }
    private var mPosition = 0


    fun setmPosition(mPosition: Int) {
        this.mPosition = mPosition
    }
}