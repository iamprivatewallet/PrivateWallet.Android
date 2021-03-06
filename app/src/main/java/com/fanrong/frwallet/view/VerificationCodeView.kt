package com.fanrong.frwallet.view

import android.content.Context
import android.content.res.Resources
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.KeyEvent
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.fanrong.frwallet.R

open class VerificationCodeView @JvmOverloads constructor(
    context: Context?,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : RelativeLayout(context, attributeSet, defStyleAttr, defStyleRes) {
    interface IInputOverListener {
        fun overListener(_value: MutableList<String>)
    }
    lateinit var overnputListener:IInputOverListener
    private var view =
        LayoutInflater.from(context).inflate(R.layout.view_verification_code, this, true)
    private var tvFirst: TextView
    private var tvSecond: TextView
    private var tvThird: TextView
    private var tvFourth: TextView
    private var tvFifth: TextView
    private var tvSixth: TextView
    private var et: EditText

    val codes = arrayListOf<String>()

    var block: ((Boolean) -> Unit)? = null

    init {
        tvFirst = view.findViewById(R.id.tvVerification1)
        tvSecond = view.findViewById(R.id.tvVerification2)
        tvThird = view.findViewById(R.id.tvVerification3)
        tvFourth = view.findViewById(R.id.tvVerification4)
        tvFifth = view.findViewById(R.id.tvVerification5)
        tvSixth = view.findViewById(R.id.tvVerification6)

//        val linearParams: LinearLayout.LayoutParams =
//            tvFirst.getLayoutParams() as LinearLayout.LayoutParams //取控件textView当前的布局参数
//        linearParams.height = (linearParams.width * 1.1).toInt() // 控件的高强制设成20
//        linearParams.width = linearParams.width // 控件的宽强制设成30
//        tvFirst.setLayoutParams(linearParams) //使设置好的布局参数应用到控件
//        tvSecond.setLayoutParams(linearParams) //使设置好的布局参数应用到控件
//        tvThird.setLayoutParams(linearParams) //使设置好的布局参数应用到控件
//        tvFourth.setLayoutParams(linearParams) //使设置好的布局参数应用到控件
//        tvFifth.setLayoutParams(linearParams) //使设置好的布局参数应用到控件
//        tvSixth.setLayoutParams(linearParams) //使设置好的布局参数应用到控件


        et = view.findViewById(R.id.etVerification)

        setTextViews()

        et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!s.isNullOrEmpty()) {
                    et.setText("")
                    if (codes.size < 5) {
                        codes.add(s.toString())
                        setTextViews()
                    }else if(codes.size == 5){
                        codes.add(s.toString())
                        setTextViews()
                        if (overnputListener != null){
                            overnputListener.overListener(codes)
                        }
                    }
                }
                block?.invoke((codes.size == 6))
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

        })

        et.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && codes.size > 0) {
                codes.removeAt(codes.size - 1)
                setTextViews()
                block?.invoke((codes.size == 6))
                true
            } else {
                false
            }
        }
    }

    private fun setTextViews() {
        setDefaultViews()
        codes.size.let {
            if (it >= 1) {
                tvFirst.text = "●"
            }
            if (it >= 2) {
                tvSecond.text = "●"
            }
            if (it >= 3) {
                tvThird.text = "●"
            }
            if (it >= 4) {
                tvFourth.text = "●"
            }
            if (it >= 5) {
                tvFifth.text = "●"
            }
            if (it == 6) {
                tvSixth.text = "●"
            }

            when (it) {
                0 -> tvFirst.setBackgroundResource(R.drawable.bg_text_checked)
                1 -> tvSecond.setBackgroundResource(R.drawable.bg_text_checked)
                2 -> tvThird.setBackgroundResource(R.drawable.bg_text_checked)
                3 -> tvFourth.setBackgroundResource(R.drawable.bg_text_checked)
                4 -> tvFifth.setBackgroundResource(R.drawable.bg_text_checked)
                5 -> tvSixth.setBackgroundResource(R.drawable.bg_text_checked)
            }
        }
    }

    private fun setDefaultViews() {
        tvFirst.text = ""
        tvSecond.text = ""
        tvThird.text = ""
        tvFourth.text = ""
        tvFifth.text = ""
        tvSixth.text = ""
        tvFirst.setBackgroundResource(R.drawable.bg_text_normal)
        tvSecond.setBackgroundResource(R.drawable.bg_text_normal)
        tvThird.setBackgroundResource(R.drawable.bg_text_normal)
        tvFourth.setBackgroundResource(R.drawable.bg_text_normal)
        tvFifth.setBackgroundResource(R.drawable.bg_text_normal)
        tvSixth.setBackgroundResource(R.drawable.bg_text_normal)
    }

    fun getText(): String {
        return if (codes.size == 0) ""
        else {
            var result = ""
            for (code in codes) {
                result += code
            }
            result
        }
    }

    fun setListener(block: (Boolean) -> Unit) {
        this.block = block
    }
}