package com.shuyun.qapp.ui.homepage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.HomeBottomInfoAdapter;
import com.shuyun.qapp.adapter.HomeLikeGroupAdapter;
import com.shuyun.qapp.adapter.HomeNewGroupAdapter;
import com.shuyun.qapp.adapter.MarkBannerAdapter2;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.HomeBottomInfoBean;
import com.shuyun.qapp.bean.MarkBannerItem2;
import com.shuyun.qapp.bean.MessageEvent;
import com.shuyun.qapp.bean.NewHomeSelectBean;
import com.shuyun.qapp.manager.ActivityRegionManager2;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.view.ActionJumpUtil;
import com.shuyun.qapp.view.GridSpacingItemDecoration;
import com.shuyun.qapp.view.ViewPagerScroller;
import com.sunfusheng.marqueeview.MarqueeView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;

/**
 * @Package: com.shuyun.qapp.ui.homepage
 * @ClassName: HomeSelectFragment
 * @Description: 精选
 * @Author: ganquan
 * @CreateDate: 2019/5/7 9:18
 */
public class HomeSelectFragment extends BaseFragment implements OnRemotingCallBackListener {
    @BindView(R.id.ll_home_fragment)
    RelativeLayout llHomeFragment;
    @BindView(R.id.banner)
    BannerViewPager banner;
    @BindView(R.id.iv_change)
    ImageView ivChange;
    @BindView(R.id.ll_change_group)
    LinearLayout llChangeGroup;
    @BindView(R.id.iv_group_bg1)
    ImageView ivGroupBg1;
    @BindView(R.id.rl_group_one)
    RelativeLayout rlGroupOne;
    @BindView(R.id.tv_group_name1)
    TextView tvGroupName1;
    @BindView(R.id.tv_group_tag1_one)
    TextView tvGroupTag1One;
    @BindView(R.id.recommend_logo1)
    TextView recommendLogo1;
    @BindView(R.id.rl_commend_one)
    RelativeLayout rlCommendOne;
    @BindView(R.id.iv_group_bg2)
    ImageView ivGroupBg2;
    @BindView(R.id.rl_group_two)
    RelativeLayout rlGroupTwo;
    @BindView(R.id.tv_group_name2)
    TextView tvGroupName2;
    @BindView(R.id.tv_group_tag2_one)
    TextView tvGroupTag2One;
    @BindView(R.id.recommend_logo2)
    TextView recommendLogo2;
    @BindView(R.id.rl_commend_two)
    RelativeLayout rlCommendTwo;
    @BindView(R.id.tv_recommend1)
    TextView tvRecommend1;
    @BindView(R.id.tv_recommend2)
    TextView tvRecommend2;
    @BindView(R.id.activityRegion)
    LinearLayout activityRegion; //动态活动区域
    @BindView(R.id.iv_ofens_bg1)
    ImageView ivOfensBg1;
    @BindView(R.id.tv_ofens_recommend1)
    TextView tvOfensRecommend1;
    @BindView(R.id.tv_ofens_name1)
    TextView tvOfensName1;
    @BindView(R.id.tv_ofens_accuracy1)
    TextView tvOfensAccuracy1;
    @BindView(R.id.iv_ofens_bg2)
    ImageView ivOfensBg2;
    @BindView(R.id.tv_ofens_recommend2)
    TextView tvOfensRecommend2;
    @BindView(R.id.tv_ofens_name2)
    TextView tvOfensName2;
    @BindView(R.id.tv_ofens_accuracy2)
    TextView tvOfensAccuracy2;
    @BindView(R.id.ll_often)
    LinearLayout llOften;
    @BindView(R.id.rv_new_group)
    RecyclerView rvNewGroup; //最新上线
    @BindView(R.id.ll_new_group)
    LinearLayout llNewGroup;
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView; //全民播报
    @BindView(R.id.ll_marqueeView)
    LinearLayout llMarqueeView;
    @BindView(R.id.rv_like_group)
    RecyclerView rvLikeGroup; //猜你喜欢
    @BindView(R.id.rv_home_bottom_info)
    RecyclerView rvHomeBottomInfo;//底部电话信息
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;

    private Activity mContext;
    Unbinder unbinder;
    private Handler mHandler = new Handler();

