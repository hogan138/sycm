package com.shuyun.qapp.ui.homepage;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.adapter.HotGroupAdapter;
import com.shuyun.qapp.bean.ConfigDialogBean;
import com.shuyun.qapp.bean.InviteBean;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.BannerBean;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.GroupBean;
import com.shuyun.qapp.bean.GroupClassifyBean;
import com.shuyun.qapp.bean.HomeGroupsBean;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.bean.SystemInfo;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.answer.WebAnswerActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.mine.WebPrizeBoxActivity;
import com.shuyun.qapp.receiver.MyReceiver;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.net.InformatListenner;
import com.shuyun.qapp.ui.classify.ClassifyActivity;
import com.shuyun.qapp.adapter.GroupTreeAdapter;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.mine.MinePrizeActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.NotificationsUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ScannerUtils;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.view.RoundImageView;
import com.sunfusheng.marqueeview.MarqueeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements CommonPopupWindow.ViewInterface {
    @BindView(R.id.iv_common_left_icon)
    RelativeLayout ivCommonLeftIcon;//左侧分享文字可点区域
    @BindView(R.id.tv_left)
    TextView tvLeft;//左侧分享文字
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题
    @BindView(R.id.iv_common_right_icon)
    ImageView ivCommonRightIcon;//右侧消息图标
    @BindView(R.id.ll_home_fragment)
    RelativeLayout llHomeFragment;//layout容器
    @BindView(R.id.banner)
    BGABanner mBannerView;//轮播图
    @BindView(R.id.marqueeView)
    MarqueeView marqueeView;//跑马灯
    @BindView(R.id.tv_change_group)
    TextView tvChangeGroup;//换一批
    @BindView(R.id.iv_group_bg1)
    RoundImageView ivGroupBg1;//1、推荐题组背景图
    @BindView(R.id.iv_group_bg2)
    RoundImageView ivGroupBg2;//2、推荐题组背景图
    @BindView(R.id.tv_group_name1)
    TextView tvGroupName1;//1、推荐题组名字
    @BindView(R.id.tv_group_name2)
    TextView tvGroupName2;//2、推荐题组名字
    @BindView(R.id.rl_active)
    RelativeLayout rlActive;//活动
    @BindView(R.id.rl_life)
    RelativeLayout rlLife;//生活
    @BindView(R.id.rl_culture)
    RelativeLayout rlCulture;//文学
    @BindView(R.id.rl_video)
    RelativeLayout rlVideo;//影视剧
    @BindView(R.id.rl_music)
    RelativeLayout rlMusic;//音乐
    @BindView(R.id.rl_history)
    RelativeLayout rlHistory;//历史
    @BindView(R.id.rl_edu)
    RelativeLayout rlEdu;//教育
    @BindView(R.id.rl_more)
    RelativeLayout rlMore;//更多
    @BindView(R.id.iv_invite_prize)
    ImageView ivInvitePrize;//邀请有奖图片
    @BindView(R.id.rv_hot_group)
    RecyclerView rvHotGroup;//热门题组

    @BindView(R.id.iv_group_icon01)
    ImageView ivGroupIcon01;//题组分类1
    @BindView(R.id.tv_hot01)
    TextView tvHot01;
    @BindView(R.id.tv_group_name01)
    TextView tvGroupName01;
    @BindView(R.id.iv_group_icon02)
    ImageView ivGroupIcon02;//题组分类2
    @BindView(R.id.tv_hot02)
    TextView tvHot02;
    @BindView(R.id.tv_group_name02)
    TextView tvGroupName02;
    @BindView(R.id.iv_group_icon03)
    ImageView ivGroupIcon03;//题组分类3
    @BindView(R.id.tv_hot03)
    TextView tvHot03;
    @BindView(R.id.tv_group_name03)
    TextView tvGroupName03;
    @BindView(R.id.iv_group_icon04)
    ImageView ivGroupIcon04;//题组分类4
    @BindView(R.id.tv_hot04)
    TextView tvHot04;
    @BindView(R.id.tv_group_name04)
    TextView tvGroupName04;
    @BindView(R.id.iv_group_icon05)
    ImageView ivGroupIcon05;//题组分类5
    @BindView(R.id.tv_hot05)
    TextView tvHot05;
    @BindView(R.id.tv_group_name05)
    TextView tvGroupName05;
    @BindView(R.id.iv_group_icon06)
    ImageView ivGroupIcon06;//题组分类6
    @BindView(R.id.tv_hot06)
    TextView tvHot06;
    @BindView(R.id.tv_group_name06)
    TextView tvGroupName06;
    @BindView(R.id.iv_group_icon07)
    ImageView ivGroupIcon07;//题组分类7
    @BindView(R.id.tv_hot07)
    TextView tvHot07;
    @BindView(R.id.tv_group_name07)
    TextView tvGroupName07;
    @BindView(R.id.iv_more)
    ImageView ivMore;//题组分类8
    @BindView(R.id.tv_hot08)
    TextView tvHot08;
    @BindView(R.id.tv_group_name08)
    TextView tvGroupName08;
    @BindView(R.id.ll_change_group)
    LinearLayout llChangeGroup;//换一批
    @BindView(R.id.iv_bx)
    ImageView ivBx;//宝箱抖动的动画
    Unbinder unbinder;
    @BindView(R.id.iv_against)
    ImageView ivAgainst;
    @BindView(R.id.ll_invite)
    LinearLayout llInvite;

    private int SHARE_CHANNEL;
    private static final String TAG = "HomeFragment";

    /**
     * 网络获取到推荐题组列表
     */
    private List<GroupBean> groupBeans;
    private Activity mContext;
    private GroupClassifyBean groupClassifyBean0;
    private GroupClassifyBean groupClassifyBean1;
    private GroupClassifyBean groupClassifyBean2;
    private GroupClassifyBean groupClassifyBean3;
    private GroupClassifyBean groupClassifyBean4;
    private GroupClassifyBean groupClassifyBean5;
    private GroupClassifyBean groupClassifyBean6;
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
            tvLeft.setVisibility(View.GONE);
        } else {
            tvLeft.setText("分享");
        }
        tvCommonTitle.setText("全民共进");
        ivCommonRightIcon.setImageResource(R.mipmap.message_n);//右侧消息按钮;

        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        Long expire = (Long) SharedPrefrenceTool.get(mContext, "expire", System.currentTimeMillis());//token的有效时间
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
         * 获取并设置轮播数据
         */
        loadHomeBanners();
        /**
         * 获取跑马灯消息
         */
        loadSystemInfo();

        /**
         * 获取宝箱数量
         */
        loadTreasureBoxNum();

        /**
         * 邀请有奖
         */
        invite();
    }

    @OnClick({R.id.iv_common_left_icon, R.id.iv_common_right_icon, R.id.ll_change_group, R.id.iv_group_bg1, R.id.iv_group_bg2,
            R.id.rl_active, R.id.rl_life, R.id.rl_culture, R.id.rl_video,
            R.id.rl_music, R.id.rl_history, R.id.rl_edu, R.id.rl_more,
            R.id.iv_invite_prize, R.id.iv_against})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left_icon://邀请分享
                showSharedPop();
                break;
            case R.id.iv_common_right_icon:
                ivCommonRightIcon.setImageResource(R.mipmap.message_n);//右侧消息按钮;
                startActivity(new Intent(mContext, InformationActivity.class));
                break;
            case R.id.ll_change_group:
                //轮训换题组
                rollRecommendGroup();
                break;
            case R.id.iv_group_bg1://推荐题组一//TODO
                if (!EncodeAndStringTool.isObjectEmpty(recommendGroup1)) {
                    int groupId = recommendGroup1.getId();
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("h5Url", recommendGroup1.getH5Url());
                    startActivity(intent);
                }
                break;
            case R.id.iv_group_bg2://推荐题组二
                if (!EncodeAndStringTool.isObjectEmpty(recommendGroup2)) {
                    int groupId = recommendGroup2.getId();
                    Intent intent = new Intent(mContext, WebAnswerActivity.class);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("h5Url", recommendGroup2.getH5Url());
                    startActivity(intent);
                }
                break;
            case R.id.rl_active://活动
                Intent intent0 = new Intent(mContext, ClassifyActivity.class);
                intent0.putExtra("id", groupClassifyBean0.getId());
                startActivity(intent0);
                break;
            case R.id.rl_life://生活
                Intent intent1 = new Intent(mContext, ClassifyActivity.class);
                intent1.putExtra("id", groupClassifyBean1.getId());
                startActivity(intent1);
                break;
            case R.id.rl_culture://文学
                Intent intent2 = new Intent(mContext, ClassifyActivity.class);
                intent2.putExtra("id", groupClassifyBean2.getId());
                startActivity(intent2);
                break;
            case R.id.rl_video://影视剧
                Intent intent3 = new Intent(mContext, ClassifyActivity.class);
                intent3.putExtra("id", groupClassifyBean3.getId());
                startActivity(intent3);
                break;
            case R.id.rl_music://音乐
                Intent intent4 = new Intent(mContext, ClassifyActivity.class);
                intent4.putExtra("id", groupClassifyBean4.getId());
                startActivity(intent4);
                break;
            case R.id.rl_history://历史
                Intent intent5 = new Intent(mContext, ClassifyActivity.class);
                intent5.putExtra("id", groupClassifyBean5.getId());
                startActivity(intent5);
                break;
            case R.id.rl_edu://教育
                Intent intent6 = new Intent(mContext, ClassifyActivity.class);
                intent6.putExtra("id", groupClassifyBean6.getId());
                startActivity(intent6);
                break;
            case R.id.rl_more://更多
                Intent intent7 = new Intent(mContext, ClassifyActivity.class);
                intent7.putExtra("id", 0);
                startActivity(intent7);
                break;
            case R.id.iv_against:
                startActivity(new Intent(getActivity(), MainAgainstActivity.class));
                break;
            case R.id.iv_invite_prize://邀请有奖
                //邀请分享
                Intent intent = new Intent();
                intent.setClass(mContext, SharedPrzieActivity.class);
                intent.putExtra("h5Url", invite_h5Url);
                startActivity(intent);
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
                                    mBannerView.setData(R.layout.banner_item, bannerData, null);
                                    mBannerView.setAdapter(new BGABanner.Adapter<View, BannerBean>() {
                                        @Override
                                        public void fillBannerItem(BGABanner banner, View itemView, BannerBean bannerBean, int position) {
                                            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
                                            ImageLoaderManager.LoadImage(mContext, bannerBean.getPicture(), imageView, R.mipmap.zw01);
                                        }
                                    });
                                    mBannerView.setDelegate(new BGABanner.Delegate<View, BannerBean>() {
                                        @Override
                                        public void onBannerItemClick(BGABanner banner, View itemView, BannerBean model, int position) {
                                            BannerBean bannerBean = bannerData.get(position);
                                            if (bannerBean.getType() == 3) {//题组跳转
                                                if (!EncodeAndStringTool.isStringEmpty(bannerBean.getUrl())) {
                                                    try {
                                                        Intent intent = new Intent(mContext, WebAnswerActivity.class);
                                                        intent.putExtra("groupId", Integer.parseInt(bannerBean.getUrl()));
                                                        intent.putExtra("h5Url", bannerBean.getH5Url());
                                                        startActivity(intent);
                                                    } catch (Exception e) {
                                                    }

                                                }
                                            } else if (bannerBean.getType() == 2) {//内部链接
                                                if (!EncodeAndStringTool.isStringEmpty(bannerBean.getUrl())) {
                                                    Intent intent = new Intent(mContext, WebBannerActivity.class);
                                                    intent.putExtra("url", bannerBean.getUrl());
                                                    intent.putExtra("name", bannerBean.getName());//名称 标题
                                                    startActivity(intent);
                                                }
                                            } else if (bannerBean.getType() == 1) {//外部链接
                                                if (!EncodeAndStringTool.isStringEmpty(bannerBean.getUrl())) {
                                                    Uri uri = Uri.parse(bannerBean.getUrl());
                                                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                    startActivity(intent);
                                                }
                                            }
                                        }
                                    });
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
                                /**
                                 * 跑马灯数据
                                 */
                                List<String> info = new ArrayList<>();
                                for (int i = 0; i < systemInfos.size(); i++) {
                                    info.add(systemInfos.get(i).getMsg());
                                }
                                try {
                                    if (!EncodeAndStringTool.isListEmpty(info)) {
                                        marqueeView.startWithList(info);
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

    /**
     * 首页题组
     */
    private void loadHomeGroups() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getHomeGroups(APKVersionCodeTools.getVerName(mContext))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<HomeGroupsBean>>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<HomeGroupsBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            HomeGroupsBean homeGroupsBean = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(homeGroupsBean)) {
                                if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getRecommend())) {
                                    groupBeans = homeGroupsBean.getRecommend();
                                    if (!EncodeAndStringTool.isListEmpty(groupBeans)) {
                                        recommendIndex = 0;
                                        rollRecommendGroup();
                                    }
                                }
                                if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getThermal())) {
                                    final List<GroupBean> groupData = homeGroupsBean.getThermal();
                                    mContext.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                rvHotGroup.setHasFixedSize(true);
                                                rvHotGroup.setNestedScrollingEnabled(false);
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

                                                GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
                                                rvHotGroup.setLayoutManager(gridLayoutManager);
                                                rvHotGroup.setAdapter(hotGroupAdapter);
                                            } catch (Exception e) {

                                            }

                                        }
                                    });
                                }
                                if (!EncodeAndStringTool.isListEmpty(homeGroupsBean.getTree())) {
                                    try {
                                        List<GroupClassifyBean> groupClassifyBeans = homeGroupsBean.getTree();
                                        groupClassifyBean0 = groupClassifyBeans.get(0);
                                        groupClassifyBean1 = groupClassifyBeans.get(1);
                                        groupClassifyBean2 = groupClassifyBeans.get(2);
                                        groupClassifyBean3 = groupClassifyBeans.get(3);
                                        groupClassifyBean4 = groupClassifyBeans.get(4);
                                        groupClassifyBean5 = groupClassifyBeans.get(5);
                                        groupClassifyBean6 = groupClassifyBeans.get(6);
                                        if (!EncodeAndStringTool.isObjectEmpty(groupClassifyBean0)) {
                                            ImageLoaderManager.LoadImage(mContext, groupClassifyBean0.getPicture(), ivGroupIcon01, R.mipmap.active);
                                            tvGroupName01.setText(groupClassifyBean0.getName());
                                        }
                                        if (!EncodeAndStringTool.isObjectEmpty(groupClassifyBean1)) {
                                            ImageLoaderManager.LoadImage(mContext, groupClassifyBean1.getPicture(), ivGroupIcon02, R.mipmap.life);
                                            tvGroupName02.setText(groupClassifyBean1.getName());
                                        }
                                        if (!EncodeAndStringTool.isObjectEmpty(groupClassifyBean2)) {
                                            ImageLoaderManager.LoadImage(mContext, groupClassifyBean2.getPicture(), ivGroupIcon03, R.mipmap.wenxue);
                                            tvGroupName03.setText(groupClassifyBean2.getName());
                                        }
                                        if (!EncodeAndStringTool.isObjectEmpty(groupClassifyBean3)) {
                                            ImageLoaderManager.LoadImage(mContext, groupClassifyBean3.getPicture(), ivGroupIcon04, R.mipmap.video);
                                            tvGroupName04.setText(groupClassifyBean3.getName());
                                        }
                                        if (!EncodeAndStringTool.isObjectEmpty(groupClassifyBean4)) {
                                            ImageLoaderManager.LoadImage(mContext, groupClassifyBean4.getPicture(), ivGroupIcon05, R.mipmap.music);
                                            tvGroupName05.setText(groupClassifyBean4.getName());
                                        }
                                        if (!EncodeAndStringTool.isObjectEmpty(groupClassifyBean5)) {
                                            ImageLoaderManager.LoadImage(mContext, groupClassifyBean5.getPicture(), ivGroupIcon06, R.mipmap.history);
                                            tvGroupName06.setText(groupClassifyBean5.getName());
                                        }
                                        if (!EncodeAndStringTool.isObjectEmpty(groupClassifyBean6)) {
                                            ImageLoaderManager.LoadImage(mContext, groupClassifyBean6.getPicture(), ivGroupIcon07, R.mipmap.edu);
                                            tvGroupName07.setText(groupClassifyBean6.getName());
                                        }
                                        ivMore.setImageResource(R.mipmap.more);
                                        tvGroupName08.setText("更多");

                                    } catch (Exception e) {
                                    }
                                }
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
                                            } else {
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

    String invite_h5Url = "";

    //邀请有奖
    private void invite() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.prizeShare()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<InviteBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<InviteBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            InviteBean inviteBean = dataResponse.getDat();
                            //邀请有奖
                            try {
                                if (inviteBean.getShare() == 1) {
                                    llInvite.setVisibility(View.VISIBLE);
                                    ImageLoaderManager.LoadImage(mContext, inviteBean.getInvite(), ivInvitePrize, R.mipmap.zw01);
                                    invite_h5Url = inviteBean.getH5Url();
                                    SharedPrefrenceTool.put(mContext, "share", inviteBean.getShare());//是否参与邀请分享 1——参与邀请
                                } else {
                                    llInvite.setVisibility(View.GONE);
                                    ivInvitePrize.setVisibility(View.GONE);
                                    SharedPrefrenceTool.put(mContext, "share", inviteBean.getShare());//是否参与邀请分享 1——参与邀请
                                }
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
            }
        } catch (Exception e) {

        }
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
            dialogShow();
        }

        /**
         * 首页题组
         */
        loadHomeGroups();

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

        //获取弹框信息
        getDialogInfo();

    }


    //开启通知弹出框
    private void dialogShow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.open_notification_popup, null);
        RelativeLayout rl_close = view.findViewById(R.id.rl_close);
        TextView tvOpenNotification = view.findViewById(R.id.tv_open_notification);
        //builer.setView(v);//这里如果使用builer.setView(v)，自定义布局只会覆盖title和button之间的那部分
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
//        dialog.getWindow().setGravity(Gravity.CENTER);//可以设置显示的位置
        rl_close.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        tvOpenNotification.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    Intent localIntent = new Intent();
                    localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    if (Build.VERSION.SDK_INT >= 9) {
                        localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        localIntent.setData(Uri.fromParts("package", mContext.getPackageName(), null));
                    } else if (Build.VERSION.SDK_INT <= 8) {
                        localIntent.setAction(Intent.ACTION_VIEW);

                        localIntent.setClassName("com.android.settings",
                                "com.android.settings.InstalledAppDetails");

                        localIntent.putExtra("com.android.settings.ApplicationPkgName",
                                mContext.getPackageName());
                    }
                    startActivity(localIntent);
                }
            }
        });
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
                                if (configDialogBean.getBtnAction().equals("action.group")) {
                                    //题组
                                    if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.group_count").equals("")) {
                                        activitydialog(configDialogBean);
                                        SaveUserInfo.getInstance(mContext).setUserInfo("action.group_count", "" + configDialogBean.getCount() + 1);
                                    } else {
                                        if (configDialogBean.getCount() >= Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.group_count"))) {
                                            activitydialog(configDialogBean);
                                            SaveUserInfo.getInstance(mContext).setUserInfo("action.group_count", "" + configDialogBean.getCount() + 1);
                                        }
                                    }
                                } else if (configDialogBean.getBtnAction().equals("action.real")) {
                                    //实名认证
                                    if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.real_count").equals("")) {
                                        activitydialog(configDialogBean);
                                        SaveUserInfo.getInstance(mContext).setUserInfo("action.real_count", "" + configDialogBean.getCount() + 1);
                                    } else {
                                        if (configDialogBean.getCount() >= Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.real_count"))) {
                                            activitydialog(configDialogBean);
                                            SaveUserInfo.getInstance(mContext).setUserInfo("action.real_count", "" + configDialogBean.getCount() + 1);
                                        }
                                    }
                                } else if (configDialogBean.getBtnAction().equals("action.h5")) {
                                    //h5页面
                                    if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.h5_count").equals("")) {
                                        activitydialog(configDialogBean);
                                        SaveUserInfo.getInstance(mContext).setUserInfo("action.h5_count", "" + configDialogBean.getCount() + 1);
                                    } else {
                                        if (configDialogBean.getCount() >= Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.h5_count"))) {
                                            activitydialog(configDialogBean);
                                            SaveUserInfo.getInstance(mContext).setUserInfo("action.h5_count", "" + configDialogBean.getCount() + 1);
                                        }
                                    }

                                } else if (configDialogBean.getBtnAction().equals("action.invite")) {
                                    //邀请
                                    if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.invite_count").equals("")) {
                                        activitydialog(configDialogBean);
                                        SaveUserInfo.getInstance(mContext).setUserInfo("action.invite_count", "" + configDialogBean.getCount() + 1);
                                    } else {
                                        if (configDialogBean.getCount() >= Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.invite_count"))) {
                                            activitydialog(configDialogBean);
                                            SaveUserInfo.getInstance(mContext).setUserInfo("action.invite_count", "" + configDialogBean.getCount() + 1);
                                        }
                                    }
                                } else if (configDialogBean.getBtnAction().equals("action.integral")) {
                                    //积分兑换
                                    if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.integral_count").equals("")) {
                                        activitydialog(configDialogBean);
                                        SaveUserInfo.getInstance(mContext).setUserInfo("action.integral_count", "" + configDialogBean.getCount() + 1);
                                    } else {
                                        if (configDialogBean.getCount() >= Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.integral_count"))) {
                                            activitydialog(configDialogBean);
                                            SaveUserInfo.getInstance(mContext).setUserInfo("action.integral_count", "" + configDialogBean.getCount() + 1);
                                        }
                                    }
                                } else if (configDialogBean.getBtnAction().equals("action.answer.against")) {
                                    //答题对战
                                    if (configDialogBean.getCount() > 0 && SaveUserInfo.getInstance(mContext).getUserInfo("action.answer.against_count").equals("")) {
                                        activitydialog(configDialogBean);
                                        SaveUserInfo.getInstance(mContext).setUserInfo("action.answer.against_count", "" + configDialogBean.getCount() + 1);
                                    } else {
                                        if (configDialogBean.getCount() >= Integer.parseInt(SaveUserInfo.getInstance(mContext).getUserInfo("action.answer.against_count"))) {
                                            activitydialog(configDialogBean);
                                            SaveUserInfo.getInstance(mContext).setUserInfo("action.answer.against_count", "" + configDialogBean.getCount() + 1);
                                        }
                                    }
                                }

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

    //活动弹框
    private void activitydialog(final ConfigDialogBean configDialogBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialog);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.open_activity_popup, null);
        RoundImageView iv_bg = view.findViewById(R.id.iv_bg);
        RelativeLayout rl_close = view.findViewById(R.id.rl_close);
        Button btn_enter = view.findViewById(R.id.btn_enter);
        try {
            //背景图
            ImageLoaderManager.LoadImage(mContext, configDialogBean.getBaseImage(), iv_bg, R.mipmap.zw01);
            if (configDialogBean.isShowBtn() == true) {
                //显示按钮
                btn_enter.setVisibility(View.VISIBLE);
                btn_enter.setText(configDialogBean.getBtnLabel());
            } else {
                //隐藏按钮
                btn_enter.setVisibility(View.GONE);
            }
        } catch (Exception e) {

        }
        final Dialog dialog = builder.create();
        dialog.show();
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.getWindow().setContentView(view);//自定义布局应该在这里添加，要在dialog.show()的后面
        rl_close.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        });
        btn_enter.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                    if (configDialogBean.getBtnAction().equals("action.group")) {
                        //题组
                        Intent intent = new Intent(mContext, WebAnswerActivity.class);
                        intent.putExtra("groupId", Integer.parseInt(configDialogBean.getContent()));
                        intent.putExtra("h5Url", configDialogBean.getH5Url());
                        startActivity(intent);
                    } else if (configDialogBean.getBtnAction().equals("action.real")) {
                        //实名认证
                        startActivity(new Intent(mContext, RealNameAuthActivity.class));
                    } else if (configDialogBean.getBtnAction().equals("action.h5")) {
                        //h5页面
                        Intent intent = new Intent(mContext, WebBannerActivity.class);
                        intent.putExtra("url", configDialogBean.getH5Url());
                        intent.putExtra("name", "全民共进");//名称 标题
                        startActivity(intent);
                    } else if (configDialogBean.getBtnAction().equals("action.invite")) {
                        //邀请
                        Intent intent = new Intent();
                        intent.setClass(mContext, SharedPrzieActivity.class);
                        intent.putExtra("h5Url", configDialogBean.getH5Url());
                        startActivity(intent);
                    } else if (configDialogBean.getBtnAction().equals("action.integral")) {
                        //积分兑换
                        startActivity(new Intent(mContext, IntegralExchangeActivity.class));
                    } else if (configDialogBean.getBtnAction().equals("action.answer.against")) {
                        //答题对战
                        startActivity(new Intent(mContext, MainAgainstActivity.class));
                    }
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HomeFragment");
        if (!EncodeAndStringTool.isObjectEmpty(timer)) {
            timer.cancel();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!EncodeAndStringTool.isObjectEmpty(timer)) {
            timer.cancel();
        }
    }

    CommonPopupWindow popupWindow;

    /**
     * 分享弹窗
     */
    public void showSharedPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(mContext).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.share_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.AnimUp)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(llHomeFragment, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 分享二维码弹窗
     */
    public void showQr() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(mContext).inflate(R.layout.share_qr_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.share_qr_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAtLocation(llHomeFragment, Gravity.CENTER, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.share_popupwindow:
                final LinearLayout ll_share = view.findViewById(R.id.ll_share);
                SpringAnimation signUpBtnAnimY = new SpringAnimation(ll_share, SpringAnimation.TRANSLATION_Y, 0);
                signUpBtnAnimY.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
                signUpBtnAnimY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
                signUpBtnAnimY.setStartVelocity(800);
                signUpBtnAnimY.start();
                ImageView ivWeChat = view.findViewById(R.id.iv_wechat);
                ImageView ivFriends = view.findViewById(R.id.iv_friends);
                ImageView ivqr = view.findViewById(R.id.iv_qr);
                ivWeChat.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN;
                        loadInviteShared(SHARE_CHANNEL);//微信分享
                    }
                });
                ivFriends.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN_CIRCLE;
                        loadInviteShared(AppConst.SHARE_MEDIA_WEIXIN_CIRCLE);//微信朋友圈分享
                    }
                });
                ivqr.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        //二维码分享
                        loadInviteShared(AppConst.SHARE_MEDIA_QR);//二维码分享
                    }
                });
                TextView tv_cancel = view.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.layout.share_qr_popupwindow:
                final LinearLayout ll_view = view.findViewById(R.id.ll_view);
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setText(sharedBean1.getTitle());
                TextView tv_content = view.findViewById(R.id.tv_content);
                tv_content.setText(sharedBean1.getContent());
                final ImageView iv_qr = view.findViewById(R.id.iv_qr);
                Bitmap mBitmap = CodeUtils.createImage(sharedBean1.getUrl(), 100, 100, null);
                iv_qr.setImageBitmap(mBitmap);
                TextView tv_save_picture = view.findViewById(R.id.tv_save_picture);
                tv_save_picture.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        //保存二维码
                        ScannerUtils.saveImageToGallery(mContext, createViewBitmap(ll_view), ScannerUtils.ScannerType.MEDIA);
                        popupWindow.dismiss();
                    }
                });
                break;
            default:
                break;
        }
    }


    //创建bitmap
    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }

    /**
     * 邀请分享
     *
     * @param channl 分享渠道id 1:微信分享;2 微信朋友圈分享 3:二维码分享
     */
    SharedBean sharedBean1;

    private void loadInviteShared(final int channl) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.inviteShared(channl)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<SharedBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<SharedBean> dataResponse) {
                        Log.i(TAG, "loadAppShared==onNext: " + dataResponse.toString());
                        if (dataResponse.isSuccees()) {
                            SharedBean sharedBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                                if (channl == 3) {
                                    sharedBean1 = dataResponse.getDat();
                                    //显示二维码弹框
                                    showQr();
                                } else {
                                    wechatShare(sharedBean);
                                }
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
     * 微信分享
     *
     * @param sharedBean
     */
    private void wechatShare(final SharedBean sharedBean) {
        SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
        if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN) {//微信
            media = SHARE_MEDIA.WEIXIN;
        } else if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN_CIRCLE) {//朋友圈
            media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        UMImage image = new UMImage(mContext, R.mipmap.logo);//网络图片
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb(sharedBean.getUrl());//默认链接AppConst.CONTACT_US
        web.setTitle(sharedBean.getTitle());//标题
        web.setThumb(image);//缩略图
        web.setDescription(sharedBean.getContent());//描述
        new ShareAction(mContext)
                .setPlatform(media)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    /**
                     * @descrption 分享开始的回调
                     * @param share_media 平台类型
                     */
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    /**
                     * @descrption 分享成功的回调
                     * @param share_media 平台类型
                     */
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        /**
                         * 入参1:分享id;2:分享结果(①分享成功,②分享失败);3:分享渠道(①微信朋友圈②微信好友)
                         */
                        loadSharedSure(sharedBean.getId(), 1, SHARE_CHANNEL);
                    }

                    /**
                     * @descrption 分享失败的回调
                     * @param share_media 平台类型
                     * @param throwable 错误原因
                     */
                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        /**
                         * 入参1:分享id;2:分享结果(①分享成功,②分享失败);3:分享渠道(①微信朋友圈②微信好友)
                         */
                        loadSharedSure(sharedBean.getId(), 2, SHARE_CHANNEL);
                    }

                    /**
                     * @descrption 分享取消的回调
                     * @param share_media 平台类型
                     */
                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                    }
                }).share();
    }

    /**
     * 分享确认
     *
     * @param id      分享id
     * @param result  分享结果1:分享成功;2:分享失败
     * @param channel 1:微信朋友圈 2:微信好友
     */
    private void loadSharedSure(int id, int result, int channel) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.sharedConfirm(id, result, channel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse dataResponse) {
                        if (dataResponse.isSuccees()) {

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
     * 监听首界面返回键
     */
    public void homeFragmentBack() {
        if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
            popupWindow.dismiss();
            popupWindow = null;
        } else {
            mContext.finish();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (msgReceiver != null) {
            mContext.unregisterReceiver(msgReceiver);
        }
    }
}


