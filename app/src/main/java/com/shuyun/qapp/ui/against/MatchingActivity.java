package com.shuyun.qapp.ui.against;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MatchTimeBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.view.RoundImageView;
import com.shuyun.qapp.view.TiaoZiView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 答题对战----匹配对手
 */

public class MatchingActivity extends BaseActivity implements View.OnClickListener, CommonPopupWindow.ViewInterface, OnRemotingCallBackListener<Object> {


    @BindView(R.id.iv_left_icon)
    ImageView ivLeftIcon;
    @BindView(R.id.iv_common_left_icon)
    RelativeLayout ivCommonLeftIcon;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_vs)
    TextView tvVs;
    @BindView(R.id.tv_anim)
    TextView tvAnim;
    @BindView(R.id.iv_head_mine)
    RoundImageView ivHeadMine;
    @BindView(R.id.iv_head_it)
    RoundImageView ivHeadIt;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;
    @BindView(R.id.tv_mine_phone)
    TextView tvMinePhone;
    @BindView(R.id.tv_it_phone)
    TextView tvItPhone;
    @BindView(R.id.tv_reduce_score)
    TextView tvReduceScore;
    @BindView(R.id.rl_score)
    RelativeLayout rlScore;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.rl_cancel_matching)
    Button rlCancelMatching;
    @BindView(R.id.iv_bottom)
    ImageView ivBottom;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    //头像图标
    private int[] icon = new int[]{R.mipmap.header02, R.mipmap.header03, R.mipmap.header04,
            R.mipmap.header05, R.mipmap.header06, R.mipmap.header07, R.mipmap.header08, R.mipmap.header09};
    /**
     * 动画
     */
    Animation mAnimation = null;

    //搜索动画
    private String s = "...";
    private TiaoZiView tiaoziView;

    //vs动画handler
    Handler handlerVs = new Handler();
    Runnable vsRunnable;

    //搜索文字动画handler
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            tiaoziView = new TiaoZiView(tvAnim, s, 300);//调用构造方法，直接开启
            handler.postDelayed(runnable, 1000);
        }
    };

    /**
     * 延时几秒,匹配成功之后更新UI界面
     */
    Handler matchSucceedHandler = new Handler();
    Runnable matchSucceedRunnable;
    /**
     * 停留在匹配成功页几秒后,进入答题页
     */
    Handler toAnswerhandler = new Handler();
    Runnable toAnswerRunnable;

    /**
     * 匹配失败弹框
     */
    Handler failHandler = new Handler();
    Runnable failRunnable;
    private MatchTimeBean matchTimeBean;
    int type;
    private String name;

    @Override
    public int intiLayout() {
        return R.layout.activity_matching;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        ivCommonLeftIcon.setOnClickListener(this);
        Intent intent = getIntent();
        type = intent.getIntExtra("type", 0);
        name = intent.getStringExtra("name");
        tvCommonTitle.setText(name);
        tvTitle.setText(name);

        ivLeftIcon.setImageResource(R.mipmap.backb);//左侧返回
        rlCancelMatching.setOnClickListener(this);

    }

    /**
     * 获取到用户匹配时长信息
     */
    private void loadMatchInfo() {

        RemotingEx.doRequest(AppConst.MATCHING_USER_TIME, ApiServiceBean.getMatchTimeInfo(), null, this);

    }

    private void Startanimation() {
        //头像飞入动画
        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(MatchingActivity.this, R.anim.trans_mine_in);
        ivHeadMine.startAnimation(translateAnimation);
        TranslateAnimation translateAnimation1 = (TranslateAnimation) AnimationUtils.loadAnimation(MatchingActivity.this, R.anim.trans_it_in);
        ivHeadIt.startAnimation(translateAnimation1);

        //vs动画
        vsRunnable = new Runnable() {
            @Override
            public void run() {
                mAnimation = AnimationUtils.loadAnimation(MatchingActivity.this, R.anim.big_small_anim);
                tvVs.setAnimation(mAnimation);
                mAnimation.start();
            }
        };
        handlerVs.postDelayed(vsRunnable, 1000);

        //底部图片动画
        ivBottom.setVisibility(View.VISIBLE);
        mAnimation = AnimationUtils.loadAnimation(MatchingActivity.this, R.anim.image_alpha);
        ivBottom.setAnimation(mAnimation);
        mAnimation.start();

        //搜索文字动画
        handler.post(runnable);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取到用户匹配时长信息
        loadMatchInfo();

    }

    /**
     * 减去消耗的积分
     */
    private void loadUseBpCons() {

        RemotingEx.doRequest(AppConst.AGAINST_REDUCE_SCORE, ApiServiceBean.useBpCons(), new Object[]{type}, this);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancleHandler();
    }

    /**
     * 取消handler回调
     */
    private void cancleHandler() {
        handler.removeCallbacks(runnable);
        handlerVs.removeCallbacks(vsRunnable);
        matchSucceedHandler.removeCallbacks(matchSucceedRunnable);
        toAnswerhandler.removeCallbacks(toAnswerRunnable);
        failHandler.removeCallbacks(failRunnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //取消handler回调
        cancleHandler();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_common_left_icon:
                exitAnimation();
                break;
            case R.id.rl_cancel_matching:
                exitAnimation();
                break;
            default:
                break;
        }
    }

    CommonPopupWindow popupWindow;

    /**
     * 匹配失败弹窗
     */
    public void showPopupWindows() {
        if (popupWindow != null && popupWindow.isShowing()) return;
        View upView = LayoutInflater.from(MatchingActivity.this).inflate(R.layout.matching_fail_popupwindow, null);
        //测量View的宽高
        CommonPopUtil.measureWidthAndHeight(upView);
        popupWindow = new CommonPopupWindow.Builder(MatchingActivity.this)
                .setView(R.layout.matching_fail_popupwindow)
                .setWidthAndHeight(upView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                .setOutsideTouchable(true)
                .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                //设置子View点击事件
                .setViewOnclickListener(this)
                .create();
        popupWindow.showAtLocation(rlMain, Gravity.CENTER, 0, 0);
    }

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.matching_fail_popupwindow:
                TextView tv_close = view.findViewById(R.id.tv_close);
                tv_close.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        finish();
                    }
                });
                TextView tv_restart_matching = view.findViewById(R.id.tv_restart_matching);
                tv_restart_matching.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        popupWindow.dismiss();
                        loadMatchInfo();
                    }
                });
                break;
            default:
                break;
        }
    }

    //返回键处理
    private void exitAnimation() {

        if (!EncodeAndStringTool.isObjectEmpty(popupWindow)) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
            } else {
                headOutAnimation();
            }
        } else {
            headOutAnimation();
        }

    }

    //头像飞出动画
    private void headOutAnimation() {
        cancleHandler();
        //头像飞出动画
        TranslateAnimation translateAnimation = (TranslateAnimation) AnimationUtils.loadAnimation(MatchingActivity.this, R.anim.trans_mine_out);
        ivHeadMine.startAnimation(translateAnimation);
        TranslateAnimation translateAnimation1 = (TranslateAnimation) AnimationUtils.loadAnimation(MatchingActivity.this, R.anim.trans_it_out);
        ivHeadIt.startAnimation(translateAnimation1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);

    }


    @Override
    public void onBackPressed() {
        exitAnimation();
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> listDataResponse) {
        if (AppConst.MATCHING_USER_TIME.equals(action)) { //获取用户匹配时长
            if (listDataResponse.isSuccees()) {
                try {
                    matchTimeBean = (MatchTimeBean) listDataResponse.getDat();
                    if (matchTimeBean.getMatchTime() < 0) {
                        ivHeadMine.setImageResource(icon[matchTimeBean.getUser().getHeaderId() - 1]);
                        //开始动画
                        Startanimation();
                        failRunnable = new Runnable() {
                            @Override
                            public void run() {
                                showPopupWindows();
                            }
                        };
                        failHandler.postDelayed(failRunnable, -(matchTimeBean.getMatchTime()) * 1000);
                    } else {
                        ivHeadMine.setImageResource(icon[matchTimeBean.getUser().getHeaderId() - 1]);

                        //开始动画
                        Startanimation();

                        /**
                         * 延时几秒后进入答题页面
                         */
                        toAnswerRunnable = new Runnable() {
                            @Override
                            public void run() {
                                finish();
                                Intent intent = new Intent(MatchingActivity.this, AgainstActivity.class);
                                intent.putExtra("type", type);
                                intent.putExtra("name", name);
                                intent.putExtra("from", getIntent().getStringExtra("from"));
                                intent.putExtra("groupId", getIntent().getLongExtra("groupId", 0));
                                intent.putExtra("my_head", matchTimeBean.getUser().getHeaderId());
                                intent.putExtra("my_phone", matchTimeBean.getUser().getPhone());
                                intent.putExtra("it_head", matchTimeBean.getRobotPic());
                                intent.putExtra("it_phone", matchTimeBean.getRobotAccount());
                                intent.putExtra("score", getIntent().getStringExtra("score"));
                                startActivity(intent);
                            }
                        };
                        /**
                         * 匹配成功之后更新UI
                         */
                        matchSucceedRunnable = new Runnable() {
                            @Override
                            public void run() {
                                //取消匹配淡出
                                Animation alphaAnimation = new AlphaAnimation(1, 0);
                                alphaAnimation.setDuration(500);
                                alphaAnimation.setInterpolator(new LinearInterpolator());
                                alphaAnimation.setFillBefore(false);
                                alphaAnimation.setFillAfter(true);
                                rlCancelMatching.startAnimation(alphaAnimation);
                                rlCancelMatching.setVisibility(View.GONE);
                                llSearch.setVisibility(View.INVISIBLE);

                                if (getIntent().getStringExtra("score").equals("")) {
                                    rlScore.setVisibility(View.GONE);
                                    toAnswerhandler.postDelayed(toAnswerRunnable, 2000);
                                } else {
                                    llSearch.setVisibility(View.INVISIBLE);
                                    rlScore.setVisibility(View.VISIBLE);
                                    tvScore.setText("积分：" + matchTimeBean.getUser().getBp());
                                    tvReduceScore.setText("-" + getIntent().getStringExtra("score"));

                                    //消耗积分淡出
                                    Animation alphaAnimation1 = new AlphaAnimation(0, 1);
                                    alphaAnimation1.setDuration(1000);
                                    alphaAnimation1.setInterpolator(new LinearInterpolator());
                                    alphaAnimation1.setFillBefore(false);
                                    alphaAnimation1.setFillAfter(true);
                                    rlScore.startAnimation(alphaAnimation1);

                                    /**
                                     * 减去消耗的积分
                                     */
                                    loadUseBpCons();
                                }
                                ivHeadIt.setImageResource(icon[matchTimeBean.getRobotPic()]);//- 1
                                tvMinePhone.setVisibility(View.VISIBLE);
                                tvItPhone.setVisibility(View.VISIBLE);
                                tvMinePhone.setText(matchTimeBean.getUser().getPhone());
                                tvItPhone.setText(matchTimeBean.getRobotAccount());

                                //机器人头像动画
                                Animation alphaAnimation2 = new AlphaAnimation(1, 0);
                                alphaAnimation2.setDuration(300);
                                alphaAnimation2.setInterpolator(new LinearInterpolator());
                                alphaAnimation2.setRepeatCount(3);
                                alphaAnimation2.setRepeatMode(Animation.REVERSE);
                                alphaAnimation2.setFillAfter(true);
                                ivHeadIt.startAnimation(alphaAnimation2);
                            }
                        };
                        matchSucceedHandler.postDelayed(matchSucceedRunnable, matchTimeBean.getMatchTime() * 1000);
                    }
                } catch (Exception e) {

                }

            } else {//错误码提示
                ErrorCodeTools.errorCodePrompt(MatchingActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
            }
        } else if (AppConst.AGAINST_REDUCE_SCORE.equals(action)) { //减去消耗的积分
            if (listDataResponse.isSuccees()) {
                //进入答题页面
                toAnswerhandler.postDelayed(toAnswerRunnable, 2000);
            } else if (listDataResponse.getErr().equals("L0001")) {//积分不足
                Toast.makeText(MatchingActivity.this, "对不起，您的积分不足！", Toast.LENGTH_SHORT).show();
                finish();
            } else {//错误码提示
                ErrorCodeTools.errorCodePrompt(MatchingActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
            }
        }
    }
}
