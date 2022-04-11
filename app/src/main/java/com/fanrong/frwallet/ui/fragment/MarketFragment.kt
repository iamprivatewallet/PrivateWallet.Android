package com.fanrong.frwallet.ui.fragment

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.fanrong.frwallet.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.android.material.tabs.TabLayout.TabLayoutOnPageChangeListener
import kotlinx.android.synthetic.main.fragment_market.*
import xc.common.framework.ui.base.BaseFragment

class MarketFragment: BaseFragment() {

    lateinit var marketList1:MarketListFragment
    lateinit var marketList2:MarketListFragment
    lateinit var marketList3:MarketListFragment
    lateinit var marketList4:MarketListFragment
    override fun getLayoutId(): Int {
        return R.layout.fragment_market
    }

    override fun initView() {
        marketList1 = MarketListFragment(0)
        marketList2 = MarketListFragment(1)
        marketList3 = MarketListFragment(2)
        marketList4 = MarketListFragment(3)

        viewPager.setAdapter(object : FragmentStatePagerAdapter(
            this.activity!!.supportFragmentManager,
            FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
        ) {
            override fun getItem(position: Int): Fragment {
                return when (position) {
                    0 -> marketList1
                    1 -> marketList2
                    2 -> marketList3
                    else -> marketList4
                }
            }

            override fun getCount(): Int {
                return 4
            }
        })
        viewPager.addOnPageChangeListener(TabLayoutOnPageChangeListener(tab_layout))
        tab_layout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                viewPager.setCurrentItem(position)
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun loadData() {
    }

    override fun onNoShakeClick(v: View) {

    }
}