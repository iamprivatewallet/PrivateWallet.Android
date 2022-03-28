package com.fanrong.frwallet.ui.walletassets

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.extFormatAddr
import com.fanrong.frwallet.tools.extHexWei2Ten2EtherKeep4_2
import com.fanrong.frwallet.tools.extWei2Ether
import com.fanrong.frwallet.wallet.eth.eth.GetTransactionRecordResp
import xc.common.tool.utils.XcTimeUtils

class TransactionAdapter : BaseQuickAdapter<GetTransactionRecordResp.DataBean.ResultBean, BaseViewHolder>(R.layout.transaction_item) {

    lateinit var currentTokenInfo: CoinDao


    override fun convert(helper: BaseViewHolder, item: GetTransactionRecordResp.DataBean.ResultBean) {

        if (item.isTransOut(currentTokenInfo.sourceAddr!!)) {
            helper.setText(R.id.tv_amount, "-" + item.value.extHexWei2Ten2EtherKeep4_2()+ CoinNameCheck.getNameByName(currentTokenInfo.coin_name))//extHexWei2Ten2EtherKeep8
        } else {
            val af = item.value.extWei2Ether()
            helper.setText(R.id.tv_amount, "+" + item.value.extHexWei2Ten2EtherKeep4_2()+CoinNameCheck.getNameByName(currentTokenInfo.coin_name))
        }


        helper.setText(R.id.tv_addr, item.getOppositeAddr(currentTokenInfo.sourceAddr!!)!!.extFormatAddr())
        helper.setText(R.id.tv_trans_time, XcTimeUtils.get_yMd_hms(item.timeStamp?.toLong()?.times(1).toString()))
        when (item.status) {
            "D" -> {
                if (item.isTransOut(currentTokenInfo.sourceAddr!!)) {
                    helper.setImageResource(R.id.iv_state, R.mipmap.src_lib_eui_icon_txsend)
                } else {
                    helper.setImageResource(R.id.iv_state, R.mipmap.src_lib_eui_icon_txreceive)
                }
            }
            "E" -> {
                helper.setImageResource(R.id.iv_state, R.mipmap.src_lib_eui_icon_txwrong)
            }
            "P" -> {
                helper.setImageResource(R.id.iv_state, R.mipmap.src_lib_eui_icon_txpending)
            }
        }

    }
}