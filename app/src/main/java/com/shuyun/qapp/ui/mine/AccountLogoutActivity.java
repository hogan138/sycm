package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.utils.OnMultiClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountLogoutActivity extends BaseActivity {

    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;//返回按钮可点击区域
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("账号注销");
    }

    /**
     * 设置布局
     *
     * @return
     */
    @Override
    public int intiLayout() {
        return R.layout.activity_account_logout;
    }


    @OnClick({R.id.iv_back, R.id.btn_logout_account})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back://返回按钮可点击区域
                finish();
                break;
            case R.id.btn_logout_account:
                logoutAccountDialog();
                break;
            default:
                break;
        }
    }

    /**
     * 账户注销弹窗
     */
    private void logoutAccountDialog() {
        new CircleDialog.Builder(this)
                .setTitle("账号注销确认")
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                        params.textSize = 40;
                    }
                })
                .setText("账号注销后,您已获得的奖金\n及奖品都将清零")
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.textSize = 40;
                        params.textColor = Color.parseColor("#666666");
                    }
                })
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setPositive("暂不注销", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

                    }
                })
                .setNegative("确认注销", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        finish();
                        startActivity(new Intent(AccountLogoutActivity.this, UploadLetterActivity.class));
                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#333333");
                    }
                })
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.animStyle = R.style.popwin_anim_style;
                    }
                })
                .show();
    }


}
