package com.fanrong.frwallet.ui.fragment

import android.graphics.Color
import android.graphics.Typeface
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.recyclerview.widget.RecyclerView
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

    var tabs = mutableListOf<String>()
    override fun initView() {
        marketList1 = MarketListFragment(0)
        marketList2 = MarketListFragment(1)
        marketList3 = MarketListFragment(2)
        marketList4 = MarketListFragment(3)

        tabs.add(getString(R.string.zx))
        tabs.add(getString(R.string.zlb))
        tabs.add(getString(R.string.defi))
        tabs.add(getString(R.string.nft))

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

        var _index= 0;
        for (item in tabs){
            var tab = tab_layout.getTabAt(_index);
            val customView = tab!!.setCustomView(R.layout.item_tablayout)
            val text = tab!!.customView!!.findViewById<TextView>(R.id.tv_content)
            if (_index == 0){
//                text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
                text.setTextColor(Color.parseColor("#12D674"))
            }
            text.setText(item)
            _index++
        }

        tab_layout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val position = tab.position
                viewPager.setCurrentItem(position)
                tab.customView!!.findViewById<TextView>(R.id.tv_content)
                val text = tab!!.customView!!.findViewById<TextView>(R.id.tv_content)
//                text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17f)
                text.setTextColor(Color.parseColor("#12D674"))
                text.invalidate()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                tab.customView!!.findViewById<TextView>(R.id.tv_content)
                val text = tab!!.customView!!.findViewById<TextView>(R.id.tv_content)
//                text.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD))
                text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15f)
                text.setTextColor(Color.parseColor("#919CAA"))
                text.invalidate()
            }
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
    }

    override fun loadData() {
    }

    override fun onNoShakeClick(v: View) {

    }
}