package com.shuyun.qapp.net;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.bean.BannerBean;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.bean.HomeNoticeBean;
import com.shuyun.qapp.bean.MainConfigBean;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.view.LoginJumpUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 登录回调信息保存处理
 */
public class LoginDataManager {
    public static final String KEY = "action";
    public static final String VALUE = "data";

    /**
     * 全局对象 适用于登录回调callBack为空时
     */
    public Object overallData = null;

    /**
     * banner登录
     */
    public static final String BANNER_LOGIN = "banner_login";
    /**
     * 首页获取专区登录
     */
    public static final String HOME_ACTIVITY_LOGIN = "home_activity_login";
    /**
     * 活动专区登录
     */
    public static final String ACTIVITY_LOGIN = "activity_login";
    /**
     * 首页活动公告登录
     */
    public static final String HOME_NOTICE_LOGIN = "home_notice_login";
    /**
     * 首页弹框登录
     */
    public static final String HOME_DIALOG_LOGIN = "home_dialog_login";
    /**
     * 个人中心登录
     */
    public static final String MINE_LOGIN = "mine_login";
    /**
     * 广告页登录
     */
    public static final String WELCOME_LOGIN = "welcome_login";
    /**
     * 答题开宝箱登录
     */
    public static final String ANSWER_LOGIN = "answer_login";
    /**
     * 练习场登录
     */
    public static final String PRACTICE_LOGIN = "practice_login";

    private static LoginDataManager manager = null;
    /**
     * key uuid
     * value {
     * key action
     * value 数据
     * }
     */
    private Map<String, Map<String, Object>> map = new HashMap<>();
    private String lastKey = null;
    private Handler mHandler = new Handler();

    protected LoginDataManager() {

    }

    public static LoginDataManager instance() {
        if (manager == null)
            manager = new LoginDataManager();
        return manager;
    }

    public void setOverallData(Object data) {
        this.overallData = data;
    }

    public Object getOverallData() {
        return overallData;
    }

    /**
     * 跳转下一级页面的数据
     *
     * @param action
     * @param data
     */
    public void addData(String action, Object data) {
        this.lastKey = UUID.randomUUID().toString();
        Map<String, Object> t = new HashMap<>();
        t.put(KEY, action);
        t.put(VALUE, data);
        map.put(this.lastKey, t);
    }

    public Map<String, Object> getLast() {
        if (lastKey == null)
            return null;
        Map<String, Object> data = map.get(lastKey);
        map.clear();
        lastKey = null;
        return data;
    }

    public void handler(final Activity mContext, final Object[] args) {
        Map<String, Object> map = LoginDataManager.instance().getLast();
        if (map == null)
            return;
        String action = (String) map.get(LoginDataManager.KEY);
//        if (mContext instanceof BaseActivity) {
//            ((BaseActivity) mContext).clear();
//        }

        if (LoginDataManager.ACTIVITY_LOGIN.equals(action)) { //活动专区
            ActivityTabBean.ResultBean resultBean = (ActivityTabBean.ResultBean) map.get(LoginDataManager.VALUE);
            LoginJumpUtil.dialogSkip(resultBean.getBtnAction(),
                    mContext,
                    resultBean.getContent(),
                    resultBean.getH5Url(),
                    resultBean.getIsLogin());
        } else if (LoginDataManager.BANNER_LOGIN.equals(action)) {//banner
            BannerBean bannerBean = (BannerBean) map.get(LoginDataManager.VALUE);
            LoginJumpUtil.dialogSkip(bannerBean.getAction(),
                    mContext,
                    bannerBean.getContent(),
                    bannerBean.getH5Url(),
                    bannerBean.getIsLogin());
        } else if (LoginDataManager.HOME_DIALOG_LOGIN.equals(action)) { //首页弹框
            ConfigDialogBean configDialogBean = (ConfigDialogBean) map.get(LoginDataManager.VALUE);
            LoginJumpUtil.dialogSkip(configDialogBean.getBtnAction(),
                    mContext,
                    configDialogBean.getContent(),
                    configDialogBean.getH5Url(),
                    configDialogBean.getIsLogin());
        } else if (LoginDataManager.HOME_ACTIVITY_LOGIN.equals(action)) { //首页活动专区
            MainConfigBean.DatasBean datasBean = (MainConfigBean.DatasBean) map.get(LoginDataManager.VALUE);
            LoginJumpUtil.dialogSkip(datasBean.getAction(),
                    mContext,
                    datasBean.getContent(),
                    datasBean.getH5Url(),
                    datasBean.getIsLogin());
        } else if (LoginDataManager.HOME_NOTICE_LOGIN.equals(action)) { //首页公告
            HomeNoticeBean homeNoticeBean = (HomeNoticeBean) map.get(LoginDataManager.VALUE);
            LoginJumpUtil.dialogSkip(homeNoticeBean.getAction(),
                    mContext,
                    homeNoticeBean.getContent(),
                    homeNoticeBean.getH5Url(),
                    homeNoticeBean.getIsLogin());
        } else if (LoginDataManager.MINE_LOGIN.equals(action)) { //个人中心
            if (mContext instanceof HomePageActivity) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((HomePageActivity) mContext).radioGroupChange(4);
                    }
                }, 10);
            }
        } else if (LoginDataManager.WELCOME_LOGIN.equals(action)) {
            Bundle bundleRedirect = (Bundle) map.get(LoginDataManager.VALUE);
            final Long model = bundleRedirect.getLong("model", 0);
            final String content = bundleRedirect.getString("content");
            if (model == 3) {//题组跳转
                if (!EncodeAndStringTool.isStringEmpty(content)) {
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", Long.valueOf(content));
                    intent.putExtra("from", "splash");
                    intent.putExtra("h5Url", bundleRedirect.getString("examUrl"));
                    mContext.startActivity(intent);
                }
            } else if (model == 2) {//内部链接
                if (!EncodeAndStringTool.isStringEmpty(content)) {
                    Intent intent = new Intent(mContext, WebH5Activity.class);
                    intent.putExtra("url", content);
                    intent.putExtra("name", "全民共进");
                    intent.putExtra("from", "splash");
                    mContext.startActivity(intent);
                }
            }
        } else if (LoginDataManager.ANSWER_LOGIN.equals(action)) {//答题登录
            if (mContext instanceof WebAnswerActivity) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((WebAnswerActivity) mContext).sendLoginCallBack("exam", (String) args[0]);
                    }
                }, 10);
            }
        } else if (LoginDataManager.PRACTICE_LOGIN.equals(action)) {//练习场登录
            if (mContext instanceof WebAnswerActivity) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((WebAnswerActivity) mContext).sendLoginCallBack("practice", null);
                    }
                }, 10);
            }
        }
    }
}
