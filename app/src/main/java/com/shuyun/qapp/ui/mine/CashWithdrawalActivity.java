package com.shuyun.qapp.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.dyhdyh.widget.loading.bar.LoadingBar;
import com.google.gson.Gson;
import com.shuyun.qapp.R;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.base.BaseSwipeBackActivity;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputWithdrawalbean;
import com.shuyun.qapp.bean.OutPutWithdraw;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.PermissionsActivity;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.PermissionsChecker;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 现金提现
 */
public class CashWithdrawalActivity extends BaseSwipeBackActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;
    @BindView(R.id.et_alipay_account)
    EditText etAlipayAccount;//支付宝账号
    @BindView(R.id.iv_clear_account)
    ImageView ivClearAccount;//清空支付宝账户icon
    @BindView(R.id.et_name)
    EditText etName;//支付宝绑定名称
    @BindView(R.id.iv_clear_name)
    ImageView ivClearName;//清空支付宝绑定姓名icon
    @BindView(R.id.et_money_number)
    EditText etMoneyNumber;//提现金额
    @BindView(R.id.iv_clear_money)
    ImageView ivClearMoney;//清空提现金额icon
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;//错误提示
    @BindView(R.id.btn_immedicate)
    Button btnImmedicate;//立即提现
    @BindView(R.id.wv_charge_rules)
    WebView wvChargeRules;//收费规则
    @BindView(R.id.ll_main)
    LinearLayout llMain;


    private InputWithdrawalbean inputWithdrawalbean;
    private static final String TAG = "CashWithdrawalActivity";
    private double money = 0.0;
    private double myCash;
    private String alipayAccount;
    private String name;
    private String moneyNumber;
    private String cash;
    // 打电话所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,

    };
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("现金提现");
        wvChargeRules.getSettings().setJavaScriptEnabled(true);
        wvChargeRules.addJavascriptInterface(new JsInteration(), "android");
        wvChargeRules.loadUrl(AppConst.CASH_WITHDRAW);
        mPermissionsChecker = new PermissionsChecker(this);
        /**
         * 首选项回显支付宝账户和支付宝绑定姓名
         */
        String spAlipayAccount = (String) SharedPrefrenceTool.get(this, "spAlipayAccount", "");
        String spName = (String) SharedPrefrenceTool.get(this, "spName", "");
        if (!EncodeAndStringTool.isStringEmpty(spAlipayAccount)) {
            etAlipayAccount.setText(spAlipayAccount);
        }
        if (!EncodeAndStringTool.isStringEmpty(spName)) {
            etName.setText(spName);
        }
        /**
         * 账户现金金额
         */
        cash = getIntent().getStringExtra("cash");
        etMoneyNumber.setHint("最低提现50元,最高提现" + cash);//默认提示
        /**
         * 将账户现金金额改成浮点类型
         */
        if (!EncodeAndStringTool.isStringEmpty(cash)) {
            myCash = Double.parseDouble(cash);
        } else {
            //如果账户金额为空,按钮设置成不可点击
            btnImmedicate.setEnabled(false);
        }
        addListener(etAlipayAccount, ivClearAccount);//给支付宝账户EditText设置变化监听事件
        addListener(etName, ivClearName);//给支付宝绑定姓名EditText设置变化监听事件
        addListener(etMoneyNumber, ivClearMoney);//给提现金额EditText设置变化监听事件
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_cash_withdrawal;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
        StatService.onResume(this);
        StatService.onResume(this);
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }

    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 关闭页面, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            finish();
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_clear_account, R.id.iv_clear_name, R.id.iv_clear_money, R.id.btn_immedicate})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear_account:
                etAlipayAccount.setText("");
                ivClearAccount.setVisibility(View.GONE);
                break;
            case R.id.iv_clear_name:
                etName.setText("");
                ivClearName.setVisibility(View.GONE);
                break;
            case R.id.iv_clear_money:
                etMoneyNumber.setText("");
                ivClearMoney.setVisibility(View.GONE);
                break;
            case R.id.btn_immedicate:
                alipayAccount = etAlipayAccount.getText().toString().trim();//支付宝账户
                SharedPrefrenceTool.put(CashWithdrawalActivity.this, "spAlipayAccount", alipayAccount);
                name = etName.getText().toString().trim();//支付宝姓名
                SharedPrefrenceTool.put(CashWithdrawalActivity.this, "spName", name);
                String[] reds = new String[]{};//针对哪几个红包提现,多个红包id用逗号分隔
                inputWithdrawalbean = new InputWithdrawalbean(moneyNumber, 1, alipayAccount, name, reds);
                if (myCash >= 50 && money < myCash && !EncodeAndStringTool.isStringEmpty(alipayAccount) && !EncodeAndStringTool.isStringEmpty(name) && !EncodeAndStringTool.isStringEmpty(moneyNumber)) {
                    CustomLoadingFactory factory = new CustomLoadingFactory();
                    LoadingBar.make(llMain, factory).show();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            loadApplyWithdrawal(inputWithdrawalbean);
                        }
                    }, 2000);
                } else {
                    ToastUtil.showToast(CashWithdrawalActivity.this, "您账户余额不足50元或您输入的金额大于账户余额,请您重新输入!");
                }
                //TODO 跳转到体现结果页面
                break;
            default:
                break;
        }
    }

    private void loadApplyWithdrawal(InputWithdrawalbean inputWithdrawalbean) {
        ApiService apiService = BasePresenter.create(8000);
        final String inputbean = new Gson().toJson(inputWithdrawalbean);
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
                        LoadingBar.cancel(llMain);
                        if (listDataResponse.isSuccees()) {
                            OutPutWithdraw outPutWithdraw = listDataResponse.getDat();
                            if (!EncodeAndStringTool.isObjectEmpty(outPutWithdraw)) {
                                startActivity(new Intent(CashWithdrawalActivity.this, CashResultActivity.class));
                                finish();
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(CashWithdrawalActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
                        }

                    }

                    @Override
                    public void onError(Throwable e) {
                        LoadingBar.cancel(llMain);
                        //保存错误信息
                        SaveErrorTxt.writeTxtToFile(e.toString(), SaveErrorTxt.FILE_PATH, TimeUtils.millis2String(System.currentTimeMillis()));

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }


    class JsInteration {

        private static final String TAG = "JsInteration";

        public JsInteration() {
        }

        /**
         * 接收js返回的手机号码,调用打电话业务
         *
         * @param phoneNum
         */
        @JavascriptInterface
        public void OpenTel(String phoneNum) {
            Log.i(TAG, "OpenTel: " + phoneNum);
            //调用打电话业务
            call(phoneNum);
        }
    }

    /**
     * 调用打电话业务
     *
     * @param phoneNum
     */
    private void call(final String phoneNum) {
        CashWithdrawalActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 调用系统打电话
                 */
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                if (ActivityCompat.checkSelfPermission(CashWithdrawalActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
        StatService.onPause(this);
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
                 * 如果是给支付宝账户EditText设置变化监听事件,则做下面操作
                 */
                if (editText.equals(etAlipayAccount)) {
                    alipayAccount = etAlipayAccount.getText().toString().trim();
                    if (EncodeAndStringTool.isStringEmpty(alipayAccount)) {
                        btnImmedicate.setEnabled(false);
                    }
                }
                /**
                 * 如果是给支付宝绑定姓名EditText设置变化监听事件,则做下面操作
                 */
                else if (editText.equals(etName)) {
                    name = etName.getText().toString();
                    if (EncodeAndStringTool.isStringEmpty(name)) {
                        btnImmedicate.setEnabled(false);
                    }
                }
                /**
                 * 如果是给提现金额EditText设置变化监听事件,则做下面操作
                 */
                else if (editText.equals(etMoneyNumber)) {
                    moneyNumber = etMoneyNumber.getText().toString().trim();//提现金额
                    //输入文字中的状态
                    if (!EncodeAndStringTool.isStringEmpty(moneyNumber)) {
                        money = Double.parseDouble(moneyNumber);
                    }
                    if (money < 50 || money > myCash) {//小于50和大于余额 提现按钮不能点击
                        tvErrorHint.setVisibility(View.VISIBLE);
                        tvErrorHint.setText("最低提现金额50元,最高" + cash + "元");//cash
                        btnImmedicate.setEnabled(false);
                    } else {
                        tvErrorHint.setVisibility(View.GONE);
                        btnImmedicate.setEnabled(true);
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
                    if (editText.equals(etMoneyNumber)) {
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