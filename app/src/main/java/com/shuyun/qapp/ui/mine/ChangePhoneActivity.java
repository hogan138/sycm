package com.shuyun.qapp.ui.mine;

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
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ToastUtil;
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
 * TODO更换绑定手机号
 */
public class ChangePhoneActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    private static final String TAG = "ChangePhoneActivity";
    @BindView(R.id.et_phone_num)
    EditText etPhoneNum;//输入的手机号
    @BindView(R.id.et_new_code)
    EditText etNewCode;//输入的验证码
    @BindView(R.id.btn_get_code2)
    Button btnGetCode2;//获取验证码按钮
    @BindView(R.id.tv_code_error2)
    TextView tvCodeError2;//验证码错误
    @BindView(R.id.btn_sure2)
    Button btnSure2;//确定绑定手机号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("更改新手机号");
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_change_phone;
    }

    @OnClick({R.id.iv_back, R.id.btn_get_code2, R.id.btn_sure2})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_get_code2:
                String phoneNum = etPhoneNum.getText().toString().trim();
                if (!EncodeAndStringTool.isStringEmpty(phoneNum)) {
                    //获取到验证码
                    long curTime0 = System.currentTimeMillis();
                    //devId+appId+v+stamp+phone+type+appSecret
                    String singString0 = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime0 + phoneNum + 6 + AppConst.APP_KEY;
                    //将拼接的字符串转化为16进制MD5
                    String myCode = encryptMD5ToString(singString0);
                    /**
                     * 签名code值
                     */
                    String signCode = getCode(myCode);
                    InputVerficationCodeBean verficationCodeBean = new InputVerficationCodeBean();
                    verficationCodeBean.setPhone(phoneNum);
                    verficationCodeBean.setType(6);
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
            case R.id.btn_sure2:
                bindNewPhoneNum();
                break;
            default:
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
                    public void onNext(DataResponse<String> codeResponse) {
                        if (codeResponse.isSuccees()) {
                            String sn = codeResponse.getDat();//验证码序号
                            if (!EncodeAndStringTool.isObjectEmpty(codeResponse)) {
                                ToastUtil.showToast(ChangePhoneActivity.this, "获取验证码成功");
                                btnGetCode2.setEnabled(false);
                                //倒计时
                                new CountDownTimer(60 * 1000, 1000) {
                                    @Override
                                    public void onTick(long millisUntilFinished) {
                                        btnGetCode2.setText(String.format("%d S", millisUntilFinished / 1000));

                                    }

                                    @Override
                                    public void onFinish() {
                                        btnGetCode2.setText("获取验证码");
                                        btnGetCode2.setEnabled(true);
                                    }
                                }.start();
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(ChangePhoneActivity.this, codeResponse.getErr(), codeResponse.getMsg());
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
     * 更改新手机号
     */
    private void bindNewPhoneNum() {
        final String phoneNumber = etPhoneNum.getText().toString().trim();
        String code = etNewCode.getText().toString().trim();
        if (!EncodeAndStringTool.checkNull(phoneNumber, code)) {
            ToastUtil.showToast(ChangePhoneActivity.this, "手机号或验证码不能为空");
        } else {
            ApiService apiService = BasePresenter.create(8000);
            apiService.verifynewphone(code, phoneNumber)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DataResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(DataResponse dataResponse) {
                            if (dataResponse.isSuccees()) {
                                //绑定成功
                                ToastUtil.showToast(ChangePhoneActivity.this, "更换手机号成功");
                                SaveUserInfo.getInstance(ChangePhoneActivity.this).setUserInfo("phone", phoneNumber);
                                finish();
                            } else if (dataResponse.getErr().equals("U0002")) {
                                ToastUtil.showToast(ChangePhoneActivity.this, "手机号码已经被其它用户绑定");
                            } else {
                                ToastUtil.showToast(ChangePhoneActivity.this, "更换手机号失败");
                                ErrorCodeTools.errorCodePrompt(ChangePhoneActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }

}
