package com.shuyun.qapp.net;


/**
 * 接口地址方法 这个和ApiService中的接口名称对应 增删改接口时 这边也需要实时同步
 */
public class ApiServiceBean {

    /**
     * 1、首页banner/app-center
     */
    public static String getHomeBanner() {
        return "getHomeBanner";
    }

    /**
     * 2 首页题组/activity-center
     */
    public static String getHomeGroups() {
        return "getHomeGroups";
    }

    /**
     * 3、登录/authorize-service
     * 加上了数美deviceId
     */
    public static String login() {
        return "login";
    }

    /**
     * 8、申请答题
     * /activity-center
     */
    public static String applyAnswer() {
        return "applyAnswer";
    }

    /**
     * 9、完成答题post请求,提交数据
     * 加上了数美deviceId
     */
    public static String completeAnswer() {
        return "completeAnswer";
    }

    /**
     * 10、查看答题结果/activity-center/
     */
    public static String lookAnswerResult() {
        return "lookAnswerResult";
    }

    /**
     * 11、用户中心/user-center
     */
    public static String getMineHomeData() {
        return "getMineHomeData";
    }

    /**
     * 12、实名认证/user-center
     */
    public static String realNameAuth() {
        return "realNameAuth";
    }

    /**
     * 13、获取我的奖品/user-center
     */
    public static String getMyPrize() {
        return "getMyTicket";
    }

    /**
     * 14、申请提现
     * /user-center
     */
    public static String applyWithdrawal() {
        return "applyWithdrawal";
    }

    /**
     * 15、查询现金流水记录
     * /user-center
     */
    public static String queryCashFlow() {
        return "queryCashFlow";
    }

    /**
     * 17、查询积分流水记录
     * /user-center
     *
     * @return
     */
    public static String queryIntegralCurrent() {
        return "queryIntegralCurrent";
    }

    /**
     * 18、获取验证码
     *
     * @return
     */
    public static String getCode() {
        return "getCode";
    }


    /**
     * 20、用户变更头像
     */
    public static String changeHeader() {
        return "changeHeader";
    }

    /**
     * 22、打开宝箱
     */
    public static String openTreasureBox() {
        return "openTreasureBox";
    }


    /**
     * 23、打开宝箱2
     */
    public static String openTreasureBox2() {
        return "openTreasureBox2";
    }

    /**
     * 24、获取宝箱数量
     */
    public static String getTreasureNum() {
        return "getTreasureNum";
    }


    /**
     * 25、获取宝箱数量
     */
    public static String getTreasureNumV2() {
        return "getTreasureNumV2";
    }

    /**
     * 26、答题分享
     */
    public static String answerShared() {
        return "answerShared";
    }

    /**
     * 27、邀请分享
     */
    public static String inviteShared() {
        return "inviteShared";
    }

    /**
     * 28、分享确认
     */
    public static String sharedConfirm() {
        return "sharedConfirm";
    }

    /**
     * 29、获取题组树
     */
    public static String getGroupTree() {
        return "getGroupTree";
    }

    /**
     * 30、用户答题记录TODO /activity-center
     */
    public static String getAnswerRecord() {
        return "getAnswerRecord";
    }

    /**
     * 31、答题题目反馈TODO
     */
    public static String getAnswerFeedBack() {
        return "getAnswerFeedBack";
    }

    /**
     * 32、领取答题机会TODO
     * /user-center
     */
    public static String getAnswerOppty() {
        return "getAnswerOppty";
    }

    /**
     * 33、答题机会领取剩余时长TODO
     * /user-center
     */
    public static String getAnswerOpptyRemainder() {
        return "getAnswerOpptyRemainder";
    }


    /**
     * 34、反馈建议TODO
     * /user-center
     */
    public static String getFeedBack() {
        return "getFeedBack";
    }

    /**
     * 35、获取系统消息/app-center
     *
     * @return
     */
    public static String getSystemInfo() {
        return "getSystemInfo";
    }

    /**
     * 36、获取广告列表 LoginInput /app-center
     *
     * @return
     */
    public static String getAd() {
        return "getAd";
    }

    /**
     * 37、绑定微信 /user-center
     */
    public static String bindWx() {
        return "bindWx";
    }

    /**
     * 38、使用道具
     * 增次卡
     *
     * @return
     */
    public static String addAnswerNum() {
        return "addAnswerNum";
    }


    /**
     * 39、题组分享
     */
    public static String groupShared() {
        return "groupShared";
    }

    /**
     * 40、获取推送消息
     */
    public static String getMsg() {
        return "getMsg";
    }


    /**
     * 41、忘记密码（修改密码）
     * 加上了数美deviceId
     */
    public static String modifyPassWord() {
        return "modifyPassWord";
    }

