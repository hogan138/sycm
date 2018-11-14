package com.shuyun.qapp.ui.integral;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.IntegralExchangeBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.webview.WebPrizeBoxActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.GlideUtils;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 积分兑换首页
 */
public class IntegralExchangeActivity extends BaseActivity implements View.OnClickListener {

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

    //开宝箱h5地址
    String h5Url = "";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("积分兑换");
        ivBack.setOnClickListener(this);
        rlStartBox.setOnClickListener(this);
        rlStartBaby.setOnClickListener(this);

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
            case R.id.rl_start_box:
                //跳到积分抽奖界面
                Intent intent = new Intent(IntegralExchangeActivity.this, WebPrizeBoxActivity.class);
                intent.putExtra("main_box", "score_box");
                intent.putExtra("h5Url", h5Url);
                startActivity(intent);
                break;
            case R.id.rl_start_baby:
                startActivity(new Intent(IntegralExchangeActivity.this, IntegralMainActivity.class));
                break;
            default:
                break;
        }
    }

    //获取积分信息
    private void getInfo() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getExchangeMain()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<IntegralExchangeBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<IntegralExchangeBean> dataResponse) {
                        if (dataResponse.isSuccees()) {
                            IntegralExchangeBean integralExchangeBean = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(integralExchangeBean)) {

                                tvScore.setText("我的积分：" + integralExchangeBean.getUserBp());
                                tvReduceScore.setText("最低消费" + integralExchangeBean.getLuckyConsBp() + "积分");

                                //开宝箱h5地址
                                h5Url = integralExchangeBean.getH5Url();

                                //保存我的积分
                                SaveUserInfo.getInstance(IntegralExchangeActivity.this).setUserInfo("my_bp", integralExchangeBean.getUserBp() + "");

                                //保存规则地址
                                SaveUserInfo.getInstance(IntegralExchangeActivity.this).setUserInfo("h5_rule", integralExchangeBean.getRuleUrl());

                                if (!EncodeAndStringTool.isListEmpty(integralExchangeBean.getLuckyPicList())) {
                                    //积分开宝箱图片
                                    llPrize1.removeAllViews();
                                    for (int i = 0; i < integralExchangeBean.getLuckyPicList().size(); i++) {
                                        llPrize1.setPadding(ConvertUtils.dp2px(15), 10, ConvertUtils.dp2px(15), 10);
                                        llPrize1.setGravity(Gravity.CENTER_VERTICAL);
                                        ImageView imageView = new ImageView(IntegralExchangeActivity.this);
                                        int imageWidth = ConvertUtils.dp2px(40);
                                        int imageHeight = ConvertUtils.dp2px(40);
                                        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
                                        imageView.setPadding(10, 0, 0, 0);
                                        GlideUtils.LoadCircleImage(IntegralExchangeActivity.this, integralExchangeBean.getLuckyPicList().get(i), imageView);
                                        llPrize1.addView(imageView);
                                    }
                                }

                                if (!EncodeAndStringTool.isListEmpty(integralExchangeBean.getTreasurePicList())) {
                                    //积分夺宝图片
                                    llPrize2.removeAllViews();
                                    for (int i = 0; i < integralExchangeBean.getTreasurePicList().size(); i++) {
                                        llPrize2.setPadding(ConvertUtils.dp2px(15), 10, ConvertUtils.dp2px(15), 10);
                                        llPrize2.setGravity(Gravity.CENTER_VERTICAL);
                                        ImageView imageView = new ImageView(IntegralExchangeActivity.this);
                                        int imageWidth = ConvertUtils.dp2px(40);
                                        int imageHeight = ConvertUtils.dp2px(40);
                                        imageView.setLayoutParams(new LinearLayout.LayoutParams(imageWidth, imageHeight));
                                        imageView.setPadding(10, 0, 0, 0);
                                        GlideUtils.LoadCircleImage(IntegralExchangeActivity.this, integralExchangeBean.getTreasurePicList().get(i), imageView);
                                        llPrize2.addView(imageView);
                                    }
                                }


                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(IntegralExchangeActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }


                    @Override
                    public void onError(Throwable e) {
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

}
