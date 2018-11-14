package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputVerficationCodeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 更改手机号---验证老手机号
 */
public class ChangePhoneNumActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;//返回按钮
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.tv_phone_number)
    TextView tvPhoneNumber;//手机号
    @BindView(R.id.et_code)
    EditText etCode;//验证码
    @BindView(R.id.btn_get_code)
    Button btnGetCode;//获取到验证码
    @BindView(R.id.tv_code_error)
    TextView tvCodeError;//验证码错误提示
    @BindView(R.id.btn_next)
    Button btnNext;//确定按钮
    private static final String TAG = "ChangePhoneNumActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("更改手机号");
        Intent intent = getIntent();
        String phoneNum = intent.getStringExtra("bind_phone");
        tvPhoneNumber.setText(phoneNum);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_change_phone_num;
    }

    @OnClick({R.id.iv_back, R.id.btn_next, R.id.btn_get_code,})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_get_code:
                String phoneNum = tvPhoneNumber.getText().toString().trim();
                if (!EncodeAndStringTool.isStringEmpty(phoneNum)) {
                    //获取到验证码
                    long curTime0 = System.currentTimeMillis();
                    //devId+appId+v+stamp+phone+type+appSecret
                    String singString0 = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime0 + phoneNum + 5 + AppConst.APP_KEY;
                    //将拼接的字符串转化为16进制MD5
                    String myCode = encryptMD5ToString(singString0);
                    /**
                     * 签名code值
                     */
                    String signCode = getCode(myCode);
                    InputVerficationCodeBean verficationCodeBean = new InputVerficationCodeBean();
                    verficationCodeBean.setPhone(phoneNum);
                    verficationCodeBean.setType(5);
                    verficationCodeBean.setDevId(AppConst.DEV_ID);
                    verficationCodeBean.setAppId(AppConst.APP_ID);
                    verficationCodeBean.setV(AppConst.V);
                    verficationCodeBean.setStamp(curTime0);
                    verficationCodeBean.setCode(signCode);
                    if (NetworkUtils.isAvailableByPing()) {
                        //调用获取验证码的接口
                        getCodeNum(verficationCodeBean);
                    } else {
                        Toast.makeText(this, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    ToastUtil.showToast(this, "请输入正确的手机号");
                }
                break;
            case R.id.btn_next:
                verifyPhoneNum();
                break;
            default:
                Toast.makeText(this, "您做了什么操作？", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    /**
     * 获取到验证码
     */
    /**
     * 获取验证码
     *
     * @param verficationCodeBean post json body
     */
    private void getCodeNum(InputVerficationCodeBean verficationCodeBean) {
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
                            String sn = loginResponse.getDat();//验证码序列号
                            ToastUtil.showToast(ChangePhoneNumActivity.this, "获取验证码成功");
                            btnGetCode.setEnabled(false);
                            //倒计时
                            new CountDownTimer(60 * 1000, 1000) {

                                @Override
                                public void onTick(long millisUntilFinished) {
                                    btnGetCode.setText(String.format("%d S", millisUntilFinished / 1000));
                                }

                                @Override
                                public void onFinish() {
                                    btnGetCode.setText("获取验证码");
                                    btnGetCode.setEnabled(true);
                                }
                            }.start();
                        } else {
                            ErrorCodeTools.errorCodePrompt(ChangePhoneNumActivity.this, loginResponse.getErr(), loginResponse.getMsg());
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
     * 验证老手机号
     */
    private void verifyPhoneNum() {
        String phoneNumber = tvPhoneNumber.getText().toString().trim();
        String code = etCode.getText().toString().trim();
        if (!EncodeAndStringTool.checkNull(phoneNumber, code)) {
            ToastUtil.showToast(ChangePhoneNumActivity.this, "手机号或验证码不能为空");
        } else {
            ApiService apiService = BasePresenter.create(8000);
            apiService.verifyoldphone(code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DataResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(DataResponse dataResponse) {
                            if (dataResponse.getErr().equals("00000")) {
                                startActivity(new Intent(ChangePhoneNumActivity.this, ChangePhoneActivity.class));
                                finish();
                            } else if (dataResponse.getErr().equals("S0003")) {
                                ToastUtil.showToast(ChangePhoneNumActivity.this, "短信验证码不匹配！");
                            } else if (dataResponse.getErr().equals("S0002")) {
                                ToastUtil.showToast(ChangePhoneNumActivity.this, "短信验证码状态不正确！");
                            } else {
                                ToastUtil.showToast(ChangePhoneNumActivity.this, "验证手机号失败！");
                                ErrorCodeTools.errorCodePrompt(ChangePhoneNumActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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

    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
        StatService.onPause(this);
    }

}
