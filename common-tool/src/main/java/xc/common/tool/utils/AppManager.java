package xc.common.tool.utils;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Iterator;
import java.util.Stack;


public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }


    public static synchronized void init(Application application){

        if (instance == null) {
            instance = new AppManager();
            activityStack = new Stack<Activity>();

            application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                    activityStack.add(activity);
                }

                @Override
                public void onActivityStarted(@NonNull Activity activity) {

                }

                @Override
                public void onActivityResumed(@NonNull Activity activity) {

                }

                @Override
                public void onActivityPaused(@NonNull Activity activity) {

                }

                @Override
                public void onActivityStopped(@NonNull Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(@NonNull Activity activity) {
                    instance.removeActivity(activity);

                }
            });
        }

    }

    /**
     * 单实例 , UI无需考虑多线程同步问题
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            throw new RuntimeException("appmanger 必须初始化 之后才能调用实例");
        }
        return instance;
    }

    /**
     * 添加Activity到栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    public Stack<Activity> getAllActivity() {
        return activityStack;
    }

    /**
     * 获取当前Activity（栈顶Activity）
     */
    public Activity currentActivity() {
        if (activityStack == null || activityStack.isEmpty()) {
            return null;
        }
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 获取当前Activity（栈顶Activity） 没有找到则返回null
     */
    public<T extends Activity> T findActivity(Class<T> cls) {
        T activity = null;
        for (Activity aty : activityStack) {
            if (aty.getClass().equals(cls)) {
                activity = (T) aty;
                break;
            }
        }
        return activity;
    }

    public boolean hasActivity(Class<? extends Activity> clazz) {
        if (activityStack.isEmpty()) {
            return false;
        }
        for (Activity activity : activityStack) {
            if (activity.getClass() == clazz) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结束当前Activity（栈顶Activity）
     */
    public void removeActivity(Activity activity) {
        activityStack.remove(activity);
    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Activity activity) {

        if (activity != null) {
            // 为与系统Activity栈保持一致，且考虑到手机设置项里的"不保留活动"选项引起的Activity生命周期调用onDestroy()方法所带来的问题,此处需要作出如下修正
            if (activity.isFinishing()) {
                activityStack.remove(activity);
            } else {
                activityStack.remove(activity);
                activity.finish();
            }
        }

    }

    /**
     * 结束指定的Activity(重载)
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity.getClass().equals(cls)) {
                activity.finish();
            }
        }
    }

    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Activity cls) {
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (activity != cls) {
                activity.finish();
                iterator.remove();
            }
        }
    }
    /**
     * 关闭除了指定activity以外的全部activity 如果cls不存在于栈中，则栈全部清空
     *
     * @param cls
     */
    public void finishOthersActivity(Class<?> cls) {
        Iterator<Activity> iterator = activityStack.iterator();
        while (iterator.hasNext()) {
            Activity activity = iterator.next();
            if (!(activity.getClass().equals(cls))) {
                activity.finish();
                iterator.remove();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 应用程序退出
     */
    public void AppExit() {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
            System.exit(0);
        }
    }
}
