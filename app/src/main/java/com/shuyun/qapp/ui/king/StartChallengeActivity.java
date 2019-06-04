package com.shuyun.qapp.ui.king;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyBankingAdapter;
import com.shuyun.qapp.adapter.StartChallengeAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.NewHomeSelectBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 开始挑战
 */
public class StartChallengeActivity extends BaseActivity implements OnRemotingCallBackListener {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_start)
    RecyclerView rvStart;

    private Context mContext;

    //开始挑战
    private List<NewHomeSelectBean.GuessBean> guessBeanList = new ArrayList<>();
    private StartChallengeAdapter startChallengeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        tvCommonTitle.setText("开始挑战");

        startChallengeAdapter = new StartChallengeAdapter(mContext, guessBeanList);
        startChallengeAdapter.setOnItemClickLitsener(new StartChallengeAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                startActivity(new Intent(mContext, KingMatchingActivity.class));
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvStart.setLayoutManager(layoutManager);
        rvStart.setHasFixedSize(true);
        rvStart.setNestedScrollingEnabled(false);
        rvStart.setAdapter(startChallengeAdapter);

        //获得精选数据
        loadSelectInfo();

    }

    /**
     * 精选数据
     */
    public void loadSelectInfo() {
        RemotingEx.doRequest("getSelectInfo", RemotingEx.Builder().homeSelectInfo(), this);
    }

    @OnClick({R.id.iv_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_start_challenge;
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
        if ("getSelectInfo".equals(action)) {
            NewHomeSelectBean newHomeSelectBean = (NewHomeSelectBean) response.getDat();
            //猜你喜欢
            List<NewHomeSelectBean.GuessBean> guessBean = newHomeSelectBean.getGuess();
            if (!EncodeAndStringTool.isListEmpty(guessBean)) {
                guessBeanList.clear();
                guessBeanList.addAll(guessBean);
                guessBeanList.addAll(guessBean);
                guessBeanList.addAll(guessBean);
                startChallengeAdapter.notifyDataSetChanged();
            }
        }
    }
}
