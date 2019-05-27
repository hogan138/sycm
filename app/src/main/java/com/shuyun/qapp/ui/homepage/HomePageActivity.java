package com.shuyun.qapp.ui.homepage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.blankj.utilcode.util.ConvertUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyHomeadapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.ActivityTimeBean;
import com.shuyun.qapp.bean.AppVersionBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.HomeTabBean;
import com.shuyun.qapp.bean.QPushBean;
import com.shuyun.qapp.manager.FragmentTouchManager;
import com.shuyun.qapp.manager.HeartBeatManager;
import com.shuyun.qapp.manager.LoginDataManager;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.activity.SeventyYearFragment;
import com.shuyun.qapp.ui.classify.ClassifyFragment;
import com.shuyun.qapp.ui.found.FoundFragment;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.mine.MineFragment;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.AliPushBind;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.ImageUitils;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SaveUserInfo1;
import com.shuyun.qapp.utils.StatusBarUtil;
import com.shuyun.qapp.view.ActionJumpUtil;
import com.shuyun.qapp.view.NoScrollViewPager;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 主页面activity
 */
public class HomePageActivity extends BaseActivity implements ViewPager.OnPageChangeListener, View.OnClickListener {

    @BindView(R.id.radioGroup)
    RadioGroup radioGroup; //首页
    @BindView(R.id.radio_main)
    RadioButton radioMain; //首页
    @BindView(R.id.radio_classify)
    RadioButton radioClassify; //分类
    @BindView(R.id.radio_found)
    RadioButton radioFound; //发现
    @BindView(R.id.radio_mine)
    RadioButton radioMine; //我的
    @BindView(R.id.pager)
    NoScrollViewPager pager;
    @BindView(R.id.iv_no_login_logo)
    ImageView ivNoLoginLogo; //未登陆logo
    @BindView(R.id.radioSevetyYear)
    RadioButton radioSevetyYear; //70周年
    @BindView(R.id.iv_seventy_year)
    ImageView ivSeventyYear;

    /**
     * fragment容器
     */
    private List<Fragment> fragments = new ArrayList<>();
    public static boolean isForeground = false; //极光推送
    private Handler mHandler = new Handler();
    //获取最新活动显示角标
    private String show = "";
    //RadioGroup的监听事件
    private int selectedIndex = 0;
    private Bundle bundle = null;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        MyActivityManager.getInstance().pushOneActivity(HomePageActivity.this);

        //保存从广告页传递数据
        bundle = getIntent().getExtras();

        pager.setOnPageChangeListener(this);
        radioMain.setOnClickListener(this);
        radioClassify.setOnClickListener(this);
        radioSevetyYear.setOnClickListener(this);
        radioFound.setOnClickListener(this);
        radioMine.setOnClickListener(this);


        //记录宝箱第一次进入
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        sharedPreferences.edit().putBoolean("Main", true).apply();

