package com.fanrong.frwallet.dapp.walletconnect

import com.basiclib.base.BaseActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.walletconnect.ConnectEvent
import com.fanrong.frwallet.dao.eventbus.walletconnect.StateChangeEvent
import com.fanrong.frwallet.dapp.walletconnect.page.ConnectedFragment
import com.fanrong.frwallet.dapp.walletconnect.page.RequestConnectFragment
import com.fanrong.frwallet.dapp.walletconnect.page.SignFragment
import com.fanrong.frwallet.dapp.walletconnect.page.TransferFragment
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.login.LoginActivity
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.wallet_connect_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.walletconnect.Session
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extShow
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.extGet


class WalletConnectActivity : BaseActivity() {

    val connectedFragment: ConnectedFragment by lazy {
        ConnectedFragment()
    }

    val requestConnectFragment: RequestConnectFragment by lazy {
        RequestConnectFragment()
    }

    val transferFragment: TransferFragment by lazy {
        TransferFragment()
    }
    val signFragment: SignFragment by lazy {
        SignFragment()
    }

    override fun getLayoutId(): Int {
        return R.layout.wallet_connect_activity
    }

    override fun initView() {
        var options: RequestOptions =  RequestOptions()
            .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
            .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
            .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
        Glide.with(iv_dapp_icon).load(WalletConnectUtil.dappPeerMeta?.icons.extGet(0)).apply(options).into(iv_dapp_icon)
        tv_dapp_name.setText(WalletConnectUtil.dappPeerMeta?.name)

        if (WalletOperator.currentWallet == null) {
            extStartActivity(LoginActivity::class.java)
            extFinishWithAnim()
            return
        }

        supportFragmentManager.beginTransaction().apply {

            var transaction = intent.getSerializableExtra(FrConstants.PP.WC_TRANSACTION) as? Session.MethodCall.SendTransaction
            var sign = intent.getSerializableExtra(FrConstants.PP.WC_SIGN) as? Session.MethodCall.SignMessage

            hide(transferFragment)
            hide(signFragment)
            hide(connectedFragment)
            hide(requestConnectFragment)
            if (transaction != null) {
                tv_type.setText("请求交易授权")
                extShow(R.id.framelayout, transferFragment)
            } else if (sign != null) {
                tv_type.setText("请求签名授权")
                extShow(R.id.framelayout, signFragment)
            } else if (WalletConnectUtil.isApproved) {
                tv_type.setText("")
                extShow(R.id.framelayout, connectedFragment)
            } else {
                tv_type.setText("请求连接钱包")
                extShow(R.id.framelayout, requestConnectFragment)
            }
            commitAllowingStateLoss()
        }

    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: ConnectEvent) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: StateChangeEvent) {
        initView()
    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }


}