package com.fanrong.frwallet.main

import android.content.Intent
import android.content.pm.PackageInfo
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.basiclib.base.BaseActivity
import com.codersun.fingerprintcompat.AonFingerChangeCallback
import com.codersun.fingerprintcompat.FingerManager
import com.codersun.fingerprintcompat.SimpleFingerCheckCallback
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.ConfigTokenOperator
import com.fanrong.frwallet.dao.eventbus.walletconnect.*
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectActivity
import com.fanrong.frwallet.dapp.walletconnect.WalletConnectUtil
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dapp.home.DappFragment
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.fragment.MarketFragment
import com.fanrong.frwallet.ui.fragment.MineFragment
import com.fanrong.frwallet.ui.fragment.WalletFragment
import com.fanrong.frwallet.view.appVersionDetailDialog
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryAppVersionReq
import com.fanrong.frwallet.wallet.eth.eth.QueryAppVersionResp
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_about_us.*
import kotlinx.android.synthetic.main.activity_main.*
import org.consenlabs.tokencore.wallet.KeystoreStorage
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.kotlinext.extStartActivityNewTask
import xc.common.kotlinext.showToast
import xc.common.tool.utils.AppManager
import xc.common.tool.utils.SPUtils
import xc.common.tool.utils.checkIsEmpty
import xc.common.utils.LibAppUtils
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.customview.ButtomTabView
import java.io.File


