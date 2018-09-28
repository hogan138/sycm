package com.shuyun.qapp.wxapi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.TimeUtils;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.LoginInput;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.bean.UserWxInfo;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.login.BindPhoneNumActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.umeng.socialize.weixin.view.WXCallbackActivity;

import org.litepal.crud.DataSupport;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.shuyun.qapp.net.MyApplication.getAppContext;
import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 微信登录
 */
public class WXEntryActivity extends WXCallbackActivity implements IWXAPIEventHandler {
    private static final int RETURN_MSG_TYPE_LOGIN = 1;
    private static final int RETURN_MSG_TYPE_SHARE = 2;
    private static final String TAG = "WXEntryActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //如果没回调onResp，八成是这句没有写
        MyApplication.mWxApi.handleIntent(getIntent(), this);
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
                            Long expire = (Long) SharedPrefrenceTool.get(WXEntryActivity.this, "expire", System.currentTimeMillis());//token的有效时间
                            long currentTimeMillis = System.currentTimeMillis();
                            /**
                             * 如果没有登录1:走登录逻辑;2:否则将微信code存本地
                             */
                            if (!AppConst.isLogon() || currentTimeMillis >= expire) {
                                long curTime = System.currentTimeMillis();
                                String tsn = EncodeAndStringTool.getTsn(getAppContext());
                                String salt = EncodeAndStringTool.generateRandomString(12);
                                SharedPrefrenceTool.put(getAppContext(), "salt", salt);
                                //devId+appId+v+stamp+mode+account+salt+appSecret
                                String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + AppConst.WX_MODE + code + salt + AppConst.APP_KEY;
                                //将拼接的字符串转化为16进制MD5
                                String myCode = encryptMD5ToString(signString);
                                /**
                                 * code值
                                 */
                                String signCode = getCode(myCode);
//                                LogUtil.logInfo("curTime" + curTime + ",signString==" + signString + ",myCode==" + myCode, TAG);

                                final LoginInput loginInput = new LoginInput();
                                loginInput.setMode(AppConst.WX_MODE);
                                loginInput.setAccount(code);
                                loginInput.setTsn(tsn);
                                loginInput.setSalt(salt);
                                loginInput.setDevId(AppConst.DEV_ID);
                                loginInput.setAppId(AppConst.APP_ID);
                                loginInput.setV(AppConst.V);
                                loginInput.setStamp(curTime);
                                loginInput.setAppVersion(APKVersionCodeTools.getVerName(WXEntryActivity.this));
                                loginInput.setCode(signCode);

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
                                SaveUserInfo.getInstance(WXEntryActivity.this).setUserInfo("wxcode", code);//将微信code存本地
                                if (!EncodeAndStringTool.isStringEmpty(code)) {
                                    loadChangeWXbind(code);
                                }
                            }
                        } else {
//                            Toast.makeText(this, "微信返回的code为空", Toast.LENGTH_SHORT).show();
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
     * 变更绑定微信
     *
     * @param wxcode
     */
    public void loadChangeWXbind(String wxcode) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.changeWXbind(wxcode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<UserWxInfo>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<UserWxInfo> dataResponse) {
                        UserWxInfo wxBindResultBean = dataResponse.getDat();
                        if (dataResponse.isSuccees()) {
                            SaveUserInfo.getInstance(WXEntryActivity.this).setUserInfo("nickname", wxBindResultBean.getNickname());
                            SaveUserInfo.getInstance(WXEntryActivity.this).setUserInfo("wxBind", String.valueOf(wxBindResultBean.getWxBind()));
                            SaveUserInfo.getInstance(WXEntryActivity.this).setUserInfo("wxHeader", wxBindResultBean.getWxHeader());
                            ToastUtil.showToast(WXEntryActivity.this, "成功变更绑定微信!");
                        } else {//错误码提示
                            ToastUtil.showToast(WXEntryActivity.this, "您的微信号已被其他账号绑定!");
                            ErrorCodeTools.errorCodePrompt(WXEntryActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 登录
     */
    public void loadLogin(final Context mContext, final LoginInput loginInput) {
        SaveUserInfo.getInstance(this).setUserInfo("account", loginInput.getAccount());
        String account = SaveUserInfo.getInstance(this).getUserInfo("account");
        /**
         * 如果两次登录用户不是同一用户,则清空本地数据库中的消息表
         */
        if (!loginInput.getAccount().equals(account)) {
            DataSupport.deleteAll(Msg.class);//清空数据库中消息
        }
        ApiService apiService = BasePresenter.create(8000);
        final String inputbean =  JSON.toJSONString(loginInput);
        Log.i(TAG, "loadLogin: " + loginInput.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        apiService.login(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<LoginResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<LoginResponse> loginResponse) {
                        if (loginResponse.isSuccees()) {
                            LoginResponse loginResp = loginResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(loginResp)) {
                                SharedPrefrenceTool.put(getAppContext(), "token", loginResp.getToken());
                                SharedPrefrenceTool.put(getAppContext(), "expire", loginResp.getExpire());//token的有效期
                                SharedPrefrenceTool.put(getAppContext(), "key", loginResp.getKey());//对称加密的秘钥。
                                SharedPrefrenceTool.put(getAppContext(), "bind", loginResp.getBind());//是否绑定用户。
                                SharedPrefrenceTool.put(getAppContext(), "random", loginResp.getRandom());//登录成果后，平台随机生成的字符串
                                AppConst.loadToken(getAppContext());
                                if (!EncodeAndStringTool.isStringEmpty(loginResp.getInvite())) {
                                    SharedPrefrenceTool.put(getAppContext(), "invite", loginResp.getInvite());
                                }
                                if (0 == loginResp.getBind()) {
                                    Intent intent = new Intent(WXEntryActivity.this, BindPhoneNumActivity.class);
                                    intent.putExtra("login_response", loginResp);
                                    startActivity(intent);
                                    finish();
                                } else if (1 == loginResp.getBind()) {
                                    Intent intent = new Intent(WXEntryActivity.this, HomePageActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, loginResponse.getErr(), loginResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(WXEntryActivity.this, LoginActivity.class));
        finish();
    }
}