        //点击登录logo
        ivNoLoginLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupChange(4);
            }
        });

        //初始化数据
        initDate();

        //获取首页动态tab
        homeTab();

        mHandler.postDelayed(runnable, 500);

        Log.e("token", AppConst.TOKEN + "             " + AppConst.sycm());
    }


    @Override
    public int intiLayout() {
        return R.layout.activity_homepage;
    }

    private void initDate() {
        //添加到集合
        fragments.add(new NewHomeFragment());
        fragments.add(new ClassifyFragment());
        fragments.add(new SeventyYearFragment());
        fragments.add(new FoundFragment());
        fragments.add(new MineFragment());

        //得到getSupportFragmentManager()的管理器
        //得到适配器
        pager.setOffscreenPageLimit(5);
        //设置适配器
        pager.setAdapter(new MyHomeadapter(getSupportFragmentManager(), fragments, this));
    }

    //ViewPager.OnPageChangeListener监听事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < fragments.size(); i++) {
            RadioButton radiobutton = (RadioButton) radioGroup.getChildAt(i);
            if (i == position) {
                radiobutton.setChecked(true);
                radiobutton.setEnabled(false);
                //设置选中的颜色
            } else {
                radiobutton.setChecked(false);
                radiobutton.setEnabled(true);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //更改Fragment
    private void changeUi(int index) {
        this.selectedIndex = index;
        pager.setCurrentItem(index, false);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        receiver(intent);

        String action = intent.getStringExtra(AppConst.APP_ACTION_PARAM);
        if (AppConst.APP_ACTION_LOGOUT.equals(action)) {
            radioGroupChange(0);
        }
    }

    private void receiver(Intent intent) {
        try {
            String from = intent.getStringExtra("from");
            //领取答题次数
            if ("msg".equals(from)) {
                radioGroupChange(4);
            } else if ("h5".equals(from)) {  //我的奖品h5返回首页
                radioGroupChange(0);
            }
        } catch (Exception e) {

        }
    }

    //在activity或者fragment中添加友盟统计
    @Override
    public void onResume() {
        super.onResume();
        isForeground = true;


        //用户已登录
        if (AppConst.isLogin()) {
            //友盟统计
            String account = SaveUserInfo.getInstance(this).getUserInfo("account");
            MobclickAgent.onProfileSignIn(account.replace(account.substring(3, 9), "******"));

            //已登录状态把过期跳转登录页设为true
            SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
            sharedPreferences.edit().putBoolean("TAU", true).apply();
        } else {
            //显示活动角标
            Drawable drawable = getResources().getDrawable(R.mipmap.found_n_red);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            radioFound.setCompoundDrawables(null, drawable, null, null);
        }

        //判断是否从广告页点击传递数据过来
        if (bundle != null && "welcome".equals(bundle.getString("from"))) {
            //从广告页过来
            skip(bundle);
            bundle = null;
            return;
        }

        //判断游客模式
        if ("0".equals(SaveUserInfo1.getInstance(this).getUserInfo("tourists")) && !AppConst.isLogin()) {
            startActivityForResult(new Intent(this, LoginActivity.class), 0x1013);
            return;
        }

        //版本更新
        updateVersion();

        try {
            if (selectedIndex == 4) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ((BaseFragment) fragments.get(selectedIndex)).refresh();
                    }
                }, 10);
            }
        } catch (Exception e) {

        }

        //查询绑定别名
        queryBind();

        //从任务跳转
        try {
            String task_jump = SaveUserInfo.getInstance(mContext).getUserInfo("task_jump");
            if (!EncodeAndStringTool.isStringEmpty(task_jump) && task_jump.equals("task_answer")) {
                //分类
                radioGroupChange(1);
                SaveUserInfo.getInstance(mContext).setUserInfo("task_jump", "");
            } else if (!EncodeAndStringTool.isStringEmpty(task_jump) && task_jump.equals("task_oppty")) {
                //我的
                radioGroupChange(4);
                SaveUserInfo.getInstance(mContext).setUserInfo("task_jump", "");
            }

        } catch (Exception e) {

        }
    }


    //未登陆进入首页显示登录logo
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //是否未登陆
            if (!AppConst.isLogin()) {
                ivNoLoginLogo.setVisibility(View.VISIBLE);
                if (ivNoLoginLogo.getAnimation() == null) {
                    //开启登录logo动画
                    TranslateAnimation animation = new TranslateAnimation(0, 0, 10, -10);
                    animation.setInterpolator(new OvershootInterpolator());
                    animation.setDuration(500);
                    animation.setRepeatCount(Animation.INFINITE);
                    animation.setRepeatMode(Animation.REVERSE);
                    ivNoLoginLogo.startAnimation(animation);
                }
            } else {
                try {
                    ivNoLoginLogo.setVisibility(View.GONE);
                    ivNoLoginLogo.clearAnimation();
                } catch (Exception e) {

                }
            }
            mHandler.postDelayed(runnable, 10);
        }
    };

    //更新版本
    private void updateVersion() {
        long curTime = System.currentTimeMillis();
        String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + APKVersionCodeTools.getVerName(this) + AppConst.APP_KEY;
        Log.e("签名", signString);
        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(signString);
        String signCode = getCode(myCode);

        RemotingEx.doRequest(RemotingEx.Builder().updateVersion(APKVersionCodeTools.getVerName(this),
                AppConst.DEV_ID,
                AppConst.APP_ID,
                AppConst.V,
                curTime,
                signCode), new OnRemotingCallBackListener<AppVersionBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<AppVersionBean> loginResponse) {
                if (loginResponse.isSuccees()) {
                    AppVersionBean appVersionBean = loginResponse.getDat();
                    if (!EncodeAndStringTool.isObjectEmpty(appVersionBean)) {
                        Long mode = appVersionBean.getMode();
                        if (mode == 0) {
                        } else if (mode == 1) {
                        } else if (mode == 2) {
                            updateDialog(appVersionBean.getUrl());
                        }
                    }

                } else {
                    ErrorCodeTools.errorCodePrompt(HomePageActivity.this, loginResponse.getErr(), loginResponse.getMsg());
                }
            }
        });
    }

    //版本更新弹框
    private void updateDialog(final String url) {
        new CircleDialog.Builder(this)
                .setTitle("监测到新版本")
                .setText("已经监测到新版本")
                .setTextColor(Color.parseColor("#333333"))
                .setCanceledOnTouchOutside(false)
                .setWidth(0.7f)
                .setPositive("前往更新", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        Uri uri = Uri.parse(url);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        startActivity(intent);
                    }
                })
                .setCancelable(false)
                .setCanceledOnTouchOutside(false)
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.animStyle = R.style.popwin_anim_style;
                    }
                })
                .show();
    }

    public void onPause() {
        super.onPause();
        isForeground = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //重置宝箱第一次进入
        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        sharedPreferences.edit().putBoolean("Main", true).apply();

        HeartBeatManager.instance().stop();

        /**
         * 销毁电商SDK相关资源引用，防止内存泄露
         */
        AlibcTradeSDK.destory();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == radioFound.getId()
                || v.getId() == radioMain.getId()
                || v.getId() == radioMine.getId()
                || v.getId() == radioClassify.getId()
                || v.getId() == radioSevetyYear.getId()) {
            radioGroupChange(Integer.valueOf(v.getTag().toString()));
        }
    }

    public void radioGroupChange(int position) {
        if (position == 4 && !AppConst.isLogin()) {
            //还原选中
            switch (selectedIndex) {
                case 0:
                    radioGroup.check(radioMain.getId());
                    break;
                case 1:
                    radioGroup.check(radioClassify.getId());
                    break;
                case 2:
                    radioGroup.check(radioSevetyYear.getId());
                    break;
                case 3:
                    radioGroup.check(radioFound.getId());
                    break;
                case 4:
                    radioGroup.check(radioMine.getId());
                    break;
            }
            LoginDataManager.instance().addData(LoginDataManager.MINE_LOGIN, new JSONObject());
            //跳转登录
            startActivityForResult(new Intent(this, LoginActivity.class), 0x1000);
            return;
        }

        //改变顶部状态栏颜色
        StatusBarUtil.setStatusBarColor(this, R.color.white, true);
        if (position == 4) {
            StatusBarUtil.setStatusBarColor(this, R.color.mine_top, true);
        } else {
            StatusBarUtil.setStatusBarColor(this, R.color.white, true);
        }


        //点击发现
        if (position == 3 && "1".equals(show)) { //未点过红色角标
            if (AppConst.isLogin()) {
                Drawable drawable = getResources().getDrawable(R.mipmap.found_s);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                radioFound.setCompoundDrawables(null, drawable, null, null);
                clickActivity();
            }
        } else if (position == 3) {
            Drawable drawable = getResources().getDrawable(R.mipmap.found_s);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            radioFound.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.found_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            radioFound.setCompoundDrawables(null, drawable, null, null);
        }

        changeUi(position);

        getActivityShow(position);
    }

    private void getActivityShow(final int i) {

        RemotingEx.doRequest(RemotingEx.Builder().getActivityShow(), new OnRemotingCallBackListener<ActivityTimeBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<ActivityTimeBean> dataResponse) {
                if (dataResponse.isSuccees()) {
                    ActivityTimeBean activityTimeBean = dataResponse.getDat();
                    if ("1".equals(activityTimeBean.getShow())) {
                        show = "1";
                        if (i == 3) {
                            //显示活动角标
                            Drawable drawable = getResources().getDrawable(R.mipmap.found_s);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                            radioFound.setCompoundDrawables(null, drawable, null, null);
                        } else {
                            if (selectedIndex == 3) {
                                Drawable drawable = getResources().getDrawable(R.mipmap.found_s);
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                                radioFound.setCompoundDrawables(null, drawable, null, null);
                            } else {
                                //显示活动角标
                                Drawable drawable = getResources().getDrawable(R.mipmap.found_n_red);
                                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                                radioFound.setCompoundDrawables(null, drawable, null, null);
                            }

                        }
                    } else {
                        show = "0";
                    }

                } else {
                    ErrorCodeTools.errorCodePrompt(HomePageActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                }
            }
        });

    }

    //首页tab
    private void homeTab() {

        RemotingEx.doRequest(RemotingEx.Builder().homeTab(), new OnRemotingCallBackListener<HomeTabBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(final String action, DataResponse<HomeTabBean> dataResponse) {
                if (dataResponse.isSuccees()) {
                    final HomeTabBean homeTabBean = dataResponse.getDat();
                    if (homeTabBean.getStatus() == 1) {
                        //显示tab
                        radioSevetyYear.setVisibility(View.VISIBLE);
                        if (!EncodeAndStringTool.isStringEmpty(homeTabBean.getTitle())) {
                            radioSevetyYear.setText(homeTabBean.getTitle());
                            radioSevetyYear.setTextColor(Color.parseColor(homeTabBean.getColor()));
                        }

                        if (homeTabBean.getMode() == 1) {
                            //正常布局
                            Drawable drawable = ImageUitils.loadImageFromNetwork(homeTabBean.getPicture());
                            //这一步必须要做, 否则不会显示.
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                            radioSevetyYear.setCompoundDrawables(null, drawable, null, null);
                        } else {
                            //图片显示
                            ivSeventyYear.setVisibility(View.VISIBLE);
                            radioSevetyYear.setCompoundDrawables(null, null, null, null);
                            radioSevetyYear.setText("");
                            GlideUtils.LoadImage1(HomePageActivity.this, homeTabBean.getPicture(), ivSeventyYear);
                            ViewGroup.LayoutParams params = ivSeventyYear.getLayoutParams();
                            params.height = ConvertUtils.dp2px(homeTabBean.getHeight());
                            params.width = ConvertUtils.dp2px(homeTabBean.getWidth());
                            ivSeventyYear.setLayoutParams(params);
                        }

                        final String Action = homeTabBean.getAction();
                        if (AppConst.H5.equals(Action)) {
                            //若是h5链接在当前展示
                            SaveUserInfo.getInstance(HomePageActivity.this).setUserInfo("home_tab_url", homeTabBean.getH5Url());
                            ivSeventyYear.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    radioGroupChange(2);
                                }
                            });
                        } else {
                            //跳页面
                            radioSevetyYear.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String content = homeTabBean.getContent();
                                    String h5Url = homeTabBean.getH5Url();
                                    ActionJumpUtil.dialogSkip(Action, HomePageActivity.this, content, h5Url, (long) 0);
                                }
                            });
                            ivSeventyYear.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    String content = homeTabBean.getContent();
                                    String h5Url = homeTabBean.getH5Url();
                                    ActionJumpUtil.dialogSkip(Action, HomePageActivity.this, content, h5Url, (long) 0);
                                }
                            });
                        }

                    } else {
                        //隐藏tab
                        radioSevetyYear.setVisibility(View.GONE);
                        ivSeventyYear.setVisibility(View.GONE);
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(HomePageActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                }
            }
        });

    }


    //活动专区点击
    private void clickActivity() {

        RemotingEx.doRequest(RemotingEx.Builder().clickActivity(), new OnRemotingCallBackListener<String>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<String> dataResponse) {
                if (dataResponse.isSuccees()) {
                } else {
                    ErrorCodeTools.errorCodePrompt(HomePageActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                }
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 0x1000) {
            LoginDataManager.instance().handler(this, null);
            return;
        } else if (resultCode == RESULT_OK && (requestCode == 0x0010 || requestCode == 0x0020)) {
            LoginDataManager.instance().handler(this, null);
            return;
        } else if (requestCode == 0x0010 || requestCode == 0x0020) {
            refresh();
            return;
        } else if (resultCode != RESULT_OK && requestCode == 0x1013) {
            finish();
            return;
        }
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseFragment) fragments.get(selectedIndex)).refresh();
            }
        }, 10);
    }

    //从广告页进来
    private void skip(Bundle bundle) {
        final Long model = bundle.getLong("model", 0);
        final String content = bundle.getString("content");
        final Long isLogin = bundle.getLong("isLogin", 0);
        if (model == 3) {//题组跳转
            if (!EncodeAndStringTool.isStringEmpty(content)) {
                //判断是否需要登录
                if (isLogin == 1 && !AppConst.isLogin()) {
                    //登录页只跳转一次
                    SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
                    sharedPreferences.edit().putBoolean("TAU", false).apply();

                    LoginDataManager.instance().addData(LoginDataManager.WELCOME_LOGIN, bundle);
                    Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 0x0010);
                } else {
                    Intent intent = new Intent(HomePageActivity.this, WebAnswerActivity.class);
                    intent.putExtra("groupId", Long.valueOf(content));
                    intent.putExtra("from", "splash");
                    intent.putExtra("h5Url", bundle.getString("examUrl"));
                    startActivity(intent);
                }
            }
        } else if (model == 2) {//内部链接
            if (!EncodeAndStringTool.isStringEmpty(content)) {
                //判断是否需要登录
                if (isLogin == 1 && !AppConst.isLogin()) {
                    //登录页只跳转一次
                    SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
                    sharedPreferences.edit().putBoolean("TAU", false).apply();

                    LoginDataManager.instance().addData(LoginDataManager.WELCOME_LOGIN, bundle);
                    Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
                    startActivityForResult(intent, 0x0020);
                } else {
                    Intent intent = new Intent(HomePageActivity.this, WebH5Activity.class);
                    intent.putExtra("url", content);
                    intent.putExtra("name", "全民共进");
                    intent.putExtra("from", "splash");
                    startActivity(intent);
                }
            }
        } else if (model == 1) {//外部链接
            if (!EncodeAndStringTool.isStringEmpty(content)) {
                Uri uri = Uri.parse(content);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        } else if (model == 0) {

        }
    }

    @Override
    public void clear() {
        super.clear();
        for (Fragment fragment : fragments) {
            ((BaseFragment) fragment).clear();
        }
    }

    //查询绑定别名
    public void queryBind() {
        if (AppConst.isLogin()) {
            //查询绑定别名
            String deviceId = PushServiceFactory.getCloudPushService().getDeviceId();
            RemotingEx.doRequest(RemotingEx.Builder().queryBind(deviceId), new OnRemotingCallBackListener<QPushBean>() {
                @Override
                public void onCompleted(String action) {

                }

                @Override
                public void onFailed(String action, String message) {

                }

                @Override
                public void onSucceed(String action, DataResponse<QPushBean> loginResponse) {
                    if (loginResponse.isSuccees()) {
                        QPushBean qPushBean = loginResponse.getDat();
                        if (qPushBean.getCount().equals("0")) {
                            //绑定别名
                            AliPushBind.bindPush();
                        }
                    } else {
                        ErrorCodeTools.errorCodePrompt(HomePageActivity.this, loginResponse.getErr(), loginResponse.getMsg());
                    }
                }
            });

        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (this.selectedIndex == 3 || this.selectedIndex == 0) {
            FragmentTouchManager.instance().apply(ev);
        }
        return super.dispatchTouchEvent(ev);
    }
}
