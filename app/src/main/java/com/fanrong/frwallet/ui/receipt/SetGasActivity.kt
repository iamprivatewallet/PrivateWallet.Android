package com.fanrong.frwallet.ui.receipt

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.dao.database.WalletDao
import com.fanrong.frwallet.dapp.DappBrowserActivity
import com.fanrong.frwallet.found.MvvmBaseActivity
import com.fanrong.frwallet.found.extInitCommonBgAutoBack
import com.fanrong.frwallet.tools.CoinNameCheck
import com.fanrong.frwallet.tools.ETHChainUtil
import com.fanrong.frwallet.tools.extGwei2Wei
import com.fanrong.frwallet.tools.extHexWei2Gwei
import com.fanrong.frwallet.ui.receipt.viewmdel.TransferViewmodel
import com.xianchao.divider.divider.ListDivider
import kotlinx.android.synthetic.main.set_gas_activity.*
import kotlinx.android.synthetic.main.splash_activity.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.kotlinext.extStartActivity
import xc.common.kotlinext.showToast
import xc.common.tool.listener.TextWatcherAfter
import xc.common.tool.utils.DensityUtil
import xc.common.tool.utils.checkIsEmpty
import xc.common.tool.utils.checkIsOnlyNumber
import xc.common.tool.utils.checkIsPosNumber
import xc.common.viewlib.utils.extGoneOrVisible
import xc.common.viewlib.utils.extInvisibleOrVisible


class SetGasActivity : MvvmBaseActivity<TransferViewmodel.State, TransferViewmodel>() {

    var minLimit = "21000"
//    var maxLimit = "210000"

    val walletInfo: WalletDao by lazy {
        intent.getSerializableExtra(FrConstants.WALLET_INFO) as WalletDao
    }
    val gasInfoBean: GasInfoBean by lazy {
        intent.getSerializableExtra(FrConstants.GAS_INFO) as GasInfoBean
    }

    val itemAdapter: SetGasAdapter by lazy {
        SetGasAdapter().apply {
            setOnItemClickListener { adapter, view, position ->
                current = position
                ll_self.extGoneOrVisible(false)
                notifyDataSetChanged()
                updataGas(false)
            }

            val mutableListOf = mutableListOf<SetGasAdapter.Item>()
            mutableListOf.add(SetGasAdapter.Item("一般", "<5分钟", "0","0"))
            recyclerview.layoutParams.height = DensityUtil.dp2px(55) * mutableListOf.size
            recyclerview.layoutParams = recyclerview.layoutParams
            current = 0
            setNewData(mutableListOf)
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.set_gas_activity
    }

    override fun initView() {
        ac_title.apply {
            extInitCommonBgAutoBack(this@SetGasActivity, "矿工费设置")
        }
        minLimit = gasInfoBean.gasLimit
        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@SetGasActivity)
            adapter = itemAdapter
            addItemDecoration(
                ListDivider.Builder()
                    .setDividerColor(Color.parseColor("#EEEEEE"))
                    .setLeftMargin(DensityUtil.dp2px(40)).build()
            )
        }

        ll_self_btn.setOnClickListener {
            if (gasInfoBean != null) {
                if (gasInfoBean.currentType == -1) {
                    et_gas_limit.setText(gasInfoBean.gasLimit)
                    et_gas_price.setText(gasInfoBean.gasPrice)
                }
            }
            updataGas(true)
        }

        btn_confirm.setOnClickListener {
            if (itemAdapter.current == -1) {
                if (et_gas_price.text.toString().checkIsEmpty()) {
                    showToast("请输入 gas price")
                    return@setOnClickListener
                }

                if (et_gas_limit.text.toString().checkIsEmpty()) {
                    showToast("请输入gas 数量")
                    return@setOnClickListener
                }
                if (et_gas_limit.text.toString().toInt() < minLimit.toInt()) {
                    showToast("Gas 过低 请输入有效的 Gas")
                    return@setOnClickListener
                }
//                if (et_gas_limit.text.toString().toInt() >= maxLimit.toInt()) {
//                    showToast("Gas 过高 请输入有效的 Gas")
//                    return@setOnClickListener
//                }
            }
            setResult(RESULT_OK, Intent().apply {
                var gasInfo: GasInfoBean
                if (itemAdapter.current == -1) {
                    gasInfo = GasInfoBean(et_gas_limit.text.toString(), et_gas_price.text.toString())
                } else {
                    gasInfo = GasInfoBean(gasInfoBean.gasLimit, itemAdapter.getItem(itemAdapter.current)!!.gasPrice!!)
                }
                gasInfo.currentType = itemAdapter.current
                putExtra(FrConstants.GAS_INFO, gasInfo)
            })

            extFinishWithAnim()
        }

        et_gas_price.addTextChangedListener(object : TextWatcherAfter() {
            override fun tryAfterTextChanged(s: Editable?) {
                super.tryAfterTextChanged(s)
                tv_gas_price_error.extGoneOrVisible(!s.toString().checkIsPosNumber())
                tv_gas_price_error.setText("请输入有效的 Gas Price")
                updataGas(true)

            }
        })

