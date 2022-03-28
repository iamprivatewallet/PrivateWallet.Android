package com.fanrong.frwallet.dapp.walletconnect.page

import android.view.View
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectUtil
import com.fanrong.frwallet.tools.CallJsCodeUtils
import com.fanrong.frwallet.tools.Web3JsUtils
import kotlinx.android.synthetic.main.wc_fragment_sign.*
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.showToastAtMainThread
import xc.common.tool.utils.checkIsEmpty

class SignFragment : BaseFragment() {

    lateinit var wallet: WalletDao

    override fun getLayoutId(): Int {
        return R.layout.wc_fragment_sign
    }

    override fun initView() {
        wallet = WalletOperator.currentWallet!!

        if (WalletConnectUtil.signMessage == null || WalletConnectUtil.signMessage!!.message.checkIsEmpty()) {
            extFinishWithAnim()
            return
        }

        et_sign.setText(WalletConnectUtil.signMessage?.message)

        btn_confirm.setOnClickListener {
            signMsg()
        }

        btn_cancel.setOnClickListener {
            WalletConnectUtil.session.rejectRequest(WalletConnectUtil.signMessage?.id ?: 0, 0, "")
            extFinishWithAnim()
        }
    }

    private fun signMsg() {
        Web3JsUtils.signStr(WalletConnectUtil.signMessage!!.message!!, wallet.privateKey) {
            var signMsg = CallJsCodeUtils.readStringJsValue(it)
            if (signMsg.checkIsEmpty()) {
                showToastAtMainThread("签名失败")
                WalletConnectUtil.session.rejectRequest(WalletConnectUtil.signMessage?.id ?: 0, 0, "")
            } else {
                showToastAtMainThread("签名成功")
                WalletConnectUtil.session.approveRequest(WalletConnectUtil.signMessage?.id ?: 0, signMsg)
            }
            extFinishWithAnim()
        }
    }

    override fun loadData() {
    }

    override fun onNoShakeClick(v: View) {
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (isVisible) {
            WalletConnectUtil.session.rejectRequest(WalletConnectUtil.signMessage?.id ?: 0, 0, "")
        }
    }
}