package com.fanrong.frwallet.found

import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

abstract class ViewPageAdapter<T> : PagerAdapter() {
    lateinit var datas:MutableList<T>

    override fun getCount(): Int {
        return datas.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        return onInstantiateItem(container, position)
//        return super.instantiateItem(container, position)
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        super.destroyItem(container, position, `object`)
        container.removeView(`object` as View?)
    }

    abstract fun onInstantiateItem(container: ViewGroup, position: Int):Any


}