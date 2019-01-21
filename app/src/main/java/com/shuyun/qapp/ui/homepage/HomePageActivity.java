package com.shuyun.qapp.ui.homepage;

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
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.alibaba.baichuan.android.trade.AlibcTradeSDK;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
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
import com.shuyun.qapp.bean.InviteBean;
import com.shuyun.qapp.bean.QPushBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.HeartBeatManager;
import com.shuyun.qapp.net.LoginDataManager;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.activity.ActivityFragment;
import com.shuyun.qapp.ui.classify.ClassifyFragment;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.mine.MineFragment;
import com.shuyun.qapp.ui.webview.WebAnswerActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.AliPushBind;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.StatusBarUtil;
import com.shuyun.qapp.view.NoScrollViewPager;

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
    private Handler mHandler = new Handler();
    //获取最新活动显示角标
    private String show = "";
    //RadioGroup的监听事件
    private int selectedIndex = 0;
    private Bundle bundle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        MyActivityManager.getInstance().pushOneActivity(HomePageActivity.this);

        //保存从广告页传递数据
        bundle = getIntent().getExtras();

        pager.setOnPageChangeListener(this);
        radioMain.setOnClickListener(this);
        radioClassify.setOnClickListener(this);
        radioActivity.setOnClickListener(this);
        radioMine.setOnClickListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        sharedPreferences.edit().putBoolean("Main", true).apply();

        //点击登录logo
        ivNoLoginLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                radioGroupChange(3);
            }
        });

        //初始化数据
        initDate();

        mHandler.postDelayed(runnable, 500);

        Log.e("token", AppConst.jwtToken + "             " + AppConst.sycm());

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

        //判断是否从广告页点击传递数据过来
        if (bundle != null && "welcome".equals(bundle.getString("from"))) {
            //从广告页过来
            skip(bundle);
            bundle = null;
            return;
        }

        //判断游客模式
        if ("0".equals(SaveUserInfo.getInstance(this).getUserInfo("tourists")) && !AppConst.isLogin()) {
            startActivityForResult(new Intent(this, LoginActivity.class), 0x1013);
            return;
        }

        //版本更新
        updateVersion();

        if (selectedIndex == 3) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((BaseFragment) fragments.get(selectedIndex)).refresh();
                }
            }, 10);
        }

        //查询绑定别名
        queryBind();
    }

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

        RemotingEx.doRequest(ApiServiceBean.updateVersion(), new Object[]{APKVersionCodeTools.getVerName(this), AppConst.DEV_ID, AppConst.APP_ID, AppConst.V, curTime, signCode}, new OnRemotingCallBackListener<AppVersionBean>() {
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
            switch (selectedIndex) {
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

    private void getActivityShow(final int i) {

        RemotingEx.doRequest(ApiServiceBean.getActivityShow(), new OnRemotingCallBackListener<ActivityTimeBean>() {
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
                        if (i == 2) {
                            //显示活动角标
                            Drawable drawable = getResources().getDrawable(R.mipmap.activity_s);
                            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                            radioActivity.setCompoundDrawables(null, drawable, null, null);
                        } else {
                            if (selectedIndex == 2) {
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
        });

    }

    //活动专区点击
    private void clickActivity() {

        RemotingEx.doRequest(ApiServiceBean.clickActivity(), new OnRemotingCallBackListener<String>() {
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

    //邀请有奖
    private void invite() {

        RemotingEx.doRequest(ApiServiceBean.inviteShare(), new OnRemotingCallBackListener<InviteBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<InviteBean> dataResponse) {
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
            RemotingEx.doRequest(ApiServiceBean.queryBind(), new Object[]{deviceId}, new OnRemotingCallBackListener<QPushBean>() {
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

}
