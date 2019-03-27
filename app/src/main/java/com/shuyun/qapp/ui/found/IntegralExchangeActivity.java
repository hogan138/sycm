package com.shuyun.qapp.ui.found;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mylhyl.circledialog.CircleDialog;
import com.mylhyl.circledialog.callback.ConfigButton;
import com.mylhyl.circledialog.callback.ConfigDialog;
import com.mylhyl.circledialog.callback.ConfigText;
import com.mylhyl.circledialog.callback.ConfigTitle;
import com.mylhyl.circledialog.params.ButtonParams;
import com.mylhyl.circledialog.params.DialogParams;
import com.mylhyl.circledialog.params.TextParams;
import com.mylhyl.circledialog.params.TitleParams;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.FoundGiftExchangeAdapter;
import com.shuyun.qapp.adapter.FoundPropsExchangeAdapter;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.ScoreExchangeBeans;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.classify.ClassifyActivity;
import com.shuyun.qapp.ui.homepage.HomePageActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.blankj.utilcode.util.ConvertUtils.dp2px;


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
                ScoreExchangeBeans.PropsBean propsBean = propsBeanList.get(position);
                //道具兑换
                ExchangeTipDialog("您将消耗" + propsBean.getActionLabel() + propsBean.getName(), "确认兑换", "我点错了", propsBean.getId());
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
                ToastUtil.showToast(mContext, "立即兑换");
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
            String err = response.getErr();
            if ("L0001".equals(err)) {
                ExchangeTipDialog("抱歉您的积分不足", "答题赚积分", "确认", "");
            } else {
                ErrorCodeTools.errorCodePrompt(mContext, err, response.getMsg());
            }
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
            Toast.makeText(mContext, "兑换成功", Toast.LENGTH_SHORT).show();
        }
    }

    //兑换弹框
    private void ExchangeTipDialog(String title, final String tv_right, String tv_left, final String goodsId) {
        new CircleDialog.Builder(this)
                .setTitle("提示")
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                        params.textSize = dp2px(16);
                    }
                })
                .setText(title)
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.textSize = dp2px(16);
                    }
                })
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setNegative(tv_left, null)
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#9B9B9B");
                    }
                })
                .setPositive(tv_right, new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        if (tv_right.equals("答题赚积分")) {
                            //分类页
                            mContext.startActivity(new Intent(mContext, ClassifyActivity.class));
                        } else {
                            propExchange(goodsId);
                        }
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
