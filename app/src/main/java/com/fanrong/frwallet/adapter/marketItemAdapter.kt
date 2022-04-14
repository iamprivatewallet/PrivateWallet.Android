package com.fanrong.frwallet.adapter

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.data.marketDataBean
import com.fanrong.frwallet.dao.database.LikeMarketItemOperator
import java.text.DecimalFormat

class marketItemAdapter : BaseQuickAdapter<marketDataBean.DataDTO, BaseViewHolder>(R.layout.item_market){
    var iv_zx = R.id.iv_zx
    override fun convert(helper: BaseViewHolder, item: marketDataBean.DataDTO) {
        //iv_zx

        //tv_name
        //tv_sz
        //tv_coin_price
        //tv_coin_zdf

        helper.setText(R.id.tv_name,item.symbol)
        helper.setText(R.id.tv_sz,"$"+item.lastVol)
        var tv_coin_zdf = helper.getView<TextView>(R.id.tv_coin_zdf)
        val df = DecimalFormat("######0.00")
        if (item.rose > 0){
            helper.setTextColor(R.id.tv_coin_price, Color.parseColor("#FF1AC190"))
            tv_coin_zdf.setBackgroundResource(R.drawable.bg_rose_green)
            helper.setText(R.id.tv_coin_zdf,"+"+df.format(item.rose).toString()+"%")
        }else{
            helper.setTextColor(R.id.tv_coin_price, Color.parseColor("#FFFF5E5E"))
            tv_coin_zdf.setBackgroundResource(R.drawable.bg_rose_red)
            helper.setText(R.id.tv_coin_zdf,df.format(item.rose).toString()+"%")
        }
        helper.setText(R.id.tv_coin_price,"$"+item.last)






        helper.addOnClickListener(R.id.iv_zx)

        val iv_zx = helper.getView<ImageView>(R.id.iv_zx)

        val lis = LikeMarketItemOperator.getItemBySymbol(item.symbol)
        if (lis!=null&& lis.size >0 &&lis.get(0).isLike!!){
            iv_zx.setImageResource(R.mipmap.icon_zx_select)
        }else{
            iv_zx.setImageResource(R.mipmap.icon_zx_unselect)
        }
    }
}