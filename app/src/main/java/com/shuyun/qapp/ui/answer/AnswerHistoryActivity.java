package com.shuyun.qapp.ui.answer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AnswerHistoryAdapter;
import com.shuyun.qapp.adapter.GroupTreeAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.HistoryDataBean;
import com.shuyun.qapp.bean.LookAnswerResultBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.mine.ShareAnswerRecordActivity;
import com.shuyun.qapp.utils.Base64Utils;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RSAUtils;
import com.shuyun.qapp.utils.ToastUtil;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 答题历史
 */
public class AnswerHistoryActivity extends BaseActivity implements CommonPopupWindow.ViewInterface, OnRemotingCallBackListener<Object> {

    @BindView(R.id.ll_answer_history)
    RelativeLayout llAnswerHistory;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //答题标题
    @BindView(R.id.rv_error_answer)
    RecyclerView rvErrorAnswer; //recycleview
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvTime)
    TextView tvTime;
    @BindView(R.id.tvRate)
    TextView tvRate;
    @BindView(R.id.tvClass)
    TextView tvClass;
    @BindView(R.id.ivLevel)
    ImageView ivLevel;
    @BindView(R.id.cardView)
    CardView cardView;

    CommonPopupWindow popupWindow;

    LookAnswerResultBean lookAnswerResult;

    private String answerId; //答题id

    private BigDecimal a = new BigDecimal("85");
    private BigDecimal b = new BigDecimal("50");

    private String from = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        answerId = intent.getStringExtra("answer_id");
