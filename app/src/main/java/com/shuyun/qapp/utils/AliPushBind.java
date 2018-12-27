package com.shuyun.qapp.utils;

import android.content.Context;

import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.RemotingEx;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.utils
 * @ClassName: AliPushBind
 * @Description: 阿里push别名绑定
 * @Author: ganquan
 * @CreateDate: 2018/12/26 15:37
 */
public class AliPushBind {

    private static Context mContext;

    private AliPushBind(Context context) {
        this.mContext = context;
    }

    //阿里推送绑定别名
    public static void bindPush() {
        String deviceId = PushServiceFactory.getCloudPushService().getDeviceId();
        RemotingEx.doRequest(ApiServiceBean.pushBind(), new Object[]{deviceId}, null);

    }

    //阿里推送解除绑定别名
    public static void UnbindPush() {
        String deviceId = PushServiceFactory.getCloudPushService().getDeviceId();
        RemotingEx.doRequest(ApiServiceBean.pushUnbind(), new Object[]{deviceId}, null);

    }
}
