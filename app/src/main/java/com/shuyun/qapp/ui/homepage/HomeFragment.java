package com.shuyun.qapp.ui.homepage;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.SizeUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.HomeBottomInfoAdapter;
import com.shuyun.qapp.adapter.HomeSortAdapter;
import com.shuyun.qapp.adapter.HotGroupAdapter;
import com.shuyun.qapp.adapter.MarkBannerAdapter;
import com.shuyun.qapp.adapter.MarkBannerAdapter1;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.AnyPositionBean;
import com.shuyun.qapp.bean.BannerBean;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.FloatWindowBean;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.bean.HomeBottomInfoBean;
import com.shuyun.qapp.bean.HomeGroupsBean;
import com.shuyun.qapp.bean.HomeNoticeBean;
import com.shuyun.qapp.bean.MainConfigBean;
import com.shuyun.qapp.bean.MarkBannerItem;
import com.shuyun.qapp.bean.MarkBannerItem1;
import com.shuyun.qapp.bean.SystemInfo;
import com.shuyun.qapp.manager.ActivityRegionManager;
import com.shuyun.qapp.manager.FloatImageviewManage;
import com.shuyun.qapp.manager.FragmentTouchManager;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.classify.ClassifyActivity;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.ui.mine.MinePrizeActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.NotificationsUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.UmengPageUtil;
import com.shuyun.qapp.view.ActionJumpUtil;
import com.shuyun.qapp.view.AnyPositionImgManage;
import com.shuyun.qapp.view.ITextBannerItemClickListener;
import com.shuyun.qapp.view.InviteSharePopupUtil;
import com.shuyun.qapp.view.MainActivityDialogInfo;
import com.shuyun.qapp.view.NotifyDialog;
import com.shuyun.qapp.view.OvalImageView;
import com.shuyun.qapp.view.TextBannerView;
import com.shuyun.qapp.view.ViewPagerScroller;
import com.sunfusheng.marqueeview.MarqueeView;

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
 * 首页
 */