    //banner
    private MarkBannerAdapter2 markBannerAdapter2;
    private List<IBannerItem> bannerList = new ArrayList<>();
    private List<NewHomeSelectBean.BannersBean> bannerData = new ArrayList<>();
    private String homeDataString = null;

    /**
     * 为您推荐
     */
    private List<NewHomeSelectBean.RecommendsBean> recommendsBeanList;
    private CountDownTimer timer;
    private NewHomeSelectBean.RecommendsBean recommendGroup1;
    private NewHomeSelectBean.RecommendsBean recommendGroup2;
    private int recommendIndex = 0;
    private int recommendWidth = 0, recommendHeight = 0;

    //常答题组
    NewHomeSelectBean.OftensBean oftensBean1;
    NewHomeSelectBean.OftensBean oftensBean2;

    //猜你喜欢
    private List<NewHomeSelectBean.GuessBean> guessBeanList = new ArrayList<>();
    private HomeLikeGroupAdapter homeLikeGroupAdapter;

    //首页底部信息
    private List<HomeBottomInfoBean.ResultBean> homeBottomResult = new ArrayList<>();
    private HomeBottomInfoAdapter homeBottomInfoAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_select, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        EventBus.getDefault().register(this);//注册Eventbus

        mContext = getActivity();
        homeDataString = null;
        timer = new CountDownTimer(5 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!EncodeAndStringTool.isListEmpty(recommendsBeanList)) {
                    //倒计时5秒切換一下推荐题组
                    rollRecommendGroup();
                }
                timer.start();
            }
        };

        //获取屏幕宽度
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        //计算为你推荐高度宽高比
        recommendWidth = ((int) Math.ceil(dm.widthPixels) - SizeUtils.dp2px(3)) / 2;
        recommendHeight = (int) Math.ceil(recommendWidth * (200f / 372f));

        //初始化
        init();


    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, 10);

        }
    }

    @OnClick({R.id.ll_change_group, R.id.rl_commend_one, R.id.rl_commend_two, R.id.rl_ofens1, R.id.rl_ofens2})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.ll_change_group: //换一换
                Animation circle_anim = AnimationUtils.loadAnimation(mContext, R.anim.anim_round_rotate);
                LinearInterpolator interpolator = new LinearInterpolator();  //设置匀速旋转，在xml文件中设置会出现卡顿
                circle_anim.setInterpolator(interpolator);
                ivChange.startAnimation(circle_anim);  //开始动画
                timer.cancel();
                timer.start();
                //轮训换题组
                rollRecommendGroup();
                break;
            case R.id.rl_commend_one://推荐题组一
                if (!EncodeAndStringTool.isObjectEmpty(recommendGroup1)) {
                    Long groupId = recommendGroup1.getId();
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("h5Url", recommendGroup1.getH5Url());
                    startActivity(intent);
                }
                break;
            case R.id.rl_commend_two://推荐题组二
                if (!EncodeAndStringTool.isObjectEmpty(recommendGroup2)) {
                    Long groupId = recommendGroup2.getId();
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("h5Url", recommendGroup2.getH5Url());
                    startActivity(intent);
                }
                break;
            case R.id.rl_ofens1: //常答题组
                if (!EncodeAndStringTool.isObjectEmpty(oftensBean1)) {
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", oftensBean1.getId());
                    intent.putExtra("h5Url", oftensBean1.getH5Url());
                    startActivity(intent);
                }
                break;
            case R.id.rl_ofens2:
                if (!EncodeAndStringTool.isObjectEmpty(oftensBean2)) {
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", oftensBean2.getId());
                    intent.putExtra("h5Url", oftensBean2.getH5Url());
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }

    private void init() {
        MarkBannerItem2 i = new MarkBannerItem2("https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/banner/xiaji.jpg");
        bannerList.add(i);
        //设置轮播图
        markBannerAdapter2 = new MarkBannerAdapter2(new GlideImageLoader(), mContext);
        markBannerAdapter2.setData(mContext, bannerList);
        banner.setBannerAdapter(markBannerAdapter2);

        //设置index 在viewpager下面
        final ViewPager mViewpager = (ViewPager) banner.getChildAt(0);
        //设置index圆点为不可见
        banner.getChildAt(1).setVisibility(View.GONE);
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
//                markBannerAdapter2.refreshAdConfig(bannerList.get(realPosition), realPosition);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        banner.setBannerItemClick(new BannerViewPager.OnBannerItemClick<IBannerItem>() {
            @Override
            public void onClick(IBannerItem data) {
                for (int i = 0; i < bannerData.size(); i++) {
                    NewHomeSelectBean.BannersBean bannerBean = bannerData.get(i);
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


        //猜你喜欢
        homeLikeGroupAdapter = new HomeLikeGroupAdapter(guessBeanList, mContext);
        homeLikeGroupAdapter.setOnItemClickLitsener(new HomeLikeGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Long groupId = Long.parseLong(guessBeanList.get(position).getId());
                Intent intent = new Intent(mContext, WebAnswerActivity.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("h5Url", guessBeanList.get(position).getH5Url());
                startActivity(intent);
            }
        });

        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rvLikeGroup.setLayoutManager(gridLayoutManager);
        //解决数据加载不完的问题
        rvLikeGroup.setHasFixedSize(true);
        rvLikeGroup.setNestedScrollingEnabled(false);
        rvLikeGroup.setAdapter(homeLikeGroupAdapter);
        rvLikeGroup.addItemDecoration(new GridSpacingItemDecoration(2, ConvertUtils.dp2px(3), false));

        //首页电话底部信息
        homeBottomInfoAdapter = new HomeBottomInfoAdapter(homeBottomResult, mContext);
        homeBottomInfoAdapter.setOnItemClickLitsener(new HomeBottomInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String number = homeBottomResult.get(position).getCall();
                if (!EncodeAndStringTool.isStringEmpty(number)) {
                    //拔打电话
                    PhoneUtils.dial(number);
                }
            }
        });
        GridLayoutManager gm = new GridLayoutManager(mContext, 1) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rvHomeBottomInfo.setLayoutManager(gm);
        //解决数据加载不完的问题
        rvHomeBottomInfo.setHasFixedSize(true);
        rvHomeBottomInfo.setNestedScrollingEnabled(false);
        rvHomeBottomInfo.setAdapter(homeBottomInfoAdapter);

    }


    @Override
    public void refresh() {

        //获取底部电话信息
        getBottomInfo();

        //获得精选数据
        loadSelectInfo();

    }

    /**
     * 精选数据
     */
    public void loadSelectInfo() {
        RemotingEx.doRequest("getSelectInfo", RemotingEx.Builder().homeSelectInfo(), this);
    }

    /**
     * 首页底部信息
     */
    private void getBottomInfo() {
        RemotingEx.doRequest("homeBottomInfo", RemotingEx.Builder().homeBottomInfo(), this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if ("refresh".equals(messageEvent.getMessage())) {
            refresh();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onSucceed(String action, DataResponse response) {

        if (!response.isSuccees()) {
            if ("U0001".equals(response.getErr())) {
                mContext.finish();
            }
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }

        //获得精选数据
        if ("homeBottomInfo".equals(action)) {
            //首页底部电话信息
            HomeBottomInfoBean homeBottomInfoBean = (HomeBottomInfoBean) response.getDat();
            List<HomeBottomInfoBean.ResultBean> resultBeans = homeBottomInfoBean.getResult();
            if (!EncodeAndStringTool.isListEmpty(resultBeans)) {
                homeBottomResult.clear();
                homeBottomResult.addAll(resultBeans);
                homeBottomInfoAdapter.notifyDataSetChanged();
            }
        }
        if ("getSelectInfo".equals(action)) {
            NewHomeSelectBean newHomeSelectBean = (NewHomeSelectBean) response.getDat();

            //为您推荐
            if (!EncodeAndStringTool.isListEmpty(newHomeSelectBean.getRecommends())) {
                recommendsBeanList = newHomeSelectBean.getRecommends();
                recommendIndex = 0;
                rollRecommendGroup();
                timer.start();
                if (recommendsBeanList.size() <= 2) {
                    llChangeGroup.setVisibility(View.GONE);
                } else {
                    llChangeGroup.setVisibility(View.VISIBLE);
                }
            }

            //活动区域
            NewHomeSelectBean.RegionBean regionBean = newHomeSelectBean.getRegion();
            //动态添加布局
            activityRegion.removeAllViews();
            activityRegion.addView(ActivityRegionManager2.getView(mContext, regionBean, llHomeFragment));

            //常答题组
            List<NewHomeSelectBean.OftensBean> oftensBeanList = newHomeSelectBean.getOftens();
            if (!EncodeAndStringTool.isListEmpty(oftensBeanList)) {
                llOften.setVisibility(View.VISIBLE);
                oftensBean1 = oftensBeanList.get(0);
                ViewGroup.LayoutParams bannerParams = ivOfensBg1.getLayoutParams();
                bannerParams.height = recommendHeight;
                ivOfensBg1.setLayoutParams(bannerParams);
                ImageLoaderManager.LoadImage(mContext, oftensBean1.getPicture(), ivOfensBg1, R.mipmap.zw01);
                tvOfensName1.setText(oftensBean1.getName());
                if (!EncodeAndStringTool.isStringEmpty(oftensBean1.getAccuracy())) {
                    tvOfensAccuracy1.setVisibility(View.VISIBLE);
                    tvOfensAccuracy1.setText("正确率：" + oftensBean1.getAccuracy() + "%");
                } else {
                    tvOfensAccuracy1.setVisibility(View.GONE);
                }
                if (oftensBean1.isRecommend()) {
                    tvOfensRecommend1.setVisibility(View.VISIBLE);
                } else {
                    tvOfensRecommend1.setVisibility(View.GONE);
                }
                oftensBean2 = oftensBeanList.get(1);
                ViewGroup.LayoutParams bannerParams1 = ivOfensBg2.getLayoutParams();
                bannerParams1.height = recommendHeight;
                ivOfensBg2.setLayoutParams(bannerParams1);
                ImageLoaderManager.LoadImage(mContext, oftensBean2.getPicture(), ivOfensBg2, R.mipmap.zw01);
                tvOfensName2.setText(oftensBean2.getName());
                if (!EncodeAndStringTool.isStringEmpty(oftensBean2.getAccuracy())) {
                    tvOfensAccuracy2.setVisibility(View.VISIBLE);
                    tvOfensAccuracy2.setText("正确率：" + oftensBean2.getAccuracy() + "%");
                } else {
                    tvOfensAccuracy2.setVisibility(View.GONE);
                }
                if (oftensBean2.isRecommend()) {
                    tvOfensRecommend2.setVisibility(View.VISIBLE);
                } else {
                    tvOfensRecommend2.setVisibility(View.GONE);
                }

            } else {
                llOften.setVisibility(View.GONE);
            }

            //最新上线
            List<NewHomeSelectBean.NewTopicsBean> newTopicsBeanList = newHomeSelectBean.getNewTopics();
            if (!EncodeAndStringTool.isListEmpty(newTopicsBeanList)) {
                llNewGroup.setVisibility(View.VISIBLE);
                //解决数据加载不完的问题
                rvNewGroup.setHasFixedSize(true);
                rvNewGroup.setNestedScrollingEnabled(false);
                HomeNewGroupAdapter homeNewGroupAdapter = new HomeNewGroupAdapter(newTopicsBeanList, mContext);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) {
                    @Override
                    public boolean canScrollVertically() { //禁止layout垂直滑动
                        return false;
                    }
                };
                linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                rvNewGroup.setLayoutManager(linearLayoutManager);
                rvNewGroup.setAdapter(homeNewGroupAdapter);
            } else {
                llNewGroup.setVisibility(View.GONE);
            }

            //全民播报
            List<NewHomeSelectBean.BroadcastsBean> broadcastsBeanList = newHomeSelectBean.getBroadcasts();
            if (!EncodeAndStringTool.isListEmpty(broadcastsBeanList)) {
                llMarqueeView.setVisibility(View.VISIBLE);
                List<String> info = new ArrayList<>();
                for (int i = 0; i < broadcastsBeanList.size(); i++) {
                    info.add(broadcastsBeanList.get(i).getMessage());
                }
                if (!EncodeAndStringTool.isListEmpty(info)) {
                    marqueeView.startWithList(info);
                }
            } else {
                llMarqueeView.setVisibility(View.GONE);
            }

            //猜你喜欢
            List<NewHomeSelectBean.GuessBean> guessBean = newHomeSelectBean.getGuess();
            if (!EncodeAndStringTool.isListEmpty(guessBean)) {
                guessBeanList.clear();
                guessBeanList.addAll(guessBean);
                homeLikeGroupAdapter.notifyDataSetChanged();
            }

            bannerData = newHomeSelectBean.getBanners();
            if (!EncodeAndStringTool.isListEmpty(bannerData)) {
                String str = JSON.toJSONString(bannerData);
                if (homeDataString != null && homeDataString.equals(str)) {
                    return;
                }
                homeDataString = str;
                //设置轮播图
                bannerList.clear();
                for (int i = 0; i < bannerData.size(); i++) {
                    NewHomeSelectBean.BannersBean bean = bannerData.get(i);
                    MarkBannerItem2 item = new MarkBannerItem2(bean.getPicture());
                    item.setName(bean.getName());
                    item.setDescription(bean.getDescription());
                    bannerList.add(item);
                }
                markBannerAdapter2.clearViews();
                markBannerAdapter2.setData(mContext, bannerList);
                //重新生成单位条
                banner.setBannerAdapter(markBannerAdapter2);
            }

        }

    }

    @Override
    public void clear() {
        super.clear();
        homeDataString = null;
    }

    /**
     * 根据角标i的值,两个一组轮询推荐题组列表
     */
    private void rollRecommendGroup() {
        /**
         * 判断推荐题组数据是否为空
         */
        if (EncodeAndStringTool.isListEmpty(recommendsBeanList)) {
            return;
        }
        /**
         * 如果角标大于等于推荐题组数据长度,将角标值为零
         */
        if (recommendIndex >= recommendsBeanList.size()) {
            recommendIndex = 0;
        }
        recommendGroup1 = recommendsBeanList.get(recommendIndex);//获取角标处的推荐题组,显示在推荐题组左侧
        /***
         * 如果角标+1不大于推荐题组数据长度,获取角标+1处的推荐题组,显示在推荐题组右侧
         * 否则获取角标为0处推荐题组数据,显示在推荐题组右侧,并将角标值为1
         */
        if (recommendIndex + 1 < recommendsBeanList.size()) {
            recommendGroup2 = recommendsBeanList.get(recommendIndex + 1);
            recommendIndex += 2;
        } else {
            recommendGroup2 = recommendsBeanList.get(0);
            recommendIndex = 1;
        }

        try {
            /**
             * 设置推荐左侧题组的值,界面显示在推荐题组左侧
             */
            if (!EncodeAndStringTool.isObjectEmpty(recommendGroup1)) {
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup1.getName())) {
                    tvGroupName1.setText(recommendGroup1.getName() + "");
                }
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup1.getPicture())) {
                    ViewGroup.LayoutParams bannerParams = ivGroupBg1.getLayoutParams();
                    bannerParams.height = recommendHeight;
                    ivGroupBg1.setLayoutParams(bannerParams);
                    ImageLoaderManager.LoadImage(mContext, recommendGroup1.getPicture(), ivGroupBg1, R.mipmap.zw01);
                }

                tvRecommend1.setText(recommendGroup1.getButton());

//                if (recommendGroup1.isRecommend()) {
//                    recommendLogo1.setVisibility(View.VISIBLE);
//                } else {
//                    recommendLogo1.setVisibility(View.GONE);
//                }
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup1.getTags())) {
                    int size = recommendGroup1.getTags().size();
                    List<String> list = recommendGroup1.getTags();
                    String temp = "";
                    if (size > 0) {
                        tvGroupTag1One.setVisibility(View.VISIBLE);
                        for (int i = 0; i < list.size(); i++) {
                            if (i % 2 == 0) {
                                temp = temp + list.get(i) + " ";
                            } else {
                                temp = temp + list.get(i) + "\n";
                            }
                        }
                        tvGroupTag1One.setText(temp);
                    } else {
                        tvGroupTag1One.setVisibility(View.INVISIBLE);
                    }
                } else {
                    tvGroupTag1One.setVisibility(View.INVISIBLE);
                }

                //左侧任意位置logo
                rlGroupOne.removeAllViews();
