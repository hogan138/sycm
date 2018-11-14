package com.shuyun.qapp.utils;

import android.content.Context;
import android.content.Intent;

import com.shuyun.qapp.ui.login.LoginActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sunxiao on 2018/6/4.
 * 错误码提示工具类
 */

public class ErrorCodeTools {
    static Map<String, String> errCode = new HashMap<String, String>() {
        {
//            put("00001", "应用未注册");
//            put("99999", "接口返回未知错误");
//            put("D0001", "不存在对应的记录");
//            put("E0001", "答题试卷不存在");
//            put("E0002", "答题机会不足,请先领取答题机会");
//            put("E0003", "完成答题时，状态不正确");
//            put("E0004", "完成答题时，状态不正确");
//            put("E0005", "今天您已经答对此题组,明天再来答哦");
//
//            put("L0001", "抽奖时，积分不足");
//            put("K0001", "题组不存在");
//            put("K0002", "题组不正确");
//            put("P0001", "参数不正确");
//            put("P0002", "参数不能为空");
//            put("P0003", "参数不在有效范围");
//            put("P0004", "缺少必要的参数");
//
//            put("A0001", "答题时，活动状态不正确");
//            put("A0002", "系统没有可用的活动");
//            put("A0003", "答题时，活动不存在");
//            put("A0004", "没有可用的抽奖活动");
//            put("A0005", "活动没有扩展中奖规则");
//            put("A0006", "活动的扩展中奖规则没有配置扩展活动");
//            put("A0007", "请求错误,稍后再试");//活动的模型不匹配
//
//            put("A1001", "答题时，活动没有配置规则");
//            put("A1002", "活动的规则不正确");//活动的规则对象不存在
//            put("A1003", "活动的扩展中奖规则没有配置扩展活动");//已经中奖了
//
//            put("U0001", "用户未注册");
//            put("U0002", "手机号码已经被其它用户绑定");
//            put("U0003", "头像不存在");
//            put("U0004", "答题机会达到上限了");
//            put("U0005", "领取答题机会的时长不足");
//
//            put("U1001", "用户现金不足");
//            put("U1002", "奖品不属于该用户");
//            put("U1003", "用户同时只能申请一次提现");
//            put("U1004", "红包不存在");
//            put("U1005", "红包状态不正确");
//
//            put("U2001", "奖品不是一个红包");
//            put("U2002", "得奖记录不存在");
//            put("U2003", "奖品的类型不匹配");
//            put("U2004", "得奖的奖品状态不正确");
//            put("U2005", "得奖的宝箱被重复打开了");
//            put("U2006", "领奖时，需要注册为商户用户");
//            put("U2007", "奖品的使用方式不支持");
//            put("U2008", "奖品已经过期");
//
//            put("S0001", "短信验证码不存在");
//            put("S0002", "短信验证码状态不正确");
//            put("S0003", "短信验证码不匹配");
//            put("S0004", "短信验证码已经失效");
//
//            put("S1001", "发送短信验证码失败");
//            put("O0001", "参数不对,请重试");//缺少运营参数配置
//
//            put("TWX01", "微信授权异常");
//            put("TWX02", "微信已经被绑定，不能重复绑定");
//
//            put("TAU01", "没有找到登录记录");
//            put("TAU02", "请求错误,重新登录");//头部缺少认证属性
//            put("TAU03", "请求错误,重新登录");//头部的认证方式不支持
//            put("TAU04", "请求错误,重新登录");//头部的认证属性中缺少jwt的授权token
//            put("TAU05", "请求错误,重新登录");//头部的认证属性中发现未注册的应用
//            put("TAU06", "请求错误,重新登录");//头部的认证属性中签名不匹配
//            put("TAU07", "登录状态不对,请重新登录");//头部的认证属性中，jwt的授权token已经失效
//            put("TAU08", "请求错误,重新登录");//头部的认证属性中缺少必要的属性
//            put("TAU09", "请求错误,重新登录");//头部的认证属性中缺少登录属性
//            put("TAU10", "请求错误,重新登录");//头部的认证属性格式不正确
//            put("TAU11", "验证码或密码不正确");//签名不匹配
        }
    };

    /**
     * 请求接口,错误码提示
     *
     * @param mContext 上下文
     * @param err
     */
    public static boolean errorCodePrompt(Context mContext, String err, String message) {
        if (err.equals("E0005")) {
            ToastUtil.showToast(mContext, "今天您已经答对此题组，明天再来答哦");
        } else if (err.equals("U0001")) {//用户未注册,跳转到登录页
            ToastUtil.showToast(mContext, "用户未注册");
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            MyActivityManager.getInstance().finishAllActivity();
            return false;
        } else if (err.equals("U0002")) {//手机号已经被其它用户绑定,跳到登录页
            ToastUtil.showToast(mContext, "手机号码已经被其它用户绑定");
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            return false;
        } else if (err.equals("TAU01") || err.equals("TAU02") || err.equals("TAU03") || err.equals("TAU04") || err.equals("TAU05")
                || err.equals("TAU06") || err.equals("TAU07") || err.equals("TAU08") || err.equals("TAU09") || err.equals("TAU10")
                ) {
            //头部的认证属性中，jwt的授权token已经失效,跳登录页
//            ToastUtil.showToast(mContext, "登录状态不对,请重新登录");
            Intent intent = new Intent(mContext, LoginActivity.class);
            mContext.startActivity(intent);
            MyActivityManager.getInstance().finishAllActivity();
            return false;
        } else {
            if (errCode.containsKey(err)) {
                String msg = errCode.get(err);
                ToastUtil.showToast(mContext, msg);
            } else {
                ToastUtil.showToast(mContext, message);
            }
        }
        return true;
    }
}
