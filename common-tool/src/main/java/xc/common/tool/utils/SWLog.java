package xc.common.tool.utils;

import android.util.Log;

import xc.common.tool.CommonTool;


/**
 * Created by terry on 7/11/16.
 */

public class SWLog {

    public static final String TAG = "SWLog";
    public static boolean IS_DEBUG = true;

    public static void i(String tag, String message) {
        if (IS_DEBUG) {
            log(LE.i, tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (IS_DEBUG) {
            log(LE.d, tag, message);
        }
    }

    public static void e(String tag, String message) {
        if (IS_DEBUG) {
            log(LE.e, tag, message);
        }
    }

    public static void i(String paramString) {
        if (IS_DEBUG) {
            log(LE.i, TAG, paramString);
        }
    }

    public static void d(String paramString) {
        if (IS_DEBUG) {
            log(LE.d, TAG, paramString);
        }
    }

    public static void e(String paramString) {
        if (IS_DEBUG) {
            log(LE.e, TAG, paramString);
        }
    }

    public static void e(Object paramString) {
        if (IS_DEBUG) {
            log(LE.e, TAG, CommonTool.INSTANCE.getGson().toJson(paramString));
        }
    }

    public static void log(LE logType, String TAG, String message) {
        if (IS_DEBUG) {
            switch (logType) {
                case i:
                    Log.i(TAG, message);
                    break;
                case d:
                    Log.d(TAG, message);
                    break;
                case e:
                    Log.e(TAG, message);
                    break;
                default:
                    Log.i(TAG, message);
                    break;
            }
        }
    }

    public enum LE {
        d, e, i
    }

}
