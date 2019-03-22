package com.shuyun.qapp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.AnswerOpptyBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.JsInterationUtil;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.ScannerUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

/**
 * @ProjectName: 全民共进
 * @Package: com.shuyun.qapp.view
 * @ClassName: AnswerSharePopupUtil
 * @Description: 答题分享
 * @Author: ganquan
 * @CreateDate: 2019/3/22 10:45
 */
public class AnswerSharePopupUtil {

    private static CommonPopupWindow commonPopupWindow;
    private static int SHARE_CHANNEL;// 1.微信朋友圈 2.微信好友


    /**
     * 分享弹窗
     */
    public static void showSharedPop(final Context mContext, View view, final Long groupId) {
        if ((!EncodeAndStringTool.isObjectEmpty(commonPopupWindow)) && commonPopupWindow.isShowing())
            return;
        View upView = LayoutInflater.from(mContext).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //取值范围0.0f-1.0f 值越小越暗
//        .setAnimationStyle(R.style.AnimUp)//设置动画
        //设置子View点击事件
        commonPopupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.share_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(final View view, int layoutResId) {
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
                                        if (EncodeAndStringTool.isStringEmpty(JsInterationUtil.wAnswerId)) {
                                            loadGroupShared(SHARE_CHANNEL, groupId, mContext, view);
                                        } else {
                                            loadAnswerShared(SHARE_CHANNEL, JsInterationUtil.wAnswerId, mContext, view);
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
                                        if (EncodeAndStringTool.isStringEmpty(JsInterationUtil.wAnswerId)) {
                                            loadGroupShared(SHARE_CHANNEL, groupId, mContext, view);
                                        } else {
                                            loadAnswerShared(SHARE_CHANNEL, JsInterationUtil.wAnswerId, mContext, view);
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
                                        if (EncodeAndStringTool.isStringEmpty(JsInterationUtil.wAnswerId)) {
                                            loadGroupShared(AppConst.SHARE_MEDIA_QR, groupId, mContext, view);
                                        } else {
                                            loadAnswerShared(AppConst.SHARE_MEDIA_QR, JsInterationUtil.wAnswerId, mContext, view);
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
                        }
                    }
                })
                .create();

        commonPopupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }


    /**
     * 题组分享
     */
    private static SharedBean sharedBean1;

