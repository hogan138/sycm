package com.shuyun.qapp.ui.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyHomeadapter;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.ActivityTimeBean;
import com.shuyun.qapp.bean.AppVersionBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InviteBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.activity.ActivityFragment;
import com.shuyun.qapp.ui.classify.ClassifyFragment;
import com.shuyun.qapp.ui.mine.AddWithdrawInfoActivity;
import com.shuyun.qapp.ui.mine.MineFragment;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ExampleUtil;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

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
public class HomePageActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    @BindView(R.id.radioGroup1)
    RadioGroup radioGroup1;
    @BindView(R.id.radio_main)
    RadioButton radioMain; //首页
    @BindView(R.id.radio_classify)
    RadioButton radioClassify; //分类
    @BindView(R.id.radio_activity)
    RadioButton radioActivity; //活动
    @BindView(R.id.radio_mine)
    RadioButton radioMine; //我的
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.pager)
    ViewPager pager;

    /**
     * fragment容器
     */
    private FragmentManager fm;
    ArrayList<Fragment> fragments = new ArrayList<>();
    public static boolean isForeground = false; //极光推送
    int i = 0;//当前下标

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }

        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);

        pager.setOnPageChangeListener(this);
        radioGroup1.setOnCheckedChangeListener(this);

        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        Boolean main_run = sharedPreferences.getBoolean("Main", true);
        sharedPreferences.edit().putBoolean("Main", true).commit();

        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        //底部导航栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(Color.parseColor("#ffffff"));
        }
        MyActivityManager.getInstance().pushOneActivity(this);

        //注册极光推送
        registerMessageReceiver();

        //初始化数据
        initDate();

        /**
         * 邀请有奖
         */
        invite();

    }

    private void initDate() {
        fragments = new ArrayList<Fragment>();
        //实例化Fragment
        HomeFragment fragmentOne = new HomeFragment();
        ClassifyFragment fragmentTwo = new ClassifyFragment();
        ActivityFragment fragmentThree = new ActivityFragment();
        MineFragment fragmentFour = new MineFragment();

        //添加到集合
        fragments.add(fragmentOne);
        fragments.add(fragmentTwo);
        fragments.add(fragmentThree);
        fragments.add(fragmentFour);


        //得到getSupportFragmentManager()的管理器
        fm = getSupportFragmentManager();
        //得到适配器
        MyHomeadapter myAdapter = new MyHomeadapter(fm, fragments, this);
        //设置适配器
        pager.setAdapter(myAdapter);
        pager.setOffscreenPageLimit(3);

    }

    //ViewPager.OnPageChangeListener监听事件
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        for (int i = 0; i < fragments.size(); i++) {
            RadioButton radiobutton = (RadioButton) radioGroup1.getChildAt(i);
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
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {

        for (int j = 0; j < fragments.size(); j++) {
            //得到radiobutton
            RadioButton radiobutton = (RadioButton) radioGroup1.getChildAt(j);
            //判断radiobutton的id是否等于选中的id
            if (radiobutton.getId() == i) {
                //设置当前页
                pager.setCurrentItem(j, false);

                //改变状态栏颜色
                if (j == 3) {
                    ImmersionBar.with(this).statusBarColor(R.color.mine_top).statusBarDarkFont(true).fitsSystemWindows(true).init();
                } else {
                    ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
                }

                //当前下标
                i = j;

                //获取最新活动显示角标
                getActivityShow(i);

                //点击活动专区
                if (j == 2 && "1".equals(show)) {
                    Drawable drawable = getResources().getDrawable(R.mipmap.activity_s);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    radioActivity.setCompoundDrawables(null, drawable, null, null);
                    clickActivity();
                } else if (j == 2) {
                    Drawable drawable = getResources().getDrawable(R.mipmap.activity_s);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    radioActivity.setCompoundDrawables(null, drawable, null, null);
                } else {
                    Drawable drawable = getResources().getDrawable(R.mipmap.activity_n);
                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                    radioActivity.setCompoundDrawables(null, drawable, null, null);
                }

                /**
                 * 大家都在答
                 */
                AppConst.i = 0;

            }
        }
    }

    //更改Fragment
    public void changeUi(int index) {
        pager.setCurrentItem(index, false);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        try {
            //领取答题次数
            if (intent.getStringExtra("from") != null && intent.getStringExtra("from").equals("msg")) {
                changeUi(3);
            } else if (intent.getStringExtra("from") != null && intent.getStringExtra("from").equals("h5")) {  //我的奖品h5返回首页
                changeUi(0);
            }
        } catch (Exception e) {

        }

    }


    //在activity或者fragment中添加友盟统计
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长

        StatService.onResume(this);

        //获取最新活动显示角标
        getActivityShow(i);

        try {
            //领取答题次数
            if (getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("msg")) {
                changeUi(3);
            } else if (getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("h5")) {  //我的奖品h5返回首页
                changeUi(0);
            }
        } catch (Exception e) {

        }

        isForeground = true;

        //版本更新
        updateVersion();

        Log.e("token", AppConst.TOKEN + "--------" + AppConst.sycm());

    }

    //更新版本
    private void updateVersion() {
        long curTime = System.currentTimeMillis();
        String signString = "" + AppConst.DEV_ID + AppConst.APP_ID + AppConst.V + curTime + APKVersionCodeTools.getVerName(this) + AppConst.APP_KEY;
        Log.e("签名", signString);
        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(signString);
        /**
         * code值
         */
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
                                int mode = appVersionBean.getMode();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        Boolean main_run = sharedPreferences.getBoolean("Main", true);
        sharedPreferences.edit().putBoolean("Main", true).commit();
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

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String messge = intent.getStringExtra(KEY_MESSAGE);
                    String extras = intent.getStringExtra(KEY_EXTRAS);
                    StringBuilder showMsg = new StringBuilder();
                    showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
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
                                    //显示活动角标
                                    Drawable drawable = getResources().getDrawable(R.mipmap.activity_n_red);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); //设置边界
                                    radioActivity.setCompoundDrawables(null, drawable, null, null);
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
}
