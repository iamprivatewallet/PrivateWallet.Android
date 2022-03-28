package com.fanrong.frwallet.ui.backup

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.core.view.children
import com.basiclib.base.BaseActivity
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.BackUpFinish
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.ui.SuccessDialog
import kotlinx.android.synthetic.main.activity_backup_words_confirm.*
import kotlinx.android.synthetic.main.words_slect_item.view.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.showToast
import xc.common.tool.utils.SWLog
import xc.common.viewlib.utils.extInvisibleOrVisible
import java.util.*


class BackupWordsConfirmActivity : BaseActivity() {


    var wordviews = mutableMapOf<String, TextView>()

    var rightWordsOrder = mutableListOf<String>()
    var selectWords = mutableListOf<String>()

    override fun getLayoutId(): Int {
        return R.layout.activity_backup_words_confirm
    }

    override fun initView() {
        val wallet = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao

        SWLog.e(wallet.mnemonic!!)


        ac_title.apply {
            extInitCommonBgAutoBack(this@BackupWordsConfirmActivity, "")
        }

        val split = wallet.mnemonic!!.split(" ")
        rightWordsOrder.addAll(split)

        Collections.shuffle(split)

        for (s in split) {
            var view = LayoutInflater.from(this).inflate(R.layout.words_item, fl_words, false) as TextView
            view.setText(s)
            view.setOnClickListener {

                if (!selectWords.contains(view.text.toString())) {
                    selectWords.add(view.text.toString())
                    val inflate = LayoutInflater.from(this).inflate(R.layout.words_slect_item, fbl_selectwords, false) as SelectWordView
                    inflate.textView.text = (it as TextView).text
                    inflate.setOnClickListener {
                        fbl_selectwords.removeView(it)
                        selectWords.remove((it as SelectWordView).textView.text.toString())
                        updateWordsView()
                    }
                    fbl_selectwords.addView(inflate)
                }

                updateWordsView()
            }
            wordviews.put(s, view)
            fl_words.addView(view)
        }


        btn_confirm.setOnClickListener {
            var isRight = true
            for (child in fbl_selectwords.children) {
                isRight = isRight && (child.iv_error.visibility != View.VISIBLE)
            }
            if (!isRight || selectWords.size != 12) {
                showToast("请正确选择助记词")
                return@setOnClickListener
            }

            SuccessDialog("助记词正确", this).apply {
                WalletOperator.updateBackUpTrue(wallet)
                setOnDismissListener {
                    EventBus.getDefault().post(BackUpFinish())
                    EventBus.getDefault().post(WalletInfoChangeEvent())
                    EventBus.getDefault().post(CurrentWalletChange())
                    extFinishWithAnim()
                }
            }.show()
        }

    }

    private fun updateWordsView() {

        fbl_selectwords.children.forEachIndexed { index, view ->
            view.iv_error.extInvisibleOrVisible(rightWordsOrder.indexOf((view as SelectWordView).textView.text.toString()) != index)
        }

        for (wordview in wordviews) {
            if (selectWords.contains(wordview.key)) {
                wordview.value.setTextColor(Color.parseColor("#EEEEEE"))
            } else {
                wordview.value.setTextColor(Color.parseColor("#000000"))
            }
        }
    }


    override fun loadData() {
    }
}