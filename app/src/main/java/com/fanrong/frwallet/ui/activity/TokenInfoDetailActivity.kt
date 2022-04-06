package com.fanrong.frwallet.ui.activity

import android.util.Log
import com.basiclib.base.BaseActivity
import com.bumptech.glide.Glide
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.AddressDao
import com.fanrong.frwallet.dao.database.CoinDao
import com.fanrong.frwallet.dao.database.ConfigTokenDao
import com.fanrong.frwallet.dao.database.SearchHistoryResult
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.wallet.eth.centerApi
import com.fanrong.frwallet.wallet.eth.eth.QueryCoinReq
import com.fanrong.frwallet.wallet.eth.eth.QueryCoinResp
import kotlinx.android.synthetic.main.activity_tokeninfo_detail.*
import xc.common.framework.net.NetCallBack
import xc.common.framework.net.netSchduler
import xc.common.framework.net.subscribeObj
import xc.common.kotlinext.showToast
import xc.common.tool.utils.checkNotEmpty
import xc.common.utils.LibAppUtils

class TokenInfoDetailActivity : BaseActivity() {
    lateinit var tokenInfo:CoinDao
    override fun getLayoutId(): Int {
        return R.layout.activity_tokeninfo_detail
    }

    override fun initView() {
        tokenInfo = intent.getSerializableExtra(FrConstants.TOKEN_INFO) as CoinDao

        ac_title.apply{
            extInitCommonBgAutoBack(this@TokenInfoDetailActivity, resources.getString(R.string.dbxq))
        }

        Glide.with(iv_coinicon).load(tokenInfo?.getTokenIcon()).into(iv_coinicon)
        tv_coinname.setText(tokenInfo.coin_name)
        tv_chainname.setText(tokenInfo.chain_name)//通道
        tv_dbqc.setText(tokenInfo.coin_name)//代币全称
        tv_gw.setText("官网")
        tv_hydz.setText(tokenInfo.contract_addr)
        tv_dbzl.setText("代币总量")
        tv_fxsj.setText("发行时间")


        tv_gw.setOnClickListener{
            LibAppUtils.copyText(tv_gw.text.toString())
            showToast(resources.getString(R.string.copysuccess))
        }
        tv_hydz.setOnClickListener{
            LibAppUtils.copyText(tv_hydz.text.toString())
            showToast(resources.getString(R.string.copysuccess))
        }

    }

    override fun loadData() {
//        centerApi.queryCoin(QueryCoinReq(CoinNameCheck.getCurrentNetID(),"1",toString)).netSchduler()
//            .subscribeObj(object : NetCallBack<QueryCoinResp> {
//                override fun onSuccess(t: QueryCoinResp) {
//
//
//                }
//
//                override fun onFailed(error: Throwable) {
//
//                }
//
//            })
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}