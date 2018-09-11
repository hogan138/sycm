package com.shuyun.qapp.ui.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputVerficationCodeBean;
import com.shuyun.qapp.bean.LoginInput;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.homepage.PermissionsActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.PermissionsChecker;
import com.shuyun.qapp.utils.RegularTool;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import org.litepal.crud.DataSupport;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
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
 * 登录
 */
public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;//手机号输入框
    @BindView(R.id.iv_clear_phone_num)
    ImageView ivClearPhoneNum;//清空输入的手机号
    @BindView(R.id.et_password)
    EditText etPassword;//密码输入框
    @BindView(R.id.iv_clear_pwd)
    ImageView ivClearPwd;//清空输入的密码
    @BindView(R.id.iv_is_show_pwd)
    ImageView ivIsShowPwd;//是否显示或隐藏密码图标
    @BindView(R.id.btn_login)
    Button btnLogin;//登录按钮
    @BindView(R.id.iv_wechat_login)
    ImageView ivWechatLogin;//微信登录按钮
    @BindView(R.id.ll_other_login)
    LinearLayout llOtherLogin;//其他登录方式
    private static final String TAG = "LoginActivity";

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            android.Manifest.permission.READ_PHONE_STATE
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        btnLogin.setEnabled(false);
        mPermissionsChecker = new PermissionsChecker(this);
        clearEditText(etPhoneNumber, ivClearPhoneNum);
        clearEditText(etPassword, ivClearPwd);
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ivWechatLogin.setVisibility(View.GONE);
            llOtherLogin.setVisibility(View.GONE);
        }

        MyActivityManager.getInstance().pushOneActivity(this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_login;
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPrefrenceTool.clear(LoginActivity.this);//清空首选项中的数据

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

        /**
         * 友盟统计
         */
        MobclickAgent.onResume(this);
        StatService.onResume(this);
        etPhoneNumber.addTextChangedListener(new TextWatcher() {//没有输入正确格式的手机号,登录按钮置灰
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isLogin();
                if (RegularTool.isMobileExact(etPhoneNumber.getText().toString())) {

                } else {
                }
            }
        });

        etPassword.addTextChangedListener(new TextWatcher() {//没有输入正确格式的手机号,登录按钮置灰
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                isLogin();
            }
        });

    }

    private void isLogin() {
        if (!EncodeAndStringTool.isStringEmpty(etPhoneNumber.getText().toString()) && !EncodeAndStringTool.isStringEmpty(etPassword.getText().toString())) {
            btnLogin.setEnabled(true);
            btnLogin.setTextColor(Color.parseColor("#ffffff"));
        } else {
            btnLogin.setEnabled(false);
            btnLogin.setTextColor(Color.parseColor("#999999"));
        }
    }

    @OnClick({R.id.et_phone_number, R.id.et_password, R.id.iv_clear_phone_num, R.id.iv_clear_pwd,
            R.id.iv_is_show_pwd, R.id.tv_forget_pwd, R.id.btn_login, R.id.iv_wechat_login})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_clear_phone_num:
                etPhoneNumber.setText("");
                ivClearPhoneNum.setVisibility(View.GONE);
                isLogin();
                break;
            case R.id.iv_clear_pwd:
                etPassword.setText("");
                ivClearPwd.setVisibility(View.GONE);
                isLogin();
                break;
            case R.id.iv_is_show_pwd:
                if (etPassword.getText().length() != 0) {
                    isShowPwd(etPassword);
                }
                break;
//            case R.id.btn_get_code1:
//                if (!cbIsAgree.isChecked()) {
//                    Toast.makeText(this, "请您先同意全名共进用户服务协议!", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String phoneNum0 = etPhoneNumber.getText().toString().trim();
//
//                if (RegularTool.isMobileExact(phoneNum0)) {//正则判断手机号的
//                    long curTime0 = System.currentTimeMillis();
//                    //devId+appId+v+stamp+phone+type+appSecret
//                    String singString0 = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime0 + phoneNum0 + 1 + AppConst.APP_KEY;
//                    //将拼接的字符串转化为16进制MD5
//                    String myCode = encryptMD5ToString(singString0);
//                    /**
//                     * 签名code值
//                     */
//                    String signCode = getCode(myCode);
//                    InputVerficationCodeBean verficationCodeBean = new InputVerficationCodeBean();
//                    verficationCodeBean.setPhone(phoneNum0);
//                    verficationCodeBean.setType(1);//type为1表示登录
//                    verficationCodeBean.setDevId(AppConst.DEV_ID);
//                    verficationCodeBean.setAppId(AppConst.APP_ID);
//                    verficationCodeBean.setV(AppConst.V);
//                    verficationCodeBean.setStamp(curTime0);
//                    verficationCodeBean.setCode(signCode);
//                    if (NetworkUtils.isAvailableByPing()) {
//                        //调用获取验证码的接口
//                        getVerficationCode(verficationCodeBean);
//                    } else {
//                        Toast.makeText(this, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
//                    }
//
//                } else {
//                    Toast.makeText(this, "您输入的手机号格式有误,请重新输入!", Toast.LENGTH_SHORT).show();
//                }
//
//                break;
            case R.id.tv_forget_pwd:
                Intent intent = new Intent(LoginActivity.this, ChangePasswordActivity.class);
                intent.putExtra("modify", "login");
                startActivity(intent);
                break;
            case R.id.btn_login:
                String phoneNum = etPhoneNumber.getText().toString().trim();
