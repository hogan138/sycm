package com.shuyun.qapp.ui.against;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.net.SykscApplication;
import com.shuyun.qapp.ui.answer.AnswerHistoryActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ScannerUtils;
import com.shuyun.qapp.utils.UmengPageUtil;
import com.shuyun.qapp.view.CircleImageView;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

/**
 * 答题对战结果页
 */

public class AgainstResultActivity extends BaseActivity implements View.OnClickListener, CommonPopupWindow.ViewInterface, OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_left_icon)
    ImageView ivLeftIcon;
    @BindView(R.id.iv_common_left_icon)
    RelativeLayout ivCommonLeftIcon;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //标题文字
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.iv_status)
    ImageView ivStatus;
    @BindView(R.id.iv_head_mine)
    CircleImageView ivHeadMine;
    @BindView(R.id.tv_mine_phone)
    TextView tvMinePhone;
    @BindView(R.id.iv_head_it)
    CircleImageView ivHeadIt;
    @BindView(R.id.tv_it_phone)
    TextView tvItPhone;
    @BindView(R.id.rl_again)
    RelativeLayout rlAgain;
    @BindView(R.id.btn_look_fail)
    Button btnLookFail;
    @BindView(R.id.tv_mine_score)
    TextView tvMineScore;
    @BindView(R.id.tv_it_score)
    TextView tvItScore;
    @BindView(R.id.tv_reduce_score)
    TextView tvReduceScore;
    @BindView(R.id.rl_against_result)
    RelativeLayout rlAgainstResult;

    //头像图标
    private int[] icon = new int[]{R.mipmap.header02, R.mipmap.header03, R.mipmap.header04,
            R.mipmap.header05, R.mipmap.header06, R.mipmap.header07, R.mipmap.header08, R.mipmap.header09};


    @Override
    public int intiLayout() {
        return R.layout.activity_against_result;
    }

    int type;

    private String answerId;//答题id

    //友盟统计场次类型
    String type_name = "";

    //答题对战进入标记
    String umeng_from = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //标题
        type = getIntent().getIntExtra("type", 0);
        tvCommonTitle.setText(getIntent().getStringExtra("name"));

        int my_score = getIntent().getIntExtra("my_score", 0);
        int it_score = getIntent().getIntExtra("it_score", 0);

        tvMineScore.setText(my_score + "");
        tvItScore.setText(it_score + "");

        if (my_score > it_score) {
            //胜利
            ivStatus.setImageResource(R.mipmap.against_success);
            /**
             * 答题对战结束
             */
            loadCompleteAnswAgainst(0);
        } else if (my_score == it_score) {
            //平局
            ivStatus.setImageResource(R.mipmap.against_hand);
            /**
             * 答题对战结束
             */
            loadCompleteAnswAgainst(1);
        } else if (my_score < it_score) {
            //失败
            ivStatus.setImageResource(R.mipmap.against_fail);
        }


        //头像手机号
        ivHeadMine.setImageResource(icon[getIntent().getIntExtra("my_head", 0) - 1]);
        tvMinePhone.setText(getIntent().getStringExtra("my_phone"));
        ivHeadIt.setImageResource(icon[getIntent().getIntExtra("it_head", 0)]);//- 1
        tvItPhone.setText(getIntent().getStringExtra("it_phone"));

        //积分消耗
        if (getIntent().getStringExtra("score").equals("")) {
            tvReduceScore.setText("不消耗积分");
        } else {
            tvReduceScore.setText("消耗积分：" + getIntent().getStringExtra("score"));
        }
        answerId = getIntent().getStringExtra("answer_id");

        ivCommonLeftIcon.setOnClickListener(this);
        ivLeftIcon.setImageResource(R.mipmap.backb);//左侧返回
        rlAgain.setOnClickListener(this);
        btnLookFail.setOnClickListener(this);

        ivRightIcon.setOnClickListener(this);
        /**
         * 检测微信是否安装,如果没有安装,需不显示分享按钮;如果安装了微信则显示分享按钮.
         */
        if (!SykscApplication.mWxApi.isWXAppInstalled()) {
            ivRightIcon.setVisibility(View.GONE);
        } else {
            ivRightIcon.setVisibility(View.VISIBLE);
            ivRightIcon.setImageResource(R.mipmap.share);//右侧分享
        }

        //记录答题对战首页标记
        umeng_from = SaveUserInfo.getInstance(this).getUserInfo("umeng_from");

        //友盟统计type
        if (type == 0) {
            type_name = "free";
        } else if (type == 1) {
            type_name = "novice";
        } else if (type == 2) {
            type_name = "intermediate";
        } else if (type == 3) {
            type_name = "advanced";
        }

    }


    //友盟页面统计
    private void startPage(String type) {

        if (!EncodeAndStringTool.isStringEmpty(umeng_from) && umeng_from.equals("home")) {
            UmengPageUtil.startPage("app_home_battle_" + type + "_again");
        } else if (!EncodeAndStringTool.isStringEmpty(umeng_from) && umeng_from.equals("found")) {
            UmengPageUtil.startPage("app_found_battle_" + type + "_again");
        }
    }


    /**
     * 答题对战结束
     */
    private void loadCompleteAnswAgainst(int isWin) {

        RemotingEx.doRequest(AppConst.AGAINST_RESULT, ApiServiceBean.completeAnswAgainst(), new Object[]{type, isWin}, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_common_left_icon: //返回键
                if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
                    if (popupWindow.isShowing()) {
                        popupWindow.dismiss();
                    } else {
                        finish();
                    }
                    popupWindow = null;
                } else {
                    finish();
                }
                break;
            case R.id.iv_right_icon: //分享
                showSharedPop();
                break;
            case R.id.rl_again://再来一局
                //友盟统计
                startPage(type_name);

                finish();
                break;
            case R.id.btn_look_fail: //查看错题
                Intent intent = new Intent(this, AnswerHistoryActivity.class);
                intent.putExtra("answer_id", answerId);
                intent.putExtra("title", tvCommonTitle.getText().toString());
                intent.putExtra("from", "answer");
                startActivity(intent);
                break;
            default:
                break;
        }
    }


    CommonPopupWindow popupWindow;


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
                ivWeChat.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN;
                        loadBattleAnswerShared(SHARE_CHANNEL);//微信分享
                    }
                });
                ivFriends.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN_CIRCLE;
                        loadBattleAnswerShared(AppConst.SHARE_MEDIA_WEIXIN_CIRCLE);//微信朋友圈分享
                    }
                });
                ivqr.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        //二维码分享
                        loadBattleAnswerShared(AppConst.SHARE_MEDIA_QR);//二维码分享
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
                        ScannerUtils.saveImageToGallery(AgainstResultActivity.this, createViewBitmap(ll_view), ScannerUtils.ScannerType.MEDIA);
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
     * 分享弹窗
     */
    public void showSharedPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(this).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(this)
                .setView(R.layout.share_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.AnimUp)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(rlAgainstResult, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 分享二维码弹窗
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
        popupWindow.showAtLocation(rlAgainstResult, Gravity.CENTER, 0, 0);
    }

    /**
     * 分享
     *
     * @param channl 分享渠道id 1:微信分享;2 微信朋友圈分享 3:二维码分享
     */
    SharedBean sharedBean1;
    private static int share_style = 0;

    private void loadBattleAnswerShared(final int channl) {

        share_style = channl;
        RemotingEx.doRequest(AppConst.AGAINST_RESULT_SHARE, ApiServiceBean.battleAnswerShared1(), new Object[]{channl, type}, this);

    }

    /**
     * 微信分享
     *
     * @param sharedBean
     */
    private void wechatShare(final SharedBean sharedBean) {//String url, String content, String title  .getUrl(), sharedBean.getContent(), sharedBean.getTitle()
        SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
        if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN) {//微信
            media = SHARE_MEDIA.WEIXIN;
        } else if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN_CIRCLE) {//朋友圈
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
    private void loadSharedSure(Long id, int result, int channel) {

        RemotingEx.doRequest(AppConst.AGAINST_SHARE_CONFIM, ApiServiceBean.sharedConfirm(), new Object[]{id, result, channel}, this);

    }

    @Override
    public void onBackPressed() {
        if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                super.onBackPressed();
            }
            popupWindow = null;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> dataResponse) {
        if (AppConst.AGAINST_RESULT.equals(action)) { //对战结果
            if (dataResponse.isSuccees()) {
                if (type == 0) { //自由对战不提示积分情况
                } else {
                    Toast.makeText(AgainstResultActivity.this, "积分 +" + dataResponse.getDat(), Toast.LENGTH_SHORT).show();
                }
            } else {//错误码提示
                ErrorCodeTools.errorCodePrompt(AgainstResultActivity.this, dataResponse.getErr(), dataResponse.getMsg());
            }
        } else if (AppConst.AGAINST_RESULT_SHARE.equals(action)) {
            if (dataResponse.isSuccees()) {
                SharedBean sharedBean = (SharedBean) dataResponse.getDat();
                if (!EncodeAndStringTool.isObjectEmpty(sharedBean)) {
                    if (share_style == 3) {
                        sharedBean1 = (SharedBean) dataResponse.getDat();
                        //显示二维码弹框
                        showQr();
                    } else {
                        wechatShare(sharedBean);
                    }
                }
            } else {
                ErrorCodeTools.errorCodePrompt(AgainstResultActivity.this, dataResponse.getErr(), dataResponse.getMsg());
            }
        } else if (AppConst.AGAINST_SHARE_CONFIM.equals(action)) {//分享确认

        }

    }
}
