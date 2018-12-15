package com.shuyun.qapp.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.shuyun.qapp.R;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.SharePublicBean;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.ScannerUtils;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import static com.blankj.utilcode.util.SizeUtils.dp2px;
import static com.umeng.socialize.net.dplus.CommonNetImpl.TAG;

/**
 * 分享
 */

public class SharePopupUtil {

    private static CommonPopupWindow popupWindow;
    private static int SHARE_CHANNEL;// 1.微信朋友圈 2.微信好友

    /**
     * 邀请分享弹窗
     */
    public static void showSharedPop(final String config, final Context context, View view) {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(context).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(context)
                .setView(R.layout.share_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
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
                                ivWeChat.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                                            popupWindow.dismiss();
                                        }
                                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN;
                                        loadShared(config, SHARE_CHANNEL, context, view);//微信分享
                                    }
                                });
                                ivFriends.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                                            popupWindow.dismiss();
                                        }
                                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN_CIRCLE;
                                        loadShared(config, AppConst.SHARE_MEDIA_WEIXIN_CIRCLE, context, view);//微信朋友圈分享
                                    }
                                });
                                ivqr.setOnClickListener(new OnMultiClickListener() {
                                    @Override
                                    public void onMultiClick(View v) {
                                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                                            popupWindow.dismiss();
                                        }
                                        //二维码分享
                                        loadShared(config, AppConst.SHARE_MEDIA_QR, context, view);//二维码分享
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
                        }
                    }
                })
                .create();

        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 分享
     *
     * @param channl1 1.微信朋友圈分享 2.微信分享;
     */
    static SharedBean sharedBean1;

    private static void loadShared(String config, final int channl, final Context context, final View view) {
        SharePublicBean sharedBean = new Gson().fromJson(config, SharePublicBean.class);
        String action = sharedBean.getAction();
        String id = sharedBean.getId();
        if (!EncodeAndStringTool.isStringEmpty(sharedBean.getId())) {
            RemotingEx.doRequest(ApiServiceBean.SharedPublic(), new Object[]{channl, action, id}, new OnRemotingCallBackListener<SharedBean>() {
                @Override
                public void onCompleted(String action) {

                }

                @Override
                public void onFailed(String action, String message) {

                }

                @Override
                public void onSucceed(String action, DataResponse<SharedBean> dataResponse) {
                    if (dataResponse.isSuccees()) {
                        SharedBean sharedBean = dataResponse.getDat();
                        if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                            if (channl == 3) {
                                sharedBean1 = dataResponse.getDat();
                                //显示二维码弹框
                                showQr(context, view);
                            } else {
                                wechatShare(sharedBean, context);
                            }
                        }
                    } else {
                        ErrorCodeTools.errorCodePrompt(context, dataResponse.getErr(), dataResponse.getMsg());
                    }
                }
            });
        } else {
            RemotingEx.doRequest(ApiServiceBean.SharedPublic1(), new Object[]{channl, action}, new OnRemotingCallBackListener<SharedBean>() {
                @Override
                public void onCompleted(String action) {

                }

                @Override
                public void onFailed(String action, String message) {

                }

                @Override
                public void onSucceed(String action, DataResponse<SharedBean> dataResponse) {
                    if (dataResponse.isSuccees()) {
                        SharedBean sharedBean = dataResponse.getDat();
                        if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                            if (channl == 3) {
                                sharedBean1 = dataResponse.getDat();
                                //显示二维码弹框
                                showQr(context, view);
                            } else {
                                wechatShare(sharedBean, context);
                            }
                        }
                    } else {
                        ErrorCodeTools.errorCodePrompt(context, dataResponse.getErr(), dataResponse.getMsg());
                    }
                }
            });
        }
    }

    /**
     * 分享二维码
     */
    public static void showQr(final Context context, View view) {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(context).inflate(R.layout.share_qr_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(context)
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
                                        popupWindow.dismiss();
                                    }
                                });
                                break;
                        }
                    }
                })
                .create();
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
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
     * 微信分享
     *
     * @param sharedBean
     */
    private static void wechatShare(final SharedBean sharedBean, final Context context) {
        SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
        if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN) {//微信
            media = SHARE_MEDIA.WEIXIN;
        } else if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN_CIRCLE) {//朋友圈
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
                        Log.i(TAG, "onResult: " + share_media);
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
                        Log.e(TAG, "onError:share_media== " + share_media + "throwable =" + throwable);

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
                        Log.i(TAG, "onCancel: " + share_media);
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
        RemotingEx.doRequest(ApiServiceBean.sharedConfirm(), new Object[]{id, result, channel}, new OnRemotingCallBackListener<Object>() {
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
}
