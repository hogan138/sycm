package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.ui.webview.WebPublicActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 现金提现
 */
public class NewCashWithdrawActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.tv_money)
    EditText tvMoney;
    @BindView(R.id.iv_add_user_info)
    ImageView ivAddUserInfo;
    @BindView(R.id.tv_name_account)
    TextView tvNameAccount;
    @BindView(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @BindView(R.id.tv_my_money)
    TextView tvMyMoney;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.btn_contact_our)
    Button btnContactOur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        rlBack.setOnClickListener(this);
        tvRule.setOnClickListener(this);
        ivAddUserInfo.setOnClickListener(this);


        SpannableString ss = new SpannableString("最低提现金额为50元");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint
        tvMoney.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_new_cash_withdraw;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_add_user_info:
                Intent intent = new Intent(NewCashWithdrawActivity.this, AddWithdrawInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_rule:
                Intent i = new Intent(NewCashWithdrawActivity.this, WebPublicActivity.class);
                i.putExtra("name", "cash_rule");
                startActivity(i);
                break;
            default:
                break;
        }
    }
}
