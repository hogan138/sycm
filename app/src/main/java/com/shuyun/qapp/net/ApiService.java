package com.shuyun.qapp.net;


import com.shuyun.qapp.bean.AccountBean;
import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.bean.ActivityTimeBean;
import com.shuyun.qapp.bean.AdBean;
import com.shuyun.qapp.bean.AddWithdrawResultBean;
import com.shuyun.qapp.bean.AnnounceBean;
import com.shuyun.qapp.bean.AnswerOpptyBean;
import com.shuyun.qapp.bean.AnswerRecordBean;
import com.shuyun.qapp.bean.AppVersionBean;
import com.shuyun.qapp.bean.ApplyAnswer;
import com.shuyun.qapp.bean.AuthNameBean;
import com.shuyun.qapp.bean.BannerBean;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.CompleteAnswerResponse;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.ExchangeHistoryBean;
import com.shuyun.qapp.bean.ExchangeMyPrizeBean;
import com.shuyun.qapp.bean.GroupAgainstBean;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.bean.GroupDetail;
import com.shuyun.qapp.bean.HomeGroupsBean;
import com.shuyun.qapp.bean.IntegralAllPrizeBean;
import com.shuyun.qapp.bean.IntegralExchangeBean;
import com.shuyun.qapp.bean.IntegralPrizeBean;
import com.shuyun.qapp.bean.InviteBean;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.LookAnswerResultBean;
import com.shuyun.qapp.bean.MainAgainstBean;
import com.shuyun.qapp.bean.MainConfigBean;
import com.shuyun.qapp.bean.MatchTimeBean;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.bean.OutPutWithdraw;
import com.shuyun.qapp.bean.PrizeDetailBean;
import com.shuyun.qapp.bean.PrizeHistoryBean;
import com.shuyun.qapp.bean.RealNameBean;
import com.shuyun.qapp.bean.RobotShowBean;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.bean.SystemInfo;
import com.shuyun.qapp.bean.ThreeFinishBean;
import com.shuyun.qapp.bean.ThreeTimePrize;
import com.shuyun.qapp.bean.UserWxInfo;
import com.shuyun.qapp.bean.XruleActivityPrizeBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * 接口地址
 */

public interface ApiService {


    /**
     * 1、首页banner/app-center
     */
    @GET("/rest/app/banner")
    Observable<DataResponse<List<BannerBean>>> getHomeBanner();

    /**
     * 2 首页题组/activity-center
     *
     * @return
     */
    @GET("/rest/act/known/group/home")
    Observable<DataResponse<HomeGroupsBean>> getHomeGroups(@Query("appVersion") String appVersion);


    /**
     * 3、登录/authorize-service
     * 加上了数美deviceId
     */
    @POST("/rest/security/login")
    Observable<DataResponse<LoginResponse>> login(@Body RequestBody body);

    /**
     * 4、获取题组详情//{id} /activity-center TODO act  参数问题  ,@Query("act") long act
     *
     * @param id
     * @return
     * @Query("act") int act  该题组参与的活动id
     */
    @GET("/rest/act/known/group/{id}")
    Observable<DataResponse<GroupDetail>> getGroupDetails(@Path("id") int id);

    /**
     * 5、答题活动的中奖扩展规则答题情况TODO
     * /activity-center
     *
     * @param id
     * @return
     */
    @GET("/rest/act/exam/xrule/act/{id}")
    Observable<DataResponse<ThreeTimePrize>> getXruleAnswerInfo(@Path("id") int id);

    /**
     * 6、扩展活动抽奖TODO
     * /activity-center
     * 加上了数美deviceId
     *
     * @param act
     * @return
     */
    @GET("/rest/act/x/lucky")
    Observable<DataResponse<XruleActivityPrizeBean>> getXruleActivityPrize(@Query("act") int act, @Query("deviceId") String deviceId);

    /**
     * 7、获取扩展抽奖活动信息TODO
     * /activity-center/
     *
     * @param act
     * @return
     */
    @GET("/rest/act/x/lucky/info")
    Observable<DataResponse<ThreeFinishBean>> getXruleActivityPrizeInfo(@Query("act") int act);

    /**
     * 8、申请答题
     * /activity-center
     *
     * @return
     */
    @GET("/rest/act/exam/V3")
    Observable<DataResponse<ApplyAnswer>> applyAnswer(@Query("groupId") int groupId, @Query("type") int type, @Query("battle") int battle);

