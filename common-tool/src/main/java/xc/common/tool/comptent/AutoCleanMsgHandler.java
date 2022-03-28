package xc.common.tool.comptent;


import android.os.Handler;

import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.List;

import xc.common.tool.listener.SimpleLifecycleObserver;

public class AutoCleanMsgHandler extends Handler {

    public List<Runnable> runnables = new ArrayList<>();

    public AutoCleanMsgHandler(LifecycleOwner owner) {
        owner.getLifecycle().addObserver(new SimpleLifecycleObserver() {

            @Override
            public void onDestroyCall() {
                for (Runnable runnable : runnables) {
                    removeCallbacks(runnable);
                }
                removeCallbacksAndMessages(null);
                runnables.clear();
            }
        });
    }

    public final void postAutoCancel(Runnable runnable) {
        runnables.add(runnable);
        post(runnable);
    }

    public final void postDelayAutoCancel(Runnable runnable, long delayMillis) {
        runnables.add(runnable);
        postDelayed(runnable, delayMillis);
    }


}
