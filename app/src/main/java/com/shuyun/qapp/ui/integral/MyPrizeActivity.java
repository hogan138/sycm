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
import com.shuyun.qapp.adapter.MyPrizeAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.ExchangeMyPrizeBean;
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
 * 积分夺宝---我的奖券
 */

public class MyPrizeActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_my_prize)
    RecyclerView rvMyPrize;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;


    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    MyPrizeAdapter myPrizeAdapter; //我的奖券

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("我的奖券");
        ivBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finish();
            }
        });
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_my_prize;
    }

    @Override
    protected void onResume() {
        super.onResume();

        myPrizeAdapter = new MyPrizeAdapter(MyPrizeActivity.this, exchangeMyPrizeBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(MyPrizeActivity.this);
        rvMyPrize.setLayoutManager(manager);
        rvMyPrize.setNestedScrollingEnabled(false);

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
     * 查询我的奖券
     */
    List<ExchangeMyPrizeBean> exchangeMyPrizeBeanList = new ArrayList<>();

    private void loadIntegralCurrent() {

        RemotingEx.doRequest(RemotingEx.Builder().getMyTicket(currentPage), new OnRemotingCallBackListener<List<ExchangeMyPrizeBean>>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<List<ExchangeMyPrizeBean>> dataResponse) {
                if (dataResponse.isSuccees()) {
                    final List<ExchangeMyPrizeBean> exchangeMyPrizeBeanList1 = dataResponse.getDat();
                    if (!EncodeAndStringTool.isListEmpty(exchangeMyPrizeBeanList1)) {
                        ivEmpty.setVisibility(View.GONE);
                        if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                            exchangeMyPrizeBeanList.clear();
                            exchangeMyPrizeBeanList.addAll(exchangeMyPrizeBeanList1);
                            rvMyPrize.setAdapter(myPrizeAdapter);
                            refreshLayout.finishRefresh();
                            refreshLayout.setLoadmoreFinished(false);
                            //进入动画
                            LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetFromRight());
                            controller.setDelay(0.3f);
                            rvMyPrize.setLayoutAnimation(controller);
                            rvMyPrize.scheduleLayoutAnimation();
                        } else if (loadState == AppConst.STATE_MORE) {
                            if (exchangeMyPrizeBeanList1.size() == 0) {//没有数据了
                                refreshLayout.finishLoadmore();
                                refreshLayout.setLoadmoreFinished(true);
                            } else {
                                exchangeMyPrizeBeanList.addAll(exchangeMyPrizeBeanList1);
                                myPrizeAdapter.notifyDataSetChanged();
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
                    ErrorCodeTools.errorCodePrompt(MyPrizeActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                }
            }
        });


    }
}
