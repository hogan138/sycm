package com.shuyun.qapp.net;

import android.content.Context;
import android.os.Handler;

/**
 * 心跳检测 在Activity的onResume中执行
 */
public class HeartBeatManager {

    private static HeartBeatManager manager = null;
    private Handler mHandler = new Handler();
    private boolean running = false;
    private static final int HeartBeatMinutes = 5;

    protected HeartBeatManager() {
    }

    public static HeartBeatManager instance() {
        if (manager == null)
            manager = new HeartBeatManager();
        return manager;
    }

    public void start(Context context) {
        running = true;
        doHeart();
    }

    private void doHeart() {
        if (!running)
            return;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                doCall();
            }
        }, HeartBeatMinutes * 1000 * 60);
    }

    private void doCall() {
        if (!AppConst.isLogin())
            return;
        //执行接口请求
        RemotingEx.doRequest(null, ApiServiceBean.heartBeat(), null, null);
    }

    public void stop() {
        running = false;
    }
}