    /**
     * 42、更改手机号----验证老手机号
     */
    public static String verifyoldphone() {
        return "verifyoldphone";
    }

    /**
     * 43、更改手机号----验证新手机号
     */
    public static String verifynewphone() {
        return "verifynewphone";
    }

    /**
     * 44、新版本查询
     */
    public static String updateVersion() {
        return "updateVersion";
    }

    /**
     * 45、变更绑定微信
     */
    public static String changeWXbind() {
        return "changeWXbind";
    }

    /**
     * 46、账户注销前置条件判断
     */
    public static String verifyCondition() {
        return "verifyCondition";
    }

    /**
     * 47、账户注销提交审核
     */
    public static String commitCondition() {
        return "commitCondition";
    }

    /**
     * 48、账户注销撤回审核
     */
    public static String removeCondition() {
        return "removeCondition";
    }

    /**
     * 49、答题对战首页
     */
    public static String mainAgainst() {
        return "mainAgainst";
    }

    /**
     * 50、答题对战题组
     */
    public static String groupAgainst() {
        return "groupAgainst";
    }

    /**
     * 51、用户匹配时长
     */
    public static String getMatchTimeInfo() {
        return "getMatchTimeInfo";
    }

    /**
     * 52、答题对战随机题组
     */
    public static String randomGroup() {
        return "randomGroup";
    }

    /**
     * 53、答题对战机器人模拟对战
     */
    public static String against() {
        return "against";
    }

    /**
     * 54、题对战用户积分消耗
     */
    public static String useBpCons() {
        return "useBpCons";
    }

    /**
     * 55、答题对战结束
     */
    public static String completeAnswAgainst() {
        return "completeAnswAgainst";
    }


    /**
     * 56、答题对战分享
     */
    public static String battleAnswerShared() {
        return "battleAnswerShared";
    }

    public static String battleAnswerShared1() {
        return "battleAnswerShared1";
    }

    /**
     * 57、积分兑换首页
     */
    public static String getExchangeMain() {
        return "getExchangeMain";
    }

    /**
     * 58、积分夺宝全部奖品
     */
    public static String getAllPrize() {
        return "getAllPrize";
    }

    /**
     * 59、积分夺宝开奖历史
     */
    public static String getPrizeHistory() {
        return "getPrizeHistory";
    }

    /**
     * 60、积分夺宝我的奖券
     */
    public static String getMyTicket() {
        return "getMyTicket";
    }

    /**
     * 61、宝贝详情
     */
    public static String getDetailInfo() {
        return "getDetailInfo";
    }

    /**
     * 62、确认兑换
     */
    public static String enterExchange() {
        return "enterExchange";
    }

    /**
     * 63、兑换记录
     */
    public static String ExchangeHistory() {
        return "ExchangeHistory";
    }

    /**
     * 64、宝贝详情分享
     * /share-service
     */
    public static String prizeShare() {
        return "prizeShare";
    }

    /**
     * 65、首页邀请有奖
     */
    public static String inviteShare() {
        return "inviteShare";
    }

    /**
     * 66、用户设置密码
     */
    public static String setPwd() {
        return "setPwd";
    }

    /**
     * 67、忘记密码验证
     */
    public static String verifyPassWord() {
        return "verifyPassWord";
    }

    /**
     * 68、首页弹框配置
     */
    public static String configDialog() {
        return "configDialog";
    }

    /**
     * 69、首页活动配置
     */
    public static String configMainActivity() {
        return "configMainActivity";
    }

    /**
     * 70、活动专区数据
     */
    public static String ActivityList() {
        return "ActivityList";
    }

    /**
     * 71、获取最新活动时间
     */
    public static String getActivityShow() {
        return "getActivityShow";
    }

    /**
     * 72、点击活动专区
     */
    public static String clickActivity() {
        return "clickActivity";
    }

    /**
     * 73、分享
     */
    public static String SharedPublic() {
        return "SharedPublic";
    }

    public static String SharedPublic1() {
        return "SharedPublic1";
    }

    /**
     * 74、提现信息完善
     */
    public static String submitWithdrawInfo() {
        return "submitWithdrawInfo";
    }

    /**
     * 75、实名认证结果查询
     */
    public static String queryRealResult() {
        return "queryRealResult";
    }

    /**
     * 76、首页公告
     */
    public static String homeNotice() {
        return "homeNotice";
    }

    /**
     * 77、宝箱记录
     */
    public static String boxRecord() {
        return "boxRecord";
    }

    /**
     * 78、我的道具列表
     */
    public static String MyProps() {
        return "MyProps";
    }

    /**
     * 79、使用道具
     */
    public static String useProps() {
        return "useProps";
    }

    /**
     * 80、心跳
     */
    public static String heartBeat() {
        return "heartBeat";
    }
}