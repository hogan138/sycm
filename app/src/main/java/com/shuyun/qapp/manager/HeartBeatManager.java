package com.shuyun.qapp.manager;

import android.content.Context;
import android.os.Handler;

import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.homepage.HomePageActivity;

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
        if(!(context instanceof HomePageActivity))
            return;
        if (running)
            return;
        running = true;
        doHeart();
    }

    private void doHeart() {
        if(!running)
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
        RemotingEx.doRequest(RemotingEx.Builder().heartBeat(), new OnRemotingCallBackListener<Object>() {
            @Override
            public void onCompleted(String action) {
                doHeart();
            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<Object> response) {

            }
        });
    }

    public void stop() {
        running = false;
    }
}
