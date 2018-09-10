package com.shuyun.qapp.ui.login;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseSwipeBackActivity;
import com.shuyun.qapp.net.AppConst;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 用户协议
 */

public class UserAgreementActivity extends BaseSwipeBackActivity {

    @BindView(R.id.wv_user_agreement)
    WebView wvUserAgreement;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.animation_iv)
    ImageView animationIv;

    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("用户协议");

        animationDrawable = (AnimationDrawable) animationIv.getDrawable();
        animationDrawable.start();


    }

    @Override
    public int intiLayout() {
        return R.layout.activity_user_agreement;
    }

    @OnClick(R.id.iv_back)
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                //关闭按钮,返回键效果
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (wvUserAgreement.canGoBack()) {
            wvUserAgreement.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
        WebSettings settings = wvUserAgreement.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvUserAgreement.loadUrl(AppConst.USER_AGREEMENT);
        wvUserAgreement.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作，我这边是弹出dialog
                if (progress == 100) {
                    animationDrawable.stop();
                    animationIv.setVisibility(View.GONE);
                }
            }
        });

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }

}
