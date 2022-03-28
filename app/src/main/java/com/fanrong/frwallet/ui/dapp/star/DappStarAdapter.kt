package com.fanrong.frwallet.ui.dapp.star

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseItemDraggableAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.ManageAssetsBalanceAdapter1
import com.fanrong.frwallet.dao.database.DappStarDao
import com.fanrong.frwallet.found.ItemSlipMenuView
import com.fanrong.frwallet.tools.BitmapUtils
import com.fanrong.frwallet.tools.bitMapAndStringConvertUtil

class DappStarAdapter : BaseItemDraggableAdapter<DappStarDao, BaseViewHolder>(R.layout.star_dapp_item, mutableListOf()) {

    var onitemDelectListener: ManageAssetsBalanceAdapter1.ItemDelectListener? = null
    var listener: OnItemClickListener? = null

    var isEdit = false

    override fun convert(helper: BaseViewHolder, item: DappStarDao) {
        val view = helper.getView<ImageView>(R.id.iv_icon)

        val bitMapByWebUrl:Bitmap? = bitMapAndStringConvertUtil.getBitMapByWebUrl(item.url)

        if (bitMapByWebUrl!=null){
            view.setImageBitmap(BitmapUtils.bigBitMap(bitMapByWebUrl))
        }else{
            view.setImageResource(R.mipmap.src_lib_eui_icon_defaultdappicon)
        }

        helper.setGone(R.id.iv_drag_view, isEdit)

        helper.setText(R.id.tv_name, item.name)
        helper.setText(R.id.tv_des, item.des)
        val item_container = helper.getView<LinearLayout>(R.id.ll_item_content)
        val menuContainer = helper.getView<ItemSlipMenuView>(R.id.ll_container)

        menuContainer.closeRightMenu()
        menuContainer.onMenuClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                onitemDelectListener?.onDelect(helper.adapterPosition)
            }
        }

        helper.getView<ImageView>(R.id.iv_remore).setOnClickListener {
            menuContainer.openRightMenu()
        }


        if (isEdit) {
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