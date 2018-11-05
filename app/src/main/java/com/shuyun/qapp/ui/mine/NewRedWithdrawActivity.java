package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.RedPacketAdapter;
import com.shuyun.qapp.adapter.RedPacketAdapter1;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputWithdrawalbean;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.OutPutWithdraw;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.ui.webview.WebPublicActivity;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ToastUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 红包提现
 */
public class NewRedWithdrawActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.iv_add_user_info)
    ImageView ivAddUserInfo;
    @BindView(R.id.tv_name_account)
    TextView tvNameAccount;
    @BindView(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @BindView(R.id.tv_money)
    EditText tvMoney;
    @BindView(R.id.rv_red_packet)
    RecyclerView rvRedPacket;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.btn_contact_our)
    Button btnContactOur;

    List<String> redIdList = new ArrayList<String>();
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    private int DELYED = 100;

    private String bankId = "";

    private String redrules = "";

    //实名信息
    String real_info = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        rlBack.setOnClickListener(this);
        tvRule.setOnClickListener(this);
        ivAddUserInfo.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        btnContactOur.setOnClickListener(this);
        tvMoney.setInputType(InputType.TYPE_NULL);
        rlUserInfo.setOnClickListener(this);

        SpannableString ss = new SpannableString("请选择您要提现的红包");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint
        tvMoney.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失


        handler.postDelayed(runnable, DELYED); //每隔1s执行


        //获取红包结合
        Bundle bundle = getIntent().getExtras();
        final String redId = bundle.getString("redId");

        if (bundle.getString("from").equals("prize")) {
            //我的奖品红包提现
            final List<MinePrize> prizeBeanList = bundle.getParcelableArrayList("redPrize");
            if (!EncodeAndStringTool.isListEmpty(prizeBeanList)) {
                for (MinePrize red : prizeBeanList) {
                    if (red.getId().equals(redId)) {
                        tvMoney.setText(new BigDecimal(red.getAmount()).toString());
                        redIdList.add(redId);
                        red.selected = true;
                        break;
                    }
                }

                RedPacketAdapter redPacketAdapter = new RedPacketAdapter(this, prizeBeanList);
                redPacketAdapter.setOnItemClickLitsener(new RedPacketAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MinePrize minePrize = prizeBeanList.get(position);
                        String redId = minePrize.getId();
                        String amount = minePrize.getAmount();
                        String money = tvMoney.getText().toString();
                        BigDecimal number = new BigDecimal(EncodeAndStringTool.isStringEmpty(money) ? "0.00" : money);
                        if (minePrize.selected) {
                            number = number.add(new BigDecimal(amount));
                            redIdList.add(redId);
                        } else {
                            number = number.subtract(new BigDecimal(amount));
                            redIdList.remove(redId);
                        }
                        tvMoney.setText(number.toString());
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rvRedPacket.setLayoutManager(layoutManager);
                rvRedPacket.setAdapter(redPacketAdapter);
            }
        } else if (bundle.getString("from").equals("box")) {
            //开宝箱提现
            final List<MinePrize.ChildMinePrize> prizeBeanList = bundle.getParcelableArrayList("redPrize");
            if (!EncodeAndStringTool.isListEmpty(prizeBeanList)) {
                for (MinePrize.ChildMinePrize red : prizeBeanList) {
                    if (red.getId().equals(redId)) {
                        tvMoney.setText(new BigDecimal(red.getAmount()).toString());
                        redIdList.add(redId);
                        red.selected = true;
                        break;
                    }
                }

                RedPacketAdapter1 redPacketAdapter = new RedPacketAdapter1(this, prizeBeanList);
                redPacketAdapter.setOnItemClickLitsener(new RedPacketAdapter1.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        MinePrize.ChildMinePrize ChildMinePrize = prizeBeanList.get(position);
                        String redId = ChildMinePrize.getId();
                        String amount = ChildMinePrize.getAmount();
                        String money = tvMoney.getText().toString();
                        BigDecimal number = new BigDecimal(EncodeAndStringTool.isStringEmpty(money) ? "0.00" : money);
                        if (ChildMinePrize.selected) {
                            number = number.add(new BigDecimal(amount));
                            redIdList.add(redId);
                        } else {
                            number = number.subtract(new BigDecimal(amount));
                            redIdList.remove(redId);
                        }
                        tvMoney.setText(number.toString());
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rvRedPacket.setLayoutManager(layoutManager);
                rvRedPacket.setAdapter(redPacketAdapter);
            }
        }


    }


    //定时器
    private Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            try {
                handler.postDelayed(this, DELYED);
                if (redIdList.size() > 0) {
                    btnEnter.setEnabled(true);
                } else {
                    btnEnter.setEnabled(false);
                    tvMoney.setText("");
                }
            } catch (Exception e) {

            }
        }
    };


    @Override
    public int intiLayout() {
        return R.layout.activity_new_red_withd;
    }

    @Override
    protected void onResume() {
        super.onResume();

        //访问个人信息
        loadMineHomeData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_rule:
                Intent i = new Intent(NewRedWithdrawActivity.this, WebBannerActivity.class);
                i.putExtra("url", redrules);
                i.putExtra("name", "红包提现");
                startActivity(i);
                break;
            case R.id.iv_add_user_info:
                startActivity(new Intent(NewRedWithdrawActivity.this, AddWithdrawInfoActivity.class));
                break;
            case R.id.rl_user_info:
                Intent intent = new Intent(this, AddWithdrawInfoActivity.class);
                intent.putExtra("info", real_info);
                startActivity(intent);
                break;
            case R.id.btn_enter:
                /**
                 * 针对哪几个红包提现,多个红包id用逗号分隔
                 * 字符串数组
                 */
                if (EncodeAndStringTool.isStringEmpty(bankId)) {
                    ToastUtil.showToast(NewRedWithdrawActivity.this, "请先完善提现信息");
                } else {
                    String moneyNumber = tvMoney.getText().toString().trim();
                    String[] redsId = new String[redIdList.size()];
                    redIdList.toArray(redsId);
                    final InputWithdrawalbean inputWithdrawalbean = new InputWithdrawalbean(moneyNumber, 2, redsId, bankId);//针对哪几个红包提现,多个红包id用逗号分隔;
                    if (redIdList.size() > 0) {
                        CustomLoadingFactory factory = new CustomLoadingFactory();
                        LoadingBar.make(rlMain, factory).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                redWithDrawal(inputWithdrawalbean);
                            }
                        }, 2000);
                    } else {
                        ToastUtil.showToast(this, "请选择提现红包金额!");
                    }
                }
                break;
            case R.id.btn_contact_our:
                Intent intent1 = new Intent(this, WebBannerActivity.class);
                intent1.putExtra("url", SaveUserInfo.getInstance(this).getUserInfo("contactUs_url"));
                intent1.putExtra("name", "联系客服");//名称 标题
                startActivity(intent1);
                break;
            default:
                break;
        }

    }

    /**
     * 红包提现
     *
     * @param inputWithdrawalbean 返回99999
     */
    private void redWithDrawal(InputWithdrawalbean inputWithdrawalbean) {
        ApiService apiService = BasePresenter.create(8000);
        final String inputbean = JSON.toJSONString(inputWithdrawalbean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        apiService.applyWithdrawal(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<OutPutWithdraw>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<OutPutWithdraw> dataResponse) {
                        LoadingBar.cancel(rlMain);
                        if (dataResponse.isSuccees()) {
                            OutPutWithdraw outPutWithdraw = dataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(outPutWithdraw)) {
                                Intent intent = new Intent(NewRedWithdrawActivity.this, WithdrawResultActivity.class);
                                intent.putExtra("from", "withdraw");
                                intent.putExtra("remark", outPutWithdraw.getRemark());
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(NewRedWithdrawActivity.this, dataResponse.getErr(), dataResponse.getMsg());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingBar.cancel(rlBack);
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    //加载个人信息
    private void loadMineHomeData() {
        ApiService apiService = BasePresenter.create(8000);
        apiService.getMineHomeData()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<MineBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<MineBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            MineBean mineBean = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                                redrules = mineBean.getRedRuleUrl();
                                try {
                                    for (int i = 0; i < mineBean.getDatas().size(); i++) {
                                        String type = mineBean.getDatas().get(i).getType();
                                        int status = mineBean.getDatas().get(i).getStatus();
                                        if (!EncodeAndStringTool.isStringEmpty(type) && "withdraw".equals(type) && status == 3) {
                                            //是否完善提现信息
                                            bankId = mineBean.getDatas().get(i).getBankId();
                                            String title = mineBean.getDatas().get(i).getTitle();
                                            real_info = title;
                                            ivAddUserInfo.setVisibility(View.GONE);
                                            rlUserInfo.setVisibility(View.VISIBLE);
                                            tvNameAccount.setText(title.substring(4, title.length()).replace("|", "   "));
                                            if (mineBean.getDatas().get(i).isEnabled()) {
                                                rlUserInfo.setEnabled(true);
                                            } else {
                                                rlUserInfo.setEnabled(false);
                                            }
                                            return;
                                        } else {
                                            ivAddUserInfo.setVisibility(View.VISIBLE);
                                            rlUserInfo.setVisibility(View.GONE);
                                        }
                                    }
                                } catch (Exception e) {

                                }
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(NewRedWithdrawActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
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
}