    private static void loadGroupShared(final int channel, Long groupId, final Context context, final View view) {
        RemotingEx.doRequest("groupShared", ApiServiceBean.groupShared(), new Object[]{channel, groupId}, new OnRemotingCallBackListener<SharedBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<SharedBean> dataResponse) {
                if (dataResponse.isSuccees()) {
                    SharedBean sharedBean = (SharedBean) dataResponse.getDat();
                    if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                        if (channel == 3) {
                            sharedBean1 = sharedBean;
                            //显示二维码弹框
                            showQr(context, view);
                        } else {
                            wechatShare(context, sharedBean);
                        }
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(context, dataResponse.getErr(), dataResponse.getMsg());
                }
            }

        });
    }

    private static void loadAnswerShared(final int channel, String id, final Context context, final View view) {
        RemotingEx.doRequest("answerShared", ApiServiceBean.answerShared(), new Object[]{channel, id}, new OnRemotingCallBackListener<SharedBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<SharedBean> dataResponse) {
                if (dataResponse.isSuccees()) {
                    SharedBean sharedBean = (SharedBean) dataResponse.getDat();
                    if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                        if (channel == 3) {
                            sharedBean1 = sharedBean;
                            //显示二维码弹框
                            showQr(context, view);
                        } else {
                            wechatShare(context, sharedBean);
                        }
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(context, dataResponse.getErr(), dataResponse.getMsg());
                }
            }

        });
    }


    private static TextView tvRemainderTime;
    private static Button btnGetImmedicate;
    private static ImageView add_answernum_logo;

    /**
     * 分享二维码
     */
    public static void showQr(final Context context, View view) {
        if (commonPopupWindow != null && commonPopupWindow.isShowing()) return;
        View upView = LayoutInflater.from(context).inflate(R.layout.share_qr_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.share_qr_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
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
                                        ScannerUtils.saveImageToGallery(context, createViewBitmap(ll_view), ScannerUtils.ScannerType.MEDIA);
                                        commonPopupWindow.dismiss();
                                        commonPopupWindow = null;
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create();
        commonPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }

    /**
     * 答题机会领取剩余时长TODO
     * U0004  答题机会已到上限
     */
    private static String remainderTime;

    private static void loadAnswerOpptyRemainder(final Context context) {
        RemotingEx.doRequest("getAnswerOpptyRemainder", ApiServiceBean.getAnswerOpptyRemainder(), null, new OnRemotingCallBackListener<String>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<String> dataResponse) {
                if (dataResponse.isSuccees()) {
                    remainderTime = (String) dataResponse.getDat();
                    if (!EncodeAndStringTool.isStringEmpty(remainderTime)) {
                        if (remainderTime.equals("0")) {
                            btnGetImmedicate.setEnabled(true);
                            add_answernum_logo.setBackgroundResource(R.mipmap.new_add_answernum_s);
                        } else {
                            btnGetImmedicate.setEnabled(false);
                            add_answernum_logo.setBackgroundResource(R.mipmap.new_add_answernum_n);
                            long time = Long.parseLong(remainderTime);
                            countDown(time);
                        }
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(context, dataResponse.getErr(), dataResponse.getMsg());
                }
            }

        });
    }

    private static CountDownTimer timer;

    /**
     * 领取答题机会倒计时
     *
     * @param remainderTime
     */
    private static void countDown(long remainderTime) {
        if (timer != null)
            timer.cancel();
        timer = new CountDownTimer(remainderTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);//初始化Formatter的转换格式。
                formatter.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
                String hms = formatter.format(millisUntilFinished);
                String time = "需等待" + hms + "后获取次数";
                tvRemainderTime.setText(time);
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }


    /**
     * 微信分享
     *
     * @param sharedBean
     */
    private static void wechatShare(final Context context, final SharedBean sharedBean) {
        SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
        if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN) {//WEI
            media = SHARE_MEDIA.WEIXIN;
        } else if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN_CIRCLE) {
            media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        UMImage image = new UMImage(context, R.mipmap.logo);//网络图片
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb(sharedBean.getUrl());//默认链接AppConst.CONTACT_US
        web.setTitle(sharedBean.getTitle());//标题
        web.setThumb(image);//缩略图
        web.setDescription(sharedBean.getContent());//描述
        new ShareAction((Activity) context)
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
                        loadSharedSure(sharedBean.getId(), 1, SHARE_CHANNEL, context);
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
                        loadSharedSure(sharedBean.getId(), 2, SHARE_CHANNEL, context);
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
    private static void loadSharedSure(Long id, int result, int channel, final Context context) {
        RemotingEx.doRequest("sharedConfirm", ApiServiceBean.sharedConfirm(), new Object[]{id, result, channel}, new OnRemotingCallBackListener<Object>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<Object> dataResponse) {
                if (dataResponse.isSuccees()) {

                } else {
                    ErrorCodeTools.errorCodePrompt(context, dataResponse.getErr(), dataResponse.getMsg());
                }

            }
        });
    }

    //创建bitmap
    public static Bitmap createViewBitmap(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        return bitmap;
    }


    /**
     * 增加答题次数弹窗
     */
    public static void showAddAnswerNum(final Context context, View view, final WebView webView) {
        if (commonPopupWindow != null && commonPopupWindow.isShowing()) return;
        View upView = LayoutInflater.from(context).inflate(R.layout.add_answer_num_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        commonPopupWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.add_answer_num_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(new CommonPopupWindow.ViewInterface() {
                    @Override
                    public void getChildView(View view, int layoutResId) {
                        switch (layoutResId) {
                            case R.layout.add_answer_num_popupwindow:
                                ImageView ivClose0 = view.findViewById(R.id.iv_close_icon0);
                                add_answernum_logo = view.findViewById(R.id.iv_logo);
                                btnGetImmedicate = view.findViewById(R.id.btn_get_immedicate);
                                tvRemainderTime = view.findViewById(R.id.tv_remainder_time);
                                loadAnswerOpptyRemainder(context);
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
                                            loadAnswerOppty(context, webView);
                                        } else {
                                            Toast.makeText(context, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                                break;
                            default:
                                break;
                        }
                    }
                })
                .create();

        commonPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

    }

    /**
     * 领取答题机会
     * U0005
     */
    private static AnswerOpptyBean answerOpptyBean;

    private static void loadAnswerOppty(final Context context, final WebView wvAnswerHome) {
        RemotingEx.doRequest("getAnswerOppty", ApiServiceBean.getAnswerOppty(), null, new OnRemotingCallBackListener<Object>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<Object> dataResponse) {
                if (dataResponse.isSuccees()) {
                    answerOpptyBean = (AnswerOpptyBean) dataResponse.getDat();
                    if (!EncodeAndStringTool.isObjectEmpty(answerOpptyBean)) {
                        btnGetImmedicate.setEnabled(false);
                        add_answernum_logo.setBackgroundResource(R.mipmap.new_add_answernum_n);
                        /**
                         * 增加答题次数之后重新请求并刷新数据,让用户可以答题TODO 调用H5页面
                         */
                        wvAnswerHome.post(new Runnable() {
                            @Override
                            public void run() {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                    wvAnswerHome.evaluateJavascript("javascript:addAnswer();", null);
                                }
                            }
                        });

                        countDown(answerOpptyBean.getRemainder());
                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(context, dataResponse.getErr(), dataResponse.getMsg());
                }

            }
        });
    }

}
