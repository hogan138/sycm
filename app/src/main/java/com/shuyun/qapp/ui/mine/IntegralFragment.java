package com.shuyun.qapp.ui.mine;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AccountRecordAdapter;
import com.shuyun.qapp.bean.AccountBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 积分账户Fragment
 * 2018/6/9
 * ganquan
 */
public class IntegralFragment extends Fragment implements OnRemotingCallBackListener<Object> {

    Unbinder unbinder;
    @BindView(R.id.rv_account_record)
    RecyclerView rvAccountRecord;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    private AccountRecordAdapter recordAdapter; //账户记录适配器
    /**
     * 查询积分流水记录
     */
    List<AccountBean> accountBeanList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recordAdapter = new AccountRecordAdapter(getActivity(), accountBeanList, AppConst.ACCOUNT_INTEGRAL_TYPE);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
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
    }

    @Override
    public void onResume() {
        super.onResume();
        loadState = AppConst.STATE_NORMAL;
        currentPage = 0;
        loadIntegralCurrent();//要做分页操作
    }

    private void loadIntegralCurrent() {
        RemotingEx.doRequest(ApiServiceBean.queryIntegralCurrent(), new Object[]{currentPage}, this);
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
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(getActivity(), response.getErr(), response.getMsg());
            return;
        }
        List<AccountBean> accountBeanList1 = (List<AccountBean>) response.getDat();
        if (!EncodeAndStringTool.isListEmpty(accountBeanList1)) {
            ivEmpty.setVisibility(View.GONE);
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                accountBeanList.clear();
                accountBeanList.addAll(accountBeanList1);
                refreshLayout.finishRefresh();
                refreshLayout.setLoadmoreFinished(false);
                //进入动画
                //LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetScaleBig());
                //controller.setDelay(0.1f);
                //rvAccountRecord.setLayoutAnimation(controller);
                //rvAccountRecord.scheduleLayoutAnimation();
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
