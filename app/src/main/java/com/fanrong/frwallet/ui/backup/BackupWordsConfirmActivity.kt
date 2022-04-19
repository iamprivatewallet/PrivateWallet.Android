package com.fanrong.frwallet.ui.backup

import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.recyclerview.widget.GridLayoutManager
import com.basiclib.base.BaseActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.SelectWordItem
import com.fanrong.frwallet.dao.data.UnselectWordItem
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.dao.eventbus.BackUpFinish
import com.fanrong.frwallet.dao.eventbus.CurrentWalletChange
import com.fanrong.frwallet.dao.eventbus.WalletInfoChangeEvent
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.OpenLockAppDialogUtils
import com.fanrong.frwallet.tools.checkPassword
import com.fanrong.frwallet.tools.checkTwoPasswordIsSame
import com.fanrong.frwallet.ui.SuccessDialog
import com.fanrong.frwallet.ui.dialog.LockAppDialog
import com.fanrong.frwallet.view.CommonButton
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_backup_words_confirm.*
import kotlinx.android.synthetic.main.activity_backup_words_confirm.ac_title
import kotlinx.android.synthetic.main.activity_backup_words_confirm.cb_save
import kotlinx.android.synthetic.main.activity_create_wallet_step1.*
import kotlinx.android.synthetic.main.words_slect_item.view.*
import org.greenrobot.eventbus.EventBus
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.showToast
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.utils.extInvisibleOrVisible
import java.util.*


class BackupWordsConfirmActivity : BaseActivity() {
    lateinit var wordAdapter: WordAdapter
    lateinit var selectWordAdapter: SelectWordAdapter

    var wordviews = mutableMapOf<String, TextView>()

    var rightWordsOrder = mutableListOf<String>()
    var selectWords = mutableListOf<String>()

    var selectWordItemList = mutableListOf<SelectWordItem>()

    override fun getLayoutId(): Int {
        return R.layout.activity_backup_words_confirm
    }

    override fun initView() {
        val wallet = intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao

        SWLog.e(wallet.mnemonic!!)


        ac_title.apply {
            extInitCommonBgAutoBack(this@BackupWordsConfirmActivity, getString(R.string.bfqb))
        }

        val split = wallet.mnemonic!!.split(" ")
        rightWordsOrder.addAll(split)
        Collections.shuffle(split)
//        for (s in split) {
//            var view = LayoutInflater.from(this).inflate(R.layout.words_item, fl_words, false) as TextView
//            view.setText(s)
//            view.setOnClickListener {
//
//                if (!selectWords.contains(view.text.toString())) {
//                    selectWords.add(view.text.toString())
//                    val inflate = LayoutInflater.from(this).inflate(R.layout.words_slect_item, fbl_selectwords, false) as SelectWordView
//                    inflate.textView.text = (it as TextView).text
//                    inflate.setOnClickListener {
//                        fbl_selectwords.removeView(it)
//                        selectWords.remove((it as SelectWordView).textView.text.toString())
//                        updateWordsView()
//                    }
//                    fbl_selectwords.addView(inflate)
//                }
//
//                updateWordsView()
//            }
//            wordviews.put(s, view)
//            fl_words.addView(view)
//        }


        wordAdapter = WordAdapter().apply {
            setOnItemClickListener {adapter, view, position ->
                val item = wordAdapter.getItem(position)
                if (item != null && !item!!.isSelected){
                    //                wordAdapter.remove(position)
                    val unselectWordItem = UnselectWordItem().apply {
                        this.cur_index = position
                        this.content = item!!.content
                        this.isSelected = true
                    }
                    wordAdapter.setData(position,unselectWordItem)
                    notifyDataSetChanged()

                    selectWords.add(item!!.content)
                    var current_index = 0
                    selectWordItemList = mutableListOf<SelectWordItem>()
                    for (item in selectWords){
                        if (rightWordsOrder.get(current_index).equals(item)){
                            val item_bean = SelectWordItem().apply {
                                this.id = current_index
                                this.content = item
                                this.isRight = true
                            }
                            selectWordItemList.add(item_bean)
                        }else{
                            val item_bean = SelectWordItem().apply {
                                this.id = current_index
                                this.content = item
                                this.isRight = false
                            }
                            selectWordItemList.add(item_bean)
                        }
                        current_index++
                    }
                    selectWordAdapter.setNewData(selectWordItemList)

                    if (selectWordItemList.size != rightWordsOrder.size){
                        updateBtnState(false)
                    }else{
                        var isRight = true
                        for (item in selectWordItemList){
                            if (!item.isRight){
                                isRight = false
                                break
                            }
                        }
                        updateBtnState(isRight)
                    }

                }
            }
        }
        recyclerview_words.apply {
            layoutManager = GridLayoutManager(this@BackupWordsConfirmActivity, 3)
            adapter = wordAdapter
        }

        var _index = 0
        var unselectWordItemList = mutableListOf<UnselectWordItem>()
        for (item in split){
            val unselectWordItem = UnselectWordItem().apply {
                this.cur_index = _index
                this.content = item
                this.isSelected = false
            }
            unselectWordItemList.add(unselectWordItem)
        }
        wordAdapter.setNewData(unselectWordItemList)


        selectWordAdapter = SelectWordAdapter().apply {
            setOnItemClickListener {adapter, view, position ->
                val item = selectWordAdapter.getItem(position)

                selectWords.removeAt(position)
                updateBtnState(false)
                var current_index = 0
                selectWordItemList = mutableListOf<SelectWordItem>()
                for (item in selectWords){
                    if (rightWordsOrder.get(current_index).equals(item)){
                        val item_bean = SelectWordItem().apply {
                            this.id = current_index
                            this.content = item
                            this.isRight = true
                        }
                        selectWordItemList.add(item_bean)
                    }else{
                        val item_bean = SelectWordItem().apply {
                            this.id = current_index
                            this.content = item
                            this.isRight = false
                        }
                        selectWordItemList.add(item_bean)
                    }
                    current_index++
                }
                selectWordAdapter.setNewData(selectWordItemList)
                val indexOf = split.indexOf(item!!.content)
                val unselectWordItem = UnselectWordItem().apply {
                    this.cur_index = indexOf
                    this.content = item!!.content
                    this.isSelected = false
                }
                wordAdapter.setData(indexOf,unselectWordItem)
                wordAdapter.notifyDataSetChanged()
            }
        }
        recyclerview_selectwords.apply {
            layoutManager = GridLayoutManager(this@BackupWordsConfirmActivity, 3)
            adapter = selectWordAdapter
        }

        cb_save.setClickListener(object : CommonButton.ClickListener {
            override fun clickListener() {
                var isRight = true
                for (item in selectWordItemList){
                    isRight = isRight && item.isRight
                }
                if (!isRight || selectWords.size != 12) {
                    showTopToast(this@BackupWordsConfirmActivity,getString(R.string.please_input_right_zjc),false)
                    return
                }

                SuccessDialog(getString(R.string.zjc_right), this@BackupWordsConfirmActivity).apply {
                    WalletOperator.updateBackUpTrue(wallet)
                    setOnDismissListener {
                        EventBus.getDefault().post(BackUpFinish())
                        EventBus.getDefault().post(WalletInfoChangeEvent())
                        EventBus.getDefault().post(CurrentWalletChange())
                        extFinishWithAnim()
                    }
                }.show()
            }
        })

    }
    private fun updateBtnState(isRight:Boolean) {
        if (isRight) {
            cb_save.setEnableState(true)
        } else {
            cb_save.setEnableState(false)
        }
    }

//    private fun updateWordsView() {
//
//        fbl_selectwords.children.forEachIndexed { index, view ->
//            view.iv_error.extInvisibleOrVisible(rightWordsOrder.indexOf((view as SelectWordView).textView.text.toString()) != index)
//        }
//
//        for (wordview in wordviews) {
//            if (selectWords.contains(wordview.key)) {
//                wordview.value.setTextColor(Color.parseColor("#EEEEEE"))
//            } else {
//                wordview.value.setTextColor(Color.parseColor("#000000"))
//            }
//        }
//    }


