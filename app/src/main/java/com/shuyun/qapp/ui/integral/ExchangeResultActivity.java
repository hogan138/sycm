package com.shuyun.qapp.ui.integral;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 积分夺宝兑换结果页
 */
public class ExchangeResultActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_look)
    TextView tvLook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        tvCommonTitle.setText("兑换成功");
        ivBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finish();
            }
        });
        tvLook.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                SaveUserInfo.getInstance(ExchangeResultActivity.this).setUserInfo("exchange_result", "exchange_result");
                finish();
            }
        });
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_exchange_result;
    }
}
