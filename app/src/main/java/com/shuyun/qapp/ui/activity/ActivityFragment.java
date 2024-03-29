package com.shuyun.qapp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.ActivityTabAdapter;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.view.ActionJumpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 活动Fragment
 */
public class ActivityFragment extends BaseFragment implements OnRemotingCallBackListener<ActivityTabBean> {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //标题文字
    Unbinder unbinder;
    @BindView(R.id.rv_activity)
    RecyclerView rvActivity; //recycleview
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout; //上拉加载、下拉刷新
    @BindView(R.id.iv_activity_empty)
    ImageView ivActivityEmpty; //空白图
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    private Activity mContext;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;
    private boolean isLoading = false;
    private Handler mHandler = new Handler();

    private ActivityTabAdapter activityTabAdapter; //活动适配器
    /**
     * 活动专区
     */
    private List<ActivityTabBean.ResultBean> activityTabBeanList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        tvCommonTitle.setText("活动专区");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLoading) {
                        init();
                    }
                    isLoading = true;

                    refresh();
                }
            }, 10);
        }
    }

    private void init() {
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_MORE;
                currentPage++;
                loadActivityList(currentPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_REFRESH;
                currentPage = 0;
                loadActivityList(currentPage);
            }
        });

        activityTabAdapter = new ActivityTabAdapter(mContext, activityTabBeanList);
        activityTabAdapter.setOnItemClickLitsener(new ActivityTabAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                ActivityTabBean.ResultBean resultBean = activityTabBeanList.get(position);
                LoginDataManager.instance().addData(LoginDataManager.ACTIVITY_LOGIN, resultBean);
                ActionJumpUtil.dialogSkip(resultBean.getBtnAction(),
                        mContext,
                        resultBean.getContent(),
                        resultBean.getH5Url(),
                        resultBean.getIsLogin());
            }
        });
        GridLayoutManager glManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.VERTICAL, false);
        rvActivity.setLayoutManager(glManager);
        rvActivity.setAdapter(activityTabAdapter);
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back: //返回
                if (mContext instanceof HomePageActivity) {
                    HomePageActivity homePageActivity = (HomePageActivity) mContext;
                    homePageActivity.radioGroupChange(0);
                }
                break;
            default:
                break;
        }
    }

    private void loadActivityList(int currentPage) {
        RemotingEx.doRequest(RemotingEx.Builder().ActivityList(String.valueOf(currentPage), "20"), this);
    }

    private void loadInfo() {
        loadState = AppConst.STATE_NORMAL;
        currentPage = 0;

        loadActivityList(currentPage);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void refresh() {
        //加载活动数据
        loadInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && (requestCode == AppConst.INVITE_CODE
                || requestCode == AppConst.GROUP_CODE
                || requestCode == AppConst.INTEGRAL_CODE
                || requestCode == AppConst.TREASURE_CODE
                || requestCode == AppConst.REAL_CODE
                || requestCode == AppConst.H5_CODE
                || requestCode == AppConst.AGAINST_CODE
                || requestCode == AppConst.OPEN_BOX_CODE
                || requestCode == AppConst.WITHDRAW_INFO_CODE
        )) {
            LoginDataManager.instance().handler(mContext, null);
        }
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
    public void onSucceed(String action, DataResponse<ActivityTabBean> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        ActivityTabBean activityTabBean = response.getDat();
        List<ActivityTabBean.ResultBean> activityTabBeanList1 = activityTabBean.getResult();
        if (!EncodeAndStringTool.isListEmpty(activityTabBeanList1) && activityTabBeanList1.size() > 0) {
            ivActivityEmpty.setVisibility(View.GONE);
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加载||下拉刷新
                activityTabBeanList.clear();
                activityTabBeanList.addAll(activityTabBeanList1);
                refreshLayout.finishRefresh();
                refreshLayout.setLoadmoreFinished(false);
            } else if (loadState == AppConst.STATE_MORE) {
                if (activityTabBeanList1.size() == 0) {//没有数据了
                    refreshLayout.finishLoadmore(); //
                    refreshLayout.setLoadmoreFinished(true);
                } else {
                    activityTabBeanList.addAll(activityTabBeanList1);
                    refreshLayout.finishLoadmore();
                    refreshLayout.setLoadmoreFinished(false);
                }
            }
            activityTabAdapter.notifyDataSetChanged();
        } else {
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                ivActivityEmpty.setVisibility(View.VISIBLE);
            }
            refreshLayout.finishLoadmore();
            refreshLayout.setLoadmoreFinished(true);
        }
    }
}