    override fun loadData() {
    }


    class WordAdapter : BaseQuickAdapter<UnselectWordItem, BaseViewHolder>(R.layout.backup_item_words) {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun convert(helper: BaseViewHolder, item: UnselectWordItem) {
            val tv_word = helper.getView<TextView>(R.id.tv_word)
            if (item.isSelected){
                //选中
                tv_word.setBackgroundResource(R.drawable.bg_transparent_kuang_30_selected)
                tv_word.setTextColor(mContext.getColor(R.color.text_hint))
            }else{
                tv_word.setBackgroundResource(R.drawable.bg_transparent_kuang_30_unselect)
                tv_word.setTextColor(mContext.getColor(R.color.dialog_content))
            }
            helper.setText(R.id.tv_word, item.content)
//            helper.setText(R.id.tv_number, (helper.adapterPosition + 1).toString())
        }

    }

    class SelectWordAdapter : BaseQuickAdapter<SelectWordItem, BaseViewHolder>(R.layout.backup_item_select_words) {
        override fun convert(helper: BaseViewHolder, wordItem: SelectWordItem) {
            var item = wordItem.content
            helper.setText(R.id.tv_word, item)
            val iv_error_state = helper.getView<ImageView>(R.id.iv_error_state)
            val tv_word = helper.getView<TextView>(R.id.tv_word)
            val rl_parent_bg = helper.getView<RelativeLayout>(R.id.rl_parent_bg)
            if (wordItem.isRight){
                iv_error_state.visibility = View.GONE
                rl_parent_bg.setBackgroundResource(R.drawable.bg_transparent_kuang_30)
            }else{
                iv_error_state.visibility = View.VISIBLE
                rl_parent_bg.setBackgroundResource(R.drawable.bg_select_word_error)
            }

//            helper.setText(R.id.tv_number, (helper.adapterPosition + 1).toString())
        }

    }
    override fun onResume() {
        super.onResume()
        OpenLockAppDialogUtils.OpenDialog(this)
    }
}