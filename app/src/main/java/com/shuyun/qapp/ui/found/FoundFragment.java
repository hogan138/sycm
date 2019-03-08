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
    private int bannerWidth = 0, bannerHeight = 0, bannerTotalHeight = 0;

    //banner
    private MarkBannerAdapter1 markBannerAdapter1;
    private List<IBannerItem> bannerList = new ArrayList<>();
    private List<BannerBean> bannerData = new ArrayList<>();
    private String bannerDataString = null;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;
    private int status = 0;
    private MineBean mineBean;

    private String mainConfigBeanString = null;

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

        loadMineHomeData();

        //获取屏幕宽度
//        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
//        bannerWidth = (int) Math.ceil(dm.widthPixels) - SizeUtils.dp2px(40);
//        bannerHeight = (int) Math.ceil(bannerWidth * (260f / 750f));
//        bannerTotalHeight = bannerHeight + SizeUtils.dp2px(20);

        //banner设置
//        ViewGroup.LayoutParams bannerParams = mBannerView.getLayoutParams();
//        bannerParams.height = bannerTotalHeight;
//        mBannerView.setLayoutParams(bannerParams);

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
                    BannerBean bannerBean = bannerData.get(i);
                    if (data.ImageUrl().equals(bannerBean.getPicture())) {
                        LoginDataManager.instance().addData(LoginDataManager.BANNER_LOGIN, bannerBean);
                        String action = bannerBean.getAction();
                        String h5Url = bannerBean.getH5Url();
                        Long is_Login = bannerBean.getIsLogin();
                        LoginJumpUtil.dialogSkip(action, mContext, bannerBean.getContent(), h5Url, is_Login);
                    }
                }
            }
        });
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back: //返回
                if (mContext instanceof HomePageActivity) {
                    HomePageActivity homePageActivity = (HomePageActivity) mContext;
                    homePageActivity.radioGroupChange(0);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 轮播图数据
     */
    public void loadHomeBanners() {
        RemotingEx.doRequest("getHomeBanner", ApiServiceBean.getHomeBanner(), null, this);
    }

    /**
     * 获取到我的首界面数据
     */
    private void loadMineHomeData() {
        RemotingEx.doRequest("mineData", ApiServiceBean.getMineHomeData(), null, this);
    }

    /**
     * 获取活动配置信息
     */
    private void getConfigInfo() {
        RemotingEx.doRequest("configMainActivity", ApiServiceBean.configMainActivity(), null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void refresh() {

        //加载轮播图
        loadHomeBanners();
        //加载活动数据
        loadMineHomeData();
        //获取活动配置信息
        getConfigInfo();
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

        if ("getHomeBanner".equals(action)) {
            bannerData = (List<BannerBean>) response.getDat();
            if (!EncodeAndStringTool.isListEmpty(bannerData)) {
                String str = JSON.toJSONString(bannerData);
                if (bannerDataString != null && bannerDataString.equals(str)) {
                    return;
                }
                bannerDataString = str;

                //设置轮播图
                bannerList.clear();
                for (int i = 0; i < bannerData.size(); i++) {
                    BannerBean bean = bannerData.get(i);
                    MarkBannerItem1 item = new MarkBannerItem1(bean.getPicture());
                    item.setAdConfigs(bean.getAdConfigs());
                    bannerList.add(item);
                }
                markBannerAdapter1.clearViews();
                markBannerAdapter1.setData(mContext, bannerList);
                //重新生成单位条
                mBannerView.setBannerAdapter(markBannerAdapter1);
            }
        } else if ("mineData".equals(action)) {
            mineBean = (MineBean) response.getDat();
            SaveUserInfo.getInstance(mContext).setUserInfo("cer", String.valueOf(mineBean.getCertification()));
            //添加标题
            initTitile();
            //添加fragment
            initFragment();

        } else if ("configMainActivity".equals(action)) {
            MainConfigBean mainConfigBean = (MainConfigBean) response.getDat();
            String str = JSON.toJSONString(mainConfigBean);
            if (mainConfigBeanString != null && mainConfigBeanString.equals(str)) {
                return;
            }
            mainConfigBeanString = str;

            //动态添加布局
            activityRegion.removeAllViews();
            activityRegion.addView(ActivityRegionManager.getView(mContext, mainConfigBean, llFound));
        }
    }

    private void initTitile() {
        mTitleList = new ArrayList<>();
        mTitleList.add("黄山行挑战赛");
        mTitleList.add("排行榜");
        mTitleList.add("热门题组");
        mTitleList.add("黄山行挑战赛");
        mTitleList.add("排行榜");
        mTitleList.add("热门题组");
        //设置tablayout模式
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        //tablayout获取集合中的名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(2)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(3)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(4)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(5)));
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(NoUsePrizeFragment.newInstance(mineBean.getCertification()));
        mFragmentList.add(UseInPrizeFragment.newInstance(mineBean.getCertification()));
        mFragmentList.add(UsePrizeFragment.newInstance(mineBean.getCertification()));
        mFragmentList.add(NoUsePrizeFragment.newInstance(mineBean.getCertification()));
        mFragmentList.add(UseInPrizeFragment.newInstance(mineBean.getCertification()));
        mFragmentList.add(UsePrizeFragment.newInstance(mineBean.getCertification()));
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