public class HomeFragment extends BaseFragment implements OnRemotingCallBackListener, FragmentTouchManager.FragmentTouchListener {
    @BindView(R.id.tv_invite)
    TextView tvInvite; //分享
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.iv_common_right_icon)
    ImageView ivCommonRightIcon;//右侧消息图标
    @BindView(R.id.ll_home_fragment)
    RelativeLayout llHomeFragment;//layout容器
    @BindView(R.id.banner)
    BannerViewPager mBannerView;//轮播图
    @BindView(R.id.ll_marqueeView)
    LinearLayout llMarqueeView; //跑马灯布局
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;//跑马灯
    @BindView(R.id.rv_hot_group)
    RecyclerView rvHotGroup;//热门题组
    @BindView(R.id.iv_bx)
    ImageView ivBx;//宝箱抖动的动画
    Unbinder unbinder;
    @BindView(R.id.ll_often)
    LinearLayout llOften; //常答题组title
    @BindView(R.id.always_banner)
    BannerViewPager alwaysBanner; //常答轮播题组
    @BindView(R.id.activityRegion)
    LinearLayout activityRegion;//自定义区域布局
    @BindView(R.id.tv_number1)
    TextView tvNumber1;  //客服电话
    @BindView(R.id.tv_number2)
    TextView tvNumber2; //招商电话
    @BindView(R.id.ll_change_group)
    LinearLayout llChangeGroup; //换一换
    @BindView(R.id.iv_change)
    ImageView ivChange; //环形图
    @BindView(R.id.iv_group_bg1)
    OvalImageView ivGroupBg1; //推荐题组图片1
    @BindView(R.id.tv_group_name1)
    TextView tvGroupName1; //推荐题组名称1
    @BindView(R.id.iv_group_bg2)
    OvalImageView ivGroupBg2; //推荐题组图片2
    @BindView(R.id.tv_group_name2)
    TextView tvGroupName2;//推荐题组名称2
    @BindView(R.id.rl_commend_one)
    RelativeLayout rlCommendOne; //推荐题组1布局
    @BindView(R.id.rl_commend_two)
    RelativeLayout rlCommendTwo; //推荐题组2布局
    @BindView(R.id.tv_group_tag1_one)
    TextView tvGroupTag1One; //推荐题组1标签文字
    @BindView(R.id.tv_group_tag1_two)
    TextView tvGroupTag1Two;
    @BindView(R.id.recommend_logo1)
    TextView recommendLogo1;
    @BindView(R.id.tv_group_tag2_one)
    TextView tvGroupTag2One;  //推荐题组2标签文字
    @BindView(R.id.tv_group_tag2_two)
    TextView tvGroupTag2Two;
    @BindView(R.id.recommend_logo2)
    TextView recommendLogo2;
    @BindView(R.id.rv_group_sort_group)
    RecyclerView rvGroupSortGroup; //分类
    @BindView(R.id.scrollView)
    NestedScrollView scrollView; //ScrollView
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle; //标题栏
    @BindView(R.id.scroll_ad)
    TextBannerView scrollAd; //公告
    @BindView(R.id.rl_ad)
    RelativeLayout rlAd; //公告布局
    @BindView(R.id.rl_group_one)
    RelativeLayout rlGroupOne; //推荐题组1任意logo
    @BindView(R.id.rl_group_two)
    RelativeLayout rlGroupTwo; //推荐题组2任意logo
    @BindView(R.id.rv_home_bottom_info)
    RecyclerView rvHomeBottomInfo; //首页底部信息
    @BindView(R.id.rl_logo)
    RelativeLayout rlLogo; //浮窗

    /**
     * 网络获取到推荐题组列表
     */
    private List<GroupBean> groupBeans;
    private CountDownTimer timer;
    private Activity mContext;
    private Handler mHandler = new Handler();
    private GroupBean recommendGroup1;
    private GroupBean recommendGroup2;
    private int recommendIndex = 0;

    private boolean isLoading = false;

    //banner
    private MarkBannerAdapter1 markBannerAdapter1;
    private List<IBannerItem> bannerList = new ArrayList<>();
    private List<BannerBean> bannerData = new ArrayList<>();
    private String bannerDataString = null;

    //常答题组
    private MarkBannerAdapter markBannerAdapter;
    private List<IBannerItem> oftenList = new ArrayList<>();
    private List<GroupBean> oftenData = new ArrayList<>();

    //大家都在答
    private List<GroupBean> groupThermalData = new ArrayList<>();
    private HotGroupAdapter groupThermalAdapter;

    //分类
    private List<GroupClassifyBean> groupClassifyBeans = new ArrayList<>();
    private HomeSortAdapter treeGroupAdapter;

    //公告
    private List<HomeNoticeBean> homeNoticeBeanList = new ArrayList<>();

    //首页底部信息
    private List<HomeBottomInfoBean.ResultBean> homeBottomResult = new ArrayList<>();
    private HomeBottomInfoAdapter homeBottomInfoAdapter;

    private String systemInfosString = null;
    private String mainConfigBeanString = null;
    private String treeString = null;
    private String thermalString = null;
    private String oftenString = null;
    private String recommendString = null;
    private String HomeNoticeBeanString = null;

    //banner
    private int bannerWidth = 0, bannerHeight = 0, bannerTotalHeight = 0;
    //常答题组
    private int alwaysWidth = 0, alwaysHeight = 0, alwaysTotalHeight = 0;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @SuppressLint({"NewApi", "ClickableViewAccessibility"})
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

        FragmentTouchManager.instance().registerFragmentTouchListener(this);

        //获取屏幕宽度
        DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
        bannerWidth = (int) Math.ceil(dm.widthPixels) - SizeUtils.dp2px(50);
        bannerHeight = (int) Math.ceil(bannerWidth * (260f / 750f));
        bannerTotalHeight = bannerHeight + SizeUtils.dp2px(20);

        alwaysWidth = (int) Math.ceil(dm.widthPixels) - SizeUtils.dp2px(126);
        alwaysHeight = (int) Math.ceil(alwaysWidth * (264f / 528f));
        alwaysTotalHeight = alwaysHeight + SizeUtils.dp2px(20);

        /**
         * 检测微信是否安装,如果没有安装,需不显示分享按钮;如果安装了微信则显示分享按钮.
         */
        if (!SykscApplication.mWxApi.isWXAppInstalled()) {
            tvInvite.setVisibility(View.GONE);
        } else {
            tvInvite.setVisibility(View.VISIBLE);
        }
        tvCommonTitle.setText("全民共进");
        ivCommonRightIcon.setImageResource(R.mipmap.message_n);//右侧消息按钮;

        timer = new CountDownTimer(5 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                if (!EncodeAndStringTool.isListEmpty(groupBeans)) {
                    //倒计时5秒切換一下推荐题组
                    rollRecommendGroup();
                }
                timer.start();
            }
        };

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
            UmengPageUtil.startPage(AppConst.APP_HOME);

            //记录标记
            SaveUserInfo.getInstance(mContext).setUserInfo("umeng_from", "home");
        }
    }

    private void init() {
        //设置TextBannerView点击监听事件，返回点击的data数据, 和position位置
        scrollAd.setItemOnClickListener(new ITextBannerItemClickListener() {
            @Override
            public void onItemClick(String data, int position) {
                HomeNoticeBean homeNoticeBean = homeNoticeBeanList.get(position);
                LoginDataManager.instance().addData(LoginDataManager.HOME_NOTICE_LOGIN, homeNoticeBean);
                ActionJumpUtil.dialogSkip(homeNoticeBean.getAction(),
                        mContext,
                        homeNoticeBean.getGroupId(),
                        homeNoticeBean.getH5Url(),
                        homeNoticeBean.getIsLogin());
            }
        });

        //banner设置
        ViewGroup.LayoutParams bannerParams = mBannerView.getLayoutParams();
        bannerParams.height = bannerTotalHeight;
        mBannerView.setLayoutParams(bannerParams);

        MarkBannerItem1 i = new MarkBannerItem1("https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/banner/xiaji.jpg");
        bannerList.add(i);
        //设置轮播图
        markBannerAdapter1 = new MarkBannerAdapter1(new GlideImageLoader(), mContext);
        markBannerAdapter1.setData(mContext, bannerList);
        mBannerView.setBannerAdapter(markBannerAdapter1);

        //设置index 在viewpager下面
        final ViewPager mViewpager = (ViewPager) mBannerView.getChildAt(0);
        //为ViewPager设置高度
        ViewGroup.LayoutParams params = mViewpager.getLayoutParams();
        params.height = bannerHeight;//getResources().getDimensionPixelSize(R.dimen.viewPager_01);
        mViewpager.setLayoutParams(params);
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
                        ActionJumpUtil.dialogSkip(action, mContext, bannerBean.getContent(), h5Url, is_Login);
                    }
                }
            }
        });
        mBannerView.setPageTransformer(true, new YZoomTransFormer(0.9f)); //banner动画

        //常答题组
        ViewGroup.LayoutParams alwaysParams = alwaysBanner.getLayoutParams();
        alwaysParams.height = alwaysTotalHeight;
        alwaysBanner.setLayoutParams(alwaysParams);

        MarkBannerItem item = new MarkBannerItem("https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/banner/xiaji.jpg");
        item.setMarkLabel("默认");
        oftenList.add(item);
        markBannerAdapter = new MarkBannerAdapter(new GlideImageLoader(), mContext);
        markBannerAdapter.setData(mContext, oftenList);
        alwaysBanner.setBannerAdapter(markBannerAdapter);

        //设置index 在viewpager下面
        ViewPager mViewpagerOften = (ViewPager) alwaysBanner.getChildAt(0);
        //为ViewPager设置高度
        params = mViewpagerOften.getLayoutParams();
        params.height = alwaysHeight;//getResources().getDimensionPixelSize(R.dimen.viewPager_02);
        mViewpagerOften.setLayoutParams(params);
        //设置时间，时间越长，速度越慢
        ViewPagerScroller pagerScrollerOften = new ViewPagerScroller(getActivity());
        pagerScrollerOften.setScrollDuration(400);
        pagerScrollerOften.initViewPagerScroll(mViewpagerOften);

        mViewpagerOften.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                int realPosition = position % oftenList.size();
                markBannerAdapter.refreshAdConfig(oftenList.get(realPosition), realPosition);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        alwaysBanner.setBannerItemClick(new BannerViewPager.OnBannerItemClick<IBannerItem>() {
            @Override
            public void onClick(IBannerItem data) {
                for (int i = 0; i < oftenData.size(); i++) {
                    GroupBean bean = oftenData.get(i);
                    if (data.ImageUrl().equals(bean.getPicture())) {
                        Intent intent = new Intent(mContext, WebAnswerActivity.class);
                        intent.putExtra("groupId", bean.getId());
                        intent.putExtra("h5Url", bean.getH5Url());
                        startActivity(intent);
                        break;
                    }
                }
            }
        });

        //大家都在答
        groupThermalAdapter = new HotGroupAdapter(groupThermalData, mContext);
        groupThermalAdapter.setOnItemClickLitsener(new HotGroupAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Long groupId = groupThermalData.get(position).getId();
                Intent intent = new Intent(mContext, WebAnswerActivity.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("h5Url", groupThermalData.get(position).getH5Url());
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
        rvHotGroup.setLayoutManager(gridLayoutManager);
        //解决数据加载不完的问题
        rvHotGroup.setHasFixedSize(true);
        rvHotGroup.setNestedScrollingEnabled(false);
        rvHotGroup.setAdapter(groupThermalAdapter);


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

        //分类
        treeGroupAdapter = new HomeSortAdapter(groupClassifyBeans, mContext);
        treeGroupAdapter.setOnItemClickLitsener(new HomeSortAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                SaveUserInfo.getInstance(mContext).setUserInfo("show_back", "show_back");
                Long groupId = groupClassifyBeans.get(position).getId();
                Intent intent1 = new Intent(mContext, ClassifyActivity.class);
                intent1.putExtra("id", groupId);
                startActivity(intent1);
            }
        });
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 1) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager1.setSmoothScrollbarEnabled(true);
        gridLayoutManager1.setAutoMeasureEnabled(true);
        rvGroupSortGroup.setLayoutManager(gridLayoutManager1);
        //解决数据加载不完的问题
        rvGroupSortGroup.setHasFixedSize(true);
        rvGroupSortGroup.setNestedScrollingEnabled(false);
        rvGroupSortGroup.setAdapter(treeGroupAdapter);
    }

    @OnClick({R.id.tv_invite,
            R.id.iv_common_right_icon,
            R.id.tv_number1,
            R.id.tv_number2,
            R.id.ll_change_group,
            R.id.rl_commend_one,
            R.id.rl_commend_two})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_invite://邀请分享
                InviteSharePopupUtil.showSharedPop(mContext, llHomeFragment);
                break;
            case R.id.iv_common_right_icon://右侧消息按钮
                ivCommonRightIcon.setImageResource(R.mipmap.message_n);
                startActivity(new Intent(mContext, InformationActivity.class));
                break;
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
            case R.id.tv_number1: //客服电话
                PhoneUtils.dial("400-650-9258");
                break;
            case R.id.tv_number2: //招商合作电话
                PhoneUtils.dial("0571-86875102");
                break;
            default:
                break;
        }
    }

    /**
     * 轮播图数据
     */
    public void loadHomeBanners() {
        RemotingEx.doRequest("getHomeBanner", RemotingEx.Builder().getHomeBanner(), this);
    }

    /**
     * 首页浮窗
     */
    public void loadHomeFloatWindow() {
        RemotingEx.doRequest("getHomefloatwindow", RemotingEx.Builder().homeFloatWindow(), this);
    }

    /**
     * 获取跑马灯消息
     */
    private void loadSystemInfo() {
        RemotingEx.doRequest("getSystemInfo", RemotingEx.Builder().getSystemInfo(), this);
    }

    /**
     * 获取活动配置信息
     */
    private void getConfigInfo() {
        RemotingEx.doRequest("configMainActivity", RemotingEx.Builder().configMainActivity(), this);
    }

    /**
     * 首页题组
     */
    private void loadHomeGroups() {
        RemotingEx.doRequest("getHomeGroups", RemotingEx.Builder().getHomeGroups(AppUtils.getAppVersionName()), this);
    }

    /**
     * 首页底部信息
     */
    private void getBottomInfo() {
        RemotingEx.doRequest("homeBottomInfo", RemotingEx.Builder().homeBottomInfo(), this);
    }

    /**
     * 根据角标i的值,两个一组轮询推荐题组列表
     */
    private void rollRecommendGroup() {
        /**
         * 判断推荐题组数据是否为空
         */
        if (EncodeAndStringTool.isListEmpty(groupBeans)) {
            return;
        }
        /**
         * 如果角标大于等于推荐题组数据长度,将角标值为零
         */
        if (recommendIndex >= groupBeans.size()) {
            recommendIndex = 0;
        }
        recommendGroup1 = groupBeans.get(recommendIndex);//获取角标处的推荐题组,显示在推荐题组左侧
        /***
         * 如果角标+1不大于推荐题组数据长度,获取角标+1处的推荐题组,显示在推荐题组右侧
         * 否则获取角标为0处推荐题组数据,显示在推荐题组右侧,并将角标值为1
         */
        if (recommendIndex + 1 < groupBeans.size()) {
            recommendGroup2 = groupBeans.get(recommendIndex + 1);
            recommendIndex += 2;
        } else {
            recommendGroup2 = groupBeans.get(0);
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
                    ImageLoaderManager.LoadImage(mContext, recommendGroup1.getPicture(), ivGroupBg1, R.mipmap.zw01);
                }
                if (recommendGroup1.isRecommend()) {
                    recommendLogo1.setVisibility(View.VISIBLE);
                } else {
                    recommendLogo1.setVisibility(View.GONE);
                }
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup1.getRemark())) {
                    String[] temp = null;
                    temp = recommendGroup1.getRemark().split(" ");
                    if (temp.length == 0) {
                        tvGroupTag1One.setVisibility(View.INVISIBLE);
                        tvGroupTag1Two.setVisibility(View.INVISIBLE);
                    } else if (temp.length == 1) {
                        tvGroupTag1One.setVisibility(View.VISIBLE);
                        tvGroupTag1One.setText(temp[0]);
                        tvGroupTag1Two.setVisibility(View.INVISIBLE);
                    } else if (temp.length == 2) {
                        tvGroupTag1One.setVisibility(View.VISIBLE);
                        tvGroupTag1One.setText(temp[0]);
                        tvGroupTag1Two.setVisibility(View.VISIBLE);
                        tvGroupTag1Two.setText(temp[1]);
                    } else if (temp.length == 3) {
                        tvGroupTag1One.setVisibility(View.VISIBLE);
                        tvGroupTag1One.setText(temp[0] + "  " + temp[1]);
                        tvGroupTag1Two.setVisibility(View.VISIBLE);
                        tvGroupTag1Two.setText(temp[2]);
                    }
                } else {
                    tvGroupTag1One.setVisibility(View.INVISIBLE);
                    tvGroupTag1Two.setVisibility(View.INVISIBLE);
                }

                //左侧任意位置logo
                rlGroupOne.removeAllViews();
                List<GroupBean.AdConfigs> list = recommendGroup1.getAdConfigs();
                if (list == null)
                    list = new ArrayList<>();

                for (GroupBean.AdConfigs adConfigs : list) {
                    Long type = adConfigs.getType();
                    if (AppConst.TYPE_HOME_GROUP != type) {
                        continue;
                    }
                    final AnyPositionBean anyPositionBean = new AnyPositionBean();
                    anyPositionBean.setType(adConfigs.getType());
                    anyPositionBean.setLocation(adConfigs.getLocation());
                    anyPositionBean.setWidth(adConfigs.getWidth());
                    anyPositionBean.setHeight(adConfigs.getHeight());
                    anyPositionBean.setPadding(adConfigs.getPadding());
                    anyPositionBean.setMargin(adConfigs.getMargin());
                    anyPositionBean.setShadow(adConfigs.getShadow());
                    anyPositionBean.setShadowColor(adConfigs.getShadowColor());
                    anyPositionBean.setShadowAlpha(adConfigs.getShadowAlpha());
                    anyPositionBean.setShadowRadius(adConfigs.getShadowRadius());
                    anyPositionBean.setImageUrl(adConfigs.getImageUrl());
                    //执行
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AnyPositionImgManage.execute(anyPositionBean, rlGroupOne, mContext);
                        }
                    }, 0);
                }
            }
            /**
             * 设置推荐右侧推荐题组的值,界面显示在推荐题组右侧
             */
            if (!EncodeAndStringTool.isObjectEmpty(recommendGroup2)) {
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup2.getName())) {
                    tvGroupName2.setText(recommendGroup2.getName() + "");
                }
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup2.getPicture())) {
                    ImageLoaderManager.LoadImage(mContext, recommendGroup2.getPicture(), ivGroupBg2, R.mipmap.zw01);
                }
                if (recommendGroup2.isRecommend()) {
                    recommendLogo2.setVisibility(View.VISIBLE);
                } else {
                    recommendLogo2.setVisibility(View.GONE);
                }
                if (!EncodeAndStringTool.isStringEmpty(recommendGroup2.getRemark())) {
                    String[] temp = null;
                    temp = recommendGroup2.getRemark().split(" ");
                    if (temp.length == 0) {
                        tvGroupTag2One.setVisibility(View.INVISIBLE);
                        tvGroupTag2Two.setVisibility(View.INVISIBLE);
                    } else if (temp.length == 1) {
                        tvGroupTag2One.setVisibility(View.VISIBLE);
                        tvGroupTag2One.setText(temp[0]);
                        tvGroupTag2Two.setVisibility(View.INVISIBLE);
                    } else if (temp.length == 2) {
                        tvGroupTag2One.setVisibility(View.VISIBLE);
                        tvGroupTag2One.setText(temp[0]);
                        tvGroupTag2Two.setVisibility(View.VISIBLE);
                        tvGroupTag2Two.setText(temp[1]);
                    } else if (temp.length == 3) {
                        tvGroupTag2One.setVisibility(View.VISIBLE);
                        tvGroupTag2One.setText(temp[0] + "  " + temp[1]);
                        tvGroupTag2Two.setVisibility(View.VISIBLE);
                        tvGroupTag2Two.setText(temp[2]);
                    }
                } else {
                    tvGroupTag2One.setVisibility(View.INVISIBLE);
                    tvGroupTag2Two.setVisibility(View.INVISIBLE);
                }

                //右侧任意位置logo
                rlGroupTwo.removeAllViews();
                List<GroupBean.AdConfigs> list = recommendGroup2.getAdConfigs();
                if (list == null)
                    list = new ArrayList<>();

                for (GroupBean.AdConfigs adConfigs : list) {
                    Long type = adConfigs.getType();
                    if (AppConst.TYPE_HOME_GROUP != type) {
                        continue;
                    }
                    final AnyPositionBean anyPositionBean = new AnyPositionBean();
                    anyPositionBean.setType(adConfigs.getType());
                    anyPositionBean.setLocation(adConfigs.getLocation());
                    anyPositionBean.setWidth(adConfigs.getWidth());
                    anyPositionBean.setHeight(adConfigs.getHeight());
                    anyPositionBean.setPadding(adConfigs.getPadding());
                    anyPositionBean.setMargin(adConfigs.getMargin());
                    anyPositionBean.setShadow(adConfigs.getShadow());
                    anyPositionBean.setShadowColor(adConfigs.getShadowColor());
                    anyPositionBean.setShadowAlpha(adConfigs.getShadowAlpha());
                    anyPositionBean.setShadowRadius(adConfigs.getShadowRadius());
                    anyPositionBean.setImageUrl(adConfigs.getImageUrl());
                    //执行
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            AnyPositionImgManage.execute(anyPositionBean, rlGroupTwo, mContext);
                        }
                    }, 0);
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取宝箱数量
     */
    private void loadTreasureBoxNum() {
        RemotingEx.doRequest("loadTreasureBoxNum", RemotingEx.Builder().getTreasureNumV2(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        FragmentTouchManager.instance().unRegisterFragmentTouchListener(this);
        unbinder.unbind();
    }

    //获取活动弹框信息
    private void getDialogInfo() {
        RemotingEx.doRequest("configDialog", RemotingEx.Builder().configDialog(), this);
    }

    //获取公告信息
    private void getNoticeInfo() {
        RemotingEx.doRequest("homeNotice", RemotingEx.Builder().homeNotice(), this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    public void refresh() {
        if (mContext != null) {
            long newNowMills = (long) SharedPrefrenceTool.get(mContext, "nowMills", (TimeUtils.getNowMills() - 86400000 * 7));//获取首选项中的时间,默认值为七天前的时间戳
            long timeSpan = TimeUtils.getTimeSpan(newNowMills, TimeUtils.getNowMills(), TimeConstants.DAY);//当前时间和首选项中时间差
            /**
             * 如果通知权限关闭且当前时间和首选项中的时间相差大于等于7天(每7天提醒一次),弹窗提示
             */
            if (!NotificationsUtils.isNotificationEnabled(mContext) && timeSpan >= 7) {
                //当前弹窗提示时间
                long nowMills = TimeUtils.getNowMills();
                SharedPrefrenceTool.put(mContext, "nowMills", nowMills);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //如果通知栏未打开,弹出前往设置打开通知栏的弹窗
                        NotifyDialog.dialogShow(mContext);
                    }
                }, 10);
            }
        }

        //获取全民播报
        loadSystemInfo();

        //获取banner轮播数据
        loadHomeBanners();

        //获取公告信息
        getNoticeInfo();

        //获取弹框信息
        getDialogInfo();

        //首页题组
        loadHomeGroups();

        //首页底部信息
        getBottomInfo();

        //获取活动配置信息
        getConfigInfo();


        //用户已登录
        if (AppConst.isLogin()) {

            //获取首页浮窗
            loadHomeFloatWindow();

            //获取宝箱数量
            loadTreasureBoxNum();

            //用户活跃度
            RemotingEx.doRequest(RemotingEx.Builder().activeness(), null);
        } else {
            //用户未登录
            try {
                ivBx.clearAnimation();
                ivBx.setVisibility(View.GONE);
            } catch (Exception e) {

            }
        }
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
    public void onSucceed(String action, DataResponse response) {
        if (!response.isSuccees()) {
            if ("U0001".equals(response.getErr())) {
                mContext.finish();
            }
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
        } else if ("getSystemInfo".equals(action)) {
            List<SystemInfo> systemInfos = (List<SystemInfo>) response.getDat();
            if (!EncodeAndStringTool.isListEmpty(systemInfos)) {
                String str = JSON.toJSONString(systemInfos);
                if (systemInfosString != null && systemInfosString.equals(str)) {
                    return;
                }
                systemInfosString = str;

                llMarqueeView.setVisibility(View.VISIBLE);
                List<String> info = new ArrayList<>();
                for (int i = 0; i < systemInfos.size(); i++) {
                    info.add(systemInfos.get(i).getMsg());
                }
                if (!EncodeAndStringTool.isListEmpty(info)) {
                    marqueeView.startWithList(info);
                }
            } else {
                llMarqueeView.setVisibility(View.GONE);
            }
        } else if ("configMainActivity".equals(action)) {
            MainConfigBean mainConfigBean = (MainConfigBean) response.getDat();
            String str = JSON.toJSONString(mainConfigBean);
            if (mainConfigBeanString != null && mainConfigBeanString.equals(str)) {
                return;
            }
            mainConfigBeanString = str;

            //动态添加布局
            activityRegion.removeAllViews();
            activityRegion.addView(ActivityRegionManager.getView(mContext, mainConfigBean, llHomeFragment));
        } else if ("getHomeGroups".equals(action)) {
            HomeGroupsBean homeGroupsBean = (HomeGroupsBean) response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(homeGroupsBean)) {
                //推荐题组
                if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getRecommend())) {
                    String str = JSON.toJSONString(homeGroupsBean.getRecommend());
                    groupBeans = homeGroupsBean.getRecommend();
                    if (recommendString == null || !recommendString.equals(str)) {
                        recommendIndex = 0;
                        rollRecommendGroup();
                        if (groupBeans.size() <= 2) {
                            llChangeGroup.setVisibility(View.GONE);
                        } else {
                            llChangeGroup.setVisibility(View.VISIBLE);
                        }
                    }
                    recommendString = str;
                }
                //常答题组
                if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getOften())) {
                    String str = JSON.toJSONString(homeGroupsBean.getOften());
                    if (oftenString == null || !oftenString.equals(str)) {
                        llOften.setVisibility(View.VISIBLE);
                        alwaysBanner.setVisibility(View.VISIBLE);
                        oftenData = homeGroupsBean.getOften();
                        oftenList.clear();
                        for (int i = 0; i < oftenData.size(); i++) {
                            GroupBean bean = oftenData.get(i);
                            MarkBannerItem item = new MarkBannerItem(bean.getPicture());
                            item.setMarkLabel(bean.getName());
                            item.setAdConfigs(bean.getAdConfigs());
                            oftenList.add(item);
                        }
                        markBannerAdapter.clearViews();
                        markBannerAdapter.setData(mContext, oftenList);
                        //重新生成单位条
                        alwaysBanner.setBannerAdapter(markBannerAdapter);
                    }
                    oftenString = str;
                } else {
                    llOften.setVisibility(View.GONE);
                    alwaysBanner.setVisibility(View.GONE);
                }
                //大家都在答
                if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getThermal())) {
                    List<GroupBean> groupData = homeGroupsBean.getThermal();
                    String str = JSON.toJSONString(homeGroupsBean.getThermal());
                    if (thermalString == null || !thermalString.equals(str)) {
                        groupThermalData.clear();
                        groupThermalData.addAll(groupData);
                        groupThermalAdapter.notifyDataSetChanged();
                    }
                    thermalString = str;
                }
                //分类
                if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getTree())) {
                    final List<GroupClassifyBean> classifyBeans = homeGroupsBean.getTree();
                    String str = JSON.toJSONString(homeGroupsBean.getTree());
                    if (treeString == null || !treeString.equals(str)) {
                        groupClassifyBeans.clear();
                        groupClassifyBeans.addAll(classifyBeans);
                        treeGroupAdapter.notifyDataSetChanged();
                    }
                    treeString = str;
                }

                timer.cancel();
                timer.start();
            }
        } else if ("loadTreasureBoxNum".equals(action)) {
            BoxBean boxBean = (BoxBean) response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(boxBean)) {
                if (boxBean.getCount() > 0) {
                    if (boxBean.getCount() == 1 && boxBean.getSource() == 3) {//从微信获取且只有一个宝箱，直接跳开宝箱页面
                        SharedPreferences sharedPreferences = mContext.getSharedPreferences("FirstRun", 0);
                        Boolean first_run = sharedPreferences.getBoolean("Main", true);
                        if (first_run) {
                            sharedPreferences.edit().putBoolean("Main", false).apply();
                            Intent intent = new Intent(mContext, WebPrizeBoxActivity.class);
                            intent.putExtra("BoxBean", boxBean);
                            intent.putExtra("main_box", "main_box");
                            startActivity(intent);
                        }
                    } else {//如果宝箱数量大于0,首页左下角显示宝箱摇晃动画
                        ivBx.setVisibility(View.VISIBLE);
                        TranslateAnimation animation = new TranslateAnimation(5, -5, 0, 0);
                        animation.setInterpolator(new OvershootInterpolator());
                        animation.setDuration(500);
                        animation.setRepeatCount(Animation.INFINITE);
                        animation.setRepeatMode(Animation.REVERSE);
                        ivBx.startAnimation(animation);
                        ivBx.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                Intent intent = new Intent(mContext, MinePrizeActivity.class);
                                intent.putExtra("status", 1);
                                startActivity(intent);
                            }
                        });
                    }
                } else {
                    ivBx.clearAnimation();
                    ivBx.setVisibility(View.GONE);
                }
                //是否实名认证
                if (!EncodeAndStringTool.isStringEmpty(boxBean.getIsRealName())) {
                    if (boxBean.getIsRealName().equals("true")) {
                        SaveUserInfo.getInstance(mContext).setUserInfo("cert", "1");
                    } else {
                        SaveUserInfo.getInstance(mContext).setUserInfo("cert", "0");
                    }
                }
            }
        } else if ("configDialog".equals(action)) {
            ConfigDialogBean configDialogBean = (ConfigDialogBean) response.getDat();
            MainActivityDialogInfo.info(configDialogBean, mContext);
        } else if ("homeNotice".equals(action)) {
            homeNoticeBeanList = (List<HomeNoticeBean>) response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(response.getDat()) && !homeNoticeBeanList.isEmpty()) {
                String str = JSON.toJSONString(homeNoticeBeanList);
                if (HomeNoticeBeanString != null && HomeNoticeBeanString.equals(str)) {
                    return;
                }
                HomeNoticeBeanString = str;

                rlAd.setVisibility(View.VISIBLE);
                //公告
                List<String> info = new ArrayList<>();
                for (int i = 0; i < homeNoticeBeanList.size(); i++) {
                    info.add(homeNoticeBeanList.get(i).getContent());
                }
                scrollAd.setDatas(info);
            } else {
                rlAd.setVisibility(View.GONE);
            }
        } else if ("homeBottomInfo".equals(action)) {    //首页底部电话信息
            HomeBottomInfoBean homeBottomInfoBean = (HomeBottomInfoBean) response.getDat();
            List<HomeBottomInfoBean.ResultBean> resultBeans = homeBottomInfoBean.getResult();
            if (!EncodeAndStringTool.isListEmpty(resultBeans)) {
                tvNumber1.setVisibility(View.GONE);
                tvNumber2.setVisibility(View.GONE);
                homeBottomResult.clear();
                homeBottomResult.addAll(resultBeans);
                homeBottomInfoAdapter.notifyDataSetChanged();
            } else {
                tvNumber1.setVisibility(View.VISIBLE);
                tvNumber2.setVisibility(View.VISIBLE);
            }

        } else if ("getHomefloatwindow".equals(action)) {
            //首页浮窗
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
    }

    @Override
    public void clear() {
        super.clear();

        bannerDataString = null;
        systemInfosString = null;
        mainConfigBeanString = null;
        treeString = null;
        thermalString = null;
        oftenString = null;
        recommendString = null;
        HomeNoticeBeanString = null;
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


}


