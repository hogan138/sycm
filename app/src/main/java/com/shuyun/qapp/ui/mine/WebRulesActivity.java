package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BaseSwipeBackActivity;
import com.shuyun.qapp.net.AppConst;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebRulesActivity extends BaseSwipeBackActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.wv_look_rules)
    WebView wvLookRules;
    @BindView(R.id.animation_iv)
    ImageView animationIv;
    AnimationDrawable animationDrawable;
    private String bulletin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        animationDrawable = (AnimationDrawable) animationIv.getDrawable();
        animationDrawable.start();

        tvCommonTitle.setText("活动规则");
        Intent intent = getIntent();
        bulletin = intent.getStringExtra("bulletin");
        wvLookRules.getSettings().setJavaScriptEnabled(true);
        wvLookRules.addJavascriptInterface(new JsInteration(), "android");
        wvLookRules.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvLookRules.loadUrl(AppConst.LOOK_RULES);

        wvLookRules.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {
                //当进度走到100的时候做自己的操作，我这边是弹出dialog
                if (progress == 100) {
                    animationDrawable.stop();
                    animationIv.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_rules;
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

    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }

    class JsInteration {

        private static final String TAG = "JsInteration";

        public JsInteration() {
        }

        /**
         * 接收js返回的规则
         *
         * @param
         */
        @JavascriptInterface
        public String actRule() {//String backParams
            return bulletin;
        }
    }
}
