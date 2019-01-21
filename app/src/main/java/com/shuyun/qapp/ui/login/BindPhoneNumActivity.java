package com.shuyun.qapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputVerficationCodeBean;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.webview.WebPublicActivity;
import com.shuyun.qapp.utils.AliPushBind;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager1;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 微信登录之后绑定手机号
 */
public class BindPhoneNumActivity extends BaseActivity {

    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;//手机号
    @BindView(R.id.et_code)
    EditText etCode; //验证码
    @BindView(R.id.btn_get_code1)
    Button btnGetCode1;//获取验证码
    @BindView(R.id.tv_60_second)
    TextView tv60Second;
    @BindView(R.id.ll_code_container)
    LinearLayout llCodeContainer;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.btn_confirm)
    Button btnConfirm;
    private static final String TAG = "BindPhoneNumActivity";
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;//返回键
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.ll_agree_text)
    LinearLayout llAgreeText;
    private String sn;
    /**
     * 微信登录成功返回值
     */
    private LoginResponse loginResp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("绑定微信");
        loginResp = (LoginResponse) getIntent().getSerializableExtra("login_response");
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_bind_phone_num;
    }

    @OnClick({R.id.iv_back, R.id.btn_get_code1, R.id.btn_confirm, R.id.ll_agree_text})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.ll_agree_text:
                //跳转到协议界面
                Intent i = new Intent(BindPhoneNumActivity.this, WebPublicActivity.class);
                i.putExtra("name", "useragree");
                startActivity(i);
                break;
            case R.id.btn_get_code1:
                String phoneNum = etPhoneNumber.getText().toString().trim();
                if (!EncodeAndStringTool.isStringEmpty(phoneNum)) {
                    long curTime0 = System.currentTimeMillis();
                    //devId+appId+v+stamp+phone+type+appSecret
                    String singString0 = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime0 + phoneNum + 3 + AppConst.APP_KEY;
                    //将拼接的字符串转化为16进制MD5
                    String myCode = encryptMD5ToString(singString0);
                    /**
                     * 签名code值
                     */
                    String signCode = getCode(myCode);
                    InputVerficationCodeBean verficationCodeBean = new InputVerficationCodeBean();
                    verficationCodeBean.setPhone(phoneNum);
                    verficationCodeBean.setType(3);//type为3表示登录
                    verficationCodeBean.setDevId(AppConst.DEV_ID);
                    verficationCodeBean.setAppId(AppConst.APP_ID);
                    verficationCodeBean.setV(AppConst.V);
                    verficationCodeBean.setStamp(curTime0);
                    verficationCodeBean.setCode(signCode);
                    if (NetworkUtils.isAvailableByPing()) {
                        //调用获取验证码的接口
                        getVerficationCode(verficationCodeBean);
                    } else {
                        Toast.makeText(this, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    tvErrorHint.setVisibility(View.VISIBLE);
                    tvErrorHint.setText("您还没有输入手机号,请您先输入手机号。");
                }
                break;
            case R.id.btn_confirm:
                String phoneNumber = etPhoneNumber.getText().toString().trim();
                String code = etCode.getText().toString().trim();
                if (!EncodeAndStringTool.checkNull(phoneNumber, code)) {
                    ToastUtil.showToast(BindPhoneNumActivity.this, "手机号或验证码不能为空");
                } else {
                    bindWx();
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
        RemotingEx.doRequest(ApiServiceBean.getCode(), new Object[]{body}, new OnRemotingCallBackListener<String>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<String> loginResponse) {
                if (loginResponse.isSuccees()) {
                    //验证码序列号
                    sn = loginResponse.getDat();
                    ToastUtil.showToast(BindPhoneNumActivity.this, "获取验证码成功");
                    btnGetCode1.setVisibility(View.GONE);
                    tv60Second.setVisibility(View.VISIBLE);
                    //60s走完之后,按钮显示,tv60Second隐藏
                    tv60Second.setEnabled(false);
                    new CountDownTimer(60 * 1000, 1000) {

                        @Override
                        public void onTick(long millisUntilFinished) {
                            tv60Second.setText(String.format("%d S", millisUntilFinished / 1000));
                        }

                        @Override
                        public void onFinish() {
                            btnGetCode1.setVisibility(View.VISIBLE);
                            tv60Second.setVisibility(View.GONE);
                        }
                    }.start();
                } else {
                    ErrorCodeTools.errorCodePrompt(BindPhoneNumActivity.this, loginResponse.getErr(), loginResponse.getMsg());
                }
            }
        });

    }

    /**
     * 绑定微信
     */
    private void bindWx() {
        final String phoneNumber = etPhoneNumber.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        if (!EncodeAndStringTool.checkNull(phoneNumber, code)) {
        } else {
            RemotingEx.doRequest(ApiServiceBean.bindWx(), new Object[]{phoneNumber, code}, new OnRemotingCallBackListener<Object>() {
                @Override
                public void onCompleted(String action) {

                }

                @Override
                public void onFailed(String action, String message) {

                }

                @Override
                public void onSucceed(String action, DataResponse<Object> dataResponse) {
                    if (dataResponse.isSuccees()) {
                        //阿里推送绑定别名
                        AliPushBind.bindPush();
                        AppConst.loadToken(SykscApplication.getAppContext());
                        KeyboardUtils.hideSoftInput(BindPhoneNumActivity.this);
                        //绑定成功  存token值
                        ToastUtil.showToast(BindPhoneNumActivity.this, "绑定成功");

                        if ("0".equals(SaveUserInfo.getInstance(BindPhoneNumActivity.this).getUserInfo("normal_login"))) {
                            //正常登录
                            startActivity(new Intent(BindPhoneNumActivity.this, HomePageActivity.class));
                            finish();
                        } else {
                            //启用游客模式
                            finish();
                        }

                    } else {
                        ErrorCodeTools.errorCodePrompt(BindPhoneNumActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                    }
                }
            });


        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(BindPhoneNumActivity.this, LoginActivity.class));
        finish();
    }
}
