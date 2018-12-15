package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 其他设置
 */
public class OtherSettingActivity extends BaseActivity {

    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;//返回按钮图片
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;//返回按钮可点击区域
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.rl_account_logout)
    RelativeLayout rlAccountLogout;

    /**
     * 设置布局
     *
     * @return
     */
    @Override
    public int intiLayout() {
        return R.layout.activity_other_setting;
    }


    @OnClick({R.id.iv_back, R.id.rl_account_logout})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回按钮可点击区域
                finish();
                break;
            case R.id.rl_account_logout://账户注销
                verifyCondition();
                break;
            default:
                break;
        }
    }

    //判断账户注销前置条件
    private void verifyCondition() {

        RemotingEx.doRequest(ApiServiceBean.verifyCondition(), new OnRemotingCallBackListener<Object>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<Object> loginResponse) {
                if (loginResponse.getErr().equals("00000")) {
                    startActivity(new Intent(OtherSettingActivity.this, AccountLogoutActivity.class));
                } else if (loginResponse.getErr().equals("CERT01")) {
                    Toast.makeText(OtherSettingActivity.this, "未实名认证，暂不支持注销账号", Toast.LENGTH_SHORT).show();
                } else if (loginResponse.getErr().equals("CERT02")) {
                    startActivity(new Intent(OtherSettingActivity.this, LogOutResultActivity.class));
                } else if (loginResponse.getErr().equals("CERT03")) {
                    Toast.makeText(OtherSettingActivity.this, "该账户近三个月内有修改密码或手机号记录", Toast.LENGTH_SHORT).show();
                } else if (loginResponse.getErr().equals("99999")) {
                    Toast.makeText(OtherSettingActivity.this, "接口返回未知错误", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("其他设置");
    }
}
