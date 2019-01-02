package com.shuyun.qapp.ui.homepage;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.GroupTreeAdapter;
import com.shuyun.qapp.adapter.HomeSortAdapter;
import com.shuyun.qapp.adapter.HotGroupAdapter;
import com.shuyun.qapp.adapter.MarkBannerAdapter;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.BannerBean;
import com.shuyun.qapp.bean.BannerItem;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.bean.HomeGroupsBean;
import com.shuyun.qapp.bean.HomeNoticeBean;
import com.shuyun.qapp.bean.MainConfigBean;
import com.shuyun.qapp.bean.MarkBannerItem;
import com.shuyun.qapp.bean.SystemInfo;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.LoginDataManager;
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
import com.shuyun.qapp.view.ITextBannerItemClickListener;
import com.shuyun.qapp.view.InviteSharePopupUtil;
import com.shuyun.qapp.view.LoginJumpUtil;
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
import cn.kevin.banner.BannerAdapter;
import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;
import cn.kevin.banner.transformer.YZoomTransFormer;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;


/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements OnRemotingCallBackListener<Object> {
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
    private List<IBannerItem> bannerList = new ArrayList<>();
    private List<BannerBean> bannerData = new ArrayList<>();
    private String bannerDataString = null;
    private BannerAdapter bannerAdapter;

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

    private String systemInfosString = null;
    private String mainConfigBeanString = null;
    private String treeString = null;
    private String thermalString = null;
    private String oftenString = null;
    private String recommendString = null;
    private String HomeNoticeBeanString = null;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity();

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
        }
    }

    private void init() {
        //设置TextBannerView点击监听事件，返回点击的data数据, 和position位置
        scrollAd.setItemOnClickListener(new ITextBannerItemClickListener() {
            @Override
            public void onItemClick(String data, int position) {
                HomeNoticeBean homeNoticeBean = homeNoticeBeanList.get(position);
                LoginDataManager.instance().addData(LoginDataManager.HOME_NOTICE_LOGIN, homeNoticeBean);
                LoginJumpUtil.dialogSkip(homeNoticeBean.getAction(),
                        mContext,
                        homeNoticeBean.getGroupId(),
                        homeNoticeBean.getH5Url(),
                        homeNoticeBean.getIsLogin());
            }
        });

        bannerList.add(new BannerItem("https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/banner/xiaji.jpg"));
        //设置轮播图
        bannerAdapter = new BannerAdapter(new GlideImageLoader());
        bannerAdapter.setData(mContext, bannerList);
        mBannerView.setBannerAdapter(bannerAdapter);

        //设置index 在viewpager下面
        ViewPager mViewpager = (ViewPager) mBannerView.getChildAt(0);
        //为ViewPager设置高度
        ViewGroup.LayoutParams params = mViewpager.getLayoutParams();
        params.height = getResources().getDimensionPixelSize(R.dimen.viewPager_01);
        mViewpager.setLayoutParams(params);
        //设置时间，时间越长，速度越慢
        ViewPagerScroller pagerScroller = new ViewPagerScroller(getActivity());
        pagerScroller.setScrollDuration(400);
        pagerScroller.initViewPagerScroll(mViewpager);

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
        mBannerView.setPageTransformer(true, new YZoomTransFormer(0.9f)); //banner动画

        //常答题组
        MarkBannerItem item = new MarkBannerItem("https://image-syksc.oss-cn-shanghai.aliyuncs.com/syksc/app/banner/xiaji.jpg");
        item.setMarkLabel("默认");
        oftenList.add(item);
        markBannerAdapter = new MarkBannerAdapter(new GlideImageLoader());
        markBannerAdapter.setData(mContext, oftenList);
        alwaysBanner.setBannerAdapter(markBannerAdapter);

        //设置index 在viewpager下面
        ViewPager mViewpagerOften = (ViewPager) alwaysBanner.getChildAt(0);
        //为ViewPager设置高度
        params = mViewpagerOften.getLayoutParams();
        params.height = getResources().getDimensionPixelSize(R.dimen.viewPager_02);
        mViewpagerOften.setLayoutParams(params);
        //设置时间，时间越长，速度越慢
        ViewPagerScroller pagerScrollerOften = new ViewPagerScroller(getActivity());
        pagerScrollerOften.setScrollDuration(400);
        pagerScrollerOften.initViewPagerScroll(mViewpagerOften);

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
        groupThermalAdapter.setOnItemClickLitsener(new GroupTreeAdapter.OnItemClickListener() {
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

        //分类
        treeGroupAdapter = new HomeSortAdapter(groupClassifyBeans, mContext);
        treeGroupAdapter.setOnItemClickLitsener(new GroupTreeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
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
            case R.id.tv_number2: //招商电话
                PhoneUtils.dial("0571-86875104");
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
     * 获取跑马灯消息
     */
    private void loadSystemInfo() {
        RemotingEx.doRequest("getSystemInfo", ApiServiceBean.getSystemInfo(), null, this);
    }

    /**
     * 获取活动配置信息
     */
    private void getConfigInfo() {
        RemotingEx.doRequest("configMainActivity", ApiServiceBean.configMainActivity(), null, this);
    }

    /**
     * 首页题组
     */
    private void loadHomeGroups() {
        RemotingEx.doRequest("getHomeGroups", ApiServiceBean.getHomeGroups(), new Object[]{AppUtils.getAppVersionName()}, this);
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
            }
        } catch (Exception e) {

        }
    }

    /**
     * 获取宝箱数量
     */
    private void loadTreasureBoxNum() {
        RemotingEx.doRequest("loadTreasureBoxNum", ApiServiceBean.getTreasureNumV2(), null, this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //获取活动弹框信息
    private void getDialogInfo() {
        RemotingEx.doRequest("configDialog", ApiServiceBean.configDialog(), null, this);
    }

    //获取公告信息
    private void getNoticeInfo() {
        RemotingEx.doRequest("homeNotice", ApiServiceBean.homeNotice(), null, this);
    }

    // scrollview滚动监听
    private void changeTitle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > dp2px(60)) {
                        tvInvite.setTextColor(Color.parseColor("#333333"));
                        ivCommonRightIcon.setImageResource(R.mipmap.message_n);//右侧消息按钮;
                        tvCommonTitle.setVisibility(View.VISIBLE);
                    } else {
                        tvInvite.setTextColor(Color.parseColor("#ffffff"));
                        ivCommonRightIcon.setImageResource(R.mipmap.messagew_n);//右侧消息按钮;
                        tvCommonTitle.setVisibility(View.GONE);
                    }
                    //顶部栏颜色渐变
                    if (scrollY <= 0) {
                        rlTitle.setBackgroundColor(Color.argb((int) 0, 255, 255, 255));//AGB由相关工具获得，或者美工提供
                    } else if (scrollY > 0 && scrollY <= dp2px(130)) {
                        float scale = (float) scrollY / dp2px(130);
                        float alpha = (255 * scale);
                        // 只是layout背景透明
                        rlTitle.setBackgroundColor(Color.argb((int) alpha, 255, 255, 255));
                    } else {
                        rlTitle.setBackgroundColor(Color.argb(255, 255, 255, 255));
                    }
                }
            });
        }
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

        //获取活动配置信息
        getConfigInfo();

        //获取宝箱数量
        if (AppConst.isLogin()) {
            loadTreasureBoxNum();
        } else {
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
    public void onSucceed(String action, DataResponse<Object> response) {
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
                    bannerList.add(new BannerItem(bannerData.get(i).getPicture()));
                }
                bannerAdapter.setData(mContext, bannerList);
                //重新生成单位条
                mBannerView.setBannerAdapter(bannerAdapter);
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
                            oftenList.add(item);
                        }
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
}


