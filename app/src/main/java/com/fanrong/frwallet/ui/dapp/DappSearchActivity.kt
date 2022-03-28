package com.fanrong.frwallet.ui.dapp

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.DappRecentListAdapter
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.tools.bitMapAndStringConvertUtil
import kotlinx.android.synthetic.main.dapp_search_activity.*
import kotlinx.android.synthetic.main.dapp_search_activity.rcv_cl
import kotlinx.android.synthetic.main.fragment_wallet.*
import kotlinx.android.synthetic.main.wallet_asset_detail_activity.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.checkIsEmpty


class DappSearchActivity : BaseActivity() {

    val dappRecentListAdapter: DappRecentListAdapter by lazy {
        DappRecentListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putString(DappBrowserActivity.PARAMS_URL, dappRecentListAdapter.getItem(position)?.webUrl)
                    putString(FrConstants.PP.IS_DAPP,"1")
                })
            }
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.dapp_search_activity
    }

    override fun initView() {
        tv_cancel.setOnClickListener {
            extFinishWithAnim()
        }
        et_search.imeOptions = EditorInfo.IME_ACTION_SEARCH
        et_search.setOnEditorActionListener { v, actionId, event ->
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString(DappBrowserActivity.PARAMS_URL, et_search.text.toString())
                putString(FrConstants.PP.IS_DAPP,"1")
            })
            return@setOnEditorActionListener true
        }

        //ll_gotoDappbrowse
        //tv_dappurl
        et_search.addTextChangedListener(object : TextWatcherAfter() {

            override fun afterTextChanged(s: Editable?) {

                tv_dappurl.setText(s.toString())
                if (s.toString().checkIsEmpty()){
                    ll_gotoDappbrowse.visibility = View.GONE
                }else{
                    ll_gotoDappbrowse.visibility = View.VISIBLE
                }

            }
        })

        ll_gotoDappbrowse.setOnClickListener{
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString(DappBrowserActivity.PARAMS_URL, et_search.text.toString())
                putString(FrConstants.PP.IS_DAPP,"1")
            })
        }
        val allWebIconConfig = bitMapAndStringConvertUtil.getAllWebIconConfig()
        rcv_cl.apply {
            layoutManager = LinearLayoutManager(this@DappSearchActivity)
            adapter = dappRecentListAdapter
        }
        dappRecentListAdapter.setNewData(allWebIconConfig)
        dappRecentListAdapter.setEmptyView(R.layout.emptyviewlayout,rcv_cl)
    }

    override fun loadData() {
    }
}