    /**
     * 9、完成答题post请求,提交数据
     * 加上了数美deviceId
     */
    @POST("/rest/act/exam/v2")
    Observable<DataResponse<CompleteAnswerResponse>> completeAnswer(@Body RequestBody body);

    /**
     * 10、查看答题结果/activity-center/
     *
     * @param id
     * @return
     */
    @GET("/rest/act/exam/V2/{id}")
    Observable<DataResponse<LookAnswerResultBean>> lookAnswerResult(@Path("id") String id);

    /**
     * 11、用户中心/user-center
     */
    @GET("/rest/user/profile")
    Observable<DataResponse<MineBean>> getMineHomeData();

    /**
     * 12、实名认证/user-center
     */

    @GET("/rest/user/certificate/apply")
    Observable<DataResponse<RealNameBean>> realNameAuth(@Query("name") String realname, @Query("idCard") String idcard);

    /**
     * 13、获取我的奖品/user-center
     */

    @GET("/rest/user/award/v3")
    Observable<DataResponse<List<MinePrize>>> getMyPrize(@Query("status") int status, @Query("page") int page);

    /**
     * 14、申请提现
     * /user-center
     *
     * @param body
     * @return
     */

    @POST("/rest/user/money/withdraw/apply")
    Observable<DataResponse<OutPutWithdraw>> applyWithdrawal(@Body RequestBody body);

    /**
     * 15、查询现金流水记录
     * /user-center
     *
     * @return
     */
    @GET("/rest/user/cash")
    Observable<DataResponse<List<AccountBean>>> queryCashFlow(@Query("page") int page);

    /**
     * /activity-center/rest/act/bp/lucky/info
     * 16、获取积分抽奖活动信息 /activity-center
     */
    @GET("/rest/act/bp/lucky/info")
    Observable<DataResponse<AnnounceBean>> getIntegralDrawInfo();

    /**
     * 17、查询积分流水记录
     * /user-center
     *
     * @return
     */
    @GET("/rest/user/bp")
    Observable<DataResponse<List<AccountBean>>> queryIntegralCurrent(@Query("page") int page);


    /**
     * 18、获取验证码
     * /authorize-service
     *
     * @return
     */
    @POST("/rest/security/sms")
    Observable<DataResponse<String>> getCode(@Body RequestBody body);


    /**
     * 19、绑定手机号 /user-center
     * 当err=S0002时，dat表示当前的状态
     *
     * @param phone 手机号
     * @param code  验证码
     * @return
     */
    @GET("/rest/user/phone/bind")
    Observable<DataResponse> bindPhomeNum(@Query("phone") String phone, @Query("code") String code);

    /**
     * 20、用户变更头像
     * /user-center
     *
     * @param headerId
     * @return
     */
    @GET("/rest/user/header")
    Observable<DataResponse<Integer>> changeHeader(@Query("headerId") int headerId);

    /**
     * 21、积分抽奖
     * /activity-center
     *
     * @return
     */

    /**
     * 21、积分抽奖
     * /activity-center
     * 加上了数美 deviceId
     *
     * @param deviceId 设备id  数美sdk返回的设备id
     * @return
     */
    @GET("/rest/act/bp/lucky")
    Observable<DataResponse<IntegralPrizeBean>> integralDraw(@Query("deviceId") String deviceId);

    /**
     * 22、打开宝箱
     * /user-center
     *
     * @param id
     * @return
     */
    @GET("/rest/user/lucky/{id}")
    Observable<DataResponse<List<MinePrize>>> openTreasureBox(@Path("id") String id);


    /**
     * 23、打开宝箱2
     * /user-center
     *
     * @param id
     * @return
     */
    @GET("/rest/user/lucky/{id}")
    Observable<Object> openTreasureBox2(@Path("id") String id);

    /**
     * 24、获取宝箱数量
     * /user-center
     *
     * @return
     */
    @GET("/rest/user/lucky/available")
    Observable<DataResponse<Integer>> getTreasureNum();


    /**
     * 25、获取宝箱数量
     * /user-center
     *
     * @return
     */
    @GET("/rest/user/lucky/available/v2")
    Observable<DataResponse<BoxBean>> getTreasureNumV2();

