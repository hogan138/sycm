package com.shuyun.qapp.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextWatcher;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.KeyboardUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputWithdrawalbean;
import com.shuyun.qapp.bean.MineBean;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 现金提现
 */
public class NewCashWithdrawActivity extends BaseActivity implements View.OnClickListener, OnRemotingCallBackListener<Object> {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.tv_money)
    EditText tvMoney;
    @BindView(R.id.iv_add_user_info)
    ImageView ivAddUserInfo;
    @BindView(R.id.tv_name_account)
    TextView tvNameAccount;
    @BindView(R.id.rl_user_info)
    RelativeLayout rlUserInfo;
    @BindView(R.id.tv_my_money)
    TextView tvMyMoney;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.btn_contact_our)
    Button btnContactOur;
    @BindView(R.id.iv_clear_money)
    ImageView ivClearMoney;
    @BindView(R.id.rl_main)
    RelativeLayout rlMain;

    private String cash;
    private Double myCash; //可提现金额
    private String moneyNumber; //输入的金额
    private Double money = 0.0; //输入的金额转Double
    private InputWithdrawalbean inputWithdrawalbean;

    private String bankId = "";
    private String cashRuls = "";   //现金提现规则
    //实名信息
    String real_info = "";
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mContext = this;

        rlBack.setOnClickListener(this);
        tvRule.setOnClickListener(this);
        ivAddUserInfo.setOnClickListener(this);
        ivClearMoney.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        btnContactOur.setOnClickListener(this);
        rlUserInfo.setOnClickListener(this);

        /**
         * 账户现金金额
         */
        cash = getIntent().getStringExtra("cash");
        tvMyMoney.setText("可提现金额" + cash + "元");//默认提示

        /**
         * 将账户现金金额改成浮点类型
         */
        if (!EncodeAndStringTool.isStringEmpty(cash)) {
            myCash = Double.parseDouble(cash);
        } else {
            //如果账户金额为空,按钮设置成不可点击
            btnEnter.setEnabled(false);
        }
        addListener(tvMoney, ivClearMoney);//给提现金额EditText设置变化监听事件

        SpannableString ss = new SpannableString("最低提现金额为50元");
        // 新建一个属性对象,设置文字的大小
        AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);
        // 附加属性到文本
        ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        // 设置hint
        tvMoney.setHint(new SpannedString(ss)); // 一定要进行转换,否则属性会消失
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_new_cash_withdraw;
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
                KeyboardUtils.hideSoftInput(this);
                break;
            case R.id.iv_add_user_info:
                startActivity(new Intent(mContext, AddWithdrawInfoActivity.class));
                break;
            case R.id.rl_user_info:
                Intent intent1 = new Intent(this, AddWithdrawInfoActivity.class);
                intent1.putExtra("info", real_info);
                startActivity(intent1);
                break;
            case R.id.tv_rule:
                Intent i = new Intent(mContext, WebH5Activity.class);
                i.putExtra("url", cashRuls);
                i.putExtra("name", "现金提现");
                startActivity(i);
                break;
            case R.id.iv_clear_money:
                tvMoney.setText("");
                ivClearMoney.setVisibility(View.GONE);
                btnEnter.setEnabled(false);
                break;
            case R.id.btn_enter:
                if (EncodeAndStringTool.isStringEmpty(bankId)) {
                    ToastUtil.showToast(mContext, "请先完善提现信息");
                } else {
                    String[] reds = new String[]{};
                    inputWithdrawalbean = new InputWithdrawalbean(moneyNumber, 1, reds, bankId);
                    if (myCash >= 50 && money <= myCash) {
                        loadApplyWithdrawal(inputWithdrawalbean);
                    } else {
                        ToastUtil.showToast(mContext, "您账户余额不足50元或您输入的金额大于账户余额,请您重新输入!");
                    }
                }
                break;
            case R.id.btn_contact_our:
                Intent i1 = new Intent(this, WebH5Activity.class);
                i1.putExtra("url", SaveUserInfo.getInstance(this).getUserInfo("contactUs_url"));
                i1.putExtra("name", "联系客服");//名称 标题
                startActivity(i1);
                break;
            default:
                break;
        }
    }

    private void loadApplyWithdrawal(InputWithdrawalbean inputWithdrawalbean) {
        CustomLoadingFactory factory = new CustomLoadingFactory();
        LoadingBar.make(rlMain, factory).show();

        final String inputbean = JSON.toJSONString(inputWithdrawalbean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        RemotingEx.doRequest("applyWithdrawal", ApiServiceBean.applyWithdrawal(), new Object[]{body}, this);
    }

    //加载个人信息
    private void loadMineHomeData() {
        RemotingEx.doRequest("getMineHomeData", ApiServiceBean.getMineHomeData(), null, this);
    }

    /**
     * 给editext设置监听
     *
     * @param editText 需要监听的EditText控件
     * @param clearPic 清空数据的图片
     *                 考虑如何和登录页监听抽取出同样的方法
     */
    protected void addListener(final EditText editText, final ImageView clearPic) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //输入文本之前的状态
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //输入文字后的状态
                String et = editText.getText().toString().trim();
                /**
                 * 0如果输入文字内容不为空,则显示清空editText内容的图标;
                 * 1如果输入文字内容不为空,则隐藏清空editText内容的图标;
                 */
                if (!EncodeAndStringTool.isStringEmpty(et)) {
                    clearPic.setVisibility(View.VISIBLE);
                } else {
                    clearPic.setVisibility(View.GONE);
                }
                /**
                 * 0如果输入文字内容不为空,则显示清空editText内容的图标;
                 * 1如果输入文字内容不为空,则隐藏清空editText内容的图标;
                 */
                if (editText.equals(tvMoney)) {
                    moneyNumber = tvMoney.getText().toString().trim();//提现金额
                    //输入文字中的状态
                    if (!EncodeAndStringTool.isStringEmpty(moneyNumber) && !".".equals(moneyNumber)) {
                        try {
                            money = Double.parseDouble(moneyNumber);
                        } catch (Exception e) {

                        }
                    }

                    if (money > myCash) {//输入金额大于账户余额,将金额改成账户余额
                        moneyNumber = cash;
                        tvMoney.setText(cash);
                        tvMoney.setSelection(cash.length());
                    }

                    if (money < 50 || money > myCash) {//小于50和大于余额 提现按钮不能点击
                        btnEnter.setEnabled(false);
                    } else {
                        btnEnter.setEnabled(true);
                    }
                }

            }
        });
        /**
         * 焦点变化监听
         */
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                /**
                 * 0如果输入文字内容不为空,而且获得焦点,则显示清空editText内容的图标;
                 * 1否则不显示清空editText内容的图标;
                 */
                String et = editText.getText().toString().trim();
                if (!EncodeAndStringTool.isStringEmpty(et) && hasFocus) {//不等于空,且得到焦点
                    clearPic.setVisibility(View.VISIBLE);
                } else if (!EncodeAndStringTool.isStringEmpty(et) && !hasFocus) {//不等于空,且失去焦点
                    clearPic.setVisibility(View.GONE);
                } else if (EncodeAndStringTool.isStringEmpty(et) && hasFocus) {
                    clearPic.setVisibility(View.GONE);
                } else if (EncodeAndStringTool.isStringEmpty(et) && !hasFocus) {
                    clearPic.setVisibility(View.GONE);
                }

            }
        });
    }


    @Override
    public void onCompleted(String action) {
        if ("applyWithdrawal".equals(action))
            LoadingBar.cancel(rlMain);
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
                cashRuls = mineBean.getCashRuleUrl();
                List<MineBean.DatasBean> list = mineBean.getDatas();
                for (int i = 0; i < list.size(); i++) {
                    MineBean.DatasBean datasBean = list.get(i);
                    String type = datasBean.getType();
                    Long status = datasBean.getStatus();
                    if ("withdraw".equals(type) && status == 3) {
                        //是否完善提现信息
                        bankId = datasBean.getBankId();
                        String title = datasBean.getTitle().replaceAll(" ", "");
                        real_info = title;
                        ivAddUserInfo.setVisibility(View.GONE);
                        rlUserInfo.setVisibility(View.VISIBLE);
                        String[] args = title.split("[|]");
                        String str = args[1] + "   " + args[2];
                        tvNameAccount.setText(str);
                        if (datasBean.isEnabled()) {
                            rlUserInfo.setEnabled(true);
                        } else {
                            rlUserInfo.setEnabled(false);
                        }
                        break;
                    } else {
                        ivAddUserInfo.setVisibility(View.VISIBLE);
                        rlUserInfo.setVisibility(View.GONE);
                    }
                }
            }
        }
    }
}
