package com.shuyun.qapp.ui.mine;

import android.content.Context;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.adapter.ChooseWithdrawInfoAdapter;
import com.shuyun.qapp.adapter.RedPacketAdapter;
import com.shuyun.qapp.adapter.RedPacketAdapter1;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputWithdrawalbean;
import com.shuyun.qapp.bean.MessageEvent;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.OutPutWithdraw;
import com.shuyun.qapp.net.ApiServiceBean;
import com.shuyun.qapp.net.OnRemotingCallBackListener;
import com.shuyun.qapp.net.RemotingEx;
import com.shuyun.qapp.ui.webview.WebH5Activity;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 红包提现
 */
public class NewRedWithdrawActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener<Object> {
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.tv_money)
    EditText tvMoney;
    @BindView(R.id.rv_red_packet)
    RecyclerView rvRedPacket;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.btn_contact_our)
    Button btnContactOur;

    List<String> redIdList = new ArrayList<String>();
    @BindView(R.id.rv_withdraw_info)
    RecyclerView rvWithdrawInfo;


    private int DELYED = 100;

    private String bankId = "";

    private String redrules = "";

    //实名信息
//    String real_info = "";
    private Context mContext;

    private ChooseWithdrawInfoAdapter chooseWithdrawInfoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mContext = this;

        rlBack.setOnClickListener(this);
        tvRule.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        btnContactOur.setOnClickListener(this);
        tvMoney.setInputType(InputType.TYPE_NULL);

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
        String from = bundle.getString("from");
        if ("prize".equals(from)) {
            //我的奖品红包提现
            final List<MinePrize> prizeBeanList = bundle.getParcelableArrayList("redPrize");
            if (!EncodeAndStringTool.isListEmpty(prizeBeanList)) {
                int position = 0;
                for (int i = 0; i < prizeBeanList.size(); i++) {
                    if (prizeBeanList.get(i).getId().equals(redId)) {
                        tvMoney.setText(prizeBeanList.get(i).getAmount());
                        redIdList.add(redId);
                        prizeBeanList.get(i).selected = true;
                        position = i;
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
                rvRedPacket.smoothScrollToPosition(position);
            }
        } else if ("box".equals(from)) {
            //开宝箱提现
            final List<MinePrize.ChildMinePrize> prizeBeanList = bundle.getParcelableArrayList("redPrize");
            if (!EncodeAndStringTool.isListEmpty(prizeBeanList)) {
                int position = 0;
                for (int i = 0; i < prizeBeanList.size(); i++) {
                    if (prizeBeanList.get(i).getId().equals(redId)) {
                        tvMoney.setText(prizeBeanList.get(i).getAmount());
                        redIdList.add(redId);
                        prizeBeanList.get(i).selected = true;
                        position = i;
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
                rvRedPacket.smoothScrollToPosition(position);
            }
        }

        EventBus.getDefault().register(this);//注册Eventbus

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
        bankId = "";
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_rule:
                Intent i = new Intent(mContext, WebH5Activity.class);
                i.putExtra("url", redrules);
                i.putExtra("name", "红包提现");
                startActivity(i);
                break;
            case R.id.btn_enter:
                /**
                 * 针对哪几个红包提现,多个红包id用逗号分隔
                 * 字符串数组
                 */
                if (EncodeAndStringTool.isStringEmpty(bankId)) {
                    ToastUtil.showToast(mContext, "请先选择提现方式");
                } else {
                    String moneyNumber = tvMoney.getText().toString().trim();
                    String[] redsId = new String[redIdList.size()];
                    redIdList.toArray(redsId);
                    final InputWithdrawalbean inputWithdrawalbean = new InputWithdrawalbean(moneyNumber, 2, redsId, bankId);//针对哪几个红包提现,多个红包id用逗号分隔;
                    if (redIdList.size() > 0) {
                        redWithDrawal(inputWithdrawalbean);
                    } else {
                        ToastUtil.showToast(this, "请选择提现红包金额!");
                    }
                }
                break;
            case R.id.btn_contact_our:
                Intent intent1 = new Intent(this, WebH5Activity.class);
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
        CustomLoadingFactory factory = new CustomLoadingFactory();
        LoadingBar.make(llMain, factory).show();

        final String inputbean = JSON.toJSONString(inputWithdrawalbean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest("applyWithdrawal", ApiServiceBean.applyWithdrawal(), new Object[]{body}, this);
    }

    //加载个人信息
    private void loadMineHomeData() {
        RemotingEx.doRequest("getMineHomeData", ApiServiceBean.getMineHomeData(), null, this);
    }

    @Override
    public void onCompleted(String action) {
        if ("applyWithdrawal".equals(action))
            LoadingBar.cancel(llMain);
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
        if ("applyWithdrawal".equals(action)) {
            OutPutWithdraw outPutWithdraw = (OutPutWithdraw) response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(outPutWithdraw)) {
                Intent intent = new Intent(mContext, WithdrawResultActivity.class);
                intent.putExtra("from", "withdraw");
                intent.putExtra("remark", outPutWithdraw.getRemark());
                startActivity(intent);
                finish();
            }
        } else if ("getMineHomeData".equals(action)) {
            MineBean mineBean = (MineBean) response.getDat();
            if (!EncodeAndStringTool.isObjectEmpty(mineBean)) {
                redrules = mineBean.getRedRuleUrl(); //红包规则

                //提现信息
                final List<MineBean.WithdrawBaseBean> withdrawBaseBeanList = mineBean.getWithdrawBase();
                //初始化适配器
                chooseWithdrawInfoAdapter = new ChooseWithdrawInfoAdapter(withdrawBaseBeanList, mContext);
                chooseWithdrawInfoAdapter.setOnItemClickLitsener(new ChooseWithdrawInfoAdapter.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        MineBean.WithdrawBaseBean withdrawBaseBean = withdrawBaseBeanList.get(position);
                        bankId = withdrawBaseBean.getBankId();
                    }
                });
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                linearLayoutManager.setSmoothScrollbarEnabled(true);
                linearLayoutManager.setAutoMeasureEnabled(true);
                rvWithdrawInfo.setLayoutManager(linearLayoutManager);
                //解决数据加载不完的问题
                rvWithdrawInfo.setHasFixedSize(true);
                rvWithdrawInfo.setNestedScrollingEnabled(false);
                rvWithdrawInfo.setAdapter(chooseWithdrawInfoAdapter);

            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        if ("wx_commit_success".equals(messageEvent.getMessage())) {
            //获取个人信息
            loadMineHomeData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }
}
