package com.shuyun.qapp.ui.login;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.KeyboardUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.ishumei.smantifraud.SmAntiFraud;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputVerficationCodeBean;
import com.shuyun.qapp.bean.LoginInput;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.manager.ActivityCallManager;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.AliPushBind;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.manager.MyActivityManager1;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.view.VerifyCodeView;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 验证码输入界面
 */
public class VerifyCodeActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.verify_code_view)
    VerifyCodeView verifyCodeView;
    @BindView(R.id.tv_send_code)
    TextView tvSendCode;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.btn_send_code)
    Button btnSendCode;

    String phone = "";
    private Activity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeyboardUtils.hideSoftInput(VerifyCodeActivity.this);
                finish();
            }
        });

        MyActivityManager1.getInstance().pushOneActivity(this);
        phone = getIntent().getStringExtra("phone");
        tvPhone.setText(phone);
        verifyCodeView.setInputCompleteListener(new VerifyCodeView.InputCompleteListener() {
            @Override
            public void inputComplete() {
                if (getIntent().getStringExtra("name").equals("login")) {
                    //验证码登录
                    register(verifyCodeView.getEditContent(), 2);
                } else if (getIntent().getStringExtra("name").equals("register")) {
                    //注册
                    register(verifyCodeView.getEditContent(), 4);
                } else if (getIntent().getStringExtra("name").equals("changePwd")) {
                    //忘记密码
                    verfifyPwd(verifyCodeView.getEditContent());
                } else if (getIntent().getStringExtra("name").equals("modifyPwd")) {
                    //修改密码
                    verfifyPwd(verifyCodeView.getEditContent());
                }
            }

            @Override
            public void invalidContent() {

            }
        });

        //获取验证码
        if (getIntent().getStringExtra("name").equals("login")) {
            tvSendCode.setVisibility(View.VISIBLE);
            sendVerifyCode(1); //验证码登录
        } else if (getIntent().getStringExtra("name").equals("register")) {
            tvSendCode.setVisibility(View.VISIBLE);
            sendVerifyCode(7); //注册
        } else if (getIntent().getStringExtra("name").equals("changePwd")) {
            tvSendCode.setVisibility(View.VISIBLE);
            sendVerifyCode(4); //忘记密码
        } else if (getIntent().getStringExtra("name").equals("modifyPwd")) {
            //修改密码
            tvCommonTitle.setText("修改密码");
            btnSendCode.setVisibility(View.VISIBLE);
        }

        tvSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().getStringExtra("name").equals("login")) {
                    sendVerifyCode(1); //验证码登录
                } else if (getIntent().getStringExtra("name").equals("register")) {
                    sendVerifyCode(7); //注册
                } else if (getIntent().getStringExtra("name").equals("changePwd")) {
                    sendVerifyCode(4); //忘记密码
                }

            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendVerifyCode(4); //修改密码
            }
        });

    }

    //2验证码登录4注册
    private void register(String code, long mode) {
        String mdCode = encryptMD5ToString(phone + encryptMD5ToString(code));
        long curTime = System.currentTimeMillis();
        String tsn = EncodeAndStringTool.getTsn(this);
        String salt = EncodeAndStringTool.generateRandomString(12);

        SharedPrefrenceTool.put(mContext, "salt", salt);
        //devId+appId+v+stamp+mode+account+tsn+salt+ appSecret+mdpwd
        String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + mode + phone + tsn + salt + AppConst.APP_KEY + mdCode;
        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(signString);
        /**
         * code值
         */
        String signCode = getCode(myCode);

        LoginInput loginInput = new LoginInput();
        loginInput.setMode(mode);
        loginInput.setAccount(phone);
        loginInput.setTsn(tsn);
        loginInput.setSalt(salt);
        loginInput.setDevId(AppConst.DEV_ID);
        loginInput.setAppId(AppConst.APP_ID);
        loginInput.setV(AppConst.V);
        loginInput.setStamp(curTime);
        loginInput.setAppVersion(APKVersionCodeTools.getVerName(mContext));
        loginInput.setCode(signCode);
        String deviceId = SmAntiFraud.getDeviceId();
        loginInput.setDeviceId(deviceId);
        try {
            //是否是答题免登陆，传入答卷id
            String examId = SaveUserInfo.getInstance(mContext).getUserInfo("answer_exam_id");
            if (!EncodeAndStringTool.isStringEmpty(examId)) {
                loginInput.setExamId(examId);
            }
        } catch (Exception e) {

        }
        loadLogin(mContext, loginInput, mode);
    }

    //发送验证码
    private void sendVerifyCode(int type) {
        long curTime0 = System.currentTimeMillis();
        //devId+appId+v+stamp+phone+type+appSecret
        String singString0 = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime0 + phone + type + AppConst.APP_KEY;
        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(singString0);
        /**
         * 签名code值
         */
        String signCode = getCode(myCode);
        InputVerficationCodeBean verficationCodeBean = new InputVerficationCodeBean();
        verficationCodeBean.setPhone(phone);
        verficationCodeBean.setType(type);//type为1表示验证码登录7表示注册
        verficationCodeBean.setDevId(AppConst.DEV_ID);
        verficationCodeBean.setAppId(AppConst.APP_ID);
        verficationCodeBean.setV(AppConst.V);
        verficationCodeBean.setStamp(curTime0);
        verficationCodeBean.setCode(signCode);
        //调用获取验证码的接口
        getVerficationCode(verficationCodeBean);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_verify_code;
    }


    /**
     * 获取到验证码
     *
     * @param verficationCodeBean post json body
     */
    private void getVerficationCode(InputVerficationCodeBean verficationCodeBean) {
        String inputbean = JSON.toJSONString(verficationCodeBean);
        Log.i(TAG, "loadLogin: " + verficationCodeBean.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest(RemotingEx.Builder().getCode(body), new OnRemotingCallBackListener<String>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<String> loginResponse) {
                if (loginResponse.isSuccees()) {
                    KeyboardUtils.showSoftInput(mContext);

                    if (getIntent().getStringExtra("name").equals("modifyPwd")) {
                        //修改密码显示按钮
                        btnSendCode.setEnabled(false);
                        btnSendCode.setTextColor(Color.parseColor("#999999"));
                        new CountDownTimer(120 * 1000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                btnSendCode.setText(String.format("%d", millisUntilFinished / 1000) + "s重新发送验证码");
                            }

                            @Override
                            public void onFinish() {
                                btnSendCode.setEnabled(true);
                                btnSendCode.setText("重新发送验证码");
                                btnSendCode.setTextColor(Color.parseColor("#ffffff"));
                            }
                        }.start();
                    } else {
                        //显示60s倒计时
                        tvSendCode.setTextColor(Color.parseColor("#999999"));
                        tvSendCode.setEnabled(false);
                        new CountDownTimer(120 * 1000, 1000) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                tvSendCode.setText(String.format("%d", millisUntilFinished / 1000) + "s重新发送验证码");
                            }

                            @Override
                            public void onFinish() {
                                tvSendCode.setEnabled(true);
                                tvSendCode.setText("重新发送验证码");
                                tvSendCode.setTextColor(Color.parseColor("#0194ec"));
                            }
                        }.start();
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(mContext, loginResponse.getErr(), loginResponse.getMsg());
                    if (getIntent().getStringExtra("name").equals("modifyPwd")) {
                        //修改密码显示按钮
                        btnSendCode.setEnabled(true);
                        btnSendCode.setText("重新发送验证码");
                        btnSendCode.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        tvSendCode.setEnabled(true);
                        tvSendCode.setText("重新发送验证码");
                        tvSendCode.setTextColor(Color.parseColor("#0194ec"));
                    }
                }
            }
        });
    }

    /**
     * 登录
     */

    public void loadLogin(final Activity mContext, final LoginInput loginInput, final long mode) {
        SaveUserInfo.getInstance(this).setUserInfo("account", loginInput.getAccount());
        SaveUserInfo.getInstance(mContext).setUserInfo("phone", loginInput.getAccount());
        final String account = SaveUserInfo.getInstance(this).getUserInfo("account");
        /**
         * 如果两次登录用户不是同一用户,则清空本地数据库中的消息表
         */
        if (!loginInput.getAccount().equals(account)) {
            LitePal.deleteAll(Msg.class);//清空数据库中消息
        }

        CustomLoadingFactory factory = new CustomLoadingFactory();
        LoadingBar.make(llMain, factory).show();

        final String inputbean = JSON.toJSONString(loginInput);
        Log.i(TAG, "loadLogin: " + loginInput.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);

        RemotingEx.doRequest(RemotingEx.Builder().login(body), new OnRemotingCallBackListener<LoginResponse>() {
            @Override
            public void onCompleted(String action) {
                LoadingBar.cancel(llMain);
            }

            @Override
            public void onFailed(String action, String message) {
                LoadingBar.cancel(llMain);
            }

            @Override
            public void onSucceed(String action, DataResponse<LoginResponse> loginResponse) {
                if (loginResponse.isSuccees()) {
                    LoginResponse loginResp = loginResponse.getDat();
                    //保存为全局对象
                    LoginDataManager.instance().setOverallData(loginResp);

                    if (!EncodeAndStringTool.isObjectEmpty(loginResp)) {
                        SharedPrefrenceTool.put(mContext, "token", loginResp.getToken());
                        SharedPrefrenceTool.put(mContext, "expire", loginResp.getExpire());//token的有效期
                        SharedPrefrenceTool.put(mContext, "key", loginResp.getKey());//对称加密的秘钥。
                        SharedPrefrenceTool.put(mContext, "bind", loginResp.getBind());//是否绑定用户。
                        SharedPrefrenceTool.put(mContext, "random", loginResp.getRandom());//登录成果后，平台随机生成的字符串
                        SaveUserInfo.getInstance(mContext).setUserInfo("cert", loginResp.getCertification());
                        AppConst.loadToken(mContext);

                        try {
                            //答题免登录返回宝箱id
                            if (!EncodeAndStringTool.isStringEmpty(loginResp.getBoxId())) {
                                SharedPrefrenceTool.put(mContext, "boxId", loginResp.getBoxId());
                            }
                        } catch (Exception e) {

                        }

                        //阿里推送绑定别名
                        AliPushBind.bindPush();

                        if (mode == 2) {
                            try {
                                if (loginResp.getSetPwd()) {
                                    //未设置密码
                                    Intent intent = new Intent(mContext, SetPasswordActivity.class);
                                    intent.putExtra("name", "register");
                                    intent.putExtra("phone", phone);
                                    intent.putExtra("code", verifyCodeView.getEditContent());
                                    intent.putExtra("token", loginResp.getToken());
                                    startActivity(intent);
                                } else {
                                    //已设置密码
                                    KeyboardUtils.hideSoftInput(mContext);
                                    //统一给活动的Activity处理
                                    if (ActivityCallManager.instance().getActivity() != null) {
                                        ActivityCallManager.instance().getActivity().callBack(loginResp);
                                    }
                                    MyActivityManager1.getInstance().finishAllActivity();
                                }
                            } catch (Exception e) {

                            }
                        } else if (mode == 4) {
                            //注册
                            Intent intent = new Intent(mContext, SetPasswordActivity.class);
                            intent.putExtra("name", getIntent().getStringExtra("name"));
                            intent.putExtra("phone", phone);
                            intent.putExtra("code", verifyCodeView.getEditContent());
                            intent.putExtra("token", loginResp.getToken());
                            startActivity(intent);
                        }

                    }
                } else {
                    if (loginResponse.getErr().equals("TAU11")) {
                        errorDialog("验证码错误，请重新输入");
                    } else {
                        ErrorCodeTools.errorCodePrompt(mContext, loginResponse.getErr(), loginResponse.getMsg());
                    }
                }
            }
        });

    }

    //错误弹框
    private void errorDialog(final String text) {
        new CircleDialog.Builder(this)
                .setText(text)
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative("好的", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

                    }
                })
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.animStyle = R.style.popwin_anim_style;
                    }
                })
                .show();
    }

    /**
     * 忘记密码验证
     */
    private void verfifyPwd(String code) {
        String phoneNum = getIntent().getStringExtra("phone");
        long curTime = System.currentTimeMillis();
        String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + phoneNum + code + AppConst.APP_KEY;
        Log.e("签名", signString);
        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(signString);
        /**
         * code值
         */
        String signCode = getCode(myCode);
        SaveUserInfo.getInstance(this).setUserInfo("account", phoneNum);
        String account = SaveUserInfo.getInstance(this).getUserInfo("account");
        /**
         * 如果两次登录用户不是同一用户,则清空本地数据库中的消息表
         */
        if (!phoneNum.equals(account)) {
            LitePal.deleteAll(Msg.class);//清空数据库中消息
        }
        CustomLoadingFactory factory = new CustomLoadingFactory();
        LoadingBar.make(llMain, factory).show();

        RemotingEx.doRequest(RemotingEx.Builder().verifyPassWord(phoneNum, AppConst.DEV_ID, AppConst.APP_ID, 4, AppConst.V, curTime, signCode), new OnRemotingCallBackListener<String>() {
            @Override
            public void onCompleted(String action) {
                LoadingBar.cancel(llMain);
            }

            @Override
            public void onFailed(String action, String message) {
                LoadingBar.cancel(llMain);
            }

            @Override
            public void onSucceed(String action, DataResponse<String> loginResponse) {
                if (loginResponse.isSuccees()) {
                    Intent intent = new Intent(mContext, SetPasswordActivity.class);
                    intent.putExtra("name", getIntent().getStringExtra("name"));
                    intent.putExtra("phone", phone);
                    intent.putExtra("code", verifyCodeView.getEditContent());
                    startActivity(intent);
                } else {
                    KeyboardUtils.hideSoftInput(mContext);
                    if (loginResponse.getErr().equals("S0003")) {
                        errorDialog("验证码错误，请重新输入");
                    } else {
                        ErrorCodeTools.errorCodePrompt(mContext, loginResponse.getErr(), loginResponse.getMsg());
                    }
                }
            }
        });

    }


}
