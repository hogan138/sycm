package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.MyPropsAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MyPropsBean;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.UmengPageUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的道具
 */
public class MyPropsActivity extends BaseActivity implements OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_props)
    RecyclerView rvProps;
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;
    @BindView(R.id.ll_main)
    LinearLayout llMain;

    private MyPropsAdapter myPropsAdapter;
    private Context mContext;
    private Handler mHandler = new Handler();
    /**
     * 获取到我的道具
     */
    List<MyPropsBean> myPropsBeanList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;
        tvCommonTitle.setText("我的道具");

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        myPropsAdapter = new MyPropsAdapter(mContext, myPropsBeanList);
        myPropsAdapter.setOnItemClickLitsener(new MyPropsAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(View view, int position) {
                MyPropsBean myPropsBean = myPropsBeanList.get(position);
                String mode = myPropsBean.getPrizeMode();
                useProps(mode);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        rvProps.setLayoutManager(layoutManager);
        rvProps.setAdapter(myPropsAdapter);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadProps();
            }
        }, 10);

        //友盟页面统计
        UmengPageUtil.startPage(AppConst.APP_PERSONAL_PROP);
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_my_props;
    }

    private void loadProps() {
        RemotingEx.doRequest("MyProps", ApiServiceBean.MyProps(), null, this);
    }

    /**
     * 使用道具
     */
    private void useProps(String mode) {
        RemotingEx.doRequest("useProps", ApiServiceBean.useProps(), new Object[]{mode}, this);
    }

    @Override
    public void onCompleted(String action) {

    }

    @Override
    public void onFailed(String action, String message) {

    }

    @Override
    public void onSucceed(String action, DataResponse<Object> response) {
        if (!response.isSuccees()) {
            ErrorCodeTools.errorCodePrompt(mContext, response.getErr(), response.getMsg());
            return;
        }
        if ("MyProps".equals(action)) {
            List<MyPropsBean> minePrizeList1 = (List<MyPropsBean>) response.getDat();
            if (!EncodeAndStringTool.isListEmpty(minePrizeList1) && minePrizeList1.size() > 0) {
                ivEmpty.setVisibility(View.GONE);
                rvProps.setVisibility(View.VISIBLE);
                myPropsBeanList.clear();
                myPropsBeanList.addAll(minePrizeList1);
                myPropsAdapter.notifyDataSetChanged();
            } else {
                ivEmpty.setVisibility(View.VISIBLE);
                rvProps.setVisibility(View.INVISIBLE);
            }
        } else if ("useProps".equals(action)) {
            Toast.makeText(mContext, "道具使用成功", Toast.LENGTH_SHORT).show();
            //刷新道具列表
            loadProps();
        }
    }
}
