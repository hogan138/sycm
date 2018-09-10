package com.shuyun.qapp.ui.mine;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.animation.SpringAnimation;
import android.support.animation.SpringForce;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AnswerOpptyBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.bean.SharedBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.InformatListenner;
import com.shuyun.qapp.ui.homepage.InformationActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.receiver.MyReceiver;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ScannerUtils;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.shuyun.qapp.view.CircleImageView;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 通用popupwindow
 * https://www.jianshu.com/p/9304d553aa67
 * 我的首界面
 */
public class MineFragment extends Fragment implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.ll_mine_fragment)
    LinearLayout llMineFragment;
    @BindView(R.id.tv_common_title1)
    TextView tvCommonTitle1;//标题
    @BindView(R.id.iv_common_right_icon)
    ImageView ivCommonRightIcon;//右边消息按钮
    @BindView(R.id.iv_header_pic)
    CircleImageView ivHeaderPic;//头像
    @BindView(R.id.tv_phone_num1)
    TextView tvPhoneNum1;//账号
    @BindView(R.id.tv_change_personal_info)
    TextView tvChangePersonalInfo;//修改个人信息
    @BindView(R.id.btn_is_name_auth)
    Button btnIsNameAuth;//是否实名认证按钮
    @BindView(R.id.tv_today_answer_num)
    TextView tvTodayAnswerNum;//今日剩余答题次数
    @BindView(R.id.tv_add_answer_num)
    TextView tvAddAnswerNum;//增加答题次数
    @BindView(R.id.tv_check_account_record)
    TextView tvCheckAccountRecord;//查看账户记录
    @BindView(R.id.tv_balance)
    TextView tvBalance;//余额
    @BindView(R.id.btn_immedicate_withdrawal)
    Button btnImmedicateWithdrawal;//立即提现
    @BindView(R.id.tv_integral_balance)
    TextView tvIntegralBalance;//积分数量
    @BindView(R.id.btn_immedicate_use)
    Button btnImmedicateUse;//立即使用
    @BindView(R.id.iv_not_use)
    ImageView ivNotUse;//未使用奖品按钮
    @BindView(R.id.tv_num)
    TextView tvNum;//未使用奖品数量
    @BindView(R.id.iv_already_use)
    ImageView ivAlreadyUse;//已使用奖品按钮
    @BindView(R.id.iv_out_of_date)
    ImageView ivOutOfDate;//已过期奖品
    @BindView(R.id.iv_all_prize)
    ImageView ivAllPrize;//全部奖品

    @BindView(R.id.rl_answer_record)
    RelativeLayout rlAnswerRecord;//答题记录
    @BindView(R.id.rl_system_set)
    RelativeLayout rlSystemSet;//系统设置
    @BindView(R.id.rl_contact_us)
    RelativeLayout rlContactUs;//联系我们
    @BindView(R.id.rl_header)
    RelativeLayout rlHeader;
    @BindView(R.id.rl_invite_share)
    RelativeLayout rlInviteShare;
    Unbinder unbinder;
    private static final String TAG = "MineFragment";
    private static final String TAG2 = "MineFragment2";
    @BindView(R.id.tv_three_prize_exprized)
    TextView tvThreePrizeExprized;
    private CommonPopupWindow popupWindow;

    //图标
    private int[] icon = new int[]{R.mipmap.header02, R.mipmap.header03, R.mipmap.header04,
            R.mipmap.header05, R.mipmap.header06, R.mipmap.header07, R.mipmap.header08, R.mipmap.header09};
    /**
     * 用户自身个人信息
     */
    private MineBean mineBean;
    private String remainderTime;
    private Activity mContext;
    private int SHARE_CHANNEL;//分享渠道2:微信好友;1:微信朋友圈
    private MyReceiver msgReceiver;


    public MineFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = (Activity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FirstRun", 0);
        Boolean first_run = sharedPreferences.getBoolean("First", true);
        if (first_run) {
            sharedPreferences.edit().putBoolean("First", false).commit();
            loadMineHome();
        } else {
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvCommonTitle1.setText("我的");
        /**
         * 检测微信是否安装,如果没有安装,需不显示分享按钮;如果安装了微信则显示分享按钮.
         */
        if (!MyApplication.mWxApi.isWXAppInstalled()) {
            rlInviteShare.setVisibility(View.GONE);
        } else {
            rlInviteShare.setVisibility(View.VISIBLE);
        }
        //初始化沉浸状态栏
        ImmersionBar.with(this).statusBarColor(R.color.mine_top).statusBarDarkFont(true).fitsSystemWindows(true).init();
        /**
         * 注册极光推送监听
         */
        msgReceiver = new MyReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("cn.jpush.android.intent.MESSAGE_RECEIVED");
        intentFilter.addAction("cn.jpush.android.intent.NOTIFICATION_RECEIVED");
        intentFilter.addAction("cn.jpush.android.intent.NOTIFICATION_OPENED");
        intentFilter.addAction("cn.jpush.android.intent.NOTIFICATION_CLICK_ACTION");
        intentFilter.addAction("cn.jpush.android.intent.CONNECTION");
        intentFilter.addCategory("com.shuyun.qapp");
        mContext.registerReceiver(msgReceiver, intentFilter);
        msgReceiver.setOnMsgListenner(new InformatListenner() {
            @Override
            public void loadInfoRefreshUi() {
                if (AppConst.hasMsg) {//如果有消息
                    ivCommonRightIcon.setImageResource(R.mipmap.messagew);
                }
            }
        });

    }


    //在activity或者fragment中添加友盟统计
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineFragment"); //统计页面，"MainScreen"为页面名称，可自定义
        Integer position = (Integer) SharedPrefrenceTool.get(mContext, "headerId", 0);
        if (position != 0) {//根据修改的头像,变更头像
            ivHeaderPic.setImageResource(icon[position - 1]);
        }

        loadMineHomeData();

        try {
            if (SaveUserInfo.getInstance(getActivity()).getUserInfo("action_msg") != null && SaveUserInfo.getInstance(getActivity()).getUserInfo("action_msg").equals("action_msg")) {
                showAddAnswerNum();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SaveUserInfo.getInstance(getActivity()).setUserInfo("action_msg", "");
                    }
                }, 1000);

            }
        } catch (Exception e) {

        }


    }


    /**
     * 获取到我的首界面数据
     */

    private void loadMineHomeData() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getMineHomeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<MineBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<MineBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            mineBean = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                                long id = mineBean.getId();
                                StatConfig.setCustomUserId(getActivity(), String.valueOf(id));
                                try {
                                    if (0 == mineBean.getMessages()) {
                                        //没有新消息
                                        ivCommonRightIcon.setImageResource(R.mipmap.messagew_n);
                                    } else {
                                        //有新消息
                                        ivCommonRightIcon.setImageResource(R.mipmap.messagew);
                                    }

                                    //TODO 圆形图片有待修改
                                    if (!EncodeAndStringTool.isStringEmpty(mineBean.getHeaderId()) && mineBean.getHeaderId() > 0) {
                                        ivHeaderPic.setImageResource(icon[mineBean.getHeaderId() - 1]);
                                    } else {
                                        ImageLoaderManager.LoadImage(mContext, mineBean.getHeader(), ivHeaderPic, R.mipmap.head);
                                    }
                                    if (!EncodeAndStringTool.isStringEmpty(mineBean.getAccount())) {
                                        tvPhoneNum1.setText(mineBean.getAccount());
                                    }
                                    Integer isCertification = mineBean.getCertification();
                                    SharedPrefrenceTool.put(mContext, "certification", isCertification);
                                    if (1 == mineBean.getCertification()) {
                                        //已实名认证
                                        btnIsNameAuth.setBackgroundResource(R.mipmap.pass);
                                    } else {
                                        //未实名认证
                                        btnIsNameAuth.setBackgroundResource(R.mipmap.notpass);
                                    }

                                    tvTodayAnswerNum.setText("今日答题次数剩余: " + mineBean.getOpporitunity());
                                    tvBalance.setText("余额:￥" + mineBean.getCash());
                                    if (!EncodeAndStringTool.isStringEmpty(mineBean.getCash())) {
                                        double money = Double.parseDouble(mineBean.getCash());
                                        if (1 == mineBean.getWithdraw() && money >= 50) {
                                            //可以提现
                                            btnImmedicateWithdrawal.setEnabled(true);
                                        } else {
                                            //不能提现和提现中 按钮变灰
                                            btnImmedicateWithdrawal.setEnabled(false);
                                        }
                                    }
                                    if (!EncodeAndStringTool.isStringEmpty(mineBean.getBp())) {
                                        SharedPrefrenceTool.put(mContext, "bp", mineBean.getBp());
                                        if (Integer.parseInt(mineBean.getBp()) > 0) {
                                            btnImmedicateUse.setEnabled(true);
                                        } else {
                                            btnImmedicateUse.setEnabled(false);
                                        }
                                    }
                                    tvIntegralBalance.setText("余额:" + mineBean.getBp());

                                    if (mineBean.getAvailablePrize() > 0) {
                                        tvNum.setText("+" + mineBean.getAvailablePrize());//未使用奖品数
                                    } else {
                                        tvNum.setVisibility(View.GONE);
                                    }

                                    if (mineBean.getUpcommings() > 0) {
                                        tvThreePrizeExprized.setText(mineBean.getUpcommings() + "件奖品快过期");
                                    } else {
                                        tvThreePrizeExprized.setVisibility(View.GONE);
                                    }

                                    SaveUserInfo.getInstance(getActivity()).setUserInfo("my_bp", mineBean.getBp() + "");

                                    //保存信息到本地
                                    SaveUserInfo.getInstance(mContext).setUserInfo("icon", mineBean.getHeaderId() + "");
                                    SaveUserInfo.getInstance(mContext).setUserInfo("icon1", mineBean.getHeader());
                                    SaveUserInfo.getInstance(mContext).setUserInfo("phone", mineBean.getPhone());
                                    SaveUserInfo.getInstance(mContext).setUserInfo("cert", mineBean.getCertification() + "");
                                    SaveUserInfo.getInstance(mContext).setUserInfo("certinfo", mineBean.getCertInfo());
                                    SaveUserInfo.getInstance(mContext).setUserInfo("nickname", mineBean.getNickname());
                                    SaveUserInfo.getInstance(mContext).setUserInfo("wxBind", String.valueOf(mineBean.getWxBind()));
                                    SaveUserInfo.getInstance(mContext).setUserInfo("wxHeader", mineBean.getWxHeader());

                                } catch (Exception e) {

                                }
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
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

    @OnClick({R.id.iv_common_right_icon, R.id.iv_header_pic, R.id.rl_header, R.id.tv_change_personal_info, R.id.btn_is_name_auth,
            R.id.tv_add_answer_num, R.id.tv_check_account_record, R.id.btn_immedicate_withdrawal, R.id.btn_immedicate_use,
            R.id.iv_not_use, R.id.iv_already_use, R.id.iv_out_of_date, R.id.iv_all_prize, R.id.rl_answer_record,
            R.id.rl_system_set, R.id.rl_contact_us, R.id.rl_invite_share})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_common_right_icon://TODO
                ivCommonRightIcon.setImageResource(R.mipmap.messagew_n);//右侧消息按钮;
                startActivity(new Intent(mContext, InformationActivity.class));
                break;
            case R.id.iv_header_pic://点击头像和修改个人信息走相同的逻辑
            case R.id.rl_header://点击头布局都跳转到修改个人信息页面
            case R.id.tv_change_personal_info:
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    Intent intent = new Intent(mContext, ChangePersonalInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.btn_is_name_auth://前往实名认证
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    if (1 == mineBean.getCertification()) {
                        //已实名认证
                        ToastUtil.showToast(mContext, "您已实名认证了");
                    } else {
                        //未实名认证
                        showAuthPop();
                    }
                }
                break;
            case R.id.tv_add_answer_num:
                showAddAnswerNum();
                break;
            case R.id.tv_check_account_record:
                startActivity(new Intent(mContext, AccountRecordActivity.class));
                break;
            case R.id.btn_immedicate_withdrawal:
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    //立即提现
                    if (mineBean.getCertification() == 1) {
                        if (1 == mineBean.getWithdraw()) {
                            Intent intent1 = new Intent(mContext, CashWithdrawalActivity.class);
                            intent1.putExtra("cash", mineBean.getCash());
                            startActivity(intent1);
                        } else if (2 == mineBean.getWithdraw()) {
                            ToastUtil.showToast(mContext, "您有一笔资金正在提现中,请耐心等待...");
                        }
                    } else {
                        showAuthPop();
                    }

                }
                break;
            case R.id.btn_immedicate_use:
                if (mineBean.getCertification() == 1) {
                    Intent intent = new Intent(mContext, IntegralExchangeActivity.class);
                    startActivity(intent);
                } else {
                    showAuthPop();
                }
                break;
            case R.id.iv_not_use:
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    Intent notUse = new Intent(mContext, MinePrizeActivity.class);
                    notUse.putExtra("status", 1);
                    notUse.putExtra("certification", mineBean.getCertification());
                    startActivity(notUse);
                }
                break;
            case R.id.iv_already_use:
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    Intent alreadyUse = new Intent(mContext, MinePrizeActivity.class);
                    alreadyUse.putExtra("certification", mineBean.getCertification());//是否实名认证
                    alreadyUse.putExtra("status", 2);
                    startActivity(alreadyUse);
                }
                break;
            case R.id.iv_out_of_date:
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    Intent outOfDate = new Intent(mContext, MinePrizeActivity.class);
                    outOfDate.putExtra("certification", mineBean.getCertification());//是否实名认证
                    outOfDate.putExtra("status", 3);
                    startActivity(outOfDate);
                }
                break;
            case R.id.iv_all_prize:
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    Intent allPrize = new Intent(mContext, MinePrizeActivity.class);
                    allPrize.putExtra("certification", mineBean.getCertification());//是否实名认证
                    allPrize.putExtra("status", 0);
                    startActivity(allPrize);
                }
                break;
            case R.id.rl_answer_record:
                startActivity(new Intent(mContext, AnswerRecordActivity.class));
                break;
            case R.id.rl_system_set:
                startActivity(new Intent(mContext, SystemSettingActivity.class));
                break;
            case R.id.rl_contact_us:
                startActivity(new Intent(mContext, WebContactUsActivity.class));
                break;
            case R.id.rl_invite_share:
                showSharedPop();
                break;
            default:
                break;

        }
    }

    /**
     * 实名认证popupWindow
     */
    public void showAuthPop() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(mContext).inflate(R.layout.real_name_auth_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.real_name_auth_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(llMineFragment, Gravity.CENTER, 0, 0);
    }


    TextView tvRemainderTime;
    Button btnGetImmedicate;

    /**
     * 增加答题次数弹窗
     */
    private void showAddAnswerNum() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(mContext).inflate(R.layout.add_answer_num_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.add_answer_num_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), upView.getMeasuredHeight())
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(llMineFragment, Gravity.CENTER, 0, 0);

    }

    @Override
    public void getChildView(final View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.real_name_auth_popupwindow:
                ImageView ivClose1 = (ImageView) view.findViewById(R.id.iv_close_icon1);
                Button btnRealNameAuth = (Button) view.findViewById(R.id.btn_real_name_auth1);
                ivClose1.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });
                btnRealNameAuth.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        startActivity(new Intent(mContext, RealNameAuthActivity.class));
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
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });
                btnGetImmedicate.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (NetworkUtils.isAvailableByPing()) {
                            loadAnswerOppty();
                        } else {
                            Toast.makeText(mContext, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                btnIntegrationPrize.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        /**
                         * 是否实名认证
                         * 0——未实名认证
                         * 1——已实名认证
                         * 2——审核中
                         * 3——未通过
                         * 4——拉黑
                         */
                        if (mineBean.getCertification() == 1) {
                            Intent intent0 = new Intent(mContext, IntegralDrawActivity.class);
                            intent0.putExtra("status", AppConst.INTEGRL_DRAW);//积分抽奖跳到开宝箱界面;
                            startActivity(intent0);
                        } else {
                            showAuthPop();
                        }
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
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN;
                        loadInviteShared(SHARE_CHANNEL);//微信分享
                    }
                });
                ivFriends.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(popupWindow) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                        SHARE_CHANNEL = AppConst.SHARE_MEDIA_WEIXIN_CIRCLE;
                        loadInviteShared(AppConst.SHARE_MEDIA_WEIXIN_CIRCLE);//微信朋友圈分享
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
                        ScannerUtils.saveImageToGallery(mContext, createViewBitmap(ll_view), ScannerUtils.ScannerType.MEDIA);
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
     * 答题机会领取剩余时长TODO
     * U0004  答题机会已到上限
     */
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
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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
                                countDown(answerOpptyBean.getRemainder());
                                /**
                                 * 领取答题机会之后需要刷新数据
                                 */
                                loadMineHomeData();
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineFragment");
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (popupWindow != null && popupWindow.isShowing()) {
                        popupWindow.dismiss();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (msgReceiver != null) {
            mContext.unregisterReceiver(msgReceiver);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 邀请分享弹窗
     */
    public void showSharedPop() {
        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(mContext).inflate(R.layout.share_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        //取值范围0.0f-1.0f 值越小越暗
//        .setAnimationStyle(R.style.AnimUp)//设置动画
        //设置子View点击事件
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.share_popupwindow)
                .setWidthAndHeight(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
//                .setAnimationStyle(R.style.popwin_anim_style_bottom)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();

        popupWindow.showAtLocation(llMineFragment, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 邀请分享
     *
     * @param channl 1:微信分享;2 微信朋友圈分享
     */
    SharedBean sharedBean1;

    private void loadInviteShared(final int channl) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.inviteShared(channl)
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
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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
        View upView = LayoutInflater.from(mContext).inflate(R.layout.share_qr_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(mContext)
                .setView(R.layout.share_qr_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAtLocation(llMineFragment, Gravity.CENTER, 0, 0);
    }

    /**
     * 微信分享
     *
     * @param sharedBean
     */
    private void wechatShare(final SharedBean sharedBean) {
        SHARE_MEDIA media = SHARE_MEDIA.WEIXIN;
        if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN) {//微信
            media = SHARE_MEDIA.WEIXIN;
        } else if (SHARE_CHANNEL == AppConst.SHARE_MEDIA_WEIXIN_CIRCLE) {//朋友圈
            media = SHARE_MEDIA.WEIXIN_CIRCLE;
        }
        UMImage image = new UMImage(mContext, R.mipmap.logo);//网络图片
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
        UMWeb web = new UMWeb(sharedBean.getUrl());//默认链接AppConst.CONTACT_US
        web.setTitle(sharedBean.getTitle());//标题
        web.setThumb(image);//缩略图
        web.setDescription(sharedBean.getContent());//描述
        new ShareAction(mContext)
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
                        Log.i(TAG, "loadSharedSure==onNext: " + dataResponse.toString());
                        if (dataResponse.isSuccees()) {

                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, dataResponse.getErr(), dataResponse.getMsg());
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
     * 获取到我的首界面数据
     */
    private void loadMineHome() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getMineHomeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<MineBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<MineBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            mineBean = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {

                                try {
                                    //是否实名认证
                                    if (1 == mineBean.getCertification()) {
                                        //已实名认证
                                    } else {
                                        //未实名认证
                                        showAuthPop();
                                    }

                                } catch (Exception e) {

                                }
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
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
     * 监听我的界面返回键
     */
    public void mineFragmentBack() {
        if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
            popupWindow.dismiss();
            popupWindow = null;
        } else {
            mContext.finish();
        }
    }
}

