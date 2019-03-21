package com.shuyun.qapp.ui.webview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.alipay.AlipayTradeManager;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.H5JumpBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.ReturnDialogBean;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.mine.NewRedWithdrawActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.JsInterationUtil;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.LoginJumpUtil;
import com.shuyun.qapp.view.RealNamePopupUtil;
import com.shuyun.qapp.view.RippleLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * h5页面（广告、首页邀请、首页轮播图、首页弹框、极光推送消息、消息列表、票务、招商银行）
 */

public class WebH5Activity extends BaseActivity {

    @BindView(R.id.wv_banner)
    WebView wvBanner; //webview
    @BindView(R.id.rl_main)
    RippleLayout rlMain;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack; //返回键
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon; //右边分享按钮

    private String url;
    private String id;
    //从广告页进入
    private String splash;

    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();

        ButterKnife.bind(this);

        initData();

        answerHomeBean.setToken(AppConst.TOKEN);
        answerHomeBean.setRandom(AppConst.RANDOM);
        answerHomeBean.setV(AppConst.V);
        answerHomeBean.setSalt(AppConst.SALT);
        answerHomeBean.setAppSecret(AppConst.APP_KEY);
        String deviceId = SmAntiFraud.getDeviceId();
        answerHomeBean.setDeviceId(deviceId);

        WebSettings settings = wvBanner.getSettings();
        settings.setSupportZoom(true);//支持缩放
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvBanner.addJavascriptInterface(new JsInterationUtil(id, (Activity) context, tvCommonTitle, ivRightIcon, rlMain, tvRight, wvBanner, answerHomeBean), "android");
        // 允许混合内容 解决部分手机 加载不出https请求里面的http下的图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        wvBanner.setWebViewClient(new WebViewClient() {
            @Override
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                if (url.contains("platformapi/startapp")) {
                    try {
                        Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                        intent.addCategory("android.intent.category.BROWSABLE");
                        intent.setComponent(null);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                    }
                }
                return false;
            }
        });

        wvBanner.loadUrl(url);

        //从发现页进入展开动画
        if (!EncodeAndStringTool.isStringEmpty(splash)) {
            if (splash.equals("found")) {
                doRippleAnim(0f, 1);
            }
        }


    }

    @Override
    public int intiLayout() {
        return R.layout.activity_web_banner;
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        try {
            splash = getIntent().getStringExtra("from");
            //奖品id
            id = intent.getStringExtra("id");
        } catch (Exception e) {

        }

        String name = intent.getStringExtra("name");
        if (name != null) {
            tvCommonTitle.setText(name);
        }

    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (wvBanner.canGoBack()) {
                    JsInterationUtil.WebViewCanBack();
                } else {
                    if (!EncodeAndStringTool.isStringEmpty(splash)) {
                        if (splash.equals("splash")) {
                            finish();
                            Intent intent = new Intent(WebH5Activity.this, HomePageActivity.class);
                            startActivity(intent);
                        } else if (splash.equals("found")) {
                            //从发现页退出动画
                            doRippleAnim(1, 0f);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                }
                            }, 800);
                        }
                    } else {
                        JsInterationUtil.WebViewNoBack();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (wvBanner.canGoBack()) {
            JsInterationUtil.WebViewCanBack();
        } else {
            if (!EncodeAndStringTool.isStringEmpty(splash)) {
                if (splash.equals("splash")) {
                    finish();
                    Intent intent = new Intent(WebH5Activity.this, HomePageActivity.class);
                    startActivity(intent);
                } else if (splash.equals("found")) {
                    //从发现页退出动画
                    doRippleAnim(1, 0f);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 800);
                }
            } else {
                JsInterationUtil.WebViewNoBack();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空所有Cookie
        CookieSyncManager.createInstance(SykscApplication.getAppContext());  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        wvBanner.setWebChromeClient(null);
        wvBanner.setWebViewClient(null);
        wvBanner.getSettings().setJavaScriptEnabled(false);
        wvBanner.clearCache(true);
    }

    //水波纹展开动画
    private void doRippleAnim(final float fromPercent, final float toPercent) {
        ValueAnimator animator = ValueAnimator.ofFloat(fromPercent, toPercent).setDuration(1000);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                final float progress = fromPercent + animation.getAnimatedFraction() * (toPercent - fromPercent);

                rlMain.setProgress(progress);

            }
        });
        animator.start();

    }
}
