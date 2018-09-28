package com.shuyun.qapp.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.bean.PushBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.InformatListenner;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.ui.integral.MyPrizeActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.mine.MinePrizeActivity;
import com.shuyun.qapp.utils.ExampleUtil;
import com.shuyun.qapp.utils.Logger;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;

import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG-Example";
    InformatListenner listenner;

    /**
     * 监听广播接收器的接收到的数据
     *
     * @param listenner
     */
    public void setOnMsgListenner(InformatListenner listenner) {
        this.listenner = listenner;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Logger.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Logger.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "收到了自定义消息。唯一标识消息的 ID是:" + bundle.getString(JPushInterface.EXTRA_MSG_ID));
                Log.d(TAG, "收到了自定义消息。消息的标题是:" + bundle.getString(JPushInterface.EXTRA_TITLE));
                Log.d(TAG, "收到了自定义消息。消息内容是:" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                Log.d(TAG, "收到了自定义消息。消息的附加字段是:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
                processCustomMessage(context, bundle);
                /**
                 * 如果收到极光消息0:更换图标,调接口;1:存储到本地,
                 */
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "收到了通知,通知标题: " + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
                Log.d(TAG, "收到了通知,通知内容:" + bundle.getString(JPushInterface.EXTRA_ALERT));
                Log.d(TAG, "收到了通知,通知的ID是:" + bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID) + "");
                Log.d(TAG, "收到了通知,通知附加字段是:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
                Log.d(TAG, "收到了通知,通知类型是" + bundle.getString(JPushInterface.EXTRA_ALERT_TYPE));

                /**
                 * 如果收到极光消息0:更换图标,调接口;1:存储到本地,
                 */
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                Log.d(TAG, "收到了通知,通知标题: " + bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE));
                Log.d(TAG, "收到了通知,通知内容:" + bundle.getString(JPushInterface.EXTRA_ALERT));
                Log.d(TAG, "收到了通知,通知的ID是:" + bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID) + "");
                Log.d(TAG, "收到了通知,通知附加字段是:" + bundle.getString(JPushInterface.EXTRA_EXTRA));
                Log.d(TAG, "收到了通知,通知类型是" + bundle.getString(JPushInterface.EXTRA_ALERT_TYPE));
                /**
                 * 如果收到极光消息0:更换图标,调接口;1:存储到本地,
                 */
                Long expire = (Long) SharedPrefrenceTool.get(context, "expire", System.currentTimeMillis());//token的有效时间
                long currentTimeMillis = System.currentTimeMillis();
                if (!AppConst.isLogon() || currentTimeMillis >= expire) {
                    //拉起登录界面
                    Intent i = new Intent(context, LoginActivity.class);
                    MyApplication.getAppContext().startActivity(i);
                    return;
                } else {
                    PushBean pushBean = JSON.parseObject(bundle.getString(JPushInterface.EXTRA_EXTRA), PushBean.class);
                    Intent i;
                    //积分夺宝中奖通知
                    if (pushBean.getPushAction().equals("push.integral.snatch.notify")) {
                        i = new Intent(context, MyPrizeActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    } else if (pushBean.getPushAction().equals("push.answer.get.notify")) {
                        SaveUserInfo.getInstance(context).setUserInfo("action_msg", "action_msg");
                        //每8小时可领取答题次数通知
                        i = new Intent(context, HomePageActivity.class);
                        i.putExtra("from", "msg");
                        context.startActivity(i);
                    } else if (pushBean.getPushAction().equals("push.prize.expire.notify")) {
                        //奖品快过期通知
                        i = new Intent(context, MinePrizeActivity.class);
                        i.putExtra("status", 1);
                        i.putExtra("certification", SaveUserInfo.getInstance(context).getUserInfo("cert"));
                        context.startActivity(i);
                    } else if (pushBean.getPushAction().equals("push.prize.notity")) {
                        // 奖品通知
                        i = new Intent(context, MinePrizeActivity.class);
                        i.putExtra("status", 1);
                        i.putExtra("certification", SaveUserInfo.getInstance(context).getUserInfo("cert"));
                        context.startActivity(i);
                    } else if (pushBean.getPushAction().equals("push.withdraw.success.notify")) {
                        //提现成功通知
                        i = new Intent(context, WebBannerActivity.class);
                        i.putExtra("url", pushBean.getPushData());
                        i.putExtra("name", "提现成功");//名称 标题
                        context.startActivity(i);
                    } else if (pushBean.getPushAction().equals("push.withdraw.error.notify")) {
                        //提现失败通知
                        i = new Intent(context, WebBannerActivity.class);
                        i.putExtra("url", pushBean.getPushData());
                        i.putExtra("name", "提现失败");//名称 标题
                        context.startActivity(i);
                    } else if (pushBean.getPushAction().equals("push.deliver.goods.notity")) {
                        //发货通知
                        i = new Intent(context, MinePrizeActivity.class);
                        i.putExtra("status", 2);
                        i.putExtra("certification", SaveUserInfo.getInstance(context).getUserInfo("cert"));
                        context.startActivity(i);
                    } else {
                        i = new Intent(context, HomePageActivity.class);
                        i.putExtras(bundle);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(i);
                    }

                }
                //接受极光推送的信息
//                Intent intent = getIntent();
//                if (null != intent) {
//                    Bundle bundle = getIntent().getExtras();
//                    String title = null;
//                    String content = null;
//                    if (bundle != null) {
//                        title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE); //标题
//                        content = bundle.getString(JPushInterface.EXTRA_ALERT); //推送内容
//                    }
//                }
            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Logger.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Logger.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Logger.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }

    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            } else if (key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Logger.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = JSON.parseObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it = json.keySet().iterator();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " + json.getString(myKey) + "]");
                    }
                } catch (Exception e) {
                    Logger.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }

    //send msg to HomePageActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (HomePageActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(HomePageActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(HomePageActivity.KEY_MESSAGE, message);
            if (!ExampleUtil.isEmpty(extras)) {
                try {
                    JSONObject extraJson = JSON.parseObject(extras);
                    if (!extraJson.isEmpty()) {
                        msgIntent.putExtra(HomePageActivity.KEY_EXTRAS, extras);
                    }
                } catch (Exception e) {

                }
            }
            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        }
    }
}
