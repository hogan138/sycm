package com.shuyun.qapp.ui.webview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AuthHeader;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.JumpTomap;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.ScannerUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_web_prize;
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
                        showSharedPop();
                    }
                    break;
                default:
                    break;
            }
        }
    };

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

    /**
     * 分享弹窗
     */

    /**
     * 1:微信朋友圈
     * 2:微信好友
     */
    private static int SHARE_CHANNEL;
    private static final int SHARE_SECCEED = 1;//分享成功
    private static final int SHARE_FAILURE = 2;//分享失败

    public void showSharedPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(WebPrizeActivity.this).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(WebPrizeActivity.this)
                .setView(R.layout.share_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.AnimUp)//设置动画
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
            case R.layout.share_popupwindow:
                final LinearLayout ll_share = view.findViewById(R.id.ll_share);
                SpringAnimation signUpBtnAnimY = new SpringAnimation(ll_share, SpringAnimation.TRANSLATION_Y, 0);
                signUpBtnAnimY.getSpring().setStiffness(SpringForce.STIFFNESS_VERY_LOW);
                signUpBtnAnimY.getSpring().setDampingRatio(SpringForce.DAMPING_RATIO_LOW_BOUNCY);
                signUpBtnAnimY.setStartVelocity(800);
                signUpBtnAnimY.start();

                ImageView ivWeChat = view.findViewById(R.id.iv_wechat);
                ImageView ivFriends = view.findViewById(R.id.iv_friends);
                ImageView ivqr = view.findViewById(R.id.iv_qr);


                ivWeChat.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN;
                        loadInviteShared(SHARE_CHANNEL);
                    }
                });
                ivFriends.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN_CIRCLE;
                        loadInviteShared(SHARE_CHANNEL);
                    }
                });
                ivqr.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        //二维码分享
                        loadInviteShared(AppConst.SHARE_MEDIA_QR);//二维码分享
                    }
                });
                TextView tv_cancel1 = view.findViewById(R.id.tv_cancel);
                tv_cancel1.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        popupWindow.dismiss();
                    }
                });
                break;
            case R.layout.share_qr_popupwindow:
                final LinearLayout ll_view = view.findViewById(R.id.ll_view);
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setText(sharedBean1.getTitle());
                TextView tv_content = view.findViewById(R.id.tv_content);
                tv_content.setText(sharedBean1.getContent());
                final ImageView iv_qr = view.findViewById(R.id.iv_qr);
                Bitmap mBitmap = CodeUtils.createImage(sharedBean1.getUrl(), 100, 100, null);
                iv_qr.setImageBitmap(mBitmap);
                TextView tv_save_picture = view.findViewById(R.id.tv_save_picture);
                tv_save_picture.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        //保存二维码
                        ScannerUtils.saveImageToGallery(getApplicationContext(), createViewBitmap(ll_view), ScannerUtils.ScannerType.MEDIA);
                        popupWindow.dismiss();
                    }
                });
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

    /**
     * 邀请分享
     */
    SharedBean sharedBean1;

    private void loadInviteShared(final int channel) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.inviteShared(channel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<SharedBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<SharedBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            SharedBean sharedBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                                if (channel == 3) {
                                    sharedBean1 = dataResponse.getDat();
                                    //显示二维码弹框
                                    showQr();
                                } else {
                                    wechatShare(sharedBean);
                                }
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(WebPrizeActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    //创建bitmap
    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }


    /**
     * 分享二维码
     */
    public void showQr() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.share_qr_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.share_qr_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAtLocation(rlMain, Gravity.CENTER, 0, 0);
    }

    /**
     * 微信分享
     */
    private void wechatShare(final SharedBean sharedBean) {
        SHARE_MEDIA share_media = SHARE_MEDIA.WEIXIN;
        /**
         * 1:微信朋友圈
         * 2:微信好友
         */
        if (SHARE_CHANNEL == 1) {
            share_media = SHARE_MEDIA.WEIXIN_CIRCLE;
        } else if (SHARE_CHANNEL == 2) {
            share_media = SHARE_MEDIA.WEIXIN;
        }
        UMImage image = new UMImage(this, R.mipmap.logo);//网络图片
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb(sharedBean.getUrl());//默认链接AppConst.CONTACT_US
        web.setTitle(sharedBean.getTitle());//标题
        web.setThumb(image);
        web.setDescription(sharedBean.getContent());//描述

        new ShareAction(this)
                .setPlatform(share_media)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    /**
                     * @param share_media 平台类型
                     * @descrption 分享开始的回调
                     */
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    /**
                     * @param share_media 平台类型
                     * @descrption 分享成功的回调
                     */
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        loadSharedSure(sharedBean.getId(), SHARE_SECCEED, SHARE_CHANNEL);
                    }

                    /**
                     * @param share_media 平台类型
                     * @param throwable   错误原因
                     * @descrption 分享失败的回调
                     */
                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        loadSharedSure(sharedBean.getId(), SHARE_FAILURE, SHARE_CHANNEL);
                    }

                    /**
                     * @param share_media 平台类型
                     * @descrption 分享取消的回调
                     */
                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                    }
                }).share();
    }

    /**
     * 分享确认
     *
     * @param id      分享id
     * @param result  分享结果1:分享成功;2:分享失败
     * @param channel 1:微信朋友圈 2:微信好友
     */
    private void loadSharedSure(int id, int result, int channel) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.sharedConfirm(id, result, channel)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse dataResponse) {
                        if (dataResponse.isSuccees()) {

                        } else {
                            ErrorCodeTools.errorCodePrompt(WebPrizeActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
