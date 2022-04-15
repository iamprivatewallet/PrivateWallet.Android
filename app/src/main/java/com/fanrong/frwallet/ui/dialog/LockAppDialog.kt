package com.fanrong.frwallet.ui.dialog

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.transition.Transition
import android.view.KeyEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.SimpleTarget
import com.fanrong.frwallet.R
import com.fanrong.frwallet.dao.FrConstants
import com.fanrong.frwallet.tools.BlurTransformation
import com.fanrong.frwallet.view.VerificationCodeView
import com.fanrong.frwallet.view.showTopToast
import kotlinx.android.synthetic.main.activity_lockapp.*
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

//        Glide.with(context).load("").apply(RequestOptions.bitmapTransform(BlurTransformation(context, 14, 3)))
//            .into(iv_boby)

//        Glide.with(context)
//            .load("")
//            .apply(bitmapTransform(BlurTransformation(context, 25, 25)))
//            .into(iv_boby)

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}