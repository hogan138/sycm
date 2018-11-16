package com.shuyun.qapp.ui.homepage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
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

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.GroupTreeAdapter;
import com.shuyun.qapp.adapter.HomeBannerAdapter;
import com.shuyun.qapp.adapter.HomeSortAdapter;
import com.shuyun.qapp.adapter.HotGroupAdapter;
import com.shuyun.qapp.adapter.MarkBannerAdapter;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.BannerBean;
import com.shuyun.qapp.bean.BannerItem;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.bean.HomeGroupsBean;
import com.shuyun.qapp.bean.MainConfigBean;
import com.shuyun.qapp.bean.MarkBannerItem;
import com.shuyun.qapp.bean.SystemInfo;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.receiver.MyReceiver;
import com.shuyun.qapp.ui.classify.ClassifyActivity;
import com.shuyun.qapp.ui.loader.GlideImageLoader;
import com.shuyun.qapp.ui.loader.GlideImageLoader1;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.mine.MinePrizeActivity;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.InformatListenner;
import com.shuyun.qapp.utils.NotificationsUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.view.H5JumpUtil;
import com.shuyun.qapp.view.InviteSharePopupUtil;
import com.shuyun.qapp.view.MainActivityDialogInfo;
import com.shuyun.qapp.view.NotifyDialog;
import com.shuyun.qapp.view.OvalImageView;
import com.shuyun.qapp.view.ViewPagerScroller;
import com.sunfusheng.marqueeview.MarqueeView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.kevin.banner.BannerViewPager;
import cn.kevin.banner.IBannerItem;
import cn.kevin.banner.transformer.YZoomTransFormer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * 首页
 */
public class HomeFragment extends Fragment {
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


    /**
     * 网络获取到推荐题组列表
     */
    private List<GroupBean> groupBeans;

