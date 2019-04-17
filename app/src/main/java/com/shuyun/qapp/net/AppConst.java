package com.shuyun.qapp.net;

import android.content.Context;

import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SaveUserInfo1;
import com.shuyun.qapp.utils.SharedPrefrenceTool;

import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 服务器地址
 */

public class AppConst {

    //            public static final String BASE_URL = "https://api-syksc.25876.com";///线上服务器
    //    public static final String BASE_URL = "http://192.168.3.250:8080";//:8002/公司内部服务器
//    public static final String BASE_URL = "http://139.224.199.106:8080";//开发测试服务器
    public static final String BASE_URL = "http://192.168.3.157";//测试环境
//    public static final String BASE_URL = "http://139.224.221.200:8080";//200
//    public static final String BASE_URL = "http://139.224.221.200:18080";

    //RSA私钥
    public static final String private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIB  AQCW05nUBJkbpBE8JGDPRWEQioZkRGNjCPnHETbsSgXVdEgS5oxxggAjDyMjrjnyJmMLbLN6es4yQwAj8KENSImqt+KmNJ+d2K0Sgd+SL0/SjENBI/B1PF30Q3LfiXfuSGooK2uOgig5pAB1/6QdBTfHuFOZY8LpomTstTgmjBMy267UaiAVuhBE3DauWo1N72OZGMVDhRDpwuyh/go1mherOMsY22l+GQNk0DpFfZbdHdGlhY70smZiBVCya8Yw54IoDS4l1+XSPfm369OsnUsK8UxwPavbwo5RnJDuj/EQzEwiIthKCh5y7bGuThWThbPkPJC0Vo0WwmOqIAzYQJRRAgMBAAECggEAHt0wNl22gxyA1mDPWrrk0QC33Z7NA8AbdOwF5DaFmReRhCSfir7CjmoTk8KcYvwN+pGE7MRim5BwX+pm2pQEb+XvQHm2TBPB3u6qtHxwBjLjtFnJZAQ1ab2/rRSxcRZqesvf16q01o7D9WGZ0MJ3lqwWl2X9xdeQdYvj8KdralWOKwPntzOv3SBoFemS+aGUaSDjtqSwN7HkGZhhPfECN4kQ/wixZcU34S3u/uDic+YhPbYGDum5m1BBX7Uq115nVLF3pWNNCsuHmdkF/4N6a5lU5CfxYWDlMgjTg3JMDLSdFlCYDzrasgthTRWQvyn/z9jjmWH7DxUG88OviJF/bQKBgQDu93ShuAHZKxUUaa7vtXrdOjLuRd2UFPlFy+KB3izJLWUIiJKEIRHCA8X+EoR5rHU83zCsQoW80twrhB8dWyFwhwas5R+hBOyqDf2hcJ+ZJqJ89aVonY+T96Rq+bD8nicdv/BWGuH+fxAEIIk6y1fZUttTKeZvxr6IOlU1ODr8fwKBgQChk88rHiVryhOx7K1uYo6PrCngFgXiah4MF9v9MnJ6Bc95VkeguvPeKrxjPUOmRVvXarND1xPHmrFoOLFJBKHOTQnbqQDChOI+tKpga0PPMRGVUze9SmkkGZn5BConwiz2vbcSul+AvBsoCa1NYJHmD/HA1BJNvVqnQHpleBtHLwKBgH6zfYdxRKmciiS0lChMlMRPY0mqiX1GUZSMMaCh6CUSiIspvmWIEx5HWecIcm0A33hS7j+nVbl0I4B/IPAzrVs4fHWXVlnNfp37pQq/6B8PuP+ATx9UyxetKeP+V7TZNew0JH3C6yhAvoExrxEZV47Gu7swcBm7yrH4G82H7t6JAoGAAtcWTSu6fKqQrg/6/HW/C8d69PVuJQy4en3w4AKDAKIZg4iAudKPdthJd3UcO0/8zUyS2h5tupR9idd1RlNOhNHV2oad/M1ZLMEAPbGk/39dT8KmuC0WrAvHKRlppElFgRDUpv+friOgVpPU+Ac6iVC+byVLLRKnBUmc3Su/TwECgYAjBgFdWem++ty/y3cG0vrtjPGSY03DsG+eeL87NCtjRnRzYM58KoFSpkhiwvkIcEWRJPzCVAL2W9qHOMBTH0E55uQUbWp4uIOcR9rc/4BWV35MzPmDC2encgImRbJP1GGp5cFjOxi4UGj9gZtbVYNauNNzlMJGp3nr2gyNcBOo7g==";

    /**
     * 微信开放平台AppId
     */
    public static final String WEIXIN_APP_ID = "wxb9167b3dcd6daa1e";

