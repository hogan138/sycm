package com.shuyun.qapp.ui.mine;

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
import com.shuyun.qapp.utils.ErrorCodeTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 账户注销结果页
 */
public class LogOutResultActivity extends BaseActivity implements OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("账号注销");
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_log_out_result;
    }


    @OnClick({R.id.iv_back, R.id.btn_logout_sure, R.id.btn_cancel})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回按钮可点击区域
                finish();
                break;
            case R.id.btn_logout_sure:
                //确定
                finish();
                break;
            case R.id.btn_cancel: //撤销注销申请
                removeCondition();
                break;
            default:
                break;
        }
    }

    //撤回注销申请
    private void removeCondition() {
        RemotingEx.doRequest(ApiServiceBean.removeCondition(), this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(this, response.getErr(), response.getMsg());
            return;
        }
        if ("00000".equals(response.getErr())) {
            finish();
            Toast.makeText(this, "撤回注销申请成功！", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "撤回注销申请失败！", Toast.LENGTH_SHORT).show();
        }
    }
}
