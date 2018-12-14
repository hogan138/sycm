package com.shuyun.qapp.ui.welcome;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AdBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.tencent.stat.MtaSDkException;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.tencent.stat.common.StatConstants;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.shuyun.qapp.utils.EncodeAndStringTool.encryptMD5ToString;
import static com.shuyun.qapp.utils.EncodeAndStringTool.getCode;

/**
 * 启动页
 */
public class WelcomeActivity extends BaseActivity implements OnRemotingCallBackListener<AdBean> {
    private static final int LOGIN_MODE = 1;

    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.iv_advertising)
    ImageView ivAdvertising;
    @BindView(R.id.wAd)
    RelativeLayout wAd;

    private CountDownTimer timer; //倒计时
    private Context mContext;
    private AdBean adBean; //广告bean

    private Handler mHandler = new Handler();
    private boolean isLoading = false;
    private boolean isStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
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
        RemotingEx.doRequest(null, ApiServiceBean.getAd(), new Object[]{
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
        ImageLoaderManager.LoadImage(mContext, adBean.getAd().get(0).getUrl(), ivAdvertising, R.mipmap.zw01);

        final Long model = adBean.getAd().get(0).getModel();
        final String content = adBean.getAd().get(0).getContent();
        final Long isLogin = adBean.getAd().get(0).getIsLogin();
        ivAdvertising.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                //TODO 这里不需要进行逻辑判断 一律打开HomePage 在首页进行处理 默认首页不进行数据请求
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
        wAd.setVisibility(View.VISIBLE);

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
        if(isStop)
            return;
        isStop = true;
        Intent intent = new Intent(mContext, HomePageActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        if (timer != null)
            timer.cancel();

        super.onDestroy();
    }

    //在activity或者fragment中添加友盟统计
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
        StatService.onResume(this);

        if (isLoading)
            return;
        isLoading = true;
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //获取广告
                getAd();
            }
        }, 100);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {
        skip();
    }

    @Override
    public void onSucceed(String action, DataResponse<AdBean> response) {
        if (response.isSuccees()) {
            adBean = response.getDat();
            if (!EncodeAndStringTool.isListEmpty(adBean.getAd())) {
                //加载广告页
                showPop();

                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //倒计时结束
                        initTime();
                    }
                }, 1000);
            } else {
                //进入首页
                skip();
            }
        } else {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
        }
    }
}
