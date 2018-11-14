package com.shuyun.qapp.ui.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.shuyun.qapp.adapter.PrizeAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ToastUtil;
import com.shuyun.qapp.view.RealNamePopupUtil;
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
 * 我的奖品使用中Fragment
 * ganquan
 */
public class UseInPrizeFragment extends Fragment {

    Unbinder unbinder;
    @BindView(R.id.rv_prize)
    RecyclerView rvPrize;
    @BindView(R.id.iv_prize_empty)
    ImageView ivPrizeEmpty;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    PrizeAdapter prizeAdapter; //奖品适配器

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public static UseInPrizeFragment newInstance(int certification) {
        Bundle args = new Bundle();
        args.putInt("certification", certification);
        UseInPrizeFragment fragment = new UseInPrizeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineFragment"); //统计页面，"MainScreen"为页面名称，可自定义
        loadMinePrize(5, currentPage);

        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_MORE;
                currentPage++;
                loadMinePrize(5, currentPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_REFRESH;
                currentPage = 0;
                loadMinePrize(5, currentPage);
            }
        });

        prizeAdapter = new PrizeAdapter(getActivity(), minePrizeList);
        prizeAdapter.setOnItemClickLitsener(new PrizeAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                MinePrize minePrize = minePrizeList.get(position);
                if (minePrize.getActionType().equals("action.use.props")) {
                    //道具（增次卡）
                    useAddCard(minePrize.getId());
                    minePrizeList.remove(position);
                    rvPrize.setAdapter(prizeAdapter);
                } else if (minePrize.getActionType().equals("action.use.loading") || minePrize.getActionType().equals("action.h5.url")) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(getActivity()).getUserInfo("cert")) == 1) {
                        //票务
                        Intent intent = new Intent(getActivity(), WebBannerActivity.class);
                        intent.putExtra("id", minePrize.getId());
                        intent.putExtra("url", minePrize.getH5Url());
                        intent.putExtra("name", minePrize.getName());
                        startActivity(intent);
                    } else {
                        RealNamePopupUtil.showAuthPop(getContext(), ((MinePrizeActivity) getActivity()).llPrize);
                    }
                } else if (minePrize.getActionType().equals("action.use.record")) {
                    //红包
                    if (Integer.parseInt(SaveUserInfo.getInstance(getActivity()).getUserInfo("cert")) == 1) {
                        startActivity(new Intent(getActivity(), CashResultActivity.class));
                    } else {
                        RealNamePopupUtil.showAuthPop(getContext(), ((MinePrizeActivity) getActivity()).llPrize);
                    }
                } else if (minePrize.getActionType().equals("action.open.box")) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(getActivity()).getUserInfo("cert")) == 1) {
                        //宝箱
                        Intent intent = new Intent(getActivity(), WebPrizeBoxActivity.class);
                        intent.putExtra("ChildMinePrize", minePrize);
                        intent.putExtra("main_box", "my_prize");
                        startActivity(intent);
                    } else {
                        RealNamePopupUtil.showAuthPop(getContext(), ((MinePrizeActivity) getActivity()).llPrize);
                    }
                } else {
                    //暂不支持 实物和电子卷 提示用户去下载新版本
                    showDownloadAppDialog();
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvPrize.setLayoutManager(layoutManager);

    }

    /**
     * 获取到我的奖品
     * 加上分页加载的功能
     *
     * @param status 1:未使用奖品;2:已使用奖品;3:已过期;0:全部奖品4:快过期
     * @param page
     */

    List<MinePrize> minePrizeList = new ArrayList<>();

    private void loadMinePrize(int status, int page) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getMyPrize(status, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())

                .subscribe(new Observer<DataResponse<List<MinePrize>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<MinePrize>> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            final List<MinePrize> minePrizeList1 = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(minePrizeList1) && minePrizeList1.size() > 0) {
                                ivPrizeEmpty.setVisibility(View.GONE);
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                                    minePrizeList.clear();
                                    minePrizeList.addAll(minePrizeList1);
                                    rvPrize.setAdapter(prizeAdapter);
                                    refreshLayout.finishRefresh();
                                    refreshLayout.setLoadmoreFinished(false);
                                    //进入动画
                                    LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetFromRight());
                                    controller.setDelay(0.3f);
                                    rvPrize.setLayoutAnimation(controller);
                                    rvPrize.scheduleLayoutAnimation();
                                } else if (loadState == AppConst.STATE_MORE) {
                                    if (minePrizeList1.size() == 0) {//没有数据了
                                        refreshLayout.finishLoadmore(); //
                                        refreshLayout.setLoadmoreFinished(true);
                                    } else {
                                        if (currentPage == 0) {
                                            minePrizeList.clear();
                                            minePrizeList.addAll(minePrizeList1);
                                            rvPrize.setAdapter(prizeAdapter);
                                            refreshLayout.finishRefresh();
                                        } else {
                                            minePrizeList.addAll(minePrizeList1);
                                            prizeAdapter.notifyDataSetChanged();
                                            refreshLayout.finishLoadmore();
                                            refreshLayout.setLoadmoreFinished(false);
                                        }
                                    }
                                }

                            } else {
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                                    ivPrizeEmpty.setVisibility(View.VISIBLE);
                                    refreshLayout.finishRefresh();
                                }
                                refreshLayout.finishLoadmore();
                                refreshLayout.setLoadmoreFinished(true);
                                currentPage = 0;
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(getActivity(), listDataResponse.getErr(), listDataResponse.getMsg());
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

    /**
     * 不支持使用的奖品类型,以后增加了会跳用户下载新版本的链接
     */
    private void showDownloadAppDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("提示")
                .setMessage("该版本暂不支持奖品的使用,请去下载最新版本？")
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                })
                .setNegativeButton("去更新", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Uri uri = Uri.parse("http://sj.qq.com/myapp/detail.htm?apkName=com.shuyun.qapp");
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);

                    }
                });

        builder.create();
        builder.show();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineFragment");
    }

    /**
     * 使用道具
     * 增次卡
     *
     * @param id
     */
    public void useAddCard(String id) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.addAnswerNum(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse dataResponse) {
                        if (dataResponse.isSuccees()) {
                            ToastUtil.showToast(getActivity(), "增次卡使用成功");
                        } else {
                            ErrorCodeTools.errorCodePrompt(getActivity(), dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                        return;
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
