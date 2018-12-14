package com.shuyun.qapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.against.MainAgainstActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.integral.IntegralMainActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.mine.AddWithdrawInfoActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.SaveUserInfo;

/**
 * h5交互跳转
 */
public class LoginJumpUtil {

    /**
     * @param action
     * @param context
     */
    public static void dialogSkip(String action, Activity context, String content, String h5Url, Long isLogin) {
        if (AppConst.INVITE.equals(action) || AppConst.H5.equals(action)) {//邀请跳转、h5页面跳转
            if (isLogin == 1 && !AppConst.isLogin()) {
                Intent intent = new Intent(context, LoginActivity.class);
                if (AppConst.INVITE.equals(action)) {
                    context.startActivityForResult(intent, AppConst.INVITE_CODE);
                } else {
                    context.startActivityForResult(intent, AppConst.H5_CODE);
                }
            } else {
                Intent intent = new Intent(context, WebH5Activity.class);
                intent.putExtra("url", h5Url);
                intent.putExtra("name", "");//名称 标题
                context.startActivity(intent);
            }
        } else if (AppConst.GROUP.equals(action)) {//题组
            if (isLogin == 1 && !AppConst.isLogin()) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivityForResult(intent, AppConst.INVITE_CODE);
            } else {
                Intent intent = new Intent(context, WebAnswerActivity.class);
                intent.putExtra("groupId", Long.parseLong(content));
                intent.putExtra("h5Url", h5Url);
                context.startActivity(intent);
            }
        } else if (AppConst.INTEGRAL.equals(action)) {//积分兑换
            if (isLogin == 1 && !AppConst.isLogin()) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivityForResult(intent, AppConst.INTEGRAL_CODE);
            } else {
                Intent intent = new Intent(context, IntegralExchangeActivity.class);
                context.startActivity(intent);
            }
        } else if (AppConst.TREASURE.equals(action)) {//积分夺宝
            if (isLogin == 1 && !AppConst.isLogin()) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivityForResult(intent, AppConst.INTEGRAL_CODE);
            } else {
                SaveUserInfo.getInstance(context).setUserInfo("h5_rule", h5Url);
                Intent intent = new Intent(context, IntegralMainActivity.class);
                context.startActivity(intent);
            }
        } else if (AppConst.REAL.equals(action)) {//实名认证
            if (isLogin == 1 && !AppConst.isLogin()) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivityForResult(intent, AppConst.REAL_CODE);
            } else {
                Intent intent = new Intent(context, RealNameAuthActivity.class);
                context.startActivity(intent);
            }
        } else if (AppConst.AGAINST.equals(action)) { //答题对战
            if (isLogin == 1 && !AppConst.isLogin()) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivityForResult(intent, AppConst.REAL_CODE);
            } else {
                Intent intent = new Intent(context, MainAgainstActivity.class);
                context.startActivity(intent);
            }
        } else if (AppConst.OPEN_BOX.equals(action)) { //积分开宝箱
            if (isLogin == 1 && !AppConst.isLogin()) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivityForResult(intent, AppConst.OPEN_BOX_CODE);
            } else {
                Intent intent = new Intent(context, WebPrizeBoxActivity.class);
                intent.putExtra("main_box", "score_box");
                intent.putExtra("h5Url", h5Url);
                context.startActivity(intent);
            }
        } else if (AppConst.WITHDRAW_INFO.equals(action)) { //提现信息
            if (isLogin == 1 && !AppConst.isLogin()) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivityForResult(intent, AppConst.WITHDRAW_INFO_CODE);
            } else {
                SaveUserInfo.getInstance(context).setUserInfo("h5_rule", h5Url);
                Intent intent = new Intent(context, AddWithdrawInfoActivity.class);
                context.startActivity(intent);
            }
        } else if (AppConst.H5_EXTERNAL.equals(action)) { //外部链接
            Uri uri = Uri.parse(h5Url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            context.startActivity(intent);
        } else if (AppConst.TASK.equals(action)) {
            //每日任务
        } else if (AppConst.DEFAULT.equals(action)) {
            //默认不跳转
        }
    }
}
