package com.fanrong.frwallet.view

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.fanrong.frwallet.R
import xc.common.tool.CommonTool


fun Any?.showTopToast(context: Context,msg: String,isSuccess:Boolean) {
    val toast = Toast(context)
    toast.duration = Toast.LENGTH_LONG
    toast.setGravity(Gravity.TOP,0,200)

    val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    val view: View = inflater.inflate(R.layout.toast_item, null)

    val iv_state = view.findViewById<ImageView>(R.id.iv_state)
    val tv_msg = view.findViewById<TextView>(R.id.tv_msg)

    if (isSuccess){
        iv_state.setImageResource(R.mipmap.toast_success)
        tv_msg.setTextColor(Color.parseColor("#FF1AC190"))
        tv_msg.setText(msg)
    }else{
        iv_state.setImageResource(R.mipmap.toast_error)
        tv_msg.setTextColor(Color.parseColor("#FFDD4E41"))
        tv_msg.setText(msg)
    }
    toast.view = view
    toast.show()
}