package com.fanrong.frwallet.ui.msgcenter

import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.CenterDataManager
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.wallet.eth.eth.QueryTransactionPageResp
import kotlinx.android.synthetic.main.activity_msg_detail.*

class MsgDetailActivity : BaseActivity() {
    var _id:String = ""
    override fun getLayoutId(): Int {
        return R.layout.activity_msg_detail
    }

    override fun initView() {
        var msgBeanModel = intent.getSerializableExtra(FrConstants.MSG_INFO) as? QueryTransactionPageResp.transactionItem
        _id = msgBeanModel?.id.toString()
        md_title.apply {
            extInitCommonBgAutoBack(this@MsgDetailActivity,"详情")
        }
        tv_msg_title.text = msgBeanModel?.title
        tv_msg_time.text = msgBeanModel?.createTime
        tv_msg_body.text = msgBeanModel?.context

    }

    override fun loadData() {
        CenterDataManager.getSystemMsgDetail(_id){
            tv_msg_name.text = it?.data?.author
        }

    }

}