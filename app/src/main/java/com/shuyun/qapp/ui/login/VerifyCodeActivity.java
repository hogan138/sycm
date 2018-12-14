package com.shuyun.qapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.ishumei.smantifraud.SmAntiFraud;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputVerficationCodeBean;
import com.shuyun.qapp.bean.LoginInput;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.event.MessageEvent;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.mine.AddWithdrawInfoActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.MyActivityManager1;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.view.VerifyCodeView;
import com.shuyun.qapp.wxapi.WXEntryActivity;

import org.greenrobot.eventbus.EventBus;
import org.litepal.crud.DataSupport;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
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

    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                }
            }

            @Override
            public void invalidContent() {

            }
        });

        //获取验证码
        if (getIntent().getStringExtra("name").equals("login")) {
            sendVerifyCode(1); //验证码登录
        } else if (getIntent().getStringExtra("name").equals("register")) {
            sendVerifyCode(7); //注册
        } else if (getIntent().getStringExtra("name").equals("changePwd")) {
            sendVerifyCode(4); //忘记密码
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

    }

    //2验证码登录4注册
    private void register(String code, int mode) {
        String mdCode = encryptMD5ToString(phone + encryptMD5ToString(code));
        long curTime = System.currentTimeMillis();
        String tsn = EncodeAndStringTool.getTsn(this);
        String salt = EncodeAndStringTool.generateRandomString(12);

        SharedPrefrenceTool.put(VerifyCodeActivity.this, "salt", salt);
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
        loginInput.setAppVersion(APKVersionCodeTools.getVerName(VerifyCodeActivity.this));
        loginInput.setCode(signCode);
        String deviceId = SmAntiFraud.getDeviceId();
        loginInput.setDeviceId(deviceId);
        try {
            //是否是答题免登陆，传入答卷id
            String examId = SaveUserInfo.getInstance(VerifyCodeActivity.this).getUserInfo("answer_exam_id");
            if (!EncodeAndStringTool.isStringEmpty(examId)) {
                loginInput.setExamId(examId);
            }
        } catch (Exception e) {

        }
        loadLogin(VerifyCodeActivity.this, loginInput, mode);
    }

    //发送验证码
    private void sendVerifyCode(int type) {
        tvSendCode.setTextColor(Color.parseColor("#999999"));
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
        ApiService apiService = BasePresenter.create(8000);
        String inputbean = JSON.toJSONString(verficationCodeBean);
        Log.i(TAG, "loadLogin: " + verficationCodeBean.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        apiService.getCode(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<String> loginResponse) {
                        if (loginResponse.isSuccees()) {
                            KeyboardUtils.showSoftInput(VerifyCodeActivity.this);
                            //显示60s倒计时
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
                        } else {
                            ErrorCodeTools.errorCodePrompt(VerifyCodeActivity.this, loginResponse.getErr(), loginResponse.getMsg());
                            tvSendCode.setEnabled(true);
                            tvSendCode.setText("重新发送验证码");
                            tvSendCode.setTextColor(Color.parseColor("#0194ec"));
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

    public void loadLogin(final Context mContext, final LoginInput loginInput, final int mode) {
        SaveUserInfo.getInstance(this).setUserInfo("account", loginInput.getAccount());
        final String account = SaveUserInfo.getInstance(this).getUserInfo("account");
        /**
         * 如果两次登录用户不是同一用户,则清空本地数据库中的消息表
         */
        if (!loginInput.getAccount().equals(account)) {
            DataSupport.deleteAll(Msg.class);//清空数据库中消息
        }
        ApiService apiService = BasePresenter.create(8000);
        final String inputbean = JSON.toJSONString(loginInput);
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
                                SharedPrefrenceTool.put(VerifyCodeActivity.this, "token", loginResp.getToken());
                                SharedPrefrenceTool.put(VerifyCodeActivity.this, "expire", loginResp.getExpire());//token的有效期
                                SharedPrefrenceTool.put(VerifyCodeActivity.this, "key", loginResp.getKey());//对称加密的秘钥。
                                SharedPrefrenceTool.put(VerifyCodeActivity.this, "bind", loginResp.getBind());//是否绑定用户。
                                SharedPrefrenceTool.put(VerifyCodeActivity.this, "random", loginResp.getRandom());//登录成果后，平台随机生成的字符串
                                AppConst.loadToken(VerifyCodeActivity.this);

                                try {
                                    //答题免登录返回宝箱id
                                    if (!EncodeAndStringTool.isStringEmpty(loginResp.getBoxId())) {
                                        SharedPrefrenceTool.put(mContext, "boxId", loginResp.getBoxId());
                                    }
                                } catch (Exception e) {

                                }

                                //设置别名
                                JPushInterface.setAlias(VerifyCodeActivity.this, new Random().nextInt(), phone);

                                if (mode == 2) {
                                    try {
                                        if (loginResp.isSetPwd()) {
                                            //未设置密码
                                            Intent intent = new Intent(VerifyCodeActivity.this, SetPasswordActivity.class);
                                            intent.putExtra("name", "register");
                                            intent.putExtra("phone", phone);
                                            intent.putExtra("code", verifyCodeView.getEditContent());
                                            intent.putExtra("token", loginResp.getToken());
                                            startActivity(intent);
                                        } else {
                                            //已设置密码
                                            CustomLoadingFactory factory = new CustomLoadingFactory();
                                            LoadingBar.make(llMain, factory).show();
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    LoadingBar.cancel(llMain);
                                                    KeyboardUtils.hideSoftInput(VerifyCodeActivity.this);
                                                    MyActivityManager1.getInstance().finishAllActivity();
                                                    EventBus.getDefault().post(new MessageEvent(AppConst.APP_VERIFYCODE_LOGIN));
                                                    if ("3".equals(SaveUserInfo.getInstance(VerifyCodeActivity.this).getUserInfo("home_mine"))) {//来自个人信息微信登录
                                                        EventBus.getDefault().post(new MessageEvent("3"));
                                                        SaveUserInfo.getInstance(VerifyCodeActivity.this).setUserInfo("home_mine", "");
                                                    }
                                                }
                                            }, 2000);
                                        }
                                    } catch (Exception e) {

                                    }
                                } else if (mode == 4) {
                                    //注册
                                    Intent intent = new Intent(VerifyCodeActivity.this, SetPasswordActivity.class);
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
            DataSupport.deleteAll(Msg.class);//清空数据库中消息
        }
        ApiService apiService = BasePresenter.create(8000);
        apiService.verifyPassWord(phoneNum, AppConst.DEV_ID, AppConst.APP_ID, 4, AppConst.V, curTime, signCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<String> loginResponse) {
                        if (loginResponse.isSuccees()) {
                            Intent intent = new Intent(VerifyCodeActivity.this, SetPasswordActivity.class);
                            intent.putExtra("name", getIntent().getStringExtra("name"));
                            intent.putExtra("phone", phone);
                            intent.putExtra("code", verifyCodeView.getEditContent());
                            intent.putExtra("token", "");
                            startActivity(intent);
                        } else {
                            if (loginResponse.getErr().equals("TAU11")) {
                                errorDialog("验证码错误，请重新输入");
                            } else {
                                ErrorCodeTools.errorCodePrompt(VerifyCodeActivity.this, loginResponse.getErr(), loginResponse.getMsg());
                            }
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


}
