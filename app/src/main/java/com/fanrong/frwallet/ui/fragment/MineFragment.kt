package com.fanrong.frwallet.ui.fragment

import android.os.Bundle
import android.view.View
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.ui.activity.WalletMangerActivity
import com.fanrong.frwallet.ui.activity.aboutUs
import com.fanrong.frwallet.ui.address.AddressListActivity
import com.fanrong.frwallet.ui.msgcenter.MessageCenterActivity
import com.fanrong.frwallet.ui.usesetting.UseSettingActivity
import kotlinx.android.synthetic.main.fragment_mine.*
import xc.common.framework.ui.base.BaseFragment
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast

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

    }

    override fun loadData() {
    }

    override fun onNoShakeClick(v: View) {
    }
}