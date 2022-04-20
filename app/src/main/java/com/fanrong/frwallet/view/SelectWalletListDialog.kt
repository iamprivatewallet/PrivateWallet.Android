package com.fanrong.frwallet.view

import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.WalletListAdapter
import com.fanrong.frwallet.adapter.WalletTypeListAdapter
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.ChainNodeOperator
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.ui.activity.SearchTokenActivity
import com.fanrong.frwallet.ui.contract.custom.CustomTokensActivity
import com.fanrong.frwallet.ui.createwallet.AddWalletActivity
import kotlinx.android.synthetic.main.dialog_select_wallet.*
import kotlinx.android.synthetic.main.fragment_wallet.*
import kotlinx.android.synthetic.main.view_wallet_list.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

/**
 * x选择钱包弹框
 */
class SelectWalletListDialog(
    var activity: AppCompatActivity
) : FullScreenDialog(activity) {


    override var contentGravity: Int? = Gravity.BOTTOM
    var isFromDapp = false
    var cur_type_wallet:MutableList<WalletListAdapter.Item>? = null
    var footerView:View? = null


    var chainlist = mutableListOf<ChainInfo>().apply {
//        add(ChainInfo("All", R.mipmap.src_lib_eui_icon_walletethereum,""))
        addAll(FrConstants.suport_chains)
    }

    val walletTypeAdapter: WalletTypeListAdapter by lazy {
        WalletTypeListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                setmPosition(position)
                notifyDataSetChanged()

                ll_current_chain.visibility = View.VISIBLE
                rcv_wm_wallet.visibility = View.VISIBLE
                rcv_wm_walleticon.visibility = View.GONE

                if (walletTypeAdapter.getItem(position)!!.name.equals("All")) {
                    mWalletAdapter.setNewData(getAllWallet())
                } else {
                    var data = mutableListOf<WalletListAdapter.Item>()
                    data.add(WalletListAdapter.Item(5, walletTypeAdapter.getItem(position)!!.name))
                    val addrs = WalletOperator.queryWalletWithChainType(walletTypeAdapter.getItem(position)!!.name)

                    tv_current_chainname.setText(walletTypeAdapter.getItem(position)!!.name)
                    tv_current_chaintype_fullname.setText(walletTypeAdapter.getItem(position)!!.fullName)
                    if (addrs.checkNotEmpty()) {
                        for (addr in addrs) {
                            data.add(WalletListAdapter.Item(6, addr))
                        }
                        mWalletAdapter.setNewData(data)
                    } else {
                        mWalletAdapter.setNewData(data)
                    }

                    if (footerView!=null && data.size <= 1){
                        val footerTitle = footerView?.findViewById<TextView>(R.id.tv_title)
                        footerTitle?.setText(context.getString(R.string.cjqb))
                    }

                    cur_type_wallet = data
                }
            }
            setNewData(chainlist)
        }

    }
    val mWalletAdapter: WalletListAdapter by lazy {
        WalletListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                if (mWalletAdapter.getItem(position)!!.myItemType == 6) {

                    // dapp 切换 弹框提示
                    if (isFromDapp) {
                        AlertDialog
                            .Builder(activity).setMessage(context.getString(R.string.dappchangewallet_tip))
                            .setNegativeButton(context.getString(R.string.qx), object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {
                                    dialog?.dismiss()
                                }
                            }).setPositiveButton(context.getString(R.string.confirm), object : DialogInterface.OnClickListener {
                                override fun onClick(dialog: DialogInterface?, which: Int) {

                                    WalletOperator.changeCurrentWallet(mWalletAdapter.getItem(position)!!.itemData!!)
                                    this@SelectWalletListDialog.dismiss()
                                    EventBus.getDefault().post(CurrentWalletChange())
                                    onConfrim?.confirm(mWalletAdapter.getItem(position)!!.itemData!!)
                                }
                            })
                            .show()
                    } else {
                        WalletOperator.changeCurrentWallet(mWalletAdapter.getItem(position)!!.itemData!!)
                        onConfrim?.confirm(mWalletAdapter.getItem(position)!!.itemData!!)
                        this@SelectWalletListDialog.dismiss()
                        EventBus.getDefault().post(CurrentWalletChange())
                    }

                }
            }
        }
    }


    fun getAllWallet(): MutableList<WalletListAdapter.Item> {
        var list = mutableListOf<WalletListAdapter.Item>()
        list.add(WalletListAdapter.Item(5, "身份钱包"))

        for (greWalletModel in WalletOperator.queryMainWallet()) {
            list.add(WalletListAdapter.Item(6, greWalletModel))
        }

        val queryOtherWallet = WalletOperator.queryOtherWallet()
        if (queryOtherWallet.checkNotEmpty()) {
            list.add(WalletListAdapter.Item(2, null))
            for (greWalletModel in queryOtherWallet) {
                list.add(WalletListAdapter.Item(6, greWalletModel))
            }
        }

        return list

    }


    override fun getContentView(): Int {
        return R.layout.dialog_select_wallet
    }

    override fun initView() {
        rcv_wm_wallet.visibility = View.VISIBLE
        rcv_wm_walleticon.visibility = View.GONE
        rcv_wm_walleticon.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = walletTypeAdapter
        }

        rcv_wm_wallet.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mWalletAdapter
        }
        var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
        val nodeInfo = ChainNodeOperator.queryCurrentNode(walletInfo.chainType!!)//当前网络

        var current_index = 0
        for (item in chainlist){
            if (item.name.equals(walletInfo.chainType)){
                walletTypeAdapter.setOnItemClick(null, current_index)
            }
            current_index++
        }


//        mWalletAdapter.setNewData(walletList1)
        iv_closedilog.setOnClickListener {
            if (rcv_wm_wallet.visibility == View.VISIBLE)
                dismiss()
            else{
                tv_operation_type.setText(context.getString(R.string.qb))
                ll_current_chain.visibility = View.VISIBLE
                rcv_wm_wallet.visibility = View.VISIBLE
                rcv_wm_walleticon.visibility = View.GONE
            }
        }
//        tv_manage.setOnClickListener {
//            dismiss()
//            activity.extStartActivity(WalletMangerActivity::class.java)
//        }

        tv_current_chainname.setText(walletInfo.chainType!!)
        tv_current_chaintype_fullname.setText(nodeInfo.nodeName)
        tv_changenetwork.setOnClickListener{
            ll_current_chain.visibility = View.GONE
            rcv_wm_wallet.visibility = View.GONE
            rcv_wm_walleticon.visibility = View.VISIBLE
            tv_operation_type.setText(context.getString(R.string.qhwl))
        }

        footerView = LayoutInflater.from(activity)?.inflate(R.layout.layout_wallet_footer, rcv_wm_wallet, false)
        mWalletAdapter.setFooterView(footerView)
        val currentWallet = WalletOperator.currentWallet!!
        footerView!!.setOnClickListener{
            if (cur_type_wallet != null && cur_type_wallet!!.size > 1){
//                val first_wallet = cur_type_wallet?.get(1)
//                activity.extStartActivity(CustomTokensActivity::class.java, Bundle().apply {
//                    putSerializable(FrConstants.WALLET_INFO, first_wallet!!.itemData)
//                })
                activity.extStartActivity(SearchTokenActivity::class.java, Bundle().apply {
                    putSerializable(FrConstants.WALLET_INFO,currentWallet)
                })
                dismiss()
            }else{
                //没有创建过钱包，就需要去创建钱包
                activity.extStartActivity(AddWalletActivity::class.java)
                dismiss()
            }
        }
    }
}
