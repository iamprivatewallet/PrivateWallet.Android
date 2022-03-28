package com.fanrong.frwallet.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.dao.database.WalletDao
import xc.common.viewlib.utils.extInvisibleOrVisible

class PswLoginDialogAdapter :   BaseQuickAdapter<WalletDao, BaseViewHolder>(R.layout.item_use_pswlogin) {

    override fun convert(helper: BaseViewHolder, item: WalletDao) {

        val iv_select = helper.itemView.findViewById<ImageView>(R.id.iv_select)
        val tv_wm_coinname = helper.itemView.findViewById<TextView>(R.id.tv_wm_coinname)
        val tv_wm_coinaddress = helper.itemView.findViewById<TextView>(R.id.tv_wm_coinaddress)
        val iv_coin_icon = helper.itemView.findViewById<ImageView>(R.id.iv_coin_icon)
        iv_select.extInvisibleOrVisible(helper.layoutPosition == mPosition)
        tv_wm_coinname.text=item.walletName ?: item.chainType
        tv_wm_coinaddress.text=item.address
        when((helper.layoutPosition+1)%5){
            0 -> {
                iv_coin_icon.setImageResource(R.mipmap.src_components_common_walletavatar_images_defaultavatar0)
            }
            1 -> {
                iv_coin_icon.setImageResource(R.mipmap.src_components_common_walletavatar_images_defaultavatar1)
            }
            2 -> {
                iv_coin_icon.setImageResource(R.mipmap.src_components_common_walletavatar_images_defaultavatar2)
            }
            3 -> {
                iv_coin_icon.setImageResource(R.mipmap.src_components_common_walletavatar_images_defaultavatar3)
            }
            4 -> {
                iv_coin_icon.setImageResource(R.mipmap.src_components_common_walletavatar_images_defaultavatar5)
            }
        }
    }
    private var mPosition = 0


    fun setmPosition(mPosition: Int) {
        this.mPosition = mPosition
    }
}