package com.fanrong.frwallet.ui.walletassets

import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.dapp.dappapi.error.AccountNotFoundException
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.ETHChainUtil
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.extWei2Ether
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.showTopToast
import com.fanrong.frwallet.wallet.bsc.bscApi
import com.fanrong.frwallet.wallet.eth.eth.Eth_GetTransactionByHashReq
import com.fanrong.frwallet.wallet.eth.eth.Eth_GetTransactionByHashResp
import com.fanrong.frwallet.wallet.eth.eth.ethApi
import com.fanrong.frwallet.wallet.heco.hecoApi
import kotlinx.android.synthetic.main.transfer_info_detail_activity.*
import org.brewchain.core.crypto.cwv.util.BytesHelper
import org.web3j.protocol.Web3j
import org.web3j.protocol.http.HttpService
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.CommonTool
import xc.common.tool.utils.XcTimeUtils
import xc.common.utils.LibAppUtils
import java.math.BigDecimal
import java.math.BigInteger


class TransferInfoDetailActivity : BaseActivity() {
    var hash:String? = ""
    val transactionInfo: TransferDao by lazy {
        intent.getSerializableExtra(FrConstants.TRANSACTION_INFO) as TransferDao
    }

    val tokenInfo: CoinDao by lazy {
        intent.getSerializableExtra(FrConstants.TOKEN_INFO) as CoinDao
    }

    override fun getLayoutId(): Int {
        return R.layout.transfer_info_detail_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@TransferInfoDetailActivity, resources.getString(R.string.jyxq))
//            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_sharegray) {
//                ShareReceiptQrDialog(this@TransferInfoDetailActivity).apply {
//                    bitmap = LibAppUtils.viewToBitmap(this@TransferInfoDetailActivity.ll_share_content)
//                }.show()
//            }

        }

        ll_to_browser.setOnClickListener {
//            var wallet = WalletOperator.queryWallet(tokenInfo)
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putSerializable(DappBrowserActivity.PARAMS_DAPP, DappInfoDao().apply {
//                    this.icon = ""
                    this.name = "区块浏览器"
                    this.des = "区块浏览器"
                    this.url = getChainBrowseURL()
                })
            })
        }
        if (tokenInfo.chain_name=="CVN"){
            ll_gas.visibility=View.GONE
        }
        transactionInfo.run {
            when (status) {
                "D" -> {
                    iv_state.setImageResource(R.mipmap.src_lib_eui_icon_txsuccessnew)
                    checkStatus(1)
                }
                "E" -> {
                    checkStatus(2)
                    iv_state.setImageResource(R.mipmap.src_lib_eui_icon_txfailednew)
                }
                else -> {
                    ll_gas.visibility=View.GONE
                    checkStatus(3)
                    iv_state.setImageResource(R.mipmap.src_lib_eui_icon_txpendingnew)
                }
            }

            var copyClickListener = object : View.OnClickListener {
                override fun onClick(v: View) {
                    LibAppUtils.copyText((v as TextView).text.toString())
                    showTopToast(this@TransferInfoDetailActivity,getString(R.string.copysuccess),true)
                }
            }

            tv_hash.setOnClickListener(copyClickListener)
            tv_to_addr.setOnClickListener(copyClickListener)
            tv_from_addr.setOnClickListener(copyClickListener)

            if (isTransOut(tokenInfo.sourceAddr!!)) {
                tv_amount.setText("-" + amount)
            } else {
                tv_amount.setText("+" + amount)
            }
            tv_coinname.setText(CoinNameCheck.getNameByName(tokenInfo.coin_name))
            tv_from_addr.setText(from)
            tv_to_addr.setText(to)
//            0000000000000000000000000000000000000000000000000de0b6b3a7640000
//            000000000000000000000000fc85cd6c929847621f77bda95ea645f46df2af53
//            if (!transactionInfo.gasUsed.checkIsEmpty()) {
//
//                tv_gas.setText(transactionInfo.gasUsed.extWei2Ether())
//            } else {
////                tv_gas.setText(transactionInfo.gasUsed.extWei2Ether())
//            }

            hash = transaction_hash
            tv_gas.setText(transactionInfo.gasUsed.extWei2Ether()+CoinNameCheck.getMainCoinName())
            tv_hash.setText(transaction_hash)
            tv_time.setText(XcTimeUtils.get_yMd_hms(timestamp!!))
        }

    }

