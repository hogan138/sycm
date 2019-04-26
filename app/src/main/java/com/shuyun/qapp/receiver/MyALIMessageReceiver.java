package com.shuyun.qapp.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.shuyun.qapp.bean.AliPushBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.integral.MyPrizeActivity;
import com.shuyun.qapp.ui.mine.AddWithdrawInfoActivity;
import com.shuyun.qapp.ui.mine.MinePrizeActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.SaveUserInfo;

import java.util.Map;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.receiver
 * @ClassName: MyALIMessageReceiver
 * @Description: 阿里推送消息接收服务
 * @Author: ganquan
 * @CreateDate: 2018/12/26 13:52
 */
public class MyALIMessageReceiver extends MessageReceiver {

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        AliPushBean aliPushBean = JSON.parseObject(extraMap, AliPushBean.class);
        String pushData = aliPushBean.getPushData();
        String pushAction = aliPushBean.getPushAction();
        Intent i;
        if (AppConst.PUSH_INTEGRAL.equals(pushAction)) {
            //积分夺宝中奖通知
            context.startActivity(new Intent(context, MyPrizeActivity.class));
        } else if (AppConst.PUSH_ANSWER_GET.equals(pushAction)) {
            //每8小时可领取答题次数通知
            SaveUserInfo.getInstance(context).setUserInfo("action_msg", "action_msg");
            i = new Intent(context, HomePageActivity.class);
            i.putExtra("from", "msg");
            context.startActivity(i);
        } else if (AppConst.PUSH_PRIZE.equals(pushAction) || AppConst.PUSH_PRIZE_EXPIRE.equals(pushAction)) {
            // 奖品通知、奖品快过期通知
            i = new Intent(context, MinePrizeActivity.class);
            i.putExtra("status", 1);
            context.startActivity(i);
        } else if (AppConst.PUSH_WITHDRAW_SUCCESS.equals(pushAction)) {
            //提现成功通知
            i = new Intent(context, WebH5Activity.class);
            i.putExtra("url", pushData);
            i.putExtra("name", "提现成功");//名称 标题
            context.startActivity(i);
        } else if (AppConst.PUSH_WITHDRAW_ERROR.equals(pushAction)) {
            //提现失败通知
            i = new Intent(context, WebH5Activity.class);
            i.putExtra("url", pushData);
            i.putExtra("name", "提现失败");//名称 标题
            context.startActivity(i);
        } else if (AppConst.PUSH_DEFAULT.equals(pushAction)) {
            //默认跳转h5
            i = new Intent(context, WebH5Activity.class);
            i.putExtra("url", pushData);
            i.putExtra("name", "全民共进");//名称 标题
            context.startActivity(i);
        } else if (AppConst.PUSH_DELEVER_GOODS.equals(pushAction)) {
            //发货通知
            i = new Intent(context, MinePrizeActivity.class);
            i.putExtra("status", 2);
            context.startActivity(i);
        } else if (AppConst.PUSH_REAL.equals(pushAction)) {
            //实名认证
            context.startActivity(new Intent(context, RealNameAuthActivity.class));
        } else if (AppConst.PUSH_WITHDRAW_INFO.equals(pushAction)) {
            //完善提现信息
            context.startActivity(new Intent(context, AddWithdrawInfoActivity.class));
        }

    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
    }
}
