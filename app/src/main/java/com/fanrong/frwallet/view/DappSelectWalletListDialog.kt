package com.fanrong.frwallet.view

import android.content.DialogInterface
import android.view.Gravity
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.WalletListAdapter
import com.fanrong.frwallet.adapter.WalletTypeListAdapter
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import kotlinx.android.synthetic.main.dialog_select_wallet.*
import kotlinx.android.synthetic.main.view_wallet_list.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.view.customview.FullScreenDialog

/**
 * 选择钱包弹框
 */
class DappSelectWalletListDialog(
    var activity: AppCompatActivity
) : FullScreenDialog(activity) {


    override var contentGravity: Int? = Gravity.BOTTOM
    var selectWalletType = "ETH"


    var chainlist = mutableListOf<ChainInfo>().apply {
        add(ChainInfo("All", R.mipmap.src_lib_eui_icon_walletethereum,""))
        addAll(FrConstants.suport_chains)
    }

    val walletTypeAdapter: WalletTypeListAdapter by lazy {
        WalletTypeListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                setmPosition(position)
                notifyDataSetChanged()

                if (walletTypeAdapter.getItem(position)!!.name.equals("All")) {
                    mWalletAdapter.setNewData(getAllWallet())
                } else {
                    var data = mutableListOf<WalletListAdapter.Item>()
                    data.add(WalletListAdapter.Item(5, walletTypeAdapter.getItem(position)!!.name))
                    val addrs = WalletOperator.queryWalletWithChainType(walletTypeAdapter.getItem(position)!!.name)
                    if (addrs.checkNotEmpty()) {
                        for (addr in addrs) {
                            data.add(WalletListAdapter.Item(6, addr))
                        }
                        mWalletAdapter.setNewData(data)
                    } else {
                        mWalletAdapter.setNewData(data)
                    }
                }
            }

            setNewData(chainlist)
        }

    }
    val mWalletAdapter: WalletListAdapter by lazy {
        WalletListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                if (mWalletAdapter.getItem(position)!!.myItemType == 6) {

                    if (!selectWalletType.contains(mWalletAdapter.getItem(position)!!.itemData!!.chainType!!)) {
                        showToast("请选择${selectWalletType}钱包")
                        return@setOnItemClickListener
                    }

                    AlertDialog.Builder(activity).setMessage("切换钱包，将会重新加载应用")
                        .setNegativeButton("取消", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dialog?.dismiss()
                            }
                        }).setPositiveButton("确认", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface?, which: Int) {
                                dismiss()
                                WalletOperator.changeCurrentWallet(mWalletAdapter.getItem(position)!!.itemData!!)
                                this@DappSelectWalletListDialog.dismiss()
                                EventBus.getDefault().post(CurrentWalletChange())
                                onConfrim?.confirm(mWalletAdapter.getItem(position)!!.itemData!!)
                            }
                        }).show()

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
        rcv_wm_walleticon.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = walletTypeAdapter
        }

        rcv_wm_wallet.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = mWalletAdapter
        }

        walletTypeAdapter.setOnItemClick(null, 0)

//        mWalletAdapter.setNewData(walletList1)
        iv_closedilog.setOnClickListener {

            dismiss()
        }
//        tv_manage.setOnClickListener {
//            dismiss()
//            activity.extStartActivity(WalletMangerActivity::class.java)
//        }
    }
}
