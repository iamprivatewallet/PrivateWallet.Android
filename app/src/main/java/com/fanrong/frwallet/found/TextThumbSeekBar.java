package com.fanrong.frwallet.found;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.SeekBar;

import com.fanrong.frwallet.R;
import com.fanrong.frwallet.main.MyApplication;

import java.text.DecimalFormat;

@SuppressLint("AppCompatCustomView")
public class TextThumbSeekBar extends SeekBar {

    // 画笔
    private Paint mPaint;
    // 进度文字位置信息
    private Rect mProgressTextRect = new Rect();
    // 滑块按钮宽度
    private int mThumbWidth = dp2px(45);

    private int mSeekBarMin;
    public TextThumbSeekBar(Context context) {
        this(context, null);
    }


    private String gasPrice = "0";
    private String gasLimit = "0";


    public TextThumbSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.seekBarStyle);
        initData();
    }

    private void initData() {
        mPaint = new TextPaint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(MyApplication.context.getResources().getColor(R.color.text_title));
        mPaint.setTextSize(sp2px(16));

        // 如果不设置padding，当滑动到最左边或最右边时，滑块会显示不全
        setPadding(mThumbWidth / 4, 0, mThumbWidth / 4, 0);
    }

    public TextThumbSeekBar(Context context,AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);
        initData();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 进度文字位置信息
        float progress = (float)getProgress();
        float current_gwei = Float.parseFloat(gasPrice) + (( progress / 25) * Float.parseFloat(gasPrice));

        DecimalFormat decimalFormat=new DecimalFormat("0.00");//构造方法的字符格式这里如果小数不足2位,会以0补足.
        String current_Gwei_save2 = decimalFormat.format(current_gwei);//format 返回的是字符串

        String progressText = current_Gwei_save2+"Gwei" + " (<3s)";
        mPaint.getTextBounds(progressText, 0, progressText.length(), mProgressTextRect);

        // 进度百分比
        float progressRatio = (float) getProgress() / getMax();
        // thumb偏移量
        float thumbOffset = (mThumbWidth - mProgressTextRect.width()) / 2 - ((mThumbWidth/2) * progressRatio);
        float thumbX = getWidth() * progressRatio + thumbOffset;
        //这里修改文字的范围,上或下
        float thumbY = getHeight() / 2f - mProgressTextRect.height() / 2f-dp2px(8);
        canvas.drawText(progressText, thumbX, thumbY, mPaint);
    }

    // 一半的gas  正常gas   gas * 2     gas * 3
    //传过来的值为最低的 gasprice即缓慢的gasprice

    /**
     *  比如现在进度是25
     *  当前进度 / 25 就是现在的gas是缓慢gas的倍数
     *  计算滑动之后的gas  缓慢gas + (当前进度 / 25) * 缓慢gas
     * @param price
     * @param limit
     */
    public void setPrice(String price,String limit){
        gasPrice = price;
        gasLimit = limit;
    }

    public void setSeekBarProgress(int progress){
        setProgress(progress);
    }

    public void setMix(int min){
        mSeekBarMin=min;
    }

    /**
     * dp转px
     *
     * @param dp dp值
     * @return px值
     */
    public int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    /**
     * sp转px
     *
     * @param sp sp值
     * @return px值
     */
    private int sp2px(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                getResources().getDisplayMetrics());
    }
}