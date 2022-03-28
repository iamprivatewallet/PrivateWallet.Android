package com.fanrong.frwallet.ui.dapp.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.DappHistoryOperator
import com.fanrong.frwallet.dao.database.DappInfoDao
import com.fanrong.frwallet.dao.database.DappStarOperator
import com.fanrong.frwallet.dao.eventbus.DappHistoryEvent
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.GlideRoundTransform
import com.fanrong.frwallet.ui.dapp.DappSearchActivity
import com.fanrong.frwallet.ui.dapp.history.DappHistoryListActivity
import com.fanrong.frwallet.ui.dapp.star.DappStarListActivity
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import com.flyco.tablayout.listener.OnTabSelectListener
import com.stx.xhb.androidx.XBanner.XBannerAdapter
import com.stx.xhb.androidx.entity.BaseBannerInfo
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.fragment_dapp.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.ext.extIsNetUrl
import xc.common.tool.utils.checkIsEmpty
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.extension.extUpdateBg
import xc.common.viewlib.tabslayout.TabEntity
import xc.common.viewlib.utils.extGoneOrVisible


class DappFragment : BaseFragment() {

    val viewmodel: DappViewmodel by lazy {
        DappViewmodel()
    }

    val chainTypeTabDatas: MutableList<TabEntity> by lazy {
        mutableListOf<TabEntity>().apply {
            add(TabEntity("ETH"))
            add(TabEntity("HECO"))
            add(TabEntity("BSC"))
            add(TabEntity("CVN"))
        }
    }

    val starResentTabDatas: MutableList<TabEntity> by lazy {
        mutableListOf<TabEntity>().apply {
            add(TabEntity(resources.getString(R.string.tj)))
            add(TabEntity(resources.getString(R.string.ll)))
        }
    }


    val recentAdapter: DappRecentAdapter by lazy {
        DappRecentAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putSerializable(DappBrowserActivity.PARAMS_DAPP, recentAdapter.getItem(position)!!)
                    putString(FrConstants.PP.IS_DAPP, "1")
                })
            }
        }
    }

    val recomendAdapter: DappRecommendAdapter by lazy {
        DappRecommendAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                    putSerializable(DappBrowserActivity.PARAMS_DAPP, DappInfoDao().apply {
                        var netModel = recomendAdapter.getItem(position)!!
                        this.des = netModel.description
                        this.url = netModel.appUrl!!
                        this.name = netModel.appName
                        this.icon = netModel.iconUrl
                    })

                    putString(FrConstants.PP.IS_DAPP, "1")
                })
            }
        }
    }


    override fun getLayoutId(): Int {
        return R.layout.fragment_dapp
    }

    override fun initView() {

        iv_scan.setOnClickListener {
            LibPremissionUtils.needCamera(this,object : PermissonSuccess {
                override fun hasSuccess() {
                    extStartActivityForResult(CaptureActivity::class.java, 101) { resultCode: Int, data: Intent? ->
                        if (resultCode == Activity.RESULT_OK) {
                            var result = data?.getStringExtra(Constant.CODED_CONTENT) ?: ""
                            if (result.extIsNetUrl()) {
                                extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                                    putString(DappBrowserActivity.PARAMS_URL, result)
                                    putString(FrConstants.PP.IS_DAPP, "1")
                                })
                            } else {
                                showToast("扫描内容非网页地址")
                            }
                        }
                    }
                }

            })

        }

        tab_layout_chain_type.apply {
            setTabData(ArrayList(chainTypeTabDatas))
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    loadData()
                }

                override fun onTabReselect(position: Int) {
                }

            })
        }
        tab_layout_chain_type.currentTab = 0

        tab_layout.apply {
            setTabData(ArrayList(starResentTabDatas))
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    loadData()
                }

                override fun onTabReselect(position: Int) {
                }

            })
        }
        tab_layout.currentTab = 0

        ll_all.setOnClickListener {
            if (tab_layout.currentTab == 0) {
                extStartActivity(DappStarListActivity::class.java)
            } else {
                extStartActivity(DappHistoryListActivity::class.java)
            }
        }


        et_search.setOnClickListener {
            extStartActivity(DappSearchActivity::class.java)
        }

        rl_recent.apply {
            layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            adapter = recentAdapter
        }
//        recentAdapter.setEmptyView(R.layout.emptyviewlayout,rl_recent)

        rv_dapp_recommend.apply {
            layoutManager = GridLayoutManager(activity,2)//LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
            adapter = recomendAdapter
            //分割线
//            addItemDecoration(ListDivider.Builder().setDividerColor(Color.parseColor("#eeeeee")).build())
        }
        recomendAdapter.setEmptyView(R.layout.emptyviewlayout,rv_dapp_recommend)

        iv_close_help_layout.setOnClickListener {
//            rl_how_star.extGoneOrVisible(isVisible = false)
            rl_recent.extGoneOrVisible(true)
        }
