package com.shuyun.qapp.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AdBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.TouristsBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.SizeUtils.dp2px;
import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 广告页
 */
public class WelcomeActivity extends BaseActivity implements OnRemotingCallBackListener<Object> {
    private static final int LOGIN_MODE = 1;

    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.icon)
    ImageView icon;
    @BindView(R.id.mainImg)
    ImageView mainImg;
    @BindView(R.id.bottomIcon)
    ImageView bottomIcon;
    @BindView(R.id.bottomLayout)
    LinearLayout bottomLayout;

    private CountDownTimer timer; //倒计时
    private Context mContext;
    private AdBean adBean; //广告bean

    private Handler mHandler = new Handler();
    private boolean isLoading = false;
    private boolean isStop = false;

    private int height = 0;

    /**
     * 动画
     */
    Animation mAnimation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        int w = (int) Math.ceil(dm.widthPixels);
        height = (int) Math.ceil(w * (722f / 504f));

        ViewGroup.LayoutParams params = mainImg.getLayoutParams();
        params.height = height;
        mainImg.setLayoutParams(params);

        //腾讯应用分析
        StatConfig.init(this);
        String appkey = "Aqc1105860265";
        try {
            // 第三个参数必须为：com.tencent.stat.common.StatConstants.VERSION
            StatService.startStatService(this, appkey, StatConstants.VERSION);
        } catch (MtaSDkException e) {
            // MTA初始化失败
            Log.e("MTA", "MTA start failed");
        }

        //保存登录状态
        SaveUserInfo.getInstance(this).setUserInfo("LOGIN_MODE", String.valueOf(LOGIN_MODE));
        SaveUserInfo.getInstance(mContext).setUserInfo("tourists", "0");

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_welcome;
    }

    private void getAd() {
        long curTime = System.currentTimeMillis();
        StringBuilder sb = new StringBuilder();
        sb.append(AppConst.DEV_ID)
                .append(AppConst.APP_ID)
                .append(AppConst.V)
                .append(curTime)
                .append(AppConst.APP_KEY);

        //将拼接的字符串转化为16进制MD5
        String myCode = encryptMD5ToString(sb.toString());
        String signCode = getCode(myCode);
        RemotingEx.doRequest(AppConst.WELCOME_AD, ApiServiceBean.getAd(), new Object[]{
                AppConst.DEV_ID,
                AppConst.APP_ID,
                AppConst.V,
                String.valueOf(curTime),
                signCode
        }, this);
    }

    /**
     * 广告弹框
     */
    public void showPop() {
        icon.setVisibility(View.GONE);
        bottomIcon.setImageResource(R.mipmap.start_logo);
        bottomLayout.setVisibility(View.VISIBLE);

        AdBean.AdInfo info = adBean.getAd().get(0);
        ImageLoaderManager.LoadImage(mContext, info.getUrl(), mainImg, R.mipmap.jiazai);
        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.image_alpha);
        mainImg.setAnimation(mAnimation);
        mAnimation.start();

        final Long model = info.getModel();
        final String content = info.getContent();
        final Long isLogin = info.getIsLogin();

        mainImg.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //TODO 这里不需要进行逻辑判断 一律打开HomePage 在首页进行处理 默认首页不进行数据请求
                //保存登录模式
                loginMode();
                isStop = true;
                Intent intent = new Intent();
                intent.putExtra("from", "welcome");
                intent.putExtra("model", model);
                intent.putExtra("content", content);
                intent.putExtra("examUrl", adBean.getExamUrl());
                intent.putExtra("isLogin", isLogin);
                intent.setClass(mContext, HomePageActivity.class);
                startActivity(intent);
                finish();
            }
        });
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skip();
            }
        });
    }

    private void initTime() {
        tvSkip.setVisibility(View.VISIBLE);
        mainImg.setVisibility(View.VISIBLE);

        long time = 3;
        Long timeout = adBean.getTimeout();
        if (timeout != null && timeout != 0)
            time = timeout;

        timer = new CountDownTimer(time * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                //倒计时3秒后跳出进入首页
                skip();
            }
        }.start();
    }

    //跳转
    private void skip() {
        if (isStop)
            return;
        isStop = true;

        //保存登录模式
        loginMode();

        Intent intent = new Intent(mContext, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    //保存登录模式
    private void loginMode() {
        SaveUserInfo.getInstance(mContext).setUserInfo("normal_login", SaveUserInfo.getInstance(mContext).getUserInfo("tourists"));
    }

    @Override
    protected void onDestroy() {
        if (timer != null)
            timer.cancel();
        if (mAnimation != null)
            mAnimation.cancel();
        super.onDestroy();
    }

    //在activity或者fragment中添加友盟统计
    @Override
    protected void onResume() {
        super.onResume();

        if (isLoading)
            return;
        isLoading = true;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取广告
                getAd();
            }
        }, 1600);

        //获取是否需要登录
        RemotingEx.doRequest(AppConst.TOURISTS, ApiServiceBean.tourists(), null, this);

    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {
        SaveUserInfo.getInstance(mContext).setUserInfo("tourists", "0");
        skip();
    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (AppConst.WELCOME_AD.equals(action)) { //广告页接口
            if (response.isSuccees()) {
                adBean = (AdBean) response.getDat();
                if (!EncodeAndStringTool.isListEmpty(adBean.getAd())) {
                    //加载广告页
                    showPop();

                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //倒计时
                            initTime();
                        }
                    }, 1000);
                } else {
                    //进入首页
                    skip();
                }
            } else {
                ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
                //进入首页
                skip();
            }
        } else if (AppConst.TOURISTS.equals(action)) { //是否是游客模式
            if (response.isSuccees()) {
                TouristsBean touristsBean = (TouristsBean) response.getDat();
                SaveUserInfo.getInstance(mContext).setUserInfo("tourists", touristsBean.getMode());
            } else {
                SaveUserInfo.getInstance(mContext).setUserInfo("tourists", "0");
            }
        }
    }
}
