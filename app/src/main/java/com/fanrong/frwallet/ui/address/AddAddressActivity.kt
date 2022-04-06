package com.fanrong.frwallet.ui.address

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import com.basiclib.base.BaseActivity
import com.bumptech.glide.Glide
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.AddressDao
import com.fanrong.frwallet.dao.database.AddressModelOperator
import com.fanrong.frwallet.dao.eventbus.AddressBookListChangeEvent
import com.fanrong.frwallet.found.GoodSnackbar
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.found.extStartActivityForResult
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.ui.dialog.GeneralHintDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.yzq.zxinglibrary.android.CaptureActivity
import com.yzq.zxinglibrary.common.Constant
import kotlinx.android.synthetic.main.add_address_activity.*
import kotlinx.android.synthetic.main.add_address_activity.ac_title
import kotlinx.android.synthetic.main.password_hint_activity.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.showToast
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.checkNotEmpty
import xc.common.utils.LibPremissionUtils
import xc.common.utils.PermissonSuccess
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.view.customview.FullScreenDialog


class AddAddressActivity : BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.add_address_activity
    }

    override fun initView() {

        var addrInfo = intent.getSerializableExtra(FrConstants.ADDR_INFO) as? AddressDao
        var result = intent?.getStringExtra(Constant.CODED_CONTENT) ?: ""
        Glide.with(iv_current_chainicon).load(CoinNameCheck.getCoinImgUrl2("ETH")).into(iv_current_chainicon)
        var chaintype = intent.getStringExtra(FrConstants.CHAIN_TYPE)
        if (chaintype.checkNotEmpty()) {
            tv_addr_type.setText(chaintype)

//            iv_current_chainicon
            Glide.with(iv_current_chainicon).load(CoinNameCheck.getCoinImgUrl2(chaintype)).into(iv_current_chainicon)
        }

        if (result.checkNotEmpty()) {
            //扫码跳过来的
            set_address.et_content.setText(result)
        }
        ac_title.apply {
            extInitCommonBgAutoBack(this@AddAddressActivity, "新建地址")

            if (addrInfo != null) {

                setLeftText("取消")
                setBackBtnHide()
                leftText.setTextColor(resources.getColor(R.color.main_blue))
                leftText.setOnClickListener {
                    extFinishWithAnim()
                }
            }

            setRightTextClickListener("完成") {
                val addressModel = AddressDao()
                addressModel.address = set_address.et_content.text.toString()
                addressModel.name = set_name.et_content.text.toString()
                addressModel.remark = set_des.et_content.text.toString()
                addressModel.type = tv_addr_type.text.toString()
                if (!(addressModel.address.checkNotEmpty() && addressModel.name.checkNotEmpty() && addressModel.type.checkNotEmpty())) {
                    GoodSnackbar.showMsg(this@AddAddressActivity, "地址信息不能为空")
                    return@setRightTextClickListener
                }
                if (addressModel.address!!.extCheckNotAddr(addressModel.type)) {
                    GoodSnackbar.showMsg(this@AddAddressActivity, "无效的 ${addressModel.type} 钱包地址")
                    return@setRightTextClickListener
                }
                if (addrInfo == null) {

                    if (AddressModelOperator.saveAddr(addressModel)) {
                        EventBus.getDefault().post(AddressBookListChangeEvent())
                        showToast("保存成功")
                        extFinishWithAnim()
                    } else {
                        showToast("地址已存在")
                    }
                } else {
                    addrInfo?.address = set_address.et_content.text.toString()
                    addrInfo?.name = set_name.et_content.text.toString()
                    addrInfo?.remark = set_des.et_content.text.toString()
                    addrInfo?.type = tv_addr_type.text.toString()
                    if (AddressModelOperator.upadate(addrInfo!!)) {
                        EventBus.getDefault().post(AddressBookListChangeEvent())
                        showToast("保存成功")
                        extFinishWithAnim()
                    } else {
                        showToast("地址已存在")
                    }
                }
            }
        }

        ll_addr_type.setOnClickListener {
            extStartActivityForResult(AddressTypeActivity::class.java, Bundle().apply {
                putString(FrConstants.WALLET_TYPE, tv_addr_type.text.toString())
            }, 101) { resultCode: Int, data: Intent? ->
                if (resultCode == RESULT_OK) {
                    tv_addr_type.setText(data!!.getStringExtra(FrConstants.WALLET_TYPE))
                    addrInfo = AddressDao()
                    addrInfo?.type = data!!.getStringExtra(FrConstants.WALLET_TYPE)
                    Glide.with(iv_current_chainicon).load(CoinNameCheck.getCoinImgUrl2(data!!.getStringExtra(FrConstants.WALLET_TYPE))).into(iv_current_chainicon)
                    set_address.et_content.setText("")
                }
            }
        }

        set_address.et_content.addTextChangedListener(object : TextWatcherAfter() {
            override fun tryAfterTextChanged(s: Editable?) {
                super.tryAfterTextChanged(s)
                updateMenu()
            }
        })

//        et_name.addTextChangedListener()
        set_name.et_content.addTextChangedListener(
            object : TextWatcherAfter() {
                override fun tryAfterTextChanged(s: Editable?) {
                    super.tryAfterTextChanged(s)
                    updateMenu()
                }
            }
        )

        set_address.iv_bottomRight.setOnClickListener {
            LibPremissionUtils.needCamera(this@AddAddressActivity, object : PermissonSuccess {
                override fun hasSuccess() {
                    extStartActivityForResult(CaptureActivity::class.java, Bundle().apply {
                        putString(Constant.JUMP_TYPE, "AddAddress")
                    }, 101) { resultCode: Int, data: Intent? ->

                        if (resultCode == Activity.RESULT_OK) {
                            var result = data?.getStringExtra(Constant.CODED_CONTENT) ?: ""
                            if (addrInfo == null){
                                if (result.extCheckIsAddr("ETH")) {
                                    set_address.et_content.setText(result.extGetRightAddress("ETH"))
                                }
                            }else{
                                if (result.extCheckIsAddr(addrInfo!!.type!!)) {
                                    set_address.et_content.setText(result.extGetRightAddress(addrInfo!!.type!!))
                                }
                            }

                        }
                    }
                }
            })
        }


//        tv_delete.extGoneOrVisible(addrInfo != null)
        if (addrInfo != null) {
            set_name.et_content.setText(addrInfo!!.name)
            set_address.et_content.setText(addrInfo?.address)
            set_des.et_content.setText(addrInfo?.remark)
            tv_addr_type.setText(addrInfo?.type)
            Glide.with(iv_current_chainicon).load(CoinNameCheck.getCoinImgUrl2(addrInfo?.type!!)).into(iv_current_chainicon)
        }
//        tv_delete.setOnClickListener {
//            GeneralHintDialog(this).apply {
//                content = "确定删除地址？"
//                onConfrim = object : FullScreenDialog.OnConfirmListener {
//                    override fun confirm(params: Any?) {
//                        AddressModelOperator.delete(addrInfo!!)
//                        extFinishWithAnim()
//                    }
//                }
//            }.show()
//        }
    }


    private fun updateMenu() {
        if (set_address.et_content.text.toString().checkNotEmpty() &&
            set_name.et_content.text.toString().checkNotEmpty()
        ) {
            ac_title.rightTextView.setTextColor(resources.getColor(R.color.main_blue))
            ac_title.rightTextView.isClickable = true
        } else {
            ac_title.rightTextView.setTextColor(Color.parseColor("#888888"))
            ac_title.rightTextView.isClickable = false
        }
    }

    override fun loadData() {
    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}