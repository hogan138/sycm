package com.shuyun.qapp.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.bean.AddWithdrawResultBean;
import com.shuyun.qapp.bean.AddWxWithdrawBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.LoginInput;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.MessageEvent;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.bean.SubmitWithdrawInfoBean;
import com.shuyun.qapp.bean.UserWxInfo;
import com.shuyun.qapp.net.ActivityCallManager;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.AliPushBind;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.greenrobot.eventbus.EventBus;
import org.litepal.LitePal;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.shuyun.qapp.net.SykscApplication.getAppContext;
import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 微信登录
 */
public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler, OnRemotingCallBackListener<Object> {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private static final String TAG = "WXEntryActivity";
    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        //如果没回调onResp，八成是这句没有写
        SykscApplication.mWxApi.handleIntent(getIntent(), this);
    }

    // 微信发送请求到第三方应用时，会回调到该方法
    @Override
    public void onReq(BaseReq req) {
    }

    // 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
    //app发送消息给微信，处理返回消息的回调
    @Override
    public void onResp(BaseResp resp) {
        Log.e(TAG, "onResp: " + resp.errStr);
        Log.e(TAG, "错误码 : " + resp.errCode + "");
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if (RETURN_MSG_TYPE_SHARE == resp.getType())
                    ToastUtil.showToast(this, "取消分享");
                else
                    ToastUtil.showToast(this, "登录失败");
                break;
            case BaseResp.ErrCode.ERR_OK:
                switch (resp.getType()) {
                    case RETURN_MSG_TYPE_LOGIN:
                        //拿到了微信返回的code,立马再去请求access_token
                        String code = ((SendAuth.Resp) resp).code;
                        Log.i(TAG, "onResp: " + "code = " + code);

                        //就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
                        if (!EncodeAndStringTool.isStringEmpty(code)) {
                            Long expire = (Long) SharedPrefrenceTool.get(mContext, "expire", System.currentTimeMillis());//token的有效时间
                            long currentTimeMillis = System.currentTimeMillis();
                            /**
                             * 如果没有登录1:走登录逻辑;2:否则将微信code存本地
                             */
                            if (!AppConst.isLogin() || currentTimeMillis >= expire) {
                                long curTime = System.currentTimeMillis();
                                String tsn = EncodeAndStringTool.getTsn(getAppContext());
                                String salt = EncodeAndStringTool.generateRandomString(12);
                                SharedPrefrenceTool.put(getAppContext(), "salt", salt);
                                StringBuilder sb = new StringBuilder();
                                sb.append(AppConst.DEV_ID)
                                        .append(AppConst.APP_ID)
                                        .append(AppConst.V)
                                        .append(curTime)
                                        .append(AppConst.WX_MODE)
                                        .append(code)
                                        .append(salt)
                                        .append(AppConst.APP_KEY);

                                //devId+appId+v+stamp+mode+account+salt+appSecret
                                //将拼接的字符串转化为16进制MD5
                                String myCode = encryptMD5ToString(sb.toString());
                                String signCode = getCode(myCode);

                                final LoginInput loginInput = new LoginInput();
                                loginInput.setMode(AppConst.WX_MODE);
                                loginInput.setAccount(code);
                                loginInput.setTsn(tsn);
                                loginInput.setSalt(salt);
                                loginInput.setDevId(AppConst.DEV_ID);
                                loginInput.setAppId(AppConst.APP_ID);
                                loginInput.setV(AppConst.V);
                                loginInput.setStamp(curTime);
                                loginInput.setAppVersion(APKVersionCodeTools.getVerName(mContext));
                                loginInput.setCode(signCode);
                                try {
                                    //是否是答题免登陆，传入答卷id
                                    String examId = SaveUserInfo.getInstance(mContext).getUserInfo("answer_exam_id");
                                    if (!EncodeAndStringTool.isStringEmpty(examId)) {
                                        loginInput.setExamId(examId);
                                    }
                                } catch (Exception e) {

                                }
                                String deviceId = SmAntiFraud.getDeviceId();
                                loginInput.setDeviceId(deviceId);
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        loadLogin(getAppContext(), loginInput);
                                    }
                                });
                            } else {
                                Log.i(TAG, "onResp: " + code);
                                SaveUserInfo.getInstance(mContext).setUserInfo("wxcode", code);//将微信code存本地

                                String weixin_addwithdraw = SaveUserInfo.getInstance(this).getUserInfo("bind_weixin_addwithdraw");
                                if (!EncodeAndStringTool.isStringEmpty(weixin_addwithdraw) && weixin_addwithdraw.equals("add")) {
                                    //完善微信提现信息
                                    addWxWtihdraw(code);
                                    SaveUserInfo.getInstance(this).setUserInfo("bind_weixin_addwithdraw", "");
                                } else {
                                    //变更微信绑定
                                    loadChangeWXbind(code);
                                }

                            }
                        }
                        break;
                    case RETURN_MSG_TYPE_SHARE:
                        Log.i(TAG, "onResp: 微信分享成功");
                        break;
                }
                break;
        }
    }

    /**
     * 完善微信提现信息
     *
     * @param wxcode
     */
    public void addWxWtihdraw(String wxcode) {
        RemotingEx.doRequest("addWxWtihdraw", ApiServiceBean.addWxWithdraw(), new Object[]{wxcode}, this);
    }


    /**
     * 变更绑定微信
     *
     * @param wxcode
     */
    public void loadChangeWXbind(String wxcode) {
        RemotingEx.doRequest("changeWXbind", ApiServiceBean.changeWXbind(), new Object[]{wxcode}, this);
    }


    /**
     * 登录
     */
    public void loadLogin(final Context mContext, final LoginInput loginInput) {
        SaveUserInfo.getInstance(mContext).setUserInfo("account", loginInput.getAccount());
        SaveUserInfo.getInstance(mContext).setUserInfo("phone", loginInput.getAccount());
        String account = SaveUserInfo.getInstance(mContext).getUserInfo("account");
        /**
         * 如果两次登录用户不是同一用户,则清空本地数据库中的消息表
         */
        if (!loginInput.getAccount().equals(account)) {
            LitePal.deleteAll(Msg.class);//清空数据库中消息
        }
        final String inputbean = JSON.toJSONString(loginInput);
        Log.i(TAG, "loadLogin: " + loginInput.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest("login", ApiServiceBean.login(), new Object[]{body}, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(mContext, LoginActivity.class));
        finish();
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if ("changeWXbind".equals(action)) {
            UserWxInfo wxBindResultBean = (UserWxInfo) response.getDat();
            if (response.isSuccees()) {
                SaveUserInfo.getInstance(mContext).setUserInfo("nickname", wxBindResultBean.getNickname());
                SaveUserInfo.getInstance(mContext).setUserInfo("wxBind", String.valueOf(wxBindResultBean.getWxBind()));
                SaveUserInfo.getInstance(mContext).setUserInfo("wxHeader", wxBindResultBean.getWxHeader());
                ToastUtil.showToast(mContext, "成功变更绑定微信!");
                //通知任务更新
                EventBus.getDefault().post(new MessageEvent("wx_bind_success"));
            } else {
                //错误码提示
                ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            }
        } else if ("login".equals(action)) {
            if (response.isSuccees()) {

                //阿里推送绑定别名
                AliPushBind.bindPush();

                LoginResponse loginResp = (LoginResponse) response.getDat();
                if (!EncodeAndStringTool.isObjectEmpty(loginResp)) {
                    //统一给活动的Activity处理
                    if (ActivityCallManager.instance().getActivity() != null) {
                        ActivityCallManager.instance().getActivity().callBack(loginResp);
                    }
                    finish();
                }
            } else {
                ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            }
        } else if ("addWxWtihdraw".equals(action)) {  //完善微信提现信息
            if (response.isSuccees()) {
                AddWxWithdrawBean addWxWithdrawBean = (AddWxWithdrawBean) response.getDat();
                String card = addWxWithdrawBean.getCard();
                String name = addWxWithdrawBean.getName();
                SubmitWithdrawInfoBean submitWithdrawInfoBean = new SubmitWithdrawInfoBean(2, card, name);
                //提交微信信息
                AddWithdrawalInfo(submitWithdrawInfoBean);
            } else {
                ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            }
        }
    }

    //完善提现信息
    private void AddWithdrawalInfo(SubmitWithdrawInfoBean submitWithdrawInfoBean) {
        final String inputbean = JSON.toJSONString(submitWithdrawInfoBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);

        RemotingEx.doRequest(ApiServiceBean.submitWithdrawInfo(), new Object[]{body}, new OnRemotingCallBackListener<AddWithdrawResultBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<AddWithdrawResultBean> listDataResponse) {
                if (listDataResponse.isSuccees()) {
                    //通知任务更新
                    EventBus.getDefault().post(new MessageEvent("wx_commit_success"));
                } else {
                    ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
                }
            }
        });

    }

}
