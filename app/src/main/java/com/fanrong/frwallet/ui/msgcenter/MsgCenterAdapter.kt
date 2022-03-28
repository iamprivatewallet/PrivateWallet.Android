package com.fanrong.frwallet.ui.msgcenter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.MsgDao
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.wallet.eth.eth.QueryTransactionPageResp

class MsgCenterAdapter : BaseQuickAdapter<QueryTransactionPageResp.transactionItem, BaseViewHolder>(R.layout.item_msgcenter) {

    lateinit var msgBeanModel: MsgDao


    override fun convert(helper: BaseViewHolder, item: QueryTransactionPageResp.transactionItem) {
        if (!item.txHash.equals("")){
            var iv_transactionStatus = helper.getView<ImageView>(R.id.iv_transaction_status)
            helper.setText(R.id.tv_msg_title,item.symbol+": "+item.amount.toString()+" "+getTransactionTip(item.toAddress))
            helper.setText(R.id.tv_msg_body,getAction(item.toAddress,item.fromAddress))
            helper.setText(R.id.tv_msg_time,item.createTime)
            iv_transactionStatus.visibility = View.VISIBLE
        }else{
            var iv_transactionStatus = helper.getView<ImageView>(R.id.iv_transaction_status)
            helper.setText(R.id.tv_msg_title,item.title)
            helper.setText(R.id.tv_msg_body,item.context)
            helper.setText(R.id.tv_msg_time,item.createTime)
            iv_transactionStatus.visibility = View.GONE
        }

    }

    fun getTransactionTip(toAddress:String):String{
        //获取当前钱包
        var walletInfo: WalletDao = WalletOperator.currentWallet!!
        if (toAddress.equals(walletInfo.address)){
            return "收款成功"
        }else{
            return "转账成功"
        }
    }
    fun getAction(address1:String,address2:String):String{
        var walletInfo: WalletDao = WalletOperator.currentWallet!!
        if (address1.equals(walletInfo.address)){
            return "发送地址: "+address2
        }else{
            return "收款地址: "+address2
        }
    }
}