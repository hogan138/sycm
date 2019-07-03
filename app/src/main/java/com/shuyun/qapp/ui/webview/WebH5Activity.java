package com.shuyun.qapp.ui.webview;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.manager.MyActivityManager1;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.JsInterationUtil;
import com.shuyun.qapp.view.RippleLayout;

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
    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;

    private String url = "";
    private String id = "";
    //从广告页进入
    private String splash = "";

    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();

    Context context;

    //    @TargetApi(19)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        MyActivityManager1.getInstance().pushOneActivity(this);

        ButterKnife.bind(this);

        initData();

        //显示加载进度
        CustomLoadingFactory factory = new CustomLoadingFactory();
        LoadingBar.make(rlMain, factory).show();

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
        settings.setDomStorageEnabled(true);// 打开本地缓存提供JS调用,至关重要
        settings.setAppCacheMaxSize(1024 * 1024 * 8);// 实现8倍缓存
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setAppCachePath(this.getCacheDir().getAbsolutePath());
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvBanner.addJavascriptInterface(new JsInterationUtil(id, (Activity) context, tvCommonTitle, ivRightIcon, rlMain, tvRight, wvBanner, answerHomeBean, ivLeftBack, rlTitle), "android");
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
        wvBanner.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    //隐藏进度条
                    LoadingBar.cancel(rlMain);
                }
            }
        });
//        WebView.setWebContentsDebuggingEnabled(true);
//        //从发现页进入展开动画
//        if (!EncodeAndStringTool.isStringEmpty(splash)) {
//            if (splash.equals("found") || splash.equals("home")) {
//                doRippleAnim(0f, 1);
//            }
//
//        }

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_web_banner;
    }

    @Override
    protected void onResume() {
        super.onResume();
        wvBanner.addJavascriptInterface(new JsInterationUtil(id, (Activity) context, tvCommonTitle, ivRightIcon, rlMain, tvRight, wvBanner, answerHomeBean, ivLeftBack, rlTitle), "android");
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
                        } else if (splash.equals("found") || splash.equals("home")) {
//                            //从发现页退出动画
//                            doRippleAnim(1, 0f);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
                            finish();
//                                }
//                            }, 800);
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
                } else if (splash.equals("found") || splash.equals("home")) {
                    //从发现页退出动画
//                    doRippleAnim(1, 0f);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
                    finish();
//                        }
//                    }, 800);
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
