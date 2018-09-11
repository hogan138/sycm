package com.shuyun.qapp.ui.login;

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

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        ivBack.setOnClickListener(this);
        llAgreeText.setOnClickListener(this);

        if ("login".equals(getIntent().getStringExtra("name"))) {
            tvTitle.setText("新用户注册");
        }

        etPhoneNumber.addTextChangedListener(new TextWatcher() {//没有输入正确格式的手机号,登录按钮置灰
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!EncodeAndStringTool.isStringEmpty(etPhoneNumber.getText().toString())) {
                    btnNext.setEnabled(true);
                    btnNext.setTextColor(Color.parseColor("#ffffff"));
                } else {
                    btnNext.setEnabled(false);
                    btnNext.setTextColor(Color.parseColor("#999999"));
                }
            }
        });
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
            case R.id.ll_agree_text:

                break;
            default:
                break;
        }
    }
}