        et_gas_limit.addTextChangedListener(object : TextWatcherAfter() {
            override fun tryAfterTextChanged(s: Editable?) {
                super.tryAfterTextChanged(s)
                if (!s.toString().checkIsOnlyNumber()) {
                    tv_gas_limit_error.setText("请输入有效的 Gas")
                    tv_gas_limit_error.extGoneOrVisible(true)
                } else if (s.toString().toInt() < minLimit.toInt()) {
                    tv_gas_limit_error.setText("Gas 过低 请输入有效的 Gas")
                    tv_gas_limit_error.extGoneOrVisible(true)
                }
//                else if (s.toString().toInt() >= maxLimit.toInt()) {
//                    tv_gas_limit_error.setText("Gas 过高 请输入有效的 Gas")
//                    tv_gas_limit_error.extGoneOrVisible(true)
//                }
                else {
                    tv_gas_limit_error.extGoneOrVisible(false)
                }

                updataGas(true)
            }
        })
        updataGas(false)
        tv_gas_des.setOnClickListener {
            extStartActivity(DappBrowserActivity::class.java, Bundle().apply {
                putString(DappBrowserActivity.PARAMS_URL, FrConstants.GAS_EXPLAIN)
            })
        }
    }

    fun updataGas(isSelf: Boolean) {
        ll_self.extGoneOrVisible(isSelf)
        iv_self_select.extInvisibleOrVisible(isSelf)
        if (isSelf) {
            itemAdapter.current = -1
            try {

                tv_gas.setText(
                    ETHChainUtil.compateGas1(
                        et_gas_limit.text.toString(), et_gas_price.text.toString().extGwei2Wei()
                    )+CoinNameCheck.getNameByName(walletInfo.chainType)
                )

                tv_gas_des.setText(
                    getString(R.string.gas_info)
                        .format(et_gas_price.text.toString(), et_gas_limit.text.toString())
                )
            } catch (e: Exception) {
                tv_gas.setText("0" +CoinNameCheck.getNameByName(walletInfo.chainType))
            }


        } else {
            val item = itemAdapter.getItem(itemAdapter.current)!!
            tv_gas.setText(ETHChainUtil.compateGas1(item.gasLimit!!, item.gasPrice.extGwei2Wei()) +CoinNameCheck.getNameByName(walletInfo.chainType))
            tv_gas_des.setText(getString(R.string.gas_info).format(item.gasPrice, item.gasLimit))
        }
        itemAdapter.notifyDataSetChanged()

    }

    override fun loadData() {
        viewmodel.getGasPrice()
    }

    override fun getViewModel(): TransferViewmodel {
        return TransferViewmodel.getViewmodel(walletInfo)
    }

    override fun stateChange(state: TransferViewmodel.State) {
        state.gasInfoResult?.run {
            val toGwei = resultData!!.gasPrice.extHexWei2Gwei()
            val mutableListOf = mutableListOf<SetGasAdapter.Item>()
            mutableListOf.add(SetGasAdapter.Item("最快", "<0.5分钟", (toGwei.toDouble() * 3).toString(),gasInfoBean.gasLimit))
            mutableListOf.add(SetGasAdapter.Item("快速", "<2分钟", (toGwei.toDouble() * 2).toString(),gasInfoBean.gasLimit))
            mutableListOf.add(SetGasAdapter.Item("一般", "<5分钟", toGwei,gasInfoBean.gasLimit))
            mutableListOf.add(SetGasAdapter.Item("较慢", "<30分钟", (toGwei.toDouble() * 0.5).toString(),gasInfoBean.gasLimit))
            recyclerview.layoutParams.height = DensityUtil.dp2px(55) * mutableListOf.size
            recyclerview.layoutParams = recyclerview.layoutParams
            itemAdapter.current = 2
            itemAdapter.setNewData(mutableListOf)
        }
        if (gasInfoBean != null) {
            tv_gas.setText(ETHChainUtil.compateGas1(gasInfoBean.gasLimit, gasInfoBean.gasPrice.extGwei2Wei()) +CoinNameCheck.getNameByName(walletInfo.chainType))
//            tv_gas_des.setText(getString(R.string.gas_info).format(gasInfoBean.gasPrice, gasInfoBean.gasLimit))
            tv_gas_des.setText(getString(R.string.gas_info).format(itemAdapter.getItem(itemAdapter.current)!!.gasPrice, gasInfoBean.gasLimit))
            if (gasInfoBean.currentType == -2) {
                return
            }
            itemAdapter.current = gasInfoBean.currentType
            if (gasInfoBean.currentType == -1) {
                updataGas(true)
                et_gas_limit.setText(gasInfoBean.gasLimit)
                et_gas_price.setText(gasInfoBean.gasPrice)
            } else {
                updataGas(false)
            }
        }
    }
}