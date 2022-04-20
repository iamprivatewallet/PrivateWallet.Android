package com.fanrong.frwallet.ui.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.CoinTypeListAdapter
import com.fanrong.frwallet.dao.ChainInfo
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.*
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.ui.activity.*
import com.fanrong.frwallet.ui.dialog.ReceiptBackupsHintDialog
import com.fanrong.frwallet.ui.receipt.ReceiptActivity
import com.fanrong.frwallet.ui.receipt.TransferActivity
import com.fanrong.frwallet.ui.walletassets.WalletAssetDetailActivty
import com.fanrong.frwallet.view.SelectWalletListDialog
import com.fanrong.frwallet.view.showTopToast
import com.fanrong.frwallet.wallet.eth.viewmodel.WalletViewmodel
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import com.yzq.zxinglibrary.android.CaptureActivity
import kotlinx.android.synthetic.main.fragment_wallet.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.DensityUtil
import xc.common.tool.utils.SPUtils
import xc.common.tool.utils.SWLog
import xc.common.utils.LibAppUtils
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

        iv_lookaccount.setOnClickListener {
            SPUtils.saveValue(FrConstants.SHOW_MONEY_SETTING, !(SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)))
            hideOrShowMoney()
        }
//        iv_menu.setOnClickListener(object : NoShakeOnClickListener {
//            override fun onNoShakeClick(v: View) {
//                SelectWalletListDialog(activity as AppCompatActivity).show()
//            }
//        })
        iv_menu.setOnClickListener {
            extStartActivity(ImportAccountActivity::class.java)
        }

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
        val isShowSmall = SPUtils.getBoolean(FrConstants.IS_ONLY_SHOW_SMALL)
        if (isShowSmall){
            iv_xiaoexianshi.setImageResource(R.mipmap.icon_toggle_on)
        }else{
            iv_xiaoexianshi.setImageResource(R.mipmap.icon_toggle_off)
        }
        iv_xiaoexianshi.setOnClickListener{
            val isShowSmall = SPUtils.getBoolean(FrConstants.IS_ONLY_SHOW_SMALL)
            SPUtils.saveValue(FrConstants.IS_ONLY_SHOW_SMALL,!isShowSmall)
            if (!isShowSmall){
                //显示
                iv_xiaoexianshi.setImageResource(R.mipmap.icon_toggle_on)

            }else{
                //不显示
                iv_xiaoexianshi.setImageResource(R.mipmap.icon_toggle_off)
            }
            if (current_list != null){
                coinTypeAdapter.setNewData(ScreenData(current_list))
            }else{
                walletViewmodel.getBalance(contractAsset!!)
            }

        }

        tv_addr.setOnClickListener {
            LibAppUtils.copyText(currentWallet.address)
            showTopToast(this.activity!!,resources.getString(R.string.copysuccess),true)
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


//        iv_wallet_detail.setOnClickListener {
//            extStartActivity(WalletInfoManageActivity::class.java, Bundle().apply {
//                putSerializable(FrConstants.WALLET_INFO, currentWallet)
//            })
//        }

        ll_searchdappandtoken.setOnClickListener{
            extStartActivity(SearchDappAndTokenActivity::class.java,Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO,currentWallet)
            })
        }
        ll_change_wallet.setOnClickListener{
            SelectWalletListDialog(activity as AppCompatActivity).show()
        }
        tv_cointype.setOnClickListener{
            extStartActivity(HomeAssetManageActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO, currentWallet)
            })
        }
        iv_more.setOnClickListener {
//            extStartActivity(AddContractActivity::class.java, Bundle().apply {
//                putSerializable(FrConstants.WALLET_INFO, currentWallet)
//            })
            //需要跳到币种搜索
            extStartActivity(SearchTokenActivity::class.java, Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO,currentWallet)
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

    }

    private fun CheckIsShowFootView(){

        val visibleViewsCount = recyclerViewItemVisibleUtils.getVisibleViewsCount(rcv_cl)
        if (visibleViewsCount<=coinTypeAdapter.data.size){
            //未满一屏,添加footview
            val footer: View? = LayoutInflater.from(activity)?.inflate(R.layout.layout_wallet_footer, rcv_cl, false)
            coinTypeAdapter.setFooterView(footer)
            footer!!.setOnClickListener{
//            extStartActivity(CustomTokensActivity::class.java, Bundle().apply {
//                putSerializable(FrConstants.WALLET_INFO, currentWallet)
//            })
                extStartActivity(SearchTokenActivity::class.java, Bundle().apply {
                    putSerializable(FrConstants.WALLET_INFO,currentWallet)
                })
            }
            ll_bottomaddcoin.visibility = View.GONE
        }else{
            //满一屏添加悬浮布局
            ll_bottomaddcoin.visibility = View.VISIBLE
            coinTypeAdapter.removeAllFooterView()
        }
    }

    private fun initWalletInfo() {
        if (currentWallet == null) {
            return
        }
        if (isOnRefresh) {
            if (currentWallet?.isMainWallet == "1" && currentWallet?.mnemonic != null && currentWallet.mnemonic!="") {
                ll_backups_home.extInvisibleOrVisible(!"1".equals(currentWallet?.isBackUp))
            } else {
                ll_backups_home.visibility = View.INVISIBLE
            }
        }
        currentAddress = currentWallet?.address!!.extFormatAddr()
        tv_wallet_name.setText(currentWallet?.walletName ?: currentWallet?.chainType)
        ll_curwallet_bg.setBackgroundResource(ChainInfo.getChainBg(currentWallet?.chainType!!))

//        ll_curwallet_bg.setPadding(DensityUtil.dp2px(18),DensityUtil.dp2px(18),DensityUtil.dp2px(18),DensityUtil.dp2px(18))
//        if (SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)) {
//            tv_addr.text = currentAddress?.substring(0, 2) + "****"
//        } else {
//            tv_addr.setText(currentWallet?.address!!.extFormatAddr())
//        }
        tv_addr.setText(currentWallet?.address!!.extFormatAddr())
        coinTypeAdapter.setNewData(ScreenData(contractAsset))
        CheckIsShowFootView()
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
            coinTypeAdapter.setNewData(ScreenData(sortAllResult))
            CheckIsShowFootView()
        }
        // 查询余额
        walletViewmodel.getBalance(contractAsset!!)
