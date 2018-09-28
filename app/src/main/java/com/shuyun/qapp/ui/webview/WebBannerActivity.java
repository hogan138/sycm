package com.shuyun.qapp.ui.webview;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
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
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.mine.AccountRecordActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
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

import static com.blankj.utilcode.util.SizeUtils.dp2px;

public class WebBannerActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.wv_banner)
    WebView wvBanner;//首页轮播图跳转页面
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    private String url;

    //从广告页进入
    private String splash;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        initData();

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
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
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
                    wvBanner.goBack();
                } else {
                    if (!EncodeAndStringTool.isStringEmpty(splash)) {
                        if (splash.equals("splash")) {
                            finish();
                            Intent intent = new Intent(WebBannerActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
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
        if (wvBanner.canGoBack()) {
            wvBanner.goBack();
        } else {
            if (!EncodeAndStringTool.isStringEmpty(splash)) {
                if (splash.equals("splash")) {
                    finish();
                    Intent intent = new Intent(WebBannerActivity.this, HomePageActivity.class);
                    startActivity(intent);
                }
            } else {
                finish();
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
                        showSharedPop();
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
         * 提现成功，进入我的账户
         */
        @JavascriptInterface
        public void goWebview(final String h5url) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(WebBannerActivity.this, SharedPrzieActivity.class);
                    intent.putExtra("h5Url", h5url);
                    startActivity(intent);
                }
            });
        }


        /**
         * 提现时败，进入我的账户
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

    private CommonPopupWindow popupWindow;

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
        View upView = LayoutInflater.from(WebBannerActivity.this).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(WebBannerActivity.this)
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
                TextView tv_cancel = view.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new OnMultiClickListener() {
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
                Bitmap mBitmap = CodeUtils.createImage(sharedBean1.getUrl(), dp2px(114), dp2px(114), null);
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

    //创建bitmap
    public Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
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
                            ErrorCodeTools.errorCodePrompt(WebBannerActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
                            ErrorCodeTools.errorCodePrompt(WebBannerActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
