package com.shuyun.qapp.ui.found;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.SizeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MarkBannerAdapter1;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.BannerBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.FoundDataBean;
import com.shuyun.qapp.bean.MainConfigBean;
import com.shuyun.qapp.bean.MarkBannerItem1;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.LoginDataManager;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.homepage.ActivityRegionManager;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.ui.mine.NoUsePrizeFragment;
import com.shuyun.qapp.ui.mine.UseInPrizeFragment;
import com.shuyun.qapp.ui.mine.UsePrizeFragment;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.LoginJumpUtil;
import com.shuyun.qapp.view.ViewPagerScroller;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;
import cn.kevin.banner.transformer.YZoomTransFormer;

/**
 * 活动Fragment
 */
public class FoundFragment extends BaseFragment implements OnRemotingCallBackListener<Object> {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //标题文字
    Unbinder unbinder;
    @BindView(R.id.banner)
    BannerViewPager mBannerView;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.activityRegion)
    LinearLayout activityRegion;
    @BindView(R.id.ll_found)
    LinearLayout llFound;

    private Activity mContext;

    private boolean isLoading = false;
    private Handler mHandler = new Handler();

    //banner
    private MarkBannerAdapter1 markBannerAdapter1;
    private List<IBannerItem> bannerList = new ArrayList<>();
    private List<FoundDataBean.BannerBean> bannerData = new ArrayList<>();
    private String bannerDataString = null;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_found, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        tvCommonTitle.setText("发现");
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (!isLoading) {
                        init();
                    }
                    isLoading = true;

                    refresh();
                }
            }, 10);
        }
    }

    private void init() {

        MarkBannerItem1 i = new MarkBannerItem1("https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/banner/xiaji.jpg");
        bannerList.add(i);
        //设置轮播图
        markBannerAdapter1 = new MarkBannerAdapter1(new GlideImageLoader(), mContext);
        markBannerAdapter1.setData(mContext, bannerList);
        mBannerView.setBannerAdapter(markBannerAdapter1);

        //设置index 在viewpager下面
        final ViewPager mViewpager = (ViewPager) mBannerView.getChildAt(0);
        //设置时间，时间越长，速度越慢
        ViewPagerScroller pagerScroller = new ViewPagerScroller(getActivity());
        pagerScroller.setScrollDuration(400);
        pagerScroller.initViewPagerScroll(mViewpager);

        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                int realPosition = position % bannerList.size();
                markBannerAdapter1.refreshAdConfig(bannerList.get(realPosition), realPosition);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        mBannerView.setBannerItemClick(new BannerViewPager.OnBannerItemClick<IBannerItem>() {
            @Override
            public void onClick(IBannerItem data) {
                for (int i = 0; i < bannerData.size(); i++) {
                    FoundDataBean.BannerBean bannerBean = bannerData.get(i);
                    if (data.ImageUrl().equals(bannerBean.getBaseImage())) {
                        LoginDataManager.instance().addData(LoginDataManager.BANNER_LOGIN, bannerBean);
                        String action = bannerBean.getBtnAction();
                        String h5Url = bannerBean.getH5Url();
                        Long is_Login = bannerBean.getIsLogin();
                        LoginJumpUtil.dialogSkip(action, mContext, bannerBean.getContent(), h5Url, is_Login);
                    }
                }
            }
        });
    }

    /**
     * 发现页数据
     */
    public void loadHomeInfo() {
        RemotingEx.doRequest("foundInfo", ApiServiceBean.foundInfo(), null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void refresh() {

        //发现页数据
        loadHomeInfo();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && (requestCode == AppConst.INVITE_CODE
                || requestCode == AppConst.GROUP_CODE
                || requestCode == AppConst.INTEGRAL_CODE
                || requestCode == AppConst.TREASURE_CODE
                || requestCode == AppConst.REAL_CODE
                || requestCode == AppConst.H5_CODE
                || requestCode == AppConst.AGAINST_CODE
                || requestCode == AppConst.OPEN_BOX_CODE
                || requestCode == AppConst.WITHDRAW_INFO_CODE
        )) {
            LoginDataManager.instance().handler(mContext, null);
        }
    }


    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {
    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }

        if ("foundInfo".equals(action)) {
            FoundDataBean foundDataBean = (FoundDataBean) response.getDat();

            bannerData = foundDataBean.getBanner();
            if (!EncodeAndStringTool.isListEmpty(bannerData)) {
                String str = JSON.toJSONString(bannerData);
                if (bannerDataString != null && bannerDataString.equals(str)) {
                    return;
                }
                bannerDataString = str;

                //设置轮播图
                bannerList.clear();
                for (int i = 0; i < bannerData.size(); i++) {
                    FoundDataBean.BannerBean bean = bannerData.get(i);
                    MarkBannerItem1 item = new MarkBannerItem1(bean.getBaseImage());
                    bannerList.add(item);
                }
                markBannerAdapter1.clearViews();
                markBannerAdapter1.setData(mContext, bannerList);
                //重新生成单位条
                mBannerView.setBannerAdapter(markBannerAdapter1);
            }

            //动态布局区域
            FoundDataBean.RegionBean regionBean = foundDataBean.getRegion();
            //动态添加布局
            activityRegion.removeAllViews();
            activityRegion.addView(ActivityRegionManager1.getView(mContext, regionBean, llFound));


            //动态tab添加
            mTitleList = new ArrayList<>();
            mFragmentList = new ArrayList<>();
            List<FoundDataBean.TablesBean> tablesBeanList = foundDataBean.getTables();
            for (int i = 0; i < tablesBeanList.size(); i++) {
                mTitleList.add(tablesBeanList.get(i).getTitle());
                if (tablesBeanList.get(i).getType() == 1) {
                    mFragmentList.add(WebFragment.newInstance(tablesBeanList.get(i).getH5Url()));
                } else if (tablesBeanList.get(i).getType() == 2) {
                    mFragmentList.add(new HotGroupFragment());
                }
            }

            //设置tablayout模式
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
            //tablayout获取集合中的名称
            tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
            tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
            tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));

            //初始化监听
            initFragment();


        }
    }


    private void initFragment() {

        //设置适配器
        vp.setAdapter(new MyPagerAdapter(getActivity().getSupportFragmentManager(), mFragmentList, mTitleList));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextColor(tabLayout.getTabTextColors());
                textView.setTextSize(19);
                textView.setTypeface(Typeface.DEFAULT_BOLD);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                if (null == view) {
                    tab.setCustomView(R.layout.custom_tab_layout_text);
                }
                TextView textView = tab.getCustomView().findViewById(android.R.id.text1);
                textView.setTextSize(15);
                textView.setTypeface(Typeface.DEFAULT);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //将tablayout与fragment关联
        tabLayout.setupWithViewPager(vp);

    }
}

