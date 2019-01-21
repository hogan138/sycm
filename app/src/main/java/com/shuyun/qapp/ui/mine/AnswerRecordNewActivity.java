package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.ViewPagerAdapter;
import com.shuyun.qapp.animation.HorizontalStackPageTransformer;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.HistoryDataBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.ErrorCodeTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 成绩单
 */
public class AnswerRecordNewActivity extends BaseActivity implements ViewPager.OnPageChangeListener, OnRemotingCallBackListener<HistoryDataBean> {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.history)
    RelativeLayout rlHistory;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tvSelect)
    TextView tvSelect;
    @BindView(R.id.tvTotal)
    TextView tvTotal;
    @BindView(R.id.iv_guide)
    ImageView ivGuide;
    @BindView(R.id.backTop)
    LinearLayout backTop;
    @BindView(R.id.rl_guide)
    RelativeLayout rlGuide;

    private int currentPage = 0;
    private int pageSize = 50;
    private long totalR = 0;
    private ViewPagerAdapter pagerAdapter;
    private List<BaseFragment> baseList = new ArrayList<>();
    private Context mContext;
    private Handler mHandler = new Handler();
    private boolean isLoading = false;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        tvCommonTitle.setText("成绩单");

        ivEmpty.setVisibility(View.VISIBLE);
        rlHistory.setVisibility(View.GONE);

        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.setList(baseList);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);
        viewPager.setPageTransformer(true, new HorizontalStackPageTransformer(mContext, 2));

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadAnswerRecord(currentPage);
            }
        }, 0);

        //第一次进入显示引导
        sharedPreferences = mContext.getSharedPreferences("FirstRun", 0);
        Boolean first_run = sharedPreferences.getBoolean("Answer_record", true);
        if (first_run) {
            rlGuide.setVisibility(View.VISIBLE);
            TranslateAnimation animation = new TranslateAnimation(50, -50, 0, 0);
            animation.setInterpolator(new OvershootInterpolator());
            animation.setDuration(1500);
            animation.setRepeatCount(Animation.INFINITE);
            animation.setRepeatMode(Animation.REVERSE);
            ivGuide.startAnimation(animation);
        }

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_answer_record_new;
    }

    @OnClick({R.id.iv_back, R.id.btn_know, R.id.tv_common_title, R.id.backTop, R.id.rl_guide})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_guide:
            case R.id.btn_know:
                rlGuide.setVisibility(View.GONE);
                sharedPreferences.edit().putBoolean("Answer_record", false).apply();
                break;
            case R.id.tv_common_title:
            case R.id.backTop:
                viewPager.setCurrentItem(0);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {
        tvSelect.setText(String.valueOf(i + 1));

        if (baseList.size() == totalR)
            return;

        //提前几条数据时加载下一页
        if (baseList.size() - i == 3 && !isLoading) {
            loadAnswerRecord(currentPage);
        }

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    private void loadAnswerRecord(int currentPage) {
        isLoading = true;
        RemotingEx.doRequest(ApiServiceBean.getExamHistoryList(), new Object[]{currentPage, pageSize}, this);
    }

    @Override
    public void onCompleted(String action) {
        isLoading = false;
    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<HistoryDataBean> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        HistoryDataBean dat = response.getDat();
        totalR = dat.getTotal();

        if (totalR == 0) {
            ivEmpty.setVisibility(View.VISIBLE);
            rlHistory.setVisibility(View.GONE);
            return;
        } else {
            ivEmpty.setVisibility(View.GONE);
            rlHistory.setVisibility(View.VISIBLE);
        }
        currentPage++;

        tvTotal.setText(String.valueOf(totalR));

        List<HistoryDataBean.ResultBean> result = dat.getResult();
        for (HistoryDataBean.ResultBean bean : result) {
            HistoryFragment fragment = new HistoryFragment();
            fragment.setRecordBean(bean);
            baseList.add(fragment);
        }

        pagerAdapter.notifyDataSetChanged();
    }

}
