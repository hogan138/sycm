package com.shuyun.qapp.ui.webview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Parcelable;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.text.Html;
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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.ishumei.smantifraud.SmAntiFraud;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.*;
import com.shuyun.qapp.bean.AnswerOpptyBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.bean.WebAnswerHomeBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.answer.AnswerHistoryActivity;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.ui.mine.IntegralDrawActivity;
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
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import com.shuyun.qapp.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

/**
 * Created by sunxiao on 2018/7/5
 * H5答题页
 */
public class WebAnswerActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.iv_common_left_icon)
    RelativeLayout ivCommonLeftIcon;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.wv_answer_home)
    WebView wvAnswerHome;
    WebAnswerHomeBean answerHomeBean = new WebAnswerHomeBean();
    @BindView(R.id.ll_h5)
    LinearLayout llH5;
    private int groupId;
    private String h5Url;
    private static final String TAG = "WebAnswerActivity";
    private String splash = "";
    /**
     * 答题Id
     */
    private String wAnswerId = null;
    /**
     * 题组名称
     */
    private String wTitle;
    /**
     * TODO
     * 是否实名认证
     * 0——未实名认证
     * 1——已实名认证
     * 2——审核中
     * 3——未通过
     * 4——拉黑
     */
    int certification;

    class JsInteration {

        private static final String TAG = "JsInteration";

        public JsInteration() {
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
            return answerHome;
        }

        /**
         * 增加答题次数弹窗
         *
         * @param wCertification
         */
        @JavascriptInterface
        public void addNum(int wCertification) {
            certification = wCertification;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showAddAnswerNum();
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
            wAnswerId = id;
            Log.i(TAG, page + "");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (page == 1) {
                        wTitle = title;
                        tvCommonTitle.setText(title);
                        ivRightIcon.setVisibility(View.VISIBLE);
                    } else if (page == 0) {
                        tvCommonTitle.setText(title);
                        ivRightIcon.setVisibility(View.GONE);
                    } else {
                        tvCommonTitle.setText("");
                        ivRightIcon.setVisibility(View.GONE);
                    }
                }
            });
        }

        /**
         * 查看答题历史
         *
         * @param id 答题id
         */
        @JavascriptInterface
        public void answerHistory(final String id) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(WebAnswerActivity.this, AnswerHistoryActivity.class);
                    intent.putExtra("answer_id", id);
                    intent.putExtra("title", wTitle);
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
                    if (!EncodeAndStringTool.isStringEmpty(splash)) {
                        if (splash.equals("splash")) {
                            finish();
                            Intent intent = new Intent(WebAnswerActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        finish();
                    }
                }
            });
        }


        /**
         * JS调用此方法,调用答题分享
         *
         * @param id 答题Id
         */
        @JavascriptInterface
        public void inviterShare(String id) {
            wAnswerId = id;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    showSharedPop();
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
         * h5页面调用网络出现错误码
         */
        @JavascriptInterface
        public void newLoginDate(final String err, final String msg) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ErrorCodeTools.errorCodePrompt(WebAnswerActivity.this, err, msg);
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
                    MinePrize minePrize = JSONObject.parseObject(prizeData.toString(), MinePrize.class);
                    if (minePrize.getActionType().equals("action.h5.url")) {
                        //实物
                        Intent intent = new Intent(WebAnswerActivity.this, WebPrizeActivity.class);
                        intent.putExtra("id", minePrize.getId());
                        intent.putExtra("url", minePrize.getH5Url());
                        intent.putExtra("name", minePrize.getName());
                        startActivity(intent);
                    } else if (minePrize.getActionType().equals("action.withdraw")) {
                        //红包
                        if (Integer.parseInt(SaveUserInfo.getInstance(WebAnswerActivity.this).getUserInfo("cert")) == 1) {
                            if (!EncodeAndStringTool.isListEmpty(minePrize.getMinePrizes())) {
                                List<MinePrize.ChildMinePrize> redPrizeList = new ArrayList<>();
                                for (int i = 0; i < minePrize.getMinePrizes().size(); i++) {
                                    MinePrize.ChildMinePrize childMinePrize1 = minePrize.getMinePrizes().get(i);
                                    if (minePrize.getMinePrizes().get(i).getActionType().equals("action.withdraw")) {
                                        redPrizeList.add(childMinePrize1);
                                    }
                                }
                                if (!EncodeAndStringTool.isListEmpty(redPrizeList)) {
                                    Intent intent = new Intent(WebAnswerActivity.this, RedWithDrawActivity.class);
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
                        startActivity(new Intent(WebAnswerActivity.this, IntegralExchangeActivity.class));
                    }
                }
            });

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        /**
         * 检测微信是否安装,如果没有安装,需不显示分享按钮,如果安装了,需要显示分享按钮
         */
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ivRightIcon.setVisibility(View.GONE);
        } else {
            ivRightIcon.setImageResource(R.mipmap.share);//右侧分享
        }

        Intent intent = getIntent();
        splash = intent.getStringExtra("from");
        groupId = intent.getIntExtra("groupId", 0);
        h5Url = intent.getStringExtra("h5Url");

        //是否参与邀请分享 1——参与邀请
        Integer share = (Integer) SharedPrefrenceTool.get(this, "share", (Integer) 0);
        answerHomeBean.setToken(AppConst.TOKEN);
        answerHomeBean.setRandom(AppConst.RANDOM);
        answerHomeBean.setV(AppConst.V);
        answerHomeBean.setSalt(AppConst.SALT);
        answerHomeBean.setAppSecret(AppConst.APP_KEY);
        answerHomeBean.setGroupId(groupId);
        answerHomeBean.setShare(share);
        String deviceId = SmAntiFraud.getDeviceId();
        answerHomeBean.setDeviceId(deviceId);

        WebSettings settings = wvAnswerHome.getSettings();
        settings.setJavaScriptEnabled(true);
        wvAnswerHome.setWebChromeClient(new WebChromeClient());//解决答题时无法弹出dialog问题.
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvAnswerHome.addJavascriptInterface(new JsInteration(), "android");
        // 允许混合内容 解决部分手机 加载不出https请求里面的http下的图片
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        if (!EncodeAndStringTool.isStringEmpty(h5Url)) {
            wvAnswerHome.loadUrl(h5Url);
//            wvAnswerHome.loadUrl("http://192.168.191.1:8080");
        } else {
            wvAnswerHome.loadUrl(AppConst.ANSWER);
        }

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_web_answer;
    }


    @OnClick({R.id.iv_common_left_icon, R.id.iv_right_icon,})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_common_left_icon:
                if (!EncodeAndStringTool.isObjectEmpty(commonPopupWindow)) {
                    if (commonPopupWindow.isShowing()) {
                        commonPopupWindow.dismiss();
                    }
                    commonPopupWindow = null;
                } else {
                    if (!EncodeAndStringTool.isStringEmpty(splash)) {
                        if (splash.equals("splash")) {
                            if (wvAnswerHome.canGoBack()) {
                                wvAnswerHome.goBack();
                            } else {
                                finish();
                                Intent intent = new Intent(this, HomePageActivity.class);
                                startActivity(intent);
                            }
                        }
                    } else {
                        if (wvAnswerHome.canGoBack()) {
                            wvAnswerHome.goBack();
                        } else {
                            finish();
                        }
                    }
                }
                break;
            case R.id.iv_right_icon:
                showSharedPop();
                break;
            default:
                break;
        }
    }


    CommonPopupWindow commonPopupWindow;

    /**
     * 分享弹窗
     */

    public void showSharedPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(commonPopupWindow)) && commonPopupWindow.isShowing())
            return;
        View upView = LayoutInflater.from(WebAnswerActivity.this).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //取值范围0.0f-1.0f 值越小越暗
