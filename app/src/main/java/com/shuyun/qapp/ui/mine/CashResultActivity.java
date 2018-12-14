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
 * 红包提现结果页
 */
public class CashResultActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;//返回
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.btn_sure3)
    Button btnSure3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("提现结果");
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_cash_result;
    }

    @OnClick({R.id.iv_back, R.id.btn_sure3})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_sure3:
                //暂时finish掉
                finish();
                break;
            default:
                break;
        }
    }
}
