package com.fanrong.frwallet.tools;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ShareUtils {
    public static void shareImage(Bitmap bmp, String content, Context mContext) {
        /**
         * 微信7.0版本号，兼容处理微信7.0版本分享到朋友圈不支持多图片的问题
         */
        final int VERSION_CODE_FOR_WEI_XIN_VER7 = 1380;
        /**
         * 微信包名
         */
        final String PACKAGE_NAME_WEI_XIN = "com.tencent.mm";
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsoluteFile();
        String fileName = "share";
        File appDir = new File(file, fileName);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        fileName = content + ".jpg";
        File currentFile = new File(appDir, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(currentFile);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ArrayList<Uri> uris = new ArrayList<>();
        Uri uri = null;
        try {
            ApplicationInfo applicationInfo = mContext.getApplicationInfo();
            int targetSDK = applicationInfo.targetSdkVersion;
            if (targetSDK >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uri = Uri.parse(android.provider.MediaStore.Images.Media.insertImage(mContext.getContentResolver(), currentFile.getAbsolutePath(), fileName, null));
            } else {
                uri = Uri.fromFile(new File(currentFile.getPath()));
            }
            uris.add(uri);
        } catch (Exception ex) {

        }
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//        ComponentName comp = new ComponentName(PACKAGE_NAME_WEI_XIN, "com.tencent.mm.ui.tools.ShareToTimeLineUI");
//        intent.setComponent(comp);
        intent.setType("image/*");
        if (VersionUtil.getVersionCode(mContext, PACKAGE_NAME_WEI_XIN) < VERSION_CODE_FOR_WEI_XIN_VER7) {
            // 微信7.0以下版本
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        } else {
            // 微信7.0及以上版本
            intent.setAction(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        }
        mContext.startActivity(Intent.createChooser(intent, content));
    }


    public static void shareUrlToOther(String title, Context mContext) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, title);
        mContext.startActivity(Intent.createChooser(intent, title));
    }
}
