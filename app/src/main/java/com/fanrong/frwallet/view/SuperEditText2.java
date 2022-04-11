package com.fanrong.frwallet.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.fanrong.frwallet.R;

import xc.common.tool.utils.DensityUtil;
import xc.common.viewlib.view.EditTextPasswordStyle;

public class SuperEditText2 extends LinearLayout {
    private Context mContext;
    private View mView;
    private boolean edittextisdisable = false;

    LinearLayout ll_parent;

    TextView tv_toplefttext_1;
    TextView tv_toprighttext_1;
    ImageView iv_topright_1;
    public EditText et_content_1;
    TextView tv_bottomright_1;
    LinearLayout ll_content_bg_1;

    TextView tv_toplefttext_2;
    TextView tv_toprighttext_2;
    ImageView iv_topright_2;
    public EditText et_content_2;
    TextView tv_bottomright_2;
    LinearLayout ll_content_bg_2;


    public interface EditTextChangeListener{
        public void changeListener(String _value);
    }

    public EditTextChangeListener changeListener_1 = null;
    public EditTextChangeListener changeListener_2 = null;

    public SuperEditText2(Context context) {
        super(context);
    }

    public SuperEditText2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public SuperEditText2(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public SuperEditText2(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    public void setEditTextChangeListener(EditTextChangeListener listener_1,EditTextChangeListener listener_2){
        changeListener_1 = listener_1;
        changeListener_2 = listener_2;
    }

    public void init(Context context,AttributeSet attrs){
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.item_supere_edittext_2,this,true);


        ll_parent = (LinearLayout) mView.findViewById(R.id.ll_parent);
        //上面一部分开始
        //左上角文字   需要传入文字右边的图片  颜色
        tv_toplefttext_1 = (TextView) mView.findViewById(R.id.tv_toplefttext_1);
        //右上角文字   颜色
        tv_toprighttext_1 = (TextView) mView.findViewById(R.id.tv_toprighttext_1);
        //右上角图片
        iv_topright_1 = (ImageView) mView.findViewById(R.id.iv_topright_1);
        //输入框   //需要传入允许输入的类型  颜色
        et_content_1 = (EditText)mView.findViewById(R.id.et_content_1);
        //右下角文字
        tv_bottomright_1 = (TextView) mView.findViewById(R.id.tv_bottomright_1);

        ll_content_bg_1 = (LinearLayout) mView.findViewById(R.id.ll_content_1);
        //上面一部分结束

        //下面一部分开始
        //左上角文字   需要传入文字右边的图片  颜色
        tv_toplefttext_2 = (TextView) mView.findViewById(R.id.tv_toplefttext_2);
        //右上角文字   颜色
        tv_toprighttext_2 = (TextView) mView.findViewById(R.id.tv_toprighttext_2);
        //右上角图片
        iv_topright_2 = (ImageView) mView.findViewById(R.id.iv_topright_2);
        //输入框   //需要传入允许输入的类型  颜色
        et_content_2 = (EditText)mView.findViewById(R.id.et_content_2);
        //右下角文字
        tv_bottomright_2 = (TextView) mView.findViewById(R.id.tv_bottomright_2);

        ll_content_bg_2 = (LinearLayout) mView.findViewById(R.id.ll_content_2);
        //下面一部分结束
        CheckBox cb_show_hide = findViewById(R.id.cb_show_hide);
        cb_show_hide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    et_content_1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_content_2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_content_1.setSelection(et_content_1.getText().length());
                    et_content_2.setSelection(et_content_2.getText().length());
                }else{
                    et_content_1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_content_2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_content_1.setSelection(et_content_1.getText().length());
                    et_content_2.setSelection(et_content_2.getText().length());
                }
            }
        });
//        cb_show_hide.setOnCheckedChangeListener { buttonView, isChecked ->
//                val trans = EditTextPasswordStyle()
//            et_password.extShowOrHide(isChecked, trans)
//            et_repassword.extShowOrHide(isChecked, trans)
//        }

        et_content_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (changeListener_1 != null){
                    changeListener_1.changeListener(s.toString());
                }
            }
        });
        et_content_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (changeListener_2 != null){
                    changeListener_2.changeListener(s.toString());
                }
            }
        });

        et_content_1.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ll_parent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_wallet_greenborder));
//                    ll_parent.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
                }else{
                    ll_parent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_wallet_item));
//                    ll_parent.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
                }
            }
        });

        et_content_2.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    ll_parent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_wallet_greenborder));
//                    ll_parent.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
                }else{
                    ll_parent.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.bg_wallet_item));
//                    ll_parent.setPadding(DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f),DensityUtil.dp2px(20f));
                }
            }
        });

        TypedArray att = mContext.obtainStyledAttributes(attrs,R.styleable.SuperEditText2);
        String topLeftTextContent_1 = att.getString(R.styleable.SuperEditText2_topLeftTextContent_1);
        String editTextHint_1 = att.getString(R.styleable.SuperEditText2_edittext_hint_1);

        String topLeftTextContent_2 = att.getString(R.styleable.SuperEditText2_topLeftTextContent_2);
        String editTextHint_2 = att.getString(R.styleable.SuperEditText2_edittext_hint_2);

        tv_toplefttext_1.setText(topLeftTextContent_1);
        tv_toplefttext_2.setText(topLeftTextContent_2);
        et_content_1.setHint(editTextHint_1);
        et_content_2.setHint(editTextHint_2);
    }
}
