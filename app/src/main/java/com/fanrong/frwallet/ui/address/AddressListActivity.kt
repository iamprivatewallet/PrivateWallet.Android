package com.fanrong.frwallet.ui.address

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.AddressDao
import com.fanrong.frwallet.dao.database.AddressModelOperator
import com.fanrong.frwallet.dao.eventbus.AddressBookListChangeEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.address_list_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.utils.extGoneOrVisible


class AddressListActivity : BaseActivity() {

    val addrAdapter: AddressListAdapter by lazy {
        AddressListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                if (intent.getStringExtra(FrConstants.CHAIN_TYPE).checkNotEmpty()) {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra(FrConstants.ADDR_INFO, addrAdapter.getItem(position)!!.address)
                    })
                    extFinishWithAnim()
                } else {

                    EditAddrDialog(this@AddressListActivity).apply {
                        addrInfo = addrAdapter.getItem(position)!!
                    }.show()
                }

            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.address_list_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@AddressListActivity, "地址薄")

            setRightBtnIconAndClick(R.mipmap.src_lib_eui_icon_addnode) {
                extStartActivity(AddAddressActivity::class.java, intent.extras ?: Bundle())
            }
        }



        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@AddressListActivity)
            adapter = addrAdapter
        }


    }

    override fun onResume() {
        super.onResume()
        if (!intent.getStringExtra(FrConstants.CHAIN_TYPE).checkNotEmpty()) {
            var allAddr: MutableList<AddressDao>? = AddressModelOperator.getAllAddr()
            addrAdapter.setNewData(allAddr)
            ll_empty_layout.extGoneOrVisible(allAddr.checkIsEmpty())
            recyclerview.extGoneOrVisible(allAddr.checkNotEmpty())
        }

    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)


        var allAddr: MutableList<AddressDao>?
        if (intent.getStringExtra(FrConstants.CHAIN_TYPE).checkNotEmpty()) {
            allAddr = AddressModelOperator.getAllAddrByType(intent.getStringExtra(FrConstants.CHAIN_TYPE))
        } else {
            allAddr = AddressModelOperator.getAllAddr()
        }

        ll_empty_layout.extGoneOrVisible(allAddr.checkIsEmpty())
        recyclerview.extGoneOrVisible(allAddr.checkNotEmpty())
        addrAdapter.setNewData(allAddr)
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: AddressBookListChangeEvent) {
        loadData()
    }
}