//        CenterDataManager.getCoinFromCanter()
    }

    var current_list : List<CoinDao>? = null
    private fun stateChange(state: WalletViewmodel.State) {

        state.balanceResult?.run {
            current_list = this.resultData
            coinTypeAdapter.setNewData(ScreenData(this.resultData))
            var total = BigDecimal.ZERO
            for (resultDatum in this.resultData!!) {
                var cnyAmount = BigDecimal(resultDatum.balance).multiply(BigDecimal(resultDatum.price.toString()))//.extToFiatMoney() = *7
                total = total.plus(cnyAmount)
//                total = total.plus(BigDecimal(resultDatum.balance.extToFiatMoney()))
            }
            totalBalance = total.toPlainString()
            if (SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)) {
                tv_balance_cny.text = "****"
                iv_lookaccount.setImageResource(R.mipmap.icon_unlook)
            } else {
                tv_balance_cny.setText(total.toPlainString())
                iv_lookaccount.setImageResource(R.mipmap.icon_look)
            }
            tv_money_symbal.setText(FrMoneyUnit.getSymbal())
            CheckIsShowFootView()
        }

    }

    private fun hideOrShowMoney() {
        if (SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)) {
            tv_balance_cny.text = "****"
//            tv_addr.text = currentAddress?.substring(0, 2) + "****"
            iv_lookaccount.setImageResource(R.mipmap.icon_unlook)
        } else {
            tv_balance_cny.text = totalBalance
//            tv_addr.text = currentAddress
            iv_lookaccount.setImageResource(R.mipmap.icon_look)
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

    fun ScreenData(list:List<CoinDao>?):List<CoinDao>?{
        val isShowSmall = SPUtils.getBoolean(FrConstants.IS_ONLY_SHOW_SMALL)
        if (list != null && !isShowSmall){
            var data = list.filter {
                it.balance != null && it.balance!!.toDouble() > 0 || it.contract_addr == null || it.contract_addr ==""
            }
            return data
        }
        return list
    }

    override fun onNoShakeClick(v: View) {
    }

}