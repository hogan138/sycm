package com.shuyun.qapp.ui.mine;

import android.content.Context;
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
import com.blankj.utilcode.util.KeyboardUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputVerficationCodeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.NetWorkUtils;
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
 * 更换绑定手机号
 */
public class ChangePhoneActivity extends BaseActivity implements OnRemotingCallBackListener {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
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

    private static final String TAG = "ChangePhoneActivity";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
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
                    StringBuilder sb = new StringBuilder();
                    sb.append(AppConst.DEV_ID)
                            .append(AppConst.APP_ID)
                            .append(AppConst.V)
                            .append(curTime0)
                            .append(phoneNum)
                            .append(6)
                            .append(AppConst.APP_KEY);
                    //将拼接的字符串转化为16进制MD5
                    String myCode = encryptMD5ToString(sb.toString());
                    String signCode = getCode(myCode);
                    InputVerficationCodeBean verficationCodeBean = new InputVerficationCodeBean();
                    verficationCodeBean.setPhone(phoneNum);
                    verficationCodeBean.setType(6);
                    verficationCodeBean.setDevId(AppConst.DEV_ID);
                    verficationCodeBean.setAppId(AppConst.APP_ID);
                    verficationCodeBean.setV(AppConst.V);
                    verficationCodeBean.setStamp(curTime0);
                    verficationCodeBean.setCode(signCode);
                    if (NetWorkUtils.isNetworkConnected(mContext)) {
                        //调用获取验证码的接口
                        getCodeNum(verficationCodeBean);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
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
     * 获取验证码
     */
    private void getCodeNum(InputVerficationCodeBean verficationCodeBean) {
        String inputbean = JSON.toJSONString(verficationCodeBean);
        Log.i(TAG, "loadLogin: " + verficationCodeBean.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest("getCode", RemotingEx.Builder().getCode(body), this);
    }

    /**
     * 更改新手机号
     */
    private void bindNewPhoneNum() {
        final String phoneNumber = etPhoneNum.getText().toString().trim();
        String code = etNewCode.getText().toString().trim();
        if (!EncodeAndStringTool.checkNull(phoneNumber, code)) {
            ToastUtil.showToast(this, "手机号或验证码不能为空");
            return;
        }
        RemotingEx.doRequest("verifynewphone", RemotingEx.Builder().verifynewphone(code, phoneNumber), this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse response) {
        if ("getCode".equals(action)) {
            if (response.isSuccees()) {
                ToastUtil.showToast(this, "获取验证码成功");
                btnGetCode2.setEnabled(false);
                //倒计时
                new CountDownTimer(60 * 1000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        btnGetCode2.setText(String.format("%dS", millisUntilFinished / 1000));
                    }

                    @Override
                    public void onFinish() {
                        btnGetCode2.setText("获取验证码");
                        btnGetCode2.setEnabled(true);
                    }
                }.start();
            }
        } else if ("verifynewphone".equals(action)) {
            final String phoneNumber = etPhoneNum.getText().toString().trim();
            if (response.isSuccees()) {
                //绑定成功
                ToastUtil.showToast(this, "更换手机号成功");
                SaveUserInfo.getInstance(this).setUserInfo("phone", phoneNumber);

                //隐藏输入法
                KeyboardUtils.hideSoftInput(this);
                finish();
            } else if ("U0002".equals(response.getErr())) {
                ToastUtil.showToast(this, response.getMsg());
            } else {
                ErrorCodeTools.errorCodePrompt(this, response.getErr(), response.getMsg());
            }
        }
    }
}
