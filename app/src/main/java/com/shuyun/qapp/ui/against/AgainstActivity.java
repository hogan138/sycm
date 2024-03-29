package com.shuyun.qapp.ui.against;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.ApplyAnswer;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.RobotInputAgainstBean;
import com.shuyun.qapp.bean.RobotShowBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.Base64Utils;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RSAUtils;
import com.shuyun.qapp.view.CircleImageView;
import com.shuyun.qapp.view.CircleProgressView;
import com.shuyun.qapp.view.TiaoZiView;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * 答题对战页面
 */

public class AgainstActivity extends BaseActivity implements OnRemotingCallBackListener {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //标题
    @BindView(R.id.btn_answer1)
    RadioButton btnAnswer1; //选项一
    @BindView(R.id.btn_answer2)
    RadioButton btnAnswer2; //选项二
    @BindView(R.id.btn_answer3)
    RadioButton btnAnswer3; //选项三
    @BindView(R.id.btn_answer4)
    RadioButton btnAnswer4; //选项四
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress; //答题进度条
    @BindView(R.id.tv_page_end)
    TextView tvPageEnd;//第几题,共有多少题
    @BindView(R.id.tv_question)
    TextView tvQuestion;//题目
    @BindView(R.id.tv_countdown)
    TextView tvCountdown_main;//倒计时
    @BindView(R.id.rl_count_down)
    RelativeLayout rlCountDown;//倒计时背景
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;//倒计时文字
    @BindView(R.id.circle_view)
    CircleProgressView circleView; //倒计时进度
    @BindView(R.id.tv_anim)
    TextView tvAnim; //生成题目倒计时文字
    @BindView(R.id.iv_head_mine)
    CircleImageView ivHeadMine; //自己头像
    @BindView(R.id.tv_mine_phone)
    TextView tvMinePhone; //自己手机号
    @BindView(R.id.tv_mine_score)
    TextView tvMineScore;//自己分数
    @BindView(R.id.iv_head_it)
    CircleImageView ivHeadIt; //机器人头像
    @BindView(R.id.tv_it_phone)
    TextView tvItPhone; //机器人手机号
    @BindView(R.id.tv_it_score)
    TextView tvItScore; //机器人分数
    @BindView(R.id.btn1_left_iv)
    ImageView btn1LeftIv;//选项一左图片
    @BindView(R.id.btn2_left_iv)
    ImageView btn2LeftIv; //选项二左图片
    @BindView(R.id.btn3_left_iv)
    ImageView btn3LeftIv; //选项三左图片
    @BindView(R.id.btn4_left_iv)
    ImageView btn4LeftIv; //选项四左图片
    @BindView(R.id.ll_question_options)
    LinearLayout llQuestionOptions;
    private static final String TAG = "AgainstActivity";
    @BindView(R.id.tv_select_a)
    TextView tvSelectA;
    @BindView(R.id.rl_select_a)
    RelativeLayout rlSelectA;
    @BindView(R.id.tv_select_b)
    TextView tvSelectB;
    @BindView(R.id.rl_select_b)
    RelativeLayout rlSelectB;
    @BindView(R.id.tv_select_c)
    TextView tvSelectC;
    @BindView(R.id.rl_select_c)
    RelativeLayout rlSelectC;
    @BindView(R.id.tv_select_d)
    TextView tvSelectD;
    @BindView(R.id.rl_select_d)
    RelativeLayout rlSelectD;
    @BindView(R.id.rl_left_bg)
    RelativeLayout rlLeftBg;
    @BindView(R.id.rl_right_bg)
    RelativeLayout rlRightBg;
    @BindView(R.id.iv_bottom)
    ImageView ivBottom;
    @BindView(R.id.rl_A)
    RelativeLayout rlA;  //选项A
    @BindView(R.id.rl_B)
    RelativeLayout rlB; //选项B
    @BindView(R.id.rl_C)
    RelativeLayout rlC; //选项C
    @BindView(R.id.rl_D)
    RelativeLayout rlD; //选项D
    @BindView(R.id.rl_countdown)
    RelativeLayout rlCountdown; //倒计时布局
    /**
     * 申请答题返回数据
     */
    private ApplyAnswer applyAnswer;
    /**
     * 题目列表
     */
    private List<ApplyAnswer.QuestionsBean> questionsBeans;
    /**
     * 答题倒计时timer
     */
    private CountDownTimer timer;
    /**
     * 当前正在答题题目
     */
    private int position = 0;
    /**
     * 题目数量
     */
    private int answerNum;
    /**
     * 当前正在回答的题目对象
     */
    private ApplyAnswer.QuestionsBean questionsBean;

    /**
     * 动画
     */
    private Animation mAnimation = null;
    private RobotShowBean robotShowBean;
    private String answer = ""; //机器人选择的答案
    private double timeConsuming = 0;//机器人答题耗时
    private String answerId = "";  //机器人选项
    private int last = 0;
    private boolean robot_isSelcet = false;
    private boolean user_isSelect = false;
    private int type;
    private int user_checked = 0;
    private String fail_id = ""; //错题id
    //头像图标
    private int[] icon = new int[]{R.mipmap.header02, R.mipmap.header03, R.mipmap.header04,
            R.mipmap.header05, R.mipmap.header06, R.mipmap.header07, R.mipmap.header08, R.mipmap.header09};
    private String questionArray = "";
    private int isLast = 1;
    private String name;