    /**
     * 26、答题分享
     * /share-service
     *
     * @param channel 分享目标渠道1:微信朋友圈;2:微信好友
     * @param id      答题id
     * @return
     */
    @GET("/rest/share/exam")
    Observable<DataResponse<SharedBean>> answerShared(@Query("channel") int channel, @Query("id") String id);

    /**
     * 27、邀请分享
     * /share-service
     *
     * @param channel 分享渠道id
     * @return
     */
    @GET("/rest/share/invite")
    Observable<DataResponse<SharedBean>> inviteShared(@Query("channel") int channel);

    /**
     * 28、分享确认
     * /share-service
     *
     * @param id      分享id
     * @param result  分享结果
     * @param channel 分享渠道
     * @return
     */
    @GET("/rest/share/{id}/confirm")
    Observable<DataResponse> sharedConfirm(@Path("id") int id, @Query("result") int result, @Query("channel") int channel);

    /**
     * 29、获取题组树
     * /activity-center
     *
     * @return
     */
    @GET("/rest/act/known/group/tree")
    Observable<DataResponse<List<GroupClassifyBean>>> getGroupTree();

    /**
     * 30、用户答题记录TODO /activity-center
     */
    @GET("/rest/act/exam/history")
    Observable<DataResponse<List<AnswerRecordBean>>> getAnswerRecord(@Query("page") int page);

    /**
     * 31、答题题目反馈TODO
     * /activity-center
     */
    @GET("/rest/act/q/{id}/fb")
    Observable<DataResponse> getAnswerFeedBack(@Path("id") int id, @Query("feedbackId") int feedbackId);


    /**
     * 32、领取答题机会TODO
     * /user-center
     */
    @GET("/rest/user/oppty")
    Observable<DataResponse<AnswerOpptyBean>> getAnswerOppty();

    /**
     * 33、答题机会领取剩余时长TODO
     * /user-center
     */
    @GET("/rest/user/oppty/remainder")
    Observable<DataResponse<String>> getAnswerOpptyRemainder();


    /**
     * 34、反馈建议TODO
     * /user-center
     */
    @POST("/rest/user/fb")
    Observable<DataResponse> getFeedBack(@Body RequestBody body);

    /**
     * 35、获取系统消息/app-center
     *
     * @return
     */
    @GET("/rest/app/msg")
    Observable<DataResponse<List<SystemInfo>>> getSystemInfo();

    /**
     * 36、获取广告列表 LoginInput /app-center
     *
     * @return
     */
    @GET("/rest/app/ad")
    Observable<DataResponse<AdBean>> getAd(@Query("devId") int devId, @Query("appId") int appId,
                                           @Query("v") int v, @Query("stamp") String stamp, @Query("code") String code);

    /**
     * 37、绑定微信 /user-center
     *
     * @param phone 绑定的手机号码
     * @param code  短信验证码
     *              inviter 邀请人id ,@Query("inviter") int inviter
     * @return
     */
    @GET("/rest/user/wx/bind")
    Observable<DataResponse> bindWx(@Query("phone") String phone, @Query("code") String code);

    /**
     * 38、使用道具
     * 增次卡
     *
     * @return
     */
    @GET("/rest/user/prop/{id}/use")
    Observable<DataResponse> addAnswerNum(@Path("id") String id);


    /**
     * 39、题组分享
     * /share-service
     *
     * @param channel 分享目标渠道1:微信朋友圈;2:微信好友
     * @param id      题组id
     * @return
     */
    @GET("/rest/share/group")
    Observable<DataResponse<SharedBean>> groupShared(@Query("channel") int channel, @Query("id") int id);

    /**
     * 40、获取推送消息
     * page 数字  分页
     *
     * @param stamp1 时间戳  本地的最新消息的时间戳  第一次：app取值为0后续：取本次存储消息的最新一条的时间戳
     * @param stamp0 时间戳  推送消息的最早的消息的时间戳  第一次：app取当前的时间戳。分页调用时：取上一页的最后一条推送消息的时间戳
     * @return
     */
    @GET("/rest/app/sys/msg")
    Observable<DataResponse<List<Msg>>> getMsg(@Query("page") int page, @Query("stamp1") long stamp1, @Query("stamp0") long stamp0);//@Query("page") int page, @Query("stamp1") long stamp1, @Query("stamp0") long stamp0


