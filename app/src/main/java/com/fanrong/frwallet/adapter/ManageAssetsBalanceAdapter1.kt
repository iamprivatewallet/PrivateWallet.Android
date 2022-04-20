package com.fanrong.frwallet.adapter

import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.found.ItemSlipMenuView
import com.fanrong.frwallet.main.MyApplication
import com.fanrong.frwallet.tools.extFormatAddr
import xc.common.tool.utils.LibViewUtils

class ManageAssetsBalanceAdapter1 : BaseItemDraggableAdapter<CoinDao, BaseViewHolder>(R.layout.item_asset_manage_1, mutableListOf()) {

    var onitemDelectListener: ItemDelectListener? = null
    var isEdit = false

    interface ItemDelectListener {
        fun onDelect(position: Int)
    }

    var onitemToTopListener: ItemToTopListener? = null

    interface ItemToTopListener {
        fun onToTop(position: Int)
    }

    val remoreId = R.id.iv_remore
    val ll_container = R.id.ll_container

    override fun convert(helper: BaseViewHolder, item: CoinDao) {
        helper.setText(R.id.tv_ham_coinname, item.coin_name)
        helper.setText(R.id.tv_contract, item.contract_addr?.extFormatAddr())
        helper.setText(R.id.tv_ham_balance, item.balance)
//iv_coin_icon
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(helper.getView<ImageView>(R.id.iv_coin_icon)).load(item.coin_icon).apply(options).into(helper.getView<ImageView>(R.id.iv_coin_icon))

        val view = helper.getView<LinearLayout>(R.id.ll_item_content)
        val menuContainer = helper.getView<ItemSlipMenuView>(R.id.ll_container)
        view.layoutParams.width = LibViewUtils.getScreenInfo(MyApplication.context).first
        view.layoutParams = view.layoutParams
        view.setOnClickListener {
            menuContainer.closeRightMenu()
            helper.getView<ImageView>(R.id.iv_drag_view).visibility = View.VISIBLE

        }

        menuContainer.closeRightMenu()
        menuContainer.onMenuClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                onitemDelectListener?.onDelect(helper.adapterPosition)
            }
        }
        //onitemToTopListener
        val iv_totop = helper.getView<ImageView>(R.id.iv_totop)
        iv_totop.setOnClickListener{
            onitemToTopListener?.onToTop(helper.adapterPosition)
        }

//        helper.addOnClickListener(R.id.ll_container)
        helper.getView<ImageView>(R.id.iv_remore).setOnClickListener {
            menuContainer.openRightMenu()
            helper.getView<ImageView>(R.id.iv_drag_view).visibility = View.GONE
        }

        helper.getView<ImageView>(R.id.iv_drag_view).visibility = View.VISIBLE
//        helper.addOnClickListener(R.id.iv_remore)

        val iv_remore = helper.getView<ImageView>(R.id.iv_remore)
        val iv_drag_view = helper.getView<ImageView>(R.id.iv_drag_view)

        if (!isEdit){
            iv_remore.visibility = View.GONE
            iv_totop.visibility = View.GONE
            iv_drag_view.visibility = View.GONE
        }else{
            iv_remore.visibility = View.VISIBLE
            iv_totop.visibility = View.VISIBLE
            iv_drag_view.visibility = View.VISIBLE
        }
    }
}