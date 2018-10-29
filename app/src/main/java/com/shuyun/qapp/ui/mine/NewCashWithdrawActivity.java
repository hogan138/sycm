package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.blankj.utilcode.util.TimeUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputWithdrawalbean;
import com.shuyun.qapp.bean.OutPutWithdraw;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.webview.WebPublicActivity;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.ToastUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 现金提现
 */
public class NewCashWithdrawActivity extends BaseActivity implements View.OnClickListener {

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
    private double myCash;
    private String moneyNumber;
    private String alipayAccount;
    private String name;
    private InputWithdrawalbean inputWithdrawalbean;

    private double money = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        rlBack.setOnClickListener(this);
        tvRule.setOnClickListener(this);
        ivAddUserInfo.setOnClickListener(this);
        ivClearMoney.setOnClickListener(this);
        btnEnter.setOnClickListener(this);

        /**
         * 账户现金金额
         */
        cash = getIntent().getStringExtra("cash");
        tvMyMoney.setHint("可提现金额" + cash + "元");//默认提示

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.iv_add_user_info:
                Intent intent = new Intent(NewCashWithdrawActivity.this, AddWithdrawInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_rule:
                Intent i = new Intent(NewCashWithdrawActivity.this, WebPublicActivity.class);
                i.putExtra("name", "cash_rule");
                startActivity(i);
                break;
            case R.id.iv_clear_money:
                tvMoney.setText("");
                ivClearMoney.setVisibility(View.GONE);
                break;
            case R.id.btn_enter:
                alipayAccount = "";//支付宝账户
                name = "";//支付宝姓名
                String[] reds = new String[]{};
                inputWithdrawalbean = new InputWithdrawalbean(moneyNumber, 1, alipayAccount, name, reds);
                if (myCash >= 50
                        && money <= myCash
                        && !EncodeAndStringTool.isStringEmpty(alipayAccount)
                        && !EncodeAndStringTool.isStringEmpty(name)
                        && !EncodeAndStringTool.isStringEmpty(moneyNumber)) {
                    CustomLoadingFactory factory = new CustomLoadingFactory();
                    LoadingBar.make(rlMain, factory).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadApplyWithdrawal(inputWithdrawalbean);
                        }
                    }, 2000);
                } else {
                    ToastUtil.showToast(NewCashWithdrawActivity.this, "您账户余额不足50元或您输入的金额大于账户余额,请您重新输入!");
                }
                break;
            default:
                break;
        }
    }

    private void loadApplyWithdrawal(InputWithdrawalbean inputWithdrawalbean) {
        ApiService apiService = BasePresenter.create(8000);
        final String inputbean = JSON.toJSONString(inputWithdrawalbean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        apiService.applyWithdrawal(body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<OutPutWithdraw>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<OutPutWithdraw> listDataResponse) {
                        LoadingBar.cancel(rlMain);
                        if (listDataResponse.isSuccees()) {
                            OutPutWithdraw outPutWithdraw = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(outPutWithdraw)) {
                                Intent intent = new Intent(NewCashWithdrawActivity.this, WithdrawResultActivity.class);
                                intent.putExtra("content", "withdraw");
                                startActivity(intent);
                                finish();
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(NewCashWithdrawActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingBar.cancel(rlMain);
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));

                    }

                    @Override
                    public void onComplete() {

                    }
                });

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
                if (editText.equals(tvMoney)) {
                    moneyNumber = tvMoney.getText().toString().trim();//提现金额
                    //输入文字中的状态
                    if (!EncodeAndStringTool.isStringEmpty(moneyNumber) && !".".equals(moneyNumber)) {
                        money = Double.parseDouble(moneyNumber);
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
                    //对于金额Editext,如果输入金额不为空,且失去焦点
                    if (editText.equals(tvMoney)) {
                        if (money > myCash) {//输入金额大于账户余额,将金额改成账户余额
                            moneyNumber = cash;
                        }
                    }
                } else if (EncodeAndStringTool.isStringEmpty(et) && hasFocus) {
                    clearPic.setVisibility(View.GONE);
                } else if (EncodeAndStringTool.isStringEmpty(et) && !hasFocus) {
                    clearPic.setVisibility(View.GONE);
                }

            }
        });
    }


}
