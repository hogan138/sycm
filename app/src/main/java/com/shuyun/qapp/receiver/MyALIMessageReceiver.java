package com.shuyun.qapp.receiver;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.shuyun.qapp.bean.AliPushBean;
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
        //积分夺宝中奖通知
        if (("push.integral.snatch.notify").equals(pushAction)) {
            context.startActivity(new Intent(context, MyPrizeActivity.class));
        } else if (("push.answer.get.notify").equals(pushAction)) {
            SaveUserInfo.getInstance(context).setUserInfo("action_msg", "action_msg");
            //每8小时可领取答题次数通知
            i = new Intent(context, HomePageActivity.class);
            i.putExtra("from", "msg");
            context.startActivity(i);
        } else if (("push.prize.notity").equals(pushAction) || ("push.prize.expire.notify").equals(pushAction)) {
            // 奖品通知、奖品快过期通知
            i = new Intent(context, MinePrizeActivity.class);
            i.putExtra("status", 1);
            i.putExtra("certification", SaveUserInfo.getInstance(context).getUserInfo("cert"));
            context.startActivity(i);
        } else if (("push.withdraw.success.notify").equals(pushAction)) {
            //提现成功通知
            i = new Intent(context, WebH5Activity.class);
            i.putExtra("url", pushData);
            i.putExtra("name", "提现成功");//名称 标题
            context.startActivity(i);
        } else if (("push.withdraw.error.notify").equals(pushAction)) {
            //提现失败通知
            i = new Intent(context, WebH5Activity.class);
            i.putExtra("url", pushData);
            i.putExtra("name", "提现失败");//名称 标题
            context.startActivity(i);
        } else if (("push.default").equals(pushAction)) {
            //默认跳转h5
            i = new Intent(context, WebH5Activity.class);
            i.putExtra("url", pushData);
            i.putExtra("name", "全民共进");//名称 标题
            context.startActivity(i);
        } else if (("push.deliver.goods.notity").equals(pushAction)) {
            //发货通知
            i = new Intent(context, MinePrizeActivity.class);
            i.putExtra("status", 2);
            i.putExtra("certification", SaveUserInfo.getInstance(context).getUserInfo("cert"));
            context.startActivity(i);
        } else if (("push.real").equals(pushAction)) {
            //实名认证
            context.startActivity(new Intent(context, RealNameAuthActivity.class));
        } else if (("push.withdraw.info").equals(pushAction)) {
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
