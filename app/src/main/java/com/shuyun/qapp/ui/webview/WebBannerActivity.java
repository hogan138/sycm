package com.shuyun.qapp.ui.webview;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.shuyun.qapp.bean.AuthHeader;
import com.shuyun.qapp.bean.H5JumpBean;
import com.shuyun.qapp.bean.ReturnDialogBean;
import com.shuyun.qapp.bean.SharePublicBean;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.mine.AccountRecordActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.JumpTomap;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.H5JumpUtil;
import com.shuyun.qapp.view.InviteSharePopupUtil;
import com.shuyun.qapp.view.SharePopupUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * h5页面（广告、首页邀请、首页轮播图、首页弹框、极光推送消息、消息列表、票务、招商银行）
 */

public class WebBannerActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.wv_banner)
    WebView wvBanner;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
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

    //经纬度、名称
    private String Longitude;
    private String Latitude;
    private String Name;

    private boolean show = false;
    ReturnDialogBean returnDialogBean;

    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        wvBanner.addJavascriptInterface(new JsInteration(), "android");
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
                    if (show) {
                        exitDialog(returnDialogBean);
                    } else {
                        wvBanner.goBack();
                    }
                } else {
                    if (!EncodeAndStringTool.isStringEmpty(splash)) {
                        if (splash.equals("splash")) {
                            finish();
                            Intent intent = new Intent(WebBannerActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        if (show) {
                            exitDialog(returnDialogBean);
                        } else {
                            finish();
                        }
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
            if (show) {
                exitDialog(returnDialogBean);
            } else {
                wvBanner.goBack();
            }
        } else {
            if (!EncodeAndStringTool.isStringEmpty(splash)) {
                if (splash.equals("splash")) {
                    finish();
                    Intent intent = new Intent(WebBannerActivity.this, HomePageActivity.class);
                    startActivity(intent);
                }
            } else {
                if (show) {
                    exitDialog(returnDialogBean);
                } else {
                    finish();
                }
            }
        }
    }

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
                        InviteSharePopupUtil.showSharedPop(WebBannerActivity.this, rlMain);
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
        public String getTicketData(String jsParams) {
            AuthHeader authHeader = new AuthHeader();
            authHeader.setAuthorization(AppConst.TOKEN);
            authHeader.setSycm(AppConst.sycm());
            if (!EncodeAndStringTool.isStringEmpty(id)) {
                authHeader.setId(id);
            }
            return JSON.toJSONString(authHeader);
        }

        /**
         * JS调用此方法,关闭H5页面进入App页面
         */
        @JavascriptInterface
        public void closeWeb(String jsParams) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
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
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCommonTitle.setText(title);
                }
            });
        }


        /**
         * 是否显示右边分享按钮
         */
        @JavascriptInterface
        public void showShare(final String config) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    SharePublicBean sharedBean = new Gson().fromJson(config, SharePublicBean.class);
                    if (sharedBean.isShow()) {
                        if (!MyApplication.mWxApi.isWXAppInstalled()) {
                            ivRightIcon.setVisibility(View.GONE);
                        } else {
                            ivRightIcon.setImageResource(R.mipmap.share);//右侧分享
                            ivRightIcon.setOnClickListener(new OnMultiClickListener() {
                                @Override
                                public void onMultiClick(View v) {
                                    SharePopupUtil.showSharedPop(config, WebBannerActivity.this, rlMain);
                                }
                            });
                        }
                    } else {
                        ivRightIcon.setVisibility(View.GONE);
                    }
                }
            });

        }

        /**
         * 顶部管理按钮显示隐藏
         */
        @JavascriptInterface
        public void headBtn(final String title, final String name, final String type, final String funname) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tvCommonTitle.setText(title);
                    if (type.equals("0")) {
                        //不显示
                        tvRight.setVisibility(View.GONE);
                    } else if (type.equals("1")) {
                        //显示
                        tvRight.setVisibility(View.VISIBLE);
                        tvRight.setText(name);
                        tvRight.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                wvBanner.loadUrl("javascript:" + funname + "()");
                            }
                        });
                    }
                }
            });
        }

        @JavascriptInterface
        public String sendKey() {
            AuthHeader authHeader = new AuthHeader();
            authHeader.setAuthorization(AppConst.TOKEN);
            authHeader.setSycm(AppConst.sycm());
            return JSON.toJSONString(authHeader);
        }

        /**
         * h5页面调用网络出现错误码
         */
        @JavascriptInterface
        public void newLoginDate(final String err, final String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ErrorCodeTools.errorCodePrompt(WebBannerActivity.this, err, msg);
                }
            });
        }

        /**
         * 调起本地地图
         */
        @JavascriptInterface
        public void gotomap(String name, String longitude, String latitude) {
            Longitude = longitude;
            Latitude = latitude;
            Name = name;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (JumpTomap.isAvilible(WebBannerActivity.this, "com.tencent.map")
                            || JumpTomap.isAvilible(WebBannerActivity.this, "com.baidu.BaiduMap")
                            || JumpTomap.isAvilible(WebBannerActivity.this, "com.autonavi.minimap")) {
                        showToMap();
                    } else {
                        showDialog();
                    }
                }
            });
        }

        /**
         * 登录返回的token需要传给H5
         *
         * @return
         */
        @JavascriptInterface
        public String answerLogin() {
            String answerHome = null;
            if (!EncodeAndStringTool.isObjectEmpty(answerHomeBean)) {
                answerHome = JSON.toJSONString(answerHomeBean);
            }
            Log.e(TAG, answerHome);
            return answerHome;
        }


        /**
         * 提现成功
         */
        @JavascriptInterface
        public void goWebview(final String h5url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    wvBanner.loadUrl(h5url);
                }
            });
        }

        /**
         * JS调用此方法,关闭H5页面进入首页面
         */
        @JavascriptInterface
        public void goMain() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    finish();
                    Intent intent = new Intent(WebBannerActivity.this, HomePageActivity.class);
                    intent.putExtra("from", "h5");
                    startActivity(intent);
                }
            });
        }

        /**
         * 提现失败，进入我的账户
         */
        @JavascriptInterface
        public void goAccount(final String err) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(WebBannerActivity.this, AccountRecordActivity.class));
                }
            });
        }


        /**
         * 接收js返回的手机号码,调用打电话业务
         *
         * @param phoneNum
         */
        @JavascriptInterface
        public void OpenTel(String phoneNum) {
            Log.i(TAG, "OpenTel: " + phoneNum);
            //调用打电话业务
            call(phoneNum);
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
         * 公共分享方法
         */
        @JavascriptInterface
        public void doShare(final String config) {
            if (!MyApplication.mWxApi.isWXAppInstalled()) {

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        SharePopupUtil.showSharedPop(config, WebBannerActivity.this, rlMain);
                    }
                });
            }
        }

        /**
         * 接收js返回的手机号码,调用发短信业务
         *
         * @param phoneNum
         */
        @JavascriptInterface
        public void sms(String phoneNum) {
            //调用发短信业务
            doSendSMSTo(phoneNum);
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


        /**
         * 与js交互跳转到不同界面
         */
        @JavascriptInterface
        public void doJump(String data) {
            final H5JumpBean h5JumpBean = new Gson().fromJson(data, H5JumpBean.class);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    H5JumpUtil.dialogSkip(h5JumpBean, WebBannerActivity.this, rlMain);
                }
            });
            Log.e("data", data);
        }

    }

    /**
     * 调用系统已有程序发短信功能
     *
     * @param phoneNumber
     */
    public void doSendSMSTo(String phoneNumber) {
        String[] smsInfo = phoneNumber.split("[$]");
        if (PhoneNumberUtils.isGlobalPhoneNumber(smsInfo[0])) {
            Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + smsInfo[0]));
            intent.putExtra("sms_body", smsInfo[1]);
            startActivity(intent);
        }
    }

    /**
     * 调用打电话业务
     *
     * @param phoneNum
     */
    private void call(final String phoneNum) {
        WebBannerActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 调用系统打电话
                 */
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                if (ActivityCompat.checkSelfPermission(WebBannerActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
    }

    /**
     * 调用本机地图弹窗
     */
    private CommonPopupWindow popupWindow;

    public void showToMap() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(WebBannerActivity.this).inflate(R.layout.share_map_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(WebBannerActivity.this)
                .setView(R.layout.share_map_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style_bottom)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.share_map_popupwindow:
                TextView tv_tencent = view.findViewById(R.id.tv_tencent);
                TextView tv_baidu = view.findViewById(R.id.tv_baidu);
                TextView tv_gaode = view.findViewById(R.id.tv_gaode);
                TextView tv_cancel = view.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });
                if (JumpTomap.isAvilible(WebBannerActivity.this, "com.tencent.map")) {
                    //腾讯地图
                    tv_tencent.setVisibility(View.VISIBLE);
                    tv_tencent.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToTencent(WebBannerActivity.this, Name, Latitude, Longitude);
                        }
                    });
                } else {
                    tv_tencent.setVisibility(View.GONE);
                }
                if (JumpTomap.isAvilible(WebBannerActivity.this, "com.baidu.BaiduMap")) {
                    //百度地图
                    tv_baidu.setVisibility(View.VISIBLE);
                    tv_baidu.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToBaidu(WebBannerActivity.this, Name);
                        }
                    });
                } else {
                    tv_baidu.setVisibility(View.GONE);
                }
                if (JumpTomap.isAvilible(WebBannerActivity.this, "com.autonavi.minimap")) {
                    //高德地图
                    tv_gaode.setVisibility(View.VISIBLE);
                    tv_gaode.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToGaode(WebBannerActivity.this, Name);
                        }
                    });
                } else {
                    tv_gaode.setVisibility(View.GONE);
                }
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        new CircleDialog.Builder(this)
                .setTitle("提示")
                .setText("您好，未在您的手机中检测到主流地图类App，安装主流地图App后，将打开地图自动导航至目的地")
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative("确定", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

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
                            if (wvBanner.canGoBack()) {
                                wvBanner.goBack();
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
                            wvBanner.loadUrl(returnDialogBean.getBtns().get(0).getH5Url());
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
                            if (wvBanner.canGoBack()) {
                                wvBanner.goBack();
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
                            wvBanner.loadUrl(returnDialogBean.getBtns().get(1).getH5Url());
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


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空所有Cookie
        CookieSyncManager.createInstance(MyApplication.getAppContext());  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        wvBanner.setWebChromeClient(null);
        wvBanner.setWebViewClient(null);
        wvBanner.getSettings().setJavaScriptEnabled(false);
        wvBanner.clearCache(true);
    }


}
