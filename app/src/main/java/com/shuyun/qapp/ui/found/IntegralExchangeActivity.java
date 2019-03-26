package com.shuyun.qapp.ui.found;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.params.DialogParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.FoundGiftExchangeAdapter;
import com.shuyun.qapp.adapter.FoundPropsExchangeAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.ScoreExchangeBeans;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 发现---积分兑换
 */

public class IntegralExchangeActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener<Object> {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.rv_props)
    RecyclerView rvProps;
    @BindView(R.id.rv_gift)
    RecyclerView rvGift;
    @BindView(R.id.ll_prop)
    LinearLayout llProp;
    @BindView(R.id.ll_gift)
    LinearLayout llGift;

    private List<ScoreExchangeBeans.PropsBean> propsBeanList = new ArrayList<>();
    private FoundPropsExchangeAdapter foundPropsExchangeAdapter;

    private List<ScoreExchangeBeans.PresentsBean> presentsBeanList = new ArrayList<>();
    private FoundGiftExchangeAdapter foundGiftExchangeAdapter;

    private Context mContext;
    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        tvCommonTitle.setText("积分兑换");
        ivBack.setOnClickListener(this);

        //初始化适配器
        foundPropsExchangeAdapter = new FoundPropsExchangeAdapter(propsBeanList, mContext);
        foundPropsExchangeAdapter.setOnItemClickLitsener(new FoundPropsExchangeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                final ScoreExchangeBeans.PropsBean propsBean = propsBeanList.get(position);
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //道具兑换
                        ExchangeDialog(propsBean.getId());
                    }
                }, 0);
            }
        });
        GridLayoutManager gridLayoutManager1 = new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager1.setSmoothScrollbarEnabled(true);
        gridLayoutManager1.setAutoMeasureEnabled(true);
        rvProps.setLayoutManager(gridLayoutManager1);
        //解决数据加载不完的问题
        rvProps.setHasFixedSize(true);
        rvProps.setNestedScrollingEnabled(false);
        rvProps.setAdapter(foundPropsExchangeAdapter);

        foundGiftExchangeAdapter = new FoundGiftExchangeAdapter(presentsBeanList, mContext);
        foundGiftExchangeAdapter.setOnItemClickLitsener(new FoundGiftExchangeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                ToastUtil.showToast(mContext, "兑换成功");
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {//禁止layout垂直滑动
                return false;
            }
        };
        gridLayoutManager.setSmoothScrollbarEnabled(true);
        gridLayoutManager.setAutoMeasureEnabled(true);
        rvGift.setLayoutManager(gridLayoutManager);
        //解决数据加载不完的问题
        rvGift.setHasFixedSize(true);
        rvGift.setNestedScrollingEnabled(false);
        rvGift.setAdapter(foundGiftExchangeAdapter);

        //获取数据
        loadHomeData();
    }


    @Override
    public int intiLayout() {
        return R.layout.activity_integral_exchange2;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            default:
                break;
        }
    }

    /**
     * 获取商品列表
     */
    private void loadHomeData() {
        RemotingEx.doRequest("getGoodsList", ApiServiceBean.scoreExchange(), null, this);
    }

    /**
     * 道具兑换
     */
    private void propExchange(String goodsId) {
        RemotingEx.doRequest("propExchange", ApiServiceBean.scoreExchangeApply(), new Object[]{goodsId}, this);
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

        if ("getGoodsList".equals(action)) {
            try {
                ScoreExchangeBeans scoreExchangeBeans = (ScoreExchangeBeans) response.getDat();
                //道具
                List<ScoreExchangeBeans.PropsBean> propsBeans = scoreExchangeBeans.getProps();
                if (!EncodeAndStringTool.isListEmpty(propsBeans)) {
                    llProp.setVisibility(View.VISIBLE);
                    propsBeanList.clear();
                    propsBeanList.addAll(propsBeans);
                    foundPropsExchangeAdapter.notifyDataSetChanged();
                } else {
                    llProp.setVisibility(View.GONE);
                }

                //礼品
                List<ScoreExchangeBeans.PresentsBean> presentsBeans = scoreExchangeBeans.getPresents();
                if (!EncodeAndStringTool.isListEmpty(presentsBeans)) {
                    llGift.setVisibility(View.VISIBLE);
                    presentsBeanList.clear();
                    presentsBeanList.addAll(presentsBeans);
                    foundGiftExchangeAdapter.notifyDataSetChanged();
                } else {
                    llGift.setVisibility(View.GONE);
                }

            } catch (Exception e) {

            }

        } else if ("propExchange".equals(action)) {
            Toast.makeText(mContext, "道具兑换成功", Toast.LENGTH_SHORT).show();
        }
    }

    //兑换道具弹框
    private void ExchangeDialog(final String goodsId) {
        new CircleDialog.Builder(this)
                .setTitle("提示")
                .setText("你确定要兑换该道具吗？")
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative("取消", null)
                .setPositive("确定", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        propExchange(goodsId);
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
}
