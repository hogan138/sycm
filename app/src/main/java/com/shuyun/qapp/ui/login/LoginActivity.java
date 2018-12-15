package com.shuyun.qapp.ui.login;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.KeyboardUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.ishumei.smantifraud.SmAntiFraud;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.LoginInput;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SyckApplication;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.MyActivityManager1;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.PermissionsChecker;
import com.shuyun.qapp.utils.RegularTool;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;

import org.litepal.crud.DataSupport;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
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
    @BindView(R.id.rl_register)
    RelativeLayout rlRegister;//注册
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.rl_close)
    RelativeLayout rlClose; //关闭
    @BindView(R.id.tv_weixin_logo)
    TextView tvWeixinLogo; //微信登录


    private static final String TAG = "LoginActivity";

    private Context mContext = null;

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码
    private Handler mHandler = new Handler();
    private int error = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);//输入法弹出布局上移
        mContext = this;
        btnLogin.setEnabled(false);
        mPermissionsChecker = new PermissionsChecker(this);
        clearEditText(etPhoneNumber, ivClearPhoneNum);
        clearEditText(etPassword, ivClearPwd);
        if (!SyckApplication.mWxApi.isWXAppInstalled()) {
            tvWeixinLogo.setVisibility(View.GONE);
        }

        MyActivityManager1.getInstance().pushOneActivity(this);

    }


    @Override
    public int intiLayout() {
        return R.layout.activity_login;
    }

    private void startPermissionsActivity() {
        try {
            PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
        } catch (Exception e) {

        }

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
        SharedPrefrenceTool.clear(mContext);//清空首选项中的数据

        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

        //手机号置为空
        SaveUserInfo.getInstance(mContext).setUserInfo("login_phone", "");

        /**
         * 焦点变化监听
         */
        etPhoneNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (etPhoneNumber.getText().length() != 0 && hasFocus) {//不等于0,切得到焦点
                } else {
                    if (RegularTool.isMobileExact(etPhoneNumber.getText().toString())) {

                    } else {
                        if (!EncodeAndStringTool.isStringEmpty(etPhoneNumber.getText().toString())) {
                            errorDialog("手机号码格式错误请重新输入");
                        }
                    }
                }
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

    @OnClick({R.id.et_phone_number, R.id.et_password, R.id.iv_clear_phone_num, R.id.iv_clear_pwd, R.id.rl_close,
            R.id.iv_is_show_pwd, R.id.tv_forget_pwd, R.id.btn_login, R.id.tv_weixin_logo, R.id.rl_register, R.id.tv_verify_login})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.rl_close:
                MyActivityManager.getInstance().finishAllActivity();
                finish();
                break;
            case R.id.iv_clear_phone_num: //清空手机号
                etPhoneNumber.setText("");
                ivClearPhoneNum.setVisibility(View.GONE);
                isLogin();
                break;
            case R.id.iv_clear_pwd://清空密码
                etPassword.setText("");
                ivClearPwd.setVisibility(View.GONE);
                isLogin();
                break;
            case R.id.iv_is_show_pwd: //密码显示隐藏切换
                isShowPwd(etPassword);
                break;
            case R.id.tv_verify_login:  //验证码登录
                //是否是答题免登陆，传入答卷id
                isAnswerLogin();
                if (!EncodeAndStringTool.isStringEmpty(etPhoneNumber.getText().toString())) {
                    if (RegularTool.isMobileExact(etPhoneNumber.getText().toString())) {
                        SaveUserInfo.getInstance(mContext).setUserInfo("login_phone", etPhoneNumber.getText().toString());
                    } else {
                        SaveUserInfo.getInstance(mContext).setUserInfo("login_phone", "");
                    }
                }
                Intent intent2 = new Intent(mContext, RegisterPhoneActivity.class);
                intent2.putExtra("name", "login");
                startActivity(intent2);
                break;
            case R.id.rl_register: //注册
                //是否是答题免登陆，传入答卷id
                isAnswerLogin();
                Intent intent1 = new Intent(mContext, RegisterPhoneActivity.class);
                intent1.putExtra("name", "register");
                startActivity(intent1);
                break;
            case R.id.tv_forget_pwd: //忘记密码
                //是否是答题免登陆，传入答卷id
                if (!EncodeAndStringTool.isStringEmpty(etPhoneNumber.getText().toString())) {
                    if (RegularTool.isMobileExact(etPhoneNumber.getText().toString())) {
                        SaveUserInfo.getInstance(mContext).setUserInfo("login_phone", etPhoneNumber.getText().toString());
                    } else {
                        SaveUserInfo.getInstance(mContext).setUserInfo("login_phone", "");
                    }
                }
                Intent intent = new Intent(mContext, RegisterPhoneActivity.class);
                intent.putExtra("name", "changePwd");
                startActivity(intent);
                break;
            case R.id.btn_login: //登录
                String phoneNum = etPhoneNumber.getText().toString().trim();
                String password = etPassword.getText().toString();
                boolean checkPwd = EncodeAndStringTool.checkNull(phoneNum, password);
                if (checkPwd) {
                    CustomLoadingFactory factory = new CustomLoadingFactory();
                    LoadingBar.make(rlMain, factory).show();

                    String mdCode = encryptMD5ToString(phoneNum + encryptMD5ToString(password));
                    long curTime = System.currentTimeMillis();
                    String tsn = EncodeAndStringTool.getTsn(this);
                    String salt = EncodeAndStringTool.generateRandomString(12);

                    SharedPrefrenceTool.put(mContext, "salt", salt);
                    //devId+appId+v+stamp+mode+account+tsn+salt+ appSecret+mdpwd
                    String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + 1 + phoneNum + tsn + salt + AppConst.APP_KEY + mdCode;
                    //将拼接的字符串转化为16进制MD5
                    String myCode = encryptMD5ToString(signString);

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
                    loginInput.setAppVersion(APKVersionCodeTools.getVerName(mContext));//app 当前版本号
                    loginInput.setCode(signCode);
                    String deviceId = SmAntiFraud.getDeviceId();
                    loginInput.setDeviceId(deviceId);
                    try {
                        //是否是答题免登陆，传入答卷id
                        String examId = getIntent().getStringExtra("examId");
                        if (!EncodeAndStringTool.isStringEmpty(examId)) {
                            loginInput.setExamId(examId);
                        }
                    } catch (Exception e) {

                    }
                    loadLogin(mContext, loginInput);
                }
                break;
            case R.id.tv_weixin_logo: //微信登录
                //是否是答题免登陆，传入答卷id
                isAnswerLogin();
                wxLogin();
                finish();
                break;
            default:
                break;

        }
    }


    //是否是答题免登陆，传入答卷id
    private void isAnswerLogin() {
        try {
            String examId = getIntent().getStringExtra("examId");
            if (!EncodeAndStringTool.isStringEmpty(examId)) {
                SaveUserInfo.getInstance(LoginActivity.this).setUserInfo("answer_exam_id", examId);
            }
        } catch (Exception e) {

        }

    }

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
        String inputBean = JSON.toJSONString(loginInput);
        Log.i(TAG, "loadLogin: " + loginInput.toString());

        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputBean);
        RemotingEx.doRequest(ApiServiceBean.login(), new Object[]{body}, new OnRemotingCallBackListener<LoginResponse>() {
            @Override
            public void onCompleted(String action) {
                LoadingBar.cancel(rlMain);
            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<LoginResponse> response) {
                if (response.isSuccees()) {
                    LoginResponse loginResp = response.getDat();
                    if (!EncodeAndStringTool.isObjectEmpty(loginResp)) {
                        SharedPrefrenceTool.put(mContext, "token", loginResp.getToken());
                        SharedPrefrenceTool.put(mContext, "expire", loginResp.getExpire());//token的有效期
                        SharedPrefrenceTool.put(mContext, "key", loginResp.getKey());//对称加密的秘钥。
                        SharedPrefrenceTool.put(mContext, "bind", loginResp.getBind());//是否绑定用户。
                        SharedPrefrenceTool.put(mContext, "random", loginResp.getRandom());//登录成果后，平台随机生成的字符串
                        SaveUserInfo.getInstance(mContext).setUserInfo("cert", loginResp.getCertification());
                        AppConst.loadToken(mContext);

                        //答题免登录返回宝箱id
                        if (!EncodeAndStringTool.isStringEmpty(loginResp.getBoxId())) {
                            SharedPrefrenceTool.put(mContext, "boxId", loginResp.getBoxId());
                        }

                        //设置别名
                        JPushInterface.setAlias(mContext, new Random().nextInt(), etPhoneNumber.getText().toString());
                        btnLogin.setEnabled(false);

                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                KeyboardUtils.hideSoftInput(LoginActivity.this);
                                try {
                                    setResult(RESULT_OK);
                                    MyActivityManager1.getInstance().finishAllActivity();
                                    finish();
                                } catch (Exception e) {

                                }
                            }
                        }, 320);
                    }
                } else {
                    if (response.getErr().equals("TAU11")) {
                        if (error == 0) {
                            error = 1;
                            errorDialog("密码错误请重新输入");
                        } else {
                            //再次输入错误密码
                            updatePasswordDialog();
                        }
                    } else {
                        if ("U0001".equals(response.getErr())) {
                            ToastUtil.showToast(mContext, "用户未注册");
                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
                        }
                    }
                }
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
        SyckApplication.mWxApi.sendReq(req);
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
                isLogin();
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
                    isLogin();
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
            ivIsShowPwd.setImageResource(R.drawable.show_pwd);
        } else {
            pwd.setInputType(type);
            pwd.setSelection(pwd.getText().length());
            ivIsShowPwd.setImageResource(R.drawable.no_show_pwd);
        }
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

    //重置密码弹框
    private void updatePasswordDialog() {
        new CircleDialog.Builder(this)
                .setText("密码错误，是否重置密码？")
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setPositive("重置", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Intent intent = new Intent(mContext, RegisterPhoneActivity.class);
                        intent.putExtra("name", "changePwd");
                        startActivity(intent);
                    }
                })
                .setNegative("忽略", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#999999");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyActivityManager.getInstance().finishAllActivity();
        finish();
    }
}


