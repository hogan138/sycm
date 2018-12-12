package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AnswerRecordAdapter;
import com.shuyun.qapp.animation.MyLayoutAnimationHelper;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AnswerRecordBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.answer.AnswerHistoryActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 成绩单
 */
public class AnswerRecordActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_answer_record)
    RecyclerView rvAnswerRecord;
    @BindView(R.id.iv_prize_empty)
    ImageView ivPrizeEmpty;//我的奖品

    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private int loadState = AppConst.STATE_NORMAL;
    private int currentPage = 0;

    AnswerRecordAdapter answerRecordAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("成绩单");

        loadAnswerRecord(currentPage);

        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_MORE;
                currentPage++;
                loadAnswerRecord(currentPage);
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                loadState = AppConst.STATE_REFRESH;
                currentPage = 0;
                loadAnswerRecord(currentPage);
            }
        });

        answerRecordAdapter = new AnswerRecordAdapter(AnswerRecordActivity.this, answerRecordBeanList);
        answerRecordAdapter.setOnItemClickLitsener(new AnswerRecordAdapter.OnItemClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                AnswerRecordBean answerRecordBean = answerRecordBeanList.get(position);
                String id = answerRecordBean.getId();
                String title = answerRecordBean.getTitle();
                if (!EncodeAndStringTool.isStringEmpty(id)) {
                    Intent intent = new Intent(AnswerRecordActivity.this, AnswerHistoryActivity.class);
                    intent.putExtra("answer_id", id);
                    intent.putExtra("title", title);
                    startActivity(intent);
                }
            }
        });
        GridLayoutManager glManager = new GridLayoutManager(AnswerRecordActivity.this, 2, LinearLayoutManager.VERTICAL, false);
        rvAnswerRecord.setLayoutManager(glManager);

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_answer_record;
    }

    @OnClick({R.id.iv_back})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(this); //统计时长
        StatService.onResume(this);
    }

    /**
     * 用户答题记录
     */

    List<AnswerRecordBean> answerRecordBeanList = new ArrayList<>();

    private void loadAnswerRecord(int currentPage) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getAnswerRecord(currentPage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<List<AnswerRecordBean>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<List<AnswerRecordBean>> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            final List<AnswerRecordBean> answerRecordBeanList1 = dataResponse.getDat();
                            if (!EncodeAndStringTool.isListEmpty(answerRecordBeanList1) && answerRecordBeanList1.size() > 0) {
                                ivPrizeEmpty.setVisibility(View.GONE);
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {//首次加載||下拉刷新
                                    answerRecordBeanList.clear();
                                    answerRecordBeanList.addAll(answerRecordBeanList1);
                                    rvAnswerRecord.setAdapter(answerRecordAdapter);
                                    refreshLayout.finishRefresh();
                                    refreshLayout.setLoadmoreFinished(false);

                                    //进入动画
//                                    LayoutAnimationController controller = new LayoutAnimationController(MyLayoutAnimationHelper.getAnimationSetScaleBig());
//                                    controller.setDelay(0.1f);
//                                    rvAnswerRecord.setLayoutAnimation(controller);
//                                    rvAnswerRecord.scheduleLayoutAnimation();

                                } else if (loadState == AppConst.STATE_MORE) {
                                    if (answerRecordBeanList1.size() == 0) {//没有数据了
                                        refreshLayout.finishLoadmore(); //
                                        refreshLayout.setLoadmoreFinished(true);
                                    } else {
                                        answerRecordBeanList.addAll(answerRecordBeanList1);
                                        answerRecordAdapter.notifyDataSetChanged();
                                        refreshLayout.finishLoadmore();
                                        refreshLayout.setLoadmoreFinished(false);
                                    }
                                }

                            } else {
                                if (loadState == AppConst.STATE_NORMAL || loadState == AppConst.STATE_REFRESH) {
                                    ivPrizeEmpty.setVisibility(View.VISIBLE);
                                }
                                refreshLayout.finishLoadmore();
                                refreshLayout.setLoadmoreFinished(true);
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(AnswerRecordActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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


    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
        StatService.onPause(this);
    }


}