//    fun getApi ():{
//        if (CoinNameCheck.getCurrentNetID().equals("56")){
//            result = bscApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(hash!!?:"")).execute().body()!!
//        }else if(CoinNameCheck.getCurrentNetID().equals("128")){
//            result = hecoApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(hash!!?:"")).execute().body()!!
//        }else if(CoinNameCheck.getCurrentNetID().equals("1")){
//            result = ethApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(hash!!?:"")).execute().body()!!
//        }
//    }

    fun checkStatus(status:Int){
        var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包
        if (transactionInfo.from.equals(walletInfo.address)){
            //转账
                if (status == 1){
                    tv_status.setText(resources.getString(R.string.zzcg))
                }else if(status == 2){
                    tv_status.setText(resources.getString(R.string.zzsb))
                } else{
                    tv_status.setText(resources.getString(R.string.jyqrz))
                }
        }else{
            //收款
                if (status == 1){
                    tv_status.setText(resources.getString(R.string.skcg))
                }else if(status == 2){
                    tv_status.setText(resources.getString(R.string.sksb))
                }else{
                    tv_status.setText(resources.getString(R.string.jyqrz))
                }

        }
    }

    override fun loadData() {
        var addrInfo = WalletOperator.currentWallet!!
        if (addrInfo == null) {
            throw AccountNotFoundException()
        }
        val nodeInfo = ChainNodeOperator.queryCurrentNode(addrInfo.chainType!!)
        var web3j: Web3j = Web3j.build(HttpService(nodeInfo.nodeUrl.toString()))

        Thread({
            var result:Eth_GetTransactionByHashResp? = null
            if (CoinNameCheck.getCurrentNetID().equals("56")){
                result = bscApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(hash!!?:"")).execute().body()!!
            }else if(CoinNameCheck.getCurrentNetID().equals("128")){
                result = hecoApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(hash!!?:"")).execute().body()!!
            }else if(CoinNameCheck.getCurrentNetID().equals("1")){
                result = ethApi.eth_getTransactionByHash(Eth_GetTransactionByHashReq(hash!!?:"")).execute().body()!!
            }

            if (nodeInfo.chainType.equals("CVN")){
                CommonTool.mainHandler.post{
                    tv_gas.setText("0CVN")
                }
            }else{
                val TransactionReceipt = web3j.ethGetTransactionReceipt(hash!!?:"").send().result
                var gasLimit = BigInteger("0")
                if (TransactionReceipt == null){

                }else{
                    gasLimit = TransactionReceipt.gasUsed
                }
                var gasPrice = BigDecimal("0")
                if (result == null||result.result==null){

                }else{
                    gasPrice = BytesHelper.hexStr2BigDecimal(result?.result!!.gasPrice,0,0);
                }

                var gasUse = ETHChainUtil.compateGasRetWei(gasLimit.toString()?:"21000", gasPrice.toPlainString())
                CommonTool.mainHandler.post{
                    tv_gas.setText(gasUse.extWei2Ether()+CoinNameCheck.getMainCoinName())
                }
            }

        }).start()
    }

    fun getChainBrowseURL():String{
        var walletInfo: WalletDao = WalletOperator.currentWallet!! //当前钱包

        val chainType = walletInfo.chainType

        if (chainType.equals("ETH")){
            return "https://cn.etherscan.com/tx/"+transactionInfo.transaction_hash
        }else if(chainType.equals("BSC")){
            return "https://bscscan.com/tx/"+transactionInfo.transaction_hash
        }else if(chainType.equals("HECO")){
            return "https://hecoinfo.com/tx/"+transactionInfo.transaction_hash
        }else if(chainType.equals("CVN")){
            return "https://scan.cvn.io/#/transactiondetail/"+transactionInfo.transaction_hash
        }else{
            return ""
        }
    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}