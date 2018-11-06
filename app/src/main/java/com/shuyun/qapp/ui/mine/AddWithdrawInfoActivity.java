package com.shuyun.qapp.ui.mine;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.shuyun.qapp.base.BaseActivity;
import com.shuyun.qapp.base.BasePresenter;
import com.shuyun.qapp.bean.AddWithdrawResultBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.InputWithdrawalbean;
import com.shuyun.qapp.bean.OutPutWithdraw;
import com.shuyun.qapp.bean.SubmitWithdrawInfoBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.login.LoginActivity;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.MyActivityManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.StringFilterUtil;
import com.shuyun.qapp.utils.ToastUtil;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 新增提现信息
 */
public class AddWithdrawInfoActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R.id.rl_back)
    RelativeLayout rlBack;
    @BindView(R.id.btn_enter)
    Button btnEnter;
    @BindView(R.id.btn_contact_our)
    Button btnContactOur;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;
    @BindView(R.id.iv_clear_name)
    ImageView ivClearName;
    @BindView(R.id.iv_clear_account)
    ImageView ivClearAccount;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_account)
    EditText etAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        rlBack.setOnClickListener(this);
        btnEnter.setOnClickListener(this);
        btnContactOur.setOnClickListener(this);
        ivClearName.setOnClickListener(this);
        ivClearAccount.setOnClickListener(this);

        addListener(etName, ivClearName);//给支付宝绑定姓名EditText设置变化监听事件
        addListener(etAccount, ivClearAccount);//给支付宝账户EditText设置变化监听事件

        try {
            String name = getIntent().getStringExtra("info").trim();
            if (!EncodeAndStringTool.isStringEmpty(name) && name.indexOf("|") != -1) {
                String[] temp = null;
                temp = name.split("[|]");
                if (temp.length >= 2) {
                    etName.setText(temp[1]);
                    etName.setSelection(temp[1].length());
                    etAccount.setText(temp[2]);
                    etAccount.setSelection(temp[2].length());
                }
            }
        } catch (Exception e) {

        }

    }

    @Override
    public int intiLayout() {
        return R.layout.activity_add_withdraw;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                KeyboardUtils.hideSoftInput(AddWithdrawInfoActivity.this);
                break;
            case R.id.iv_clear_name:
                etName.setText("");
                ivClearName.setVisibility(View.GONE);
                break;
            case R.id.iv_clear_account:
                etAccount.setText("");
                ivClearAccount.setVisibility(View.GONE);
                break;
            case R.id.btn_enter:
                String name = etName.getText().toString().trim();
                String account = etAccount.getText().toString().trim();
                if (!EncodeAndStringTool.isStringEmpty(name)) {
                    if (!name.equals(StringFilterUtil.stringFilter(name))) {
                        ToastUtil.showToast(AddWithdrawInfoActivity.this, "请输入正确支付宝姓名");
                    } else {
                        if (!EncodeAndStringTool.isStringEmpty(account)) {
                            if (!account.equals(StringFilterUtil.stringFilter1(account))) {
                                ToastUtil.showToast(AddWithdrawInfoActivity.this, "请输入正确支付宝账号");
                            } else {
                                ShowDialog(name, account);
                            }
                        } else {
                            ToastUtil.showToast(AddWithdrawInfoActivity.this, "请输入支付宝账号");
                        }
                    }
                } else {
                    ToastUtil.showToast(AddWithdrawInfoActivity.this, "请输入姓名");
                }
                break;
            case R.id.btn_contact_our:
                Intent i = new Intent(this, WebBannerActivity.class);
                i.putExtra("url", SaveUserInfo.getInstance(this).getUserInfo("contactUs_url"));
                i.putExtra("name", "联系客服");//名称 标题
                startActivity(i);
                break;
            default:
                break;
        }
    }


    private void ShowDialog(final String name, final String account) {

        new CircleDialog.Builder(this)
                .setTitle("提现信息确认")
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                        params.textSize = 40;
                    }
                })
                .setText("支付宝绑定姓名:" + name + "\n支付宝账号:" + account + "\n请确认信息无误")
                .configText(new ConfigText() {
                    @Override
                    public void onConfig(TextParams params) {
                        params.textSize = 40;
                        params.textColor = Color.parseColor("#666666");
                    }
                })
                .setTextColor(Color.parseColor("#333333"))
                .setWidth(0.7f)
                .setPositive("确定", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                        SubmitWithdrawInfoBean submitWithdrawInfoBean = new SubmitWithdrawInfoBean(1, account, name);
                        AddWithdrawalInfo(submitWithdrawInfoBean);
                    }
                })
                .configPositive(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#333333");
                    }
                })
                .setNegative("取消", new OnMultiClickListener() {
                    @Override
                    public void onMultiClick(View v) {
                    }
                })
                .configNegative(new ConfigButton() {
                    @Override
                    public void onConfig(ButtonParams params) {
                        params.textColor = Color.parseColor("#333333");
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

    //完善提现信息
    private void AddWithdrawalInfo(SubmitWithdrawInfoBean submitWithdrawInfoBean) {
        ApiService apiService = BasePresenter.create(8000);
        final String inputbean = JSON.toJSONString(submitWithdrawInfoBean);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), inputbean);
        apiService.submitWithdrawInfo(body)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<AddWithdrawResultBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AddWithdrawResultBean> listDataResponse) {
                        if (listDataResponse.isSuccees()) {
                            Intent intent = new Intent(AddWithdrawInfoActivity.this, WithdrawResultActivity.class);
                            intent.putExtra("from", "add");
                            startActivity(intent);
                            finish();
                        } else {
                            tvErrorHint.setVisibility(View.VISIBLE);
                            tvErrorHint.setText(listDataResponse.getMsg());
                            ErrorCodeTools.errorCodePrompt(AddWithdrawInfoActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
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
                 * 如果是给支付宝绑定姓名EditText设置变化监听事件,则做下面操作
                 */
                if (editText.equals(etName)) {
                    if (!et.equals(StringFilterUtil.stringFilter(et))) {
                        ToastUtil.showToast(AddWithdrawInfoActivity.this, "请输入正确支付宝姓名");
                    }
                }

                /**
                 * 如果是给支付宝账户EditText设置变化监听事件,则做下面操作
                 */
                if (editText.equals(etAccount)) {
                    if (!et.equals(StringFilterUtil.stringFilter1(et))) {
                        ToastUtil.showToast(AddWithdrawInfoActivity.this, "请输入正确支付宝账号");
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


}
