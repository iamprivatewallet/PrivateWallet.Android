package com.fanrong.frwallet.dapp.walletconnect.page

import android.view.View
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.data.NodeList
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectUtil
import com.fanrong.frwallet.tools.extFormatAddr
import com.fanrong.frwallet.view.SelectWalletListDialog
import kotlinx.android.synthetic.main.wc_fragment_request_connect.*
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.ext.extHasDefault
import xc.common.viewlib.view.customview.FullScreenDialog

class RequestConnectFragment : BaseFragment() {

    var wallet: WalletDao? = null

    override fun getLayoutId(): Int {
        return R.layout.wc_fragment_request_connect
    }

    override fun initView() {

        wallet = WalletOperator.currentWallet

        tv_change_wallet.setOnClickListener {
            SelectWalletListDialog(activity as BaseActivity).apply {
                onConfrim = object : FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        wallet = params as WalletDao
                        this@RequestConnectFragment.tv_wallet_addr.setText(wallet?.address?.extFormatAddr())
                        this@RequestConnectFragment.tv_wallet_name.setText("(${wallet?.walletName.extHasDefault(wallet!!.chainType!!)})")
                    }
                }
            }.show()
        }

        tv_wallet_addr.setText(wallet?.address?.extFormatAddr())
        tv_wallet_name.setText("(${wallet?.walletName.extHasDefault(wallet!!.chainType!!)})")

        btn_confirm.setOnClickListener {
            WalletConnectUtil.connectWallet = wallet
            WalletConnectUtil.session.approve(mutableListOf(wallet!!.address), NodeList.getCurrentNodeObj("ETH").chainId!!.toLong()!!)
        }

        btn_cancel.setOnClickListener {
            WalletConnectUtil.session.reject()
            extFinishWithAnim()
        }
    }

    override fun loadData() {
    }

    override fun onNoShakeClick(v: View) {
    }

    override fun onBackPressed() {
        if (isVisible) {
            WalletConnectUtil.session.kill()
        }
    }

}