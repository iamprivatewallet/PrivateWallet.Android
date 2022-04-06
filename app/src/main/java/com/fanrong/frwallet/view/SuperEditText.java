package com.fanrong.frwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fanrong.frwallet.R;

import xc.common.tool.utils.DensityUtil;

public class SuperEditText extends LinearLayout {
    private Context mContext;
    private View mView;
    private boolean edittextisdisable = false;

    public TextView tv_toplefttext;
    public TextView tv_toprighttext;
    public ImageView iv_topright;
    public EditText et_content;
    public TextView tv_bottomright;
    public LinearLayout ll_content_bg;
    public ImageView iv_bottomRight;

    public EditTextChangeListener changeListener = null;
    public ClickTopRightTextListener topRightListener = null;
    public ClickBottomRightTextListener bottomRightListener = null;
    public ClickTopRightImageListener topRightImageListener = null;

    public interface EditTextChangeListener{
        public void changeListener(String _value);
    }
    public interface ClickTopRightTextListener{
        public void onClickText();
    }
    public interface ClickBottomRightTextListener{
        public void onClickText();
    }
    public interface ClickTopRightImageListener{
        public void onClickImage();
    }

    public SuperEditText(Context context) {
        super(context,null);
    }

    public SuperEditText(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SuperEditText(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SuperEditText(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public void setTopRightText(String str){
        tv_toprighttext.setVisibility(View.VISIBLE);
        iv_topright.setVisibility(View.GONE);
        tv_toprighttext.setText(str);
    }
    public void setBottomRightText(String str){
        tv_bottomright.setText(str);
    }
    public void setChangeListener(EditTextChangeListener listener){
        changeListener = listener;
    }
    public void setTopRightListener(ClickTopRightTextListener listener){
        topRightListener = listener;
    }
    public void setTopRightImageListener(ClickTopRightImageListener listener){
        tv_toprighttext.setVisibility(View.GONE);
        iv_topright.setVisibility(View.VISIBLE);
        topRightImageListener = listener;
    }
    public void setBottomRightListener(ClickBottomRightTextListener listener){
        bottomRightListener = listener;
    }
    public void setEditTextInputType(int type){
        et_content.setInputType(type);
    }
    public void setEditTextValue(String _value){
        et_content.setText(_value!=null?_value:"");
    }
    public String getEditTextValue(){
        return et_content.getText().toString();
    }


    private void init(Context context,AttributeSet attrs){
        mContext = context;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.item_supere_edittext,this,true);

        //左上角文字   需要传入文字右边的图片  颜色
        tv_toplefttext = (TextView) mView.findViewById(R.id.tv_toplefttext);
        //右上角文字   颜色
        tv_toprighttext = (TextView) mView.findViewById(R.id.tv_toprighttext);
        //右上角图片
        iv_topright = (ImageView) mView.findViewById(R.id.iv_topright);
        //输入框   //需要传入允许输入的类型  颜色
        et_content = (EditText)mView.findViewById(R.id.et_content);
        //右下角文字
        tv_bottomright = (TextView) mView.findViewById(R.id.tv_bottomright);

        ll_content_bg = (LinearLayout) mView.findViewById(R.id.ll_content_bg);

        iv_bottomRight = (ImageView) mView.findViewById(R.id.iv_bottomRight);

        tv_toprighttext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topRightListener != null){
                    topRightListener.onClickText();
                }
            }
        });
        tv_bottomright.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomRightListener != null){
                    bottomRightListener.onClickText();
                }
            }
        });
        iv_topright.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (topRightImageListener != null){
                    topRightImageListener.onClickImage();
                }
            }
        });


        TypedArray att = mContext.obtainStyledAttributes(attrs,R.styleable.SuperEditText);
        String topLeftTextContent = att.getString(R.styleable.SuperEditText_topLeftTextContent);
        int topLeftTextDrawable = att.getResourceId(R.styleable.SuperEditText_topLeftTextDrawable,-1);
        String topRightTextContent = att.getString(R.styleable.SuperEditText_topRightTextContent);
        int topRightImage = att.getResourceId(R.styleable.SuperEditText_topRightImage,-1);
        String bottomRightTextContent = att.getString(R.styleable.SuperEditText_bottomRightTextContent);
        String editTextHint = att.getString(R.styleable.SuperEditText_edittext_hint);
//        int input_type = att.getInteger(R.styleable.SuperEditText_edittext_input_type,0);
        int bottomRightImg = att.getResourceId(R.styleable.SuperEditText_bottomRightImg,-1);

        edittextisdisable = att.getBoolean(R.styleable.SuperEditText_edittextisdisable,false);

        SpannableString ss = new SpannableString(editTextHint);
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(14,true);
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        et_content.setHint(new SpannedString(ss));
//        et_content.setInputType(input_type);


        tv_toplefttext.setText(topLeftTextContent);
        if (topLeftTextDrawable != -1){
            tv_toplefttext.setCompoundDrawablesWithIntrinsicBounds(0,0,topLeftTextDrawable,0);
            tv_toplefttext.setCompoundDrawablePadding(10);
        }

        if (bottomRightImg == -1){
            iv_bottomRight.setVisibility(GONE);
        }else{
            iv_bottomRight.setVisibility(VISIBLE);
            iv_bottomRight.setImageResource(bottomRightImg);
        }

        tv_toprighttext.setText(topRightTextContent);
        if (topRightImage != -1){
            iv_topright.setImageResource(topRightImage);
        }

        tv_bottomright.setText(bottomRightTextContent);

        if (edittextisdisable){
            //为true,说明被禁用
            et_content.setEnabled(false);
            et_content.setFocusable(false);
            et_content.setKeyListener(null);//重点
            et_content.addTextChangedListener(null);

            ll_content_bg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_wallet_disable));
            ll_content_bg.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
        }
        et_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (changeListener != null){
                    changeListener.changeListener(s.toString());
                }
            }
        });
        //获得焦点的监听
        et_content.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ll_content_bg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_wallet_greenborder));
                    ll_content_bg.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
                }else{
                    ll_content_bg.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_wallet_item));
                    ll_content_bg.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
                }
            }
        });

    }
}
