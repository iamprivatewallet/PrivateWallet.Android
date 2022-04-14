package com.fanrong.frwallet.ui.dapp.history

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.ManageAssetsBalanceAdapter1
import com.fanrong.frwallet.dao.database.DappHistoryDao
import com.fanrong.frwallet.found.ItemSlipMenuView
import com.fanrong.frwallet.tools.BitmapUtils
import com.fanrong.frwallet.tools.bitMapAndStringConvertUtil

class DappHistoryAdapter : BaseItemDraggableAdapter<DappHistoryDao, BaseViewHolder>(R.layout.dapp_history_item, mutableListOf()) {

    var onitemDelectListener: ManageAssetsBalanceAdapter1.ItemDelectListener? = null
    var listener: OnItemClickListener? = null

    var isEdit = false

    override fun convert(helper: BaseViewHolder, item: DappHistoryDao) {
        val view = helper.getView<ImageView>(R.id.iv_icon)

//        val bitMapByWebUrl: Bitmap? = bitMapAndStringConvertUtil.getBitMapByWebUrl(item.url)
//        if (bitMapByWebUrl!=null){
//            view.setImageBitmap(BitmapUtils.bigBitMap(bitMapByWebUrl))
//        }else{
//            view.setImageResource(R.mipmap.src_lib_eui_icon_defaultdappicon)
//        }
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(view).load(item.icon).apply(options).into(view)

//        val all = bitMapAndStringConvertUtil.getWebIconConfigByWebUrl(item.url)
//        if (all != null && all.size >0){
//            helper.setText(R.id.tv_name, all?.get(0)?.title)
//        }else{
//            helper.setText(R.id.tv_name, "--")
//        }
        helper.setText(R.id.tv_name, item.name)
        helper.setText(R.id.tv_des, item.des)

        val item_container = helper.getView<LinearLayout>(R.id.ll_item_content)
        val menuContainer = helper.getView<ItemSlipMenuView>(R.id.ll_container)

        menuContainer.closeRightMenu()
        menuContainer.onMenuClickListener = View.OnClickListener {
            onitemDelectListener?.onDelect(helper.adapterPosition)
        }

        helper.getView<ImageView>(R.id.iv_remore).setOnClickListener {
            menuContainer.openRightMenu()
        }


        if (isEdit){
            item_container.setOnClickListener {
                menuContainer.closeRightMenu()
            }
        } else {
            item_container.setOnClickListener {
                listener?.onItemClick(this, it, helper.adapterPosition)
            }
        }

        helper.setGone(R.id.iv_remore, isEdit)

    }


    override fun setOnItemClickListener(listener: OnItemClickListener?) {
//        super.setOnItemClickListener(listener)
        this.listener = listener
    }

}