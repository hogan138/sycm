package com.shuyun.qapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.webview.WebPublicActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager1;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RegularTool;
import com.shuyun.qapp.utils.SaveUserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 输入手机号
 */
public class RegisterPhoneActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.et_phone_number)
    EditText etPhoneNumber;
    @BindView(R.id.iv_clear_phone_num)
    ImageView ivClearPhoneNum;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.ll_agree_text)
    LinearLayout llAgreeText;

    private Context mContext;
    private boolean isRegister = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this;

        ivBack.setOnClickListener(this);
        llAgreeText.setOnClickListener(this);
        ivClearPhoneNum.setOnClickListener(this);
        btnNext.setOnClickListener(this);

        btnNext.setEnabled(false);

        MyActivityManager1.getInstance().pushOneActivity(this);

        clearEditText(etPhoneNumber, ivClearPhoneNum);

        if ("register".equals(getIntent().getStringExtra("name"))) {
            tvTitle.setText("新用户注册");
            isRegister = true;
        } else if ("login".equals(getIntent().getStringExtra("name"))) {
            tvTitle.setText("输入手机号");
            String phone = SaveUserInfo.getInstance(this).getUserInfo("login_phone");
            if (!EncodeAndStringTool.isStringEmpty(phone)) {
                etPhoneNumber.setText(phone);
                etPhoneNumber.setSelection(etPhoneNumber.length());
            }
        } else if ("changePwd".equals(getIntent().getStringExtra("name"))) {
            tvTitle.setText("输入手机号");
            llAgreeText.setVisibility(View.GONE);
            String phone = SaveUserInfo.getInstance(this).getUserInfo("login_phone");
            if (!EncodeAndStringTool.isStringEmpty(phone)) {
                etPhoneNumber.setText(phone);
                etPhoneNumber.setSelection(etPhoneNumber.length());
            }
        }

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_register_phone;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear_phone_num:
                etPhoneNumber.setText("");
                ivClearPhoneNum.setVisibility(View.GONE);
                isLogin();
                break;
            case R.id.ll_agree_text:
                //跳转到协议界面
                Intent i = new Intent(mContext, WebPublicActivity.class);
                i.putExtra("name", "useragree");
                startActivity(i);
                break;
            case R.id.btn_next:
                //判断是否已注册
                if (isRegister) {
                    btnNext.setEnabled(false);
                    registered();
                } else {
                    skip();
                }
                break;
            default:
                break;
        }
    }

    //跳过发送验证码
    private void skip() {
        final String account = etPhoneNumber.getText().toString();
        Intent intent = new Intent(mContext, VerifyCodeActivity.class);
        intent.putExtra("phone", account);
        intent.putExtra("name", getIntent().getStringExtra("name"));
        startActivity(intent);
    }

    private void registered() {
        final String account = etPhoneNumber.getText().toString();
        RemotingEx.doRequest(RemotingEx.Builder().registered(account), new OnRemotingCallBackListener<Object>() {
            @Override
            public void onCompleted(String action) {
                btnNext.setEnabled(true);
            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<Object> response) {
                if (!response.isSuccees()) {
                    ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
                    return;
                }
                skip();
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
                    isLogin();
                } else {
                    clearPic.setVisibility(View.GONE);
                    isLogin();
                }
            }
        });
    }

    private void isLogin() {
        if (!EncodeAndStringTool.isStringEmpty(etPhoneNumber.getText().toString())) {
            if (RegularTool.isMobileExact(etPhoneNumber.getText().toString()) && etPhoneNumber.getText().toString().length() == 11) {
                btnNext.setEnabled(true);
                btnNext.setTextColor(Color.parseColor("#ffffff"));
            } else {
                if (etPhoneNumber.getText().toString().length() == 11) {
                    errorDialog("手机号码格式错误请重新输入");
                }
                btnNext.setEnabled(false);
                btnNext.setTextColor(Color.parseColor("#999999"));
            }

        } else {
            btnNext.setEnabled(false);
            btnNext.setTextColor(Color.parseColor("#999999"));
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
}
