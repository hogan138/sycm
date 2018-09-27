package com.shuyun.qapp.ui.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.BoxBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.mine.RealNameAuthActivity;
import com.shuyun.qapp.ui.mine.RedWithDrawActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ScannerUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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

    /**
     * 1:微信朋友圈
     * 2:微信好友
     */
    private static int SHARE_CHANNEL;
    private static final int SHARE_SECCEED = 1;//分享成功
    private static final int SHARE_FAILURE = 2;//分享失败
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

    @Override
    public void onBackPressed() {
        if (wvPrizeBox.canGoBack()) {
            wvPrizeBox.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private CommonPopupWindow popupWindow;

    /**
     * 分享弹窗
     */
    public void showSharedPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(WebPrizeBoxActivity.this).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(WebPrizeBoxActivity.this)
                .setView(R.layout.share_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.AnimUp)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(llWebBox, Gravity.BOTTOM, 0, 0);
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
                            ErrorCodeTools.errorCodePrompt(WebPrizeBoxActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
        popupWindow.showAtLocation(llWebBox, Gravity.CENTER, 0, 0);
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
                            ErrorCodeTools.errorCodePrompt(WebPrizeBoxActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
