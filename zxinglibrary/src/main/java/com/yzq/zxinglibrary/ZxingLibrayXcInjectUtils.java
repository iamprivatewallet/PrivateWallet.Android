package com.yzq.zxinglibrary;

import android.content.Context;

public class ZxingLibrayXcInjectUtils {
    public static AttachBaseContext attachBaseContext;
    public static void registerActivity_AttachBaseContext(AttachBaseContext attachBaseContext1) {
        attachBaseContext = attachBaseContext1;

    }

    public static interface AttachBaseContext {
        public Context attachBaseContext(Context context);
    }
}
