package com.shuyun.qapp.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.ActivityTabAdapter;
import com.shuyun.qapp.adapter.AnswerRecordAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.ActivityTabBean;
import com.shuyun.qapp.bean.AnswerRecordBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.answer.AnswerHistoryActivity;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.homepage.MainAgainstActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.mine.AnswerRecordActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
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
                    homePageActivity.changeFragment(0);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 用户答题记录
     */

    List<ActivityTabBean.ResultBean> activityTabBeanlist = new ArrayList<>();

    private void loadAnswerRecord(int currentPage) {
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

                                        //进入动画
                                        LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetScaleBig());
                                        controller.setDelay(0.1f);
                                        rvActivity.setLayoutAnimation(controller);
                                        rvActivity.scheduleLayoutAnimation();

                                    } else if (loadState == AppConst.STATE_MORE) {
                                        if (activityTabBeanlist1.size() == 0) {//没有数据了
                                            refreshLayout.finishLoadmore(); //
                                            refreshLayout.setLoadmoreFinished(true);
                                        } else {
                                            activityTabBeanlist.addAll(activityTabBeanlist1);
                                            activityTabAdapter.notifyDataSetChanged();
                                            refreshLayout.finishLoadmore();
                                            refreshLayout.setLoadmoreFinished(false);
                                            //进入动画
                                            LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetScaleBig());
                                            controller.setDelay(0.1f);
                                            rvActivity.setLayoutAnimation(controller);
                                            rvActivity.scheduleLayoutAnimation();
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
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ActivityFragment");

        loadState = AppConst.STATE_NORMAL;
        currentPage = 0;

        loadAnswerRecord(currentPage);

        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_MORE;
                currentPage++;
                loadAnswerRecord(currentPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_REFRESH;
                currentPage = 0;
                loadAnswerRecord(currentPage);
            }
        });

        activityTabAdapter = new ActivityTabAdapter(mContext, activityTabBeanlist);
        activityTabAdapter.setOnItemClickLitsener(new ActivityTabAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                ActivityTabBean.ResultBean resultBean = activityTabBeanlist.get(position);
                String action = resultBean.getBtnAction();
                String h5Url = resultBean.getH5Url();

                if ("action.group".equals(action)) {
                    //题组
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", Integer.parseInt(resultBean.getContent()));
                    intent.putExtra("h5Url", h5Url);
                    startActivity(intent);
                } else if ("action.real".equals(action)) {
                    //实名认证
                    startActivity(new Intent(mContext, RealNameAuthActivity.class));
                } else if ("action.h5".equals(action)) {
                    //h5
                    Intent intent = new Intent(mContext, WebBannerActivity.class);
                    intent.putExtra("url", h5Url);
                    intent.putExtra("name", "全民共进");//名称 标题
                    startActivity(intent);
                } else if ("action.invite".equals(action)) {
                    //邀请
                    Intent intent = new Intent();
                    intent.setClass(mContext, WebBannerActivity.class);
                    intent.putExtra("url", h5Url);
                    intent.putExtra("name", "邀请分享");
                    startActivity(intent);
                } else if ("action.integral".equals(action)) {
                    //积分兑换
                    startActivity(new Intent(mContext, IntegralExchangeActivity.class));
                } else if ("action.answer.against".equals(action)) {
                    //答题对战
                    startActivity(new Intent(mContext, MainAgainstActivity.class));
                } else if ("action.day.task".equals(action)) {
                    //每日任务

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

