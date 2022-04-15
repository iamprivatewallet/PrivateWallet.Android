package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.SeekBar
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.dao.data.GasInfoBean
import com.fanrong.frwallet.tools.*
import com.fanrong.frwallet.view.CommonButton
import kotlinx.android.synthetic.main.activity_create_wallet_step1.*
import kotlinx.android.synthetic.main.dialog_setgas.*
import kotlinx.android.synthetic.main.dialog_setgas.cb_save
import kotlinx.android.synthetic.main.dialog_setgas.ll_zdy
import kotlinx.android.synthetic.main.dialog_setgas.seekbar
import kotlinx.android.synthetic.main.dialog_setgas.set_gas
import kotlinx.android.synthetic.main.dialog_setgas.set_gaslimit
import kotlinx.android.synthetic.main.dialog_setgas.tv_gas
import kotlinx.android.synthetic.main.dialog_setgas.tv_gas_cny
import kotlinx.android.synthetic.main.dialog_setgas.tv_speed1
import kotlinx.android.synthetic.main.dialog_setgas.tv_speed2
import kotlinx.android.synthetic.main.dialog_setgas.tv_speed3
import kotlinx.android.synthetic.main.dialog_setgas.tv_speed4
import kotlinx.android.synthetic.main.dialog_setgas.tv_zdy
import kotlinx.android.synthetic.main.set_gas_activity.*
import kotlinx.android.synthetic.main.transfer_activity.*
import xc.common.tool.utils.checkNotEmpty
import xc.common.viewlib.extension.extShowOrDismissDialog
import xc.common.viewlib.view.customview.FullScreenDialog
import java.math.BigDecimal

class SetGasDialog(context: Context) : FullScreenDialog(context) {
    override var contentGravity: Int? = Gravity.BOTTOM
    var gasTypeIsZdy = false
    lateinit var gasInfo: GasInfoBean
    var gasPrice_half:Float = 0f

    override fun getContentView(): Int {
        return R.layout.dialog_setgas
    }

    override fun initView() {

        iv_close.setOnClickListener {
            dismiss()
        }

        tv_zdy.setOnClickListener{
            if (!gasTypeIsZdy){
                gasTypeIsZdy = true
                ll_zdy.visibility = View.VISIBLE
            }
            else{
                gasTypeIsZdy = false
                ll_zdy.visibility = View.GONE
            }
            updateBtnState()
        }


        tv_speed1.setOnClickListener{
            seekbar.setSeekBarProgress(0)
            setSeekBarProgressType(0.0)
            gasTypeIsZdy = false
            ll_zdy.visibility = View.GONE
            updateBtnState()
        }
        tv_speed2.setOnClickListener{
            seekbar.setSeekBarProgress(25)
            setSeekBarProgressType(1.0)
            gasTypeIsZdy = false
            ll_zdy.visibility = View.GONE
            updateBtnState()
        }
        tv_speed3.setOnClickListener{
            seekbar.setSeekBarProgress(50)
            setSeekBarProgressType(2.0)
            gasTypeIsZdy = false
            ll_zdy.visibility = View.GONE
            updateBtnState()
        }
        tv_speed4.setOnClickListener{
            seekbar.setSeekBarProgress(75)
            setSeekBarProgressType(3.0)
            gasTypeIsZdy = false
            ll_zdy.visibility = View.GONE
            updateBtnState()
        }
        seekbar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                gasTypeIsZdy = false
                ll_zdy.visibility = View.GONE
                updateBtnState()
                val d = BigDecimal(progress.toString()).divide(BigDecimal("25")).toDouble()
                setSeekBarProgressType(d)

                val calc_gasPrice = (gasPrice_half + ((progress / 25.0) * gasPrice_half)).toString()
                gasInfo!!.gasPrice = calc_gasPrice

                var gasAMount = ETHChainUtil.compateGas1(gasInfo!!.gasLimit, gasInfo!!.gasPrice.extGwei2Wei())
                tv_gas.setText(gasAMount+ CoinNameCheck.getMainCoinName())
                tv_gas_cny.setText("≈$"+gasAMount.extToFiatMoney())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        seekbar.setPrice(gasPrice_half.toString(),gasInfo!!.gasLimit)
        seekbar.invalidate()

        cb_save.setClickListener(object : CommonButton.ClickListener {
            override fun clickListener() {
                var gasInfoBean: GasInfoBean
                if (gasTypeIsZdy) {
                    gasInfoBean = GasInfoBean(set_gaslimit.et_content.text.toString(), set_gas.et_content.text.toString())
                } else {
                    gasInfoBean = GasInfoBean(gasInfo.gasLimit,gasInfo.gasPrice)
                }
//                gasInfo.currentType = itemAdapter.current
//                putExtra(FrConstants.GAS_INFO, gasInfo)

                onConfrim?.confirm(gasInfoBean)
            }
        })
    }

    private fun updateBtnState() {
        if (gasTypeIsZdy){
            if (set_gas.et_content.text.toString().checkNotEmpty() && set_gaslimit.et_content.text.toString().checkNotEmpty()){
                cb_save.setEnableState(true)
            }else{
                cb_save.setEnableState(false)
            }
        }else{
            cb_save.setEnableState(true)
        }
    }

    private fun setSeekBarProgressType(cur_addsuType:Double){
        if (cur_addsuType < 1){
            //缓慢
            tv_speed1.setBackgroundResource(R.drawable.bg_select)
            tv_speed2.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed3.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed4.setBackgroundResource(R.drawable.bg_common_input_bg)
        }else if (cur_addsuType < 2){
            //正常
            tv_speed1.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed2.setBackgroundResource(R.drawable.bg_select)
            tv_speed3.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed4.setBackgroundResource(R.drawable.bg_common_input_bg)
        }else if (cur_addsuType < 3){
            //快速
            tv_speed1.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed2.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed3.setBackgroundResource(R.drawable.bg_select)
            tv_speed4.setBackgroundResource(R.drawable.bg_common_input_bg)
        }else{
            //很快
            tv_speed1.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed2.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed3.setBackgroundResource(R.drawable.bg_common_input_bg)
            tv_speed4.setBackgroundResource(R.drawable.bg_select)
        }
    }
}