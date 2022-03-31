package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.WalletListAdapter
import com.fanrong.frwallet.adapter.WalletTypeListAdapter
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletOperator
import kotlinx.android.synthetic.main.select_wallet_network_dialog.*
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

class SelectWalletNetworkListDialog(context: Context): FullScreenDialog(context) {
    override var contentGravity: Int? = Gravity.BOTTOM
    var chainlist = mutableListOf<ChainInfo>().apply {
//        add(ChainInfo("All", R.mipmap.src_lib_eui_icon_walletethereum,""))
        addAll(FrConstants.suport_chains)
    }
    lateinit var changeListener:IChainChangeListener
    interface IChainChangeListener{
        fun chainChange(position:Int)
    }

    val walletTypeAdapter: WalletTypeListAdapter by lazy {
        WalletTypeListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                setmPosition(position)
                notifyDataSetChanged()

                if (changeListener != null){
                    changeListener.chainChange(position)
                }
                dismiss()
            }
            setNewData(chainlist)
        }
    }
    override fun getContentView(): Int {
        return R.layout.select_wallet_network_dialog
    }

    override fun initView() {
        rcv_wm_walleticon.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = walletTypeAdapter
        }
        iv_closedilog.setOnClickListener{
            dismiss()
        }

    }
}