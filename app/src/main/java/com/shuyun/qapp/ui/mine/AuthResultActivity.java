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
import com.shuyun.qapp.bean.AuthNameBean;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 实名认证
 */
public class AuthResultActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.btn_sure)
    Button btnSure;
    @BindView(R.id.iv_auth_result_icon)
    ImageView ivAuthResultIcon;//实名结果图片
    @BindView(R.id.tv_auth_rsult)
    TextView tvAuthRsult;//认证结果提示信息


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("实名认证");
        AuthNameBean authNameBean = getIntent().getParcelableExtra("authName_result");
        if (!EncodeAndStringTool.isObjectEmpty(authNameBean)) {
            /**
             * 实名认证状态
             * 1:已认证
             * 2:审核中
             * 3:未通过
             */
            int status = authNameBean.getStatus();
            if (1 == status) {
                ivAuthResultIcon.setImageResource(R.mipmap.result_real_success);
                tvAuthRsult.setText("认证成功");
            } else if (3 == status) {
                ivAuthResultIcon.setImageResource(R.mipmap.result_real_fail);
                tvAuthRsult.setText("认证失败\n姓名和身份证号码不匹配");
            }
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_auth_info;
    }

    @OnClick({R.id.iv_back, R.id.btn_sure})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_sure:
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
        StatService.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
        StatService.onPause(this);
    }
}
