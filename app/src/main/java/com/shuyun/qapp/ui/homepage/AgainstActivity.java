package com.shuyun.qapp.ui.homepage;

import android.animation.ValueAnimator;
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
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.google.gson.Gson;
import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.ApplyAnswer;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.RobotInputAgainstBean;
import com.shuyun.qapp.bean.RobotShowBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.Base64Utils;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RSAUtils;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.view.CircleImageView;
import com.shuyun.qapp.view.CircleProgressView;
import com.shuyun.qapp.view.TiaoZiView;
import com.umeng.analytics.MobclickAgent;

import java.security.PrivateKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;


/**
 * 答题对战页面
 */

public class AgainstActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.btn_answer1)
    RadioButton btnAnswer1;
    @BindView(R.id.btn_answer2)
    RadioButton btnAnswer2;
    @BindView(R.id.btn_answer3)
    RadioButton btnAnswer3;
    @BindView(R.id.btn_answer4)
    RadioButton btnAnswer4;
    @BindView(R.id.pb_progress)
    ProgressBar pbProgress;
    @BindView(R.id.tv_page_end)
    TextView tvPageEnd;//第几题,共有多少题
    @BindView(R.id.tv_question)
    TextView tvQuestion;//题目
    @BindView(R.id.tv_countdown)
    TextView tvCountdown_main;//倒计时
    @BindView(R.id.ll_exam_image)
    LinearLayout llExamImage;
    @BindView(R.id.rl_count_down)
    RelativeLayout rlCountDown;//倒计时背景
    @BindView(R.id.tv_count_down)
    TextView tvCountDown;//倒计时文字
    @BindView(R.id.circle_view)
    CircleProgressView circleView;
    @BindView(R.id.tv_anim)
    TextView tvAnim;
    @BindView(R.id.iv_head_mine)
    CircleImageView ivHeadMine;
    @BindView(R.id.tv_mine_phone)
    TextView tvMinePhone;
    @BindView(R.id.tv_mine_score)
    TextView tvMineScore;
    @BindView(R.id.iv_head_it)
    CircleImageView ivHeadIt;
    @BindView(R.id.tv_it_phone)
    TextView tvItPhone;
    @BindView(R.id.tv_it_score)
    TextView tvItScore;
    @BindView(R.id.btn1_left_iv)
    ImageView btn1LeftIv;
    @BindView(R.id.btn2_left_iv)
    ImageView btn2LeftIv;
    @BindView(R.id.btn3_left_iv)
    ImageView btn3LeftIv;
    @BindView(R.id.btn4_left_iv)
    ImageView btn4LeftIv;
    @BindView(R.id.ll_question_options)
    LinearLayout llQuestionOptions;
    private static final String TAG = "AgainstActivity";
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

    private Handler timehandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            disableAllOptionButton();
            if (mapAnswerButton.isEmpty()) {
                mapAnswerButton.put(R.id.btn_answer1, btnAnswer1);
                mapAnswerButton.put(R.id.btn_answer2, btnAnswer2);
                mapAnswerButton.put(R.id.btn_answer3, btnAnswer3);
                mapAnswerButton.put(R.id.btn_answer4, btnAnswer4);
            }

            if (getIntent().getStringExtra("from").equals("common")) {
                //普通答题
                applyAnswer(getIntent().getIntExtra("groupId", 0));
            } else if (getIntent().getStringExtra("from").equals("random")) {
                //随机题目
                randomGroup();
            }
            /**
             * 给选择设置监听
             */
            optionToChoose();
        }
    };

    int type;

    int user_checked = 0;

    String fail_id = ""; //错题id

    //头像图标
    private int[] icon = new int[]{R.mipmap.header02, R.mipmap.header03, R.mipmap.header04,
            R.mipmap.header05, R.mipmap.header06, R.mipmap.header07, R.mipmap.header08, R.mipmap.header09};

    String questionArray = "";

    int isLast = 1;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        //标题
        type = getIntent().getIntExtra("type", 0);
        name = getIntent().getStringExtra("name");
        tvCommonTitle.setText(name);

        //头像手机号
        ivHeadMine.setImageResource(icon[getIntent().getIntExtra("my_head", 0) - 1]);
        tvMinePhone.setText(getIntent().getStringExtra("my_phone"));
        ivHeadIt.setImageResource(icon[getIntent().getIntExtra("it_head", 0)]);// - 1
        tvItPhone.setText(getIntent().getStringExtra("it_phone"));

        timehandler.sendEmptyMessageDelayed(0, 4000);
        CountDownTimer timer = new CountDownTimer(4 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tvCountDown.setText(String.format("%d", millisUntilFinished / 1000));
                TiaoZiView tiaoziView = new TiaoZiView(tvAnim, "...", 300);//调用构造方法，直接开启

                //隐藏 直接显示或隐藏ll_question_options
                hideRadioButton();
            }

            @Override
            public void onFinish() {
                rlCountDown.setVisibility(View.GONE);

                //显示  直接显示或隐藏 ll_question_options
                showRadioButton();
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
                .setPositive("离开", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (!EncodeAndStringTool.isObjectEmpty(timehandler)) {
                            timehandler.removeCallbacksAndMessages(null);
                        }
                        finish();
                    }
                })
                .setNegative("继续答题", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {

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
    private void applyAnswer(int groupId) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.applyAnswer(groupId, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<ApplyAnswer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<ApplyAnswer> listDataResponse) {
                        String err = listDataResponse.getErr();
                        if (listDataResponse.isSuccees()) {
                            applyAnswer = listDataResponse.getDat();
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
                                ErrorCodeTools.errorCodePrompt(AgainstActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                                finish();
                                return;
                            } else {
                                ErrorCodeTools.errorCodePrompt(AgainstActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                            }
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
     * 随机题目
     */
    private void randomGroup() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.randomGroup(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<ApplyAnswer>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<ApplyAnswer> listDataResponse) {
                        String err = listDataResponse.getErr();
                        if (listDataResponse.isSuccees()) {
                            applyAnswer = listDataResponse.getDat();
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
                                ErrorCodeTools.errorCodePrompt(AgainstActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                                finish();
                                return;
                            } else {
                                ErrorCodeTools.errorCodePrompt(AgainstActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                            }
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

    private Handler handler = new Handler();
    private Map<Integer, RadioButton> mapAnswerButton = new HashMap<>(); // 选项的映射：key：btnAnswer1~4.id

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
                    synchronized (AgainstActivity.this) {
                        disableAllOptionButton(); // 不允许用户进行任何选择了
                        ++position;
                        // 进入下一题
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //用户选项id
                                userOptionId = "0";
                                user_checked = 0;

                                userCont = applyAnswer.getTimeout() + 1;

                                //用户未答 机器人已答
                                if (user_isSelect == false && robot_isSelcet == true) {
                                    showRobotSelect();
                                } else {
                                    nextReplyQuestion();
                                }

                            }
                        }, 100); // 100毫秒后进入下一题

                        if (position == questionsBeans.size()) {
                            //答题完成
                        } else {
                            //倒计时进度条
                            showProgress();
                        }
                    }
                }
            };
        } else {
        }
    }


    //用户未答 机器人已答 展示机器人选项
    private void showRobotSelect() {

        if (user_isSelect == false && robot_isSelcet == true) {
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
            if (!answerId.equals(oks)) {
                if (oksIndex.equals("0")) {
                    btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                    btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                } else if (oksIndex.equals("1")) {
                    btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                    btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                } else if (oksIndex.equals("2")) {
                    btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                    btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                } else if (oksIndex.equals("3")) {
                    btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                    btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                }
            }

            //显示机器人选项
            if (answer.equals("0")) {
                btn1LeftIv.setVisibility(View.VISIBLE);
                btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                if (answerId.equals(oks)) {
                    btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                    btn1LeftIv.setImageResource(R.mipmap.btn_logo_success);
                } else {
                    btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_2);
                    btn1LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                }
            } else if (answer.equals("1")) {
                btn2LeftIv.setVisibility(View.VISIBLE);
                btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                if (answerId.equals(oks)) {
                    btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                    btn2LeftIv.setImageResource(R.mipmap.btn_logo_success);
                } else {
                    btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_2);
                    btn2LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                }
            } else if (answer.equals("2")) {
                btn3LeftIv.setVisibility(View.VISIBLE);
                btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                if (answerId.equals(oks)) {
                    btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                    btn3LeftIv.setImageResource(R.mipmap.btn_logo_success);
                } else {
                    btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_2);
                    btn3LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                }
            } else if (answer.equals("3")) {
                btn4LeftIv.setVisibility(View.VISIBLE);
                btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                if (answerId.equals(oks)) {
                    btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                    btn4LeftIv.setImageResource(R.mipmap.btn_logo_success);
                } else {
                    btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_2);
                    btn4LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                }
            }

            if (position == questionsBeans.size()) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
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
                }, 500);

            } else {

                // 延时一定时间后进入下一题: 等待5毫秒
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        nextReplyQuestion();
                        handler1.removeCallbacks(runnable);
                    }
                }, 500);
            }


        }
    }

    private int questionId = 0; //上一题目题目id
    private int userScore = 0; //用户积分
    private int robotScore = 0; //机器人积分
    private String robotId = ""; //机器人id
    private String userOptionId = "0"; //用户选项id
    private int nextQuestionId = 0; //当前题目id
    private double userCont = 0; //用户耗时
    private String robotOptionId = "";

    private void nextReplyQuestion() {

        robot_isSelcet = false;
        user_isSelect = false;

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
                    questionId = 0;
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
        btnAnswer1.setTextColor(Color.parseColor("#227fc5"));
        btnAnswer2.setTextColor(Color.parseColor("#227fc5"));
        btnAnswer3.setTextColor(Color.parseColor("#227fc5"));
        btnAnswer4.setTextColor(Color.parseColor("#227fc5"));
        btnAnswer1.setBackgroundResource(R.drawable.radiobutton_select2);
        btnAnswer2.setBackgroundResource(R.drawable.radiobutton_select2);
        btnAnswer3.setBackgroundResource(R.drawable.radiobutton_select2);
        btnAnswer4.setBackgroundResource(R.drawable.radiobutton_select2);
        btn1LeftIv.setVisibility(View.GONE);
        btn2LeftIv.setVisibility(View.GONE);
        btn3LeftIv.setVisibility(View.GONE);
        btn4LeftIv.setVisibility(View.GONE);
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
                            btnAnswer1.setBackgroundResource(R.drawable.radiobutton_select);  //答题正确
                            btn1LeftIv.setVisibility(View.VISIBLE);
                            btn1LeftIv.setImageResource(R.mipmap.btn_logo_success);
                            btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                            user_checked = 1;
                        } else {
                            btnAnswer1.setBackgroundResource(R.drawable.radiobutton_select2);  //答题错误
                            btn1LeftIv.setVisibility(View.VISIBLE);
                            btn1LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                            btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                            user_checked = 2;
                        }

                        Animation alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(400);
                        alphaAnimation.setInterpolator(new LinearInterpolator());
                        alphaAnimation.setRepeatCount(2);
                        alphaAnimation.setRepeatMode(Animation.REVERSE);
                        ivHeadMine.startAnimation(alphaAnimation);
                        tvMineScore.startAnimation(alphaAnimation);

                        //用户选项信息
                        userOptionId = String.valueOf(option.getId());
                        userCont = applyAnswer.getTimeout() - Double.valueOf(tvCountdown_main.getText().toString());
                        user_isSelect = true;


                        if (timeConsuming == -1.0) {

                        } else {

                            if (robot_isSelcet == true) {

                                handler1.removeCallbacks(runnable);

                                //双方都错
                                if (user_checked == 2 && !answerId.equals(oks)) {
                                    if (oksIndex.equals("0")) {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("1")) {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("2")) {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("3")) {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                                    }
                                }

                                //显示机器人选项
                                if (answer.equals("0")) {
                                    btn1LeftIv.setVisibility(View.VISIBLE);
                                    if (user_checked == 1) {
                                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                } else if (answer.equals("1")) {
                                    btn2LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }

                                } else if (answer.equals("2")) {
                                    btn3LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }

                                } else if (answer.equals("3")) {
                                    btn4LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                }

                                // 延时一定时间后进入下一题: 等待5毫秒
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ++position;
                                        nextReplyQuestion();
                                    }
                                }, 1000);
                            }
                        }

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
                            btnAnswer2.setBackgroundResource(R.drawable.radiobutton_select);  //答题正确
                            btn2LeftIv.setVisibility(View.VISIBLE);
                            btn2LeftIv.setImageResource(R.mipmap.btn_logo_success);
                            btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                            user_checked = 1;
                        } else {
                            btnAnswer2.setBackgroundResource(R.drawable.radiobutton_select2);  //答题错误
                            btn2LeftIv.setVisibility(View.VISIBLE);
                            btn2LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                            btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                            user_checked = 2;
                        }

                        Animation alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(400);
                        alphaAnimation.setInterpolator(new LinearInterpolator());
                        alphaAnimation.setRepeatCount(2);
                        alphaAnimation.setRepeatMode(Animation.REVERSE);
                        ivHeadMine.startAnimation(alphaAnimation);
                        tvMineScore.startAnimation(alphaAnimation);

                        //用户选项信息
                        userOptionId = String.valueOf(option.getId());
                        userCont = applyAnswer.getTimeout() - Double.valueOf(tvCountdown_main.getText().toString());
                        user_isSelect = true;

                        if (timeConsuming == -1.0) {

                        } else {

                            if (robot_isSelcet == true) {

                                handler1.removeCallbacks(runnable);

                                //双方都错
                                if (user_checked == 2 && !answerId.equals(oks)) {
                                    if (oksIndex.equals("0")) {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("1")) {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("2")) {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("3")) {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                                    }
                                }

                                //显示机器人选项
                                if (answer.equals("0")) {
                                    btn1LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                } else if (answer.equals("1")) {
                                    btn2LeftIv.setVisibility(View.VISIBLE);
                                    if (user_checked == 1) {
                                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }

                                } else if (answer.equals("2")) {
                                    btn3LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }

                                } else if (answer.equals("3")) {
                                    btn4LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                }


                                // 延时一定时间后进入下一题: 等待5毫秒
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ++position;
                                        nextReplyQuestion();
                                    }
                                }, 1000);

                            }
                        }
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
                            btnAnswer3.setBackgroundResource(R.drawable.radiobutton_select);  //答题正确
                            btn3LeftIv.setVisibility(View.VISIBLE);
                            btn3LeftIv.setImageResource(R.mipmap.btn_logo_success);
                            btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                            user_checked = 1;
                        } else {
                            btnAnswer3.setBackgroundResource(R.drawable.radiobutton_select2); //答题错误
                            btn3LeftIv.setVisibility(View.VISIBLE);
                            btn3LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                            btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                            user_checked = 2;
                        }

                        Animation alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(400);
                        alphaAnimation.setInterpolator(new LinearInterpolator());
                        alphaAnimation.setRepeatCount(2);
                        alphaAnimation.setRepeatMode(Animation.REVERSE);
                        ivHeadMine.startAnimation(alphaAnimation);
                        tvMineScore.startAnimation(alphaAnimation);

                        //用户选项信息
                        userOptionId = String.valueOf(option.getId());
                        userCont = applyAnswer.getTimeout() - Double.valueOf(tvCountdown_main.getText().toString());
                        user_isSelect = true;

                        if (timeConsuming == -1.0) {

                        } else {

                            if (robot_isSelcet == true) {

                                handler1.removeCallbacks(runnable);

                                //双方都错
                                if (user_checked == 2 && !answerId.equals(oks)) {
                                    if (oksIndex.equals("0")) {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("1")) {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("2")) {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("3")) {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                                    }
                                }

                                //显示机器人选项
                                if (answer.equals("0")) {
                                    btn1LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                } else if (answer.equals("1")) {
                                    btn2LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }

                                } else if (answer.equals("2")) {
                                    btn3LeftIv.setVisibility(View.VISIBLE);
                                    if (user_checked == 1) {
                                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }

                                } else if (answer.equals("3")) {
                                    btn4LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                }

                                // 延时一定时间后进入下一题: 等待5毫秒
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ++position;
                                        nextReplyQuestion();
                                    }
                                }, 1000);
                            }
                        }
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
                            btnAnswer4.setBackgroundResource(R.drawable.radiobutton_select);  //答题正确
                            btn4LeftIv.setVisibility(View.VISIBLE);
                            btn4LeftIv.setImageResource(R.mipmap.btn_logo_success);
                            btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                            user_checked = 1;
                        } else {
                            btnAnswer4.setBackgroundResource(R.drawable.radiobutton_select2); //答题错误
                            btn4LeftIv.setVisibility(View.VISIBLE);
                            btn4LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                            btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                            user_checked = 2;
                        }

                        Animation alphaAnimation = new AlphaAnimation(1, 0);
                        alphaAnimation.setDuration(400);
                        alphaAnimation.setInterpolator(new LinearInterpolator());
                        alphaAnimation.setRepeatCount(2);
                        alphaAnimation.setRepeatMode(Animation.REVERSE);
                        ivHeadMine.startAnimation(alphaAnimation);
                        tvMineScore.startAnimation(alphaAnimation);

                        //用户选项信息
                        userOptionId = String.valueOf(option.getId());
                        userCont = applyAnswer.getTimeout() - Double.valueOf(tvCountdown_main.getText().toString());
                        user_isSelect = true;

                        if (timeConsuming == -1.0) {

                        } else {

                            if (robot_isSelcet == true) {  //机器人答过

                                handler1.removeCallbacks(runnable);

                                //双方都错
                                if (user_checked == 2 && !answerId.equals(oks)) {
                                    if (oksIndex.equals("0")) {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("1")) {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("2")) {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                                    } else if (oksIndex.equals("3")) {
                                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                                    }
                                }

                                //显示机器人选项
                                if (answer.equals("0")) {
                                    btn1LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                } else if (answer.equals("1")) {
                                    btn2LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                } else if (answer.equals("2")) {
                                    btn3LeftIv.setVisibility(View.VISIBLE);
                                    btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                                    if (answerId.equals(oks) && user_checked == 2) {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_2);
                                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                } else if (answer.equals("3")) {
                                    btn4LeftIv.setVisibility(View.VISIBLE);
                                    if (user_checked == 1) {
                                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_success);
                                    } else {
                                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                                    }
                                }

                                // 延时一定时间后进入下一题: 等待5毫秒
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        ++position;
                                        nextReplyQuestion();
                                    }
                                }, 1000);

                            }
                        }
                    }

                }
            }
        });

    }

    /**
     * 机器人对战
     */
    RobotShowBean robotShowBean;

    private String answer = ""; //机器人选择的答案

    private double timeConsuming = 0;//机器人答题耗时

    private String answerId = "";  //机器人选项

    int last = 0;

    private boolean robot_isSelcet = false;

    private boolean user_isSelect = false;

    private Handler handler1 = new Handler();

    Runnable runnable = new Runnable() {
        @Override
        public void run() {

            //用户已答 机器人已答
            if (user_isSelect == true && robot_isSelcet == true) {

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

                //显示正确答案
                if (user_checked == 2 && !answerId.equals(oks)) {
                    if (oksIndex.equals("0")) {
                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                        btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                    } else if (oksIndex.equals("1")) {
                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                        btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                    } else if (oksIndex.equals("2")) {
                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                        btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                    } else if (oksIndex.equals("3")) {
                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                        btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                    }
                }

                //显示机器人选项
                if (answer.equals("0")) {
                    btn1LeftIv.setVisibility(View.VISIBLE);
                    btnAnswer1.setTextColor(Color.parseColor("#ffffff"));
                    if (answerId.equals(oks)) {
                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_1);
                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_success);
                    } else {
                        btnAnswer1.setBackgroundResource(R.drawable.rbtn_bg_2);
                        btn1LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                    }
                } else if (answer.equals("1")) {
                    btn2LeftIv.setVisibility(View.VISIBLE);
                    btnAnswer2.setTextColor(Color.parseColor("#ffffff"));
                    if (answerId.equals(oks)) {
                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_1);
                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_success);
                    } else {
                        btnAnswer2.setBackgroundResource(R.drawable.rbtn_bg_2);
                        btn2LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                    }
                } else if (answer.equals("2")) {
                    btn3LeftIv.setVisibility(View.VISIBLE);
                    btnAnswer3.setTextColor(Color.parseColor("#ffffff"));
                    if (answerId.equals(oks)) {
                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_1);
                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_success);
                    } else {
                        btnAnswer3.setBackgroundResource(R.drawable.rbtn_bg_2);
                        btn3LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                    }
                } else if (answer.equals("3")) {
                    btn4LeftIv.setVisibility(View.VISIBLE);
                    btnAnswer4.setTextColor(Color.parseColor("#ffffff"));
                    if (answerId.equals(oks)) {
                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_1);
                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_success);
                    } else {
                        btnAnswer4.setBackgroundResource(R.drawable.rbtn_bg_2);
                        btn4LeftIv.setImageResource(R.mipmap.btn_logo_fail);
                    }
                }

                // 延时一定时间后进入下一题: 等待5毫秒
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ++position;
                        nextReplyQuestion();
                        handler1.removeCallbacks(runnable);
                    }
                }, 500);
            }

            handler1.postDelayed(runnable, 500);
        }
    };

    private void getRobotAgainst(final RobotInputAgainstBean robotInputAgainstBean) {
        ApiService apiService = BasePresenter.create(8000);
        String inputbean = new Gson().toJson(robotInputAgainstBean);
        Log.i(TAG, "show_input: " + robotInputAgainstBean.toString());
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        apiService.against(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<RobotShowBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<RobotShowBean> listDataResponse) {
                        Log.i(TAG, "onNext1: " + listDataResponse.toString());
                        if (listDataResponse.isSuccees()) {
                            robotShowBean = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(robotShowBean)) {
                                Log.e("返回参数", robotShowBean.toString());

                                robot_isSelcet = false;
                                user_isSelect = false;

                                //机器人答题耗时
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        robot_isSelcet = true;
                                        Animation alphaAnimation = new AlphaAnimation(1, 0);
                                        alphaAnimation.setDuration(400);
                                        alphaAnimation.setInterpolator(new LinearInterpolator());
                                        alphaAnimation.setRepeatCount(2);
                                        alphaAnimation.setRepeatMode(Animation.REVERSE);
                                        ivHeadIt.startAnimation(alphaAnimation);
                                        tvItScore.startAnimation(alphaAnimation);
                                    }
                                }, (int) robotShowBean.getTimeConsuming() * 1000);

                                handler1.post(runnable);

                                if (last == 1) {   //最后一题

                                    userScore = robotShowBean.getUserScore();
                                    robotScore = robotShowBean.getRobotScore();
                                    finish();
                                    Intent intent = new Intent(AgainstActivity.this, AgainstResultActivity.class);
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
                                            btn.setBackgroundResource(R.drawable.radiobutton_select2);
                                        }
                                        /**
                                         * 如果选项长度小于4个,隐藏掉多余的radioButton
                                         */
                                        for (int i = index; i < answerIds.length; ++i) {
                                            int id = answerIds[i];
                                            RadioButton btn = mapAnswerButton.get(id);
                                            btn.setVisibility(View.INVISIBLE);
                                            btn.setBackgroundResource(R.drawable.radiobutton_select2);
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
                            ErrorCodeTools.errorCodePrompt(AgainstActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
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


    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
        }
    }
}