//        rl_how_star.setOnClickListener {
//            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
//                putString(DappBrowserActivity.PARAMS_URL, FrConstants.HELPER_HOW_STAR)
//            })
//        }

        viewmodel.observerDataChange(this, this::stateChange)


        setBanner()
    }


    fun setBanner(){
        val mutableListOf = mutableListOf<BannerBean>()
        var item1 = BannerBean("http://www.pptok.com/wp-content/uploads/2012/08/xunguang-7.jpg")
        var item2 = BannerBean("http://imageprocess.yitos.net/images/public/20160910/99381473502384338.jpg")
        var item3 = BannerBean("http://imageprocess.yitos.net/images/public/20160910/77991473496077677.jpg")
        var item4 = BannerBean("http://imageprocess.yitos.net/images/public/20160906/1291473163104906.jpg")
        mutableListOf.add(item1)
        mutableListOf.add(item2)
        mutableListOf.add(item3)
        mutableListOf.add(item4)

        //设置数据源
        mBanner.setBannerData(mutableListOf)

        //设置点击事件
        mBanner.setOnItemClickListener{ banner, model, view, position ->
            showToast(position.toString())
        }
        //图片加载
        mBanner.loadImage(XBannerAdapter { banner, model, view, position -> //1、此处使用的Glide加载图片，可自行替换自己项目中的图片加载框架
            //2、返回的图片路径为Object类型，你只需要强转成你传输的类型就行，切记不要胡乱强转！

            val myOptions = RequestOptions()
                .transform(GlideRoundTransform(activity, 10))

            Glide.with(activity).load(mutableListOf.get(position).xBannerUrl).apply(myOptions).into(view as ImageView)

        })
    }

    fun stateChange(state: DappViewmodel.State) {
        extShowOrDismissDialog(state.isShowLoading)

        state.errorinfo?.run {
            showToast(this.msg)
        }

        state.initDappResult?.run {
            recomendAdapter.setNewData(resultData)
        }
        state?.loadMoreResult?.run {
            recomendAdapter.data.addAll(resultData!!)
            recomendAdapter.notifyDataSetChanged()
        }
    }

    override fun loadData() {
        viewmodel.queryRecommentDapp(chainTypeTabDatas.get(tab_layout_chain_type.currentTab).title)
        rl_recent.extGoneOrVisible(true)
//        rl_how_star.extGoneOrVisible(false)

        if (tab_layout.currentTab == 0) {
            //收藏
            val queryStar = DappStarOperator.queryStar()
            var list = mutableListOf<DappInfoDao>()
            var current_index = 0
            for (dappStarDao in queryStar) {
                if (current_index < 4){
                    list.add(DappInfoDao().apply {
                        this.icon = dappStarDao.icon
                        this.des = dappStarDao.des
                        this.name = dappStarDao.name
                        this.url = dappStarDao.url
                    })
                }
                current_index++
            }

            if (current_index >= 4){
                ll_all.visibility = View.VISIBLE
            }else{
                ll_all.visibility = View.GONE
            }

            if (queryStar.checkIsEmpty()) {
//                rl_recent.extGoneOrVisible(false)
//                rl_how_star.extGoneOrVisible(true)
            }

            recentAdapter.setNewData(list)
            rl_recent.extUpdateBg(R.drawable.rl_empty_bg_dapp)
        } else {
            //最近的浏览记录
            val queryDappSortByTime = DappHistoryOperator.queryDappSortByTime()
            var list = mutableListOf<DappInfoDao>()
            var current_index = 0
            for (history in queryDappSortByTime) {
                if (current_index < 4){
                    list.add(DappInfoDao().apply {
                        this.icon = history.icon
                        this.des = history.des
                        this.name = history.name
                        this.url = history.url
                    })
                }
                current_index++
            }

            if (current_index >= 4){
                ll_all.visibility = View.VISIBLE
            }else{
                ll_all.visibility = View.GONE
            }

            recentAdapter.setNewData(list)
            rl_recent.extUpdateBg(R.drawable.rl_empty_bg_dapp)
        }
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    override fun onNoShakeClick(v: View) {
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: DappHistoryEvent) {
        loadData()
    }
}

class BannerBean:BaseBannerInfo {
    var url:String? = ""
    constructor(imgUrl:String){
        url = imgUrl
    }
    override fun getXBannerUrl(): String? {
        return url
    }

    override fun getXBannerTitle(): String? {
        return ""
    }

}