//                if (LOGIN_MODE == CODE_LOGIN) {
//                    String code = etCode.getText().toString();
//                    boolean checkCode = EncodeAndStringTool.checkNull(phoneNum, code);
//                    if (checkCode) {
//                        String mdCode = encryptMD5ToString(phoneNum + encryptMD5ToString(code));
//                        long curTime = System.currentTimeMillis();
//                        String tsn = EncodeAndStringTool.getTsn(this);
//                        String salt = EncodeAndStringTool.generateRandomString(12);
//
//                        SharedPrefrenceTool.put(LoginActivity.this, "salt", salt);
//                        //devId+appId+v+stamp+mode+account+tsn+salt+ appSecret+mdpwd
//                        String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + LOGIN_MODE + phoneNum + tsn + salt + AppConst.APP_KEY + mdCode;
//                        //将拼接的字符串转化为16进制MD5
//                        String myCode = encryptMD5ToString(signString);
//                        /**
//                         * code值
//                         */
//                        String signCode = getCode(myCode);
//
//                        LoginInput loginInput = new LoginInput();
//                        loginInput.setMode(LOGIN_MODE);
//                        loginInput.setAccount(phoneNum);
//                        loginInput.setTsn(tsn);
//                        loginInput.setSalt(salt);
//                        loginInput.setDevId(AppConst.DEV_ID);
//                        loginInput.setAppId(AppConst.APP_ID);
//                        loginInput.setV(AppConst.V);
//                        loginInput.setStamp(curTime);
//                        loginInput.setAppVersion(APKVersionCodeTools.getVerName(LoginActivity.this));
//                        loginInput.setCode(signCode);
//                        String deviceId = SmAntiFraud.getDeviceId();
//                        loginInput.setDeviceId(deviceId);
//                        loadLogin(LoginActivity.this, loginInput);
//
//                    } else {
//                    }
//
//                } else if (LOGIN_MODE == PWD_LOGIN) {
                String password = etPassword.getText().toString();
                boolean checkPwd = EncodeAndStringTool.checkNull(phoneNum, password);
                if (checkPwd) {
                    String mdCode = encryptMD5ToString(phoneNum + encryptMD5ToString(password));
                    long curTime = System.currentTimeMillis();
                    String tsn = EncodeAndStringTool.getTsn(this);
                    String salt = EncodeAndStringTool.generateRandomString(12);

                    SharedPrefrenceTool.put(LoginActivity.this, "salt", salt);
                    //devId+appId+v+stamp+mode+account+tsn+salt+ appSecret+mdpwd
                    String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + 1 + phoneNum + tsn + salt + AppConst.APP_KEY + mdCode;
                    //将拼接的字符串转化为16进制MD5
                    String myCode = encryptMD5ToString(signString);
                    /**
                     * code值
                     */
                    String signCode = getCode(myCode);
                    LoginInput loginInput = new LoginInput();
                    loginInput.setMode(1);
                    loginInput.setAccount(phoneNum);
                    loginInput.setTsn(tsn);
                    loginInput.setSalt(salt);
                    loginInput.setDevId(AppConst.DEV_ID);
                    loginInput.setAppId(AppConst.APP_ID);
                    loginInput.setV(AppConst.V);
                    loginInput.setStamp(curTime);
                    loginInput.setAppVersion(APKVersionCodeTools.getVerName(LoginActivity.this));//app 当前版本号
                    loginInput.setCode(signCode);
                    String deviceId = SmAntiFraud.getDeviceId();
                    loginInput.setDeviceId(deviceId);
                    loadLogin(LoginActivity.this, loginInput);
                }
//                }
                break;
            case R.id.iv_wechat_login:
                wxLogin();
                finish();
                break;
            default:
                break;

        }
    }

    /**
     * 获取到验证码
     *
     * @param verficationCodeBean post json body
     */
