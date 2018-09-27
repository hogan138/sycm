package com.shuyun.qapp.ui.webview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AuthHeader;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.JumpTomap;
import com.shuyun.qapp.utils.OnMultiClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//黄山电子门票、招商银行等h5页面
public class WebPrizeActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {
    @BindView(R.id.wv_prize)
    WebView wvPrize;//奖品WebView
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_rignt)
    TextView tvRignt;
    private String id;
    private String url;
    private static final String TAG = "WebPrizeActivity";

    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();

    //经纬度、名称
    private String Longitude;
    private String Latitude;
    private String Name;

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

        WebSettings settings = wvPrize.getSettings();
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setJavaScriptEnabled(true);
        wvPrize.addJavascriptInterface(new JsInteration(), "android");
        // 允许混合内容 解决部分手机 加载不出https请求里面的http下的图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        /**
         * 拦截HTML页面中的点击事件 让网页在本应用内打开
         */
        wvPrize.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                wvPrize.loadUrl(url);
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                return false;

            }
        });

        wvPrize.loadUrl(url);
//        wvPrize.loadUrl("http://192.168.191.1:8080?id=20180914122828769BkuxwigiN2ddli4&prizeId=1042&scheduleId=0&prizeType=0");
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_web_prize;
    }


    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        //奖品id
        id = intent.getStringExtra("id");
        String name = intent.getStringExtra("name");
        if (!EncodeAndStringTool.isStringEmpty(name)) {
            tvCommonTitle.setText(name);
        }
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (wvPrize.canGoBack()) {
                    wvPrize.goBack();
                } else {
                    super.onBackPressed();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (wvPrize.canGoBack()) {
            wvPrize.goBack();
        } else {
            super.onBackPressed();
        }
    }

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
            String header = new Gson().toJson(authHeader);
            return header.toString();
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
         * h5页面调用网络出现错误码
         */
        @JavascriptInterface
        public void newLoginDate(final String err, final String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ErrorCodeTools.errorCodePrompt(WebPrizeActivity.this, err, msg);
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
                    if (JumpTomap.isAvilible(WebPrizeActivity.this, "com.tencent.map")
                            || JumpTomap.isAvilible(WebPrizeActivity.this, "com.baidu.BaiduMap")
                            || JumpTomap.isAvilible(WebPrizeActivity.this, "com.autonavi.minimap")) {
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
                answerHome = new Gson().toJson(answerHomeBean);
            }
            Log.e(TAG, answerHome.toString());
            return answerHome.toString();
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
                        tvRignt.setVisibility(View.GONE);
                    } else if (type.equals("1")) {
                        //显示
                        tvRignt.setVisibility(View.VISIBLE);
                        tvRignt.setText(name);
                        tvRignt.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                wvPrize.loadUrl("javascript:" + funname + "()");
                            }
                        });
                    }
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
                    Intent intent = new Intent(WebPrizeActivity.this, HomePageActivity.class);
                    intent.putExtra("from", "h5");
                    startActivity(intent);
                }
            });
        }

    }

    /**
     * 调用打电话业务
     *
     * @param phoneNum
     */
    private void call(final String phoneNum) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 调用系统打电话
                 */
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                if (ActivityCompat.checkSelfPermission(WebPrizeActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        View upView = LayoutInflater.from(WebPrizeActivity.this).inflate(R.layout.share_map_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(WebPrizeActivity.this)
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
                if (JumpTomap.isAvilible(WebPrizeActivity.this, "com.tencent.map")) {
                    //腾讯地图
                    tv_tencent.setVisibility(View.VISIBLE);
                    tv_tencent.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToTencent(WebPrizeActivity.this, Name, Latitude, Longitude);
                        }
                    });
                } else {
                    tv_tencent.setVisibility(View.GONE);
                }
                if (JumpTomap.isAvilible(WebPrizeActivity.this, "com.baidu.BaiduMap")) {
                    //百度地图
                    tv_baidu.setVisibility(View.VISIBLE);
                    tv_baidu.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToBaidu(WebPrizeActivity.this, Name);
                        }
                    });
                } else {
                    tv_baidu.setVisibility(View.GONE);
                }
                if (JumpTomap.isAvilible(WebPrizeActivity.this, "com.autonavi.minimap")) {
                    //高德地图
                    tv_gaode.setVisibility(View.VISIBLE);
                    tv_gaode.setOnClickListener(new OnMultiClickListener() {
                        @Override
                        public void onMultiClick(View v) {
                            JumpTomap.goToGaode(WebPrizeActivity.this, Name);
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //清空所有Cookie
        CookieSyncManager.createInstance(MyApplication.getAppContext());  //Create a singleton CookieSyncManager within a context
        CookieManager cookieManager = CookieManager.getInstance(); // the singleton CookieManager instance
        cookieManager.removeAllCookie();// Removes all cookies.
        CookieSyncManager.getInstance().sync(); // forces sync manager to sync now

        wvPrize.setWebChromeClient(null);
        wvPrize.setWebViewClient(null);
        wvPrize.getSettings().setJavaScriptEnabled(false);
        wvPrize.clearCache(true);
    }
}
