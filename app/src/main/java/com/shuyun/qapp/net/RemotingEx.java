package com.shuyun.qapp.net;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.utils.NetWorkUtils;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.ToastUtil;

import java.lang.reflect.Method;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */
public class RemotingEx {

    /**
     * 执行请求
     *
     * @param method   方法
     * @param params   参数
     * @param listener 回调响应
     */
    public static <T> void doRequest(String method, Object[] params, final OnRemotingCallBackListener<T> listener) {
        doRequest(null, method, params, listener);
    }

    /**
     * 执行请求
     *
     * @param method   方法
     * @param listener 回调响应
     */
    public static <T> void doRequest(String method, final OnRemotingCallBackListener<T> listener) {
        doRequest(null, method, null, listener);
    }

    /**
     * 执行请求
     *
     * @param action   动作类型
     * @param method   方法
     * @param params   参数
     * @param listener 回调响应
     */
    public static <T> void doRequest(final String action, String method, Object[] params, final OnRemotingCallBackListener<T> listener) {
        try {
            ApiService apiService = BasePresenter.Builder();
            Method m = null;
            if (params != null && params.length > 0) {
                Class[] classes = new Class[params.length];
                for (int i = 0, j = params.length; i < j; i++) {
                    classes[i] = params[i].getClass();
                }
                try {
                    m = apiService.getClass().getDeclaredMethod(method, classes);
                } catch (Exception e) {
                    Method[] methods = apiService.getClass().getDeclaredMethods();
                    for (Method mt : methods) {
                        if (method.equals(mt.getName())) {
                            m = mt;
                            break;
                        }
                    }
                }
            } else {
                m = apiService.getClass().getDeclaredMethod(method);
            }
            Observable<DataResponse<T>> observable = (Observable<DataResponse<T>>) m.invoke(apiService, params);
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DataResponse<T>>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(DataResponse<T> response) {
                            try {
                                if (listener != null)
                                    listener.onSucceed(action, response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            //保存错误信息
                            SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                            //网络不可用
                            if (!NetWorkUtils.isNetworkConnected(SykscApplication.getAppContext())) {
                                ToastUtil.showToast(SykscApplication.getAppContext(), "网络不可用，请检查网络连接");
                            }
                            if (listener != null)
                                listener.onFailed(action, e.getMessage());
                        }

                        @Override
                        public void onComplete() {
                            if (listener != null)
                                listener.onCompleted(action);
                        }
                    });
        } catch (Exception e) {
            if (listener != null)
                listener.onFailed(action, e.getMessage());
            if (listener != null)
                listener.onCompleted(action);
        }
    }

}
