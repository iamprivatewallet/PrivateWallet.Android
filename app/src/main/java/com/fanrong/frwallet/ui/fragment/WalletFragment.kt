package com.fanrong.frwallet.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.CoinTypeListAdapter
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.*
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.ui.activity.IdentityWalletManageActivity
import com.fanrong.frwallet.ui.activity.WalletInfoManageActivity
import com.fanrong.frwallet.ui.contract.AddContractActivity
import com.fanrong.frwallet.ui.contract.custom.CustomTokensActivity
import com.fanrong.frwallet.ui.dialog.ReceiptBackupsHintDialog
import com.fanrong.frwallet.ui.receipt.ReceiptActivity
import com.fanrong.frwallet.ui.receipt.TransferActivity
import com.fanrong.frwallet.ui.walletassets.WalletAssetDetailActivty
import com.fanrong.frwallet.view.SelectWalletListDialog
import com.fanrong.frwallet.wallet.eth.viewmodel.WalletViewmodel
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import com.yzq.zxinglibrary.android.CaptureActivity
import kotlinx.android.synthetic.main.fragment_wallet.*
import kotlinx.android.synthetic.main.wallet_asset_detail_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.framework.listener.NoShakeOnClickListener
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.DensityUtil
import xc.common.tool.utils.SPUtils
import xc.common.tool.utils.SWLog
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
import xc.common.viewlib.utils.extInvisibleOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog
import java.math.BigDecimal


class WalletFragment : BaseFragment() {

    val coinTypeAdapter: CoinTypeListAdapter by lazy {
        CoinTypeListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                extStartActivity(WalletAssetDetailActivty::class.java, Bundle().apply {
                    putSerializable(FrConstants.TOKEN_INFO, coinTypeAdapter.getItem(position))
                })
            }
        }
    }
    lateinit var walletViewmodel: WalletViewmodel

    lateinit var currentWallet: WalletDao


    var contractAsset: MutableList<CoinDao>? = null
    var sortAllResult = mutableListOf<CoinDao>()
    var coinSortList = mutableListOf<CoinDao>()
    var isOnRefresh: Boolean = true

    var totalBalance: String? = null
    var currentAddress: String? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_wallet
    }

    override fun initView() {
        currentWallet = WalletOperator.currentWallet!!
        walletViewmodel = WalletViewmodel.getViewmodel(currentWallet!!)

//        tv_current_node.setText(NodeList.getCurrentNode(currentWallet.chainType!!))

        tv_balance_cny.setOnClickListener {
            SPUtils.saveValue(FrConstants.SHOW_MONEY_SETTING, !(SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)))
            hideOrShowMoney()
        }
        iv_menu.setOnClickListener(object : NoShakeOnClickListener {
            override fun onNoShakeClick(v: View) {
                SelectWalletListDialog(activity as AppCompatActivity).show()
            }
        })
//        iv_menu.setOnClickListener {
//            SelectWalletListDialog(activity as AppCompatActivity).show()
//        }

        iv_scan.setOnClickListener {
            LibPremissionUtils.needCamera(this, object : PermissonSuccess {
                override fun hasSuccess() {
                    extStartActivityForResult(CaptureActivity::class.java, 101) { resultCode: Int, data: Intent? ->
                        if (resultCode == Activity.RESULT_OK) {
                            QrcodeDecode.handleScan(data!!.extras!!, activity as BaseActivity)
                        }
                    }
                }
            })

        }

        tv_addr.setOnClickListener {
            if (currentWallet.isBackUp == "0" && currentWallet.isMainWallet == "1") {
                ReceiptBackupsHintDialog(this.activity!!).apply {
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            extStartActivity(IdentityWalletManageActivity::class.java, Bundle().apply {
                                putSerializable(FrConstants.WALLET_INFO, currentWallet)
                            })
                        }
                    }
                }.show()
                return@setOnClickListener
            }
            extStartActivity(ReceiptActivity::class.java, Bundle().apply {
                try {
                    putSerializable(FrConstants.TOKEN_INFO, coinTypeAdapter.getItem(0))
                    putString(FrConstants.ADDR_INFO, currentWallet?.address!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }

        ll_receipt.setOnClickListener {
            if (currentWallet.isBackUp == "0" && currentWallet.isMainWallet == "1") {
                ReceiptBackupsHintDialog(this.activity!!).apply {
                    onConfrim = object :
                        FullScreenDialog.OnConfirmListener {
                        override fun confirm(params: Any?) {
                            extStartActivity(IdentityWalletManageActivity::class.java, Bundle().apply {
                                putSerializable(FrConstants.WALLET_INFO, currentWallet)
                        })
                        }
                    }
                }.show()
                return@setOnClickListener
            }
            extStartActivity(ReceiptActivity::class.java, Bundle().apply {
                try {
                    putSerializable(FrConstants.TOKEN_INFO, coinTypeAdapter.getItem(0))
                    putString(FrConstants.ADDR_INFO, currentWallet?.address!!)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            })
        }

        ll_withdraw.setOnClickListener {
            if (coinTypeAdapter.data.size >0){
                extStartActivity(TransferActivity::class.java, Bundle().apply {
                    putSerializable(FrConstants.TOKEN_INFO, coinTypeAdapter.getItem(0))
                })
            }
        }


        iv_wallet_detail.setOnClickListener {
            extStartActivity(WalletInfoManageActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, currentWallet)
            })
        }
        iv_more.setOnClickListener {
            extStartActivity(AddContractActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, currentWallet)
            })
        }
        swr_wallet.setOnRefreshListener {
            isOnRefresh = false
            initWalletInfo()
            isOnRefresh = true
            swr_wallet.postDelayed(Runnable {
                swr_wallet.isRefreshing = false
            }, 2000)
        }


