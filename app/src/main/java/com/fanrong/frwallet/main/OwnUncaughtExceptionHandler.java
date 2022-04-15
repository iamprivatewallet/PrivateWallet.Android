package com.fanrong.frwallet.main;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class OwnUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(@NonNull @NotNull Thread t, @NonNull @NotNull Throwable ex) {
        StackTraceElement[] elements = ex.getStackTrace();

        StringBuilder reason =new StringBuilder(ex.toString());

        if (elements !=null && elements.length >0) {
            for (StackTraceElement element : elements) {
                reason.append("\n");

                reason.append(element.toString());

            }

        }

        Log.e("zyq", reason.toString());

//        android.os.Process.killProcess(android.os.Process.myPid());

    }

}
