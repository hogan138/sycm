package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.AnswerRecordAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.AnswerRecordBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.answer.AnswerHistoryActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    /**
     * 用户答题记录
     */

    List<AnswerRecordBean> answerRecordBeanList = new ArrayList<>();

    private void loadAnswerRecord(int currentPage) {
        RemotingEx.doRequest(ApiServiceBean.getAnswerRecord(), new Object[]{currentPage}, new OnRemotingCallBackListener<List<AnswerRecordBean>>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<List<AnswerRecordBean>> dataResponse) {
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
        });

    }
}
