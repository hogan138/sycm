package com.shuyun.qapp.ui.found;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.androidkun.xtablayout.XTabLayout;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MarkBannerAdapter1;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.FloatWindowBean;
import com.shuyun.qapp.bean.FoundDataBean;
import com.shuyun.qapp.bean.MarkBannerItem1;
import com.shuyun.qapp.manager.ActivityRegionManager1;
import com.shuyun.qapp.manager.FragmentTouchManager;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.ui.webview.WebFragment;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.UmengPageUtil;
import com.shuyun.qapp.manager.FloatImageviewManage;
import com.shuyun.qapp.view.ActionJumpUtil;
import com.shuyun.qapp.view.ViewPagerScroller;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;

/**
 * 活动Fragment
 */
public class FoundFragment extends BaseFragment implements OnRemotingCallBackListener<Object>, FragmentTouchManager.FragmentTouchListener {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //标题文字
    Unbinder unbinder;
    @BindView(R.id.banner)
    BannerViewPager mBannerView;
    @BindView(R.id.tab_layout)
    XTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.activityRegion)
    LinearLayout activityRegion;
    @BindView(R.id.rl_found)
    RelativeLayout rlFound;
    @BindView(R.id.rl_logo)
    RelativeLayout rlLogo;
    @BindView(R.id.coordinator)
    CoordinatorLayout coordinator;

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

    @SuppressLint("NewApi")
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();
        tvCommonTitle.setText("发现");

        FragmentTouchManager.instance().registerFragmentTouchListener(this);

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

            //友盟页面统计
            UmengPageUtil.startPage(AppConst.APP_FOUND);

            //记录标记
            SaveUserInfo.getInstance(mContext).setUserInfo("umeng_from", "found");
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
                    if (data.ImageUrl().equals(bannerBean.getPicture())) {
                        LoginDataManager.instance().addData(LoginDataManager.BANNER_LOGIN, bannerBean);
                        String action = bannerBean.getAction();
                        String h5Url = bannerBean.getH5Url();
                        Long is_Login = bannerBean.getIsLogin();
                        ActionJumpUtil.dialogSkip(action, mContext, bannerBean.getContent(), h5Url, is_Login);
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

    /**
     * 浮窗数据
     */
    public void loadfloatWindow() {
        RemotingEx.doRequest("floatWindow", ApiServiceBean.floatWindow(), null, this);
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

        //浮窗数据
        loadfloatWindow();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        try {
            if ("foundInfo".equals(action)) {
                FoundDataBean foundDataBean = (FoundDataBean) response.getDat();
                String str = JSON.toJSONString(foundDataBean);
                if (bannerDataString != null && bannerDataString.equals(str)) {
                    return;
                }
                bannerDataString = str;

                //设置轮播图
                bannerData = foundDataBean.getBanner();
                if (!EncodeAndStringTool.isListEmpty(bannerData)) {
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
                activityRegion.addView(ActivityRegionManager1.getView(mContext, regionBean, rlFound));

                //动态tab添加
                mTitleList = new ArrayList<>();
                mFragmentList = new ArrayList<>();
                List<FoundDataBean.TablesBean> tablesBeanList = foundDataBean.getTables();

                //添加标题
                //添加fragment
                for (int i = 0; i < tablesBeanList.size(); i++) {
                    mTitleList.add(tablesBeanList.get(i).getTitle());
                    if (tablesBeanList.get(i).getType() == 1) {
                        mFragmentList.add(WebFragment.newInstance(tablesBeanList.get(i).getH5Url())); //h5页面
                    } else if (tablesBeanList.get(i).getType() == 2) {
                        mFragmentList.add(HotGroupFragment.newInstance(tablesBeanList.get(i).getGroups()));//热门题组
                    }
                }

                tabLayout.removeAllTabs(); //tablayout移除所有tab
                vp.removeAllViewsInLayout(); //viewpager清空view

                //设置适配器
                vp.setAdapter(new MyPagerAdapter(getChildFragmentManager(), mFragmentList, mTitleList));
                vp.addOnPageChangeListener(new XTabLayout.TabLayoutOnPageChangeListener(tabLayout));
                //将tablayout与fragment关联
                tabLayout.setupWithViewPager(vp);


            } else if ("floatWindow".equals(action)) {
                final FloatWindowBean floatWindowBean = (FloatWindowBean) response.getDat();
                Long status = floatWindowBean.getStatus();
                if (status == 1) {
                    rlLogo.setVisibility(View.VISIBLE);
                    //添加图片
                    FloatImageviewManage.execute(floatWindowBean, rlLogo, mContext);
                } else {
                    rlLogo.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {

        }

    }


    @Override
    public void onDestroy() {
        FragmentTouchManager.instance().unRegisterFragmentTouchListener(this);
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            FloatImageviewManage.hideImageview();
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            FloatImageviewManage.showImageview();
        }
        return false;
    }

    @Override
    public void clear() {
        super.clear();
        bannerDataString = null;
    }
}
