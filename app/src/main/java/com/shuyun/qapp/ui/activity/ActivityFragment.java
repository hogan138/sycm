package com.shuyun.qapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.ActivityTabAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.homepage.MainAgainstActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.integral.IntegralMainActivity;
import com.shuyun.qapp.ui.mine.AddWithdrawInfoActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.RealNamePopupUtil;
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

import static com.blankj.utilcode.util.ActivityUtils.startActivity;

/**
 * 活动Fragment
 */
public class ActivityFragment extends Fragment {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    Unbinder unbinder;
    @BindView(R.id.rv_activity)
    RecyclerView rvActivity;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_activity_empty)
    ImageView ivActivityEmpty;
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    private Activity mContext;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    ActivityTabAdapter activityTabAdapter;

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
        tvCommonTitle.setText("活动专区");

        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();


    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
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
        if (isVisibleToUser) {
            loadInfo();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ActivityFragment");

        loadInfo();

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
                String action = resultBean.getBtnAction();
                String h5Url = resultBean.getH5Url();

                if (AppConst.GROUP.equals(action)) {
                    //题组
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", Integer.parseInt(resultBean.getContent().trim()));
                    intent.putExtra("h5Url", h5Url);
                    startActivity(intent);
                } else if (AppConst.REAL.equals(action)) {
                    //实名认证
                    startActivity(new Intent(mContext, RealNameAuthActivity.class));
                } else if (AppConst.H5.equals(action)) {
                    //h5
                    Intent intent = new Intent(mContext, WebBannerActivity.class);
                    intent.putExtra("url", h5Url);
                    intent.putExtra("name", "");//名称 标题
                    startActivity(intent);
                } else if (AppConst.INVITE.equals(action)) {
                    //邀请
                    Intent intent = new Intent();
                    intent.setClass(mContext, WebBannerActivity.class);
                    intent.putExtra("url", h5Url);
                    intent.putExtra("name", "邀请分享");
                    startActivity(intent);
                } else if (AppConst.INTEGRAL.equals(action)) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("cert")) == 1) {
                        //积分兑换
                        startActivity(new Intent(mContext, IntegralExchangeActivity.class));
                    } else {
                        RealNamePopupUtil.showAuthPop(mContext, llMain);
                    }
                } else if (AppConst.AGAINST.equals(action)) {
                    //答题对战
                    startActivity(new Intent(mContext, MainAgainstActivity.class));
                } else if (AppConst.TASK.equals(action)) {
                    //每日任务
                } else if (AppConst.DEFAULT.equals(action)) {
                    //默认
                } else if (AppConst.OPEN_BOX.equals(action)) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("cert")) == 1) {
                        //积分开宝箱
                        Intent intent = new Intent(mContext, WebPrizeBoxActivity.class);
                        intent.putExtra("main_box", "score_box");
                        intent.putExtra("h5Url", h5Url);
                        startActivity(intent);
                    } else {
                        RealNamePopupUtil.showAuthPop(mContext, llMain);
                    }
                } else if (AppConst.TREASURE.equals(action)) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("cert")) == 1) {
                        //积分夺宝
                        //保存规则地址
                        SaveUserInfo.getInstance(mContext).setUserInfo("h5_rule", h5Url);
                        startActivity(new Intent(mContext, IntegralMainActivity.class));
                    } else {
                        RealNamePopupUtil.showAuthPop(mContext, llMain);
                    }
                } else if (AppConst.WITHDRAW_INFO.equals(action)) {
                    //提现信息
                    startActivity(new Intent(mContext, AddWithdrawInfoActivity.class));
                } else if (AppConst.H5_EXTERNAL.equals(action)) {
                    //外部链接
                    Uri uri = Uri.parse(h5Url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }


            }
        });
        GridLayoutManager glManager = new GridLayoutManager(mContext, 1, LinearLayoutManager.VERTICAL, false);
        rvActivity.setLayoutManager(glManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ActivityFragment");
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

