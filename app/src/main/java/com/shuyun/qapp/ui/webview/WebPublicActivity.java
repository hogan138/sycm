package com.shuyun.qapp.ui.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.PhoneUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.JsInterationUtil;
import com.shuyun.qapp.utils.SaveUserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公用的静态webview
 */
public class WebPublicActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.webView)
    WebView webView;
    @BindView(R.id.animation_iv)
    ImageView animationIv;

    private String bulletin;
    AnimationDrawable animationDrawable;

    String name = "";

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        context = getApplicationContext();

        name = getIntent().getStringExtra("name");
        if ("useragree".equals(name)) {
            tvCommonTitle.setText("用户协议");
        } else if ("about".equals(name)) {
            tvCommonTitle.setText("关于我们");
        } else if ("rule".equals(name)) {
            tvCommonTitle.setText("活动规则");
        } else if ("rules".equals(name)) {
            tvCommonTitle.setText("活动规则");
            Intent intent = getIntent();
            bulletin = intent.getStringExtra("bulletin");
        }

        animationDrawable = (AnimationDrawable) animationIv.getDrawable();
        animationDrawable.start();


    }

    @Override
    public int intiLayout() {
        return R.layout.activity_contact_us;
    }


    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    public void onResume() {
        super.onResume();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JsInterationUtil((Activity) context, bulletin), "android");
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        if ("useragree".equals(name)) {
            webView.loadUrl(AppConst.USER_AGREEMENT); //用户协议
        } else if ("about".equals(name)) {
            webView.loadUrl(AppConst.ABOUT_US); //关于我们
        } else if ("rule".equals(name)) {
            webView.loadUrl(SaveUserInfo.getInstance(WebPublicActivity.this).getUserInfo("h5_rule"));//积分夺宝规则
        } else if ("rules".equals(name)) {
            webView.loadUrl(AppConst.LOOK_RULES);//积分开宝箱规则
        }

        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作，我这边是弹出dialog
                if (progress == 100) {
                    animationDrawable.stop();
                    animationIv.setVisibility(View.GONE);
                }
            }
        });
    }
}
