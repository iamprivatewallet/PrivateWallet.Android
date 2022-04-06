package com.fanrong.frwallet.ui.usesetting

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.Switch
import androidx.annotation.RequiresApi
import com.basiclib.base.BaseActivity
import com.codersun.fingerprintcompat.AonFingerChangeCallback
import com.codersun.fingerprintcompat.FingerManager
import com.codersun.fingerprintcompat.FingerManager.SupportResult
import com.codersun.fingerprintcompat.SimpleFingerCheckCallback
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.eventbus.ShowMoneyEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.main.MainActivity
import com.fanrong.frwallet.main.MyApplication
import com.fanrong.frwallet.tools.AppLanguageUtils
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.ui.dapp.setting.DappSettingActivity
import com.fanrong.frwallet.ui.dialog.GeneralHintDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.ui.node.ChainNodeSettingActivity
import com.fanrong.otherlib.eventbus.extRegisterAutoUnregister
import kotlinx.android.synthetic.main.import_wallet_keystore_activity.*
import kotlinx.android.synthetic.main.use_setting_activity.*
import kotlinx.android.synthetic.main.use_setting_activity.ac_title
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.utils.SPUtils
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.view.customview.FullScreenDialog


class UseSettingActivity : BaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.use_setting_activity
    }

    var isCallback: Boolean = true

    @RequiresApi(Build.VERSION_CODES.M)
    override fun initView() {
        if (SPUtils.getBoolean(FrConstants.REDUP_GREENDOWN)) {
            iv_kstate.setImageResource(R.mipmap.icon_toggle_on)
        }else{
            iv_kstate.setImageResource(R.mipmap.icon_toggle_off)
        }
//        if (SPUtils.getBoolean(FrConstants.SHOW_MONEY_SETTING)) {
//            sth_hide_money.isChecked = true
//        }
        tv_lang.text = SPUtils.getString(FrConstants.LUA_SETTING)
        tv_unit.text = SPUtils.getString(FrConstants.UNIT_SETTING)
        ac_title.apply {

            extInitCommonBgAutoBack(this@UseSettingActivity,getString(R.string.setting))
            setBackgroundColor(Color.parseColor("#FFFFFF"))
        }


        rl_lan_setting.setOnClickListener {
            extStartActivityForResult(LanguageAndCoinTypeSelectActivity::class.java, Bundle().apply {
                putString(FrConstants.LUA_COINTYPE_SETTING, "多语言")
                putString(FrConstants.ISMORELAN, "1")
                putString(FrConstants.LUA_SETTING, SPUtils.getString(FrConstants.LUA_SETTING))
            }, 101) { i: Int, intent: Intent? ->
                if (i == Activity.RESULT_OK) {
                    if (intent?.getStringExtra(FrConstants.LUA_SETTING).checkNotEmpty()) {
                        if (intent != null) {
                            changeLanguage(intent)
                        }
                    }
                }
            }
        }
        rl_unit_setting.setOnClickListener {
            extStartActivityForResult(LanguageAndCoinTypeSelectActivity::class.java, Bundle().apply {
                putString(FrConstants.LUA_COINTYPE_SETTING, "货币单位")
                putString(FrConstants.ISMORELAN, "0")
                putString(FrConstants.UNIT_SETTING, SPUtils.getString(FrConstants.UNIT_SETTING))
            }, 101) { i: Int, intent: Intent? ->
                if (i == Activity.RESULT_OK) {
                    if (intent?.getStringExtra(FrConstants.UNIT_SETTING).checkNotEmpty()) {
                        tv_unit.text = intent?.getStringExtra(FrConstants.UNIT_SETTING)
                    }
                }
            }
        }
        rl_node_setting.setOnClickListener {
            extStartActivity(ChainNodeSettingActivity::class.java)
        }
//        sth_finger.setOnCheckedChangeListener { aaa, isChecked ->
//            if (!isCallback) {
//                return@setOnCheckedChangeListener
//            }
//            if (isChecked) {
//                openFingerPrint()
//            } else {
//                GeneralHintDialog(this).apply {
//                    content = "确认关闭验证？"
//                    onConfrim = object : FullScreenDialog.OnConfirmListener {
//                        override fun confirm(params: Any?) {
//                            SPUtils.saveValue(FrConstants.FINGER_SETTING, false);
//                        }
//                    }
//                    onCancel = object : FullScreenDialog.OnCancelListener {
//                        override fun cancel() {
//                            setFingerChecked(aaa as Switch, true)
//                        }
//                    }
//                }.show()
//
//            }
//        }
//        sth_hide_money.setOnCheckedChangeListener { _, isChecked ->
//            if (isChecked) {
//                SPUtils.saveValue(FrConstants.SHOW_MONEY_SETTING, true)
//            } else {
//                SPUtils.saveValue(FrConstants.SHOW_MONEY_SETTING, false)
//            }
//            EventBus.getDefault().post(ShowMoneyEvent())
//        }

        //dapp设置
//        rl_dapp_setting.setOnClickListener {
//            extStartActivity(DappSettingActivity::class.java)
//        }
//
        iv_kstate.setOnClickListener{
            val isShow = SPUtils.getBoolean(FrConstants.REDUP_GREENDOWN)
            SPUtils.saveValue(FrConstants.REDUP_GREENDOWN,!isShow)

            if (SPUtils.getBoolean(FrConstants.REDUP_GREENDOWN)) {
                iv_kstate.setImageResource(R.mipmap.icon_toggle_on)
            }else{
                iv_kstate.setImageResource(R.mipmap.icon_toggle_off)
            }
        }
    }

    private fun changeLanguage(intent: Intent) {
        val newLanguage = FrConstants.language_code[FrConstants.language.indexOf(intent?.getStringExtra(FrConstants.LUA_SETTING))]
        tv_lang.text = intent?.getStringExtra(FrConstants.LUA_SETTING)
        SPUtils.saveValue(FrConstants.LAN_CODE, newLanguage)
        AppLanguageUtils.changeAppLanguage(this, newLanguage)
        AppLanguageUtils.changeAppLanguage(MyApplication.context, newLanguage)
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        extFinishWithAnim()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun openFingerPrint() {
//        when (FingerManager.checkSupport(this@UseSettingActivity)) {
//            SupportResult.DEVICE_UNSUPPORTED -> {
//                showToast("您的设备不支持指纹")
//                setFingerChecked(sth_finger, false)
//            }
//            SupportResult.SUPPORT_WITHOUT_DATA -> {
//                showToast("请在系统录入指纹后再验证")
//                setFingerChecked(sth_finger, false)
//            }
//            SupportResult.SUPPORT -> FingerManager.build().setApplication(application)
//                .setTitle("指纹验证")
//                .setDes("请按下指纹")
//                .setNegativeText("取消")
//                .setFingerCheckCallback(object : SimpleFingerCheckCallback() {
//                    override fun onSucceed() {
//                        showToast("指纹开通成功")
//                        SPUtils.saveValue(FrConstants.FINGER_SETTING, true);
//                    }
//
//                    override fun onError(error: String) {
//                        showToast(error)
//                        setFingerChecked(sth_finger, false)
//                    }
//
//                    override fun onCancel() {
//                        showToast("您取消了识别")
//                        setFingerChecked(sth_finger, false)
//                    }
//                })
//                .setFingerChangeCallback(object : AonFingerChangeCallback() {
//                    override fun onFingerDataChange() {
//                        showToast("指纹数据发生了变化")
//                        setFingerChecked(sth_finger, false)
//                    }
//                })
//                .create()
//                .startListener(this)
//        }
    }

    fun setFingerChecked(sw: Switch, state: Boolean) {
        isCallback = false
        sw.isChecked = state
        isCallback = true
    }

    override fun loadData() {
        EventBus.getDefault().extRegisterAutoUnregister(this)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onReceiptEvent(event: ShowMoneyEvent) {

    }

    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}