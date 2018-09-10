package com.shuyun.qapp.ui.mine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseSwipeBackActivity;
import com.shuyun.qapp.bean.OutPutWithdraw;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CashResultActivity extends BaseSwipeBackActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;//返回
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.iv_cash_result_icon)
    ImageView ivCashResultIcon;
    @BindView(R.id.tv_cash_hint)
    TextView tvCashHint;//提现结果提示
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

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }
}
