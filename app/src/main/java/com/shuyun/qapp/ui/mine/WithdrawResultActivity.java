package com.shuyun.qapp.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 现金提现成功页面
 */
public class WithdrawResultActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_rsult)
    TextView tvRsult;
    @BindView(R.id.btn_sure)
    Button btnSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("提现信息");

        ivBack.setOnClickListener(this);

        btnSure.setOnClickListener(this);

        String content = getIntent().getStringExtra("from");
        if ("add".equals(content)) {
            //添加信息
            tvRsult.setText("完善成功");
            btnSure.setText("完成");
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_withdraw_result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_sure:
                finish();
                break;
            default:
                break;
        }
    }
}