class MainActivity : BaseActivity(), KeystoreStorage {
    var fragmentWallet: WalletFragment? = null
    var fragmentMe: MineFragment? = null
    var fragmentDapp: DappFragment? = null
    var fragmentMarket:MarketFragment? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {

        AppManager.getAppManager().finishOthersActivity(this)

        var labs = mutableListOf<ButtomTabView.TabData>()
        labs.add(
            ButtomTabView.TabData(
                getString(R.string.qb),
                R.mipmap.icon_tabwallet_select,
                R.mipmap.icon_tabwallet_unselect
            )
        )
        labs.add(
            ButtomTabView.TabData(
                getString(R.string.shichang),
                R.mipmap.icon_market_select,
                R.mipmap.icon_market_unselect
            )
        )
        labs.add(
            ButtomTabView.TabData(
                getString(R.string.llq),
                R.mipmap.icon_dappbrowse_select,
                R.mipmap.icon_dappbrowse_unselect
            )
        )
        labs.add(
            ButtomTabView.TabData(
                getString(R.string.gd),
                R.mipmap.icon_more_select,
                R.mipmap.icon_more_unselect
            )
        )

        btv_tab.labInfos = labs

        btv_tab.onTabClickListener = object : ButtomTabView.OnTabClickListener {
            override fun onTabClicked(index: Int, data: ButtomTabView.TabData) {
                changeTab(index)
            }
        }
        btv_tab.clickTab(0)
        when (FingerManager.checkSupport(this@MainActivity)) {
            FingerManager.SupportResult.DEVICE_UNSUPPORTED -> {
//                showToast("???????????????????????????")
//                setFingerChecked(sth_finger, false)
            }
            FingerManager.SupportResult.SUPPORT_WITHOUT_DATA -> {
//                showToast("????????????????????????????????????")
//                setFingerChecked(sth_finger, false)
            }
            FingerManager.SupportResult.SUPPORT -> {
                FingerManager.updateFingerData(this)
            }
        }

        rl_dapp.setOnClickListener {
            AppManager.getAppManager().finishActivity(WalletConnectActivity::class.java)
            extStartActivityNewTask(WalletConnectActivity::class.java)
        }

        getAppInfo()
    }
    var appVersionCode:Int = 0
    fun getAppInfo(){
        var pkName = packageName;
        val packageInfo: PackageInfo = getApplicationContext().getPackageManager().getPackageInfo(getPackageName(), 0)
        appVersionCode = packageInfo.versionCode

    }
    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)

        ConfigTokenOperator.initServiceToken()

        if (SPUtils.getStaticValue("appUpdateStatus"+appVersionCode,0) != -1){
            centerApi.queryAppVersion(QueryAppVersionReq("zh_CN")).netSchduler()
                .subscribeObj(object : NetCallBack<QueryAppVersionResp> {
                    override fun onSuccess(t: QueryAppVersionResp) {
                        if (t.code == 1 && t.data != null){
                            if (appVersionCode<t.data?.code!!.toInt()){
                                appVersionDetailDialog(this@MainActivity as AppCompatActivity,t,appVersionCode).show()
                            }
                        }
                    }

                    override fun onFailed(error: Throwable) {

                    }

                })
        }
    }

    private fun changeTab(position: Int) {

        val transaction = supportFragmentManager.beginTransaction()
        if (fragmentWallet != null) {
            transaction.hide(fragmentWallet!!)
        }
        if (fragmentDapp != null) {
            transaction.hide(fragmentDapp!!)
        }
        if (fragmentMe != null) {
            transaction.hide(fragmentMe!!)
        }
        if (fragmentMarket != null){
            transaction.hide(fragmentMarket!!)
        }

        when (position) {
            0 -> {

                if (fragmentWallet == null) {
                    fragmentWallet = WalletFragment()
                }
                if (!fragmentWallet!!.isAdded) {
                    transaction.add(R.id.fl_content, fragmentWallet!!)
                }
                transaction.show(fragmentWallet!!)
            }
            1 -> {
                //fragmentMarket
                if (fragmentMarket == null) {
                    fragmentMarket = MarketFragment()
                }
                if (!fragmentMarket!!.isAdded) {
                    transaction.add(R.id.fl_content, fragmentMarket!!)
                }
                transaction.show(fragmentMarket!!)
            }
            2 -> {
                if (fragmentDapp == null) {
                    fragmentDapp = DappFragment()
                }
                if (!fragmentDapp!!.isAdded) {
                    transaction.add(R.id.fl_content, fragmentDapp!!)
                }
                transaction.show(fragmentDapp!!)
            }
            3 -> {
                if (fragmentMe == null) {
                    fragmentMe = MineFragment()
                }

                if (!fragmentMe!!.isAdded) {
                    transaction.add(R.id.fl_content, fragmentMe!!)
                }
                transaction.show(fragmentMe!!)
            }

        }
        transaction.commitAllowingStateLoss()
    }

    override fun getKeystoreDir(): File {
        return this.getFilesDir();
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent == null) {
            return
        }


        var qrInfo = intent!!.getStringExtra(FrConstants.PP.WALLET_QRCODE)
        if (qrInfo.checkIsEmpty()) {
            qrInfo = intent!!.data.toString()
        }
        WalletConnectUtil.connectSocket(wcString = qrInfo)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: ConnectEvent) {
        extShowOrDismissDialog(false)
        AppManager.getAppManager().finishActivity(WalletConnectActivity::class.java)
        extStartActivityNewTask(WalletConnectActivity::class.java)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: StateChangeEvent) {
        rl_dapp.extGoneOrVisible(isVisible = WalletConnectUtil.isConnected)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: TransactionEvent) {
        AppManager.getAppManager().finishActivity(WalletConnectActivity::class.java)
        extStartActivityNewTask(WalletConnectActivity::class.java, Bundle().apply {
            putSerializable(FrConstants.PP.WC_TRANSACTION, event.call)
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: SignEvent) {
        AppManager.getAppManager().finishActivity(WalletConnectActivity::class.java)
        extStartActivityNewTask(WalletConnectActivity::class.java, Bundle().apply {
            putSerializable(FrConstants.PP.WC_SIGN, event.call)
        })
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: DoConnectBeforeEvent) {
        extShowOrDismissDialog(true)
    }


    override fun onBackPressed() {
        LibAppUtils.doubleClose()
    }

    fun backHome() {
        AppManager.getAppManager().finishOthersActivity(this)
        btv_tab.switchToIndex(0)
    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }


}