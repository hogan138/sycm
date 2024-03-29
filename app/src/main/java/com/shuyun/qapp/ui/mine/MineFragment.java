package com.shuyun.qapp.ui.mine;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
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

import com.shuyun.qapp.R;
import com.shuyun.qapp.alipay.AlipayTradeManager;
import com.shuyun.qapp.base.BaseFragment;
import com.shuyun.qapp.bean.AnswerOpptyBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.ui.homepage.InformationActivity;
import com.shuyun.qapp.ui.integral.IntegralCenterActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.manager.ImageLoaderManager;
import com.shuyun.qapp.utils.NetWorkUtils;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.UmengPageUtil;
import com.shuyun.qapp.view.CircleImageView;
import com.shuyun.qapp.view.RealNamePopupUtil;
import com.tencent.stat.StatConfig;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 通用popupwindow
 * https://www.jianshu.com/p/9304d553aa67
 * 我的首界面
 */
public class MineFragment extends BaseFragment implements CommonPopupWindow.ViewInterface, OnRemotingCallBackListener {

    @BindView(R.id.ll_mine_fragment)
    LinearLayout llMineFragment;
    @BindView(R.id.iv_common_right_icon)
    ImageView ivCommonRightIcon;//右边消息按钮
    @BindView(R.id.iv_header_pic)
    CircleImageView ivHeaderPic;//头像
    @BindView(R.id.tv_phone_num1)
    TextView tvPhoneNum1;//账号
    @BindView(R.id.tv_today_answer_num)
    TextView tvTodayAnswerNum;//今日剩余答题次数
    @BindView(R.id.tv_balance)
    TextView tvBalance;//余额
    Unbinder unbinder;
    @BindView(R.id.iv_real_logo)
    ImageView ivRealLogo;//实名认证logo
    @BindView(R.id.ll_add)
    LinearLayout llAdd;//增加答题次数
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.rl_share)
    RelativeLayout rlInviteShare;
    @BindView(R.id.iv_point_prize)
    ImageView ivPointPrize;
    @BindView(R.id.ll_invite_code)
    LinearLayout llInviteCode; //邀请码

    private CommonPopupWindow popupWindow;
    private static final String LOAD_MINE_HOME_DATA = "loadMineHomeData";//个人信息
    private static final String LOAD_ANSWER_OPPTY_REMAINDER = "loadAnswerOpptyRemainder";//答题机会领取
    private static final String LOAD_ANSWER_OPPTY = "loadAnswerOppty"; //领取答题机会
    private Handler mHandler = new Handler();
    //图标
    private int[] icon = new int[]{R.mipmap.header02, R.mipmap.header03, R.mipmap.header04,
            R.mipmap.header05, R.mipmap.header06, R.mipmap.header07, R.mipmap.header08, R.mipmap.header09};
    /**
     * 用户自身个人信息
     */
    private MineBean mineBean;
    private String remainderTime;
    private Context mContext;
    private AnswerOpptyBean answerOpptyBean;
    private TextView tvRemainderTime;
    private Button btnGetImmedicate;
    private ImageView add_answernum_logo;
    private CountDownTimer timer;


    private String codeUrl = ""; //兑换码链接
    private String inviteUrl = "";//邀请码链接

    public MineFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
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

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            }, 10);

            //友盟页面统计
            UmengPageUtil.startPage(AppConst.APP_PERSONAL);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity();
    }

    /**
     * 获取到我的首界面数据
     */
    private void loadMineHomeData() {
        RemotingEx.doRequest(LOAD_MINE_HOME_DATA, RemotingEx.Builder().getMineHomeData(), this);
    }

    @OnClick({R.id.rl_back, R.id.iv_common_right_icon, R.id.iv_header_pic, R.id.rl_cardview,
            R.id.iv_real_logo, R.id.rl_my_score, R.id.rl_my_cash, R.id.rl_suggestion,
            R.id.ll_add, R.id.rl_prize, R.id.rl_prop, R.id.rl_order, R.id.rl_my_record,
            R.id.rl_exchange_code, R.id.rl_setting, R.id.rl_call, R.id.rl_share, R.id.ll_invite_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                if (mContext instanceof HomePageActivity) {
                    HomePageActivity homePageActivity = (HomePageActivity) mContext;
                    homePageActivity.radioGroupChange(0);
                }
                break;
            case R.id.iv_common_right_icon: //右侧消息按钮;
                ivCommonRightIcon.setImageResource(R.mipmap.messagew_n);
                startActivity(new Intent(mContext, InformationActivity.class));
//                startActivity(new Intent(mContext, AnswerKingMainActivity.class));
                break;
            case R.id.iv_header_pic://点击头像和修改个人信息走相同的逻辑
            case R.id.rl_cardview:
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    Intent intent = new Intent(mContext, ChangePersonalInfoActivity.class);
                    startActivity(intent);
                }
                break;
            case R.id.iv_real_logo://前往实名认证
                //未实名认证
                startActivity(new Intent(mContext, RealNameAuthActivity.class));
                break;
            case R.id.ll_add: //增加答题次数
                showAddAnswerNum();
                break;
            case R.id.rl_suggestion: //意见反馈
                startActivity(new Intent(mContext, FeedbackActivity.class));
                break;
            case R.id.rl_my_score: //我的积分
                Intent intent = new Intent(mContext, IntegralCenterActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_my_cash:  //现金提现
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    if (mineBean.getCertification() == 1) {
                        startActivity(new Intent(mContext, CashRecordActivity.class));
                    } else {
                        RealNamePopupUtil.showAuthPop(mContext, llMineFragment, getString(R.string.real_withdraw_describe));
                    }
                }
                break;
            case R.id.rl_prize: //奖品
                if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                    Intent notUse = new Intent(mContext, MinePrizeActivity.class);
                    notUse.putExtra("status", 1);
                    notUse.putExtra("certification", mineBean.getCertification());
                    startActivity(notUse);
                }
                break;
            case R.id.rl_prop://道具
                startActivity(new Intent(mContext, MyPropsActivity.class));
                break;
            case R.id.rl_my_record: //成绩单
                startActivity(new Intent(mContext, AnswerRecordNewActivity.class));
                break;
            case R.id.rl_exchange_code://兑换码
                if (!EncodeAndStringTool.isStringEmpty(codeUrl)) {
                    Intent code = new Intent(mContext, WebH5Activity.class);
                    code.putExtra("url", codeUrl);
                    code.putExtra("name", "");//名称 标题
                    startActivity(code);
                }
                break;
            case R.id.rl_order: //我的订单
                if (mineBean.getOrderEnabled() == 0) {
                    Toast.makeText(mContext, "敬请期待", Toast.LENGTH_SHORT).show();
                } else if (mineBean.getOrderEnabled() == 1) {
                    AlipayTradeManager.instance().showMyOrdersPage(getActivity(), 0);
                }
                break;
            case R.id.rl_setting: //系统设置
                startActivity(new Intent(mContext, SystemSettingActivity.class));
                break;
            case R.id.rl_call: //客服中心
                Intent in = new Intent(mContext, WebH5Activity.class);
                in.putExtra("url", SaveUserInfo.getInstance(mContext).getUserInfo("contactUs_url"));
                in.putExtra("name", "联系客服");
                startActivity(in);
                break;
            case R.id.rl_share: //分享
                if (!EncodeAndStringTool.isStringEmpty(inviteUrl)) {
                    Intent code = new Intent(mContext, WebH5Activity.class);
                    code.putExtra("url", inviteUrl);
                    code.putExtra("name", "");//名称 标题
                    startActivity(code);
                }
                break;
            case R.id.ll_invite_code:
                startActivity(new Intent(mContext, BindInviteCodeActivity.class));
                break;
            default:
                break;

        }
    }

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
                add_answernum_logo = view.findViewById(R.id.iv_logo);
                btnGetImmedicate = view.findViewById(R.id.btn_get_immedicate);
                tvRemainderTime = view.findViewById(R.id.tv_remainder_time);
                loadAnswerOpptyRemainder();
                ivClose0.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (popupWindow != null && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                            try {
                                timer.cancel();
                            } catch (Exception e) {

                            }
                        }
                    }
                });
                btnGetImmedicate.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (NetWorkUtils.isNetworkConnected(mContext)) {
                            loadAnswerOppty();
                        } else {
                            Toast.makeText(mContext, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }


    /**
     * 答题机会领取
     * U0004  答题机会已到上限
     */
    private void loadAnswerOpptyRemainder() {
        RemotingEx.doRequest(LOAD_ANSWER_OPPTY_REMAINDER, RemotingEx.Builder().getAnswerOpptyRemainder(), this);
    }

    /**
     * 领取答题机会
     * U0005
     */
    private void loadAnswerOppty() {
        RemotingEx.doRequest(LOAD_ANSWER_OPPTY, RemotingEx.Builder().getAnswerOppty(), this);
    }

    /**
     * 领取答题机会倒计时
     *
     * @param remainderTime
     */
    private void countDown(long remainderTime) {
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

    @Override
    public void onPause() {
        super.onPause();
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }

        if (LOAD_MINE_HOME_DATA.equals(action)) { //个人信息
            mineBean = (MineBean) response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                if (1 != mineBean.getCertification()) {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            RealNamePopupUtil.showAuthPop(mContext, llMineFragment, getString(R.string.real_main_describe));
                        }
                    }, 10);
                }

                long id = mineBean.getId();
                StatConfig.setCustomUserId(mContext, String.valueOf(id));
                if (0 == mineBean.getMessages()) {
                    //没有新消息
                    ivCommonRightIcon.setImageResource(R.mipmap.messagew_n);
                } else {
                    //有新消息
                    ivCommonRightIcon.setImageResource(R.mipmap.messagew);
                }

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
                    ivRealLogo.setVisibility(View.GONE);
                } else {
                    //未实名认证
                    ivRealLogo.setVisibility(View.VISIBLE);
                }
                tvTodayAnswerNum.setText("今日答题次数剩余: " + mineBean.getOpporitunity());
                tvBalance.setText(mineBean.getCash());

                if (!EncodeAndStringTool.isStringEmpty(mineBean.getBp())) {
                    SharedPrefrenceTool.put(mContext, "bp", mineBean.getBp());
                }

                tvScore.setText(mineBean.getBp());

                //有未使用的奖品，显示小红点
                if (mineBean.getAvailablePrize() > 0) {
                    ivPointPrize.setVisibility(View.VISIBLE);
                } else {
                    ivPointPrize.setVisibility(View.GONE);
                }

                SaveUserInfo.getInstance(mContext).setUserInfo("my_bp", mineBean.getBp());

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

                //兑换码
                codeUrl = mineBean.getCodeUrl();

                //邀请码
                inviteUrl = mineBean.getInviteUrl();

                //是否显示绑定邀请码入口
                Long inviteCode = mineBean.getInviteCode();
                if (inviteCode == 0) {
                    //隐藏
                    llInviteCode.setVisibility(View.GONE);
                } else if (inviteCode == 1) {
                    //显示
                    llInviteCode.setVisibility(View.VISIBLE);
                }

            }
        } else if (LOAD_ANSWER_OPPTY_REMAINDER.equals(action)) {
            remainderTime = (String) response.getDat();
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
        } else if (LOAD_ANSWER_OPPTY.equals(action)) {
            answerOpptyBean = (AnswerOpptyBean) response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(answerOpptyBean)) {
                btnGetImmedicate.setEnabled(false);
                add_answernum_logo.setBackgroundResource(R.mipmap.new_add_answernum_n);
                answerOpptyBean.getRemainder();
                countDown(answerOpptyBean.getRemainder());
                /**
                 * 领取答题机会之后需要刷新数据
                 */
                loadMineHomeData();
            }
        }
    }

    @Override
    public void refresh() {
        Integer position = (Integer) SharedPrefrenceTool.get(mContext, "headerId", 0);
        if (position != 0) {//根据修改的头像,变更头像
            ivHeaderPic.setImageResource(icon[position - 1]);
        }
        try {
            if ("action_msg".equals(SaveUserInfo.getInstance(mContext).getUserInfo("action_msg"))) {
                showAddAnswerNum();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SaveUserInfo.getInstance(mContext).setUserInfo("action_msg", "");
                    }
                }, 1000);
            }
        } catch (Exception e) {
        }

        loadMineHomeData();
    }
}

