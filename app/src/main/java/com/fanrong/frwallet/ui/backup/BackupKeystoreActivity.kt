package com.fanrong.frwallet.ui.backup

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.ui.dialog.ExportPrivateKeyHintDialog
import com.fanrong.frwallet.wallet.WalletHelper
import com.flyco.tablayout.listener.OnTabSelectListener
import com.yzq.zxinglibrary.EncodingUtils
import kotlinx.android.synthetic.main.backup_keystore_activity.*
import kotlinx.android.synthetic.main.backup_keystore_activity.ac_title
import kotlinx.android.synthetic.main.backup_keystore_activity.btn_copy_pri
import kotlinx.android.synthetic.main.backup_keystore_activity.iv_qrcode
import kotlinx.android.synthetic.main.backup_keystore_activity.ll_qrcode
import kotlinx.android.synthetic.main.backup_keystore_activity.ll_text
import kotlinx.android.synthetic.main.backup_keystore_activity.rl_hide_qr
import kotlinx.android.synthetic.main.backup_keystore_activity.tab_layout
import kotlinx.android.synthetic.main.backup_keystore_activity.tv_hide_qr
import kotlinx.android.synthetic.main.backup_private_key_activity.*
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils
import xc.common.viewlib.tabslayout.TabEntity
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog


class BackupKeystoreActivity : BaseActivity() {


    val walletInfo: WalletDao by lazy {
        intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
    }

    override fun getLayoutId(): Int {
        return R.layout.backup_keystore_activity
    }

    override fun initView() {

        ac_title.apply {
            extInitCommonBgAutoBack(this@BackupKeystoreActivity, "导出 Keystore")
        }

        tab_layout.apply {
            var tabs = mutableListOf<TabEntity>()
            tabs.add(TabEntity("Keystore"))
            tabs.add(TabEntity("二维码"))
            setTabData(ArrayList(tabs))
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    ll_text.extGoneOrVisible(position == 0)
                    ll_qrcode.extGoneOrVisible(position == 1)
                }

                override fun onTabReselect(position: Int) {
                }
            })
        }


        WalletHelper.getWalletKeystore(walletInfo, walletInfo.password) {
            if (it.success) {
                tv_keystore.setText(it.result.toString())
                iv_qrcode.setImageBitmap(EncodingUtils.createQRCodeWithoutWhite(tv_keystore.text.toString(), 400, 400, null))
            } else {
                showToast(it.error.toString())
            }
        }


        tab_layout.currentTab = 0

        ll_text.extGoneOrVisible(true)
        ll_qrcode.extGoneOrVisible(false)

        tv_hide_qr.setOnClickListener {
            rl_hide_qr.extGoneOrVisible(false)
            iv_qrcode.extGoneOrVisible(true)
        }
        iv_qrcode.setOnClickListener {
            rl_hide_qr.extGoneOrVisible(true)
            iv_qrcode.extGoneOrVisible(false)
        }
        iv_qrcode.callOnClick()


        btn_copy_pri.setOnClickListener {
            val toString = tv_keystore.text.toString()
            ExportPrivateKeyHintDialog(this).apply {
                type = "Keystore"
                onConfrim = object : FullScreenDialog.OnConfirmListener {
                    override fun confirm(params: Any?) {
                        LibAppUtils.copyText(toString)
                        showToast("复制成功")
                    }
                }
            }.show()
        }
    }

    override fun loadData() {
    }
}