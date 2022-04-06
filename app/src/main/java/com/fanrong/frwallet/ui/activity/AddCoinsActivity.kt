package com.fanrong.frwallet.ui.activity

import android.app.Activity
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.AddConinTypesAdapter
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.SuccessDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.wallet.WalletHelper
import kotlinx.android.synthetic.main.activity_add_coins.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.utils.checkNotEmpty

class AddCoinsActivity : BaseActivity() {

    val isFromCreate: Boolean by lazy {
        return@lazy FrConstants.PRE_PAGE_CREATE.equals(intent.getStringExtra(FrConstants.PRE_PAGE))
    }

    val mAdapter: AddConinTypesAdapter by lazy {
        AddConinTypesAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                val item = mAdapter.getItem(position)!!

                if (!item.isDefaultSupport) {
                    if (mAdapter.addCoins.contains(position)) {
                        mAdapter.addCoins.remove(position)
                    } else {
                        mAdapter.addCoins.add(position)
                    }
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_add_coins
    }

    override fun initView() {
        ac_title.apply {
//            setTitleText("添加币种")
            extInitCommonBgAutoBack(this@AddCoinsActivity,"添加币种")
            if (isFromCreate) {
                setBackBtnHide()
            }
        }
        rcv_ac_wallet.apply {
            layoutManager = LinearLayoutManager(this@AddCoinsActivity)
            adapter = mAdapter
        }

        var walletlisttype = mutableListOf<String>()
        for (greWalletModel in WalletOperator.queryMainWallet()) {
            walletlisttype.add(greWalletModel.chainType!!)
        }

        for (suportChain in FrConstants.suport_chains) {
            suportChain.isDefaultSupport = walletlisttype.contains(suportChain.name)
        }


        mAdapter.setNewData(FrConstants.suport_chains)
        tv_ac_confirm.setOnClickListener {

            if (mAdapter.addCoins.checkNotEmpty()) {
                for (position in mAdapter.addCoins) {
                    WalletHelper.createMainWallet(mAdapter.getItem(position)!!.name, WalletOperator.queryMainWallet()[0].mnemonic!!)
                }

                SuccessDialog("添加成功", this).apply {
                    setOnDismissListener {
                        setResult(Activity.RESULT_OK)
                        extFinishWithAnim()
                        EventBus.getDefault().post(WalletInfoChangeEvent())
                    }
                }.show()

            } else {
                setResult(Activity.RESULT_OK)
                extFinishWithAnim()
            }
        }
    }

    override fun loadData() {
    }

    override fun onBackPressed() {
        // 创建身份过来不让返回
        if (!isFromCreate) {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }

}