    /**
     * 后台配的appKey
     */
    public static final String APP_KEY = "G9cSpkRWAmrvNmT84rHIhQUeolfk88htOrIxSrQFRxA8lSY9ZNGnztHPNtLeIigXCAFsyXtk7sGkmVlKLp8kLffYvsNy6vE9HMDNLEee2peDbnvvMgk1hxsmGdVIUW";
    public static final long WX_MODE = 3;
    public static final long DEV_ID = 1;
    public static final long APP_ID = 2;
    public static final long V = 1;
    public static String TOKEN;
    public static String SALT;
    public static String RANDOM;
    public static String jwtToken;

    //================= refresh state ====================
    public static final int STATE_NORMAL = 0;
    public static final int STATE_REFRESH = 1;
    public static final int STATE_MORE = 2;

    /**
     * 获取到登录之后的token等参数
     *
     * @param mContext
     */
    public static void loadToken(Context mContext) {
        TOKEN = (String) SharedPrefrenceTool.get(mContext, "token", "");
        SALT = (String) SharedPrefrenceTool.get(mContext, "salt", "");
        RANDOM = (String) SharedPrefrenceTool.get(mContext, "random", "");
        if (!EncodeAndStringTool.isStringEmpty(TOKEN)) {
            String[] tokenStr = TOKEN.split(" ");
            jwtToken = tokenStr[2];
        }
    }

    public static boolean isLogin() {
        return !EncodeAndStringTool.isStringEmpty(TOKEN);
    }

    //登录方式（强制或游客模式）
    public static boolean isNormalLogin(Context mContext) {
        String mode = SaveUserInfo1.getInstance(mContext).getUserInfo("normal_login");
        if (mode == null || "".equals(mode) || "0".equals(mode))
            return true;
        return false;
    }

    public static String sycm() {
        long curTime = System.currentTimeMillis();
        String codeStr = jwtToken + curTime + AppConst.V + SALT + RANDOM + AppConst.APP_KEY;
        return "" + AppConst.V + "_" + String.valueOf(curTime) + "_" + getCode(EncodeAndStringTool.encryptMD5ToString(codeStr));
    }

    /**
     * 微信朋友圈分享
     */
    public static final int SHARE_MEDIA_WEIXIN_CIRCLE = 1;
    /**
     * 微信分享
     */
    public static final int SHARE_MEDIA_WEIXIN = 2;
    /**
     * 二维码分享
     */
    public static final int SHARE_MEDIA_QR = 3;

    /**
     * 用户协议
     */
    public static final String USER_AGREEMENT = "https://api-syksc.25876.com/web/h5/agreement.html";
    /**
     * 关于我们
     */
    public static final String ABOUT_US = "https://api-syksc.25876.com/web/h5/about.html";
    /**
     * 活动规则
     */
    public static final String LOOK_RULES = "https://api-syksc.25876.com/web/h5/static.html#/text";

    /**
     * H5答题
     */
    public static final String ANSWER = BASE_URL + "/web/h5/index.html?v=3.0.8";

    /**
     * 开宝箱
     */
    public static final String BOX = BASE_URL + "/web/h5/index.html?prize=1";

    /**
     * 账户记录
     */
    public static final int ACCOUNT_CASH_TYPE = 0;
    public static final int ACCOUNT_INTEGRAL_TYPE = 1;


    /**
     * 动作action
     */
    public static final String DEFAULT = "action.default";//默认不跳转
    public static final String GROUP = "action.group";//题组
    public static final String REAL = "action.real";//实名认证
    public static final String H5 = "action.h5";//H5
    public static final String INVITE = "action.invite";//邀请
    public static final String INTEGRAL = "action.integral";//积分兑换
    public static final String OPEN_BOX = "action.integral.open.box";//积分开宝箱
    public static final String TREASURE = "action.integral.treasure";//积分夺宝
    public static final String AGAINST = "action.answer.against";//答题对战
    public static final String TASK = "action.day.task";//每日任务
    public static final String WITHDRAW_INFO = "action.withdraw.info";//提现信息
    public static final String H5_EXTERNAL = "action.h5.external";//h5跳转外部链接
    public static final String ACTION_SIGN = "action.sign";//签到
    public static final String ACTION_INTEGRAL_GOODS = "action.integral.goods";//积分兑换
    public static final String ACTION_PRIZE = "action.prize";//我的奖品

    //邀请登录跳转
    public static final int INVITE_CODE = 0x2001;
    //题组跳转
    public static final int GROUP_CODE = 0x2002;
    //积分兑换
    public static final int INTEGRAL_CODE = 0x2003;
    //积分夺宝
    public static final int TREASURE_CODE = 0x2004;
    //实名认证
    public static final int REAL_CODE = 0x2005;
    //h5页面跳转
    public static final int H5_CODE = 0x2006;
    //答题对战跳转
    public static final int AGAINST_CODE = 0x2007;
    //积分开宝箱跳转
    public static final int OPEN_BOX_CODE = 0x2008;
    //提现信息跳转
    public static final int WITHDRAW_INFO_CODE = 0x2009;


