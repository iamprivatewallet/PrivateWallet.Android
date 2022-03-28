package xc.common.utils

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.os.LocaleList
import android.util.DisplayMetrics
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import xc.common.kotlinext.showToast
import xc.common.tool.CommonTool
import xc.common.tool.utils.AppManager
import xc.common.tool.utils.SWLog
import xc.common.tool.utils.checkIsEmpty
import java.io.File
import java.io.FileOutputStream
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

object LibAppUtils {


    fun updateLanguage(context: Context, locale: Locale): Context? {

        val configuration = Configuration(context.resources.configuration)

        val displayMetrics = context.resources.displayMetrics
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
//            Locale.setDefault(locale)
            configuration.setLocale(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                configuration.setLocales(LocaleList(locale))
            }
            return context.createConfigurationContext(configuration)
        } else {
            context.resources.updateConfiguration(configuration, displayMetrics)
            return context
        }
    }


    fun hasNewVersion(localVersion: String?, serviceVersion: String?): Boolean {
        if (localVersion.checkIsEmpty() || serviceVersion.checkIsEmpty()) {
            return false
        }

        try {
            return localVersion!!.replace(".", "").toInt() <
                    serviceVersion!!.replace(".", "").toInt()
        } catch (e: Exception) {
            return false
        }
    }

    fun getWindowSize(window: Window): Point {
        var point = Point();
        window.windowManager.defaultDisplay.getSize(point)
        return point
    }

    @SuppressLint("MissingPermission")
    fun getDeviceId(mContext: Context): String {
        return android.provider.Settings.Secure.getString(
            mContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID
        )
//        var manager = mContext.getSystemService(TELEPHONY_SERVICE) as TelephonyManager
//        return manager.deviceId
    }

    fun getPackageName(mContext: Context): String {
        return mContext.packageName
    }

    fun getAppVersionName(mContext: Context): String {
        return mContext.packageManager.getPackageInfo(mContext.packageName, 0).versionName
    }

    fun getDeviceName(mContext: Context): String {
        var model: String? = Build.MODEL
        if (model != null) {
            model = model.trim { it <= ' ' }.replace("\\s*".toRegex(), "")
        } else {
            model = ""
        }
        return model
    }


    fun isInstalled(mContext: Context, packageName: String): Boolean {
        var manager = mContext.getPackageManager()
        //获取所有已安装程序的包信息
        var installedPackages = manager.getInstalledPackages(0);
        if (installedPackages != null) {
            for (installedPackage in installedPackages) {
                if (installedPackage.packageName.equals(packageName))
                    return true;
            }
        }
        return false

    }

    fun copyText(content: String) {
        //获取剪贴板管理器：
        var manager =
            CommonTool.context!!.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // 创建普通字符型ClipData
        var mClipData = ClipData.newPlainText("Label", content);
        // 将ClipData内容放到系统剪贴板里。
        manager.setPrimaryClip(mClipData);
    }


    fun hideSoftKeyboard(activity: Activity) {
        var inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            activity.getCurrentFocus()!!.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }


    fun hideSoftKeyboardWithShowing(activity: Activity) {
        var inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputMethodManager.isActive) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS)
