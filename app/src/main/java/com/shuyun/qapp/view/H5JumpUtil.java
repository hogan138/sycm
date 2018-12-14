package com.shuyun.qapp.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.against.MainAgainstActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.integral.IntegralMainActivity;
import com.shuyun.qapp.ui.mine.AddWithdrawInfoActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.SaveUserInfo;

/**
 * h5交互跳转
 */
public class H5JumpUtil {

    public static void dialogSkip(String action, String content, String h5Url, Context context, Long isLogin) {
        if (AppConst.GROUP.equals(action)) {
            //题组
            Intent intent = new Intent(context, WebAnswerActivity.class);
            intent.putExtra("groupId", Long.parseLong(content));
            intent.putExtra("isLogin", isLogin);
            intent.putExtra("h5Url", h5Url);
            context.startActivity(intent);
        } else if (AppConst.REAL.equals(action)) {
            //实名认证
            Intent intent = new Intent(context, RealNameAuthActivity.class);
            intent.putExtra("isLogin", isLogin);
            context.startActivity(intent);
        } else if (AppConst.H5.equals(action) || AppConst.INVITE.equals(action)) {
            //h5页面、邀请
            Intent intent = new Intent(context, WebH5Activity.class);
            intent.putExtra("url", h5Url);
            intent.putExtra("name", "");//名称 标题
            intent.putExtra("isLogin", isLogin);
            context.startActivity(intent);
        } else if (AppConst.INTEGRAL.equals(action)) {
            //积分兑换
            Intent intent = new Intent(context, IntegralExchangeActivity.class);
            intent.putExtra("isLogin", isLogin);
            context.startActivity(intent);
        } else if (AppConst.AGAINST.equals(action)) {
            //答题对战
            Intent intent = new Intent(context, MainAgainstActivity.class);
            intent.putExtra("isLogin", isLogin);
            context.startActivity(intent);
        } else if (AppConst.OPEN_BOX.equals(action)) {
            //积分开宝箱
            Intent intent = new Intent(context, WebPrizeBoxActivity.class);
            intent.putExtra("main_box", "score_box");
            intent.putExtra("h5Url", h5Url);
            intent.putExtra("isLogin", isLogin);
            context.startActivity(intent);
        } else if (AppConst.TREASURE.equals(action)) {
            //积分夺宝
            //保存规则地址
            SaveUserInfo.getInstance(context).setUserInfo("h5_rule", h5Url);
            Intent intent = new Intent(context, IntegralMainActivity.class);
            intent.putExtra("isLogin", isLogin);
            context.startActivity(intent);
        } else if (AppConst.TASK.equals(action)) {
            //每日任务
        } else if (AppConst.WITHDRAW_INFO.equals(action)) {
            //提现信息
            SaveUserInfo.getInstance(context).setUserInfo("h5_rule", h5Url);
            Intent intent = new Intent(context, AddWithdrawInfoActivity.class);
            intent.putExtra("isLogin", isLogin);
            context.startActivity(intent);
        } else if (AppConst.H5_EXTERNAL.equals(action)) {
            //外部链接
            Uri uri = Uri.parse(h5Url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } else if (AppConst.DEFAULT.equals(action)) {
            //默认不跳转
        }
    }
}
