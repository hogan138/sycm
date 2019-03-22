package com.shuyun.qapp.ui.webview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.JsInterationUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * h5开宝箱webview
 */
public class WebPrizeBoxActivity extends BaseActivity {

    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();
    @BindView(R.id.iv_left_back)
    ImageView ivLeftBack;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.wv_prize_box)
    WebView wvPrizeBox;
    @BindView(R.id.ll_web_box)
    LinearLayout llWebBox;
    private MinePrize minePrize;

    private BoxBean boxBean;

    String main_box;


    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        context = this;

        Intent intent = getIntent();
        try {
            //我的奖品
            minePrize = intent.getParcelableExtra("ChildMinePrize");
            boxBean = intent.getParcelableExtra("BoxBean");
            main_box = getIntent().getStringExtra("main_box");
        } catch (Exception e) {

        }

        answerHomeBean.setToken(AppConst.TOKEN);
        answerHomeBean.setRandom(AppConst.RANDOM);
        answerHomeBean.setV(AppConst.V);
        answerHomeBean.setSalt(AppConst.SALT);
        answerHomeBean.setAppSecret(AppConst.APP_KEY);
        String deviceId = SmAntiFraud.getDeviceId();
        answerHomeBean.setDeviceId(deviceId);

        tvCommonTitle.setText("全民共进");
        WebSettings settings = wvPrizeBox.getSettings();
        settings.setJavaScriptEnabled(true);
        wvPrizeBox.setWebChromeClient(new WebChromeClient());//解决答题时无法弹出dialog问题.
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvPrizeBox.addJavascriptInterface(new JsInterationUtil(answerHomeBean, (Activity) context, llWebBox, main_box, minePrize, boxBean, tvCommonTitle, wvPrizeBox), "android");
        // 允许混合内容 解决部分手机 加载不出https请求里面的http下的图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (!EncodeAndStringTool.isStringEmpty(getIntent().getStringExtra("main_box")) && getIntent().getStringExtra("main_box").equals("main_box")) {
            if (!EncodeAndStringTool.isStringEmpty(boxBean.getH5Url())) {
                //首页开宝箱
                wvPrizeBox.loadUrl(boxBean.getH5Url());
            } else {
                //为空加载本地
                wvPrizeBox.loadUrl(AppConst.BOX);
            }
        } else if (!EncodeAndStringTool.isStringEmpty(getIntent().getStringExtra("main_box")) && getIntent().getStringExtra("main_box").equals("my_prize")) {
            if (!EncodeAndStringTool.isStringEmpty(minePrize.getH5Url())) {
                //我的奖品开宝箱
                wvPrizeBox.loadUrl(minePrize.getH5Url());
            } else {
                //为空加载本地
                wvPrizeBox.loadUrl(AppConst.BOX);
            }
        } else if (!EncodeAndStringTool.isStringEmpty(getIntent().getStringExtra("main_box")) && getIntent().getStringExtra("main_box").equals("score_box")) {
            String h5Url = getIntent().getStringExtra("h5Url");
            if (!EncodeAndStringTool.isStringEmpty(h5Url)) {
                //积分开宝箱
                wvPrizeBox.loadUrl(h5Url);
            } else {
                //为空加载本地
                wvPrizeBox.loadUrl(AppConst.BOX);
            }
        }


    }

    @Override
    public int intiLayout() {
        return R.layout.activity_web_prize_box;
    }

    @OnClick({R.id.iv_left_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_left_back:
                if (wvPrizeBox.canGoBack()) {
                    JsInterationUtil.WebViewCanBack();
                } else {
                    JsInterationUtil.WebViewNoBack();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (wvPrizeBox.canGoBack()) {
            JsInterationUtil.WebViewCanBack();
        } else {
            JsInterationUtil.WebViewNoBack();
        }
    }

}
