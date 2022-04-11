package com.fanrong.frwallet.ui.fragment

import android.view.View
import com.fanrong.frwallet.R
import kotlinx.android.synthetic.main.fragment_marketlist.*
import xc.common.framework.ui.base.BaseFragment

class MarketListFragment(_type:Int): BaseFragment() {
    var type:Int = _type
    override fun getLayoutId(): Int {
        return R.layout.fragment_marketlist
    }

    override fun initView() {
        tv_test.setText(type.toString())
    }

    override fun loadData() {

    }

    override fun onNoShakeClick(v: View) {

    }
}