//        .setAnimationStyle(R.style.AnimUp)//设置动画
        //设置子View点击事件
        commonPopupWindow = new CommonPopupWindow.Builder(WebAnswerActivity.this)
                .setView(R.layout.share_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        commonPopupWindow.showAtLocation(llH5, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 分享二维码
     */
    public void showQr() {
        if (commonPopupWindow != null && commonPopupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.share_qr_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.share_qr_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();
        commonPopupWindow.showAtLocation(llH5, Gravity.CENTER, 0, 0);
    }

    /**
     * 题组分享
     */
    SharedBean sharedBean1;

    private void loadGroupShared(final int channl, int groupId) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.groupShared(channl, groupId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<SharedBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<SharedBean> dataResponse) {
                        Log.i(TAG, "loadGroupShared==onNext: " + dataResponse.toString());
                        if (dataResponse.isSuccees()) {
                            SharedBean sharedBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                                if (channl == 3) {
                                    sharedBean1 = dataResponse.getDat();
                                    //显示二维码弹框
                                    showQr();
                                } else {
                                    wechatShare(sharedBean);
                                }
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(WebAnswerActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
     * 答题分享
     */

    private void loadAnswerShared(final int channl, String id) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.answerShared(channl, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<SharedBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<SharedBean> dataResponse) {
                        Log.i(TAG, "loadAnswerShared==onNext: " + dataResponse.toString());
                        if (dataResponse.isSuccees()) {
                            SharedBean sharedBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                                if (channl == 3) {
                                    sharedBean1 = dataResponse.getDat();
                                    //显示二维码弹框
                                    showQr();
                                } else {
                                    wechatShare(sharedBean);
                                }
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(WebAnswerActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
     * 微信分享
     *
     * @param sharedBean
     */
    private void wechatShare(final SharedBean sharedBean) {
        SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
        if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN) {//WEI
            media = SHARE_MEDIA.WEIXIN;
        } else if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN_CIRCLE) {
            media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        UMImage image = new UMImage(this, R.mipmap.logo);//网络图片
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb(sharedBean.getUrl());//默认链接AppConst.CONTACT_US
        web.setTitle(sharedBean.getTitle());//标题
        web.setThumb(image);//缩略图
        web.setDescription(sharedBean.getContent());//描述
        new ShareAction(this)
                .setPlatform(media)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    /**
                     * @descrption 分享开始的回调
                     * @param share_media 平台类型
                     */
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    /**
                     * @descrption 分享成功的回调
                     * @param share_media 平台类型
                     */
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        /**
                         * 入参1:分享id;2:分享结果(①分享成功,②分享失败);3:分享渠道(①微信朋友圈②微信好友)
                         */
                        loadSharedSure(sharedBean.getId(), 1, SHARE_CHANNEL);
                    }

                    /**
                     * @descrption 分享失败的回调
                     * @param share_media 平台类型
                     * @param throwable 错误原因
                     */
                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        /**
                         * 入参1:分享id;2:分享结果(①分享成功,②分享失败);3:分享渠道(①微信朋友圈②微信好友)
                         */
                        loadSharedSure(sharedBean.getId(), 2, SHARE_CHANNEL);
                    }

                    /**
                     * @descrption 分享取消的回调
                     * @param share_media 平台类型
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
                        Log.i(TAG, "loadSharedSure==onNext: " + dataResponse.toString());
                        if (dataResponse.isSuccees()) {

                        } else {
                            ErrorCodeTools.errorCodePrompt(WebAnswerActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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

    TextView tvRemainderTime;
    Button btnGetImmedicate;

    /**
     * 增加答题次数弹窗
     */
    private void showAddAnswerNum() {
        if (commonPopupWindow != null && commonPopupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.add_answer_num_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.add_answer_num_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        commonPopupWindow.showAtLocation(llH5, Gravity.CENTER, 0, 0);

    }

    /**
     * 分享渠道
     */
    private int SHARE_CHANNEL;

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
                ivWeChat.setOnClickListener(new View.OnClickListener() {//分享给微信好友
                    @Override
                    public void onClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(commonPopupWindow) && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                            commonPopupWindow = null;
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN;
                        if (EncodeAndStringTool.isStringEmpty(wAnswerId)) {
                            loadGroupShared(SHARE_CHANNEL, groupId);
                        } else {
                            loadAnswerShared(SHARE_CHANNEL, wAnswerId);
                        }
                    }
                });
                ivFriends.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {//分享到朋友圈
                        if (!EncodeAndStringTool.isObjectEmpty(commonPopupWindow) && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                            commonPopupWindow = null;
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN_CIRCLE;
                        if (EncodeAndStringTool.isStringEmpty(wAnswerId)) {
                            loadGroupShared(SHARE_CHANNEL, groupId);
                        } else {
                            loadAnswerShared(SHARE_CHANNEL, wAnswerId);
                        }
                    }
                });
                ivqr.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(commonPopupWindow) && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                            commonPopupWindow = null;
                        }
                        if (EncodeAndStringTool.isStringEmpty(wAnswerId)) {
                            loadGroupShared(AppConst.SHARE_MEDIA_QR, groupId);
                        } else {
                            loadAnswerShared(AppConst.SHARE_MEDIA_QR, wAnswerId);
                        }
                    }
                });
                TextView tv_cancel = view.findViewById(R.id.tv_cancel);
                tv_cancel.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        commonPopupWindow.dismiss();
                        commonPopupWindow = null;
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
                        commonPopupWindow.dismiss();
                        commonPopupWindow = null;
                    }
                });
                break;
            case R.layout.add_answer_num_popupwindow:
                ImageView ivClose0 = view.findViewById(R.id.iv_close_icon0);
                btnGetImmedicate = view.findViewById(R.id.btn_get_immedicate);
                tvRemainderTime = view.findViewById(R.id.tv_remainder_time);
                loadAnswerOpptyRemainder();
                Button btnIntegrationPrize = view.findViewById(R.id.btn_integration_prize);
                ivClose0.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (commonPopupWindow != null && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                        }
                    }
                });
                btnGetImmedicate.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (NetworkUtils.isAvailableByPing()) {
                            loadAnswerOppty();
                        } else {
                            Toast.makeText(WebAnswerActivity.this, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnIntegrationPrize.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (commonPopupWindow != null && commonPopupWindow.isShowing()) {
                            commonPopupWindow.dismiss();
                        }
                        /**TODO
                         * 是否实名认证
                         * 0——未实名认证
                         * 1——已实名认证
                         * 2——审核中
                         * 3——未通过
                         * 4——拉黑
                         */
                        String cert = SaveUserInfo.getInstance(WebAnswerActivity.this).getUserInfo("cert");
                        if (!EncodeAndStringTool.isStringEmpty(cert)) {
                            certification = Integer.parseInt(cert);
                        }
                        if (certification == 1) {//groupDetail.getCertification()
                            Intent intent0 = new Intent(WebAnswerActivity.this, IntegralDrawActivity.class);
                            intent0.putExtra("status", AppConst.INTEGRL_DRAW);//积分抽奖跳到开宝箱界面;
                            startActivity(intent0);
                        } else if (certification == 2) {
                            ToastUtil.showToast(WebAnswerActivity.this, "您已成功提交认证申请..\n预计将在24小时内审核完成");
                        } else {
                            showAuthPop();
                        }
                    }
                });
                break;
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
                        startActivity(new Intent(WebAnswerActivity.this, RealNameAuthActivity.class));
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
     * 答题机会领取剩余时长TODO
     * U0004  答题机会已到上限
     */
    private String remainderTime;

    private void loadAnswerOpptyRemainder() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getAnswerOpptyRemainder()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<String>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<String> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            remainderTime = dataResponse.getDat();
                            if (!EncodeAndStringTool.isStringEmpty(remainderTime)) {
                                if (remainderTime.equals("0")) {
                                    btnGetImmedicate.setEnabled(true);
                                } else {
                                    btnGetImmedicate.setEnabled(false);
                                    long time = Long.parseLong(remainderTime);
                                    countDown(time);
                                }
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(WebAnswerActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                        return;
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    private CountDownTimer timer;

    /**
     * 领取答题机会倒计时
     *
     * @param remainderTime
     */
    private void countDown(long remainderTime) {
        timer = new CountDownTimer(remainderTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");//初始化Formatter的转换格式。
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                String hms = formatter.format(millisUntilFinished);
                String time = "需等待<font color='#227fc5'>" + hms + "</font>后\n可获取一次答题次数";
                tvRemainderTime.setText(Html.fromHtml(time));
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    private AnswerOpptyBean answerOpptyBean;

    /**
     * 领取答题机会
     * U0005
     */
    private void loadAnswerOppty() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getAnswerOppty()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<AnswerOpptyBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AnswerOpptyBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            answerOpptyBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(answerOpptyBean)) {
                                btnGetImmedicate.setEnabled(false);
                                answerOpptyBean.getRemainder();
                                /**
                                 * 增加答题次数之后重新请求并刷新数据,让用户可以答题TODO 调用H5页面
                                 */
                                wvAnswerHome.loadUrl("javascript:addAnswer()");
                                countDown(answerOpptyBean.getRemainder());
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(WebAnswerActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                        return;
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 实名认证popupWindow
     */
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

        commonPopupWindow.showAtLocation(llH5, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onBackPressed() {
        if (!EncodeAndStringTool.isObjectEmpty(commonPopupWindow)) {
            if (commonPopupWindow.isShowing()) {
                commonPopupWindow.dismiss();
            }
            commonPopupWindow = null;
        } else {
            if (!EncodeAndStringTool.isStringEmpty(splash)) {
                if (splash.equals("splash")) {
                    if (wvAnswerHome.canGoBack()) {
                        wvAnswerHome.goBack();
                    } else {
                        super.onBackPressed();
                        Intent intent = new Intent(this, HomePageActivity.class);
                        startActivity(intent);
                    }
                }
            } else {
                if (wvAnswerHome.canGoBack()) {
                    wvAnswerHome.goBack();
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

}
