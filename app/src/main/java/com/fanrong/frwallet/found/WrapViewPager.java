package com.fanrong.frwallet.found;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class WrapViewPager extends ViewPager {

    public WrapViewPager(@NonNull Context context) {
        super(context);
    }

    public WrapViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams params = child.getLayoutParams();
            child.measure(widthMeasureSpec,getChildMeasureSpec(heightMeasureSpec,0,params.height));
            int h = child.getMeasuredHeight();
            if (h>height)height=h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height,MeasureSpec.EXACTLY);


        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
