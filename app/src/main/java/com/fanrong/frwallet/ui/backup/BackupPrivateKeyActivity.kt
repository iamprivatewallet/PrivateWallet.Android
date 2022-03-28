package com.fanrong.frwallet.ui.backup

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.ui.dialog.ExportPrivateKeyHintDialog
import com.flyco.tablayout.listener.OnTabSelectListener
import com.yzq.zxinglibrary.EncodingUtils
import kotlinx.android.synthetic.main.backup_keystore_activity.*
import kotlinx.android.synthetic.main.backup_private_key_activity.*
import kotlinx.android.synthetic.main.backup_private_key_activity.ac_title
import kotlinx.android.synthetic.main.backup_private_key_activity.btn_copy_pri
import kotlinx.android.synthetic.main.backup_private_key_activity.iv_qrcode
import kotlinx.android.synthetic.main.backup_private_key_activity.ll_qrcode
import kotlinx.android.synthetic.main.backup_private_key_activity.ll_text
import kotlinx.android.synthetic.main.backup_private_key_activity.rl_hide_qr
import kotlinx.android.synthetic.main.backup_private_key_activity.tab_layout
import kotlinx.android.synthetic.main.backup_private_key_activity.tv_hide_qr
import kotlinx.android.synthetic.main.wallet_asset_detail_activity.*
import xc.common.kotlinext.showToast
import xc.common.utils.LibAppUtils
import xc.common.viewlib.tabslayout.TabEntity
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog


class BackupPrivateKeyActivity : BaseActivity() {

    val walletInfo: WalletDao by lazy {
        intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
    }

    override fun getLayoutId(): Int {
        return R.layout.backup_private_key_activity
    }

    override fun initView() {

        ac_title.apply {
            extInitCommonBgAutoBack(this@BackupPrivateKeyActivity, "导出私钥")
        }

        tab_layout.apply {
            var tabs = mutableListOf<TabEntity>()
            tabs.add(TabEntity("私钥"))
            tabs.add(TabEntity("二维码"))
            setTabData(ArrayList(tabs))
            setOnTabSelectListener(object : OnTabSelectListener {
                override fun onTabSelect(position: Int) {
                    ll_text.extGoneOrVisible(position == 0)
                    ll_qrcode.extGoneOrVisible(position == 1)
                }

                override fun onTabReselect(position: Int) {
                    ll_text.extGoneOrVisible(position == 0)
                    ll_qrcode.extGoneOrVisible(position == 1)
                }
            })
        }
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

        iv_qrcode.setImageBitmap(EncodingUtils.createQRCodeWithoutWhite(walletInfo.privateKey, 400, 400, null))

        tv_private.setText(walletInfo.privateKey)

        btn_copy_pri.setOnClickListener {
            val toString = tv_private.text.toString()
            ExportPrivateKeyHintDialog(this).apply {
                type="私钥"
                onConfrim=object : FullScreenDialog.OnConfirmListener{
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