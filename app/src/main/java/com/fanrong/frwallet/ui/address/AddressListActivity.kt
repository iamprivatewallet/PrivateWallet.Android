package com.fanrong.frwallet.ui.address

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.AddressDao
import com.fanrong.frwallet.dao.database.AddressModelOperator
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.AddressBookListChangeEvent
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.activity.ImportAccountActivity
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.activity_wallet_manger.*
import kotlinx.android.synthetic.main.address_list_activity.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.LibViewUtils
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
            setOnItemChildClickListener{adapter, view, position ->
                if (view.id == addrAdapter.delectId){
                    val item = addrAdapter.getItem(position)
                    if (item != null){
                        AddressModelOperator.delete(item!!)
                        var allAddr: MutableList<AddressDao>? = AddressModelOperator.getAllAddr()
                        addrAdapter.setNewData(allAddr)
                        addrAdapter.notifyDataSetChanged()
                    }

                }
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.address_list_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@AddressListActivity, getString(R.string.dzb))
            rightTextView.text = getString(R.string.bj)
            rightTextView.setTextColor(resources.getColor(R.color.main_blue))
            rightTextView.visibility = View.VISIBLE
            LibViewUtils.setViewTwoState(rightTextView, onChangeState1 = {
                rightTextView.text = getString(R.string.bj)
                addrAdapter.isEdit = false
                addrAdapter.notifyDataSetChanged()
            }, onChangeState2 = {
                rightTextView.text = getString(R.string.wc)
                addrAdapter.isEdit = true
                addrAdapter.notifyDataSetChanged()
            })
            setBackIcon(R.mipmap.src_lib_eui_icon_back)
            setOnBackClickListener {
                extFinishWithAnim()
            }
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@AddressListActivity)
            adapter = addrAdapter
        }
        val footer: View? = LayoutInflater.from(this)?.inflate(R.layout.layout_wallet_footer, rcv_wm_wallet, false)
        val tv_title = footer?.findViewById<TextView>(R.id.tv_title)
        tv_title?.setText(resources.getString(R.string.tjlxr))
        addrAdapter.setFooterView(footer)
        footer!!.setOnClickListener{
            extStartActivity(AddAddressActivity::class.java, intent.extras ?: Bundle())
        }


        tv_clickadd.setOnClickListener{
            extStartActivity(AddAddressActivity::class.java, intent.extras ?: Bundle())
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

        OpenLockAppDialogUtils.OpenDialog(this)

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