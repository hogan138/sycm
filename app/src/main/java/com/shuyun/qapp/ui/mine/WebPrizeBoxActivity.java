package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebPrizeBoxActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

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

    class JsInteration {

        private static final String TAG = "JsInteration";

        public JsInteration() {
        }

        @JavascriptInterface
        public String answerLogin() {
            String answerHome = null;
            if (!EncodeAndStringTool.isObjectEmpty(answerHomeBean)) {
                answerHome = new Gson().toJson(answerHomeBean);
            }
            Log.i(TAG, answerHome.toString());
            return answerHome.toString();
        }

        /**
         * 我的奖品开宝箱
         *
         * @return
         */
        @JavascriptInterface
        public String prizeData() {
            String prizeBox = null;
            if (!EncodeAndStringTool.isStringEmpty(getIntent().getStringExtra("main_box")) && getIntent().getStringExtra("main_box").equals("main_box")) {
                boxBean = getIntent().getParcelableExtra("BoxBean");
                prizeBox = new Gson().toJson(boxBean);
            } else {
                if (!EncodeAndStringTool.isObjectEmpty(minePrize)) {
                    prizeBox = new Gson().toJson(minePrize);
                }
            }
            Log.i(TAG, prizeBox.toString());
            return prizeBox.toString();
        }

        /**
         * 头部标题
         *
         * @param page  是否显示分享图标1:显示;其他值不显示
         * @param title 标题
         * @param id    答题Id
         */
        @JavascriptInterface
        public void header(final int page, final String title, String id) {
            Log.i(TAG, page + "");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCommonTitle.setText(title);
                }
            });
        }

        /**
         * 衢州活动弹窗和商户引流跳外部链接
         *
         * @param url
         */
        @JavascriptInterface
        public void openWeb(final String url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Uri uri = Uri.parse(url);
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
        }

        /**
         * JS调用此方法,关闭H5页面进入App页面
         */
        @JavascriptInterface
        public void closeWeb() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }

        /**
         * h5页面调用网络出现错误码
         */
        @JavascriptInterface
        public void newLoginDate(final String err, final String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ErrorCodeTools.errorCodePrompt(WebPrizeBoxActivity.this, err, msg);
                }
            });
        }

        /**
         * 开宝箱奖品跳转
         */
        @JavascriptInterface
        public void gotoprize(final String prizeData) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("奖品返回", prizeData.toString());
                    MinePrize minePrize = new Gson().fromJson(prizeData.toString(), MinePrize.class);
                    if (minePrize.getActionType().equals("action.h5.url")) {
                        //实物
                        Intent intent = new Intent(WebPrizeBoxActivity.this, WebPrizeActivity.class);
                        intent.putExtra("id", minePrize.getId());
                        intent.putExtra("url", minePrize.getH5Url());
                        intent.putExtra("name", minePrize.getName());
                        startActivity(intent);
                    } else if (minePrize.getActionType().equals("action.withdraw")) {
                        //红包
                        if (Integer.parseInt(SaveUserInfo.getInstance(WebPrizeBoxActivity.this).getUserInfo("cert")) == 1) {
                            if (!EncodeAndStringTool.isListEmpty(minePrize.getMinePrizes())) {
                                List<MinePrize.minePrize> redPrizeList = new ArrayList<>();
                                for (int i = 0; i < minePrize.getMinePrizes().size(); i++) {
                                    MinePrize.minePrize minePrize1 = minePrize.getMinePrizes().get(i);
                                    redPrizeList.add(minePrize1);
                                }
                                if (!EncodeAndStringTool.isListEmpty(redPrizeList)) {
                                    Intent intent = new Intent(WebPrizeBoxActivity.this, RedWithDrawActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putParcelableArrayList("redPrize", (ArrayList<? extends Parcelable>) redPrizeList);
                                    bundle.putString("redId", minePrize.getId());
                                    bundle.putString("from", "box");
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        } else {
                            //显示实名认证弹窗
                            showAuthPop();
                        }
                    } else if (minePrize.getActionType().equals("action.bp.use")) {
                        //积分
                        startActivity(new Intent(WebPrizeBoxActivity.this, IntegralExchangeActivity.class));
                    }
                }
            });

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        try {
            //我的奖品
            minePrize = intent.getParcelableExtra("minePrize");
            boxBean = intent.getParcelableExtra("BoxBean");

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
        wvPrizeBox.addJavascriptInterface(new JsInteration(), "android");
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
                    wvPrizeBox.goBack();
                } else {
                    finish();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 实名认证popupWindow
     */
    CommonPopupWindow commonPopupWindow;

    public void showAuthPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(commonPopupWindow)) && commonPopupWindow.isShowing())
            return;
        View upView = LayoutInflater.from(this).inflate(R.layout.real_name_auth_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.real_name_auth_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        commonPopupWindow.showAtLocation(llWebBox, Gravity.CENTER, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.real_name_auth_popupwindow:
                ImageView ivClose1 = (ImageView) view.findViewById(R.id.iv_close_icon1);
                Button btnRealNameAuth = (Button) view.findViewById(R.id.btn_real_name_auth1);
                ivClose1.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (commonPopupWindow != null && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                btnRealNameAuth.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (commonPopupWindow != null && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                        }
                        startActivity(new Intent(WebPrizeBoxActivity.this, RealNameAuthActivity.class));
                    }
                });
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (wvPrizeBox.canGoBack()) {
            wvPrizeBox.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
