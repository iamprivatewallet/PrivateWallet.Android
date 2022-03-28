package com.fanrong.frwallet.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.WalletListAdapter
import com.fanrong.frwallet.adapter.WalletTypeListAdapter
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.ui.createwallet.AddWalletActivity
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_wallet_manger.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.checkNotEmpty

class WalletMangerActivity : BaseActivity() {
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
                    val addrs = WalletOperator.queryWalletWithChainType(walletTypeAdapter.getItem(position)!!.name)
                    if (addrs.checkNotEmpty()) {
                        var data = mutableListOf<WalletListAdapter.Item>()
                        for (addr in addrs) {
                            data.add(WalletListAdapter.Item(3, addr))
                        }
                        mWalletAdapter.setNewData(data)
                    } else {
                        mWalletAdapter.setNewData(mutableListOf())
                    }
                }
            }

            setNewData(chainlist)
        }

    }
    val mWalletAdapter: WalletListAdapter by lazy {
        WalletListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                if (mWalletAdapter.getItem(position)!!.myItemType == 1) {
                    extStartActivity( IdentityWalletManageActivity::class.java)
                } else if (mWalletAdapter.getItem(position)!!.myItemType == 3) {

                    extStartActivity(WalletInfoManageActivity::class.java, Bundle().apply {
                        putSerializable(FrConstants.WALLET_INFO, mWalletAdapter.getItem(position)!!.itemData)
                    })
                } else if (mWalletAdapter.getItem(position)!!.myItemType == 4) {
                    extStartActivity(AddCoinsActivity::class.java, Bundle().apply {
                        putSerializable(FrConstants.WALLET_INFO, WalletOperator.queryIdentityWallet())
                    })
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_wallet_manger
    }

    @SuppressLint("ResourceAsColor")
    override fun initView() {
        wm_title.apply {
            setTitleText("管理钱包")
//            setTitleTextColor(R.color.black)
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }
//        tv_wm_manage.setOnClickListener {


//        }
        rcv_wm_walleticon.apply {
            layoutManager = LinearLayoutManager(this@WalletMangerActivity)
            adapter = walletTypeAdapter
        }

        rcv_wm_wallet.apply {
            layoutManager = LinearLayoutManager(this@WalletMangerActivity)
            adapter = mWalletAdapter
        }

        tv_add_wallet.setOnClickListener {
            extStartActivity(AddWalletActivity::class.java)
        }

        walletTypeAdapter.setOnItemClick(null, 0)
    }

    fun getAllWallet(): MutableList<WalletListAdapter.Item> {
        var list = mutableListOf<WalletListAdapter.Item>()
        list.add(WalletListAdapter.Item(1, null))

        for (greWalletModel in WalletOperator.queryMainWallet()) {
            list.add(WalletListAdapter.Item(3, greWalletModel))
        }


        list.add(WalletListAdapter.Item(4, null))

        val queryOtherWallet = WalletOperator.queryOtherWallet()
        if (queryOtherWallet.checkNotEmpty()) {
            list.add(WalletListAdapter.Item(2, null))
            for (greWalletModel in queryOtherWallet) {
                list.add(WalletListAdapter.Item(3, greWalletModel))
            }
        }

        return list

    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: WalletInfoChangeEvent) {
        initView()
    }


}