package com.fanrong.frwallet.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.extFormatAddr
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils

class WalletListAdapter : BaseMultiItemQuickAdapter<WalletListAdapter.Item, BaseViewHolder>(mutableListOf()) {


    init {
        //身份钱包带管理按钮
        addItemType(1, R.layout.wallet_manager_addr_type1)
        addItemType(2, R.layout.wallet_manager_addr_type1)
        //正常的钱包样式
        addItemType(3, R.layout.wallet_manager_addr_type2)
        //添加币种
        addItemType(4, R.layout.wallet_manager_addr_type3)

        // 弹框身份钱包显示样式
        addItemType(5, R.layout.wallet_manager_addr_type1)
        // 弹框身份钱包显示样式
        addItemType(6, R.layout.wallet_manager_addr_type2)
    }

    class Item : MultiItemEntity {

        var myItemType = 1

        override fun getItemType(): Int {
            return myItemType
        }

        var itemData: WalletDao? = null
        var otherInfo: Any? = null

        constructor(myItemType: Int, itemData: WalletDao?) {
            this.myItemType = myItemType
            this.itemData = itemData
        }

        constructor(myItemType: Int, otherInfo: Any?) {
            this.myItemType = myItemType
            this.otherInfo = otherInfo
        }
    }

    override fun convert(helper: BaseViewHolder, item: Item) {
        if (item.itemType == 1) {
            helper.setText(R.id.tv_wallet_type, "身份钱包")
            helper.setVisible(R.id.ll_manager, true)

        } else if (item.itemType == 2) {
            helper.setText(R.id.tv_wallet_type, "创建或导入")
            helper.setVisible(R.id.ll_manager, false)
        } else if (item.itemType == 3) {
            helper.setText(R.id.tv_wm_coinname, item.itemData?.walletName ?: CoinNameCheck.getNameByName(item.itemData!!.chainType))
//            helper.setBackgroundRes(R.id.ll_container, ChainInfo.getChainBg(item.itemData!!.chainType!!))
//            val ll_container = helper.getView<LinearLayout>(R.id.ll_container)
//
//            ll_container.setPadding(10,10,10,10)
            helper.setText(R.id.tv_wm_coinaddress, item.itemData!!.address!!.extFormatAddr())
        } else if (item.itemType == 5) {
            //这是钱包列表的title：ETH。HECO。BSC。CVN   样式里面隐藏了
            helper.setText(R.id.tv_wallet_type, item.otherInfo.toString())
            helper.setVisible(R.id.ll_manager, false)
        } else if (item.itemType == 6) {
            helper.setText(R.id.tv_wm_coinname, item.itemData?.walletName ?: CoinNameCheck.getNameByName(item.itemData!!.chainType))
//            helper.setBackgroundRes(R.id.ll_container, ChainInfo.getChainBg(item.itemData!!.chainType!!))
//            val ll_container = helper.getView<LinearLayout>(R.id.ll_container)
//            ll_container.setPadding(10,10,10,10)
            helper.setText(R.id.tv_wm_coinaddress, item.itemData!!.address!!.extFormatAddr())
//            helper.setImageResource(R.id.iv_operation, R.drawable.current_wallet_drawable)
            helper.setVisible(R.id.iv_operation, "1".equals(item.itemData!!.isCurrentWallet))
        } else {

    }

        val addressView = helper.getView<TextView>(R.id.tv_wm_coinaddress)
        if (addressView != null){
            addressView.setOnClickListener{
                LibAppUtils.copyText(item.itemData!!.address!!)
                showToast("复制成功")
            }
        }

    }
}