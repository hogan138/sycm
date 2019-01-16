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
import butterknife.OnClick;

/**
 * 现金记录
 */
public class CashRecordActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_cash)
    TextView tvCash;
    @BindView(R.id.tv_all_cash)
    TextView tvAllCash;
    @BindView(R.id.btn_cash)
    Button btnCash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("现金余额");
        tvCash.setText("¥" + getIntent().getStringExtra("cash"));

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_check_account_record;
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }
}
