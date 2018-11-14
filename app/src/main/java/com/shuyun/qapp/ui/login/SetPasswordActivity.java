package com.shuyun.qapp.ui.login;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.EncryptUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.LoginResponse;
import com.shuyun.qapp.bean.Msg;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;

import org.litepal.crud.DataSupport;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 设置密码
 */
public class SetPasswordActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_rigth_title)
    TextView tvRigthTitle;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.iv_clear_pwd)
    ImageView ivClearPwd;
    @BindView(R.id.iv_is_show_pwd)
    ImageView ivIsShowPwd;
    @BindView(R.id.btn_finish)
    Button btnFinish;
    @BindView(R.id.tv_length)
    TextView tvLength;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        MyActivityManager.getInstance().pushOneActivity(this);
        btnFinish.setEnabled(false);
        clearEditText(etPassword, ivClearPwd);
        rlBack.setOnClickListener(this);
        ivClearPwd.setOnClickListener(this);
        ivIsShowPwd.setOnClickListener(this);
        tvRigthTitle.setOnClickListener(this);
        btnFinish.setOnClickListener(this);

        if (getIntent().getStringExtra("name").equals("changePwd")) {
            //忘记密码
            tvRigthTitle.setVisibility(View.GONE);
        } else if (getIntent().getStringExtra("name").equals("register")) {
            //注册
            tvRigthTitle.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_set_password;
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
            if (etPassword.getText().length() != 0) {
                ivIsShowPwd.setImageResource(R.drawable.show_pwd);
            }
        } else {
            pwd.setInputType(type);
            pwd.setSelection(pwd.getText().length());
            ivIsShowPwd.setImageResource(R.drawable.no_show_pwd);
        }
    }

    private void isLogin() {
        if (!EncodeAndStringTool.isStringEmpty(etPassword.getText().toString())) {
            if (etPassword.getText().length() > 3 && etPassword.getText().length() < 21) {
                //4-20位字符
                tvLength.setTextColor(Color.parseColor("#0194ec"));
                btnFinish.setEnabled(true);
                btnFinish.setTextColor(Color.parseColor("#ffffff"));
            } else {
                tvLength.setTextColor(Color.parseColor("#F05939"));
                btnFinish.setEnabled(false);
                btnFinish.setTextColor(Color.parseColor("#999999"));
            }
            //抖动动画
            if (etPassword.getText().length() > 20) {
                ObjectAnimator animator = ObjectAnimator.ofFloat(tvLength, "translationX", 0, 100, -100, 0);
                animator.setDuration(200);
                animator.start();
            }
        } else {
            btnFinish.setEnabled(false);
            btnFinish.setTextColor(Color.parseColor("#999999"));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_clear_pwd:
                etPassword.setText("");
                ivClearPwd.setVisibility(View.GONE);
                isLogin();
                break;
            case R.id.tv_rigth_title:
                startActivity(new Intent(SetPasswordActivity.this, HomePageActivity.class));
                MyActivityManager.getInstance().finishAllActivity();
                break;
            case R.id.iv_is_show_pwd:
                if (etPassword.getText().length() != 0) {
                    isShowPwd(etPassword);
                }
                break;
            case R.id.btn_finish:
                if (getIntent().getStringExtra("name").equals("changePwd")) {
                    //忘记密码
                    updatePwd();
                } else if (getIntent().getStringExtra("name").equals("register")) {
                    //注册
                    setPwd();
                }
                break;
            default:
                break;
        }
    }


    /**
     * 修改密码
     */
    private void updatePwd() {
        String phoneNum = getIntent().getStringExtra("phone");
        String code = getIntent().getStringExtra("code");
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
            DataSupport.deleteAll(Msg.class);//清空数据库中消息
        }
        String deviceId = SmAntiFraud.getDeviceId();
        ApiService apiService = BasePresenter.create(8000);
        apiService.modifyPassWord(phoneNum, password, AppConst.DEV_ID, AppConst.APP_ID, salt, tsn, deviceId, APKVersionCodeTools.getVerName(this), AppConst.V, curTime, signCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<LoginResponse>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<LoginResponse> loginResponse) {
                        LoginResponse changeResult = loginResponse.getDat();
                        if (loginResponse.isSuccees()) {
                            if (!EncodeAndStringTool.isObjectEmpty(changeResult)) {
                                SharedPrefrenceTool.put(SetPasswordActivity.this, "salt", salt);
                                SharedPrefrenceTool.put(SetPasswordActivity.this, "token", changeResult.getToken());
                                SharedPrefrenceTool.put(SetPasswordActivity.this, "expire", changeResult.getExpire());//token的有效期
                                SharedPrefrenceTool.put(SetPasswordActivity.this, "key", changeResult.getKey());//对称加密的秘钥。
                                SharedPrefrenceTool.put(SetPasswordActivity.this, "bind", changeResult.getBind());//是否绑定用户。
                                SharedPrefrenceTool.put(SetPasswordActivity.this, "random", changeResult.getRandom());//登录成果后，平台随机生成的字符串
                                AppConst.loadToken(SetPasswordActivity.this);

                                MyActivityManager.getInstance().finishAllActivity();
                                startActivity(new Intent(SetPasswordActivity.this, HomePageActivity.class));
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(SetPasswordActivity.this, loginResponse.getErr(), loginResponse.getMsg());
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


    //注册设置密码
    private void setPwd() {
        String phoneNum = getIntent().getStringExtra("phone");
        String pwd = etPassword.getText().toString();
        //phone+短信验证码+phone+短信验证码，取前24位作为秘钥
        String key = phoneNum + phoneNum + phoneNum;
        String new_key = key.substring(0, 24);
        String iv = "SYKSC258";
        //DESede/CBC/PKCS5Padding
        String password = EncryptUtils.encrypt3DES2HexString(pwd.getBytes(), new_key.getBytes(), "DESede/CBC/PKCS5Padding", iv.getBytes());

        ApiService apiService = BasePresenter.create(8000);
        apiService.setPwd(password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<String> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            MyActivityManager.getInstance().finishAllActivity();
                            startActivity(new Intent(SetPasswordActivity.this, HomePageActivity.class));
                        } else {
                            ErrorCodeTools.errorCodePrompt(SetPasswordActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
