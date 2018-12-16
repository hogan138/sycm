package com.shuyun.qapp.ui.mine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.PrizeAdapter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ToastUtil;
import com.shuyun.qapp.view.RealNamePopupUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 我的奖品未使用Fragment
 * 2018/6/9
 * ganquan
 */
public class NoUsePrizeFragment extends Fragment implements OnRemotingCallBackListener<Object> {

    Unbinder unbinder;
    @BindView(R.id.rv_prize)
    RecyclerView rvPrize;
    @BindView(R.id.iv_prize_empty)
    ImageView ivPrizeEmpty;

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.rl_prize)
    RelativeLayout rlPrize;//我的奖品界面

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    private PrizeAdapter prizeAdapter;
    /**
     * 获取到我的奖品
     * 加上分页加载的功能
     *
     * @param status 1:未使用奖品;2:已使用奖品;3:已过期;0:全部奖品4:快过期
     * @param page
     */
    List<MinePrize> minePrizeList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prize, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    public static NoUsePrizeFragment newInstance(int certification) {
        Bundle args = new Bundle();
        args.putInt("certification", certification);
        NoUsePrizeFragment fragment = new NoUsePrizeFragment();
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
                loadMinePrize(1, currentPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_REFRESH;
                currentPage = 0;
                loadMinePrize(1, currentPage);
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
                    prizeAdapter.notifyDataSetChanged();
                } else if (minePrize.getActionType().equals("action.h5.url")) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(getActivity()).getUserInfo("cert")) == 1) {
                        //票务
                        Intent intent = new Intent(getActivity(), WebH5Activity.class);
                        intent.putExtra("id", minePrize.getId());
                        intent.putExtra("url", minePrize.getH5Url());
                        intent.putExtra("name", minePrize.getName());
                        startActivity(intent);
                    } else {
                        RealNamePopupUtil.showAuthPop(getContext(), ((MinePrizeActivity) getActivity()).llPrize, getString(R.string.real_gift_describe));
                    }
                } else if (minePrize.getActionType().equals("action.withdraw")) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(getActivity()).getUserInfo("cert")) == 1) {
                        List<MinePrize> redPrizeList = new ArrayList<>();
                        //红包
                        for (int i = 0; i < minePrizeList.size(); i++) {
                            MinePrize minePrize1 = minePrizeList.get(i);
                            if (minePrizeList.get(i).getActionType().equals("action.withdraw")) {
                                redPrizeList.add(minePrize1);
                            }
                        }
                        if (!EncodeAndStringTool.isListEmpty(redPrizeList)) {
                            Intent intent = new Intent(getActivity(), NewRedWithdrawActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("redPrize", (ArrayList<? extends Parcelable>) redPrizeList);
                            bundle.putString("redId", minePrize.getId());
                            bundle.putString("from", "prize");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    } else {
                        RealNamePopupUtil.showAuthPop(getContext(), ((MinePrizeActivity) getActivity()).llPrize, getString(R.string.real_gift_describe));
                    }
                } else if (minePrize.getActionType().equals("action.use.record")) {
                    if (Integer.parseInt(SaveUserInfo.getInstance(getActivity()).getUserInfo("cert")) == 1) {
                        startActivity(new Intent(getActivity(), CashResultActivity.class));
                    } else {
                        RealNamePopupUtil.showAuthPop(getContext(), ((MinePrizeActivity) getActivity()).llPrize, getString(R.string.real_gift_describe));
                    }

                } else if (minePrize.getActionType().equals("action.open.box")) {
                    //宝箱
                    Intent intent = new Intent(getActivity(), WebPrizeBoxActivity.class);
                    intent.putExtra("ChildMinePrize", minePrize);
                    intent.putExtra("main_box", "my_prize");
                    startActivity(intent);
                } else {
                    //暂不支持 实物和电子卷 提示用户去下载新版本
                    showDownloadAppDialog();
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
        loadMinePrize(1, currentPage);
    }

    private void loadMinePrize(int status, int page) {
        RemotingEx.doRequest("getMyPrize", ApiServiceBean.getMyPrize(), new Object[]{status, page}, this);
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
        currentPage = 0;
    }

    /**
     * 使用道具
     * 增次卡
     *
     * @param id
     */
    public void useAddCard(String id) {
        RemotingEx.doRequest("addAnswerNum", ApiServiceBean.addAnswerNum(), new Object[]{id}, this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {
        if(currentPage == 0)
            refreshLayout.finishRefresh();
        else
            refreshLayout.finishLoadmore();
    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(getActivity(), response.getErr(), response.getMsg());
            return;
        }
        if ("getMyPrize".equals(action)) {
            final List<MinePrize> minePrizeList1 = (List<MinePrize>) response.getDat();
            if (!EncodeAndStringTool.isListEmpty(minePrizeList1) && minePrizeList1.size() > 0) {
                ivPrizeEmpty.setVisibility(View.GONE);
                rvPrize.setVisibility(View.VISIBLE);
                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加载||下拉刷新
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
                    rvPrize.setVisibility(View.INVISIBLE);
                    refreshLayout.finishRefresh();
                }
                refreshLayout.finishLoadmore();
                refreshLayout.setLoadmoreFinished(true);
                currentPage = 0;
            }
        } else if ("addAnswerNum".equals(action)) {
            ToastUtil.showToast(getActivity(), "增次卡使用成功");
        }
    }
}
