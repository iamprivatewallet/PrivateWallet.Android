package com.fanrong.frwallet.dapp.walletconnect.page

import android.view.View
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectUtil
import com.fanrong.frwallet.tools.extFormatAddr
import kotlinx.android.synthetic.main.wc_fragment_connected.*
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.ext.extHasDefault

class ConnectedFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.wc_fragment_connected
    }

    override fun initView() {

        var wallet = WalletConnectUtil.getWalletNotNull()

        tv_wallet_addr.setText(wallet.address.extFormatAddr())
        tv_wallet_name.setText("(${wallet.walletName.extHasDefault(wallet.chainType!!)})")
        tv_url.setText(WalletConnectUtil.dappPeerMeta?.url)

        tv_disconnect.setOnClickListener {
            WalletConnectUtil.session.kill()
            extFinishWithAnim()
        }
    }

    override fun loadData() {
    }

    override fun onNoShakeClick(v: View) {
    }
}