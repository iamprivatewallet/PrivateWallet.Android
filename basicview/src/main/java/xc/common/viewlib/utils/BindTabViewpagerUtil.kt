package xc.common.viewlib.utils

import androidx.viewpager.widget.ViewPager
import com.flyco.tablayout.CommonTabLayout
import com.flyco.tablayout.listener.OnTabSelectListener

object BindTabViewpagerUtil {

    fun bind(tabLayout: CommonTabLayout, viewPager: ViewPager) {

        tabLayout.setOnTabSelectListener(object : OnTabSelectListener {
            override fun onTabSelect(position: Int) {
                if (viewPager.currentItem != position) {
                    viewPager.currentItem = position
                }
            }

            override fun onTabReselect(position: Int) {
            }
        })

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (tabLayout.currentTab != position) {
                    tabLayout.currentTab = position
                }
            }
        })
    }

}