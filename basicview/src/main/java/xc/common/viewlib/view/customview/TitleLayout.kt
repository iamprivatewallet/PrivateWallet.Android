package xc.common.viewlib.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import xc.common.viewlib.R

class TitleLayout @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    fun setTitleText(titleText: String?) {
        val textView =
            findViewById<TextView>(R.id.tv_title)
        textView.text = titleText
    }

    fun setTitleTextColor(@ColorInt color: Int) {
        val textView =
            findViewById<TextView>(R.id.tv_title)
        textView.setTextColor(color)
    }

    val titleTextView: TextView
        get() = findViewById(R.id.tv_title)

    fun setOnBackClickListener(onBackClickListener: (v: View) -> Unit) {
        findViewById<View>(R.id.iv_back).setOnClickListener(
            onBackClickListener
        )
    }

    fun setBackIcon(@DrawableRes resId: Int) {
        val iv_back =
            findViewById<ImageView>(R.id.iv_back)
        iv_back.setImageResource(resId)
    }

    fun setBackBtnHide() {
        val imageView =
            findViewById<ImageView>(R.id.iv_back)
        imageView.visibility = View.GONE
    }

    fun setRightIconHide() {
        val imageView =
            findViewById<ImageView>(R.id.btn_right)
        imageView.visibility = View.GONE
    }

    fun setRightBtnIcon(iconResource: Int) {
        val imageView =
            findViewById<ImageView>(R.id.btn_right)
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(iconResource)
    }


    fun setRightBtnIconAndClick(iconResource: Int, onClickListener: (v: View) -> Unit) {
        val imageView =
            findViewById<ImageView>(R.id.btn_right)
        imageView.visibility = View.VISIBLE
        imageView.setImageResource(iconResource)
        imageView.setOnClickListener(onClickListener)
    }


    fun setRightTextColor(color: Int) {
        val tv_right_text =
            findViewById<TextView>(R.id.tv_right_text)
        tv_right_text.setTextColor(color)
    }

    var rightText: String?
        get() {
            val tv_right_text =
                findViewById<TextView>(R.id.tv_right_text)
            return tv_right_text.text.toString()
        }
        set(text) {
            val tv_right_text =
                findViewById<TextView>(R.id.tv_right_text)
            tv_right_text.text = text
            tv_right_text.visibility = View.VISIBLE
        }

    val rightTextView: TextView
        get() = findViewById(R.id.tv_right_text)

    fun setRightTextClickListener(onClickListener: OnClickListener?) {
        findViewById<View>(R.id.tv_right_text).setOnClickListener(
            onClickListener
        )
    }

    fun setLeftText(text: String?) {
        val tv_left_text =
            findViewById<TextView>(R.id.tv_left_text)
        tv_left_text.text = text
        tv_left_text.visibility = View.VISIBLE
        setBackBtnHide()
    }

    val leftText: TextView
        get() = findViewById(R.id.tv_left_text)

    fun setLeftTextColor(color: Int) {
        val tv_left_text =
            findViewById<TextView>(R.id.tv_left_text)
        tv_left_text.setTextColor(color)
        tv_left_text.visibility = View.VISIBLE
        setBackBtnHide()
    }

    fun setLeftTextClickListener(onClickListener: (v: View) -> Unit) {
        findViewById<View>(R.id.tv_left_text).setOnClickListener(
            onClickListener
        )
    }

    @JvmName("setRightText1")
    fun setRightText(text: String?) {
        val tv_right_text = findViewById<TextView>(R.id.tv_right_text)
        tv_right_text.text = text
        tv_right_text.visibility = View.VISIBLE
        setRightIconHide()
    }

    //    public void setRightText(String text) {
//        TextView tv_right_text = findViewById(R.id.tv_right_text);
//        tv_right_text.setText(text);
//        tv_right_text.setVisibility(View.VISIBLE);
//        setRightIconHide();
//    }
//
    fun setRightTextClickListener(text: String, onClickListener: (v: View) -> Unit) {
        val tv_right_text = findViewById<TextView>(R.id.tv_right_text)
        tv_right_text.setText(text)
        tv_right_text.visibility = View.VISIBLE
        tv_right_text.setOnClickListener(onClickListener)
    }

    fun setTitleBg(@ColorInt color: Int) {
        findViewById<View>(R.id.ll_title_wraper).setBackgroundColor(
            color
        )
    }

    init {
        LayoutInflater.from(context)
            .inflate(R.layout.bv_title_layout, this)
    }
}