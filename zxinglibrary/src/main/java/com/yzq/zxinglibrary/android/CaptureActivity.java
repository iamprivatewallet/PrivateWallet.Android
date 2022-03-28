package com.yzq.zxinglibrary.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.google.zxing.Result;
import com.yzq.zxinglibrary.BottomJumpDialog;
import com.yzq.zxinglibrary.R;
import com.yzq.zxinglibrary.ZxingLibrayXcInjectUtils;
import com.yzq.zxinglibrary.bean.ZxingConfig;
import com.yzq.zxinglibrary.camera.CameraManager;
import com.yzq.zxinglibrary.common.Constant;
import com.yzq.zxinglibrary.decode.DecodeImgCallback;
import com.yzq.zxinglibrary.decode.DecodeImgThread;
import com.yzq.zxinglibrary.decode.ImageUtil;
import com.yzq.zxinglibrary.view.ViewfinderView;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import xc.common.viewlib.view.customview.FullScreenDialog;


/**
 * @author: yzq
 * @date: 2017/10/26 15:22
 * @declare :扫一扫
 */

public class CaptureActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final String TAG = CaptureActivity.class.getSimpleName();
    public ZxingConfig config;
    private SurfaceView previewView;
    private TextView tv_flashlight;
    private ViewfinderView viewfinderView;
    private AppCompatImageView flashLightIv;
    private TextView flashLightTv;
    private ImageView backIv;
    private TextView tv_home;
    private LinearLayoutCompat flashLightLayout;
    private LinearLayoutCompat albumLayout;
    private LinearLayoutCompat bottomLayout;
    private boolean hasSurface;
    private InactivityTimer inactivityTimer;
    private BeepManager beepManager;
    private CameraManager cameraManager;
    private CaptureActivityHandler handler;
    private SurfaceHolder surfaceHolder;

    @Override
    protected void attachBaseContext(Context newBase) {
//        ZxingLibrayXcInjectUtils.attachBaseContext.attachBaseContext(newBase)
        super.attachBaseContext(newBase);
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }


    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 保持Activity处于唤醒状态
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        }

        /*先获取配置信息*/
        try {
            config = (ZxingConfig) getIntent().getExtras().get(Constant.INTENT_ZXING_CONFIG);
        } catch (Exception e) {

            Log.i("config", e.toString());
        }

        if (config == null) {
            config = new ZxingConfig();
        }


        setContentView(R.layout.activity_capture);


        initView();

        hasSurface = false;

        inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        beepManager.setPlayBeep(config.isPlayBeep());
        beepManager.setVibrate(config.isShake());


    }


    private void initView() {
        previewView = findViewById(R.id.preview_view);
        previewView.setOnClickListener(this);
        tv_flashlight = findViewById(R.id.tv_flashlight);
        tv_flashlight.setOnClickListener(this);

        LinearLayout ll_flashlight = findViewById(R.id.ll_flashlight);
        ViewGroup.LayoutParams layoutParams = ll_flashlight.getLayoutParams();
        layoutParams.height = (int) (getScreenWidth() * 0.6);
        layoutParams.width = (int) (getScreenWidth() * 0.6);
        ll_flashlight.setLayoutParams(layoutParams);

        viewfinderView = findViewById(R.id.viewfinder_view);
        viewfinderView.setZxingConfig(config);


        backIv = findViewById(R.id.backIv);
        backIv.setOnClickListener(this);

        tv_home = findViewById(R.id.tv_home);
        tv_home.setOnClickListener(this);

        TextView tv_gallery = findViewById(R.id.tv_gallery);
        tv_gallery.setOnClickListener(this);


    }


    public int getScreenWidth() {
        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        return width;
    }


    /**
     * @param pm
     * @return 是否有闪光灯
     */
    public static boolean isSupportCameraLedFlash(PackageManager pm) {
        if (pm != null) {
            FeatureInfo[] features = pm.getSystemAvailableFeatures();
            if (features != null) {
                for (FeatureInfo f : features) {
                    if (f != null && PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * @param flashState 切换闪光灯图片
     */
    public void switchFlashImg(int flashState) {

        if (flashState == Constant.FLASH_OPEN) {
            flashLightIv.setImageResource(R.drawable.ic_open);
            flashLightTv.setText(getString(R.string.zxinglib_CaptureActivity_text1));
        } else {
            flashLightIv.setImageResource(R.drawable.ic_close);
            flashLightTv.setText(getString(R.string.zxinglib_CaptureActivity_text1));
        }

    }

    /**
     * @param rawResult 返回的扫描结果
     */
    public void handleDecode(Result rawResult) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        if (removePrefix(rawResult.getText()).length()==40){
            if (getIntent().getStringExtra(Constant.JUMP_TYPE)!=null){
                Intent intent = getIntent();
                intent.putExtra(Constant.CODED_CONTENT, rawResult.getText());
                setResult(RESULT_OK, intent);
                finish();
                return;
            }
            showBottomJumpDialog(rawResult);
            return;
        }
        Intent intent = getIntent();
        intent.putExtra(Constant.CODED_CONTENT, rawResult.getText());
        setResult(RESULT_OK, intent);
        this.finish();
    }


    private void switchVisibility(View view, boolean b) {
        if (b) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }
    private void showBottomJumpDialog(Result rawResult){
            final BottomJumpDialog bottomJumpDialog = new BottomJumpDialog(this);
            bottomJumpDialog.setOnAddAdrresListener(new BottomJumpDialog.OnAddAdrresListener() {
                @Override
                public void jump() {
                    //跳转添加地址
                    Intent intent = getIntent();
                    intent.putExtra(Constant.CODED_CONTENT, rawResult.getText());
                    intent.putExtra(Constant.JUMP_TYPE, "AddAddress");
                    setResult(RESULT_OK, intent);
                    bottomJumpDialog.dismiss();
                    finish();
                }
            });
            bottomJumpDialog.setOnConfrim(new FullScreenDialog.OnConfirmListener() {
                @Override
                public void confirm(@Nullable Object params) {
                    //转账
                    Intent intent = getIntent();
                    intent.putExtra(Constant.CODED_CONTENT, rawResult.getText());
                    intent.putExtra(Constant.JUMP_TYPE, "Transfer");
                    setResult(RESULT_OK, intent);
                    bottomJumpDialog.dismiss();
                    finish();
                }
            });
            bottomJumpDialog.show();
    }


    @Override
    protected void onResume() {
        super.onResume();

        cameraManager = new CameraManager(getApplication(), config);

        viewfinderView.setCameraManager(cameraManager);
        handler = null;

        surfaceHolder = previewView.getHolder();
        if (hasSurface) {

            initCamera(surfaceHolder);
        } else {
            // 重置callback，等待surfaceCreated()来初始化camera
            surfaceHolder.addCallback(this);
        }

        beepManager.updatePrefs();
        inactivityTimer.onResume();

    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            return;
        }
        try {
            // 打开Camera硬件设备
            cameraManager.openDriver(surfaceHolder);
            // 创建一个handler来打开预览，并抛出一个运行时异常
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager);
            }
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.zxinglib_CaptureActivity_text3));
        builder.setMessage(getString(R.string.zxinglib_msg_camera_framework_bug));
        builder.setPositiveButton(R.string.zxinglib_button_ok, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    protected void onPause() {

        Log.i("CaptureActivity", "onPause");
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        inactivityTimer.onPause();
        beepManager.close();
        cameraManager.closeDriver();

        if (!hasSurface) {

            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        inactivityTimer.shutdown();
        super.onDestroy();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
                               int height) {

    }


    boolean isTurnOn = false;

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.backIv || i == R.id.tv_home) {
            finish();
        } else if (i == R.id.tv_gallery) {
            toGallery();
//            Toast.makeText(this, "相册", Toast.LENGTH_SHORT).show();
        } else if (i == R.id.tv_flashlight) {
            if (!isTurnOn) {
                isTurnOn = true;
                cameraManager.turnLightOn();
            } else {
                isTurnOn = false;
                cameraManager.turnLightOff();
            }
        }

    }

    void toGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, Constant.REQUEST_IMAGE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constant.REQUEST_IMAGE && resultCode == RESULT_OK) {
            String path = ImageUtil.getImageAbsolutePath(this, data.getData());

            new DecodeImgThread(path, new DecodeImgCallback() {
                @Override
                public void onImageDecodeSuccess(Result result) {
                    handleDecode(result);
                }

                @Override
                public void onImageDecodeFailed() {
                    Toast.makeText(CaptureActivity.this, getString(R.string.zxinglib_CaptureActivity_text4), Toast.LENGTH_SHORT).show();
                }
            }).run();

        }
    }
    public String removePrefix(String s){
        if (s.startsWith("0x")){
            return s.substring(2);
        }
        return s;
    }

}