    /**
     * 41、忘记密码（修改密码）
     * 加上了数美deviceId
     */
    @GET("/rest/security/pwd/mod")
    Observable<DataResponse<LoginResponse>> modifyPassWord(@Query("phone") String phone, @Query("pwd") String pwd,
                                                           @Query("devId") int devId, @Query("appId") int appId,
                                                           @Query("salt") String salt, @Query("tsn") String tsn, @Query("deviceId") String deviceId,
                                                           @Query("appVersion") String appVersion, @Query("v") int v,
                                                           @Query("stamp") long stamp, @Query("code") String code);

    /**
     * 42、更改手机号----验证老手机号
     */
    @GET("/rest/user/phone/change/old")
    Observable<DataResponse<String>> verifyoldphone(@Query("code") String code);

    /**
     * 43、更改手机号----验证新手机号
     */
    @GET("/rest/user/phone/change/new")
    Observable<DataResponse<String>> verifynewphone(@Query("code") String code, @Query("phone") String phone);

    /**
     * 44、新版本查询
     */
    @GET("/rest/app/upgrade/version")
    Observable<DataResponse<AppVersionBean>> updateVersion(@Query("appVersion") String appVersion, @Query("devId") int devId,
                                                           @Query("appId") int appId, @Query("v") int v,
                                                           @Query("stamp") long stamp, @Query("code") String code);

    /**
     * 45、变更绑定微信
     *
     * @param code 微信的临时票据
     * @return
     */
    @GET("/rest/user/alter/wx")
    Observable<DataResponse<UserWxInfo>> changeWXbind(@Query("code") String code);

    /**
     * 46、账户注销前置条件判断
     */
    @GET("/rest/accountDel/condition")
    Observable<DataResponse> verifyCondition();

    /**
     * 47、账户注销提交审核
     */
    @GET("/rest/accountDel/publish")
    Observable<DataResponse> commitCondition(@Query("picUrl") String picUrl);

    /**
     * 48、账户注销撤回审核
     */
    @GET("/rest/accountDel/cancel")
    Observable<DataResponse> removeCondition();

    /**
     * 49、答题对战首页
     */
    @GET("/rest/battle/bIndex")
    Observable<DataResponse<MainAgainstBean>> mainAgainst();

    /**
     * 50、答题对战题组
     */
    @GET("/rest/battle/bKnowGroup")
    Observable<DataResponse<List<GroupAgainstBean>>> groupAgainst(@Query("type") int type);


    /**
     * 51、用户匹配时长
     * //@param jwtToken @Query("jwtToken") String jwtToken
     *
     * @return
     */
    @GET("/rest/battle/bMatch")
    Observable<DataResponse<MatchTimeBean>> getMatchTimeInfo();

    /**
     * 52、答题对战随机题组
     */
    @GET("/rest/battle/bRandomGroup/V2")
    Observable<DataResponse<ApplyAnswer>> randomGroup(@Query("type") int type);


    /**
     * 53、答题对战机器人模拟对战
     */
    @POST("/rest/battle/bBegin")
    Observable<DataResponse<RobotShowBean>> against(@Body RequestBody body);

    /**
     * 54、题对战用户积分消耗
     *
     * @param type 用于判断是什么场次的
     * @return
     */
    @GET("/rest/battle/bUserBpCons")
    Observable<DataResponse> useBpCons(@Query("type") int type);

    /**
     * 55、答题对战结束
     *
     * @param type  用于判断是什么场次的
     * @param isWin 是否胜利(0 为胜利，其他为平局)
     * @return
     */
    @GET("/rest/battle/bEnd")
    Observable<DataResponse<Integer>> completeAnswAgainst(@Query("type") int type, @Query("isWin") int isWin);


    /**
     * 56、答题对战分享
     * /share-service
     *
     * @param channel 分享渠道id:1——微信朋友圈;2——微信好友;3——二维码;
     * @return
     */
    @GET("/rest/share/battle")
    Observable<DataResponse<SharedBean>> battleAnswerShared(@Query("channel") int channel);

    @GET("/rest/share/battleEnd")
    Observable<DataResponse<SharedBean>> battleAnswerShared1(@Query("channel") int channel, @Query("type") int type);

