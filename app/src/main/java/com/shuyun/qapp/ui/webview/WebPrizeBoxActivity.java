package com.shuyun.qapp.ui.webview;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
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
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.H5JumpBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.ReturnDialogBean;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.mine.NewRedWithdrawActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.view.H5JumpUtil;
import com.shuyun.qapp.view.InviteSharePopupUtil;
import com.shuyun.qapp.view.RealNamePopupUtil;

import java.util.ArrayList;
import java.util.List;

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

    private boolean show = false;
    ReturnDialogBean returnDialogBean;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    /**
                     * 检测微信是否安装,如果没有安装,需不显示分享按钮;如果安装了微信则显示分享按钮.
                     */
                    if (!MyApplication.mWxApi.isWXAppInstalled()) {

                    } else {
                        InviteSharePopupUtil.showSharedPop(WebPrizeBoxActivity.this, llWebBox);
                    }
                    break;
                default:
                    break;
            }
        }
    };

    class JsInteration {

        private static final String TAG = "JsInteration";

        public JsInteration() {
        }

        @JavascriptInterface
        public String answerLogin() {
            String answerHome = null;
            if (!EncodeAndStringTool.isObjectEmpty(answerHomeBean)) {
                answerHome = JSON.toJSONString(answerHomeBean);
            }
            Log.i(TAG, answerHome);
            return answerHome;
        }

        /**
         * 我的奖品开宝箱
         *
         * @return
         */
        @JavascriptInterface
        public String prizeData() {
            String main_box = getIntent().getStringExtra("main_box");
            String prizeBox = null;
            if (!EncodeAndStringTool.isStringEmpty(main_box) && "main_box".equals(main_box)) {
                boxBean = getIntent().getParcelableExtra("BoxBean");
                prizeBox = JSON.toJSONString(boxBean);
            } else {
                if (!EncodeAndStringTool.isObjectEmpty(minePrize)) {
                    prizeBox = JSON.toJSONString(minePrize);
                }
            }
            Log.i(TAG, prizeBox);
            return prizeBox;
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
                    Log.e("奖品返回", prizeData);
//                    MinePrize minePrize = JSON.parseObject(prizeData, MinePrize.class);
                    MinePrize minePrize = new Gson().fromJson(prizeData, MinePrize.class);
                    if (minePrize.getActionType().equals("action.h5.url")) {
                        //实物
                        Intent intent = new Intent(WebPrizeBoxActivity.this, WebH5Activity.class);
                        intent.putExtra("id", minePrize.getId());
                        intent.putExtra("url", minePrize.getH5Url());
                        intent.putExtra("name", minePrize.getName());
                        startActivity(intent);
                    } else if (minePrize.getActionType().equals("action.withdraw")) {
                        //红包
                        if (Integer.parseInt(SaveUserInfo.getInstance(WebPrizeBoxActivity.this).getUserInfo("cert")) == 1) {
                            if (!EncodeAndStringTool.isListEmpty(minePrize.getMinePrizes())) {
                                List<MinePrize.ChildMinePrize> redPrizeList = new ArrayList<>();
                                for (int i = 0; i < minePrize.getMinePrizes().size(); i++) {
                                    MinePrize.ChildMinePrize childMinePrize1 = minePrize.getMinePrizes().get(i);
                                    redPrizeList.add(childMinePrize1);
                                }
                                if (!EncodeAndStringTool.isListEmpty(redPrizeList)) {
                                    Intent intent = new Intent(WebPrizeBoxActivity.this, NewRedWithdrawActivity.class);
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
                            RealNamePopupUtil.showAuthPop(getApplicationContext(), llWebBox, getString(R.string.real_gift_describe));
                        }
                    } else if (minePrize.getActionType().equals("action.bp.use")) {
                        //积分
                        startActivity(new Intent(WebPrizeBoxActivity.this, IntegralExchangeActivity.class));

                    }
                }
            });

        }

        /**
         * 接收Js传的参数调用分享业务
         *
         * @param shareMode
         */
        @JavascriptInterface
        public void Share(String shareMode) {
            Log.i(TAG, "Share: " + shareMode);
            Message msg = new Message();
            msg.what = 1;
            handler.sendMessage(msg);
        }


        /**
         * 与js交互跳转到不同界面
         */
        @JavascriptInterface
        public void doJump(String data) {
            final H5JumpBean h5JumpBean = new Gson().fromJson(data, H5JumpBean.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        H5JumpUtil.dialogSkip(h5JumpBean.getBtnAction(), h5JumpBean.getContent(), h5JumpBean.getH5Url(), WebPrizeBoxActivity.this, llWebBox);
                    } catch (Exception e) {
                    }
                }
            });
            Log.e("data", data);
        }

        /**
         * 返回弹框h5调用方法
         */
        @JavascriptInterface
        public void pageLoad(String data) {
            returnDialogBean = new Gson().fromJson(data, ReturnDialogBean.class);
            show = returnDialogBean.isShow();
            Log.e("data", data);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        try {
            //我的奖品
            minePrize = intent.getParcelableExtra("ChildMinePrize");
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
                    if (show) {
                        exitDialog(returnDialogBean);
                    } else {
                        wvPrizeBox.goBack();
                    }
                } else {
                    if (show) {
                        exitDialog(returnDialogBean);
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
        if (wvPrizeBox.canGoBack()) {
            if (show) {
                exitDialog(returnDialogBean);
            } else {
                wvPrizeBox.goBack();
            }
        } else {
            if (show) {
                exitDialog(returnDialogBean);
            } else {
                super.onBackPressed();
            }
        }
    }

    /**
     * 中途退出弹窗
     */
    private void exitDialog(final ReturnDialogBean returnDialogBean) {
        new CircleDialog.Builder(this)
                .setTitle(returnDialogBean.getTitle())
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                        params.textSize = 40;
                    }
                })
                .setCanceledOnTouchOutside(false)
                .setText(returnDialogBean.getContent())
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.textSize = 40;
                        params.textColor = Color.parseColor("#666666");
                    }
                })
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative(returnDialogBean.getBtns().get(0).getLabel(), new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        String action = returnDialogBean.getBtns().get(0).getAction();
                        if ("return.previous.page".equals(action)) {
                            //返回上一页
                            if (wvPrizeBox.canGoBack()) {
                                wvPrizeBox.goBack();
                            } else {
                                finish();
                            }
                        } else if ("continue.to.perform".equals(action)) {
                            //继续执行

                        } else if ("determined.to.leave".equals(action)) {
                            //确定离开
                            finish();
                        } else if ("open.new.page".equals(action)) {
                            //新开页面
                            wvPrizeBox.loadUrl(returnDialogBean.getBtns().get(0).getH5Url());
                        }
                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#333333");
                    }
                })
                .setPositive(returnDialogBean.getBtns().get(1).getLabel(), new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        String action = returnDialogBean.getBtns().get(1).getAction();
                        if ("return.previous.page".equals(action)) {
                            //返回上一页
                            if (wvPrizeBox.canGoBack()) {
                                wvPrizeBox.goBack();
                            } else {
                                finish();
                            }
                        } else if ("continue.to.perform".equals(action)) {
                            //继续执行
                        } else if ("determined.to.leave".equals(action)) {
                            //确定离开
                            finish();
                        } else if ("open.new.page".equals(action)) {
                            //新开页面
                            wvPrizeBox.loadUrl(returnDialogBean.getBtns().get(1).getH5Url());
                        }
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#0194ec");
                    }
                })
                .configDialog(new ConfigDialog() {
                    @Override
                    public void onConfig(DialogParams params) {
                        params.animStyle = R.style.popwin_anim_style;
                    }
                })
                .show();
    }
}