    private Activity mContext;
    private MyReceiver msgReceiver;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /**
         * 检测微信是否安装,如果没有安装,需不显示分享按钮;如果安装了微信则显示分享按钮.
         */
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            tvInvite.setVisibility(View.GONE);
        } else {
            tvInvite.setVisibility(View.VISIBLE);
        }
        tvCommonTitle.setText("全民共进");
        ivCommonRightIcon.setImageResource(R.mipmap.message_n);//右侧消息按钮;

        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();

        //token的有效时间
        Long expire = (Long) SharedPrefrenceTool.get(mContext, "expire", System.currentTimeMillis());
        long currentTimeMillis = System.currentTimeMillis();
        if (!AppConst.isLogon() || currentTimeMillis >= expire) {
            //拉起登录界面
            Intent intent = new Intent(mContext, LoginActivity.class);
            startActivity(intent);
            mContext.finish();
            return;
        }

        /**
         * 注册极光推送监听
         */
        msgReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.jpush.android.intent.MESSAGE_RECEIVED");
        intentFilter.addAction("cn.jpush.android.intent.NOTIFICATION_RECEIVED");
        intentFilter.addAction("cn.jpush.android.intent.NOTIFICATION_OPENED");
        intentFilter.addAction("cn.jpush.android.intent.NOTIFICATION_CLICK_ACTION");
        intentFilter.addAction("cn.jpush.android.intent.CONNECTION");
        intentFilter.addCategory("com.shuyun.qapp");
        mContext.registerReceiver(msgReceiver, intentFilter);
        msgReceiver.setOnMsgListenner(new InformatListenner() {
            @Override
            public void loadInfoRefreshUi() {
                if (AppConst.hasMsg) {//如果有消息
                    ivCommonRightIcon.setImageResource(R.mipmap.message_h);
                }
            }
        });

        /**
         * 获取banner轮播数据
         */
        loadHomeBanners();

        /**
         * 获取全民播报
         */
        loadSystemInfo();

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {  //显示
            /**
             * 获取弹框信息
             */
            getDialogInfo();

            /**
             * 首页题组
             */
            loadHomeGroups();

            /**
             * 获取活动配置信息
             */
            getConfigInfo();

            /**
             * 获取宝箱数量
             */
            loadTreasureBoxNum();
        }
    }

    @OnClick({R.id.tv_invite, R.id.iv_common_right_icon,
            R.id.tv_number1, R.id.tv_number2, R.id.ll_change_group,
            R.id.rl_commend_one, R.id.rl_commend_two
    })
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
                if (circle_anim != null) {
                    ivChange.startAnimation(circle_anim);  //开始动画
                }
                //轮训换题组
                rollRecommendGroup();
                break;
            case R.id.rl_commend_one://推荐题组一
                if (!EncodeAndStringTool.isObjectEmpty(recommendGroup1)) {
                    int groupId = recommendGroup1.getId();
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("h5Url", recommendGroup1.getH5Url());
                    startActivity(intent);
                }
                break;
            case R.id.rl_commend_two://推荐题组二
                if (!EncodeAndStringTool.isObjectEmpty(recommendGroup2)) {
                    int groupId = recommendGroup2.getId();
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
        ApiService apiService = BasePresenter.create(8000);
        apiService.getHomeBanner()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<BannerBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<BannerBean>> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            try {
                                final List<BannerBean> bannerData = listDataResponse.getDat();
                                if (!EncodeAndStringTool.isListEmpty(bannerData)) {
                                    //设置轮播图
//                                    BannerAdapter adapter = new BannerAdapter(new GlideImageLoader());
                                    HomeBannerAdapter adapter = new HomeBannerAdapter(new GlideImageLoader1());
                                    List<IBannerItem> list = new ArrayList<>();
                                    for (int i = 0; i < bannerData.size(); i++) {
                                        list.add(new BannerItem(bannerData.get(i).getPicture()));
                                    }
                                    adapter.setData(getContext(), list);
                                    mBannerView.setBannerAdapter(adapter);

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
                                                if (data.ImageUrl().equals(bannerData.get(i).getPicture())) {
                                                    String action = bannerData.get(i).getAction();
                                                    String h5Url = bannerData.get(i).getH5Url();
                                                    try {
                                                        H5JumpUtil.dialogSkip(action, bannerData.get(i).getContent(), h5Url, mContext, llHomeFragment);
                                                    } catch (Exception e) {
                                                    }
                                                }
                                            }
                                        }
                                    });
                                    mBannerView.setPageTransformer(true, new YZoomTransFormer(.8f)); //banner动画
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {//错误码提示
                            if (listDataResponse.getErr().equals("U0001")) {
                                mContext.finish();
                            }
                            ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
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

    /**
     * 获取跑马灯消息
     */
    private void loadSystemInfo() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getSystemInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<SystemInfo>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<SystemInfo>> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            List<SystemInfo> systemInfos = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(systemInfos)) {
                                try {
                                    llMarqueeView.setVisibility(View.VISIBLE);
                                    /**
                                     * 跑马灯数据
                                     */
                                    List<String> info = new ArrayList<>();
                                    for (int i = 0; i < systemInfos.size(); i++) {
                                        info.add(systemInfos.get(i).getMsg());
                                    }
                                    if (!EncodeAndStringTool.isListEmpty(info)) {
                                        marqueeView.startWithList(info);
                                    }
                                } catch (Exception e) {
                                }
                            } else {
                                llMarqueeView.setVisibility(View.GONE);
                            }
                        } else {
                            if (dataResponse.getErr().equals("U0001")) {
                                mContext.finish();
                            }
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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

    /**
     * 获取活动配置信息
     */
    private void getConfigInfo() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.configMainActivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<MainConfigBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<MainConfigBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            MainConfigBean mainConfigBean = dataResponse.getDat();
                            try {
                                //动态添加布局
                                activityRegion.removeAllViews();
                                activityRegion.addView(ActivityRegionManager.getView(mContext, mainConfigBean, llHomeFragment));
                            } catch (Exception e) {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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

    /**
     * 首页题组
     */
    private void loadHomeGroups() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getHomeGroups(AppUtils.getAppVersionName())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<HomeGroupsBean>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<HomeGroupsBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            try {
                                final HomeGroupsBean homeGroupsBean = listDataResponse.getDat();
                                if (!EncodeAndStringTool.isObjectEmpty(homeGroupsBean)) {
                                    if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getRecommend())) { //推荐题组
                                        groupBeans = homeGroupsBean.getRecommend();
                                        if (!EncodeAndStringTool.isListEmpty(groupBeans)) {
                                            recommendIndex = 0;
                                            rollRecommendGroup();
                                            if (groupBeans.size() <= 2) {
                                                llChangeGroup.setVisibility(View.GONE);
                                            } else {
                                                llChangeGroup.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    }

                                    if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getOften())) { //常答题组
                                        llOften.setVisibility(View.VISIBLE);
                                        alwaysBanner.setVisibility(View.VISIBLE);

                                        //常答题组
                                        MarkBannerAdapter adapter1 = new MarkBannerAdapter(new GlideImageLoader());
                                        List<IBannerItem> list1 = new ArrayList<>();
                                        for (int i = 0; i < homeGroupsBean.getOften().size(); i++) {
                                            MarkBannerItem item = new MarkBannerItem(homeGroupsBean.getOften().get(i).getPicture());
                                            item.setMarkLabel(homeGroupsBean.getOften().get(i).getName());
                                            list1.add(item);
                                        }
                                        adapter1.setData(getContext(), list1);
                                        alwaysBanner.setBannerAdapter(adapter1);

                                        //设置index 在viewpager下面
                                        ViewPager mViewpager1 = (ViewPager) alwaysBanner.getChildAt(0);
                                        //为ViewPager设置高度
                                        ViewGroup.LayoutParams params = mViewpager1.getLayoutParams();
                                        params.height = getResources().getDimensionPixelSize(R.dimen.viewPager_02);
                                        mViewpager1.setLayoutParams(params);
                                        //设置时间，时间越长，速度越慢
                                        ViewPagerScroller pagerScroller = new ViewPagerScroller(getActivity());
                                        pagerScroller.setScrollDuration(400);
                                        pagerScroller.initViewPagerScroll(mViewpager1);

                                        alwaysBanner.setBannerItemClick(new BannerViewPager.OnBannerItemClick<IBannerItem>() {
                                            @Override
                                            public void onClick(IBannerItem data) {
                                                for (int i = 0; i < homeGroupsBean.getOften().size(); i++) {
                                                    if (data.ImageUrl().equals(homeGroupsBean.getOften().get(i).getPicture())) {
                                                        try {
                                                            Intent intent = new Intent(mContext, WebAnswerActivity.class);
                                                            intent.putExtra("groupId", homeGroupsBean.getOften().get(i).getId());
                                                            intent.putExtra("h5Url", homeGroupsBean.getOften().get(i).getH5Url());
                                                            startActivity(intent);
                                                        } catch (Exception e) {
                                                        }
                                                    }
                                                }
                                            }
                                        });
                                    } else {
                                        llOften.setVisibility(View.GONE);
                                        alwaysBanner.setVisibility(View.GONE);
                                    }

                                    if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getThermal())) {   //大家都在答
                                        final List<GroupBean> groupData = homeGroupsBean.getThermal();
                                        try {
                                            HotGroupAdapter hotGroupAdapter = new HotGroupAdapter(groupData, mContext);
                                            hotGroupAdapter.setOnItemClickLitsener(new GroupTreeAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    int groupId = groupData.get(position).getId();
                                                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                                                    intent.putExtra("groupId", groupId);
                                                    intent.putExtra("h5Url", groupData.get(position).getH5Url());
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
                                            rvHotGroup.setAdapter(hotGroupAdapter);
                                        } catch (Exception e) {
                                        }
                                    }
                                    if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getTree())) {  //分类
                                        final List<GroupClassifyBean> groupClassifyBeans = homeGroupsBean.getTree();
                                        try {
                                            HomeSortAdapter hotGroupAdapter = new HomeSortAdapter(groupClassifyBeans, mContext);
                                            hotGroupAdapter.setOnItemClickLitsener(new GroupTreeAdapter.OnItemClickListener() {
                                                @Override
                                                public void onItemClick(View view, int position) {
                                                    int groupId = groupClassifyBeans.get(position).getId();
                                                    Intent intent1 = new Intent(mContext, ClassifyActivity.class);
                                                    intent1.putExtra("id", groupId);
                                                    startActivity(intent1);
                                                }
                                            });
                                            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 1) {
                                                @Override
                                                public boolean canScrollVertically() {//禁止layout垂直滑动
                                                    return false;
                                                }
                                            };
                                            gridLayoutManager.setSmoothScrollbarEnabled(true);
                                            gridLayoutManager.setAutoMeasureEnabled(true);
                                            rvGroupSortGroup.setLayoutManager(gridLayoutManager);
                                            //解决数据加载不完的问题
                                            rvGroupSortGroup.setHasFixedSize(true);
                                            rvGroupSortGroup.setNestedScrollingEnabled(false);
                                            rvGroupSortGroup.setAdapter(hotGroupAdapter);
                                        } catch (Exception e) {
                                        }
                                    }
                                }
                            } catch (Exception e) {
                            }
                        } else {//错误码提示
                            if (listDataResponse.getErr().equals("U0001")) {
                                mContext.finish();
                            }
                            ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
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

    GroupBean recommendGroup1;
    GroupBean recommendGroup2;
    int recommendIndex = 0;

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
        ApiService apiService = BasePresenter.create(8000);
        apiService.getTreasureNumV2()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<BoxBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<BoxBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            BoxBean boxBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(boxBean)) {
                                try {
                                    if (boxBean.getCount() > 0) {//如果宝箱数量大于0,首页左下角显示宝箱摇晃动画
                                        if (boxBean.getCount() == 1 && boxBean.getSource() == 3) {
                                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FirstRun", 0);
                                            Boolean first_run = sharedPreferences.getBoolean("Main", true);
                                            if (first_run) {
                                                sharedPreferences.edit().putBoolean("Main", false).commit();
                                                Intent intent = new Intent(mContext, WebPrizeBoxActivity.class);
                                                intent.putExtra("BoxBean", boxBean);
                                                intent.putExtra("main_box", "main_box");
                                                startActivity(intent);
                                            }
                                        } else {
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

                                } catch (Exception e) {

                                }
                            }
                        } else {
                            if (dataResponse.getErr().equals("U0001")) {
                                mContext.finish();
                            }
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    CountDownTimer timer;

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("HomeFragment"); //统计页面，"MainScreen"为页面名称，可自定义
        long newNowMills = (long) SharedPrefrenceTool.get(mContext, "nowMills", (TimeUtils.getNowMills() - 86400000 * 7));//获取首选项中的时间,默认值为七天前的时间戳
        long timeSpan = TimeUtils.getTimeSpan(newNowMills, TimeUtils.getNowMills(), TimeConstants.DAY);//当前时间和首选项中时间差
        /**
         * 如果通知权限关闭且当前时间和首选项中的时间相差大于等于7天(每7天提醒一次),弹窗提示
         */
        if (!NotificationsUtils.isNotificationEnabled(mContext) && timeSpan >= 7) {
            //当前弹窗提示时间
            long nowMills = TimeUtils.getNowMills();
            SharedPrefrenceTool.put(mContext, "nowMills", (long) nowMills);
            //如果通知栏未打开,弹出前往设置打开通知栏的弹窗
            NotifyDialog.dialogShow(mContext);
        }

        //5秒更新推荐题组
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
        }.start();

    }

    //获取活动弹框信息
    private void getDialogInfo() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.configDialog()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<ConfigDialogBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<ConfigDialogBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            try {
                                ConfigDialogBean configDialogBean = dataResponse.getDat();
                                MainActivityDialogInfo.info(configDialogBean, mContext, llHomeFragment);
                            } catch (Exception e) {
                            }

                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (msgReceiver != null) {
            mContext.unregisterReceiver(msgReceiver);
        }
    }
}


