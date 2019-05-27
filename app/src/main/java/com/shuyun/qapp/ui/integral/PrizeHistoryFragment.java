package com.shuyun.qapp.ui.integral;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.PrizeHistoryAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.ExchangeHistoryBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.RoundImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 积分夺宝----兑换记录
 */
public class PrizeHistoryFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_mine)
    TextView tvMine;
    @BindView(R.id.tv_winning)
    TextView tvWinning;
    @BindView(R.id.rv_record)
    RecyclerView rvRecord;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.iv_picture)
    RoundImageView ivPicture;
    @BindView(R.id.tv_load_more)
    TextView tvLoadMore;

    private int page = 0;

    private int type = 0;

    Unbinder unbinder;

    PrizeHistoryAdapter freeGroupAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.prize_history_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        tvAll.setOnClickListener(this);
        tvMine.setOnClickListener(this);
        tvWinning.setOnClickListener(this);
        tvLoadMore.setOnClickListener(this);

        rvRecord.setHasFixedSize(true);
        rvRecord.setNestedScrollingEnabled(false);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvRecord.setLayoutManager(manager);

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //显示
            page = 0;
            treasureUserChangeDataListBeanList.clear();
            if (SaveUserInfo.getInstance(getActivity()).getUserInfo("ScheduleStatus").equals("0")) {
                type = 0;
                tvLoadMore.setText("点击加载更多");
                //未开奖
                tvAll.setTextColor(Color.parseColor("#0194ec"));
                tvMine.setTextColor(Color.parseColor("#999999"));
                tvWinning.setTextColor(Color.parseColor("#999999"));
                freeGroupAdapter = new PrizeHistoryAdapter(treasureUserChangeDataListBeanList, getActivity());
                loadHomeGroups(type, page);
            } else {
                type = 2;
                tvLoadMore.setText("点击加载更多");
                //已开奖
                tvAll.setTextColor(Color.parseColor("#999999"));
                tvMine.setTextColor(Color.parseColor("#999999"));
                tvWinning.setTextColor(Color.parseColor("#0194ec"));
                freeGroupAdapter = new PrizeHistoryAdapter(treasureUserChangeDataListBeanList, getActivity());
                loadHomeGroups(type, page);
                getActivity().findViewById(R.id.ll_exchange).setVisibility(View.GONE);
                getActivity().findViewById(R.id.ll_look_result).setVisibility(View.GONE);
            }

        } else {
            try {
                if (SaveUserInfo.getInstance(getActivity()).getUserInfo("ScheduleStatus").equals("0")) {
                    //未开奖
                    getActivity().findViewById(R.id.ll_exchange).setVisibility(View.VISIBLE);
                } else {
                    //已开奖
                    getActivity().findViewById(R.id.ll_look_result).setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_all:
                page = 0;
                type = 0;
                tvLoadMore.setText("点击加载更多");
                tvAll.setTextColor(Color.parseColor("#0194ec"));
                tvMine.setTextColor(Color.parseColor("#999999"));
                tvWinning.setTextColor(Color.parseColor("#999999"));
                treasureUserChangeDataListBeanList.clear();
                freeGroupAdapter = new PrizeHistoryAdapter(treasureUserChangeDataListBeanList, getActivity());
                loadHomeGroups(type, page);
                break;
            case R.id.tv_mine:
                page = 0;
                type = 1;
                tvLoadMore.setText("点击加载更多");
                tvAll.setTextColor(Color.parseColor("#999999"));
                tvMine.setTextColor(Color.parseColor("#0194ec"));
                tvWinning.setTextColor(Color.parseColor("#999999"));
                treasureUserChangeDataListBeanList.clear();
                freeGroupAdapter = new PrizeHistoryAdapter(treasureUserChangeDataListBeanList, getActivity());
                loadHomeGroups(type, page);
                break;
            case R.id.tv_winning:
                page = 0;
                type = 2;
                tvLoadMore.setText("点击加载更多");
                tvAll.setTextColor(Color.parseColor("#999999"));
                tvMine.setTextColor(Color.parseColor("#999999"));
                tvWinning.setTextColor(Color.parseColor("#0194ec"));
                treasureUserChangeDataListBeanList.clear();
                freeGroupAdapter = new PrizeHistoryAdapter(treasureUserChangeDataListBeanList, getActivity());
                loadHomeGroups(type, page);
                break;
            case R.id.tv_load_more:
                page++;
                loadHomeGroups(type, page);
                break;
            default:
                break;
        }

    }


    /**
     * 奖券兑换记录
     */
    List<ExchangeHistoryBean.TreasureUserChangeDataListBean> treasureUserChangeDataListBeanList = new ArrayList<>();

    private void loadHomeGroups(final int type, final int page) {

        RemotingEx.doRequest(RemotingEx.Builder().ExchangeHistory(SaveUserInfo.getInstance(getActivity()).getUserInfo("scheduleId"), type, page), new OnRemotingCallBackListener<ExchangeHistoryBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<ExchangeHistoryBean> listDataResponse) {
                if (listDataResponse.isSuccees()) {
                    ExchangeHistoryBean groupAgainstBean = listDataResponse.getDat();

                    GlideUtils.LoadImage(getActivity(), groupAgainstBean.getMainPic(), ivPicture);
                    tvName.setText(groupAgainstBean.getPrizeName());
                    tvContent.setText(groupAgainstBean.getPrizePurpose());

                    if (groupAgainstBean.getScheduleStatus() == 0) {
                        //未开奖
                        tvWinning.setVisibility(View.GONE);
                    } else {
                        //已开奖
                        tvWinning.setVisibility(View.VISIBLE);
                        tvAll.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ConvertUtils.dp2px(50), 1));
                        tvMine.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ConvertUtils.dp2px(50), 1));
                        tvWinning.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ConvertUtils.dp2px(50), 2));
                    }

                    if (EncodeAndStringTool.isListEmpty(groupAgainstBean.getTreasureUserChangeDataList())) {
                        if (page == 0) {
                            rvRecord.setAdapter(freeGroupAdapter);
                        }
                        tvLoadMore.setText("没有更多数据了");
                    } else {
                        if (page == 0) {
                            treasureUserChangeDataListBeanList.addAll(groupAgainstBean.getTreasureUserChangeDataList());
                            rvRecord.setAdapter(freeGroupAdapter);
                            //进入动画
                            LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetFromTop());
                            controller.setDelay(0.1f);
                            rvRecord.setLayoutAnimation(controller);
                            rvRecord.scheduleLayoutAnimation();
                        } else {
                            treasureUserChangeDataListBeanList.addAll(groupAgainstBean.getTreasureUserChangeDataList());
                            freeGroupAdapter.notifyDataSetChanged();
                        }

                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(getActivity(), listDataResponse.getErr(), listDataResponse.getMsg());
                }
            }
        });


    }

}