//        String title = intent.getStringExtra("title");
//        tvCommonTitle.setText(title);

        from = intent.getStringExtra("from");

        if (!EncodeAndStringTool.isStringEmpty(answerId)) {
            loadLookAnswerResult(answerId);
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_answer_history;
    }

    private Long questionId = Long.valueOf(0);

    /**
     * 查看答题结果
     *
     * @param answerId 答题Id
     */
    private void loadLookAnswerResult(String answerId) {

        RemotingEx.doRequest(AppConst.ANSWER_HISTORY, ApiServiceBean.lookAnswerResult(), new Object[]{answerId}, this);

    }

    @OnClick({R.id.iv_back, R.id.tv_share})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back: //返回键
                finish();
                break;
            case R.id.tv_share: //分享
                HistoryDataBean.ResultBean recordBean = new HistoryDataBean.ResultBean();
                recordBean.setId(lookAnswerResult.getId());
                recordBean.setTitle(lookAnswerResult.getGroupName());
                recordBean.setAccuracy(lookAnswerResult.getAccuracy());
                recordBean.setFullName(lookAnswerResult.getFullName());
                recordBean.setPicture(lookAnswerResult.getGroupPicture());
                recordBean.setTime(lookAnswerResult.getExamTime().toString());

                Bundle bundle = new Bundle();
                bundle.putSerializable("share", recordBean);
                Intent intent = new Intent(this, ShareAnswerRecordActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    int positionId = 1;

    @Override
    public void getChildView(View view, int layoutResId) {
        switch (layoutResId) {
            case R.layout.feedback_popupwindow: //反馈弹窗
                ImageView ivClose = view.findViewById(R.id.iv_close_icon2);
                final TextView commitFeedBack = view.findViewById(R.id.tv_commit_feedback);
                RadioGroup rgFeedBack = view.findViewById(R.id.rg_feedback);
                ivClose.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) {
                            popupWindow.dismiss();
                        }
                    }
                });

                rgFeedBack.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == R.id.tv_out_of_style) {
                            positionId = 1;
                        } else if (checkedId == R.id.tv_answer_error) {
                            positionId = 2;
                        } else if (checkedId == R.id.tv_incnformity) {
                            positionId = 3;
                        } else if (checkedId == R.id.tv_other) {
                            positionId = 4;
                        }
                        commitFeedBack.setOnClickListener(new OnMultiClickListener() {
                            @Override
                            public void onMultiClick(View v) {
                                loadAnswerFeedBack(positionId);
                                if ((!EncodeAndStringTool.isObjectEmpty(popupWindow)) && popupWindow.isShowing()) {
                                    popupWindow.dismiss();
                                }
                            }
                        });
                    }
                });
                commitFeedBack.setOnClickListener(new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        ToastUtil.showToast(AnswerHistoryActivity.this, "您还没有选择反馈选项,请您先选择再提交!");
                    }
                });

                break;
            default:
                break;
        }
    }

    /**
     * 答题题目反馈
     *
     * @param positionId
     */
    private void loadAnswerFeedBack(int positionId) {

        RemotingEx.doRequest(AppConst.ANSWER_FEEDBACK, ApiServiceBean.getAnswerFeedBack(), new Object[]{questionId, positionId}, this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (AppConst.ANSWER_HISTORY.equals(action)) { //答题历史
            if (response.isSuccees()) {
                lookAnswerResult = (LookAnswerResultBean) response.getDat();
                if (!EncodeAndStringTool.isObjectEmpty(lookAnswerResult)) {

                    tvCommonTitle.setText(lookAnswerResult.getGroupName());
                    if (from.equals("h5")) {
                        cardView.setVisibility(View.VISIBLE);
                        tvTitle.setText(lookAnswerResult.getGroupName());
                        Date currentTime = new Date(Long.valueOf(lookAnswerResult.getExamTime()));
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                        String time = formatter.format(currentTime).replace(" ", "\n");
                        tvTime.setText(time);
                        String fullName = lookAnswerResult.getFullName();
                        if (fullName != null) {
                            String[] names = fullName.split("/");
                            tvClass.setText(names[0]);
                        } else {
                            tvClass.setText("");
                        }

                        StringBuffer sb = new StringBuffer();
                        sb.append(lookAnswerResult.getAccuracy()).append("%");
                        tvRate.setText(sb.toString());

                        //正确率：85%以上A ，正确率50%~85% 的B，其他C吧
                        BigDecimal rate = new BigDecimal(lookAnswerResult.getAccuracy());
                        if (rate.compareTo(a) > 0) {
                            ivLevel.setImageResource(R.mipmap.a);
                        } else if (rate.compareTo(b) >= 0 && rate.compareTo(a) <= 0) {
                            ivLevel.setImageResource(R.mipmap.b);
                        } else {
                            ivLevel.setImageResource(R.mipmap.c);
                        }
                    } else {
                        cardView.setVisibility(View.GONE);
                    }


                    final List<LookAnswerResultBean.QuestionsBean> questionsBeans = lookAnswerResult.getQuestions();
                    if (!EncodeAndStringTool.isListEmpty(questionsBeans)) {
                        AnswerHistoryAdapter historyAdapter = new AnswerHistoryAdapter(AnswerHistoryActivity.this, questionsBeans);
                        historyAdapter.setOnItemClickLitsener(new GroupTreeAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                //调用CommonPopupWindow展示反馈弹窗
                                if (popupWindow != null && popupWindow.isShowing())
                                    return;
                                View upView = LayoutInflater.from(AnswerHistoryActivity.this).inflate(R.layout.feedback_popupwindow, null);
                                LookAnswerResultBean.QuestionsBean questionsBean = questionsBeans.get(position);
                                questionId = questionsBean.getId();
                                // /测量View的宽高
                                CommonPopUtil.measureWidthAndHeight(upView);
                                popupWindow = new CommonPopupWindow.Builder(AnswerHistoryActivity.this)
                                        .setView(R.layout.feedback_popupwindow)
                                        .setWidthAndHeight(upView.getMeasuredWidth(), ViewGroup.LayoutParams.WRAP_CONTENT)
                                        .setBackGroundLevel(0.5f)//取值范围0.0f-1.0f 值越小越暗
                                        .setOutsideTouchable(false)
                                        .setAnimationStyle(R.style.popwin_anim_style)//设置动画
                                        //设置子View点击事件
                                        .setViewOnclickListener(AnswerHistoryActivity.this)
                                        .create();
                                TextView tvQuestion1 = popupWindow.getContentView().findViewById(R.id.tv_question1);

                                //私钥解密
                                try {
                                    PrivateKey private_key = RSAUtils.loadPrivateKey(AppConst.private_key);
                                    byte[] decryptByte = RSAUtils.decryptDataPrivateKey(Base64Utils.decode(questionsBean.getTitle()), private_key);
                                    tvQuestion1.setText(new String(decryptByte));
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                popupWindow.showAtLocation(llAnswerHistory, Gravity.CENTER, 0, 0);
                            }
                        });
                        LinearLayoutManager layoutManager = new LinearLayoutManager(AnswerHistoryActivity.this);
                        rvErrorAnswer.setLayoutManager(layoutManager);
                        //解决数据加载不完的问题
                        rvErrorAnswer.setHasFixedSize(true);
                        rvErrorAnswer.setNestedScrollingEnabled(false);
                        rvErrorAnswer.setAdapter(historyAdapter);
                    }

                } else {
                    AnswerHistoryAdapter historyAdapter = new AnswerHistoryAdapter(AnswerHistoryActivity.this, null);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(AnswerHistoryActivity.this);
                    rvErrorAnswer.setLayoutManager(layoutManager);
                    rvErrorAnswer.setAdapter(historyAdapter);
                }
            } else {
                ErrorCodeTools.errorCodePrompt(AnswerHistoryActivity.this, response.getErr(), response.getMsg());
            }
        } else if (AppConst.ANSWER_FEEDBACK.equals(action)) { //答题反馈
            if (response.isSuccees()) {
                ToastUtil.showToast(AnswerHistoryActivity.this, "提交成功!");
                questionId = Long.valueOf(0);
            } else {
                ErrorCodeTools.errorCodePrompt(AnswerHistoryActivity.this, response.getErr(), response.getMsg());
            }
        }
    }
}
