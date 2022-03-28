package com.fanrong.frwallet.ui.login

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.found.ViewPageAdapter
import com.fanrong.frwallet.ui.createwallet.CreateWalletStep1Activity
import com.fanrong.frwallet.ui.importwallet.ImportWalletActivity
import kotlinx.android.synthetic.main.activity_login.*
import xc.common.kotlinext.extStartActivity
import xc.common.tool.utils.AppManager


class LoginActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }

    override fun initView() {
        AppManager.getAppManager().finishOthersActivity(this)

//        viewpager.apply {
//            adapter = ImgAdapter().apply {
//                datas = mutableListOf(R.mipmap.src_lib_eui_icon_landing1, R.mipmap.src_lib_eui_icon_landing2, R.mipmap.src_lib_eui_icon_landing3)
//            }
//        }
//
//        vpp.apply {
//            addPoints(3)
//            setupViewPager(viewpager)
//        }


        rl_import.setOnClickListener {

//            ProtocolDialog(this).apply {
//                onConfrim = object : FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        extStartActivity(ImportWalletActivity::class.java)
//                    }
//                }
//            }.show()
            extStartActivity(ImportWalletActivity::class.java)
        }

        rl_create.setOnClickListener {

//            ProtocolDialog(this).apply {
//                onConfrim = object : FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        extStartActivity(CreateWalletStep1Activity::class.java)
//                    }
//                }
//            }.show()
            extStartActivity(CreateWalletStep1Activity::class.java)
        }
//        qy_wallet1.setOnClickListener {
//            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
//                putString(DappBrowserActivity.PARAMS_URL, FrConstants.MIGRATION_WALLET_1)
//            })
//        }
    }

    override fun loadData() {
    }


    class ImgAdapter : ViewPageAdapter<Int>() {
        override fun onInstantiateItem(container: ViewGroup, position: Int): Any {
            val inflate = LayoutInflater.from(container.context).inflate(R.layout.login_viewpage_item, container, false) as ImageView
            inflate.setImageResource(datas.get(position))
            container.addView(inflate)
            return inflate
        }

    }
}