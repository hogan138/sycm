package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新增提现信息
 */
public class AddWithdrawInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_name)
    EditText tvName;
    @BindView(R.id.tv_money)
    EditText tvMoney;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.btn_contact_our)
    Button btnContactOur;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        rlBack.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        btnContactOur.setOnClickListener(this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_add_withdraw;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                KeyboardUtils.hideSoftInput(AddWithdrawInfoActivity.this);
                break;
            case R.id.btn_enter:
                Intent intent = new Intent(AddWithdrawInfoActivity.this, WithdrawResultActivity.class);
                intent.putExtra("content", "add");
                startActivity(intent);
                finish();
                break;
            case R.id.btn_contact_our:
                break;
            default:
                break;
        }
    }
}