//        ll_node_set.setOnClickListener {
//            extStartActivity(ChangeNodeActivity::class.java, Bundle().apply {
//                putString(FrConstants.CHAIN_TYPE, currentWallet!!.chainType)
//                putString(FrConstants.PRE_PAGE, "home")
//            })
//        }
        tv_backups_cancel.setOnClickListener {
            ll_backups_home.visibility = View.INVISIBLE
        }
        tv_backups_ok.setOnClickListener {
            extStartActivity(IdentityWalletManageActivity::class.java)
        }
        rcv_cl.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = coinTypeAdapter
        }

        walletViewmodel.observerDataChange(this, this::stateChange)

        initWalletInfo()
        val footer: View? = LayoutInflater.from(activity)?.inflate(R.layout.layout_wallet_footer, rcv_cl, false)
        coinTypeAdapter.setFooterView(footer)
        footer!!.setOnClickListener{
            extStartActivity(CustomTokensActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, currentWallet)
            })
        }
    }

    private fun initWalletInfo() {
        if (currentWallet == null) {
            return
        }
        if (isOnRefresh) {
            if (currentWallet?.isMainWallet == "1") {
                ll_backups_home.extInvisibleOrVisible(!"1".equals(currentWallet?.isBackUp))
            } else {
                ll_backups_home.visibility = View.INVISIBLE
            }
        }
        currentAddress = currentWallet?.address!!.extFormatAddr()
        tv_wallet_name.setText(currentWallet?.walletName ?: currentWallet?.chainType)
        ll_curwallet_bg.setBackgroundResource(ChainInfo.getChainBg(currentWallet?.chainType!!))

        ll_curwallet_bg.setPadding(DensityUtil.dp2px(18),DensityUtil.dp2px(18),DensityUtil.dp2px(18),DensityUtil.dp2px(10))
        if (SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)) {
            tv_addr.text = currentAddress?.substring(0, 2) + "****"
        } else {
            tv_addr.setText(currentWallet?.address!!.extFormatAddr())
        }
        coinTypeAdapter.setNewData(contractAsset)
        contractAsset = CoinOperator.queryContractAssetWithWallet(currentWallet)
        //排序
        if (contractAsset!!.size > 1 && currentWallet!!.sortType != "3") {
            coinSortList.clear()
            sortAllResult.clear()
            if (currentWallet!!.sortType == "0") {
                coinSortList.addAll(contractAsset!!.subList(1, contractAsset!!.size).sortedBy { it.coin_name })
            } else if (currentWallet!!.sortType == "1") {
                coinSortList.addAll(contractAsset!!.subList(1, contractAsset!!.size).sortedByDescending { it.balance })
            } else if (currentWallet!!.sortType == "2") {
                coinSortList.addAll(contractAsset!!.subList(1, contractAsset!!.size).sortedWith(compareBy { it.coin_name.toUpperCase() }))
            }
            sortAllResult.add(contractAsset!![0])
            sortAllResult.addAll(coinSortList)
            contractAsset = sortAllResult
            SWLog.e(sortAllResult.size.toString() + "--sortAllResult---")
            CoinOperator.saveSortCoins(currentWallet, sortAllResult)
            coinTypeAdapter.setNewData(sortAllResult)
        }
        // 查询余额
        walletViewmodel.getBalance(contractAsset!!)
//        CenterDataManager.getCoinFromCanter()
    }

    private fun stateChange(state: WalletViewmodel.State) {

        state.balanceResult?.run {
            coinTypeAdapter.setNewData(this.resultData)
            var total = BigDecimal.ZERO
            for (resultDatum in this.resultData!!) {
                var cnyAmount = BigDecimal(resultDatum.balance).multiply(BigDecimal(resultDatum.price.toString()))//.extToFiatMoney() = *7
                total = total.plus(cnyAmount)
//                total = total.plus(BigDecimal(resultDatum.balance.extToFiatMoney()))
            }
            totalBalance = total.toPlainString()
            if (SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)) {
                tv_balance_cny.text = "****"
            } else {
                tv_balance_cny.setText(total.toPlainString())
            }
            tv_money_symbal.setText(FrMoneyUnit.getSymbal())
        }

    }

    private fun hideOrShowMoney() {
        if (SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)) {
            tv_balance_cny.text = "****"
            tv_addr.text = currentAddress?.substring(0, 2) + "****"
        } else {
            tv_balance_cny.text = totalBalance
            tv_addr.text = currentAddress
        }
        coinTypeAdapter.notifyDataSetChanged()
    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: CurrentWalletChange) {
        initView()
        ConfigTokenOperator.initServiceToken()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: WalletInfoChangeEvent) {
        initView()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: ShowMoneyEvent) {
        hideOrShowMoney()
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: TransferFinishEvent) {
        walletViewmodel.getBalance(contractAsset!!)
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun onReceiptEvent(event: TokenPriceFinishEvent) {
//        val currentWallet = WalletOperator.currentWallet!!//有chainType
//        val contractAsset = CoinOperator.queryContractAssetWithWallet(currentWallet) //币种的数组，contractAsset[0].coin_name
//        coinTypeAdapter.setNewData(contractAsset)
//        coinTypeAdapter.notifyDataSetChanged()
//    }

    override fun onNoShakeClick(v: View) {
    }

}