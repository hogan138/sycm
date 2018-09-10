package com.shuyun.qapp.utils;

import android.content.Context;
import android.util.Log;

import com.tencent.stat.StatConfig;
import com.tencent.stat.StatCrashCallback;
import com.tencent.stat.StatCrashReporter;
import com.tencent.stat.StatTrackLog;

public class MTACrashModule {
    public static void initMtaCrashModule(Context app){
        StatCrashReporter crashReporter = StatCrashReporter.getStatCrashReporter(app);
        // 开启异常时的实时上报
        crashReporter.setEnableInstantReporting(true);
        // 开启java异常捕获
        crashReporter.setJavaCrashHandlerStatus(true);
        // 开启Native c/c++，即so的异常捕获
        // 请根据需要添加，记得so文件
        crashReporter.setJniNativeCrashStatus(true);

        // 添加关键日志，若crash时会把关键日志信息一起上报并展示，协助定位
        StatTrackLog.log("init module");
        // 添加Crash的标签，crash时会自动上报并展示，协助定位
        StatConfig.setCrashKeyValue("qmgj", "android");

        // crash时的回调，业务可根据需要自选决定是否添加
        crashReporter.addCrashCallback(new StatCrashCallback() {

            @Override
            public void onJniNativeCrash(String tombstoneString) {
                // native dump内容，包含异常信号、进程、线程、寄存器、堆栈等信息
                // 具体请参考：Android原生的tombstone文件格式
                Log.d("app", "MTA StatCrashCallback onJniNativeCrash:\n" + tombstoneString);
            }

            @Override
            public void onJavaCrash(Thread thread, Throwable ex) {
                //thread:crash线程信息
                // ex:crash堆栈
                Log.d("app", "MTA StatCrashCallback onJavaCrash:\n", ex);
            }
        });
    }
}