//            inputMethodManager.hideSoftInputFromWindow(
//                activity?.getCurrentFocus()?.getWindowToken(),
//                InputMethodManager.HIDE_NOT_ALWAYS
//            )
        } else {
            // DO NOTHING
        }

    }

    fun showSoftInputWindow(activity: Activity, view: View) {
        var inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED)
    }

    fun showSoftInput(activity: Activity) {
        var inputMethodManager =
            activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (inputMethodManager.isActive) {
            inputMethodManager.toggleSoftInput(0, InputMethodManager.SHOW_FORCED)
        }

    }

    fun call(activity: Activity, phone: String) {

        //创建打电话的意图
        var intent = Intent();
        //设置拨打电话的动作
        intent.setAction(Intent.ACTION_CALL);
        //设置拨打电话的号码
        intent.setData(Uri.parse("tel:" + phone));
        //开启打电话的意图
        activity.startActivity(intent);
    }


    fun isNetworkConnected(): Boolean {
        var context = CommonTool.context!!
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_NETWORK_STATE
            ) != 0
        ) {
            SWLog.e("not permission")
            return true
        }

        if (context != null) {
            val mConnectivityManager = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//            val mNetworkInfo = mConnectivityManager!!.getActiveNetworkInfo()
//            if (mNetworkInfo != null) {
//                return mNetworkInfo!!.isAvailable()
//            }
        }
        return false
    }

    fun viewToBitmap(view: View): Bitmap {
        val width = view.measuredWidth
        val height = view.measuredHeight
        val createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(createBitmap)
        canvas.drawColor(Color.WHITE)
//        view.layout(0, 0, width, height)
        view.draw(canvas)
        view.parent.requestLayout()
        return createBitmap
    }

    fun inviteViewToBitmap(view: View): Bitmap {
        val width = view.measuredWidth
        val height = view.measuredHeight
        val createBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(createBitmap)
        canvas.drawColor(Color.WHITE)
        view.layout(0, 0, width, height)
        view.draw(canvas)
        return createBitmap
    }

    fun saveBitmap(bitmap: Bitmap, dir: String, fileName: String): String {
        val directory = Environment.getExternalStorageDirectory().absolutePath + "/$dir"
        return saveBitmap1(bitmap, directory, fileName)
    }

    fun saveBitmap1(bitmap: Bitmap, dir: String, fileName: String): String {
        var directory = dir
        val dir = File(directory)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        val file = File(directory, fileName)
        if (!file.exists()) {
            file.createNewFile()
        }
        val outputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        outputStream.flush()
        outputStream.close()

//        var contentValues = ContentValues()
//        contentValues.put(MediaStore.Images.Media.DATA, file.getAbsolutePath())
//        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
//        contentValues.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis())
//        var uri = Constents.context!!.getContentResolver()
//            .insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

//        //通知相册更新
//        var string = MediaStore.Images.Media.insertImage(
//            BasicLibComponant.getContext().getContentResolver(),
//            bitmap, fileName, null
//        )

//        val intent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//        val uri = Uri.fromFile(file)
//        intent.data = uri
//        BasicLibComponant.getContext().sendBroadcast(intent)
        return file.absolutePath
    }

    fun isAppMainProgress(): Boolean {
        val pid = android.os.Process.myPid()
        val processAppName = getAppName(pid)
        if (processAppName == null || processAppName != CommonTool.context!!.getPackageName()) {
            // 则此application的onCreate 是被service 调用的，直接返回
            return false
        }
        return true
    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    fun getAppName(pid: Int): String? {
        var processName: String? = null
        val activityManager = CommonTool.context!!
            .getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val list = activityManager.getRunningAppProcesses()
        val i = list.iterator()
        while (i.hasNext()) {
            val info = i.next() as ActivityManager.RunningAppProcessInfo
            try {
                if (info.pid === pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName
                    // 返回当前进程名
                    return processName
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // 没有匹配的项，返回为null
        return null
    }


    fun phoneIsLongScreen(context: Context): Boolean {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outSize = Point()
        wm.defaultDisplay.getSize(outSize)
        val scaleWH = BigDecimal(outSize.y).divide(BigDecimal(outSize.x), 1, RoundingMode.DOWN)
            .compareTo(BigDecimal(1.8))
        return scaleWH == 1
    }


    /**
     * 判断手机是否含有虚拟按键  99%
     */
    fun hasVirtualNavigationBar(context: Context): Boolean {
        var hasSoftwareKeys = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val d =
                (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

            val realDisplayMetrics = DisplayMetrics()
            d.getRealMetrics(realDisplayMetrics)

            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels

            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)

            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels

            hasSoftwareKeys = realWidth - displayWidth > 0 || realHeight - displayHeight > 0
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            hasSoftwareKeys = !hasMenuKey && !hasBackKey
        }

        return hasSoftwareKeys
    }

    /**
     * 获取导航栏高度，有些没有虚拟导航栏的手机也能获取到，建议先判断是否有虚拟按键
     */
    fun getNavigationBarHeight(context: Context): Int {
        val resourceId =
            context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) context.resources.getDimensionPixelSize(resourceId) else 0
    }


    fun shareText(str: String, context: Context) {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "text/plain"
        textIntent.putExtra(Intent.EXTRA_TEXT, str)
        context.startActivity(textIntent)
    }


    fun shareImg(path: String, context: Context) {
        val textIntent = Intent(Intent.ACTION_SEND)
        textIntent.type = "image/*"
        textIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(path))
        context.startActivity(Intent.createChooser(textIntent, "分享"))
    }


    fun openBrowser(context: Context, url: String?) {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.data = Uri.parse(url)
        context.startActivity(intent)
    }


    var lastClickTime: Long = 0
    fun doubleClose(msg: String = "再次点击退出应用") {
        if (System.currentTimeMillis() - lastClickTime < 2000) {
            AppManager.getAppManager().AppExit()
        } else {
            showToast(msg)
            lastClickTime = System.currentTimeMillis()
        }
    }

}