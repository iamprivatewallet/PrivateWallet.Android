package com.lzy.imagepicker;

import android.content.Context;

public class ImagePickerXcInjectUtils {
    public static AttachBaseContext attachBaseContext;
    public static void registerActivity_AttachBaseContext(AttachBaseContext attachBaseContext1) {
        attachBaseContext = attachBaseContext1;

    }

    public static interface AttachBaseContext {
        public Context attachBaseContext(Context context);
    }
}
