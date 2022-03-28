package com.fanrong.frwallet.ui.usesetting

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.UserSettingListAdapter
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import kotlinx.android.synthetic.main.activity_language_and_coin_type_select.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.utils.SPUtils

class LanguageAndCoinTypeSelectActivity : BaseActivity() {
    var showType :String ?=null
    var selectTyoe :String ?=null
    val userSettingListAdapter: UserSettingListAdapter by lazy {
        UserSettingListAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                selectTyoe=userSettingListAdapter.data[position]
                setmPosition(position)
                notifyDataSetChanged()
            }
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.activity_language_and_coin_type_select
    }
    override fun initView() {
        showType= intent.extras!!.getString(FrConstants.LUA_COINTYPE_SETTING)
        lac_title.apply {
            extInitCommonBgAutoBack(this@LanguageAndCoinTypeSelectActivity, showType!!)
            setRightText("保存")
            setRightTextClickListener(object : View.OnClickListener {
                override fun onClick(v: View?) {
                    if (showType=="多语言"){
                        setResult(RESULT_OK, Intent().apply {
                            putExtra(FrConstants.LUA_SETTING, selectTyoe)
                        })
                        SPUtils.saveValue(FrConstants.LUA_SETTING,selectTyoe!!)
                    }else{
                        setResult(RESULT_OK, Intent().apply {
                            putExtra(FrConstants.UNIT_SETTING, selectTyoe)
                        })
                        SPUtils.saveValue(FrConstants.UNIT_SETTING,selectTyoe!!)
                    }
                    extFinishWithAnim()
                }
            })
        }
        rcv_cl.apply {
            layoutManager=LinearLayoutManager(this@LanguageAndCoinTypeSelectActivity)
            adapter=userSettingListAdapter
        }
        if (showType=="多语言"){
            userSettingListAdapter.setNewData(FrConstants.language)
            selectTyoe=SPUtils.getString(FrConstants.LUA_SETTING)
            userSettingListAdapter.setmPosition(FrConstants.language.indexOf(SPUtils.getString(FrConstants.LUA_SETTING)))
        }else{
            selectTyoe=SPUtils.getString(FrConstants.UNIT_SETTING)
            userSettingListAdapter.setNewData(FrConstants.monetary_unit)
            userSettingListAdapter.setmPosition(FrConstants.monetary_unit.indexOf(SPUtils.getString(FrConstants.UNIT_SETTING)))
        }

        userSettingListAdapter.notifyDataSetChanged()
    }

    override fun loadData() {

    }

}