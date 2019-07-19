package com.shuyun.qapp.ui.king;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的战绩
 */
public class MyRecordActivity extends BaseActivity {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        tvCommonTitle.setText("我的战绩");

    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_my_record;
    }
}