//                List<GroupBean.AdConfigs> list = recommendGroup1.getAdConfigs();
//                if (list == null)
//                    list = new ArrayList<>();
//
//                for (GroupBean.AdConfigs adConfigs : list) {
//                    Long type = adConfigs.getType();
//                    if (AppConst.TYPE_HOME_GROUP != type) {
//                        continue;
//                    }
//                    final AnyPositionBean anyPositionBean = new AnyPositionBean();
//                    anyPositionBean.setType(adConfigs.getType());
//                    anyPositionBean.setLocation(adConfigs.getLocation());
//                    anyPositionBean.setWidth(adConfigs.getWidth());
//                    anyPositionBean.setHeight(adConfigs.getHeight());
//                    anyPositionBean.setPadding(adConfigs.getPadding());
//                    anyPositionBean.setMargin(adConfigs.getMargin());
//                    anyPositionBean.setShadow(adConfigs.getShadow());
//                    anyPositionBean.setShadowColor(adConfigs.getShadowColor());
//                    anyPositionBean.setShadowAlpha(adConfigs.getShadowAlpha());
//                    anyPositionBean.setShadowRadius(adConfigs.getShadowRadius());
//                    anyPositionBean.setImageUrl(adConfigs.getImageUrl());
//                    //执行
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            AnyPositionImgManage.execute(anyPositionBean, rlGroupOne, mContext);
//                        }
//                    }, 0);
//                }
            }
            /**
             * 设置推荐右侧推荐题组的值,界面显示在推荐题组右侧
             */
            if (!EncodeAndStringTool.isObjectEmpty(recommendGroup2)) {
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup2.getName())) {
                    tvGroupName2.setText(recommendGroup2.getName() + "");
                }
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup2.getPicture())) {
                    ViewGroup.LayoutParams bannerParams = ivGroupBg2.getLayoutParams();
                    bannerParams.height = recommendHeight;
                    ivGroupBg2.setLayoutParams(bannerParams);
                    ImageLoaderManager.LoadImage(mContext, recommendGroup2.getPicture(), ivGroupBg2, R.mipmap.zw01);
                }

                tvRecommend2.setText(recommendGroup2.getButton());