    /**
     * 57、积分兑换首页
     */
    @GET("/rest/act/bp/bpExchangeIndex")
    Observable<DataResponse<IntegralExchangeBean>> getExchangeMain();

    /**
     * 58、积分夺宝全部奖品
     */
    @GET("/rest/act/bp/treasure/allPrize")
    Observable<DataResponse<List<IntegralAllPrizeBean>>> getAllPrize(@Query("page") int page);

    /**
     * 59、积分夺宝开奖历史
     */
    @GET("/rest/act/bp/treasure/treasureHistory")
    Observable<DataResponse<List<PrizeHistoryBean>>> getPrizeHistory(@Query("page") int page);

    /**
     * 60、积分夺宝我的奖券
     */
    @GET("/rest/act/bp/treasure/myTicket")
    Observable<DataResponse<List<ExchangeMyPrizeBean>>> getMyPrize(@Query("page") int page);

    /**
     * 61、宝贝详情
     */
    @GET("/rest/act/bp/treasure/prizeDetail")
    Observable<DataResponse<PrizeDetailBean>> getDetailInfo(@Query("scheduleId") String scheduleId);

    /**
     * 62、确认兑换
     */
    @GET("/rest/act/bp/treasure/confirmExchange")
    Observable<DataResponse> enterExchange(@Query("scheduleId") String scheduleId);

    /**
     * 63、兑换记录
     */
    @GET("/rest/act/bp/treasure/changeRecord")
    Observable<DataResponse<ExchangeHistoryBean>> ExchangeHistory(@Query("scheduleId") String scheduleId, @Query("type") int type, @Query("page") int page);

    /**
     * 64、宝贝详情分享
     * /share-service
     *
     * @param channel 分享渠道id:1——微信朋友圈;2——微信好友;3——二维码;
     * @return
     */
    @GET("/rest/share/mineTreasureExchange")
    Observable<DataResponse<SharedBean>> prizeShare(@Query("channel") int channel, @Query("scheduleId") String scheduleId);

    /**
     * 65、首页邀请有奖
     */
    @GET("/rest/user/inviteShare")
    Observable<DataResponse<InviteBean>> prizeShare();

    /**
     * 66、用户设置密码
     */
    @GET("/rest/user/setPwd")
    Observable<DataResponse<String>> setPwd(@Query("pwd") String pwd);

    /**
     * 67、忘记密码验证
     */
    @GET("/rest/security/code/verify")
    Observable<DataResponse<String>> verifyPassWord(@Query("phone") String phone, @Query("devId") int devId,
                                                    @Query("appId") int appId, @Query("type") int type, @Query("v") int v,
                                                    @Query("stamp") long stamp, @Query("code") String code);

    /**
     * 68、首页弹框配置
     */
    @GET("/rest/home/config")
    Observable<DataResponse<ConfigDialogBean>> configDialog();

    /**
     * 69、首页活动配置
     */
    @GET("/rest/home/activity/config")
    Observable<DataResponse<MainConfigBean>> configMainActivity();

    /**
     * 70、活动专区数据
     */
    @GET("/rest/act/zone/list")
    Observable<DataResponse<ActivityTabBean>> ActivityList(@Query("pageStart") String pageStart, @Query("pageSize") String pageSize);

    /**
     * 71、获取最新活动时间
     */
    @GET("/rest/act/zone/status")
    Observable<DataResponse<ActivityTimeBean>> getActivityShow();

    /**
     * 72、点击活动专区
     */
    @GET("/rest/act/zone/action")
    Observable<DataResponse<String>> clickActivity();

    /**
     * 73、分享
     *
     * @param channel 分享渠道id
     * @return
     */
    @GET("/rest/share")
    Observable<DataResponse<SharedBean>> SharedPublic(@Query("channel") int channel, @Query("action") String action, @Query("id") String id);

    @GET("/rest/share")
    Observable<DataResponse<SharedBean>> SharedPublic1(@Query("channel") int channel, @Query("action") String action);

    /**
     * 74、提现信息完善
     */
    @POST("/rest/user/money/withdraw/submit")
    Observable<DataResponse<AddWithdrawResultBean>> submitWithdrawInfo(@Body RequestBody body);

    /**
     * 75、实名认证结果查询
     */
    @GET("/rest/user/certificate/query")
    Observable<DataResponse<AuthNameBean>> queryRealResult(@Query("bizNo") String bizNo);
}