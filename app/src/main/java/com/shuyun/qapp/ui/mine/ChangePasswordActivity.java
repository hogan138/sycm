package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.KeyboardUtils;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputVerficationCodeBean;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.manager.MyActivityManager;
import com.shuyun.qapp.utils.NetWorkUtils;
import com.shuyun.qapp.utils.RegularTool;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;

import org.litepal.LitePal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 修改密码
 */
public class ChangePasswordActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.iv_clear_phone_num)
    ImageView ivClearPhoneNum;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.btn_get_code1)
    Button btnGetCode1;
    @BindView(R.id.tv_60_second)
    TextView tv60Second;
    @BindView(R.id.ll_code_container)
    LinearLayout llCodeContainer;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_clear_pwd)
    ImageView ivClearPwd;
    @BindView(R.id.iv_is_show_pwd)
    ImageView ivIsShowPwd;
    @BindView(R.id.ll_password_container)
    LinearLayout llPasswordContainer;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.tv_new_psw)
    TextView tvNewPsw;//新密码
    private static final String TAG = "ChangePasswordActivity";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        tvCommonTitle.setText("修改密码");
        tvNewPsw.setText("新密码");

        etPhoneNumber.setText(SaveUserInfo.getInstance(this).getUserInfo("phone"));
        etPhoneNumber.setInputType(InputType.TYPE_NULL);

        clearEditText(etPassword, ivClearPwd);

        MyActivityManager.getInstance().pushOneActivity(this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_change_password;
    }

    @OnClick({R.id.iv_back, R.id.iv_clear_pwd,
            R.id.iv_is_show_pwd, R.id.btn_get_code1, R.id.btn_login, R.id.iv_clear_phone_num})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //关闭按钮,返回键效果
                finish();
                break;
            case R.id.iv_clear_phone_num:
                etPhoneNumber.setText("");
                ivClearPhoneNum.setVisibility(View.GONE);
                break;
            case R.id.iv_clear_pwd:
                etPassword.setText("");
                ivClearPwd.setVisibility(View.GONE);
                break;
            case R.id.iv_is_show_pwd:
                isShowPwd(etPassword);
                break;
            case R.id.btn_get_code1:
                String phoneNum0 = etPhoneNumber.getText().toString().trim();
                //隐藏按钮,显示60s倒计时
                if (RegularTool.isMobileExact(phoneNum0)) {//正则判断手机号的
                    long curTime0 = System.currentTimeMillis();
                    //devId+appId+v+stamp+phone+type+appSecret
                    String singString0 = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime0 + phoneNum0 + 4 + AppConst.APP_KEY;
                    //将拼接的字符串转化为16进制MD5
                    String myCode = encryptMD5ToString(singString0);
                    /**
                     * 签名code值
                     */
                    String signCode = getCode(myCode);
                    InputVerficationCodeBean verficationCodeBean = new InputVerficationCodeBean();
                    verficationCodeBean.setPhone(phoneNum0);
                    verficationCodeBean.setType(4);//type为4忘记密码
                    verficationCodeBean.setDevId(AppConst.DEV_ID);
                    verficationCodeBean.setAppId(AppConst.APP_ID);
                    verficationCodeBean.setV(AppConst.V);
                    verficationCodeBean.setStamp(curTime0);
                    verficationCodeBean.setCode(signCode);
                    if (NetWorkUtils.isNetworkConnected(mContext)) {
                        //调用获取验证码的接口
                        getVerficationCode(verficationCodeBean);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(this, "您输入的手机号格式有误,请重新输入!", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_login:
                String phoneNum = etPhoneNumber.getText().toString().trim();
                String code = etCode.getText().toString();
                String password = etPassword.getText().toString();
                boolean checkCode = EncodeAndStringTool.checkNull(phoneNum, code);
                if (checkCode) {
                    if (!EncodeAndStringTool.isStringEmpty(password)) {
                        getVerficationCode();
                    } else {
                        tvErrorHint.setVisibility(View.VISIBLE);
                        tvErrorHint.setText("密码不能为空！");
                    }

                } else {
                    tvErrorHint.setVisibility(View.VISIBLE);
                    tvErrorHint.setText("验证码不能为空！");
                }
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
                    String sn = loginResponse.getDat();//验证码序列号
                    ToastUtil.showToast(ChangePasswordActivity.this, "获取验证码成功");
                    btnGetCode1.setVisibility(View.GONE);
                    tv60Second.setVisibility(View.VISIBLE);
                    //60s走完之后,按钮显示,tv60Second隐藏
                    tv60Second.setEnabled(false);
                    new CountDownTimer(60 * 1000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            tv60Second.setText(String.format("%dS", millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            btnGetCode1.setVisibility(View.VISIBLE);
                            tv60Second.setVisibility(View.GONE);
                        }
                    }.start();
                } else {
                    ErrorCodeTools.errorCodePrompt(ChangePasswordActivity.this, loginResponse.getErr(), loginResponse.getMsg());
                }
            }
        });

    }

    /**
     * 修改密码
     */
    private void getVerficationCode() {
        String phoneNum = etPhoneNumber.getText().toString().trim();
        String code = etCode.getText().toString();
        String tsn = EncodeAndStringTool.getTsn(this);
        final String salt = EncodeAndStringTool.generateRandomString(12);
        long curTime = System.currentTimeMillis();
        String pwd = etPassword.getText().toString();

        //phone+短信验证码+phone+短信验证码，取前24位作为秘钥
        String key = phoneNum + code + phoneNum + code;
        String new_key = key.substring(0, 24);
        String iv = "SYKSC258";
        //DESede/CBC/PKCS5Padding
        String password = EncryptUtils.encrypt3DES2HexString(pwd.getBytes(), new_key.getBytes(), "DESede/CBC/PKCS5Padding", iv.getBytes());

        //devId+appId+v+stamp+phone+短信验证码+bin2hex(加密后的密码)+appSecret
        String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + phoneNum + code + password + AppConst.APP_KEY;
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
        String deviceId = SmAntiFraud.getDeviceId();

        RemotingEx.doRequest(RemotingEx.Builder().modifyPassWord(phoneNum,
                password,
                AppConst.DEV_ID,
                AppConst.APP_ID,
                salt,
                tsn,
                deviceId,
                APKVersionCodeTools.getVerName(this),
                AppConst.V,
                curTime,
                signCode), new OnRemotingCallBackListener<LoginResponse>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<LoginResponse> loginResponse) {
                LoginResponse changeResult = loginResponse.getDat();
                if (loginResponse.isSuccees()) {
                    if (!EncodeAndStringTool.isObjectEmpty(changeResult)) {
                        SharedPrefrenceTool.put(ChangePasswordActivity.this, "salt", salt);
                        SharedPrefrenceTool.put(ChangePasswordActivity.this, "token", changeResult.getToken());
                        SharedPrefrenceTool.put(ChangePasswordActivity.this, "expire", changeResult.getExpire());//token的有效期
                        SharedPrefrenceTool.put(ChangePasswordActivity.this, "key", changeResult.getKey());//对称加密的秘钥。
                        SharedPrefrenceTool.put(ChangePasswordActivity.this, "bind", changeResult.getBind());//是否绑定用户。
                        SharedPrefrenceTool.put(ChangePasswordActivity.this, "random", changeResult.getRandom());//登录成果后，平台随机生成的字符串
                        LoginResponse.User user = changeResult.getUser();

                        AppConst.loadToken(ChangePasswordActivity.this);
                        Toast.makeText(ChangePasswordActivity.this, "修改密码成功！", Toast.LENGTH_SHORT).show();

                        finish();
                        KeyboardUtils.hideSoftInput(ChangePasswordActivity.this);
                    } else {
                        tvErrorHint.setVisibility(View.VISIBLE);
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(ChangePasswordActivity.this, loginResponse.getErr(), loginResponse.getMsg());
                }
            }
        });
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
            ivIsShowPwd.setImageResource(R.mipmap.show_pwd);
        } else {
            pwd.setInputType(type);
            pwd.setSelection(pwd.getText().length());
            ivIsShowPwd.setImageResource(R.mipmap.no_show_pwd);
        }
    }
}
