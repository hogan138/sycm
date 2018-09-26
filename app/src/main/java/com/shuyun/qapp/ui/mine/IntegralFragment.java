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
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;

import com.blankj.utilcode.util.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AccountRecordAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.bean.AccountBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 积分账户Fragment
 * <p>
 * 2018/6/9
 * <p>
 * ganquan
 */
public class IntegralFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.rv_account_record)
    RecyclerView rvAccountRecord;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    AccountRecordAdapter recordAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cash, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineFragment"); //统计页面，"MainScreen"为页面名称，可自定义

        recordAdapter = new AccountRecordAdapter(getActivity(), accountBeanList, AppConst.ACCOUNT_INTEGRAL_TYPE);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        rvAccountRecord.setLayoutManager(manager);

        loadIntegralCurrent();//要做分页操作

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

    /**
     * 查询积分流水记录
     */
    List<AccountBean> accountBeanList = new ArrayList<>();

    private void loadIntegralCurrent() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.queryIntegralCurrent(currentPage)//分页加载0
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<AccountBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<AccountBean>> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            final List<AccountBean> accountBeanList1 = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(accountBeanList1)) {
                                ivEmpty.setVisibility(View.GONE);
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                                    accountBeanList.clear();
                                    accountBeanList.addAll(accountBeanList1);
                                    rvAccountRecord.setAdapter(recordAdapter);
                                    refreshLayout.finishRefresh();
                                    refreshLayout.setLoadmoreFinished(false);
                                    //进入动画
                                    LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetScaleBig());
                                    controller.setDelay(0.1f);
                                    rvAccountRecord.setLayoutAnimation(controller);
                                    rvAccountRecord.scheduleLayoutAnimation();
                                } else if (loadState == AppConst.STATE_MORE) {
                                    if (accountBeanList1.size() == 0) {//没有数据了
                                        refreshLayout.finishLoadmore();
                                        refreshLayout.setLoadmoreFinished(true);
                                    } else {
                                        accountBeanList.addAll(accountBeanList1);
                                        recordAdapter.notifyDataSetChanged();
                                        refreshLayout.finishLoadmore();
                                        refreshLayout.setLoadmoreFinished(false);
                                    }
                                }

                            } else {
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                                    ivEmpty.setVisibility(View.VISIBLE);
                                    refreshLayout.finishRefresh();
                                }
                                refreshLayout.finishLoadmore();
                                refreshLayout.setLoadmoreFinished(true);
                            }

                        } else {
                            ErrorCodeTools.errorCodePrompt(getActivity(), dataResponse.getErr(), dataResponse.getMsg());
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
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
