package com.shuyun.qapp.ui.integral;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.IntegralExchangeBean;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.mine.IntegralAccountActivity;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.UmengPageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 积分中心
 */
public class IntegralCenterActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_reduce_score)
    TextView tvReduceScore;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.ll_prize1)
    LinearLayout llPrize1;
    @BindView(R.id.ll_prize2)
    LinearLayout llPrize2;
    @BindView(R.id.rl_start_box)
    RelativeLayout rlStartBox;
    @BindView(R.id.rl_start_baby)
    RelativeLayout rlStartBaby;
    @BindView(R.id.tv_look_detail)
    TextView tvLookDetail;
    @BindView(R.id.tv_flip_card_reduce_score)
    TextView tvFlipCardReduceScore;
    @BindView(R.id.ll_card)
    LinearLayout llCard;
    @BindView(R.id.rl_flip_card)
    RelativeLayout rlFlipCard;
    @BindView(R.id.tv_score_label)
    TextView tvScoreLabel;
    @BindView(R.id.tv_overdue_score)
    TextView tvOverdueScore;

    //开宝箱h5地址
    String h5Url = "";
    //翻牌地址
    String flip_url = "";
    //积分过期url
    String overdueUrl = "";

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this;

        tvCommonTitle.setText("积分中心");
        ivBack.setOnClickListener(this);
        rlStartBox.setOnClickListener(this);
        rlStartBaby.setOnClickListener(this);
        rlFlipCard.setOnClickListener(this);
        tvLookDetail.setOnClickListener(this);
        tvOverdueScore.setOnClickListener(this);


        //友盟页面统计
        UmengPageUtil.startPage(AppConst.APP_PERSONAL_BP);
    }

    @Override
    protected void onResume() {
        super.onResume();

        //获取积分信息
        getInfo();

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_integral_exchange;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_overdue_score:
                Intent i = new Intent(mContext, WebH5Activity.class);
                i.putExtra("url", overdueUrl);
                i.putExtra("name", "全民共进");
                startActivity(i);
                break;
            case R.id.tv_look_detail:
                startActivity(new Intent(mContext, IntegralAccountActivity.class));
                break;
            case R.id.rl_start_box:
                //跳到积分抽奖界面
                Intent intent = new Intent(mContext, WebPrizeBoxActivity.class);
                intent.putExtra("main_box", "score_box");
                intent.putExtra("h5Url", h5Url);
                startActivity(intent);
                break;
            case R.id.rl_start_baby:
                startActivity(new Intent(mContext, IntegralMainActivity.class));
                break;
            case R.id.rl_flip_card:
                Intent intent1 = new Intent(mContext, WebPrizeBoxActivity.class);
                intent1.putExtra("main_box", "score_box");
                intent1.putExtra("h5Url", flip_url);
                startActivity(intent1);
                break;
            default:
                break;
        }
    }

    //获取积分信息
    private void getInfo() {

        RemotingEx.doRequest(RemotingEx.Builder().getExchangeMain(), new OnRemotingCallBackListener<IntegralExchangeBean>() {
            @Override
            public void onCompleted(String action) {

            }

            @Override
            public void onFailed(String action, String message) {

            }

            @Override
            public void onSucceed(String action, DataResponse<IntegralExchangeBean> dataResponse) {
                if (dataResponse.isSuccees()) {
                    IntegralExchangeBean integralExchangeBean = dataResponse.getDat();
                    if (!EncodeAndStringTool.isObjectEmpty(integralExchangeBean)) {

                        tvScore.setText("" + integralExchangeBean.getUserBp());
                        tvReduceScore.setText(integralExchangeBean.getLuckyConsBp() + "积分/次");
                        tvFlipCardReduceScore.setText(integralExchangeBean.getFlipConsBp() + "积分/次");

                        //开宝箱h5地址
                        h5Url = integralExchangeBean.getH5Url();

                        //积分翻牌地址
                        flip_url = integralExchangeBean.getFlipUrl();

                        //保存我的积分
                        SaveUserInfo.getInstance(IntegralCenterActivity.this).setUserInfo("my_bp", integralExchangeBean.getUserBp() + "");

                        //保存规则地址
                        SaveUserInfo.getInstance(IntegralCenterActivity.this).setUserInfo("h5_rule", integralExchangeBean.getRuleUrl());

                        if (!EncodeAndStringTool.isListEmpty(integralExchangeBean.getLuckyPicList())) {
                            //积分开宝箱图片
                            llPrize1.removeAllViews();
                            for (int i = 0; i < integralExchangeBean.getLuckyPicList().size(); i++) {
                                llPrize1.setPadding(0, 10, ConvertUtils.dp2px(15), 10);
                                llPrize1.setGravity(Gravity.CENTER_VERTICAL);
                                ImageView imageView = new ImageView(IntegralCenterActivity.this);
                                int imageWidth = ConvertUtils.dp2px(38);
                                int imageHeight = ConvertUtils.dp2px(38);
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
                                imageView.setPadding(ConvertUtils.dp2px(7), 0, 0, 0);
                                GlideUtils.LoadCircleImage(IntegralCenterActivity.this, integralExchangeBean.getLuckyPicList().get(i), imageView);
                                llPrize1.addView(imageView);
                            }
                        }

                        if (!EncodeAndStringTool.isListEmpty(integralExchangeBean.getTreasurePicList())) {
                            //积分夺宝图片
                            llPrize2.removeAllViews();
                            for (int i = 0; i < integralExchangeBean.getTreasurePicList().size(); i++) {
                                llPrize2.setPadding(0, 10, ConvertUtils.dp2px(15), 10);
                                llPrize2.setGravity(Gravity.CENTER_VERTICAL);
                                ImageView imageView = new ImageView(IntegralCenterActivity.this);
                                int imageWidth = ConvertUtils.dp2px(38);
                                int imageHeight = ConvertUtils.dp2px(38);
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
                                imageView.setPadding(ConvertUtils.dp2px(7), 0, 0, 0);
                                GlideUtils.LoadCircleImage(IntegralCenterActivity.this, integralExchangeBean.getTreasurePicList().get(i), imageView);
                                llPrize2.addView(imageView);
                            }
                        }

                        if (!EncodeAndStringTool.isStringEmpty(integralExchangeBean.getFlipPicList())) {
                            //积分翻牌
                            llCard.removeAllViews();
                            for (int i = 0; i < integralExchangeBean.getFlipPicList().size(); i++) {
                                llCard.setPadding(0, 10, ConvertUtils.dp2px(15), 10);
                                llCard.setGravity(Gravity.CENTER_VERTICAL);
                                ImageView imageView = new ImageView(IntegralCenterActivity.this);
                                int imageWidth = ConvertUtils.dp2px(38);
                                int imageHeight = ConvertUtils.dp2px(38);
                                imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
                                imageView.setPadding(ConvertUtils.dp2px(7), 0, 0, 0);
                                GlideUtils.LoadCircleImage(IntegralCenterActivity.this, integralExchangeBean.getFlipPicList().get(i), imageView);
                                llCard.addView(imageView);
                            }
                        }

                        IntegralExchangeBean.BpExpireBean bpExpireBean = integralExchangeBean.getBpExpire();
                        if (!EncodeAndStringTool.isObjectEmpty(bpExpireBean)) {
                            tvOverdueScore.setVisibility(View.VISIBLE);
                            tvScoreLabel.setVisibility(View.GONE);
                            tvOverdueScore.setText(bpExpireBean.getBp() + "积分即将过期");
                            //积分过期地址
                            overdueUrl = bpExpireBean.getH5Url();
                        } else {
                            tvOverdueScore.setVisibility(View.GONE);
                            tvScoreLabel.setVisibility(View.VISIBLE);
                        }

                    }
                } else {
                    ErrorCodeTools.errorCodePrompt(IntegralCenterActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                }
            }
        });

    }

}
