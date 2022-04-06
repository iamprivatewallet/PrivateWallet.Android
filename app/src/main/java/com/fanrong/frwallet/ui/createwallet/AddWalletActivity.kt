package com.fanrong.frwallet.ui.createwallet

import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.import.ImportWalletDialog
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_add_coins.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim

class AddWalletActivity : BaseActivity() {
    val mAdapter: AddWalletAdapter by lazy {
        AddWalletAdapter().apply {
            setOnItemClickListener { adapter, view, position ->

                ImportWalletDialog(mAdapter.getItem(position)!!.name, this@AddWalletActivity).apply {
                }.show()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_wallet
    }

    override fun initView() {
        ac_title.apply {
            setTitleText("添加钱包")
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }
        rcv_ac_wallet.apply {
            layoutManager = LinearLayoutManager(this@AddWalletActivity)
            adapter = mAdapter
        }
        mAdapter.setNewData(FrConstants.suport_chains)
    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: WalletInfoChangeEvent) {
        extFinishWithAnim()
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}