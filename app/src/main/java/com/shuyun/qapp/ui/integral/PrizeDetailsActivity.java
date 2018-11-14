package com.shuyun.qapp.ui.integral;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyPagerAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.PrizeDetailBean;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.webview.WebDetailFragment;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.CustomLoadingFactory;
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
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.blankj.utilcode.util.SizeUtils.dp2px;

/**
 * 积分夺宝----宝贝详情
 */
public class PrizeDetailsActivity extends BaseActivity implements View.OnClickListener, CommonPopupWindow.ViewInterface {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_reduce_score)
    TextView tvReduceScore;
    @BindView(R.id.ll_exchange)
    LinearLayout llExchange;
    @BindView(R.id.ll_look_result)
    LinearLayout llLookResult;
    @BindView(R.id.rl_exchange)
    RelativeLayout rlExchange;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    private List<Fragment> mFragmentList;
    private List<String> mTitleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        ivBack.setOnClickListener(this);
        rlExchange.setOnClickListener(this);
        ivRightIcon.setOnClickListener(this);
        llLookResult.setOnClickListener(this);


        /**
         * 检测微信是否安装,如果没有安装,需不显示分享按钮;如果安装了微信则显示分享按钮.
         */
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            ivRightIcon.setVisibility(View.GONE);
        } else {
            ivRightIcon.setVisibility(View.VISIBLE);
            ivRightIcon.setImageResource(R.mipmap.share);//右侧分享
        }

        //获取宝贝详情
        getDetailInfo(getIntent().getStringExtra("scheduleId"));

        SaveUserInfo.getInstance(PrizeDetailsActivity.this).setUserInfo("scheduleId", getIntent().getStringExtra("scheduleId"));


        //添加标题
        initTitile();
        //添加fragment
        initFragment();
        //设置适配器
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), mFragmentList, mTitleList));
        //将tablayout与fragment关联
        tabLayout.setupWithViewPager(vp);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取宝贝详情《刷新积分》
        getDetailInfo1(getIntent().getStringExtra("scheduleId"));

        if (!EncodeAndStringTool.isStringEmpty(SaveUserInfo.getInstance(PrizeDetailsActivity.this).getUserInfo("exchange_result"))
                && SaveUserInfo.getInstance(PrizeDetailsActivity.this).getUserInfo("exchange_result").equals("exchange_result")) {
            vp.setCurrentItem(1);
            SaveUserInfo.getInstance(PrizeDetailsActivity.this).setUserInfo("exchange_result", "");
        }
    }


    @Override
    public int intiLayout() {
        return R.layout.activity_prize_details;
    }

    private void initTitile() {
        mTitleList = new ArrayList<>();
        mTitleList.add("宝贝详情");
        mTitleList.add("兑换记录");
        //设置tablayout模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //tablayout获取集合中的名称
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(mTitleList.get(1)));
    }

    private void initFragment() {
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new WebDetailFragment());
        mFragmentList.add(new PrizeHistoryFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_exchange:
                ExchangeDialog();
                break;
            case R.id.iv_right_icon:
                //分享
                showSharedPop();
                break;
            case R.id.ll_look_result:
                vp.setCurrentItem(1);
                break;
            default:
                break;
        }
    }

    /**
     * 查询宝贝详情
     */
    private void getDetailInfo(String scheduleId) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getDetailInfo(scheduleId)//分页加载0
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<PrizeDetailBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<PrizeDetailBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            PrizeDetailBean prizeDetailBean = dataResponse.getDat();
                            if (prizeDetailBean.getScheduleStatus() == 0) {
                                //未开奖
                                llExchange.setVisibility(View.VISIBLE);
                                tvScore.setText(prizeDetailBean.getUserBP() + "");
                                tvReduceScore.setText("消耗" + prizeDetailBean.getBpcons() + "积分");

                                if (prizeDetailBean.getUserBP() >= prizeDetailBean.getBpcons()) {
                                    //可以兑换
                                    rlExchange.setBackgroundColor(Color.parseColor("#0194ec"));
                                    rlExchange.setClickable(true);
                                } else {
                                    //积分不足
                                    rlExchange.setBackgroundColor(Color.parseColor("#999999"));
                                    rlExchange.setClickable(false);
                                }
                                SaveUserInfo.getInstance(PrizeDetailsActivity.this).setUserInfo("ScheduleStatus", "0");
                            } else {
                                //已开奖
                                llLookResult.setVisibility(View.VISIBLE);
                                SaveUserInfo.getInstance(PrizeDetailsActivity.this).setUserInfo("ScheduleStatus", "1");
                            }

                        } else {
                            ErrorCodeTools.errorCodePrompt(PrizeDetailsActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
     * 查询宝贝详情1<用来刷新积分>
     */
    private void getDetailInfo1(String scheduleId) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getDetailInfo(scheduleId)//分页加载0
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<PrizeDetailBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<PrizeDetailBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            PrizeDetailBean prizeDetailBean = dataResponse.getDat();
                            if (prizeDetailBean.getScheduleStatus() == 0) {
                                //未开奖
                                tvScore.setText(prizeDetailBean.getUserBP() + "");
                                tvReduceScore.setText("消耗" + prizeDetailBean.getBpcons() + "积分");

                                if (prizeDetailBean.getUserBP() >= prizeDetailBean.getBpcons()) {
                                    //可以兑换
                                    rlExchange.setBackgroundColor(Color.parseColor("#0194ec"));
                                    rlExchange.setClickable(true);
                                } else {
                                    //积分不足
                                    rlExchange.setBackgroundColor(Color.parseColor("#999999"));
                                    rlExchange.setClickable(false);
                                }
                                SaveUserInfo.getInstance(PrizeDetailsActivity.this).setUserInfo("ScheduleStatus", "0");
                            } else {
                                //已开奖
                                SaveUserInfo.getInstance(PrizeDetailsActivity.this).setUserInfo("ScheduleStatus", "1");
                            }

                        } else {
                            ErrorCodeTools.errorCodePrompt(PrizeDetailsActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
     * 确认兑换
     */
    private void enterExchange(String scheduleId) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.enterExchange(scheduleId)//分页加载0
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse dataResponse) {
                        LoadingBar.cancel(rlMain);
                        if (dataResponse.isSuccees()) {
                            startActivity(new Intent(PrizeDetailsActivity.this, ExchangeResultActivity.class));
                        } else {
                            ErrorCodeTools.errorCodePrompt(PrizeDetailsActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingBar.cancel(rlMain);
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    /**
     * 确认兑换弹窗
     */
    private void ExchangeDialog() {
        new CircleDialog.Builder(this)
                .setTitle("提示")
                .setText("确认兑换该奖品奖券吗？")
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.textColor = Color.parseColor("#666666");
                    }
                })
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setPositive("兑换", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        CustomLoadingFactory factory = new CustomLoadingFactory();
                        LoadingBar.make(rlMain, factory).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                enterExchange(getIntent().getStringExtra("scheduleId"));
                            }
                        }, 2000);

                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#0194ec");
                    }
                })
                .setNegative("取消", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#333333");
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
                        ScannerUtils.saveImageToGallery(PrizeDetailsActivity.this, createViewBitmap(ll_view), ScannerUtils.ScannerType.MEDIA);
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
    CommonPopupWindow popupWindow;

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

        popupWindow.showAtLocation(rlMain, Gravity.BOTTOM, 0, 0);
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
        popupWindow.showAtLocation(rlMain, Gravity.CENTER, 0, 0);
    }

    /**
     * 分享
     *
     * @param channl 分享渠道id 1:微信分享;2 微信朋友圈分享 3:二维码分享
     */
    SharedBean sharedBean1;

    private void loadBattleAnswerShared(final int channl) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.prizeShare(channl, getIntent().getStringExtra("scheduleId"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<SharedBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<SharedBean> dataResponse) {
                        Log.i(TAG, "loadAppShared==onNext: " + dataResponse.toString());
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
                            ErrorCodeTools.errorCodePrompt(PrizeDetailsActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
                        Log.i(TAG, "onResult: " + share_media);
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
                        Log.e(TAG, "onError:share_media== " + share_media + "throwable =" + throwable);

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
                            ErrorCodeTools.errorCodePrompt(PrizeDetailsActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