    public static final String APP_ACTION_LOGOUT = "APP_ACTION_LOGOUT";//退出登录
    public static final String APP_ACTION_PARAM = "APP_ACTION";//动作参数

    public static final String AGAINST_MAIN_INFO = "against_main_info";//答题对战首页
    public static final String AGAINST_SHARE = "against_share";//对战分享
    public static final String AGAINST_SHARE_CONFIM = "against_share_confim";//对战分享确认
    public static final String GET_GROUP_LIST = "get_group_list"; //对战获取题组列表
    public static final String MATCHING_USER_TIME = "matching_user_time"; //用户匹配时长
    public static final String AGAINST_REDUCE_SCORE = "against_reduce_score"; //消耗积分
    public static final String AGAINST_APPLY_ANSWER = "against_apply_answer"; //申请答题
    public static final String AGAINST_RANDOM_ANSWER = "against_random_answer";//随机题目
    public static final String AGAINST_ROBOT = "against_robot";//机器人对战
    public static final String AGAINST_RESULT = "against_result";//对战结果
    public static final String AGAINST_RESULT_SHARE = "against_result_share";//对战结果分享
    public static final String ANSWER_HISTORY = "answer_history"; //答题历史
    public static final String ANSWER_FEEDBACK = "answer_feedback";//答题反馈
    public static final String WELCOME_AD = "welcome_ad";//广告页
    public static final String TOURISTS = "tourists";//是否是游客模式

    /**
     * push跳转
     */
    public static final String PUSH_INTEGRAL = "push.integral.snatch.notify"; //积分夺宝中奖通知
    public static final String PUSH_ANSWER_GET = "push.answer.get.notify"; //每8小时可领取答题次数通知
    public static final String PUSH_PRIZE = "push.prize.notity"; //奖品通知
    public static final String PUSH_PRIZE_EXPIRE = "push.prize.expire.notify"; //奖品快过期通知
    public static final String PUSH_WITHDRAW_SUCCESS = "push.withdraw.success.notify"; //提现成功通知
    public static final String PUSH_WITHDRAW_ERROR = "push.withdraw.error.notify"; //提现失败通知
    public static final String PUSH_DEFAULT = "push.default"; //默认跳转h5
    public static final String PUSH_DELEVER_GOODS = "push.deliver.goods.notity"; //发货通知
    public static final String PUSH_REAL = "push.real"; //实名认证
    public static final String PUSH_WITHDRAW_INFO = "push.withdraw.info"; //完善提现信息


    /**
     * 题组树广告位
     */
    public static final long TYPE_GROUP_TREE = 1;

    /**
     * H5题组广告位
     */
    public static final long TYPE_GROUP_H5 = 2;

    /**
     * banner广告位
     */
    public static final long TYPE_BANNER = 3;

    /**
     * 首页题组广告位
     */
    public static final long TYPE_HOME_GROUP = 4;


    //任务action
    public static final String ACTION_RECEIVE = "action.receive";//领取奖励
    public static final String ACTION_FINISH = "action.finish";//已完成
    public static final String ACTION_BIND_WX = "action.bind.wx";//绑定微信
    public static final String ACTION_ONE_EXAM = "action.one.exam";//完成一次答题
    public static final String action_oppty = "action.oppty";//领取一次答题机会
    public static final String ACTION_INVITE_FRIENDS = "action.invite.friends";//邀请好友
    public static final String ACTION_DAY_EXAM = "action.day.exam";//每日答题，跳转到分类页
    public static final String ACTION_HS_GROUP_EXAM = "action.hs.group.exam";//黄山题组答题一次


    //友盟页面统计
    public static final String APP_HOME = "app_home"; //首页
    public static final String APP_GROUP_CLASS = "app_group_class"; //分类
    public static final String APP_FOUND = "app_found"; //发现
    public static final String APP_PERSONAL = "app_personal"; //我的
    public static final String APP_FOUND_HOT_GROUP = "app_found_hot_group"; //发现-热门题组
    public static final String APP_PERSONAL_BP = "app_personal_bp"; //积分中心
    public static final String APP_PERSONAL_CASH = "app_personal_cash"; //现金金额
    public static final String APP_PERSONAL_PRIZE = "app_personal_prize"; //我的奖品
    public static final String APP_PERSONAL_PROP = "app_personal_prop"; //我的道具
    public static final String APP_PERSONAL_TRANSCRIPT = "app_personal_transcript"; //成绩单


}