//                if (recommendGroup2.isRecommend()) {
//                    recommendLogo2.setVisibility(View.VISIBLE);
//                } else {
//                    recommendLogo2.setVisibility(View.GONE);
//                }

                if (!EncodeAndStringTool.isStringEmpty(recommendGroup2.getTags())) {
                    int size = recommendGroup2.getTags().size();
                    List<String> list = recommendGroup2.getTags();
                    String temp = "";
                    if (size > 0) {
                        tvGroupTag2One.setVisibility(View.VISIBLE);
                        for (int i = 0; i < list.size(); i++) {
                            if (i % 2 == 0) {
                                temp = temp + list.get(i) + " ";
                            } else {
                                temp = temp + list.get(i) + "\n";
                            }
                        }
                        tvGroupTag2One.setText(temp);
                    } else {
                        tvGroupTag2One.setVisibility(View.INVISIBLE);
                    }
                } else {
                    tvGroupTag2One.setVisibility(View.INVISIBLE);
                }

                //右侧任意位置logo
                rlGroupTwo.removeAllViews();
//                List<GroupBean.AdConfigs> list = recommendGroup2.getAdConfigs();
//                if (list == null)
//                    list = new ArrayList<>();
//
//                for (GroupBean.AdConfigs adConfigs : list) {
//                    Long type = adConfigs.getType();
//                    if (AppConst.TYPE_HOME_GROUP != type) {
//                        continue;
//                    }
//                    final AnyPositionBean anyPositionBean = new AnyPositionBean();
//                    anyPositionBean.setType(adConfigs.getType());
//                    anyPositionBean.setLocation(adConfigs.getLocation());
//                    anyPositionBean.setWidth(adConfigs.getWidth());
//                    anyPositionBean.setHeight(adConfigs.getHeight());
//                    anyPositionBean.setPadding(adConfigs.getPadding());
//                    anyPositionBean.setMargin(adConfigs.getMargin());
//                    anyPositionBean.setShadow(adConfigs.getShadow());
//                    anyPositionBean.setShadowColor(adConfigs.getShadowColor());
//                    anyPositionBean.setShadowAlpha(adConfigs.getShadowAlpha());
//                    anyPositionBean.setShadowRadius(adConfigs.getShadowRadius());
//                    anyPositionBean.setImageUrl(adConfigs.getImageUrl());
//                    //执行
//                    mHandler.postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//                            AnyPositionImgManage.execute(anyPositionBean, rlGroupTwo, mContext);
//                        }
//                    }, 0);
//                }
            }
        } catch (Exception e) {

        }
    }


}
