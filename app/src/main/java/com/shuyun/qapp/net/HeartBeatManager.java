package com.shuyun.qapp.net;

import android.content.Context;
import android.os.Handler;

import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
        if(!AppConst.isLogon())
            return;
        //执行接口请求
        ApiService apiService = BasePresenter.create(8000);
        apiService.heartBeat()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<Object>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<Object> DataResponse) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        doHeart();
                    }
                });
    }

    public void stop() {
        running = false;
    }
}
