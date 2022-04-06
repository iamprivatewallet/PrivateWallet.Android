package com.fanrong.frwallet.ui.address

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import kotlinx.android.synthetic.main.address_type_activity.*
import xc.common.kotlinext.extFinishWithAnim


class AddressTypeActivity : BaseActivity() {


    val addrAdapter: AddAddressTypeAdapter by lazy {
        AddAddressTypeAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                setResult(RESULT_OK, Intent().apply {
                    putExtra(FrConstants.WALLET_TYPE, addrAdapter.getItem(position)!!.name)
                })
                extFinishWithAnim()
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.address_type_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@AddressTypeActivity, getString(R.string.xzdzlx))
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@AddressTypeActivity)
            adapter = addrAdapter
        }

        addrAdapter.current = intent.getStringExtra(FrConstants.WALLET_TYPE)
        addrAdapter.setNewData(FrConstants.suport_chains)
    }

    override fun loadData() {
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}