//    private void getVerficationCode(InputVerficationCodeBean verficationCodeBean) {
//        ApiService apiService = BasePresenter.create(8000);
//        String inputbean = new Gson().toJson(verficationCodeBean);
//        Log.i(TAG, "loadLogin: " + verficationCodeBean.toString());
//        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
//        apiService.getCode(body)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<DataResponse<String>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                    }
//
//                    @Override
//                    public void onNext(DataResponse<String> loginResponse) {
//                        if (loginResponse.isSuccees()) {
//                            String sn = loginResponse.getDat();//验证码序列号
//                            ToastUtil.showToast(LoginActivity.this, "获取验证码成功");
//                            //显示60s倒计时
//                            btnGetCode1.setVisibility(View.GONE);
//                            tv60Second.setVisibility(View.VISIBLE);
//                            //60s走完之后,按钮显示,tv60Second隐藏
//                            tv60Second.setEnabled(false);
//                            new CountDownTimer(60 * 1000, 1000) {
//
//                                @Override
//                                public void onTick(long millisUntilFinished) {
//                                    tv60Second.setText(String.format("%d S", millisUntilFinished / 1000));
//                                }
//
//                                @Override
//                                public void onFinish() {
//                                    btnGetCode1.setVisibility(View.VISIBLE);
//                                    tv60Second.setVisibility(View.GONE);
//                                }
//                            }.start();
//                        } else {
//                            ErrorCodeTools.errorCodePrompt(LoginActivity.this, loginResponse.getErr(), loginResponse.getMsg());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //保存错误信息
//                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

    /**
     * 登录
     */
    public void loadLogin(final Context mContext, final LoginInput loginInput) {
        SaveUserInfo.getInstance(this).setUserInfo("account", loginInput.getAccount());
        final String account = SaveUserInfo.getInstance(this).getUserInfo("account");
        /**
         * 如果两次登录用户不是同一用户,则清空本地数据库中的消息表
         */
        if (!loginInput.getAccount().equals(account)) {
            DataSupport.deleteAll(Msg.class);//清空数据库中消息
        }
        ApiService apiService = BasePresenter.create(8000);
        String inputbean = new Gson().toJson(loginInput);
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
                                SharedPrefrenceTool.put(LoginActivity.this, "token", loginResp.getToken());
                                SharedPrefrenceTool.put(LoginActivity.this, "expire", loginResp.getExpire());//token的有效期
                                SharedPrefrenceTool.put(LoginActivity.this, "key", loginResp.getKey());//对称加密的秘钥。
                                SharedPrefrenceTool.put(LoginActivity.this, "bind", loginResp.getBind());//是否绑定用户。
                                SharedPrefrenceTool.put(LoginActivity.this, "random", loginResp.getRandom());//登录成果后，平台随机生成的字符串
                                AppConst.loadToken(LoginActivity.this);
                                //设置别名
                                JPushInterface.setAlias(LoginActivity.this, new Random().nextInt(), etPhoneNumber.getText().toString());

                                btnLogin.setEnabled(false);

                                Intent intent = new Intent(LoginActivity.this, HomePageActivity.class);
                                startActivity(intent);
                                finish();
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

    /**
     * 拉起微信授权页,调用微信登录界面
     */
    private void wxLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "diandi_wx_login";
        MyApplication.mWxApi.sendReq(req);
    }


    /**
     * 友盟统计
     */
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //防止内存泄漏
        UMShareAPI.get(this).release();
    }

    /**
     * 清空编辑框
     *
     * @param editText 需要清空的EditText控件
     * @param clearPic 清空数据的图片
     */
    protected void clearEditText(final EditText editText, final ImageView clearPic) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (editText.getText().length() != 0) {
                    clearPic.setVisibility(View.VISIBLE);
                } else {
                    clearPic.setVisibility(View.GONE);
                }
            }
        });
        /**
         * 焦点变化监听
         */
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (editText.getText().length() != 0 && hasFocus) {//不等于0,切得到焦点
                    clearPic.setVisibility(View.VISIBLE);
                    if (!EncodeAndStringTool.isStringEmpty(etPhoneNumber.getText().toString()) && !EncodeAndStringTool.isStringEmpty(etPassword.getText().toString())) {
                        btnLogin.setEnabled(true);
                        btnLogin.setTextColor(Color.parseColor("#ffffff"));
                    } else {
                        btnLogin.setEnabled(false);
                        btnLogin.setTextColor(Color.parseColor("#999999"));
                    }
                } else {
                    clearPic.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * 是否显示密码
     *
     * @param pwd
     */
    private void isShowPwd(EditText pwd) {
        int type = InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD;
        if (pwd.getInputType() == type) {
            pwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            pwd.setSelection(pwd.getText().length());
            if (etPassword.getText().length() != 0) {
                ivIsShowPwd.setImageResource(R.drawable.show_pwd);
            }
        } else {
            pwd.setInputType(type);
            pwd.setSelection(pwd.getText().length());
            ivIsShowPwd.setImageResource(R.drawable.no_show_pwd);
        }
    }
}


