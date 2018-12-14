package com.shuyun.qapp.ui.answer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AnswerHistoryAdapter;
import com.shuyun.qapp.adapter.GroupTreeAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.LookAnswerResultBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.utils.Base64Utils;
import com.shuyun.qapp.utils.CommonPopUtil;
import com.shuyun.qapp.utils.CommonPopupWindow;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RSAUtils;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.ToastUtil;

import java.security.PrivateKey;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 答题历史
 */
public class AnswerHistoryActivity extends BaseActivity implements CommonPopupWindow.ViewInterface {

    @BindView(R.id.ll_answer_history)
    LinearLayout llAnswerHistory;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle; //答题标题
    @BindView(R.id.rv_error_answer)
    RecyclerView rvErrorAnswer; //recycleview

    CommonPopupWindow popupWindow;
    private String answerId; //答题id


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        answerId = intent.getStringExtra("answer_id");
        String title = intent.getStringExtra("title");
        tvCommonTitle.setText(title);
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
     * 查看答题结果 TODO adapter 中不要为数字
     *
     * @param answerId 答题Id
     */
    private void loadLookAnswerResult(String answerId) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.lookAnswerResult(answerId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<LookAnswerResultBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<LookAnswerResultBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            LookAnswerResultBean lookAnswerResult = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(lookAnswerResult)) {
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
                                    rvErrorAnswer.setAdapter(historyAdapter);
                                    //进入动画
                                    LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetFromTop());
                                    controller.setDelay(0.1f);
                                    rvErrorAnswer.setLayoutAnimation(controller);
                                    rvErrorAnswer.scheduleLayoutAnimation();
                                }

                            } else {
                                AnswerHistoryAdapter historyAdapter = new AnswerHistoryAdapter(AnswerHistoryActivity.this, null);
                                LinearLayoutManager layoutManager = new LinearLayoutManager(AnswerHistoryActivity.this);
                                rvErrorAnswer.setLayoutManager(layoutManager);
                                rvErrorAnswer.setAdapter(historyAdapter);
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(AnswerHistoryActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
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

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back: //返回键
                finish();
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
                                if (NetworkUtils.isAvailableByPing()) {
                                    loadAnswerFeedBack(positionId);
                                } else {
                                    Toast.makeText(AnswerHistoryActivity.this, "网络链接失败，请检查网络链接！", Toast.LENGTH_SHORT).show();
                                }
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
        if (questionId > 0) {
            ApiService apiService = BasePresenter.create(8000);
            apiService.getAnswerFeedBack(questionId, positionId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<DataResponse>() {
                        @Override
                        public void onSubscribe(Disposable d) {
                        }

                        @Override
                        public void onNext(DataResponse dataResponse) {
                            if (dataResponse.isSuccees()) {
                                ToastUtil.showToast(AnswerHistoryActivity.this, "提交成功!");
                                questionId = Long.valueOf(0);
                            } else {
                                ErrorCodeTools.errorCodePrompt(AnswerHistoryActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                            }

                        }

                        @Override
                        public void onError(Throwable e) {
                            questionId = Long.valueOf(0);
                            //保存错误信息
                            SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                            return;
                        }

                        @Override
                        public void onComplete() {
                            popupWindow.dismiss();
                            questionId = Long.valueOf(0);
                        }
                    });
        } else {
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
