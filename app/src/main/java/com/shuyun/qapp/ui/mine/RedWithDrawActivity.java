package com.shuyun.qapp.ui.mine;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.shuyun.qapp.bean.MinePrize;
import com.shuyun.qapp.bean.OutPutWithdraw;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.net.AppConst;
import com.shuyun.qapp.ui.homepage.PermissionsActivity;
import com.shuyun.qapp.utils.CustomLoadingFactory;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.PermissionsChecker;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
 * 红包提现
 * recyclerView多个选中状态
 */
public class RedWithDrawActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommonTitle;//标题栏
    @BindView(R.id.et_alipay_account)
    EditText etAlipayAccount;//支付宝账户
    @BindView(R.id.et_name)
    EditText etName;//支付宝姓名
    @BindView(R.id.et_money_number)
    EditText etMoneyNumber;//提现金额
    @BindView(R.id.rv_red_packet)
    RecyclerView rvRedPacket;//红包列表
    @BindView(R.id.btn_immedicate1)
    Button btnImmedicate1;//立即提现按钮
    private static final String TAG = "RedWithDrawActivity";
    List<String> redIdList = new ArrayList<String>();
    @BindView(R.id.wv_red_charge_rules)
    WebView wvRedChargeRules;//红包提现规则


    // 打电话所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CALL_PHONE,

    };
    @BindView(R.id.ll_main)
    LinearLayout llMain;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    private static final int REQUEST_CODE = 0; // 请求码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommonTitle.setText("即时提现红包");
        /**
         * 申请打电话权限
         */
        mPermissionsChecker = new PermissionsChecker(this);
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        }
        wvRedChargeRules.getSettings().setJavaScriptEnabled(true);
        wvRedChargeRules.addJavascriptInterface(new JsInteration(), "android");
        wvRedChargeRules.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        wvRedChargeRules.loadUrl(AppConst.RED_WITHDRAW);
        etMoneyNumber.setInputType(InputType.TYPE_NULL);
        Bundle bundle = getIntent().getExtras();
        final String redId = bundle.getString("redId");

        if (bundle.getString("from").equals("prize")) {
            //我的奖品红包提现
            final List<MinePrize> prizeBeanList = bundle.getParcelableArrayList("redPrize");
            if (!EncodeAndStringTool.isListEmpty(prizeBeanList)) {
                for (MinePrize red : prizeBeanList) {
                    if (red.getId().equals(redId)) {
                        etMoneyNumber.setText(new BigDecimal(red.getAmount()).toString());
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
                        String money = etMoneyNumber.getText().toString();
                        BigDecimal number = new BigDecimal(EncodeAndStringTool.isStringEmpty(money) ? "0.00" : money);
                        if (minePrize.selected) {
                            number = number.add(new BigDecimal(amount));
                            redIdList.add(redId);
                        } else {
                            number = number.subtract(new BigDecimal(amount));
                            redIdList.remove(redId);
                        }
                        etMoneyNumber.setText(number.toString());
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
                        etMoneyNumber.setText(new BigDecimal(red.getAmount()).toString());
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
                        String money = etMoneyNumber.getText().toString();
                        BigDecimal number = new BigDecimal(EncodeAndStringTool.isStringEmpty(money) ? "0.00" : money);
                        if (ChildMinePrize.selected) {
                            number = number.add(new BigDecimal(amount));
                            redIdList.add(redId);
                        } else {
                            number = number.subtract(new BigDecimal(amount));
                            redIdList.remove(redId);
                        }
                        etMoneyNumber.setText(number.toString());
                    }
                });
                LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                rvRedPacket.setLayoutManager(layoutManager);
                rvRedPacket.setAdapter(redPacketAdapter);
            }
        }


    }

    @Override
    public int intiLayout() {
        return R.layout.activity_red_with_draw;
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

    @OnClick({R.id.iv_back, R.id.btn_immedicate1})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                KeyboardUtils.hideSoftInput(RedWithDrawActivity.this);
                break;
            case R.id.btn_immedicate1:
                //立即提现
                final InputWithdrawalbean inputWithdrawalbean = new InputWithdrawalbean();//针对哪几个红包提现,多个红包id用逗号分隔;
                String alipayAccount = etAlipayAccount.getText().toString().trim();
                String name = etName.getText().toString().trim();
                String moneyNumber = etMoneyNumber.getText().toString().trim();
                //针对哪几个红包提现,多个红包id用逗号分隔;
                inputWithdrawalbean.setCardNo(alipayAccount);//支付宝账号
                inputWithdrawalbean.setRealname(name);//支付宝账号名称
                inputWithdrawalbean.setAmount(moneyNumber);//提现金额
                inputWithdrawalbean.setType(2);//1:现金提现;2:红包提现;
                String[] redsId = new String[redIdList.size()];
                redIdList.toArray(redsId);
                /**
                 * 针对哪几个红包提现,多个红包id用逗号分隔
                 * 字符串数组
                 */
                inputWithdrawalbean.setReds(redsId);
                if (redIdList.size() > 0) {
                    if (!EncodeAndStringTool.isStringEmpty(alipayAccount) && !EncodeAndStringTool.isStringEmpty(name) && !EncodeAndStringTool.isStringEmpty(moneyNumber)) {
                        CustomLoadingFactory factory = new CustomLoadingFactory();
                        LoadingBar.make(llMain, factory).show();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                redWithDrawal(inputWithdrawalbean);
                            }
                        }, 2000);

                    } else {
                        ToastUtil.showToast(this, "您没有输入支付宝账户、姓名或提现金额,请重新输入!");
                    }
                }
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
        String inputbean = JSON.toJSONString(inputWithdrawalbean);
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
                        LoadingBar.cancel(llMain);
                        if (dataResponse.isSuccees()) {
                            Intent intent = new Intent(RedWithDrawActivity.this, CashResultActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            ErrorCodeTools.errorCodePrompt(RedWithDrawActivity.this, dataResponse.getErr(), dataResponse.getMsg());
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
        RedWithDrawActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                /**
                 * 调用系统打电话
                 */
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + phoneNum));
                if (ActivityCompat.checkSelfPermission(RedWithDrawActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);
            }
        });
    }

    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this); //统计时长
    }
}
