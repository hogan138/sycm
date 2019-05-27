package com.shuyun.qapp.ui.integral;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.OpenPrizeHistoryAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.PrizeHistoryBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 积分夺宝----开奖历史
 */
public class OpenPrizeHistoryActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_open_prize_history)
    RecyclerView rvOpenPrizeHistory;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    OpenPrizeHistoryAdapter openPrizeHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        tvCommonTitle.setText("开奖历史");
        ivBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        openPrizeHistoryAdapter = new OpenPrizeHistoryAdapter(OpenPrizeHistoryActivity.this, prizeHistoryBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(OpenPrizeHistoryActivity.this);
        rvOpenPrizeHistory.setLayoutManager(manager);
        rvOpenPrizeHistory.setNestedScrollingEnabled(false);

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

    @Override
    public int intiLayout() {
        return R.layout.activity_open_prize_history;
    }

    /**
     * 查询开奖历史
     */
    List<PrizeHistoryBean> prizeHistoryBeanList = new ArrayList<>();

    private void loadIntegralCurrent() {

        RemotingEx.doRequest(RemotingEx.Builder().getPrizeHistory(currentPage), new OnRemotingCallBackListener<List<PrizeHistoryBean>>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<List<PrizeHistoryBean>> dataResponse) {
                if (dataResponse.isSuccees()) {
                    final List<PrizeHistoryBean> prizeHistoryBeanList1 = dataResponse.getDat();
                    if (!EncodeAndStringTool.isListEmpty(prizeHistoryBeanList1)) {
                        ivEmpty.setVisibility(View.GONE);
                        if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                            prizeHistoryBeanList.clear();
                            prizeHistoryBeanList.addAll(prizeHistoryBeanList1);
                            rvOpenPrizeHistory.setAdapter(openPrizeHistoryAdapter);
                            refreshLayout.finishRefresh();
                            refreshLayout.setLoadmoreFinished(false);
                            //进入动画
                            LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetFromLeft());
                            controller.setDelay(0.3f);
                            rvOpenPrizeHistory.setLayoutAnimation(controller);
                            rvOpenPrizeHistory.scheduleLayoutAnimation();
                        } else if (loadState == AppConst.STATE_MORE) {
                            if (prizeHistoryBeanList1.size() == 0) {//没有数据了
                                refreshLayout.finishLoadmore();
                                refreshLayout.setLoadmoreFinished(true);
                            } else {
                                prizeHistoryBeanList.addAll(prizeHistoryBeanList1);
                                openPrizeHistoryAdapter.notifyDataSetChanged();
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
                    ErrorCodeTools.errorCodePrompt(OpenPrizeHistoryActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                }
            }
        });

    }
}
