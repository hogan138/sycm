package com.shuyun.qapp.ui.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新增收货地址
 */
public class AddNewAddressActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rl_address)
    RelativeLayout rlAddress;
    @BindView(R.id.et_address_deatail)
    EditText etAddressDeatail;
    @BindView(R.id.tv_add_address)
    TextView tvAddAddress;
    @BindView(R.id.iv_default)
    ImageView ivDefault;

    private boolean is_default = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        tvCommonTitle.setText("添加收货地址");

        ivBack.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);
        ivDefault.setOnClickListener(this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_add_new_address;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_default:
                if (!is_default) {
                    ivDefault.setImageResource(R.mipmap.default_checked_logo);
                    is_default = true;
                } else {
                    ivDefault.setImageResource(R.mipmap.default_no_checked_logo);
                    is_default = false;
                }
                break;
            case R.id.tv_add_address:
                break;
            default:
                break;
        }
    }
}