    private Handler handler = new Handler();
    private Map<Integer, RadioButton> mapAnswerButton = new HashMap<>(); // 选项的映射：key：btnAnswer1~4.id
    private Context mContext;

    @SuppressLint("HandlerLeak")
    private Handler timehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //设置按钮不可点击
            disableAllOptionButton();
            if (mapAnswerButton.isEmpty()) {
                mapAnswerButton.put(R.id.btn_answer1, btnAnswer1);
                mapAnswerButton.put(R.id.btn_answer2, btnAnswer2);
                mapAnswerButton.put(R.id.btn_answer3, btnAnswer3);
                mapAnswerButton.put(R.id.btn_answer4, btnAnswer4);
            }
            String from = getIntent().getStringExtra("from");
            if ("common".equals(from)) {
                //普通答题
                applyAnswer(getIntent().getLongExtra("groupId", 0));
            } else if ("random".equals(from)) {
                //随机题目
                randomGroup();
            }
            /**
             * 给选择设置监听
             */
            optionToChoose();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        //标题
        type = getIntent().getIntExtra("type", 0);
        name = getIntent().getStringExtra("name");
        tvCommonTitle.setText(name);

        //头像手机号
        ivHeadMine.setImageResource(icon[getIntent().getIntExtra("my_head", 0) - 1]);
        tvMinePhone.setText(getIntent().getStringExtra("my_phone"));
        ivHeadIt.setImageResource(icon[getIntent().getIntExtra("it_head", 0)]);// - 1
        tvItPhone.setText(getIntent().getStringExtra("it_phone"));

