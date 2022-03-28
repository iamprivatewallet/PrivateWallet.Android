package xc.common.tool.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import xc.common.tool.CommonTool;


public class DensityUtil {
    private static float scale;

    private static Context getContext(){
        return CommonTool.INSTANCE.getContext();
    }

    /**
     * 获取屏幕高度像素
     *
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕高度像素
     *
     * @return
     */
    public static int getScreenHeight() {
        DisplayMetrics dm = CommonTool.INSTANCE.getContext().getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        if (scale == 0)
            scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        if (scale == 0)
            scale = CommonTool.INSTANCE.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(int dpValue) {
        return dp2px(dpValue + 0.0f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        if (scale == 0)
            scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(float pxValue) {
        return px2dp(getContext(),pxValue);
    }


    public static int getScreenWidth(Context context) {
        WindowManager manager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = manager.getDefaultDisplay();
        return display.getWidth();
    }


    public static float sp2px(Context resources, float sp) {
        final float scale = resources.getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }

    public static float sp2px(float sp) {
        final float scale = CommonTool.INSTANCE.getContext().getResources().getDisplayMetrics().scaledDensity;
        return sp * scale;
    }
}
