package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AddressListAdapter;
import com.shuyun.qapp.adapter.MyPropsAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AddressListBeans;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MyPropsBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 收货地址列表
 */
public class AddressListActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_address_info)
    RecyclerView rvAddressInfo;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.tv_add_address)
    TextView tvAddAddress;

    private Context mContext;

    private AddressListAdapter addressListAdapter;
    List<AddressListBeans> addressListBeansList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this;

        tvCommonTitle.setText("我的收货地址");
        ivBack.setOnClickListener(this);
        tvAddAddress.setOnClickListener(this);


        addressListAdapter = new AddressListAdapter(mContext, addressListBeansList);
        addressListAdapter.setEditorClickLitsener(new AddressListAdapter.OnEditorClickListener() {
            @Override
            public void onEditorClick(View view, int position) {
                //点击编辑
                AddressListBeans addressListBeans = addressListBeansList.get(position);
                Intent intent = new Intent(mContext, AddNewAddressActivity.class);
                intent.putExtra("from", "modify");
                intent.putExtra("address", addressListBeans);
                startActivity(intent);
            }
        });
        addressListAdapter.setOnItemClickLitsener(new AddressListAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                String from = getIntent().getStringExtra("from");
                if (!EncodeAndStringTool.isStringEmpty(from) && from.equals("goodsExchange")) {
                    //商品详情兑换选择地址
                    AddressListBeans addressListBeans = addressListBeansList.get(position);
                    Intent data = new Intent();
                    data.putExtra("address", addressListBeans);
                    setResult(RESULT_OK, data);
                    finish();
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvAddressInfo.setLayoutManager(layoutManager);
        rvAddressInfo.setAdapter(addressListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取收货地址
        getAddressInfo();
    }

    //获取收货地址
    private void getAddressInfo() {
        RemotingEx.doRequest("addressList", ApiServiceBean.getAddressList(), null, this);
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
                Intent intent = new Intent(mContext, AddNewAddressActivity.class);
                intent.putExtra("from", "add");
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        if ("addressList".equals(action)) {
            if (!EncodeAndStringTool.isObjectEmpty(response.getDat())) {
                List<AddressListBeans> addressListBeans = (List<AddressListBeans>) response.getDat();
                if (!EncodeAndStringTool.isListEmpty(addressListBeans) && addressListBeans.size() > 0) {
                    llEmpty.setVisibility(View.GONE);
                    rvAddressInfo.setVisibility(View.VISIBLE);
                    addressListBeansList.clear();
                    addressListBeansList.addAll(addressListBeans);
                    addressListAdapter.notifyDataSetChanged();
                } else {
                    llEmpty.setVisibility(View.VISIBLE);
                    rvAddressInfo.setVisibility(View.INVISIBLE);
                }
            } else {
                llEmpty.setVisibility(View.VISIBLE);
                rvAddressInfo.setVisibility(View.INVISIBLE);
            }

        }
    }
}
