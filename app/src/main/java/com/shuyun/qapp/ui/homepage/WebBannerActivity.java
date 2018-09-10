package com.shuyun.qapp.ui.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseSwipeBackActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebBannerActivity extends BaseSwipeBackActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.wv_banner)
    WebView wvBanner;//首页轮播图跳转页面
    private int type;
    private String url;

    //从广告页进入
    private String splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initData();
        if (type != 0) {
            WebSettings settings = wvBanner.getSettings();
            settings.setSupportZoom(true);//支持缩放
            settings.setJavaScriptEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            wvBanner.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                    return false;
                }
            });
            wvBanner.loadUrl(url);

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
        /**
         * 链接的类型1:外部链接;2:内部链接
         */
        type = intent.getIntExtra("type", 0);
        url = intent.getStringExtra("url");

        splash = getIntent().getStringExtra("from");
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
                    wvBanner.goBack();
                } else {
                    if (!EncodeAndStringTool.isStringEmpty(splash)) {
                        if (splash.equals("splash")) {
                            finish();
                            Intent intent = new Intent(WebBannerActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        finish();
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
            wvBanner.goBack();
        } else {
            if (!EncodeAndStringTool.isStringEmpty(splash)) {
                if (splash.equals("splash")) {
                    finish();
                    Intent intent = new Intent(WebBannerActivity.this, HomePageActivity.class);
                    startActivity(intent);
                }
            } else {
                finish();
            }
        }
    }
}
