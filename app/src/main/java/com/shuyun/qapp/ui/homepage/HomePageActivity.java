package com.shuyun.qapp.ui.homepage;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AppVersionBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.activity.ActivityFragment;
import com.shuyun.qapp.ui.classify.ClassifyFragment;
import com.shuyun.qapp.ui.integral.MyPrizeActivity;
import com.shuyun.qapp.ui.mine.MineFragment;
import com.shuyun.qapp.ui.welcome.SplashActivity;
import com.shuyun.qapp.utils.APKVersionCodeTools;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ExampleUtil;
import com.shuyun.qapp.utils.ListDataSave;
import com.shuyun.qapp.utils.LogUtil;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;
import static com.umeng.commonsdk.stateless.UMSLEnvelopeBuild.mContext;

public class HomePageActivity extends AppCompatActivity {

    private static boolean isLogin = false;
    @BindView(R.id.home_fragment_container)
    FrameLayout homeFragmentContainer;

    /**
     * 底部导航器
     */
    @BindView(R.id.home_bottome_switcher_container)
    LinearLayout homeBottomeSwitcherContainer;

    /**
     * fragment容器
     */
    ArrayList<Fragment> fragments = new ArrayList<>();

    public static boolean isForeground = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        ButterKnife.bind(this);

        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        Boolean first_run = sharedPreferences.getBoolean("First", true);
        sharedPreferences.edit().putBoolean("First", true).commit();

        Boolean main_run = sharedPreferences.getBoolean("Main", true);
        sharedPreferences.edit().putBoolean("Main", true).commit();

        init();
        setListener();

        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
        MyActivityManager.getInstance().pushOneActivity(this);

        //注册极光推送
        registerMessageReceiver();  // used for receive msg


    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        try {
            //领取答题次数
            if (intent.getStringExtra("from") != null && intent.getStringExtra("from").equals("msg")) {
                onClickListener.onClick(homeBottomeSwitcherContainer.getChildAt(2));
            } else if (intent.getStringExtra("from") != null && intent.getStringExtra("from").equals("h5")) {  //我的奖品h5返回首页
                onClickListener.onClick(homeBottomeSwitcherContainer.getChildAt(0));
            }
        } catch (Exception e) {

        }

    }

    private void init() {
        fragments.clear();
        //添加fragment
        fragments.add(new HomeFragment());
        fragments.add(new ClassifyFragment());
        fragments.add(new ActivityFragment());
        fragments.add(new MineFragment());
        onClickListener.onClick(homeBottomeSwitcherContainer.getChildAt(0));

    }

    /**
     * 完成一个通用底部导航的处理
     */
    private void setListener() {
        //所有孩子,不包括孙子
        int childCount = homeBottomeSwitcherContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            LinearLayout childAt = (LinearLayout) homeBottomeSwitcherContainer.getChildAt(i);
            childAt.setOnClickListener(onClickListener);
        }
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int index = homeBottomeSwitcherContainer.indexOfChild(view);
            changeUi(index);
            changeFragment(index);
        }
    };


    /**
     * 改变Index对应的孩子的状态，包括这个孩子中多有控件的状态（不可用状态：enable=false）
     * 改变其他的孩子的状态，，包括这些孩子中多有控件的状态
     *
     * @param index
     */
    public void changeUi(int index) {

        int childCount = homeBottomeSwitcherContainer.getChildCount();

        for (int i = 0; i < childCount; i++) {
            if (i == index) {
                // 每个Item中的控件都需要切换状态,不可再点击了
                setEnable(homeBottomeSwitcherContainer.getChildAt(i), false);

            } else {
                // 每个Item中的控件都需要切换状态,可以再点击
                setEnable(homeBottomeSwitcherContainer.getChildAt(i), true);
            }

        }

    }

    public void changeFragment(int index) {
        Fragment fragment = fragments.get(index);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.home_fragment_container, fragment)
                .commit();

    }

    /**
     * 将每个Item中的所用控件状态一同改变
     * 由于我们处理一个通用的代码，那么Item可能会有很多层，所以我们需要使用递归
     *
     * @param item
     * @param b
     */
    private void setEnable(View item, boolean b) {
        item.setEnabled(b);
        if (item instanceof ViewGroup) {
            int childCount = ((ViewGroup) item).getChildCount();
            for (int i = 0; i < childCount; i++) {
                setEnable(((ViewGroup) item).getChildAt(i), b);
            }
        }
    }


    //在activity或者fragment中添加友盟统计

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长

        StatService.onResume(this);

        try {
            //领取答题次数
            if (getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("msg")) {
                fragments.clear();
                //添加fragment
                fragments.add(new HomeFragment());
                fragments.add(new ClassifyFragment());
                fragments.add(new ActivityFragment());
                fragments.add(new MineFragment());
                onClickListener.onClick(homeBottomeSwitcherContainer.getChildAt(3));
                setListener();
            } else if (getIntent().getStringExtra("from") != null && getIntent().getStringExtra("from").equals("h5")) {  //我的奖品h5返回首页
                fragments.clear();
                //添加fragment
                fragments.add(new HomeFragment());
                fragments.add(new ClassifyFragment());
                fragments.add(new ActivityFragment());
                fragments.add(new MineFragment());
                onClickListener.onClick(homeBottomeSwitcherContainer.getChildAt(0));
                setListener();
            }
        } catch (Exception e) {

        }

        isForeground = true;
        //版本更新
        updateVersion();

        Log.e("token", AppConst.TOKEN + "--------" + AppConst.sycm());

    }

    //更新版本
    ListDataSave listDataSave;

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

                                //保存h5 url地址
                                SaveUserInfo.getInstance(HomePageActivity.this).setUserInfo("box_h5_url", appVersionBean.getBoxUrl());
                                SaveUserInfo.getInstance(HomePageActivity.this).setUserInfo("exam_h5_url", appVersionBean.getExamUrl());

                                int mode = appVersionBean.getMode();
                                if (mode == 0) {
                                } else if (mode == 1) {
                                } else if (mode == 2) {
                                    updateDialog(appVersionBean.getUrl());
                                }
                            }
                            List<String> listUrl = new ArrayList<>();
                            listUrl.clear();
                            for (int i = 0; i < appVersionBean.getApis().size(); i++) {
                                listUrl.add(appVersionBean.getApis().get(i));
                            }
                            //保存轮询IP
                            try {
                                listDataSave = new ListDataSave(HomePageActivity.this, "url");
                                listDataSave.setDataList("forUrl", listUrl);
                                Log.e("url", listDataSave.getDataList("forUrl").toString());
                            } catch (Exception e) {

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.home_fragment_container);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (fragment instanceof MineFragment) {
                ((MineFragment) fragment).mineFragmentBack();
                return true;
            } else if (fragment instanceof HomeFragment) {
                ((HomeFragment) fragment).homeFragmentBack();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences sharedPreferences = getSharedPreferences("FirstRun", 0);
        Boolean first_run = sharedPreferences.getBoolean("First", true);
        sharedPreferences.edit().putBoolean("First", true).commit();

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

}
