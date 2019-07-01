package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.WithdrawResultBigGroupAdapter;
import com.shuyun.qapp.adapter.WithdrawResultSamllGroupAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.WithdrawResultGroupBean;
import com.shuyun.qapp.manager.MyActivityManager1;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ObjectUtil;
import com.shuyun.qapp.view.GridSpacingItemDecoration;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新提现结果
 */

public class NewWithdrawResultActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_rsult)
    TextView tvRsult;
    @BindView(R.id.ll_group)
    LinearLayout llGroup;
    @BindView(R.id.rv_big_group)
    RecyclerView rvBigGroup;
    @BindView(R.id.rv_small_group)
    RecyclerView rvSmallGroup;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this;

        tvCommonTitle.setText("提现结果");

        ivBack.setOnClickListener(this);

        tvRsult.setText(getIntent().getStringExtra("remark"));

        MyActivityManager1.getInstance().pushOneActivity(this);


        //获取题组信息
        getGroupInfo();

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_new_withdraw_result;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                MyActivityManager1.getInstance().popOneActivity(this);
                break;
            default:
                break;
        }
    }

    //获取题组信息
    private void getGroupInfo() {
        RemotingEx.doRequest("getGroupInfo", RemotingEx.Builder().withdrawResultGroupList(), this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse response) {
        if ("getGroupInfo".equals(action)) {
            try {
                WithdrawResultGroupBean withdrawResultGroupBean = ObjectUtil.cast(response.getDat());
                List<WithdrawResultGroupBean.BigBean> bigBeanList = withdrawResultGroupBean.getBig();
                List<WithdrawResultGroupBean.SmallBean> smallBeanList = withdrawResultGroupBean.getSmall();
                if (!EncodeAndStringTool.isListEmpty(bigBeanList) || !EncodeAndStringTool.isListEmpty(smallBeanList)) {
                    llGroup.setVisibility(View.VISIBLE);
                    if (!EncodeAndStringTool.isListEmpty(bigBeanList)) {
                        //大图题组显示
                        rvBigGroup.setHasFixedSize(true);
                        rvBigGroup.setNestedScrollingEnabled(false);
                        WithdrawResultBigGroupAdapter withdrawResultBigGroupAdapter = new WithdrawResultBigGroupAdapter(bigBeanList, mContext);
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext) {
                            @Override
                            public boolean canScrollVertically() { //禁止layout垂直滑动
                                return false;
                            }
                        };
                        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                        rvBigGroup.setLayoutManager(linearLayoutManager);
                        rvBigGroup.setAdapter(withdrawResultBigGroupAdapter);
                    }

                    if (!EncodeAndStringTool.isListEmpty(smallBeanList)) {
                        //小图题组显示
                        rvSmallGroup.setHasFixedSize(true);
                        rvSmallGroup.setNestedScrollingEnabled(false);
                        WithdrawResultSamllGroupAdapter withdrawResultSamllGroupAdapter = new WithdrawResultSamllGroupAdapter(smallBeanList, mContext);
                        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2) {
                            @Override
                            public boolean canScrollVertically() {//禁止layout垂直滑动
                                return false;
                            }
                        };
                        rvSmallGroup.setLayoutManager(gridLayoutManager);
                        rvSmallGroup.setAdapter(withdrawResultSamllGroupAdapter);
                        rvSmallGroup.addItemDecoration(new GridSpacingItemDecoration(2, ConvertUtils.dp2px(10), false));
                    }

                } else {
                    llGroup.setVisibility(View.GONE);
                }
            } catch (Exception e) {

            }
        }
    }
}
