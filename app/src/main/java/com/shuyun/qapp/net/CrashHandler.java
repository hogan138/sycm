package com.shuyun.qapp.net;

import android.util.Log;

/**
 * 系统异常的捕获
 * Created by wujr on 16/8/3.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static CrashHandler crash = new CrashHandler();
    private static final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler(){
    }
    public static CrashHandler instance(){
        return crash;
    }
    public void init(){
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e(TAG, "未知异常被触发");
        ex.printStackTrace();
        mDefaultHandler.uncaughtException(thread, ex);
    }
}