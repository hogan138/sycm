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

import com.blankj.utilcode.util.TimeUtils;
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
import com.shuyun.qapp.bean.AuthNameBean;
import com.shuyun.qapp.bean.DataResponse;
import com.shuyun.qapp.bean.MineBean;
import com.shuyun.qapp.bean.SubmitWithdrawInfoBean;
import com.shuyun.qapp.net.ApiService;
import com.shuyun.qapp.ui.webview.WebBannerActivity;
import com.shuyun.qapp.utils.EncodeAndStringTool;
import com.shuyun.qapp.utils.ErrorCodeTools;
import com.shuyun.qapp.utils.ImageLoaderManager;
import com.shuyun.qapp.utils.OnMultiClickListener;
import com.shuyun.qapp.utils.RegularTool;
import com.shuyun.qapp.utils.SaveErrorTxt;
import com.shuyun.qapp.utils.SaveUserInfo;
import com.shuyun.qapp.utils.SharedPrefrenceTool;
import com.shuyun.qapp.utils.ToastUtil;
import com.tencent.stat.StatConfig;
import com.tencent.stat.StatService;
import com.umeng.analytics.MobclickAgent;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * 实名认证界面
 */
public class RealNameAuthActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.tv_common_title)
    TextView tvCommontitle;//标题
    @BindView(R.id.btn_confirm)
    Button btnConfirm;//确定
    @BindView(R.id.et_realName)
    EditText etRealName;//真实名字
    @BindView(R.id.et_id_card)
    EditText etIdCard;//身份证
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_error_hint)
    TextView tvErrorHint;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.iv_clear_name)
    ImageView ivClearName;
    @BindView(R.id.iv_clear_id)
    ImageView ivClearId;

    String phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        tvCommontitle.setText("实名认证");

        phone = SaveUserInfo.getInstance(this).getUserInfo("phone");
        //电话号码
        tvPhone.setText(phone);

        addListener(etRealName, ivClearName);//给支付宝绑定姓名EditText设置变化监听事件
        addListener(etIdCard, ivClearId);//给支付宝账户EditText设置变化监听事件

        //error_hint
        //1.认证失败  2.实名信息重复绑定   3.姓名和身份证号码不匹配
    }

    @Override
    public int intiLayout() {
        return R.layout.activity_real_name_auth;
    }


    /**
     * 实名认证
     *
     * @param realName 真实名字
     * @param etIdCard 身份证
     */
    private void loadAuthNameData(String realName, String etIdCard) {
        ApiService apiService = BasePresenter.create(8000);
        apiService.realNameAuth(realName, etIdCard)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DataResponse<AuthNameBean>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(DataResponse<AuthNameBean> authNameBeanDataResponse) {
                        if (authNameBeanDataResponse.isSuccees()) {
                            AuthNameBean authNameBean = authNameBeanDataResponse.getDat();
                            //进行芝麻信用认证
                            if (!EncodeAndStringTool.isObjectEmpty(authNameBean)) {

                                //显示查询弹框
//                                ShowDialog();

//                                Intent intent = new Intent(RealNameAuthActivity.this, WebBannerActivity.class);
//                                intent.putExtra("url", "https://openapi.alipay.com/gateway.do?alipay_sdk=alipay-sdk-java-3.3.49.ALL&app_id=2018041902582802&biz_content=%7B%22biz_no%22%3A%22ZM201811023000000454500346282074%22%7D&charset=UTF-8&format=json&method=zhima.customer.certification.certify&sign=aQy3yV9HiiRoZm4I7RXu3SKKgkEYP%2FHdxtf9HGHlhnlVeJjmEW7npaaBCYyyWFpxUNAcu%2BZwzGN8PKEiaTa5B8G1mmKRjGaETYgQwTUA6Ce8brkUnKtkBW%2Fy8KeAKJRJ9d77WR1ncO0N8DC7dFj8JeMF8%2BYCxY4b65CYyq4iNZCbKnr%2F8%2BWE%2BTT%2Fxcb%2BTW4L4Ek1GFsqi%2F0XA8%2Fg0cgNwfkOICoALbPrUlw5r4iqI3aJiE2MidxiSfFcg38iM8HDPUy6bR5tv21Li0w5EA%2FtH09r7iX5Zcv7EhiUzGxnbOU%2BJ4RGEkq9mJOxxzjkUckrJf8ilmNL7WvMq8EVMeO%2FHQ%3D%3D&sign_type=RSA2&timestamp=2018-11-02+14%3A42%3A22&version=1.0");
//                                intent.putExtra("name", "芝麻认证");
//                                startActivity(intent);

//                                Intent intent = new Intent(RealNameAuthActivity.this, AuthResultActivity.class);
//                                intent.putExtra("authName_result", authNameBean);
//                                startActivity(intent);
//                                SaveUserInfo.getInstance(RealNameAuthActivity.this).setUserInfo("cert", String.valueOf(authNameBean.getStatus()));
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(RealNameAuthActivity.this, authNameBeanDataResponse.getErr(), authNameBeanDataResponse.getMsg());
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

    @OnClick({R.id.iv_back, R.id.btn_confirm, R.id.btn_contact_our, R.id.iv_clear_name, R.id.iv_clear_id})
    public void click(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_clear_name:
                ivClearName.setVisibility(View.INVISIBLE);
                etRealName.setText("");
                break;
            case R.id.iv_clear_id:
                ivClearId.setVisibility(View.INVISIBLE);
                etIdCard.setText("");
                break;
            case R.id.btn_confirm:
                String realName = etRealName.getText().toString().trim();
                String idCard = etIdCard.getText().toString().trim();
                if (!EncodeAndStringTool.isStringEmpty(realName)) {
                    if (!EncodeAndStringTool.isStringEmpty(idCard)) {
                        if (RegularTool.isIDCard(idCard)) {
                            loadAuthNameData(realName, idCard);
                        } else {
                            ToastUtil.showToast(this, "请输入正确的身份证号码");
                        }
                    } else {
                        ToastUtil.showToast(this, "请输入身份证号");
                    }
                } else {
                    ToastUtil.showToast(this, "请输入姓名");
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


    private void ShowDialog() {

        new CircleDialog.Builder(this)
                .setTitle("")
                .configTitle(new ConfigTitle() {
                    @Override
                    public void onConfig(TitleParams params) {
                        params.textSize = 40;
                    }
                })
                .setText("")
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

    /**
     * 获取到我的首界面数据
     */
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
                                try {
                                    int cerCount = mineBean.getCertCount();
                                    tvCount.setText("今日认证剩余次数：" + cerCount + "次");
                                    if (cerCount <= 0) {
                                        btnConfirm.setText("今日认证次数已用完");
                                        btnConfirm.setEnabled(false);
                                        btnConfirm.setBackgroundResource(R.drawable.common_btn_bg_10);
                                    } else {
                                        btnConfirm.setText("确认");
                                        btnConfirm.setEnabled(true);
                                        btnConfirm.setBackgroundResource(R.drawable.common_btn_bg_4);
                                    }
                                } catch (Exception e) {

                                }
                            } else {
                            }
                        } else {
                            ErrorCodeTools.errorCodePrompt(RealNameAuthActivity.this, listDataResponse.getErr(), listDataResponse.getMsg());
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

    //在activity或者fragment中添加友盟统计
    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); //统计时长
        StatService.onResume(this);

        //获取个人信息
        loadMineHomeData();
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
                    clearPic.setVisibility(View.INVISIBLE);
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
                    clearPic.setVisibility(View.INVISIBLE);
                } else if (EncodeAndStringTool.isStringEmpty(et) && hasFocus) {
                    clearPic.setVisibility(View.INVISIBLE);
                } else if (EncodeAndStringTool.isStringEmpty(et) && !hasFocus) {
                    clearPic.setVisibility(View.INVISIBLE);
                }

            }
        });
    }

}
