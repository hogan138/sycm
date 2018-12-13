package com.shuyun.qapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.blankj.utilcode.util.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.ActivityTabAdapter;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.view.LoginJumpUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 活动Fragment
 */
public class ActivityFragment extends BaseFragment {

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

    private ActivityTabAdapter activityTabAdapter; //活动适配器
    private ActivityTabBean.ResultBean selectedItem = null;

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

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back: //返回
                if (mContext instanceof HomePageActivity) {
                    HomePageActivity homePageActivity = (HomePageActivity) mContext;
                    homePageActivity.changeUi(0);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 活动专区
     */
    List<ActivityTabBean.ResultBean> activityTabBeanlist = new ArrayList<>();

    private void loadActivityList(int currentPage) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.ActivityList(String.valueOf(currentPage), "20")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<ActivityTabBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<ActivityTabBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            ActivityTabBean activityTabBean = dataResponse.getDat();
                            List<ActivityTabBean.ResultBean> activityTabBeanlist1 = activityTabBean.getResult();
                            if (!EncodeAndStringTool.isListEmpty(activityTabBeanlist1) && activityTabBeanlist1.size() > 0) {
                                try {
                                    ivActivityEmpty.setVisibility(View.GONE);
                                    if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加载||下拉刷新
                                        activityTabBeanlist.clear();
                                        activityTabBeanlist.addAll(activityTabBeanlist1);
                                        rvActivity.setAdapter(activityTabAdapter);
                                        refreshLayout.finishRefresh();
                                        refreshLayout.setLoadmoreFinished(false);

                                    } else if (loadState == AppConst.STATE_MORE) {
                                        if (activityTabBeanlist1.size() == 0) {//没有数据了
                                            refreshLayout.finishLoadmore(); //
                                            refreshLayout.setLoadmoreFinished(true);
                                        } else {
                                            activityTabBeanlist.addAll(activityTabBeanlist1);
                                            activityTabAdapter.notifyDataSetChanged();
                                            refreshLayout.finishLoadmore();
                                            refreshLayout.setLoadmoreFinished(false);
                                        }
                                    }
                                } catch (Exception e) {
                                }
                            } else {
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                                    ivActivityEmpty.setVisibility(View.VISIBLE);
                                }
                                refreshLayout.finishLoadmore();
                                refreshLayout.setLoadmoreFinished(true);
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!isVisibleToUser)
            return;
        refresh();
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ActivityFragment");
    }

    private void loadInfo() {
        loadState = AppConst.STATE_NORMAL;
        currentPage = 0;

        loadActivityList(currentPage);

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

        activityTabAdapter = new ActivityTabAdapter(mContext, activityTabBeanlist);
        activityTabAdapter.setOnItemClickLitsener(new ActivityTabAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                ActivityTabBean.ResultBean resultBean = activityTabBeanlist.get(position);
                selectedItem = resultBean;
                LoginJumpUtil.dialogSkip(resultBean.getBtnAction(),
                        mContext,
                        resultBean.getContent(),
                        resultBean.getH5Url(),
                        resultBean.getIsLogin(),
                        null);
            }
        });
        GridLayoutManager glManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.VERTICAL, false);
        rvActivity.setLayoutManager(glManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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
                || requestCode == AppConst.TREASURE_CODE)) {
            LoginJumpUtil.dialogSkip(selectedItem.getBtnAction(),
                    mContext,
                    selectedItem.getContent(),
                    selectedItem.getH5Url(),
                    selectedItem.getIsLogin(),
                    null);
        }
    }
}

