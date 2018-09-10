package com.shuyun.qapp.ui.integral;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.IntegralAllPrizeAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.base.BaseSwipeBackActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.IntegralAllPrizeBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 积分夺宝首页
 */

public class IntegralMainActivity extends BaseSwipeBackActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.iv_gift_history)
    ImageView ivGiftHistory;
    @BindView(R.id.iv_my_gift)
    ImageView ivMyGift;
    @BindView(R.id.tv_all_gift)
    TextView tvAllGift;
    @BindView(R.id.rv_all_gift)
    RecyclerView rvAllGift;
    @BindView(R.id.tv_all_gift1)
    LinearLayout tvAllGift1;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    IntegralAllPrizeAdapter integralAllPrizeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        rlBack.setOnClickListener(this);
        ivGiftHistory.setOnClickListener(this);
        ivMyGift.setOnClickListener(this);
        tvRule.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();

        integralAllPrizeAdapter = new IntegralAllPrizeAdapter(IntegralMainActivity.this, integralAllPrizeBeanList);
        LinearLayoutManager manager = new LinearLayoutManager(IntegralMainActivity.this);
        rvAllGift.setLayoutManager(manager);
        rvAllGift.setNestedScrollingEnabled(false);

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
        return R.layout.activity_integral_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_rule:
                startActivity(new Intent(IntegralMainActivity.this, WebRuleActivity.class));
                break;
            case R.id.iv_gift_history:
                startActivity(new Intent(IntegralMainActivity.this, OpenPrizeHistoryActivity.class));
                break;
            case R.id.iv_my_gift:
                startActivity(new Intent(IntegralMainActivity.this, MyPrizeActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 查询全部奖品
     */
    List<IntegralAllPrizeBean> integralAllPrizeBeanList = new ArrayList<>();

    private void loadIntegralCurrent() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getAllPrize(currentPage)//分页加载0
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<IntegralAllPrizeBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<IntegralAllPrizeBean>> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            final List<IntegralAllPrizeBean> integralAllPrizeBeanList1 = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(integralAllPrizeBeanList1)) {
                                ivEmpty.setVisibility(View.GONE);
                                rvAllGift.setVisibility(View.VISIBLE);
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                                    integralAllPrizeBeanList.clear();
                                    integralAllPrizeBeanList.addAll(integralAllPrizeBeanList1);
                                    rvAllGift.setAdapter(integralAllPrizeAdapter);
                                    refreshLayout.finishRefresh();
                                    refreshLayout.setLoadmoreFinished(false);
                                    //进入动画
                                    LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetFromBottom());
                                    controller.setDelay(0.1f);
                                    rvAllGift.setLayoutAnimation(controller);
                                    rvAllGift.scheduleLayoutAnimation();
                                    if (integralAllPrizeBeanList1.size() <= 4) {
                                        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                            @Override
                                            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                                if (scrollY > ConvertUtils.dp2px(110)) {
                                                    tvAllGift.setVisibility(View.GONE);
                                                    tvAllGift1.setVisibility(View.VISIBLE);
                                                } else {
                                                    tvAllGift.setVisibility(View.VISIBLE);
                                                    tvAllGift1.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    } else {
                                        scrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                                            @Override
                                            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                                                if (scrollY > ConvertUtils.dp2px(130)) {
                                                    tvAllGift.setVisibility(View.GONE);
                                                    tvAllGift1.setVisibility(View.VISIBLE);
                                                } else {
                                                    tvAllGift.setVisibility(View.VISIBLE);
                                                    tvAllGift1.setVisibility(View.GONE);
                                                }
                                            }
                                        });
                                    }

                                } else if (loadState == AppConst.STATE_MORE) {
                                    if (integralAllPrizeBeanList1.size() == 0) {//没有数据了
                                        refreshLayout.finishLoadmore();
                                        refreshLayout.setLoadmoreFinished(true);
                                    } else {
                                        integralAllPrizeBeanList.addAll(integralAllPrizeBeanList1);
                                        integralAllPrizeAdapter.notifyDataSetChanged();
                                        refreshLayout.finishLoadmore();
                                        refreshLayout.setLoadmoreFinished(false);
                                    }
                                }

                            } else {
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                                    ivEmpty.setVisibility(View.VISIBLE);
                                    rvAllGift.setVisibility(View.INVISIBLE);
                                    refreshLayout.finishRefresh();
                                }
                                refreshLayout.finishLoadmore();
                                refreshLayout.setLoadmoreFinished(true);
                            }

                        } else {
                            ErrorCodeTools.errorCodePrompt(IntegralMainActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
}
