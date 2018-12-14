package com.shuyun.qapp.ui.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.TimeUtils;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyHomeadapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.ActivityTimeBean;
import com.shuyun.qapp.bean.AppVersionBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InviteBean;
import com.shuyun.qapp.event.MessageEvent;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.HeartBeatManager;
import com.shuyun.qapp.net.LoginDataManager;
import com.shuyun.qapp.ui.activity.ActivityFragment;
import com.shuyun.qapp.ui.classify.ClassifyFragment;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.mine.MineFragment;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ExampleUtil;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.StatusBarUtil;
import com.shuyun.qapp.view.NoScrollViewPager;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    @BindView(R.id.radio_activity)
    RadioButton radioActivity; //活动
    @BindView(R.id.radio_mine)
    RadioButton radioMine; //我的
    @BindView(R.id.pager)
    NoScrollViewPager pager;
    @BindView(R.id.iv_no_login_logo)
    ImageView ivNoLoginLogo; //未登陆logo

    /**
     * fragment容器
     */
    private List<Fragment> fragments = new ArrayList<>();
    public static boolean isForeground = false; //极光推送
    private int i = 0;//当前下标
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);

        //注册极光推送
        registerMessageReceiver();

        pager.setOnPageChangeListener(this);
        radioMain.setOnClickListener(this);
        radioClassify.setOnClickListener(this);
        radioActivity.setOnClickListener(this);
        radioMine.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        sharedPreferences.edit().putBoolean("Main", true).commit();

        //点击登录logo
        ivNoLoginLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupChange(3);
            }
        });

        //初始化数据
        initDate();

        //判断是否从广告页传递数据过来
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && "welcome".equals(bundle.getString("from"))) {
            ((HomeFragment) fragments.get(0)).setRefresh(false);
            //从广告页过来
            skip(bundle);
            ((HomeFragment) fragments.get(0)).setRefresh(true);
        } else {
            ((HomeFragment) fragments.get(0)).setRefresh(true);
        }

        /*//沉浸式代码配置
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        //用来设置整体下移，状态栏沉浸
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);*/
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_homepage;
    }

    private void initDate() {
        //添加到集合
        fragments.add(new HomeFragment());
        fragments.add(new ClassifyFragment());
        fragments.add(new ActivityFragment());
        fragments.add(new MineFragment());


        //得到getSupportFragmentManager()的管理器
        //得到适配器
        pager.setOffscreenPageLimit(4);
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

    //RadioGroup的监听事件
    private int index = 0;

    //更改Fragment
    private void changeUi(int index) {
        this.index = index;
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
                radioGroupChange(3);
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

        HeartBeatManager.instance().start(this);
        MobclickAgent.onResume(this); //统计时长
        StatService.onResume(this);

        //receiver(getIntent());

        //版本更新
        updateVersion();

        mHandler.postDelayed(runnable, 500);

        //开启登录logo动画
        TranslateAnimation animation = new TranslateAnimation(0, 0, 10, -10);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(500);
        animation.setRepeatCount(Animation.INFINITE);
        animation.setRepeatMode(Animation.REVERSE);
        ivNoLoginLogo.startAnimation(animation);
    }

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //是否未登陆
            if (!AppConst.isLogin()) {
                ivNoLoginLogo.setVisibility(View.VISIBLE);
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

        final ApiService apiService = BasePresenter.create(8000);
        apiService.updateVersion(APKVersionCodeTools.getVerName(this), AppConst.DEV_ID, AppConst.APP_ID, AppConst.V, curTime, signCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<AppVersionBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AppVersionBean> loginResponse) {
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
        MobclickAgent.onPause(this); //统计时长

        StatService.onPause(this);

        isForeground = false;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        /*if ("3".equals(messageEvent.getMessage())) { //个人信息微信登录返回
            radioGroupChange(3);
        }*/
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        sharedPreferences.edit().putBoolean("Main", true).commit();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }

    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.shuyun.qapp.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == radioActivity.getId()
                || v.getId() == radioMain.getId()
                || v.getId() == radioMine.getId()
                || v.getId() == radioClassify.getId()) {
            radioGroupChange(Integer.valueOf(v.getTag().toString()));
        }
    }

    public void radioGroupChange(int position) {
        if (position == 3 && !AppConst.isLogin()) {
            //还原选中
            switch (index) {
                case 0:
                    radioGroup.check(radioMain.getId());
                    break;
                case 1:
                    radioGroup.check(radioClassify.getId());
                    break;
                case 2:
                    radioGroup.check(radioActivity.getId());
                    break;
            }
            LoginDataManager.instance().addData(LoginDataManager.MINE_LOGIN, new JSONObject());
            //跳转登录
            startActivityForResult(new Intent(this, LoginActivity.class), 0x1000);
            return;
        }

        StatusBarUtil.setStatusBarColor(this, R.color.white);
        if (position == 3) {
            StatusBarUtil.setStatusBarColor(this, R.color.mine_top);
        } else {
            StatusBarUtil.setStatusBarColor(this, R.color.white);
        }
        //点击活动专区
        if (position == 2 && "1".equals(show)) { //未点过红色角标
            if (AppConst.isLogin()) {
                Drawable drawable = getResources().getDrawable(R.mipmap.activity_s);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                radioActivity.setCompoundDrawables(null, drawable, null, null);
                clickActivity();
            }
        } else if (position == 2) {
            Drawable drawable = getResources().getDrawable(R.mipmap.activity_s);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            radioActivity.setCompoundDrawables(null, drawable, null, null);
        } else {
            Drawable drawable = getResources().getDrawable(R.mipmap.activity_n);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
            radioActivity.setCompoundDrawables(null, drawable, null, null);
        }

        changeUi(position);

        getActivityShow(position);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String message = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + message + "\n");
                    if (!ExampleUtil.isEmpty(extras)) {
                        showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                    }
                }
            } catch (Exception e) {
            }
        }
    }

    //获取最新活动显示角标
    String show = "";

    private void getActivityShow(final int i) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getActivityShow()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<ActivityTimeBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<ActivityTimeBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            ActivityTimeBean activityTimeBean = dataResponse.getDat();
                            if ("1".equals(activityTimeBean.getShow())) {
                                show = "1";
                                if (i == 2) {
                                    //显示活动角标
                                    Drawable drawable = getResources().getDrawable(R.mipmap.activity_s);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                                    radioActivity.setCompoundDrawables(null, drawable, null, null);
                                } else {
                                    if (index == 2) {
                                        Drawable drawable = getResources().getDrawable(R.mipmap.activity_s);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                                        radioActivity.setCompoundDrawables(null, drawable, null, null);
                                    } else {
                                        //显示活动角标
                                        Drawable drawable = getResources().getDrawable(R.mipmap.activity_n_red);
                                        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                                        radioActivity.setCompoundDrawables(null, drawable, null, null);
                                    }

                                }
                            } else {
                                show = "0";
                            }

                        } else {
                            ErrorCodeTools.errorCodePrompt(HomePageActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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

    //活动专区点击
    private void clickActivity() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.clickActivity()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<String> dataResponse) {
                        if (dataResponse.isSuccees()) {
                        } else {
                            ErrorCodeTools.errorCodePrompt(HomePageActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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

    //邀请有奖
    private void invite() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.inviteShare()
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
                                    SharedPrefrenceTool.put(HomePageActivity.this, "share", inviteBean.getShare());//是否参与邀请分享 1——参与邀请
                                } else {
                                    SharedPrefrenceTool.put(HomePageActivity.this, "share", inviteBean.getShare());//是否参与邀请分享 1——参与邀请
                                }
                            } catch (Exception e) {

                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(HomePageActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
        }
        for (Fragment fragment : fragments) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void refresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((BaseFragment) fragments.get(index)).refresh();
            }
        }, 320);
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
}
