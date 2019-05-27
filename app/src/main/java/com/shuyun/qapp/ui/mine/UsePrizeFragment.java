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
import com.shuyun.qapp.adapter.PrizeAdapter;
import com.shuyun.qapp.alipay.AlipayTradeManager;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.LockSkipUtils;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.RealNamePopupUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 我的奖品已使用Fragment
 * 2018/6/9
 * ganquan
 */
public class UsePrizeFragment extends Fragment implements OnRemotingCallBackListener<List<MinePrize>> {

    Unbinder unbinder;
    @BindView(R.id.rv_prize)
    RecyclerView rvPrize;
    @BindView(R.id.iv_prize_empty)
    ImageView ivPrizeEmpty;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    private PrizeAdapter prizeAdapter;

    /**
     * 获取到我的奖品 已使用
     *
     * @param status 1:未使用奖品;2:已使用奖品;3:已过期;0:全部奖品4:快过期
     */
    List<MinePrize> minePrizeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public static UsePrizeFragment newInstance(int certification) {
        Bundle args = new Bundle();
        args.putInt("certification", certification);
        UsePrizeFragment fragment = new UsePrizeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_MORE;
                currentPage++;
                loadMinePrize(2, currentPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_REFRESH;
                currentPage = 0;
                loadMinePrize(2, currentPage);
            }
        });

        prizeAdapter = new PrizeAdapter(getActivity(), minePrizeList);
        prizeAdapter.setOnItemClickLitsener(new PrizeAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                MinePrize minePrize = minePrizeList.get(position);
                String actionType = minePrize.getActionType();
                Long lock = minePrize.getLock();
                if (lock == 1) {
                    //锁定信息
                    LockSkipUtils.ExchangeTipDialog(getActivity(), minePrize);
                } else if (lock == 0) {
                    if (actionType.equals("action.alipay.coupon")) {
                        //使用优惠券
                        if (Integer.parseInt(SaveUserInfo.getInstance(getActivity()).getUserInfo("cert")) == 1) {
                            AlipayTradeManager.instance().showBasePage(getActivity(), minePrize.getH5Url());
                        } else {
                            RealNamePopupUtil.showAuthPop(getContext(), ((MinePrizeActivity) getActivity()).llPrize, getString(R.string.real_gift_describe));
                        }
                    }
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPrize.setLayoutManager(layoutManager);
        rvPrize.setAdapter(prizeAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadState = AppConst.STATE_NORMAL;
        currentPage = 0;
        loadMinePrize(2, currentPage);
    }

    private void loadMinePrize(int status, int page) {
        RemotingEx.doRequest(RemotingEx.Builder().getMyPrize(status, page), this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {
        if (currentPage == 0)
            refreshLayout.finishRefresh();
        else
            refreshLayout.finishLoadmore();
    }

    @Override
    public void onSucceed(String action, DataResponse<List<MinePrize>> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(getActivity(), response.getErr(), response.getMsg());
            return;
        }
        final List<MinePrize> minePrizeList1 = response.getDat();
        if (!EncodeAndStringTool.isListEmpty(minePrizeList1) && minePrizeList1.size() > 0) {
            ivPrizeEmpty.setVisibility(View.GONE);
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                minePrizeList.clear();
                minePrizeList.addAll(minePrizeList1);
                refreshLayout.finishRefresh();
                refreshLayout.setLoadmoreFinished(false);
            } else if (loadState == AppConst.STATE_MORE) {
                if (minePrizeList1.size() == 0) {//没有数据了
                    refreshLayout.finishLoadmore(); //
                    refreshLayout.setLoadmoreFinished(true);
                } else {
                    if (currentPage == 0) {
                        minePrizeList.clear();
                        minePrizeList.addAll(minePrizeList1);
                        refreshLayout.finishRefresh();
                    } else {
                        minePrizeList.addAll(minePrizeList1);
                        refreshLayout.finishLoadmore();
                        refreshLayout.setLoadmoreFinished(false);
                    }
                }
            }
            prizeAdapter.notifyDataSetChanged();
        } else {
            if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                ivPrizeEmpty.setVisibility(View.VISIBLE);
                refreshLayout.finishRefresh();
            }
            refreshLayout.finishLoadmore();
            refreshLayout.setLoadmoreFinished(true);
            currentPage = 0;
        }
    }
}
