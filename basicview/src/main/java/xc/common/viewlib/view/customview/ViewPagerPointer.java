package xc.common.viewlib.view.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import xc.common.viewlib.R;


public class ViewPagerPointer extends LinearLayout {

    Context mContext;
    private ImageView[] points;
    ViewPager viewPager;

    int childMargin;

    int selectDrawable = R.drawable.basiclib_page_dot_focused;
    int unSelectDrawable = R.drawable.basiclib_page_dot_un_focused;

    public ViewPagerPointer(Context context) {
        super(context);
        mContext = context;
        childMargin = dp2px(mContext, 6f);
    }

    public ViewPagerPointer(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        childMargin = dp2px(mContext, 6f);
    }

    public ViewPagerPointer(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        childMargin = dp2px(mContext, 6f);
    }

    public void addPoints(int size) {
        removeAllViews();
        points = new ImageView[size];
        for (int i = 0; i < size; i++) {
            points[i] = new ImageView(mContext);
            LayoutParams layoutParams = new LayoutParams(new ViewGroup.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            layoutParams.leftMargin = childMargin;
            layoutParams.rightMargin = childMargin;
            if (i == 0) {
                points[i].setBackgroundResource(selectDrawable);
            } else {
                points[i].setBackgroundResource(unSelectDrawable);
            }
            addView(points[i], layoutParams);
        }
    }

    public void setChildMarginDp(int marginDp) {
        childMargin = dp2px(mContext, marginDp);
    }

    public void setSelectDrawable(int selectDrawable) {
        this.selectDrawable = selectDrawable;
    }

    public void setUnSelectDrawable(int unSelectDrawable) {
        this.unSelectDrawable = unSelectDrawable;
    }

    private void setImageBackground(int selectItems) {
        if (points != null) {
            for (int i = 0; i < points.length; i++) {
                if (i == selectItems) {
                    points[i].setBackgroundResource(selectDrawable);
                } else {
                    points[i].setBackgroundResource(unSelectDrawable);
                }
            }
        }
    }


    public void setupViewPager(final ViewPager viewPager) {
        this.viewPager = viewPager;
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                i = i % viewPager.getAdapter().getCount();
                setImageBackground(i);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dp2px(Context context, float dpValue) {
        return (int) (context.getResources().getDisplayMetrics().density + dpValue + 0.5f);
    }
}
