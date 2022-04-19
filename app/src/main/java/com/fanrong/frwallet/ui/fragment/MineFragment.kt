package com.fanrong.frwallet.ui.fragment

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.tools.ShareUtils
import com.fanrong.frwallet.ui.activity.ApplicationLockActivity
import com.fanrong.frwallet.ui.activity.NetWorkManagerActivity
import com.fanrong.frwallet.ui.activity.WalletMangerActivity
import com.fanrong.frwallet.ui.activity.aboutUs
import com.fanrong.frwallet.ui.address.AddressListActivity
import com.fanrong.frwallet.ui.msgcenter.MessageCenterActivity
import com.fanrong.frwallet.ui.usesetting.UseSettingActivity
import com.yzq.zxinglibrary.EncodingUtils
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_mine.iv_downloadqrcode
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess

class MineFragment : BaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_mine
    }

    override fun initView() {
        rl_me_wallet.setOnClickListener {
            extStartActivity(WalletMangerActivity::class.java)
        }
        rl_me_addressbook.setOnClickListener {
            extStartActivity(AddressListActivity::class.java)
        }
        rl_me_dapphome.setOnClickListener {
            extStartActivity(aboutUs::class.java)
        }
        rl_me_editname.setOnClickListener {
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString(DappBrowserActivity.PARAMS_URL, FrConstants.USER_AGREEMENT)
            })
        }
        rl_me_explorer.setOnClickListener {
            showToast("探索")
        }
        rl_me_helpcenter.setOnClickListener {
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString(DappBrowserActivity.PARAMS_URL, FrConstants.HELP_FEEDBACK)
            })
        }
        rl_me_znz.setOnClickListener {
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString(DappBrowserActivity.PARAMS_URL, FrConstants.USER_GUIDE)
            })
        }
        rl_me_setting.setOnClickListener {
            extStartActivity(UseSettingActivity::class.java)
        }
        iv_message.setOnClickListener {
            extStartActivity(MessageCenterActivity::class.java)
        }
        rl_me_yys.setOnClickListener{
            extStartActivity(ApplicationLockActivity::class.java)
        }
        rl_me_network.setOnClickListener{
            extStartActivity(NetWorkManagerActivity::class.java)
        }



        iv_downloadqrcode.setImageBitmap(EncodingUtils.createQRCodeWithoutWhite("https://privatewallet.tech/download", 200, 200, null))
        rl_me_fxapp.setOnClickListener {
            ll_share_layout.visibility = View.VISIBLE
            LibPremissionUtils.needStore(this, object : PermissonSuccess {
                override fun hasSuccess() {
                    //打开系统分享
                    ll_share_content_invisible.setDrawingCacheEnabled(true)
                    ll_share_content_invisible.buildDrawingCache()
                    val bitmap: Bitmap = Bitmap.createBitmap(ll_share_content_invisible.getDrawingCache())
                    ShareUtils.shareImage(bitmap,"share_app",context)

//                    ShareReceiptQrDialog(this@ReceiptActivity).apply {
////                        bitmap = LibAppUtils.inviteViewToBitmap(this@ReceiptActivity.ll_share_content)
//                        receiptAddress = tokenInfo?.sourceAddr
//                        coinName = CoinNameCheck.getNameByName(tokenInfo.coin_name)
//                    }.show()
                }
            })
        }
        tv_cancel.setOnClickListener{
            ll_share_layout.visibility = View.GONE
        }
    }

    override fun loadData() {
    }

    override fun onNoShakeClick(v: View) {
    }
}