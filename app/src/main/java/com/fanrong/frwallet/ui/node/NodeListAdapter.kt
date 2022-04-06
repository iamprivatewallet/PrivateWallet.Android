package com.fanrong.frwallet.ui.node

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.ChainNodeDao
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.utils.extInvisibleOrVisible

class NodeListAdapter : BaseQuickAdapter<ChainNodeDao, BaseViewHolder>(R.layout.node_item) {

    open var moreViewId = R.id.ll_boby

    var titleIndexs = mutableListOf<Int>()

    override fun setNewData(data: MutableList<ChainNodeDao>?) {
        var lastType = -1
        for (datum in data!!) {
            if (lastType != (datum.netType)) {
                titleIndexs.add(data.indexOf(datum))
                lastType = datum.netType
            }
        }
        super.setNewData(data)

    }

    override fun convert(helper: BaseViewHolder, item: ChainNodeDao) {

//        helper.setGone(R.id.tv_node_type, titleIndexs.indexOf(helper.adapterPosition) != -1)
//        helper.setText(R.id.tv_node_type, item.typeStr)

        val iv_current = helper.getView<ImageView>(R.id.iv_current)
        //helper.getView<ImageView>(R.id.iv_current).extInvisibleOrVisible(1 == item.isCurrent)
        if (1 == item.isCurrent){
            iv_current.setImageResource(R.mipmap.icon_select_coin)
        }else{
            iv_current.setImageResource(R.mipmap.icon_unselect_coin)
        }
//        helper.getView<ImageView>(R.id.iv_more).extGoneOrVisible(1 != item.netType)

        helper.addOnClickListener(R.id.ll_boby)
//        if (item.netType == 1) {
//            helper.getView<ImageView>(R.id.iv_more).extGoneOrVisible(false)
//        } else {
//            helper.getView<ImageView>(R.id.iv_more).extGoneOrVisible(true)
//        }

        helper.setText(R.id.tv_node_name, item.nodeName)
        helper.setText(R.id.tv_node_url, item.nodeUrl)


    }
}