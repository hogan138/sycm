package com.shuyun.qapp.ui.found;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.FoundGiftExchangeAdapter;
import com.shuyun.qapp.adapter.FoundPropsExchangeAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.bean.HomeGroupsBean;
import com.shuyun.qapp.net.ApiServiceBean;
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
 * 发现---积分兑换
 */

public class IntegralExchangeActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_props)
    RecyclerView rvProps;
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;
    @BindView(R.id.rv_gift)
    RecyclerView rvGift;

    private List<GroupClassifyBean> groupClassifyBeans = new ArrayList<>();
    private FoundPropsExchangeAdapter foundPropsExchangeAdapter;
    private FoundGiftExchangeAdapter foundGiftExchangeAdapter;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = getApplicationContext();

        tvCommonTitle.setText("积分兑换");
        ivBack.setOnClickListener(this);
        rlMore.setOnClickListener(this);

        //获取数据
        loadHomeData();

        //初始化适配器
        foundPropsExchangeAdapter = new FoundPropsExchangeAdapter(groupClassifyBeans, mContext);
        foundPropsExchangeAdapter.setOnItemClickLitsener(new FoundPropsExchangeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.showToast(mContext, "兑换成功");
            }
        });
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager1.setSmoothScrollbarEnabled(true);
        gridLayoutManager1.setAutoMeasureEnabled(true);
        rvProps.setLayoutManager(gridLayoutManager1);
        //解决数据加载不完的问题
        rvProps.setHasFixedSize(true);
        rvProps.setNestedScrollingEnabled(false);
        rvProps.setAdapter(foundPropsExchangeAdapter);

        foundGiftExchangeAdapter = new FoundGiftExchangeAdapter(groupClassifyBeans, mContext);
        foundGiftExchangeAdapter.setOnItemClickLitsener(new FoundGiftExchangeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.showToast(mContext, "兑换成功");
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rvGift.setLayoutManager(gridLayoutManager);
        //解决数据加载不完的问题
        rvGift.setHasFixedSize(true);
        rvGift.setNestedScrollingEnabled(false);
        rvGift.setAdapter(foundGiftExchangeAdapter);

    }


    @Override
    public int intiLayout() {
        return R.layout.activity_integral_exchange2;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_more:
                break;
            default:
                break;
        }
    }

    /**
     * 获取题组列表
     */
    private void loadHomeData() {
        RemotingEx.doRequest("getHomeGroups", ApiServiceBean.getHomeGroups(), new Object[]{AppUtils.getAppVersionName()}, this);
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

        if ("getHomeGroups".equals(action)) {
            HomeGroupsBean homeGroupsBean = (HomeGroupsBean) response.getDat();
            if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getTree())) {
                final List<GroupClassifyBean> classifyBeans = homeGroupsBean.getTree();
                groupClassifyBeans.clear();
                groupClassifyBeans.addAll(classifyBeans);
                foundPropsExchangeAdapter.notifyDataSetChanged();
                foundGiftExchangeAdapter.notifyDataSetChanged();
            }
        }
    }
}
