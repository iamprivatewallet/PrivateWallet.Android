package com.fanrong.frwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fanrong.frwallet.R;

import xc.common.tool.utils.DensityUtil;

public class SingleEditText extends LinearLayout {
    private Context mContext;
    private View mView;
    private boolean edittextisdisable = false;


    public TextView tv_toplefttext;
    public EditText et_content;

    public SingleEditText(Context context) {
        super(context);
    }

    public SingleEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SingleEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SingleEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }
    private void init(Context context,AttributeSet attrs){

        mContext = context;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.item_singleedittext,this,true);

        tv_toplefttext = (TextView) mView.findViewById(R.id.tv_toplefttext);
        et_content = (EditText) mView.findViewById(R.id.et_content);

        TypedArray att = mContext.obtainStyledAttributes(attrs,R.styleable.SingleEditText);
        String topLeftTextContent = att.getString(R.styleable.SingleEditText_topLeftText);
        String edittext_hint = att.getString(R.styleable.SingleEditText_hint);

        tv_toplefttext.setText(topLeftTextContent);
        et_content.setHint(edittext_hint);

        //获得焦点的监听
        et_content.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    et_content.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_edittext_select_kuang));
//                    et_content.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
                }else{
                    et_content.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_edittext_kuang));
//                    et_content.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
                }
            }
        });

    }
}
