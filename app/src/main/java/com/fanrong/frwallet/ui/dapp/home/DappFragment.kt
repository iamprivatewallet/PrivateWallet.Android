package com.fanrong.frwallet.ui.dapp.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.*
import com.fanrong.frwallet.dao.eventbus.DappHistoryEvent
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.GlideRoundTransform
import com.fanrong.frwallet.ui.activity.SearchDappAndTokenActivity
import com.fanrong.frwallet.ui.activity.SearchTokenActivity
import com.fanrong.frwallet.ui.dapp.DappSearchActivity
import com.fanrong.frwallet.ui.dapp.history.DappHistoryListActivity
import com.fanrong.frwallet.ui.dapp.star.DappStarListActivity
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.*
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import com.flyco.tablayout.listener.OnTabSelectListener
import com.stx.xhb.androidx.XBanner.XBannerAdapter
import com.stx.xhb.androidx.entity.BaseBannerInfo
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.activity_searchdappandtoken.*
import kotlinx.android.synthetic.main.fragment_dapp.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.ext.extIsNetUrl
import xc.common.tool.utils.SPUtils
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkNotEmpty
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.extension.extUpdateBg
import xc.common.viewlib.tabslayout.TabEntity
import xc.common.viewlib.utils.extGoneOrVisible


class DappFragment : BaseFragment() {

    var dataDTO:QueryDappHomeResp.DataDTO? = null

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
                                showToast(getString(R.string.smddfwz))
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
//            extStartActivity(DappSearchActivity::class.java)
            var currentWallet: WalletDao = WalletOperator.currentWallet!!
            extStartActivity(SearchDappAndTokenActivity::class.java,Bundle().apply {
                putSerializable(FrConstants.WALLET_INFO,currentWallet)
            })
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
    }

    fun refresh(){
        setBanner()
        setTJAndRecent()
        setTjPosition()
    }
    fun setTjPosition(){
        if (dataDTO!=null && dataDTO!!.banner_2_2 != null && dataDTO!!.banner_2_2!!.size > 0){
            var options: RequestOptions =  RequestOptions()
                .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
                .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
                .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
            Glide.with(iv_leftposition).load(dataDTO!!.banner_2_2!!.get(0).imgH5).apply(options).into(iv_leftposition)
            iv_leftposition.setOnClickListener{
                if (dataDTO!!.banner_2_2!!.get(0).clickUrl.checkNotEmpty()){
                    extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                        putString(DappBrowserActivity.PARAMS_URL,dataDTO!!.banner_2_2!!.get(0).clickUrl )
                        putString(FrConstants.PP.IS_DAPP,"1")
                    })
                }
            }
        }

        if (dataDTO!=null && dataDTO!!.banner_2_3 != null && dataDTO!!.banner_2_3!!.size > 0){
            var options: RequestOptions =  RequestOptions()
                .placeholder(R.mipmap.src_lib_eui_icon_defaultdappicon)//图片加载出来前，显示的图片
                .fallback( R.mipmap.src_lib_eui_icon_defaultdappicon) //url为空的时候,显示的图片
                .error(R.mipmap.src_lib_eui_icon_defaultdappicon);//图片加载失败后，显示的图片
            Glide.with(iv_rightpositon).load(dataDTO!!.banner_2_3!!.get(0).imgH5).apply(options).into(iv_rightpositon)
            iv_rightpositon.setOnClickListener{
                if (dataDTO!!.banner_2_3!!.get(0).clickUrl.checkNotEmpty()){
                    extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                        putString(DappBrowserActivity.PARAMS_URL,dataDTO!!.banner_2_3!!.get(0).clickUrl )
                        putString(FrConstants.PP.IS_DAPP,"1")
                    })
                }
            }
        }
    }
    fun setBanner(){
        val bannerList = mutableListOf<BannerBean>()

        if (dataDTO!=null&&dataDTO!!.banner_1_1!=null){
            for (item in dataDTO!!.banner_1_1!!){
                bannerList.add(BannerBean(item.imgH5!!,item.clickUrl!!,item.title!!))
            }
        }

        //设置数据源
        mBanner.setBannerData(bannerList)

        //设置点击事件
        mBanner.setOnItemClickListener{ banner, model, view, position ->
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString(DappBrowserActivity.PARAMS_URL, bannerList.get(position).clickUrl)
                putString(FrConstants.PP.IS_DAPP,"1")
            })
        }
        //图片加载
        mBanner.loadImage(XBannerAdapter { banner, model, view, position -> //1、此处使用的Glide加载图片，可自行替换自己项目中的图片加载框架
            //2、返回的图片路径为Object类型，你只需要强转成你传输的类型就行，切记不要胡乱强转！

            val myOptions = RequestOptions()
                .transform(GlideRoundTransform(activity, 10))

            Glide.with(activity).load(bannerList.get(position).xBannerUrl).apply(myOptions).into(view as ImageView)

        })

    }
    fun setTJAndRecent(){
        if (tab_layout.currentTab == 0) {
            //推荐(收藏)
//            val queryStar = DappStarOperator.queryStar()
            var list = mutableListOf<DappInfoDao>()
            var current_index = 0
            if (dataDTO!=null&&dataDTO!!.dappTop!=null){
                for (dappStarDao in dataDTO!!.dappTop!!) {
                    if (current_index < 4){
                        list.add(DappInfoDao().apply {
                            this.icon = dappStarDao.iconUrl
                            this.des = dappStarDao.description
                            this.name = dappStarDao.appName
                            this.url = dappStarDao.appUrl
                        })
                    }
                    current_index++
                }
            }
            if (current_index >= 4){
                ll_all.visibility = View.VISIBLE
            }else{
                ll_all.visibility = View.GONE
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

        EventBus.getDefault().extRegisterAutoUnregister(this)

        centerApi.queryDappHomeMain(QueryDappHomeReq(CoinNameCheck.getCurrentNetID())).netSchduler()
            .subscribeObj(object : NetCallBack<QueryDappHomeResp> {
                override fun onSuccess(t: QueryDappHomeResp) {
                    dataDTO = t.data
                    refresh()
                }

                override fun onFailed(error: Throwable) {
                    Log.d("fwef", "onFailed: ")
                }
            })
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
    var clickUrl:String? = ""
    var title:String? = ""
    constructor(imgUrl:String,_clickurl:String,_title:String){
        url = imgUrl
        clickUrl = _clickurl
        title = _title

    }
    override fun getXBannerUrl(): String? {
        return url
    }

    override fun getXBannerTitle(): String? {
        return ""
    }

}