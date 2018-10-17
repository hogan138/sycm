package com.shuyun.qapp.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import com.shuyun.qapp.bean.H5JumpBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.MainAgainstActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.integral.IntegralMainActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.SaveUserInfo;

/**
 * h5交互跳转
 */
public class H5JumpUtil {

    public static void dialogSkip(H5JumpBean h5JumpBean, Context context, View view) {
        String action = h5JumpBean.getBtnAction();
        String h5Url = h5JumpBean.getH5Url();
        if (AppConst.GROUP.equals(action)) {
            //题组
            Intent intent = new Intent(context, WebAnswerActivity.class);
            intent.putExtra("groupId", Integer.parseInt(h5JumpBean.getContent()));
            intent.putExtra("h5Url", h5Url);
            context.startActivity(intent);
        } else if (AppConst.REAL.equals(action)) {
            //实名认证
            context.startActivity(new Intent(context, RealNameAuthActivity.class));
        } else if (AppConst.H5.equals(action)) {
            //h5页面
            Intent intent = new Intent(context, WebBannerActivity.class);
            intent.putExtra("url", h5Url);
            intent.putExtra("name", "全民共进");//名称 标题
            context.startActivity(intent);
        } else if (AppConst.INVITE.equals(action)) {
            //邀请
            Intent intent = new Intent();
            intent.setClass(context, WebBannerActivity.class);
            intent.putExtra("url", h5Url);
            intent.putExtra("name", "邀请分享");
            context.startActivity(intent);
        } else if (AppConst.INTEGRAL.equals(action)) {
            if (Integer.parseInt(SaveUserInfo.getInstance(context).getUserInfo("cert")) == 1) {
                //积分兑换
                context.startActivity(new Intent(context, IntegralExchangeActivity.class));
            } else {
                RealNamePopupUtil.showAuthPop(context, view);
            }
        } else if (AppConst.AGAINST.equals(action)) {
            //答题对战
            context.startActivity(new Intent(context, MainAgainstActivity.class));
        } else if (AppConst.OPEN_BOX.equals(action)) {
            if (Integer.parseInt(SaveUserInfo.getInstance(context).getUserInfo("cert")) == 1) {
                //积分开宝箱
                Intent intent = new Intent(context, WebPrizeBoxActivity.class);
                intent.putExtra("main_box", "score_box");
                intent.putExtra("h5Url", h5Url);
                context.startActivity(intent);
            } else {
                RealNamePopupUtil.showAuthPop(context, view);
            }
        } else if (AppConst.TREASURE.equals(action)) {
            if (Integer.parseInt(SaveUserInfo.getInstance(context).getUserInfo("cert")) == 1) {
                //积分夺宝
                //保存规则地址
                SaveUserInfo.getInstance(context).setUserInfo("h5_rule", h5Url);
                context.startActivity(new Intent(context, IntegralMainActivity.class));
            } else {
                RealNamePopupUtil.showAuthPop(context, view);
            }
        } else if (AppConst.TASK.equals(action)) {
            //每日任务
        } else if (AppConst.WITHDRAW_INFO.equals(action)) {
            //提现信息
            Intent i = new Intent(context, WebBannerActivity.class);
            i.putExtra("url", h5Url);
            i.putExtra("name", "提现");//名称 标题
            context.startActivity(i);
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