        //底部图片动画
        ivBottom.setVisibility(View.VISIBLE);
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.image_alpha);
        ivBottom.setAnimation(mAnimation);
        mAnimation.start();

        //生成题目倒计时
        timehandler.sendEmptyMessageDelayed(0, 4000);
        CountDownTimer timer = new CountDownTimer(4 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                tvCountDown.setText(String.format("%d", millisUntilFinished / 1000));
                TiaoZiView tiaoziView = new TiaoZiView(tvAnim, "...", 300);//调用构造方法，直接开启

                //隐藏
                hideRadioButton();

                //隐藏顶部布局
                rlCountdown.setVisibility(View.GONE);
                rlRightBg.setVisibility(View.GONE);
                rlLeftBg.setVisibility(View.GONE);
                pbProgress.setVisibility(View.GONE);
            }

            @Override
            public void onFinish() {
                rlCountDown.setVisibility(View.GONE);

                //显示
                showRadioButton();
                //隐藏顶部布局
                rlCountdown.setVisibility(View.VISIBLE);
                rlRightBg.setVisibility(View.VISIBLE);
                rlLeftBg.setVisibility(View.VISIBLE);
                pbProgress.setVisibility(View.VISIBLE);
            }
        }.start();

    }

    //隐藏
    private void hideRadioButton() {
        btnAnswer1.setVisibility(View.GONE);
        btnAnswer2.setVisibility(View.GONE);
        btnAnswer3.setVisibility(View.GONE);
        btnAnswer4.setVisibility(View.GONE);
    }

    //显示
    private void showRadioButton() {
        btnAnswer1.setVisibility(View.VISIBLE);
        btnAnswer2.setVisibility(View.VISIBLE);
        btnAnswer3.setVisibility(View.VISIBLE);
        btnAnswer4.setVisibility(View.VISIBLE);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_against;
    }


    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                backAnswerHint();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        backAnswerHint();
    }

    /**
     * 正在答题的时候,用户点击了返回键
     */
    private void backAnswerHint() {
        new CircleDialog.Builder(this)
                .setTitle("提示")
                .setText("答题未结束,真的要离开么?")
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setPositive("继续答题", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

                    }
                })
                .setNegative("离开", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(timehandler)) {
                            timehandler.removeCallbacksAndMessages(null);
                        }
                        finish();
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

    /**
     * 申请答题
     */
    private void applyAnswer(Long groupId) {
        RemotingEx.doRequest(AppConst.AGAINST_APPLY_ANSWER, RemotingEx.Builder().applyAnswer(groupId, 1, type), this);
    }

    /**
     * 随机题目
     */
    private void randomGroup() {
        RemotingEx.doRequest(AppConst.AGAINST_RANDOM_ANSWER, RemotingEx.Builder().randomGroup(type), this);
    }

    private void initTimer() {
        if (timer != null) {
            timer.cancel();
        }
        final int timeout = applyAnswer.getTimeout() + 1;//加一操作
        if (position == questionsBeans.size()) {
            //答题完成
        } else {
            //倒计时进度条
            showProgress();
        }

        if (timeout > 0) {
            timer = new CountDownTimer(timeout * 1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    tvCountdown_main.setText(String.format("%d", millisUntilFinished / 1000));
                    if (millisUntilFinished < 6000) {
                        CircleProgressView.updateColor();
                    }
                }

                @Override
                public void onFinish() {
                    //超时未答
                    //选择正确答案按钮,改变一下背景
                    synchronized (mContext) {
                        disableAllOptionButton(); // 不允许用户进行任何选择了
                        ++position;
                        // 进入下一题
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animation alphaAnimation = new AlphaAnimation(1, 0);
                                alphaAnimation.setDuration(100);
                                alphaAnimation.setInterpolator(new LinearInterpolator());
                                alphaAnimation.setRepeatCount(1);
                                alphaAnimation.setRepeatMode(Animation.REVERSE);

                                //用户背景闪烁
                                if (user_isSelect && user_checked == 1) {
                                    //用户答对
                                    rlLeftBg.setBackgroundResource(R.mipmap.lanse1);
                                } else {
                                    rlLeftBg.setBackgroundResource(R.mipmap.hongse2);
                                }
                                rlLeftBg.startAnimation(alphaAnimation);

                                //机器人背景闪烁
                                String oks = "";
                                oks = questionsBeans.get(position - 1).getOks();
                                //私钥解密
                                try {
                                    PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                                    byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oks), private_key);
                                    oks = new String(decryptByte);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (robot_isSelcet && answerId.equals(oks)) {
                                    //机器人答对
                                    rlRightBg.setBackgroundResource(R.mipmap.lanse2);
                                } else {
                                    rlRightBg.setBackgroundResource(R.mipmap.hongse1);
                                }
                                rlRightBg.startAnimation(alphaAnimation);

                                //用户未答 机器人已答
                                if (!user_isSelect && robot_isSelcet) {
                                    //用户选项id
                                    userOptionId = "0";
                                    user_checked = 0;
                                    userCont = applyAnswer.getTimeout() + 1;
                                    showRobotSelect();
                                } else if (user_isSelect && robot_isSelcet) {
                                    //用户已答 机器人已答
                                    showRobotSelect();
                                } else {
                                    //1、用户答 机器人没答
                                    //2、双方都没答

                                    //显示正确选项
                                    showRightSelect();

                                    //进入下一题
                                    enterNext();

                                }
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (position == questionsBeans.size()) {
                                            //答题完成
                                        } else {
                                            //倒计时进度条
                                            showProgress();
                                        }
                                    }
                                }, 2000);
                            }
                        }, 100);

                    }
                }
            };
        } else {
        }
    }

    //显示正确选项
    private void showRightSelect() {
        String oksIndex = "";
        oksIndex = questionsBeans.get(position - 1).getOksIndex();
        //私钥解密
        try {
            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oksIndex), private_key);
            oksIndex = new String(decryptByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //显示正确答案
        if (oksIndex.equals("0")) {
            btnAnswer1.setTextColor(Color.parseColor("#41B1EF"));
        } else if (oksIndex.equals("1")) {
            btnAnswer2.setTextColor(Color.parseColor("#41B1EF"));
        } else if (oksIndex.equals("2")) {
            btnAnswer3.setTextColor(Color.parseColor("#41B1EF"));
        } else if (oksIndex.equals("3")) {
            btnAnswer4.setTextColor(Color.parseColor("#41B1EF"));
        }
    }


    //显示机器人选项并显示正确选项
    private void showRobotSelect() {

        //显示正确选项
        showRightSelect();

        String oks = "";
        oks = questionsBeans.get(position - 1).getOks();
        //私钥解密
        try {
            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oks), private_key);
            oks = new String(decryptByte);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //显示机器人选项
        if (answer.equals("0")) {
            if (answerId.equals(oks)) {
                btn1LeftIv.setImageResource(R.mipmap.lanse);
                btnAnswer1.setTextColor(Color.parseColor("#41B1EF"));
                tvSelectA.setTextColor(Color.parseColor("#ffffff"));
            } else {
                btn1LeftIv.setImageResource(R.mipmap.hongse);
                btnAnswer1.setTextColor(Color.parseColor("#F16F77"));
                tvSelectA.setTextColor(Color.parseColor("#ffffff"));
            }
        } else if (answer.equals("1")) {
            if (answerId.equals(oks)) {
                btn2LeftIv.setImageResource(R.mipmap.lanse);
                btnAnswer2.setTextColor(Color.parseColor("#41B1EF"));
                tvSelectB.setTextColor(Color.parseColor("#ffffff"));
            } else {
                btn2LeftIv.setImageResource(R.mipmap.hongse);
                btnAnswer2.setTextColor(Color.parseColor("#F16F77"));
                tvSelectB.setTextColor(Color.parseColor("#ffffff"));
            }
        } else if (answer.equals("2")) {
            if (answerId.equals(oks)) {
                btn3LeftIv.setImageResource(R.mipmap.lanse);
                btnAnswer3.setTextColor(Color.parseColor("#41B1EF"));
                tvSelectC.setTextColor(Color.parseColor("#ffffff"));
            } else {
                btn3LeftIv.setImageResource(R.mipmap.hongse);
                btnAnswer3.setTextColor(Color.parseColor("#F16F77"));
                tvSelectC.setTextColor(Color.parseColor("#ffffff"));
            }
        } else if (answer.equals("3")) {
            if (answerId.equals(oks)) {
                btn4LeftIv.setImageResource(R.mipmap.lanse);
                btnAnswer4.setTextColor(Color.parseColor("#41B1EF"));
                tvSelectD.setTextColor(Color.parseColor("#ffffff"));
            } else {
                btn4LeftIv.setImageResource(R.mipmap.hongse);
                btnAnswer4.setTextColor(Color.parseColor("#F16F77"));
                tvSelectD.setTextColor(Color.parseColor("#ffffff"));
            }
        }

        //进入下一题
        enterNext();

    }

    //进入下一题
    private void enterNext() {
        if (position == questionsBeans.size()) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    //最后一题
                    questionId = questionsBeans.get(position - 1).getId();
                    last = 1;
                    //获取与机器人对战数据
                    RobotInputAgainstBean robotInputAgainstBean = new RobotInputAgainstBean();
                    robotInputAgainstBean.setType(type);
                    robotInputAgainstBean.setQuestionId(questionId);
                    robotInputAgainstBean.setUserScore(userScore);
                    robotInputAgainstBean.setUserOptionId(userOptionId);
                    robotInputAgainstBean.setRobotScore(robotScore);
                    robotInputAgainstBean.setRobotId(robotId);
                    robotInputAgainstBean.setIsLast(1);
                    robotInputAgainstBean.setUserConst(0.0);
                    robotInputAgainstBean.setQuestionArry("");
                    robotInputAgainstBean.setRobotOptionId(robotOptionId);
                    robotInputAgainstBean.setExamId(fail_id);
                    getRobotAgainst(robotInputAgainstBean);
                }
            }, 2000);

        } else {
            // 延时一定时间后进入下一题: 等待2秒
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    nextReplyQuestion();
                }
            }, 2000);
        }
    }

    private Long questionId = 0L; //上一题目题目id
    private int userScore = 0; //用户积分
    private int robotScore = 0; //机器人积分
    private String robotId = ""; //机器人id
    private String userOptionId = "0"; //用户选项id
    private Long nextQuestionId = 0L; //当前题目id
    private double userCont = 0; //用户耗时
    private String robotOptionId = "";

    //请求下一题数据
    private void nextReplyQuestion() {
        robot_isSelcet = false;
        user_isSelect = false;
        rlLeftBg.setBackgroundResource(R.mipmap.huisebanyuan);
        rlRightBg.setBackgroundResource(R.mipmap.huisebanyuan1);

        if (!EncodeAndStringTool.isListEmpty(questionsBeans)) {

            //最后一题
            if (position == questionsBeans.size() - 1) {
                //TODO  position-1
                questionsBean = questionsBeans.get(position);
                questionArray = "";
                for (int i = 0; i < questionsBean.getOptions().size(); i++) {
                    questionArray = questionArray + "," + questionsBean.getOptions().get(i).getId();
                }
                Log.e("array", questionArray);

                questionId = questionsBeans.get(position - 1).getId();

                //当前题目id
                nextQuestionId = questionsBean.getId();

                isLast = 0;

                robotOptionId = answerId;

                //获取与机器人对战数据
                RobotInputAgainstBean robotInputAgainstBean = new RobotInputAgainstBean();
                robotInputAgainstBean.setType(type);
                robotInputAgainstBean.setQuestionId(questionId);
                robotInputAgainstBean.setUserScore(userScore);
                robotInputAgainstBean.setRobotScore(robotScore);
                robotInputAgainstBean.setRobotId(robotId);
                robotInputAgainstBean.setIsLast(isLast);
                robotInputAgainstBean.setUserOptionId(userOptionId);
                robotInputAgainstBean.setNextQuestionId(nextQuestionId);
                robotInputAgainstBean.setUserConst(userCont);
                robotInputAgainstBean.setRobotOptionId(robotOptionId);
                robotInputAgainstBean.setExamId(fail_id);
                robotInputAgainstBean.setQuestionArry(questionArray.substring(1, questionArray.length()));
                getRobotAgainst(robotInputAgainstBean);

            } else if (position == questionsBeans.size()) {
                //判题
                questionId = questionsBeans.get(position - 1).getId();

                last = 1;

                //获取与机器人对战数据
                RobotInputAgainstBean robotInputAgainstBean = new RobotInputAgainstBean();
                robotInputAgainstBean.setType(type);
                robotInputAgainstBean.setQuestionId(questionId);
                robotInputAgainstBean.setUserScore(userScore);
                robotInputAgainstBean.setUserOptionId(userOptionId);
                robotInputAgainstBean.setRobotScore(robotScore);
                robotInputAgainstBean.setRobotId(robotId);
                robotInputAgainstBean.setIsLast(1);
                robotInputAgainstBean.setUserConst(0.0);
                robotInputAgainstBean.setQuestionArry("");
                robotInputAgainstBean.setRobotOptionId(robotOptionId);
                robotInputAgainstBean.setExamId(fail_id);
                getRobotAgainst(robotInputAgainstBean);

            } else {
                questionsBean = questionsBeans.get(position);
                questionArray = "";
                for (int i = 0; i < questionsBean.getOptions().size(); i++) {
                    questionArray = questionArray + "," + questionsBean.getOptions().get(i).getId();
                }
                Log.e("array", questionArray);

                if (position == 0) {
                    //上一题目的id
                    questionId = Long.valueOf(0);
                } else {
                    //上一题目的id
                    questionId = questionsBeans.get(position - 1).getId();
                }

                robotOptionId = answerId;

                //当前题目id
                nextQuestionId = questionsBean.getId();

                //获取与机器人对战数据
                RobotInputAgainstBean robotInputAgainstBean = new RobotInputAgainstBean();
                robotInputAgainstBean.setType(type);
                robotInputAgainstBean.setQuestionId(questionId);
                robotInputAgainstBean.setUserScore(userScore);
                robotInputAgainstBean.setRobotScore(robotScore);
                robotInputAgainstBean.setRobotId(robotId);
                robotInputAgainstBean.setUserOptionId(userOptionId);
                robotInputAgainstBean.setIsLast(isLast);
                robotInputAgainstBean.setNextQuestionId(nextQuestionId);
                robotInputAgainstBean.setUserConst(userCont);
                robotInputAgainstBean.setExamId(fail_id);
                robotInputAgainstBean.setRobotOptionId(robotOptionId);
                robotInputAgainstBean.setQuestionArry(questionArray.substring(1, questionArray.length()));
                getRobotAgainst(robotInputAgainstBean);
            }


        }
    }

    //清除所有选中项
    private void removeSelect() {
        btnAnswer1.setChecked(false);
        btnAnswer2.setChecked(false);
        btnAnswer3.setChecked(false);
        btnAnswer4.setChecked(false);
        rlSelectA.setEnabled(true);
        rlSelectB.setEnabled(true);
        rlSelectC.setEnabled(true);
        rlSelectD.setEnabled(true);
        tvSelectA.setTextColor(Color.parseColor("#151515"));
        tvSelectB.setTextColor(Color.parseColor("#151515"));
        tvSelectC.setTextColor(Color.parseColor("#151515"));
        tvSelectD.setTextColor(Color.parseColor("#151515"));
        btnAnswer1.setTextColor(Color.parseColor("#666666"));
        btnAnswer2.setTextColor(Color.parseColor("#666666"));
        btnAnswer3.setTextColor(Color.parseColor("#666666"));
        btnAnswer4.setTextColor(Color.parseColor("#666666"));
        btn1LeftIv.setImageResource(R.mipmap.huse);
        btn2LeftIv.setImageResource(R.mipmap.huse);
        btn3LeftIv.setImageResource(R.mipmap.huse);
        btn4LeftIv.setImageResource(R.mipmap.huse);
    }

    //答题倒计时进度条
    private void showProgress() {
        CircleProgressView.returnColor();
        //倒计时进度条
        ValueAnimator animator = ValueAnimator.ofFloat(0, 100);
        animator.setDuration(applyAnswer.getTimeout() * 1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float current = (float) animation.getAnimatedValue();
                circleView.setmCurrent((int) current);
            }
        });
        animator.start();
    }

    //设置radiobutton不可点击
    private void disableAllOptionButton() {
        for (RadioButton btn : mapAnswerButton.values()) {
            btn.setEnabled(false);
        }
    }

    /**
     * 给选项设置监听
     */
    private void optionToChoose() {

        btnAnswer1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // 该选项被选中了
                    // 把所有的选项都设置为不可点击
                    disableAllOptionButton();
                    ApplyAnswer.QuestionsBean.OptionsBean option = questionsBeans.get(position).getOptions().get(0);
                    if (!EncodeAndStringTool.isObjectEmpty(option)) {
                        String oks = "";
                        oks = questionsBeans.get(position).getOks();
                        //私钥解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oks), private_key);
                            oks = new String(decryptByte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String oksIndex = "";
                        oksIndex = questionsBeans.get(position).getOksIndex();
                        //私钥解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oksIndex), private_key);
                            oksIndex = new String(decryptByte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("选项", option.toString() + "    " + oks);
                        if (oks.equals(String.valueOf(option.getId()))) {
                            //答题正确
                            btn1LeftIv.setImageResource(R.mipmap.lanse);
                            tvSelectA.setTextColor(Color.parseColor("#ffffff"));
                            btnAnswer1.setTextColor(Color.parseColor("#41B1EF"));
                            user_checked = 1;
                        } else {
                            //答题错误
                            btn1LeftIv.setImageResource(R.mipmap.hongse);
                            tvSelectA.setTextColor(Color.parseColor("#ffffff"));
                            btnAnswer1.setTextColor(Color.parseColor("#F16F77"));
                            user_checked = 2;
                        }

                        //选项后其他选项置灰
                        tvSelectB.setTextColor(Color.parseColor("#D1D1D1"));
                        tvSelectC.setTextColor(Color.parseColor("#D1D1D1"));
                        tvSelectD.setTextColor(Color.parseColor("#D1D1D1"));
                        btn2LeftIv.setImageResource(R.mipmap.huse);
                        btn3LeftIv.setImageResource(R.mipmap.huse);
                        btn4LeftIv.setImageResource(R.mipmap.huse);
                        btnAnswer2.setTextColor(Color.parseColor("#D1D1D1"));
                        btnAnswer3.setTextColor(Color.parseColor("#D1D1D1"));
                        btnAnswer4.setTextColor(Color.parseColor("#D1D1D1"));

                        //用户选项信息
                        userOptionId = String.valueOf(option.getId());
                        userCont = applyAnswer.getTimeout() - Double.valueOf(tvCountdown_main.getText().toString());
                        user_isSelect = true;

                    }

                }
            }
        });

        btnAnswer2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // 该选项被选中了
                    // 把所有的选项都设置为不可点击
                    disableAllOptionButton();
                    ApplyAnswer.QuestionsBean.OptionsBean option = questionsBeans.get(position).getOptions().get(1);
                    if (!EncodeAndStringTool.isObjectEmpty(option)) {
                        String oks = "";
                        oks = questionsBeans.get(position).getOks();
                        //私钥解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oks), private_key);
                            oks = new String(decryptByte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String oksIndex = "";
                        oksIndex = questionsBeans.get(position).getOksIndex();
                        //私钥解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oksIndex), private_key);
                            oksIndex = new String(decryptByte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("选项", option.toString() + "    " + oks);
                        if (oks.equals(String.valueOf(option.getId()))) {
                            //答题正确
                            btn2LeftIv.setImageResource(R.mipmap.lanse);
                            tvSelectB.setTextColor(Color.parseColor("#ffffff"));
                            btnAnswer2.setTextColor(Color.parseColor("#41B1EF"));
                            user_checked = 1;
                        } else {
                            //答题错误
                            btn2LeftIv.setImageResource(R.mipmap.hongse);
                            tvSelectB.setTextColor(Color.parseColor("#ffffff"));
                            btnAnswer2.setTextColor(Color.parseColor("#F16F77"));
                            user_checked = 2;
                        }

                        //选项后其他选项置灰
                        tvSelectA.setTextColor(Color.parseColor("#D1D1D1"));
                        tvSelectC.setTextColor(Color.parseColor("#D1D1D1"));
                        tvSelectD.setTextColor(Color.parseColor("#D1D1D1"));
                        btn1LeftIv.setImageResource(R.mipmap.huse);
                        btn3LeftIv.setImageResource(R.mipmap.huse);
                        btn4LeftIv.setImageResource(R.mipmap.huse);
                        btnAnswer1.setTextColor(Color.parseColor("#D1D1D1"));
                        btnAnswer3.setTextColor(Color.parseColor("#D1D1D1"));
                        btnAnswer4.setTextColor(Color.parseColor("#D1D1D1"));

                        //用户选项信息
                        userOptionId = String.valueOf(option.getId());
                        userCont = applyAnswer.getTimeout() - Double.valueOf(tvCountdown_main.getText().toString());
                        user_isSelect = true;

                    }

                }
            }
        });

        btnAnswer3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // 该选项被选中了
                    // 把所有的选项都设置为不可点击
                    disableAllOptionButton();
                    ApplyAnswer.QuestionsBean.OptionsBean option = questionsBeans.get(position).getOptions().get(2);
                    if (!EncodeAndStringTool.isObjectEmpty(option)) {
                        String oks = "";
                        oks = questionsBeans.get(position).getOks();
                        //私钥解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oks), private_key);
                            oks = new String(decryptByte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String oksIndex = "";
                        oksIndex = questionsBeans.get(position).getOksIndex();
                        //私钥解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oksIndex), private_key);
                            oksIndex = new String(decryptByte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("选项", option.toString() + "    " + oks);
                        if (oks.equals(String.valueOf(option.getId()))) {
                            //答题正确
                            btn3LeftIv.setImageResource(R.mipmap.lanse);
                            tvSelectC.setTextColor(Color.parseColor("#ffffff"));
                            btnAnswer3.setTextColor(Color.parseColor("#41B1EF"));
                            user_checked = 1;
                        } else {
                            //答题错误
                            btn3LeftIv.setImageResource(R.mipmap.hongse);
                            tvSelectC.setTextColor(Color.parseColor("#ffffff"));
                            btnAnswer3.setTextColor(Color.parseColor("#F16F77"));
                            user_checked = 2;
                        }

                        //选项后其他选项置灰
                        tvSelectA.setTextColor(Color.parseColor("#D1D1D1"));
                        tvSelectB.setTextColor(Color.parseColor("#D1D1D1"));
                        tvSelectD.setTextColor(Color.parseColor("#D1D1D1"));
                        btn1LeftIv.setImageResource(R.mipmap.huse);
                        btn2LeftIv.setImageResource(R.mipmap.huse);
                        btn4LeftIv.setImageResource(R.mipmap.huse);
                        btnAnswer1.setTextColor(Color.parseColor("#D1D1D1"));
                        btnAnswer2.setTextColor(Color.parseColor("#D1D1D1"));
                        btnAnswer4.setTextColor(Color.parseColor("#D1D1D1"));

                        //用户选项信息
                        userOptionId = String.valueOf(option.getId());
                        userCont = applyAnswer.getTimeout() - Double.valueOf(tvCountdown_main.getText().toString());
                        user_isSelect = true;

                    }

                }
            }
        });

        btnAnswer4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {

                    // 该选项被选中了
                    // 把所有的选项都设置为不可点击
                    disableAllOptionButton();
                    ApplyAnswer.QuestionsBean.OptionsBean option = questionsBeans.get(position).getOptions().get(3);
                    if (!EncodeAndStringTool.isObjectEmpty(option)) {
                        String oks = "";
                        oks = questionsBeans.get(position).getOks();
                        //私钥解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oks), private_key);
                            oks = new String(decryptByte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        String oksIndex = "";
                        oksIndex = questionsBeans.get(position).getOksIndex();
                        //私钥解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(oksIndex), private_key);
                            oksIndex = new String(decryptByte);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.e("选项", option.toString() + "    " + oks);
                        if (oks.equals(String.valueOf(option.getId()))) {
                            //答题正确
                            btn4LeftIv.setImageResource(R.mipmap.lanse);
                            tvSelectD.setTextColor(Color.parseColor("#ffffff"));
                            btnAnswer4.setTextColor(Color.parseColor("#41B1EF"));
                            user_checked = 1;
                        } else {
                            //答题错误
                            btn4LeftIv.setImageResource(R.mipmap.hongse);
                            tvSelectD.setTextColor(Color.parseColor("#ffffff"));
                            btnAnswer4.setTextColor(Color.parseColor("#F16F77"));
                            user_checked = 2;
                        }

                        //选项后其他选项置灰
                        tvSelectA.setTextColor(Color.parseColor("#D1D1D1"));
                        tvSelectB.setTextColor(Color.parseColor("#D1D1D1"));
                        tvSelectC.setTextColor(Color.parseColor("#D1D1D1"));
                        btn1LeftIv.setImageResource(R.mipmap.huse);
                        btn2LeftIv.setImageResource(R.mipmap.huse);
                        btn3LeftIv.setImageResource(R.mipmap.huse);
                        btnAnswer1.setTextColor(Color.parseColor("#D1D1D1"));
                        btnAnswer2.setTextColor(Color.parseColor("#D1D1D1"));
                        btnAnswer3.setTextColor(Color.parseColor("#D1D1D1"));

                        //用户选项信息
                        userOptionId = String.valueOf(option.getId());
                        userCont = applyAnswer.getTimeout() - Double.valueOf(tvCountdown_main.getText().toString());
                        user_isSelect = true;

                    }

                }
            }
        });

    }

    /**
     * 机器人对战
     */
    private void getRobotAgainst(final RobotInputAgainstBean robotInputAgainstBean) {
        String inputbean = JSON.toJSONString(robotInputAgainstBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);

        RemotingEx.doRequest(AppConst.AGAINST_ROBOT, RemotingEx.Builder().against(body), this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse listDataResponse) {
        if (AppConst.AGAINST_APPLY_ANSWER.equals(action)) { //用户申请答题
            String err = listDataResponse.getErr();
            if (listDataResponse.isSuccees()) {
                applyAnswer = (ApplyAnswer) listDataResponse.getDat();
                if (!EncodeAndStringTool.isObjectEmpty(applyAnswer)) {
                    if (!EncodeAndStringTool.isStringEmpty(applyAnswer.getId()) && !EncodeAndStringTool.isListEmpty(applyAnswer.getQuestions())) {
                        fail_id = applyAnswer.getId();
                        questionsBeans = applyAnswer.getQuestions();
                        answerNum = questionsBeans.size(); // 以实际题目为准

                        if (answerNum == 0) {
                            finish();
                            return;
                        }
                        position = 0;

                        //用户选项id
                        userOptionId = "0";
                        user_checked = 0;

                        robot_isSelcet = false;
                        user_isSelect = false;

                        nextReplyQuestion();
                    }

                } else {
                    finish();
                }
            } else {
                if (err.equals("E0002")) {
                    ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
                    finish();
                    return;
                } else {
                    ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
                }
            }
        } else if (AppConst.AGAINST_RANDOM_ANSWER.equals(action)) { //随机题目
            String err = listDataResponse.getErr();
            if (listDataResponse.isSuccees()) {
                applyAnswer = (ApplyAnswer) listDataResponse.getDat();
                if (!EncodeAndStringTool.isObjectEmpty(applyAnswer)) {
                    if (!EncodeAndStringTool.isStringEmpty(applyAnswer.getId()) && !EncodeAndStringTool.isListEmpty(applyAnswer.getQuestions())) {
                        fail_id = applyAnswer.getId();
                        questionsBeans = applyAnswer.getQuestions();
                        answerNum = questionsBeans.size(); // 以实际题目为准

                        if (answerNum == 0) {
                            finish();
                            return;
                        }
                        position = 0;
                        //用户选项id
                        userOptionId = "0";
                        user_checked = 0;


                        robot_isSelcet = false;
                        user_isSelect = false;
                        nextReplyQuestion();
                    }

                } else {
                    finish();
                }

            } else {
                if (err.equals("E0002")) {
                    ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
                    finish();
                    return;
                } else {
                    ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
                }
            }
        } else if (AppConst.AGAINST_ROBOT.equals(action)) { //与机器人对战
            if (listDataResponse.isSuccees()) {
                robotShowBean = (RobotShowBean) listDataResponse.getDat();
                if (!EncodeAndStringTool.isObjectEmpty(robotShowBean)) {
                    Log.e("返回参数", robotShowBean.toString());

                    robot_isSelcet = false;
                    user_isSelect = false;

                    //机器人是否答题
                    if ("0".equals(robotShowBean.getAnswerId())) {
                        robot_isSelcet = false;
                    } else {
                        robot_isSelcet = true;
                    }

                    if (last == 1) {   //最后一题

                        userScore = robotShowBean.getUserScore();
                        robotScore = robotShowBean.getRobotScore();
                        finish();
                        Intent intent = new Intent(mContext, AgainstResultActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("name", name);
                        intent.putExtra("my_head", getIntent().getIntExtra("my_head", 0));
                        intent.putExtra("my_phone", getIntent().getStringExtra("my_phone"));
                        intent.putExtra("it_head", getIntent().getIntExtra("it_head", 0));
                        intent.putExtra("it_phone", getIntent().getStringExtra("it_phone"));
                        intent.putExtra("score", getIntent().getStringExtra("score"));
                        intent.putExtra("my_score", userScore);
                        intent.putExtra("it_score", robotScore);
                        intent.putExtra("answer_id", fail_id);//答题id
                        startActivity(intent);

                    } else {
                        if (!EncodeAndStringTool.isStringEmpty(robotShowBean.getAnswer())) {
                            answer = robotShowBean.getAnswer();
                        } else {
                            answer = "";
                        }
                        answerId = robotShowBean.getAnswerId();
                        robotId = robotShowBean.getRobotId();
                        userScore = robotShowBean.getUserScore();
                        robotScore = robotShowBean.getRobotScore();
                        timeConsuming = robotShowBean.getTimeConsuming();

                        //积分显示
                        tvMineScore.setText(userScore + "");
                        tvItScore.setText(robotScore + "");

                        initTimer();

                        // 展现题目
                        questionsBean = questionsBeans.get(position);

                        //题目标题RSA解密
                        try {
                            PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                            byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(questionsBean.getTitle()), private_key);
                            String title = new String(decryptByte);
                            tvQuestion.setText(title + "");
                            Log.e("题目标题", title);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //选项
                        List<ApplyAnswer.QuestionsBean.OptionsBean> options = questionsBean.getOptions();
                        if (!EncodeAndStringTool.isListEmpty(options)) {
                            int answerIds[] = {R.id.btn_answer1, R.id.btn_answer2, R.id.btn_answer3, R.id.btn_answer4};
                            int index = 0;
                            for (ApplyAnswer.QuestionsBean.OptionsBean option : options) {
                                int id = answerIds[index];
                                ++index;
                                RadioButton btn = mapAnswerButton.get(id);
                                btn.setVisibility(View.VISIBLE);
                                //选项名称解密
                                try {
                                    PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                                    byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(option.getTitle()), private_key);
                                    String title = new String(decryptByte);
                                    btn.setText(title + "");
                                    Log.e("选项标题", title);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                btn.setEnabled(true);
                            }
                            /**
                             * 如果选项长度小于4个,隐藏掉多余的radioButton
                             */
                            for (int i = index; i < answerIds.length; ++i) {
                                int id = answerIds[i];
                                RadioButton btn = mapAnswerButton.get(id);
                                btn.setVisibility(View.INVISIBLE);
                            }

                            //左侧环形图片显示隐藏
                            if (options.size() == 1) {
                                mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                rlA.setAnimation(mAnimation);
                                mAnimation.start();
                                rlSelectA.setVisibility(View.VISIBLE);
                                rlSelectB.setVisibility(View.GONE);
                                rlSelectC.setVisibility(View.GONE);
                                rlSelectD.setVisibility(View.GONE);
                            } else if (options.size() == 2) {
                                mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                rlA.setAnimation(mAnimation);
                                mAnimation.start();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                        rlB.setAnimation(mAnimation);
                                        mAnimation.start();
                                    }
                                }, 100);
                                rlSelectA.setVisibility(View.VISIBLE);
                                rlSelectB.setVisibility(View.VISIBLE);
                                rlSelectC.setVisibility(View.GONE);
                                rlSelectD.setVisibility(View.GONE);
                            } else if (options.size() == 3) {
                                mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                rlA.setAnimation(mAnimation);
                                mAnimation.start();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                        rlB.setAnimation(mAnimation);
                                        mAnimation.start();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                                rlC.setAnimation(mAnimation);
                                                mAnimation.start();
                                            }
                                        }, 100);
                                    }
                                }, 100);
                                rlSelectA.setVisibility(View.VISIBLE);
                                rlSelectB.setVisibility(View.VISIBLE);
                                rlSelectC.setVisibility(View.VISIBLE);
                                rlSelectD.setVisibility(View.GONE);
                            } else if (options.size() == 4) {
                                mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                rlA.setAnimation(mAnimation);
                                mAnimation.start();
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                        rlB.setAnimation(mAnimation);
                                        mAnimation.start();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                                rlC.setAnimation(mAnimation);
                                                mAnimation.start();
                                                new Handler().postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        mAnimation = AnimationUtils.loadAnimation(mContext, R.anim.select_alpha);
                                                        rlD.setAnimation(mAnimation);
                                                        mAnimation.start();

                                                    }
                                                }, 100);
                                            }
                                        }, 100);
                                    }
                                }, 100);
                                rlSelectA.setVisibility(View.VISIBLE);
                                rlSelectB.setVisibility(View.VISIBLE);
                                rlSelectC.setVisibility(View.VISIBLE);
                                rlSelectD.setVisibility(View.VISIBLE);
                            }


                            //清除所有选中项
                            removeSelect();

                            //答题进度
                            tvPageEnd.setText((position + 1) + "/" + answerNum + "题");

                            //答题进度条
                            int progress = (int) (((position + 1) / (double) answerNum) * 100);
                            pbProgress.setProgress(progress);

                            // 等待用户答题
                            if (timer != null) {
                                // 启动定时器
                                timer.start();
                            }
                        }
                    }
                }

            } else {
                ErrorCodeTools.errorCodePrompt(mContext, listDataResponse.getErr(), listDataResponse.getMsg());
            }
        }
    }
}

