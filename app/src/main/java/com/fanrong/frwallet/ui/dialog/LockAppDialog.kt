package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.graphics.Bitmap
import android.view.KeyEvent
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.view.VerificationCodeView
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_lockapp.*
import xc.common.kotlinext.extFinishWithAnim
import xc.common.tool.utils.SPUtils
import xc.common.viewlib.view.customview.FullScreenDialog

class LockAppDialog(context: Context) : FullScreenDialog(context) {
    lateinit var bitmap: Bitmap
    override fun getContentView(): Int {
        return R.layout.activity_lockapp

    }
    override fun initView() {
        setCanceledOnTouchOutside(false)

        vfcv.overnputListener = object : VerificationCodeView.IInputOverListener {
            override fun overListener(_value: MutableList<String>) {
                var password = ""
                for (item in _value){
                    password+=item
                }
                val string = SPUtils.getString(FrConstants.APPLOCKPASSWORD, "")
                if (string.equals(password)){
                    dismiss()
                    onCancel?.cancel()
                }else{
                    showTopToast(context,context.getString(R.string.mmcw),false)
                }
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}