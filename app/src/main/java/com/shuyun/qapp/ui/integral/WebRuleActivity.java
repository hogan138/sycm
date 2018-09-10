package com.shuyun.qapp.ui.integral;

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
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 积分夺宝规则
 */
public class WebRuleActivity extends BaseSwipeBackActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.wv_look_rules)
    WebView wvLookRules;
    @BindView(R.id.animation_iv)
    ImageView animationIv;

    AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        animationDrawable = (AnimationDrawable) animationIv.getDrawable();
        animationDrawable.start();

        tvCommonTitle.setText("活动规则");
        ivBack.setOnClickListener(new OnMultiClickListener() {
            @Override
            public void onMultiClick(View v) {
                finish();
            }
        });

        WebSettings settings = wvLookRules.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptEnabled(true);
        wvLookRules.loadUrl(SaveUserInfo.getInstance(WebRuleActivity.this).getUserInfo("h5_rule"));

        wvLookRules.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                if (progress == 100) {
                    animationDrawable.stop();
                    animationIv.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_web_rule;
    }
}
