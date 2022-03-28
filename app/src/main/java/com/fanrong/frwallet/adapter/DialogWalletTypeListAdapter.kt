package com.fanrong.frwallet.adapter

import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R

class DialogWalletTypeListAdapter :   BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_dialog_coin_icon_list) {

    override fun convert(helper: BaseViewHolder, item: String) {
        val iv_wallet_icon = helper.itemView.findViewById<ImageView>(R.id.iv_wallet_icon)
        val indcator_wmlist = helper.itemView.findViewById<View>(R.id.indcator_wmlist)
        indcator_wmlist.visibility=View.INVISIBLE
        when (item) {
            "ALL" -> {
                iv_wallet_icon.setImageResource(R.mipmap.src_lib_eui_icon_walletidentity)
            }
            "ETH" -> {
                iv_wallet_icon.setImageResource(R.mipmap.src_lib_eui_icon_walletethereum)
            }
            "BTC" -> {
                iv_wallet_icon.setImageResource(R.mipmap.src_lib_eui_icon_walletbitcoin)
            }
            "TRX" -> {
                iv_wallet_icon.setImageResource(R.mipmap.src_lib_eui_icon_wallettron)
            }
        }

        //点击触发
        if (helper.layoutPosition == mPosition){
            indcator_wmlist.visibility=View.VISIBLE
            when (item) {
                "ALL" -> {
                    iv_wallet_icon.setImageResource(R.mipmap.src_lib_eui_icon_walletidentitynormal)
                }
                "ETH" -> {
                    iv_wallet_icon.setImageResource(R.mipmap.src_lib_eui_icon_walletethereumnormal)
                }
                "BTC" -> {
                    iv_wallet_icon.setImageResource(R.mipmap.src_lib_eui_icon_walletbitcoinnormal)
                }
                "TRX" -> {
                    iv_wallet_icon.setImageResource(R.mipmap.src_lib_eui_icon_wallettronnormal)
                }
            }

        }
    }
    private var mPosition = 0


    fun setmPosition(mPosition: Int) {
        this.mPosition = mPosition
    }
}