package com.fanrong.frwallet.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.fanrong.frwallet.R
import com.fanrong.frwallet.adapter.PswLoginDialogAdapter
import com.fanrong.frwallet.adapter.UserSettingListAdapter
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dao.database.WalletOperator
import com.fanrong.frwallet.inteface.OnAssetsSortDialogItemClick
import com.fanrong.frwallet.inteface.OnSelectWalletDialogItemClick
import com.tencent.bugly.proguard.aa
import com.tencent.bugly.proguard.r

/**
 * 密码登录选择钱包弹框
 */
class PswLoginSelectWalletDialog(
    var activity: AppCompatActivity,
    val onSelectWalletDialogItemClick: OnSelectWalletDialogItemClick
) : DialogFragment() {

    private val TAG = "base_bottom_dialog"
     lateinit var selectWallet: WalletDao
    private val DEFAULT_DIM = 0.5f
    val pswLoginDialogAdapter: PswLoginDialogAdapter by lazy {
        PswLoginDialogAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                onSelectWalletDialogItemClick.selectWallet(pswLoginDialogAdapter.data[position])
                setmPosition(position)
                notifyDataSetChanged()
                dismiss()
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.BottomDialog)
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog!!.window?.requestFeature(Window.FEATURE_NO_TITLE)
        dialog!!.setCanceledOnTouchOutside(getCancelOutside())
        dialog!!.window?.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // 背景变暗.
        val v = inflater.inflate(getContentView(), container, false)
        initView(v)
        return v
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        dialog!!.window?.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//清除标记
//    }
    override fun onStart() {
        super.onStart()
        val window = dialog!!.window
        val params = window?.attributes
        params?.dimAmount = getDimAmount()
        params?.width = WindowManager.LayoutParams.MATCH_PARENT
        val outMetrics = DisplayMetrics()
        activity.windowManager.defaultDisplay.getMetrics(outMetrics)
        params?.height = WindowManager.LayoutParams.WRAP_CONTENT
        params?.gravity = Gravity.BOTTOM
        window?.attributes = params
        dialog!!.window?.setWindowAnimations(R.style.BottomDialogAnimation)
    }

    fun getHeight(): Int {
        return -1
    }

    fun getDimAmount(): Float {
        return DEFAULT_DIM
    }

    fun getCancelOutside(): Boolean {
        return true
    }

    fun getFragmentTag(): String {
        return TAG
    }

    fun show() {
        show(activity.supportFragmentManager)
    }

    fun show(fragmentManager: FragmentManager) {
        show(fragmentManager, getFragmentTag())
    }

    fun getContentView() = R.layout.dialog_use_pswlogin

    private fun initView(view: View) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rcv_usepsw_dialog)
        val allWallet = WalletOperator.queryAllWallet()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = pswLoginDialogAdapter
        }
        pswLoginDialogAdapter.setNewData(allWallet)
        for (wallet in allWallet){
            if (selectWallet.chainType==wallet.chainType&&selectWallet.address==wallet.address){
                pswLoginDialogAdapter.setmPosition(allWallet.indexOf(wallet))
                pswLoginDialogAdapter.notifyDataSetChanged()
            }
        }

    }
}
