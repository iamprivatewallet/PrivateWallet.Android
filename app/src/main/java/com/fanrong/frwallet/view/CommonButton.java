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

import java.util.List;

public class CommonButton extends LinearLayout {
    private Context mContext;
    private View mView;
    private boolean edittextisdisable = false;


    public LinearLayout ll_boby;
    public TextView tv_content;


    public interface ClickListener{
        public void clickListener();
    }
    public ClickListener listener;

    public CommonButton(Context context) {
        super(context);
    }

    public CommonButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Init(context,attrs);
    }

    public CommonButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public CommonButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void Init(Context context,AttributeSet attrs){

        mContext = context;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.item_common_buttom,this,true);

        ll_boby = (LinearLayout) mView.findViewById(R.id.ll_boby);
        tv_content = (TextView) mView.findViewById(R.id.tv_content);

        TypedArray att = mContext.obtainStyledAttributes(attrs,R.styleable.CommonButton);
        String btnText = att.getString(R.styleable.CommonButton_btnText);
        boolean isCanUSe = att.getBoolean(R.styleable.CommonButton_isCanUse, true);
        tv_content.setText(btnText);

        ll_boby.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.clickListener();
                }
            }
        });

        setEnableState(isCanUSe);
    }

    public void setClickListener(ClickListener _listener){
        listener = _listener;
    }
    public void setEnableState(Boolean isCan){
        ll_boby.setClickable(isCan);
        if (isCan){
            ll_boby.setBackgroundResource(R.drawable.bg_btn_solid);
        }else{
            ll_boby.setBackgroundResource(R.drawable.bg_btn_unable_solid);
        }
    }


}
