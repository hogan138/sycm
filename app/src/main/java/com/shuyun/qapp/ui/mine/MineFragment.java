package com.shuyun.qapp.ui.mine;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
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
import com.gyf.barlibrary.ImmersionBar;
import com.shuyun.qapp.net.MyApplication;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AnswerOpptyBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.InformatListenner;
import com.shuyun.qapp.ui.homepage.InformationActivity;
import com.shuyun.qapp.ui.integral.IntegralExchangeActivity;
import com.shuyun.qapp.receiver.MyReceiver;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.JumpTomap;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.shuyun.qapp.view.CircleImageView;
import com.shuyun.qapp.view.InviteSharePopupUtil;
import com.shuyun.qapp.view.RealNamePopupUtil;
import com.tencent.stat.StatConfig;
import com.umeng.analytics.MobclickAgent;

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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            //        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("FirstRun", 0);
//        Boolean first_run = sharedPreferences.getBoolean("First", true);
//        if (first_run) {
//            sharedPreferences.edit().putBoolean("First", false).commit();
            //个人信息
            loadMineHomeData1();
            //个人信息
            loadMineHomeData();
//        } else {
//        }
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
        ImmersionBar.with(this).statusBarColor(R.color.white).statusBarDarkFont(true).fitsSystemWindows(true).init();
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

        //个人信息
        loadMineHomeData();
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
                                        tvNum.setVisibility(View.VISIBLE);
                                        tvNum.setText("+" + mineBean.getAvailablePrize());//未使用奖品数
                                    } else {
                                        tvNum.setVisibility(View.GONE);
                                    }

                                    if (mineBean.getUpcommings() > 0) {
                                        tvThreePrizeExprized.setText(mineBean.getUpcommings() + "件奖品快过期");
                                    } else {
                                        tvThreePrizeExprized.setVisibility(View.GONE);
                                    }

                                    SaveUserInfo.getInstance(getActivity()).setUserInfo("my_bp", mineBean.getBp());

                                    //保存信息到本地
                                    SaveUserInfo.getInstance(mContext).setUserInfo("icon", mineBean.getHeaderId() + "");
                                    SaveUserInfo.getInstance(mContext).setUserInfo("icon1", mineBean.getHeader());
                                    SaveUserInfo.getInstance(mContext).setUserInfo("phone", mineBean.getPhone());
                                    SaveUserInfo.getInstance(mContext).setUserInfo("cert", mineBean.getCertification() + "");
                                    SaveUserInfo.getInstance(mContext).setUserInfo("nickname", mineBean.getNickname());
                                    SaveUserInfo.getInstance(mContext).setUserInfo("wxBind", String.valueOf(mineBean.getWxBind()));
                                    SaveUserInfo.getInstance(mContext).setUserInfo("wxHeader", mineBean.getWxHeader());

                                    //保存联系客服
                                    SaveUserInfo.getInstance(mContext).setUserInfo("contactUs_url", mineBean.getContactUs());

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
            case R.id.iv_common_right_icon:
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
                        RealNamePopupUtil.showAuthPop(mContext, llMineFragment);
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
                            Intent intent1 = new Intent(mContext, NewCashWithdrawActivity.class);
                            intent1.putExtra("cash", mineBean.getCash());
                            startActivity(intent1);
                        } else if (2 == mineBean.getWithdraw()) {
                            ToastUtil.showToast(mContext, "您有一笔资金正在提现中,请耐心等待...");
                        }
                    } else {
                        RealNamePopupUtil.showAuthPop(mContext, llMineFragment);
                    }

                }
                break;
            case R.id.btn_immedicate_use:
                if (mineBean.getCertification() == 1) {
                    Intent intent = new Intent(mContext, IntegralExchangeActivity.class);
                    startActivity(intent);
                } else {
                    RealNamePopupUtil.showAuthPop(mContext, llMineFragment);
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
            case R.id.rl_answer_record: //成绩单
                startActivity(new Intent(mContext, AnswerRecordActivity.class));
                break;
            case R.id.rl_system_set: //系统设置
                startActivity(new Intent(mContext, SystemSettingActivity.class));
                break;
            case R.id.rl_contact_us: //联系客服
                Intent intent = new Intent(mContext, WebBannerActivity.class);
                intent.putExtra("url", SaveUserInfo.getInstance(mContext).getUserInfo("contactUs_url"));
                intent.putExtra("name", "联系客服");
                startActivity(intent);
//                Intent intent = new Intent(mContext, WebBannerActivity.class);
//                intent.putExtra("url", "https://openapi.alipay.com/gateway.do?alipay_sdk=alipay-sdk-java-3.3.49.ALL&app_id=2018041902582802&biz_content=%7B%22biz_no%22%3A%22ZM201811023000000454500346282074%22%7D&charset=UTF-8&format=json&method=zhima.customer.certification.certify&sign=aQy3yV9HiiRoZm4I7RXu3SKKgkEYP%2FHdxtf9HGHlhnlVeJjmEW7npaaBCYyyWFpxUNAcu%2BZwzGN8PKEiaTa5B8G1mmKRjGaETYgQwTUA6Ce8brkUnKtkBW%2Fy8KeAKJRJ9d77WR1ncO0N8DC7dFj8JeMF8%2BYCxY4b65CYyq4iNZCbKnr%2F8%2BWE%2BTT%2Fxcb%2BTW4L4Ek1GFsqi%2F0XA8%2Fg0cgNwfkOICoALbPrUlw5r4iqI3aJiE2MidxiSfFcg38iM8HDPUy6bR5tv21Li0w5EA%2FtH09r7iX5Zcv7EhiUzGxnbOU%2BJ4RGEkq9mJOxxzjkUckrJf8ilmNL7WvMq8EVMeO%2FHQ%3D%3D&sign_type=RSA2&timestamp=2018-11-02+14%3A42%3A22&version=1.0");
//                intent.putExtra("name", "芝麻认证");
//                startActivity(intent);
                break;
            case R.id.rl_invite_share: //分享
                InviteSharePopupUtil.showSharedPop(mContext, llMineFragment);
                break;
            default:
                break;

        }
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
                            startActivity(new Intent(mContext, IntegralExchangeActivity.class));
                        } else {
                            RealNamePopupUtil.showAuthPop(mContext, llMineFragment);
                        }
                    }
                });
                break;
            default:
                break;
        }
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


    /**
     * 获取到我的首界面数据
     */

    private void loadMineHomeData1() {
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
                                    if (1 == mineBean.getCertification()) {
                                    } else {
                                        RealNamePopupUtil.showAuthPop(mContext, llMineFragment);
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
}

