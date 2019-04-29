package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.RemotingEx;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 收货地址列表
 */
public class AddressListActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_address_info)
    RecyclerView rvAddressInfo;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.tv_add_address)
    TextView tvAddAddress;


    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        tvCommonTitle.setText("我的收货地址");
        ivBack.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);


        //获取收货地址
        getAddressInfo();
    }

    //获取收货地址
    private void getAddressInfo() {
//        RemotingEx.doRequest(ApiServiceBean.queryIntegralCurrent(), new Object[]{currentPage}, this);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_address_list;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_add_address: //添加收货地址
                startActivity(new Intent(this, AddNewAddressActivity.class));
                break;
            default:
                break;
        }
    }
}
