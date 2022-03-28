package com.fanrong.frwallet.ui.dapp.home

import android.graphics.Bitmap
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.DappInfoDao
import com.fanrong.frwallet.tools.BitmapUtils
import com.fanrong.frwallet.tools.bitMapAndStringConvertUtil
import xc.common.tool.utils.checkNotEmpty


class DappRecentAdapter : BaseQuickAdapter<DappInfoDao, BaseViewHolder>(R.layout.dapp_recent_item) {
    override fun convert(helper: BaseViewHolder, item: DappInfoDao) {
        if (item.name.checkNotEmpty()) {
            helper.setText(R.id.tv_dapp_name, item.name)
        } else {
            helper.setText(R.id.tv_dapp_name, item.url)
        }

        val img = helper.getView<ImageView>(R.id.iv_image)
//        if (item.icon==null || item.icon == ""){
//            img.setImageResource(R.mipmap.src_lib_eui_icon_defaultdappicon)
//        }else{
//            Glide.with(img).load(item.icon).into(img)
//        }
        val bitMapByWebUrl: Bitmap? = bitMapAndStringConvertUtil.getBitMapByWebUrl(item.url)
        if (bitMapByWebUrl!=null){
            img.setImageBitmap(BitmapUtils.bigBitMap(bitMapByWebUrl))
        }else{
            img.setImageResource(R.mipmap.src_lib_eui_icon_defaultdappicon)
        }

    }
}