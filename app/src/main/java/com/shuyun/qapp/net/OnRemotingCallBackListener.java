package com.shuyun.qapp.net;

import com.shuyun.qapp.bean.DataResponse;

/**
 * 网络请求响应接口
 * @param <T>
 */
public interface OnRemotingCallBackListener<T> {

    /**
     * 默认回调
     * @param action 动作类型
     */
    void onCompleted(String action);

    /**
     * 失败回调
     * @param action 动作类型
     * @param message 错误信息
     */
    void onFailed(String action, String message);

    /**
     * 成功回调
     * @param action 动作类型
     * @param response 响应内容
     */
    void onSucceed(String action, DataResponse<T> response);
}