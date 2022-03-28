package xc.common.viewlib.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.core.graphics.ColorUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class StatusBarUtils {

    /**
     * 修改状态栏背景为透明状态并全屏，字体颜色随着深浅颜色自动改变
     */
    public static void setStatusBarColor(Activity activity, int bgColor) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = activity.getWindow();
            // 设置状态栏底色颜色为透明
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
             if (isLightColor(bgColor)) {
                 setAndroidNativeLightStatusBar(activity,true);
             } else {
                 setAndroidNativeLightStatusBar(activity,false);
             }
             }
        }

        /**
         * 谷歌原生修改状态栏字体颜色
         */
        private static void setAndroidNativeLightStatusBar (Activity activity,boolean dark){
            View decor = activity.getWindow().getDecorView();
            if (dark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        }

        /**
         * 小米修改字体颜色
         */
        public static boolean MIUISetStatusBarLightMode (Activity activity,boolean dark){
            boolean result = false;
            Window window = activity.getWindow();
            if (window != null) {
                Class clazz = window.getClass();
                try {
                    int darkModeFlag = 0;
                    Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                    Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                    darkModeFlag = field.getInt(layoutParams);
                    Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                    if (dark) {
                        extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                    } else {
                        extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                    }
                    result = true;

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && RomUtils.isMiUIV7OrAbove()) {
                        //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                        if (dark) {
                            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        } else {
                            activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        }
                    }
                } catch (Exception e) {

                }
            }
            return result;
        }

        /**
         * 魅族修改字体颜色
         */
        private static boolean setFlymeLightStatusBar (Activity activity,boolean dark){
            boolean result = false;
            if (activity != null) {
                try {
                    WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
                    Field darkFlag = WindowManager.LayoutParams.class
                            .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                    Field meizuFlags = WindowManager.LayoutParams.class
                            .getDeclaredField("meizuFlags");
                    darkFlag.setAccessible(true);
                    meizuFlags.setAccessible(true);
                    int bit = darkFlag.getInt(null);
                    int value = meizuFlags.getInt(lp);
                    if (dark) {
                        value |= bit;
                    } else {
                        value &= ~bit;
                    }
                    meizuFlags.setInt(lp, value);
                    activity.getWindow().setAttributes(lp);
                    result = true;
                } catch (Exception e) {
                }
            }
            return result;
        }

        private static boolean isLightColor ( @ColorInt int color){
            return ColorUtils.calculateLuminance(color) >= 0.5;
        }

    }
