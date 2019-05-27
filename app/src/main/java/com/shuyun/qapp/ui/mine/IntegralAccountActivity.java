package com.shuyun.qapp.ui.mine;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AccountRecordAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AccountBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 积分账户
 * 2018/6/9
 * ganquan
 */
public class IntegralAccountActivity extends BaseActivity implements OnRemotingCallBackListener<List<AccountBean>> {

    @BindView(R.id.rv_account_record)
    RecyclerView rvAccountRecord;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    private AccountRecordAdapter recordAdapter; //账户记录适配器
    /**
     * 查询积分流水记录
     */
    List<AccountBean> accountBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        tvCommonTitle.setText("积分明细");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        loadIntegralCurrent();//要做分页操作

        try {
            recordAdapter = new AccountRecordAdapter(this, accountBeanList, AppConst.ACCOUNT_INTEGRAL_TYPE);
            LinearLayoutManager manager = new LinearLayoutManager(this);
            rvAccountRecord.setLayoutManager(manager);
            rvAccountRecord.setAdapter(recordAdapter);

            refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
                @Override
                public void onLoadmore(RefreshLayout refreshlayout) {
                    loadState = AppConst.STATE_MORE;
                    currentPage++;
                    loadIntegralCurrent();
                }

                @Override
                public void onRefresh(RefreshLayout refreshlayout) {
                    loadState = AppConst.STATE_REFRESH;
                    currentPage = 0;
                    loadIntegralCurrent();
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_record_cash;
    }

    private void loadIntegralCurrent() {
        RemotingEx.doRequest(RemotingEx.Builder().queryIntegralCurrent(currentPage), this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {
        if (currentPage == 0) {
            refreshLayout.finishRefresh();
        } else {
            refreshLayout.finishLoadmore();
        }
    }

    @Override
    public void onSucceed(String action, DataResponse<List<AccountBean>> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(this, response.getErr(), response.getMsg());
            return;
        }
        List<AccountBean> accountBeanList1 = response.getDat();
        if (!EncodeAndStringTool.isListEmpty(accountBeanList1)) {
            ivEmpty.setVisibility(View.GONE);
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                accountBeanList.clear();
                accountBeanList.addAll(accountBeanList1);
                refreshLayout.finishRefresh();
                refreshLayout.setLoadmoreFinished(false);
            } else if (loadState == AppConst.STATE_MORE) {
                if (accountBeanList1.size() == 0) {//没有数据了
                    refreshLayout.finishLoadmore();
                    refreshLayout.setLoadmoreFinished(true);
                } else {
                    accountBeanList.addAll(accountBeanList1);
                    refreshLayout.finishLoadmore();
                    refreshLayout.setLoadmoreFinished(false);
                }
            }
            recordAdapter.notifyDataSetChanged();
        } else {
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                ivEmpty.setVisibility(View.VISIBLE);
                refreshLayout.finishRefresh();
            }
            refreshLayout.finishLoadmore();
            refreshLayout.setLoadmoreFinished(true);
        }
    }
}
